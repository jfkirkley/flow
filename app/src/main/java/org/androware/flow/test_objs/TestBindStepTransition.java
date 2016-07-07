package org.androware.flow.test_objs;


import org.androware.flow.Step;
import org.androware.flow.StepTransition;
import org.androware.flow.TransitionActor;
import org.androware.flow.binding.BeanBinder;


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

        Object target = step.getFlow().getBoundObject(step.objectLoaderSpec.objectId);

        BeanBinder testWrapper = (BeanBinder) target;
        if(testWrapper != null) {
            testWrapper.set("v1", testValues[nextIndex()]);
            testWrapper.set("v2", testValues[nextIndex()]);
            testWrapper.set("v3", testValues[nextIndex()]);
            testWrapper.set("v4", testValues[nextIndex()]);
            testWrapper.set("v5", testValues[nextIndex()]);
        }
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
