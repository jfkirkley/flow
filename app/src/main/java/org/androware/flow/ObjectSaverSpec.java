package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

import java.util.HashMap;

/**
 * Created by jkirkley on 7/4/16.
 */

public class
ObjectSaverSpec {

    public String objectSaverClassName;
    public String objectId;
    public HashMap<String, Object> properties;

    public ObjectSaver buildSaver() {
        return (ObjectSaver) ReflectionUtils.newInstance(objectSaverClassName);
    }

    public void buildAndSave(Object object) {
        ObjectSaver objectPersistor = buildSaver();
        objectPersistor.save(this, object);
    }

    public void addProp(String key, Object value) {
        if(properties == null){
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }
}
