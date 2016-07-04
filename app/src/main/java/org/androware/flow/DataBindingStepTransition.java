package org.androware.flow;

/**
 * Created by jkirkley on 7/2/16.
 */
public class DataBindingStepTransition implements StepTransition {



    public class Binder {
        String elementId;

        public Binder(TransitionActor transitionActor, String elementId) {

        }

        public void push(Object value) {
        }

        public Object pull() {
            return null;
        }
    }


    @Override
    public void preTransition(Step step, TransitionActor actor) {

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
