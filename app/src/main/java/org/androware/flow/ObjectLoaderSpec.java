package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.y;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectLoaderSpec {

    public String objectLoaderClassName;
    public String objectClassName;
    public String objectId;

    public Map<String, Object> properties;

    public ObjectLoaderSpec() {
    }

    public ObjectLoaderSpec( String objectLoaderClassName,
                             String objectClassName,
                             String objectId,
                             Map<String, Object> properties ) {

        this.objectLoaderClassName = objectLoaderClassName;
        this.objectClassName = objectClassName;
        this.objectId = objectId;
        this.properties = properties;
    }


    public ObjectLoader buildLoader() {

        return (ObjectLoader) ReflectionUtils.newInstance(objectLoaderClassName);
    }

    public Object buildAndLoad(Step step) {

        ObjectLoader objectLoader = buildLoader();
        return objectLoader.load(this, step);
    }

}
