package org.androware.flow;

import android.util.Log;

/**
 * Created by jkirkley on 5/9/16.
 */
public class DefaultStepTransition implements StepTransition {
    public void l(String s) {
        Log.d(Constants.TAG, s);
    }

    @Override
    public void preTransition(Step step, TransitionActor actor) {
        l("Def: pre" );
        l(step.toString());
    }

    @Override
    public void postTransition(Step step, TransitionActor actor) {
        l("Def: post" );
        l(step.toString());
    }

    @Override
    public void init(Step step) {

    }
    @Override
    public void pause(Step step) {
        l("Def: pause" );
    }

    @Override
    public void stop(Step step) {
        l("Def: stop" );
    }
}
