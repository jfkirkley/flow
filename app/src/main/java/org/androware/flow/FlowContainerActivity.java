package org.androware.flow;


import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;


/**
 * Created by jkirkley on 5/8/16.
 */
public class FlowContainerActivity extends FragmentActivity {


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

    public void saveStateData(Map dataMap){

    }

    public void resetStateData(Map dataMap){

    }

    public String getKey() {
        return flow != null? flow.name: Utils.getStringFromExtra(getIntent(), "flowName");
    }

    boolean stateReset = false;

    private void firstInit() {

        if(JsonFlowEngine.inst().validDataForThisFlowActivity(this)) {

            JsonFlowEngine.inst().resetCurrentFlowContainerActivity(this);

            Step lastStep  = stepStack.peek();
            if(lastStep != null && stepsToActivity(lastStep) ) {
                stepStack.pop();  // remove the last step if it goes to an activity (avoid redundant steps)
            }
            stateReset = true;

        } else {
            JsonFlowEngine.inst().setCurrentFlowContainerActivity(this);
            String flowName = Utils.getStringFromExtra(getIntent(), "flowName");
            flow = JsonFlowEngine.inst(this).loadFlow(flowName);
        }
    }

    private void secondInit() {

        //String flowName = Utils.getStringFromExtra(getIntent(), "flowName");

        //if(JsonFlowEngine.inst().validDataForThisFlowActivity(flowName)) {
        if(stateReset) {

            //JsonFlowEngine.inst().resetCurrentFlowContainerActivity(this);
            setContentView(flowLayoutId);
            doPreInit();

            //loadStep(flow.getStartNav());

        } else {

            //JsonFlowEngine.inst().setCurrentFlowContainerActivity(this);
            //flow = JsonFlowEngine.inst().loadFlow(flowName);

            flowLayoutId = ResourceUtils.getResId("layout", flow.layout);
            setContentView(flowLayoutId);

            doPreInit();

            stepFragmentCache = new HashMap<>();
            activityClassStack = new Stack<>();
            stepStack = new Stack<>();

            JsonFlowEngine.inst().setActivityState(this);

            loadStep(flow.getStartNav());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        l("onCreate called");

        firstInit();

        super.onCreate(savedInstanceState);

        secondInit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        l("onRestoreInstanceState called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);

        // Save our own state now
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
        if (step.processor != null) {
            return ReflectionUtils.getClass(step.processor);
        }
        return null;
    }

    public Step getCurrStep() {
        return stepStack.peek();
    }

    public boolean isActivity(Class processorClass) {
        return Activity.class.isAssignableFrom(processorClass);
    }

    public boolean stepsToActivity(Step step) {
        Class processorClass = getProcessorType(step);
        if (processorClass != null) {
            return isActivity(processorClass);
        }
        return false;
    }

    public boolean isFragment(Class processorClass) {
        boolean value = Fragment.class.isAssignableFrom(processorClass);
        l("isfrag:" + value);
        return value;
    }

    public void loadStep(Nav nav) {
        loadStep(nav, null);
    }

    public void loadStep(Nav nav, Object params) {

        l("load step: " + nav.target);
        Step step = flow.getStep(nav);

        if (step != null) {  // step generators may return null to indicate a boundary has been reached

            step.pushParams(params, stepStack);

            Class processorClass = getProcessorType(step);

            if (processorClass == null || isFragment(processorClass)) {
                loadStepFragment(step);

            } else if (isActivity(processorClass)) {
                loadActivity(step, processorClass);
            }
        }
    }

    public Class getLastActivityClass() {
        return activityClassStack.peek();
    }

    public void loadActivity(Step step, Class activityClass) {

        //step.preTransition();
        HashMap bundledData = new HashMap();
        bundledData.put("stepName", step.name);
        bundledData.putAll(step.data);

        // TODO: need to provide place to pop off the last when activity is done
        activityClassStack.push(activityClass);

        // pass all params onto the new activity
        step.addAllParams(bundledData);

        if (FlowContainerActivity.class.isAssignableFrom(activityClass)) {
            JsonFlowEngine.inst(this).startFlow(step.targetFlow, activityClass, bundledData);
        } else {
            Utils.startActivity(bundledData, activityClass, this);
        }
        stepStack.push(step);
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
            fragmentTransaction.commitAllowingStateLoss();
        }
        stepStack.push(step);

        // NOTE:  post transition happens in onCreateView of the StepFragment class

    }

    public Step getPrevStepInSameContainer(String parentContainer) {
        for (Step step : stepStack) {
            if (step.parentContainer.equals(parentContainer)) {
                return step;
            }
        }
        return null;
    }

    public Step popStep() {
        return popStep(getSupportFragmentManager().beginTransaction(), true);
    }

    public Step popStep(FragmentTransaction fragmentTransaction, boolean doCommit) {
        if (stepStack.size() > 1) {
            Step poppedStep = stepStack.pop();
            poppedStep.popParamsToLastEndPoint();

            // Note: activity steps don't load fragments, so the previous step is already loaded
            if (!stepsToActivity(poppedStep)) {

                // reload the previous frag
                Step step = stepStack.peek();

                int containerId = ResourceUtils.getResId("id", step.parentContainer);

                Step prevStep = poppedStep.parentContainer != null && poppedStep.parentContainer.equals(step.parentContainer) ?
                        getPrevStepInSameContainer(step.parentContainer) : null;

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

        // force bindings to update
        flow.getBindEngine().invalidateAll();

        justRestarted = true;

    }

    @Override
    protected void onResume() {
        //l("onResume ======================================== ");
        super.onResume();
        if (justRestarted) {
            JsonFlowEngine.inst().setCurrentFlowContainerActivity(this);
            popStep();
            justRestarted = false;
        }
    }

    @Override
    public void onBackPressed() {
        Step step = popStep();

        if (step == null && !flow.isRoot) {
            super.onBackPressed();
        } else {
            //l("popped step: " + step.toStringTest());
        }

    }

    public void fragmentStepVisible(Step step) {
        l("fragmentStepVisible ======================================== >>>>>>>>>>>>>> :: " + step.getName());

    }

    public Flow getFlow() {
        return flow;
    }

    public Stack<Step> getStepStack() {
        return stepStack;
    }

    public void setStepStack(Stack<Step> stepStack) {
        this.stepStack = stepStack;
    }

    public HashMap<String, StepFragment> getStepFragmentCache() {
        return stepFragmentCache;
    }

    public void setStepFragmentCache(HashMap<String, StepFragment> stepFragmentCache) {
        this.stepFragmentCache = stepFragmentCache;
    }

    public boolean isJustRestarted() {
        return justRestarted;
    }

    public void setJustRestarted(boolean justRestarted) {
        this.justRestarted = justRestarted;
    }

    public int getFlowLayoutId() {
        return flowLayoutId;
    }

    public void setFlowLayoutId(int flowLayoutId) {
        this.flowLayoutId = flowLayoutId;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Stack<Class> getActivityClassStack() {
        return activityClassStack;
    }

    public void setActivityClassStack(Stack<Class> activityClassStack) {
        this.activityClassStack = activityClassStack;
    }

}
