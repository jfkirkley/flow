package org.androware.flow.test_objs;


import org.androware.flow.Step;
import org.androware.flow.StepTransition;
import org.androware.flow.TransitionActor;


/**
 * Created by jkirkley on 7/5/16.
 */

public class TestBindStepTransition implements StepTransition {

    static String testValues [] = {"oh yeah","this is it","I cant quit","you be cool","do it ","blew it","flew ","wief","kwefk","ewoiweoi","klwefkllk"};
    static int index = -1;
    @Override
    public void preTransition(Step step, TransitionActor actor) {
    }

    private int nextIndex() {
        ++index;
        if(index < testValues.length) {
            return index;
        }
        index = 0;
        return index;
    }

    @Override
    public void postTransition(Step step, TransitionActor actor) {
        String objName = TestWrapper.class.getName();
        Object target = step.getFlow().getBoundObject(objName);

        TestWrapper testWrapper = (TestWrapper) target;

        testWrapper.v1.set( testValues[nextIndex()]);
        testWrapper.v2.set( testValues[nextIndex()]);
        testWrapper.v3.set( testValues[nextIndex()]);
        testWrapper.v4.set( testValues[nextIndex()]);
        testWrapper.v5.set( testValues[nextIndex()]);

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
