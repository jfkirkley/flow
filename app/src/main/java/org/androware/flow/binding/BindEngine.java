package org.androware.flow.binding;

import org.androware.androbeans.utils.StringNameAndAliasComparable;
import org.androware.flow.BoundStepFragment;
import org.androware.flow.JsonFlowEngine;
import org.androware.flow.StepFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.R.attr.id;

/**
 * Created by jkirkley on 7/6/16.
 */

public class BindEngine {
    public static final String TAG = "bind";


    static BindEngine theBindEngine = null;

    public static BindEngine inst() {
        if(theBindEngine == null) {
            theBindEngine = new BindEngine();
        }
        return theBindEngine;
    }

    EventCatcher eventCatcher;

    List<TwoWayMapper> twoWayMappers;

    //private TreeMap<StringNameAndAliasComparable, BeanBinder> beanBinderMap = new TreeMap<>();
    private TreeMap<String, BeanBinder> beanBinderMap = new TreeMap<>();

    boolean ignoreEvents = false;

    public BindEngine() {
        twoWayMappers = new ArrayList<>();
        beanBinderMap = new TreeMap<>();
        eventCatcher = new EventCatcher(this);
    }


    public void handleEvent(WidgetEventInfo widgetEventInfo) {
        if(!ignoreEvents) {
            Pivot pivot = widgetEventInfo.pivot;
            Object newValue = widgetEventInfo.newValue;

            // update bean
            updateBean(pivot, newValue, false);

            // handle cascades
            List<Pivot> cascadePivots = pivot.getCascadePivots();

            Object cascadeRetVal = newValue;
            if(cascadePivots != null) {
                for (Pivot p : cascadePivots) {
                    cascadeRetVal = updateBean(p, cascadeRetVal, true);
                }
            }
        }
    }

    public Object updateBean(Pivot pivot, Object newValue, boolean isCascade) {
        BeanBinder beanBinder = getBeanBinder(pivot.beanId);
        Object cascadeRetVal = beanBinder.set(pivot.beanField, newValue, false);

        // broadcast
        if(isCascade) {
            broadcast2Mappers(pivot, cascadeRetVal);
        } else {
            broadcast2Mappers(pivot, newValue);
        }
        // save changes if requested
        beanBinder.persist();
        return cascadeRetVal;
    }

    public void invalidateAll() {
        for(Object k: beanBinderMap.keySet()) {
            BeanBinder beanBinder = beanBinderMap.get(k);
            if(beanBinder.step != null) {
                StepFragment stepFragment = beanBinder.step.getStepFragment();
                if(stepFragment instanceof BoundStepFragment) {
                    ((BoundStepFragment)stepFragment).invalidate();
                }
            }
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

    public void addBeanBinder(BeanBinder beanBinder) {
        beanBinderMap.put(beanBinder.beanId, beanBinder);
        addBeanBinderByAlias(beanBinder);
    }

    public void addBeanBinderByAlias(BeanBinder beanBinder) {
        if(beanBinder.alias != null && beanBinder.alias.length()>0){
            beanBinderMap.put(beanBinder.alias, beanBinder);
        }
    }

    public BeanBinder getBeanBinder(ObjectLoaderSpec objectLoaderSpec) {
        BeanBinder beanBinder = getBeanBinder(objectLoaderSpec.objectId);
        if(beanBinder == null && objectLoaderSpec.alias != null){
            return getBeanBinder(objectLoaderSpec.alias);
        }
        return beanBinder;
    }

    public BeanBinder getBeanBinder(String id) {
        if(id == null) {
            return null;
        }
        BeanBinder beanBinder = beanBinderMap.get(id);

        /*
        if(beanBinder == null) {
            return JsonFlowEngine.inst().getGlobalBeanBinder(id);
        }
        */
        return beanBinder;
    }

    public void removeBeanBinder(ObjectLoaderSpec objectLoaderSpec) {
        if(objectLoaderSpec.objectId != null) {
            removeBeanBinder(objectLoaderSpec.objectId);
        }
        if(objectLoaderSpec.alias != null) {
            removeBeanBinder(objectLoaderSpec.alias);
        }
    }

    public void removeBeanBinder(String id) {
        beanBinderMap.remove(id);
    }

    /*
    public BeanBinder getBeanBinder(String id, String alias) {
        return getBeanBinder(new StringNameAndAliasComparable(id, alias));
    }
*/
    public BeanBinder getBeanBinder(Pivot pivot) {
        return getBeanBinder(pivot.beanId);
    }

    public void addTwoWayMapper(TwoWayMapper twoWayMapper) {
        twoWayMappers.add(twoWayMapper);
    }

    public EventCatcher getEventCatcher() {
        return eventCatcher;
    }

    public boolean hasBeanBinder(ObjectLoaderSpec objectLoaderSpec) {
        return (objectLoaderSpec.objectId != null && beanBinderMap.containsKey(objectLoaderSpec.objectId)) ||
                objectLoaderSpec.alias != null && beanBinderMap.containsKey(objectLoaderSpec.alias);
    }
}