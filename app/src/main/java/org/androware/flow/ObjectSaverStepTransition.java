package org.androware.flow;

import org.androware.flow.binding.BeanBinder;

import static org.androware.flow.CachedObjectLoader.CACHED_OBJECT_NAME;

/**
 * Created by jkirkley on 7/5/16.
 */

public class ObjectSaverStepTransition implements StepTransition {
    @Override
    public void preTransition(Step step, TransitionActor actor) {
        BeanBinder beanBinder = (BeanBinder)step.getFlow().getBoundObject(step.objectSaverSpec.objectId);
        step.getObjectSaverSpec().buildAndSave(beanBinder.getBean());
    }

    @Override
    public void postTransition(Step step, TransitionActor actor) {

    }

    @Override
    public void pause(Step step) {

    }

    @Override
    public void stop(Step step) {

    }

    @Override
    public void init(Step step) {

    }
}
