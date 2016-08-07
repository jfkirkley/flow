package org.androware.flow.binding;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ReflectionUtils;

import org.androware.flow.Step;


/**
 * Created by jkirkley on 7/6/16.
 */

public class BeanBinder {

    public void l(String s) {
        FilterLog.inst().log(BindEngine.TAG, s);
    }


    Object bean;

    String beanId;
    Step step;

    public BeanBinder(Object b, String id, Step s) {
        bean = b;
        beanId = id;
        step = s;
    }

    public Object get(String name) {
        int i = name.indexOf('(');
        if(i != -1) {
            // this is a getter method
            return ReflectionUtils.callMethod(bean, name.substring(0, i));
        } else {
            return ReflectionUtils.getFieldValue(bean, name);
        }
    }

    public void set(String name, Object value) {
        set(name, value, true);
    }

    public void set(String name, Object value, boolean broadcastChange) {
        l("set:  " + name + " = " + value);

        int i = name.indexOf('(');
        if(i != -1) {
            // this is a setter method
            ReflectionUtils.callMethod(bean, name.substring(0, i), value);
        } else {
            ReflectionUtils.setField(bean.getClass(), name, bean, value);
        }

        if(broadcastChange) {
            step.getFlow().getBindEngine().broadcast2Mappers(new Pivot(beanId + "." + name, null), value);
        }
    }

    public Object getBean() {
        return bean;
    }

    public String getBeanId() {
        return beanId;
    }

    public void update(Object object) {
        if( bean instanceof Updatable ) {
            ((Updatable)bean).update(object);
        }
    }
}
