package org.androware.flow;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Stack;


import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;


/**
 * Created by jkirkley on 5/8/16.
 */
public class FlowContainerActivity extends FragmentActivity {

    HashMap<String, Stack<Step>> container2StepStackMap;
    protected Stack<Step> stepStack;
    HashMap<String, StepFragment> stepFragmentCache;
    Stack<Class> activityClassStack;
    int flowLayoutId;

    Flow flow;

    public void l(String s) {
        FilterLog.inst().log(Constants.TAG, s);
    }

    public void doPreInit() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JsonFlowEngine.inst().setCurrentFlowContainerActivity(this);
        String flowName = Utils.getStringFromExtra(getIntent(), "flowName");
        flow = JsonFlowEngine.inst().loadFlow(flowName);

        doPreInit();

        flowLayoutId = ResourceUtils.getResId("layout", flow.layout);
        setContentView(flowLayoutId);

        container2StepStackMap = new HashMap<>();
        stepFragmentCache = new HashMap<>();
        activityClassStack = new Stack<>();
        stepStack = new Stack<>();

        // for the firststep there is no user UI Nav obj, so we fake one
        loadStep(flow.getStartNav());
    }

    public Stack<Step> getStepFragmentStack(Step step) {
        Stack<Step> stepStack = container2StepStackMap.get(step.parentContainer);
        if (stepStack == null) {
            stepStack = new Stack<>();
            container2StepStackMap.put(step.parentContainer, stepStack);
        }
        return stepStack;
    }

    public StepFragment getStepFragment(Step step) {
        StepFragment stepFragment = stepFragmentCache.get(step.getName());
        if (stepFragment == null) {
            stepFragment = step.buildStepFragment();
            stepFragmentCache.put(step.getName(), stepFragment);
        }
        return stepFragment;
    }

    public Class getProcessorType(Step step) {
        if( step.processor != null ) {
            try {
                return Class.forName(step.processor);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public Step getCurrStep() {
        return stepStack.peek();
    }

    public boolean isActivity(Class processorClass) {
        return Activity.class.isAssignableFrom(processorClass);
    }

    public boolean isFragment(Class processorClass) {
        return Fragment.class.isAssignableFrom(processorClass);
    }

    public void loadStep(Nav nav) {
        loadStep(nav, null);
    }

    public void loadStep(Nav nav, Object params) {

        l("load step: " + nav.target);
        Step step = flow.getStep(nav);

        step.pushParams(params, stepStack);

        Class processorClass = getProcessorType(step);

        if(processorClass == null || isFragment(processorClass)) {
            loadStepFragment(step);

        } else if(isActivity(processorClass)) {
            loadActivity(step, processorClass);
        }
    }

    public Class getLastActivityClass() {
        return activityClassStack.peek();
    }

    public void loadActivity(Step step, Class activityClass) {

        //step.preTransition();
        step.data.put("stepName", step.name);

        // TODO: need to provide place to pop off the last when activity is done
        activityClassStack.push(activityClass);

        // pass all params onto the new activity
        step.addAllParams(step.data);

        if(FlowContainerActivity.class.isAssignableFrom(activityClass)) {
            JsonFlowEngine.inst(this).startFlow(step.targetFlow, activityClass, step.data);
        } else {
            Utils.startActivity(step.data, activityClass, this);
        }

        // NOTE:  post transition happens in onCreate of the BaseFlowActivity class
    }

    public void loadStepFragment(Step step) {
        loadStepFragment(step, getSupportFragmentManager().beginTransaction(), true);
    }

    public void loadStepFragment(Step step, FragmentTransaction fragmentTransaction, boolean doCommit) {

        String stepName = step.getName();

        l(step.parentContainer);
        int containerId = ResourceUtils.getResId("id", step.parentContainer);

        ViewGroup parentContainer = (ViewGroup) findViewById(containerId);
        l("child count: " + parentContainer.getChildCount());

        StepFragment stepFragment = getStepFragment(step);

        step.getCurrNav().setAnims(fragmentTransaction);

        step.preTransition(stepFragment);
        // NOTE:  post transition happens in onCreateView of the StepFragment class
        Step lastStep = getPrevStepInSameContainer(step.parentContainer);

        if (lastStep == null) {
            // no previous step in this container
            fragmentTransaction.add(containerId, stepFragment, stepName);

        } else {

            lastStep.pause();

            fragmentTransaction.replace(containerId, stepFragment);
        }

        if (doCommit) {
            fragmentTransaction.commit();
        }
         stepStack.push(step);

        // NOTE:  post transition happens in onCreateView of the StepFragment class

    }
    public Step getPrevStepInSameContainer(String parentContainer){
        for(Step step: stepStack) {
            if(step.parentContainer.equals(parentContainer)) {
                return step;
            }
        }
        return null;
    }

    public Step popStep() {
        return popStep(getSupportFragmentManager().beginTransaction(), true);
    }

    public Step popStep(FragmentTransaction fragmentTransaction, boolean doCommit) {
        if(stepStack.size()>1) {
            Step poppedStep = stepStack.pop();
            Step step = stepStack.peek();

            int containerId = ResourceUtils.getResId("id", step.parentContainer);

            Step prevStep = poppedStep.parentContainer.equals(step.parentContainer) ? getPrevStepInSameContainer(step.parentContainer) : null;

            StepFragment stepFragment = step.getStepFragment();

            step.preTransition(stepFragment);
            // NOTE:  post transition happens in onCreateView of the StepFragment class

            if (prevStep == null) {

                fragmentTransaction.add(containerId, stepFragment, step.getName());

            } else {
                prevStep.pause();
                fragmentTransaction.replace(containerId, stepFragment);
            }
            if (doCommit) {
                fragmentTransaction.commit();
            }
            return poppedStep;
        }
        return null;
    }

    boolean justRestarted = false;
    @Override
    protected void onRestart() {
        //l("onRestart &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ");
        super.onRestart();
        justRestarted = true;

    }

    @Override
    protected void onResume() {
        //l("onResume ======================================== ");
        super.onResume();
        if(justRestarted) {
            JsonFlowEngine.inst().setCurrFlow(flow);
            if (stepStack.size() > 0) {
                Step lastStep = stepStack.pop();
                if (lastStep != null) {
                    loadStepFragment(lastStep);
                }
            }
            justRestarted = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Step step = popStep();

        if(step == null){
            super.onBackPressed();
        } else {
            //l("popped step: " + step.toStringTest());
        }

    }

    public void fragmentStepVisible(Step step) {
        l("fragmentStepVisible ======================================== >>>>>>>>>>>>>> :: " + step.getName());

    }

    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        //l("onAttachFragment ======================================== >>>>>>>>>>>>>> :: " + fragment);
    }


    public Flow getFlow() {
        return flow;
    }

}
