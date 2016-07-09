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
        return ReflectionUtils.getFieldValue(bean, name);
    }

    public void set(String name, Object value) {
        set(name, value, true);
    }

    public void set(String name, Object value, boolean broadcastChange) {
        l("set:  " + name + " = " + value);

        ReflectionUtils.setField(bean.getClass(), name, bean, value);

        if(broadcastChange) {
            step.getFlow().getBindEngine().broadCast2Mappers(new Pivot(beanId + "." + name, null), value);
        }
    }

    public Object getBean() {
        return bean;
    }


}
