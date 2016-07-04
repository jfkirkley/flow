package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectLoaderSpec {


    public String objectLoaderClassName;
    public HashMap<String, Object> properties;

    public ObjectLoader buildLoader() {
        return (ObjectLoader) ReflectionUtils.newInstance(objectLoaderClassName);
    }

    public Object buildAndLoad(Step step) {
        ObjectLoader objectLoader = buildLoader();
        return objectLoader.load(this, step);
    }

}
