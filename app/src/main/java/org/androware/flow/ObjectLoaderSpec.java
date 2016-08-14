package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.value;
import static android.R.attr.y;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectLoaderSpec {
    public final static String ON_FLOW_INIT = "onFlowInit";
    public final static String ON_PRE_PRE_STEP_TRANS = "onPrePreStepTrans";
    public final static String ON_POST_PRE_STEP_TRANS = "onPostPreStepTrans";
    public final static String ON_PRE_POST_STEP_TRANS = "onPrePostStepTrans";
    public final static String ON_POST_POST_STEP_TRANS = "onPostPostStepTrans";
    public final static String ON_DEMAND = "onDemand";

    public String objectLoaderClassName;
    public String objectClassName;
    public String objectId;
    public String alias;

    public String when = ON_FLOW_INIT;

    public Map<String, Object> properties;

    public ObjectLoaderSpec() {
    }

    public ObjectLoaderSpec( String objectLoaderClassName,
                             String objectClassName,
                             String objectId,
                             Map<String, Object> properties,
                             String alias) {

        this.objectLoaderClassName = objectLoaderClassName;
        this.objectClassName = objectClassName;
        this.objectId = objectId;
        this.properties = properties;
        this.alias = alias;
    }


    public ObjectLoader buildLoader() {

        return (ObjectLoader) ReflectionUtils.newInstance(objectLoaderClassName);
    }

    public Object buildAndLoad(Step step) {
        return buildAndLoad(step.getFlow(), step);
    }

    public Object buildAndLoad(Flow flow) {
        return buildAndLoad(flow, null);
    }

    public Object buildAndLoad(Flow flow, Step step) {

        ObjectLoader objectLoader = buildLoader();
        return objectLoader.load(this, flow, step);
    }

    public void addProp(String key, Object value) {
        if(properties == null){
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    public Object getProp(String key) {
        if(properties != null){
            return properties.get(key);
        }
        return null;
    }

    public boolean isWhen(String phase) {
        return phase.equals(when) || phase.equals(ON_DEMAND);
    }
}
