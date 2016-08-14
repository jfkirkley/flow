package org.androware.flow;


import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.androware.androbeans.JsonObjectReader;
import org.androware.androbeans.LinkObjectReadListener;
import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.StringNameAndAliasComparable;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;


/**
 * Created by jkirkley on 5/6/16.
 */
public class JsonFlowEngine {

    static JsonFlowEngine jsonFlowEngine = null;

    //private TreeMap<StringNameAndAliasComparable, BeanBinder> globalBeanBinderMap = new TreeMap<>();

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
        return jsonFlowEngine;
    }

    public void setCurrFlow(Flow currFlow) {
        this.currFlow = currFlow;
    }

    public Flow loadFlow(String flowName) {
        try {
            JsonObjectReader jsonObjectReader = new JsonObjectReader(ResourceUtils.getResourceInputStream(activity, flowName, "raw"), Flow.class);
            jsonObjectReader.addObjectReadListener(new LinkObjectReadListener());
            this.currFlow = (Flow) jsonObjectReader.read();
        } catch (ObjectReadException e) {
        } catch (IOException e) {
            // TODO handle exeptions properly
        }
        //TestJSON.buildTest(getFlowFileInputStream("test_flow2"), Flow.class);

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

    public void setCurrentFlowContainerActivity(FlowContainerActivity currentFlowContainerActivity) {
        this.currentFlowContainerActivity = currentFlowContainerActivity;
        currFlow = currentFlowContainerActivity.getFlow();
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
