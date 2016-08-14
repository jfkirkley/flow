package org.androware.flow;

import org.androware.flow.binding.BeanBinder;

/**
 * Created by jkirkley on 7/4/16.
 */


/*
TODO:  Need inter-relation system to spawn app templates from UML style specs
 */
public class  CachedObjectLoader implements ObjectLoader {

    public static final String CACHED_OBJECT_NAME = "cachedObjectName";

    public CachedObjectLoader() {}

    @Override
    public Object load(ObjectLoaderSpec spec, Flow flow, Step step) {

        BeanBinder beanBinder = (BeanBinder)step.getFlow().getBoundObject(spec.objectId);

        if(beanBinder != null) {
            Boolean setStep = (Boolean) spec.getProp("setStep");

            if (setStep != null && setStep) {
                beanBinder.setStep(step);
            }
        }

        return beanBinder;
    }
}
