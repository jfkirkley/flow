package org.androware.flow.binding;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.flow.Flow;
import org.androware.flow.Step;
import org.androware.flow.base.ObjectLoaderSpecBase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectLoaderSpec extends ObjectLoaderSpecBase {

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

    public boolean scopeEndsAtFlow() {
        return scope != null && (scope.equals(FLOW_SCOPE) || scope.equals(STEP_SCOPE));
    }
}
