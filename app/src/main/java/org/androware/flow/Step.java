package org.androware.flow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.ConstructorSpec;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.flow.base.ObjectLoaderSpecBase;
import org.androware.flow.base.StepBase;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.ObjectLoaderSpec;
import org.androware.flow.binding.ObjectSaverSpec;
import org.androware.flow.binding.TwoWayMapper;

import static android.R.attr.name;
import static android.R.attr.value;


/**
 * Created by jkirkley on 5/7/16.
 */
public class Step extends StepBase {

    public static final int MAX_PARAMS = 1024;


    private Flow flow;

    private List<BeanBinder> beanBinderList = null;

    private int previousParamStackEndPoint = -1;

    private static final String[] overwritableProps = {"layout", "processor", "parentContainer", "transitionClassName", "targetFlow", "viewCustomizerSpec"};

    public void overWriteProps(Bundle extras) {
        overWriteProps(extras, false);
    }

    public void overWriteProps(Bundle extras, boolean doInit) {
        for (String prop : overwritableProps) {

            if (ReflectionUtils.getFieldType(getClass(), prop) == String.class) {
                String value = extras.getString(prop);

                value = value == null? extras.getString(name + "_" + prop): value;  // check for step specific props

                if (value != null) {
                    ReflectionUtils.forceSetField(Step.class, prop, this, value);
                }

            } else if (prop.equals("viewCustomizerSpec")) {
                Map map = (Map) extras.getSerializable(prop);
                if (map != null) {
                    // TODO, make this more generic please
                    try {
                        viewCustomizerSpec = (ConstructorSpec) ObjectReaderFactory.getInstance().makeAndRunInitializingMapReader(map, ConstructorSpec.class);
                    } catch (ObjectReadException e) {
                    }
                }
            }
        }
        if (doInit) {
            __init__();
        }
    }

    public String getTargetFlow() {
        Object savedTargetFlow = getParam("targetFlow", true);
        return savedTargetFlow == null? targetFlow: savedTargetFlow.toString();
    }

    public ViewCustomizer getViewCustomizer() {
        return viewCustomizer;
    }

    public void setViewCustomizer(ViewCustomizer viewCustomizer) {
        this.viewCustomizer = viewCustomizer;
    }

    private ViewCustomizer viewCustomizer;

    private Nav currNav;

    private Stack<Object> paramStack = new Stack<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public StepFragment buildStepFragment() {
        stepFragment = StepFragment.newInstance(this);
        return stepFragment;
    }

    public StepFragment getStepFragment() {
        return stepFragment;
    }

    public void setStepFragment(StepFragment stepFragment) {
        this.stepFragment = stepFragment;
    }

    public StepFragment stepFragment;

    public void setStepTransition(StepTransition stepTransition) {
        this.stepTransition = stepTransition;
    }

    public StepTransition stepTransition;

    public Step() {
    }

    public Step(Nav nav) {
        this.currNav = nav;
        this.name = nav.target;
    }

    public void postInit(FlowContainerActivity flowContainerActivity) {
        if (twoWayMapper != null) {
            ((TwoWayMapper)twoWayMapper).setStep(this);
        }
        loadBoundObjects(ObjectLoaderSpecBase.ON_FLOW_INIT);
        overWriteProps(flowContainerActivity.getIntent().getExtras(), true);
    }

    public boolean alreadyInList(ObjectLoaderSpec objectLoaderSpec) {
        for(BeanBinder beanBinder: beanBinderList) {
            if(beanBinder.equals(objectLoaderSpec)) {
                return true;
            }
        }
        return false;
    }

    public void loadBoundObjects(String phase) {
        if(objectLoaderSpecs != null ) {
            if(beanBinderList == null) {
                beanBinderList = new ArrayList<>();
            }
            for(ObjectLoaderSpecBase objectLoaderSpecBase: objectLoaderSpecs) {
                ObjectLoaderSpec objectLoaderSpec = (ObjectLoaderSpec) objectLoaderSpecBase;
                if(objectLoaderSpec.isWhen(phase) ) {
                    if(!flow.hasBoundObject(objectLoaderSpec)) {
                        BeanBinder beanBinder = (BeanBinder) objectLoaderSpec.buildAndLoad(flow, this);
                        flow.addBoundObject(beanBinder);
                        beanBinderList.add(beanBinder);
                    } else {
                        if(!alreadyInList(objectLoaderSpec)) {
                            beanBinderList.add(flow.getBoundObject(objectLoaderSpec));
                        }
                    }
                }
            }
        }
    }

    public void clearFlowScopeBoundObjects() {
        if(objectLoaderSpecs != null ) {
            for(ObjectLoaderSpecBase objectLoaderSpecBase: objectLoaderSpecs) {
                ObjectLoaderSpec objectLoaderSpec = (ObjectLoaderSpec) objectLoaderSpecBase;
                if (objectLoaderSpec.scopeEndsAtFlow()) {
                    flow.removeBoundObject(objectLoaderSpec);
                }
            }
        }
    }


    public void __init__() {
        if (viewCustomizerSpec != null) {
            viewCustomizer = (ViewCustomizer) viewCustomizerSpec.build();
        }
    }


    public void customizeView(Activity activity, View view) {
        // customize(Activity activity, InstaBean spec, View page, Object config);
        if (viewCustomizer != null) {
            viewCustomizer.customize(activity, this, view, ui);
        }
    }


    public void setNavHandlers(View view) {
        setNavHandlers(view, JsonFlowEngine.inst().getCurrentFlowContainerActivity());
    }

    public void setNavHandlers(View view, FlowContainerActivity activity) {

        if (navMap != null) {
            for (String targetKey : navMap.keySet()) {
                Nav nav = (Nav)navMap.get(targetKey);
                nav.setNavHandler(view, activity, targetKey);
            }
        }
    }

    public void preTransition(TransitionActor actor) {
        loadBoundObjects(ObjectLoaderSpecBase.ON_PRE_PRE_STEP_TRANS);
        stepTransition.preTransition(this, actor);
        loadBoundObjects(ObjectLoaderSpecBase.ON_POST_PRE_STEP_TRANS);
    }

    public void postTransition(TransitionActor actor) {
        loadBoundObjects(ObjectLoaderSpecBase.ON_PRE_POST_STEP_TRANS);
        stepTransition.postTransition(this, actor);
        loadBoundObjects(ObjectLoaderSpecBase.ON_POST_POST_STEP_TRANS);
    }

    public void stop() {
        stepTransition.stop(this);
    }

    public void pause() {
        stepTransition.pause(this);
    }

    public Object getParams() {
        return getParams(0);
    }

    public Object getParams(int numStepsBack) {
        if (numStepsBack < paramStack.size()) {
            return paramStack.get(paramStack.size() - numStepsBack - 1);
        }
        return null;
    }

    public Object getParam(String key, int numStepsBack) {
        Map params = (Map) getParams(numStepsBack);
        if (params != null) {
            return params.get(key);
        }
        return null;
    }

    public Object getParam(String key, boolean fullDepthSearch) {

        if(fullDepthSearch) {
            Object p = null;
            int i = 0;
            while (p == null && i < paramStack.size()) {
                p = getParam(key, i++);
            }
            return p;
        }
        return getParam(key, 0);
    }

    public List popParamsToLastEndPoint() {
        List list = new ArrayList();
        if (previousParamStackEndPoint >= 0) {
            while (paramStack.size() > previousParamStackEndPoint) {
                list.add(paramStack.pop());
            }
        }
        return list;
    }

    public void pushParams(Object params, Stack<Step> stepStack) {
        if (paramStack.size() > MAX_PARAMS) {

            Log.w(Constants.TAG, "Warning:   param stack size exceeds max size (" + MAX_PARAMS + ") for step " + name);

        } else {

            previousParamStackEndPoint = paramStack.size();
            if (stepStack != null && stepStack.size() > 1) {
                paramStack.addAll(stepStack.get(stepStack.size() - 1).getParamStack());
            }
            this.paramStack.push(params);
        }
    }

    public Map addAllParams(Map map) {
        for (Object object : paramStack) {
            if (object instanceof Map) {
                Map m = (Map) object;
                for (Object k : m.keySet()) {
                    map.put(k, m.get(k));
                }
            }
        }
        return map;
    }

    public Stack<Object> getParamStack() {
        return paramStack;
    }

    public void setParamStack(Stack<Object> paramStack) {
        this.paramStack = paramStack;
    }

    public Nav getCurrNav() {
        return currNav;
    }

    public void setCurrNav(Nav currNav) {
        this.currNav = currNav;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public List<BeanBinder> getBeanBinderList() {
        if(beanBinderList == null) {
            loadBoundObjects(ObjectLoaderSpecBase.ON_DEMAND);
        }
        return beanBinderList;
    }

    public BeanBinder getBeanBinder(String id) {
        return flow.getBoundObject(id);
    }

    public TwoWayMapper getTwoWayMapper() {
        return (TwoWayMapper)twoWayMapper;
    }

    public ObjectSaverSpec getObjectSaverSpec() {
        return (ObjectSaverSpec)objectSaverSpec;
    }

}

