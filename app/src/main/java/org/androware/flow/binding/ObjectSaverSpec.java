package org.androware.flow.binding;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.flow.base.ObjectSaverSpecBase;

import java.util.HashMap;

/**
 * Created by jkirkley on 7/4/16.
 */

public class ObjectSaverSpec extends ObjectSaverSpecBase {

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
