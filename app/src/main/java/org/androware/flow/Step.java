package org.androware.flow;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


import org.androware.androbeans.MapObjectReader;
import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.legacy.InstaBean;
import org.androware.androbeans.legacy.JSONinstaBean;
import org.androware.androbeans.utils.ConstructorSpec;
import org.androware.androbeans.utils.ReflectionUtils;


/**
 * Created by jkirkley on 5/7/16.
 */
public class Step {

    public static final int MAX_PARAMS = 1024;

    public String layout;
    public String processor;
    public String parentContainer;
    public String transitionClassName;

    public String targetFlow;
    public UI ui;

    private static final String[] overwritableProps = {"layout", "processor", "parentContainer", "transitionClassName", "targetFlow", "viewCustomizerSpec"};

    public void overWriteProps(Bundle extras) {
        overWriteProps(extras, false);
    }

    public void overWriteProps(Bundle extras, boolean doInit) {
        for (String prop : overwritableProps) {

            if(ReflectionUtils.getFieldType(getClass(), prop) == String.class) {
                String value = extras.getString(prop);
                if (value != null) {
                    ReflectionUtils.forceSetField(Step.class, prop, this, value);
                }
            } else if (prop.equals("viewCustomizerSpec")) {
                Map map = (Map)extras.getSerializable(prop);
                // TODO, make this more generic please
                try {
                    ReflectionUtils.forceSetField(Step.class, prop, this, (new MapObjectReader(map, ConstructorSpec.class)).read());
                } catch (ObjectReadException e) {
                }
            }
        }
        if(doInit){
            __init__();
        }
    }

    public ConstructorSpec viewCustomizerSpec;

    public ViewCustomizer getViewCustomizer() {
        return viewCustomizer;
    }

    public void setViewCustomizer(ViewCustomizer viewCustomizer) {
        this.viewCustomizer = viewCustomizer;
    }

    private ViewCustomizer viewCustomizer;

    private Nav currNav;

    public HashMap<String, String> meta;
    public HashMap<String, String> data;

    public HashMap<String, Nav> navMap;

    private Stack<Object> paramStack = new Stack<>();

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public StepFragment buildStepFragment() {
        stepFragment = StepFragment.newInstance(name);
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

        for (String targetKey : navMap.keySet()) {
            Nav nav = navMap.get(targetKey);
            nav.setNavHandler(view, activity, targetKey);
        }
    }

    public void preTransition(Object actor) {
        stepTransition.preTransition(this, actor);
    }

    public void postTransition(Object actor) {
        stepTransition.postTransition(this, actor);
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

    public Object getParam(String key) {
        return getParam(key, 0);
    }

    public void pushParams(Object params, Stack<Step> stepStack) {
        if (paramStack.size() > MAX_PARAMS) {

            Log.w(Constants.TAG, "Warning:   param stack size exceeds max size (" + MAX_PARAMS + ") for step " + name);

        } else {

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
                    if (!map.containsKey(k)) {
                        map.put(k, m.get(k));
                    }
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

}

