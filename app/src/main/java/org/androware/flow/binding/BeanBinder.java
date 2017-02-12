package org.androware.flow.binding;

import android.util.Log;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ReflectionUtils;

import org.androware.androbeans.utils.StringNameAndAliasComparable;
import org.androware.flow.JsonFlowEngine;
import org.androware.flow.Step;
import org.androware.flow.base.ObjectSaverSpecBase;

import java.util.Map;



/**
 * Created by jkirkley on 7/6/16.
 */

public class BeanBinder {

    public void l(String s) {
        FilterLog.inst().log(BindEngine.TAG, s);
    }

    Object bean;

    String alias;
    String beanId;

    PivotGroup pivotGroup;

    Step step;

    BindEngine bindEngine;


    public BeanBinder(Object b, String id, BindEngine bindEngine) {
        this(b, id, null, bindEngine);
    }


    public BeanBinder(Object b, String id, String alias, BindEngine bindEngine) {
        this(b, id, alias, bindEngine, null);
    }

    public BeanBinder(Object b, String id, BindEngine bindEngine, Step step) {
        this(b, id, null, bindEngine, step);
    }

    public BeanBinder(Object b, String id, String alias, BindEngine bindEngine, Step step) {
        bean = b;
        beanId = id;
        this.bindEngine = bindEngine;
        this.step = step;
        this.alias = alias;

        if(ReflectionUtils.hasMethod(bean.getClass(), "setBeanBinder", this.getClass())) {
            // if bean desires, provide a link back to the binder
            ReflectionUtils.callMethod(bean, "setBeanBinder", this);
        }
    }

    public boolean equals(BeanBinder beanBinder) {
        return beanBinder == null? false: beanBinder.beanId.equals(beanId) || (beanBinder.alias != null && beanBinder.alias.equals(alias));
    }

    public boolean equals(ObjectLoaderSpec objectLoaderSpec) {
        return objectLoaderSpec == null? false: objectLoaderSpec.objectId.equals(beanId) || (objectLoaderSpec.alias != null && objectLoaderSpec.alias.equals(alias));
    }

    public Object get(String name) {
        if(bean instanceof Map) {
            return ((Map)bean).get(name);
        }

        int i = name.indexOf('(');
        if(i != -1) {
            // this is a getter method
            return ReflectionUtils.callMethod(bean, name.substring(0, i));
        } else {
            return ReflectionUtils.getFieldValue(bean, name);
        }
    }

    public Object set(String name, Object value) {
        return set(name, value, true);
    }

    public Object set(String name, Object value, boolean broadcastChange) {
        l("set:  " + name + " = " + value);
        Object retVal = null;
        int i = name.indexOf('(');
        if(i != -1) {
            // this is a setter method
            String methodName = name.substring(0, i);

            if(ReflectionUtils.hasMethod(bean.getClass(), methodName, value.getClass(), Step.class)) {
                retVal = ReflectionUtils.callMethod(bean, methodName, value, step);
            } else {
                retVal = ReflectionUtils.callMethod(bean, methodName, value);
            }

        } else {
            ReflectionUtils.setField(bean.getClass(), name, bean, value);
        }

        if(broadcastChange) {
            bindEngine.broadcast2Mappers(new Pivot(beanId + "." + name, null), value);
            // also broadcast to alias
            bindEngine.broadcast2Mappers(new Pivot(alias + "." + name, null), value);
        }
        return retVal;
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

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }


    public BindEngine getBindEngine() {
        return bindEngine;
    }

    public PivotGroup getPivotGroup() {
        return pivotGroup;
    }

    public void setPivotGroup(PivotGroup pivotGroup) {
        this.pivotGroup = pivotGroup;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public StringNameAndAliasComparable getComparableId() {
        return new StringNameAndAliasComparable(beanId, alias);
    }

    public void persist() {
        Step currStep = step != null? step: JsonFlowEngine.inst().getCurrStep();
        l( "currStep: " + currStep);


        if(currStep != null )
            l( "" + currStep.objectSaverSpec);

        if(currStep != null && currStep.objectSaverSpec != null )
            l( currStep.objectSaverSpec.saveTrigger + " :: " + currStep.objectSaverSpec.objectId);



        if(currStep == null || currStep.objectSaverSpec == null || currStep.objectSaverSpec.objectId == null || currStep.objectSaverSpec.saveTrigger == null) {
            return;
        }
        l( currStep.objectSaverSpec.saveTrigger + " :: " + currStep.objectSaverSpec.objectId);
        if (currStep.objectSaverSpec.saveTrigger.equals(ObjectSaverSpecBase.CHANGE_TRIGGER) &&
                (currStep.objectSaverSpec.objectId.equals(beanId) || currStep.objectSaverSpec.objectId.equals(alias))) {
            l( "save away");
            currStep.getObjectSaverSpec().buildAndSave(bean);

        }
    }
}
