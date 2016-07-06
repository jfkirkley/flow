package org.androware.flow;

import static org.androware.flow.CachedObjectLoader.CACHED_OBJECT_NAME;

/**
 * Created by jkirkley on 7/5/16.
 */

public class ObjectSaverStepTransition implements StepTransition {
    @Override
    public void preTransition(Step step, TransitionActor actor) {
        if (step.objectSaverSpec.properties.containsKey(CACHED_OBJECT_NAME)) {
            String objName = (String)step.objectSaverSpec.properties.get(CACHED_OBJECT_NAME);
            Object target = step.getFlow().getBoundObject(objName);
            step.objectSaverSpec.buildAndSave(target);
        }
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
