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
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.StringNameAndAliasComparable;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;

import static android.R.attr.name;


/**
 * Created by jkirkley on 5/6/16.
 */
public class JsonFlowEngine {

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

        public boolean sameActivity(String flowName) {
            return flow.name.equals(flowName);
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
            Log.d("flow", "flowName: " + flowName);
            this.currFlow.name = flowName;
        } catch (ObjectReadException e) {
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

    public void startFlow(String flowName, Class theFlowContainerActivityClass, HashMap map) {
        map.put("flowName", flowName);
        Utils.startActivity(theFlowContainerActivityClass,  map, activity);
    }

    public void startFlow(String flowName) {
        startFlow(flowName, FlowContainerActivity.class,  new HashMap());
    }

    public FlowContainerActivity getCurrentFlowContainerActivity() {
        return currentFlowContainerActivity;
    }

    public boolean isSavedActivity(String flowName) {
        return flowName2StateMap.containsKey(flowName);
    }

    public void resetCurrentFlowContainerActivity(FlowContainerActivity currentFlowContainerActivity, String flowName) {
        FlowActivityState flowActivityState = flowName2StateMap.get(flowName);
        flowActivityState.setData(currentFlowContainerActivity);
        this.currentFlowContainerActivity = currentFlowContainerActivity;
        this.currFlow = flowActivityState.flow;
    }

    public void setCurrentFlowContainerActivity(FlowContainerActivity currentFlowContainerActivity) {
        this.currentFlowContainerActivity = currentFlowContainerActivity;
        currFlow = currentFlowContainerActivity.getFlow();

        if(currFlow != null && !flowName2StateMap.containsKey(currFlow.name)) {
            flowName2StateMap.put(currFlow.name, new FlowActivityState(currentFlowContainerActivity));
        }

    }

    public void setActivityState(FlowContainerActivity currentFlowContainerActivity) {
        if(!flowName2StateMap.containsKey(currFlow.name)) {
            flowName2StateMap.put(currFlow.name, new FlowActivityState(currentFlowContainerActivity));
        }
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
