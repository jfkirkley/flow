package org.androware.flow;

import android.databinding.ObservableArrayMap;

import org.androware.androbeans.utils.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by jkirkley on 7/2/16.
 */

public class ObjectBindingObeservableArrayMap extends ObservableArrayMap {

    Object target;

    public ObjectBindingObeservableArrayMap(Object t) {
        super();
        target = t;
        Field fields [] = t.getClass().getFields();

        for(Field field: fields){
            try {
                put(field.getName(), field.get(t) );
            } catch (IllegalAccessException e) {
            }
        }
    }

    @Override
    public Object put(Object o, Object o2) {
        ReflectionUtils.setField(target.getClass(), (String)o, target, o2);
        return super.put(o, o2);
    }
}
