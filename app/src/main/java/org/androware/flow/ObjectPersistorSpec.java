package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectPersistorSpec {

    public String objectPersistorClassName;
    public HashMap<String, Object> properties;

    public ObjectPersistor buildPersistor() {
        return (ObjectPersistor) ReflectionUtils.newInstance(objectPersistorClassName);
    }

    public void buildAndPersist(Object object) {
        ObjectPersistor objectPersistor = buildPersistor();
        objectPersistor.persist(this, object);
    }

}
