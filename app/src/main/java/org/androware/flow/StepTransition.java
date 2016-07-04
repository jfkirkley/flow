package org.androware.flow;

/**
 * Created by jkirkley on 5/9/16.
 */
public interface StepTransition {

    public void preTransition(Step step, TransitionActor actor);
    public void postTransition(Step step, TransitionActor actor);
    public void pause(Step step);
    public void stop(Step step);
    public void init(Step step);

}
