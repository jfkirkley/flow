package org.androware.flow.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jkirkley on 7/6/16.
 */

public class BindEngine {
    public static final String TAG = "bind";

    List<TwoWayMapper> twoWayMappers;
    Map<String, BeanBinder> beanBinderMap;
    boolean ignoreEvents = false;

    public BindEngine() {
        twoWayMappers = new ArrayList<>();
        beanBinderMap = new HashMap<>();
    }


    public void handleEvent(WidgetEventInfo widgetEventInfo) {
        if(!ignoreEvents) {
            Pivot pivot = widgetEventInfo.pivot;
            Object newValue = widgetEventInfo.newValue;

            // update bean
            BeanBinder beanBinder = beanBinderMap.get(pivot.beanId);
            beanBinder.set(pivot.beanField, newValue, false);

            // broadcast
            broadcast2Mappers(pivot, newValue);
        }
    }

    public void broadcast2Mappers(Pivot pivot, Object newValue) {
        ignoreEvents = true;   // suppress events sent as a result of mapping values (infinite loop)

        // broadcast change to mappers
        for (TwoWayMapper twoWayMapper : twoWayMappers) {
            twoWayMapper.update(pivot, newValue);
        }

        ignoreEvents = false;
    }

    public void addBeanBinder(String beanId, BeanBinder beanBinder) {
        beanBinderMap.put(beanId, beanBinder);
    }

    public void addTwoWayMapper(TwoWayMapper twoWayMapper) {
        twoWayMappers.add(twoWayMapper);
    }
}