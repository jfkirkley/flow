package org.androware.flow;


import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.androware.androbeans.JsonObjectReader;
import org.androware.androbeans.LinkObjectReadListener;
import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.StringNameAndAliasComparable;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;

import static android.R.attr.name;


/**
 * Created by jkirkley on 5/6/16.
 */
/*
TODO:
- need multiple object savers
- implement saveTrigger for transition and flowEnd


 */
public class JsonFlowEngine {

    public static void l(String t) {
        FilterLog.inst().log("flow", t);
    }

    public static class FlowActivityState {
        protected Stack<Step> stepStack;
        HashMap<String, StepFragment> stepFragmentCache;
        Stack<Class> activityClassStack;
        int flowLayoutId;

        Flow flow;

        Map otherStateData = new HashMap();

        public FlowActivityState (FlowContainerActivity flowContainerActivity) {
            stepStack = flowContainerActivity.getStepStack();
            stepFragmentCache = flowContainerActivity.getStepFragmentCache();
            activityClassStack = flowContainerActivity.getActivityClassStack();
            flowLayoutId = flowContainerActivity.getFlowLayoutId();
            flow = flowContainerActivity.getFlow();

            flowContainerActivity.saveStateData(otherStateData);
        }

        public boolean isValid() {
            return flow != null;
        }

        public void setData(FlowContainerActivity flowContainerActivity) {
            flowContainerActivity.setFlow(flow);
            flowContainerActivity.setStepStack(stepStack);
            flowContainerActivity.setStepFragmentCache(stepFragmentCache);
            flowContainerActivity.setActivityClassStack(activityClassStack);
            flowContainerActivity.setFlowLayoutId(flowLayoutId);

            flowContainerActivity.resetStateData(otherStateData);

        }

    }

    //private FlowActivityState flowActivityState = null;

    private Map<String, FlowActivityState> flowName2StateMap = new HashMap<>();

    static JsonFlowEngine jsonFlowEngine = null;


    private TreeMap<String, BeanBinder> globalBeanBinderMap = new TreeMap<>();

    private FlowContainerActivity currentFlowContainerActivity;

    public Flow getCurrFlow() {
        return currFlow;
    }

    public Step getCurrStep() {
        return currFlow != null? currFlow.getCurrStep(): null;
    }

    Flow currFlow;

    Activity activity;

    public JsonFlowEngine(Activity activity) {
        this.activity = activity;
    }

    public static JsonFlowEngine inst() {
        return inst(null);
    }

    public static JsonFlowEngine inst(Activity activity) {
        if(jsonFlowEngine == null) {
            jsonFlowEngine = new JsonFlowEngine(activity);
        }
        else if(activity != null) {
            jsonFlowEngine.activity = activity;
        }
        return jsonFlowEngine;
    }

    public Flow loadFlow(String flowName) {

        try {
            this.currFlow = (Flow) ObjectReaderFactory.getInstance(activity).makeAndRunLinkedJsonReader(flowName, Flow.class);
            l( "flowName: " + flowName);
            this.currFlow.name = flowName;
        } catch (ObjectReadException e) {
            e.printStackTrace();
            // TODO handle exeptions properly
        }

        return this.currFlow;
    }

    protected InputStream getFlowFileInputStream(String flowName) {
        return ResourceUtils.getResourceInputStream(activity, flowName, "raw");
    }

    public Step getFlowStep(String stepName) {
        return this.currFlow.getStep(stepName);
    }

    public void startFlow(String flowName, Class theFlowContainerActivityClass, HashMap map, boolean forceRestart) {
        map.put("flowName", flowName);
        if(forceRestart) {
            map.put("forceFullRestart", "yes");
        }
        Utils.startActivity(theFlowContainerActivityClass,  map, activity);
    }

    public void startFlow(String flowName) {
        startFlow(flowName, false);
    }

    public void startFlow(String flowName, boolean forceRestart) {
        startFlow(flowName, FlowContainerActivity.class,  new HashMap(), forceRestart);
    }

    public FlowContainerActivity getCurrentFlowContainerActivity() {
        return currentFlowContainerActivity;
    }

    public boolean validDataForThisFlowActivity(String flowName) {
        return flowName2StateMap.containsKey(flowName);
    }

    public boolean validDataForThisFlowActivity(FlowContainerActivity flowContainerActivity) {
        FlowActivityState flowActivityState = flowName2StateMap.get(flowContainerActivity.getKey());
        return flowActivityState != null && flowActivityState.isValid();
    }

    public void resetCurrentFlowContainerActivity(FlowContainerActivity currentFlowContainerActivity) {
        FlowActivityState flowActivityState = flowName2StateMap.get(currentFlowContainerActivity.getKey());
        flowActivityState.setData(currentFlowContainerActivity);
        this.currentFlowContainerActivity = currentFlowContainerActivity;
        this.currFlow = flowActivityState.flow;
    }

    public void setCurrentFlowContainerActivity(FlowContainerActivity currentFlowContainerActivity) {
        this.currentFlowContainerActivity = currentFlowContainerActivity;
        currFlow = currentFlowContainerActivity.getFlow();

        if(currFlow != null) {
            setActivityState(currentFlowContainerActivity);
        }
        /*
        if(currFlow != null && !flowName2StateMap.containsKey(currFlow.name)) {
            flowName2StateMap.put(currFlow.name, new FlowActivityState(currentFlowContainerActivity));
        }
        */
    }

    public void setActivityState(FlowContainerActivity currentFlowContainerActivity) {
        if(!flowName2StateMap.containsKey(currentFlowContainerActivity.getKey())) {
            flowName2StateMap.put(currentFlowContainerActivity.getKey(), new FlowActivityState(currentFlowContainerActivity));
        }
    }

    public void removeActivityState(FlowContainerActivity flowContainerActivity) {

        flowName2StateMap.remove(flowContainerActivity.getKey());
    }

    public void resumeCurrentFlow() {
        Utils.raiseToForegroundActivity(currentFlowContainerActivity.getClass(), this.activity);
    }

    public void addGlobalBeanBinder(BeanBinder beanBinder) {
        globalBeanBinderMap.put(beanBinder.getBeanId(), beanBinder);
        addGlobalBeanBinderByAlias(beanBinder);
    }

    public void addGlobalBeanBinderByAlias(BeanBinder beanBinder) {
        if(beanBinder.getAlias() != null){
            globalBeanBinderMap.put(beanBinder.getAlias(), beanBinder);
        }
    }

    public BeanBinder getGlobalBeanBinder(String beanBinderId) {
        return globalBeanBinderMap.get(beanBinderId);
    }
/*
    public BeanBinder getGlobalBeanBinder(String beanBinderId) {
        return getGlobalBeanBinder(new StringNameAndAliasComparable(beanBinderId));
    }

    public BeanBinder getGlobalBeanBinder(String beanBinderId, String alias) {
        return getGlobalBeanBinder(new StringNameAndAliasComparable(beanBinderId, alias));
    }
*/
}
