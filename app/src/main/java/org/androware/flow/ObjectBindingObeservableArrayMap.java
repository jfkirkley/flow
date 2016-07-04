package org.androware.flow;

import android.databinding.ObservableArrayMap;

import java.lang.reflect.Field;

/**
 * Created by jkirkley on 7/2/16.
 */

public class ObjectBindingObeservableArrayMap extends ObservableArrayMap {

    public ObjectBindingObeservableArrayMap(Object t) {
        super();
        Field fields [] = t.getClass().getFields();

        for(Field field: fields){
            try {
                put(field.getName(), field.get(t) );
            } catch (IllegalAccessException e) {
            }
        }
    }
}
