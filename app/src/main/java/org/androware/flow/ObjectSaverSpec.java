package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectSaverSpec {

    public String objectSaverClassName;
    public HashMap<String, Object> properties;

    public ObjectSaver buildSaver() {
        return (ObjectSaver) ReflectionUtils.newInstance(objectSaverClassName);
    }

    public void buildAndSave(Object object) {
        ObjectSaver objectPersistor = buildSaver();
        objectPersistor.save(this, object);
    }

}
