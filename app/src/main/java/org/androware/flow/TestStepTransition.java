package org.androware.flow;

import android.util.Log;

/**
 * Created by jkirkley on 5/9/16.
 */
public class TestStepTransition implements StepTransition {
    public void l(String s) {
        Log.d(Constants.TAG, s);
    }

    @Override
    public void preTransition(Step step, Object actor) {
        l("TEST: " + step.toString());
    }

    @Override
    public void postTransition(Step step, Object actor) {
        l("TEST: " + step.toString());
    }

    @Override
    public void pause(Step step) {
        l("TEST: pause" );
    }

    @Override
    public void stop(Step step) {
        l("TEST: stop" );
    }

    @Override
    public void init(Step step) {
        l("TEST: init" );
    }
}
