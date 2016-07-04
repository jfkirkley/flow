package org.androware.flow;

import android.databinding.ObservableArrayMap;

import java.lang.reflect.Field;

/**
 * Created by jkirkley on 7/3/16.
 */

public class ObervableArrayMapFactory {

    public static ObservableArrayMap build(Object object) {
        ObservableArrayMap<String, Object> objectObservableArrayMap = new ObservableArrayMap<>();
        Field fields [] = object.getClass().getFields();

        for(Field field: fields){
            try {
                objectObservableArrayMap.put(field.getName(), field.get(object) );
            } catch (IllegalAccessException e) {
            }
        }

        return objectObservableArrayMap;
    }

}
