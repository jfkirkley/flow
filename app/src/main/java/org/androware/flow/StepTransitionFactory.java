package org.androware.flow;

import java.util.HashMap;

/**
 * Created by jkirkley on 5/9/16.
 */
public class StepTransitionFactory {
    private static StepTransitionFactory ourInstance = new StepTransitionFactory();

    private HashMap<String, StepTransition> transitionCache = new HashMap<>();

    public static StepTransitionFactory getInstance() {
        return ourInstance;
    }

    private StepTransitionFactory() {
    }

    public StepTransition makeStepTransition(Step step) {
        String stepTransitionClassName = step.transitionClassName;

        if( stepTransitionClassName != null) {
            StepTransition stepTransition = transitionCache.get(stepTransitionClassName);
            if(stepTransition == null) {
                try {
                    stepTransition = (StepTransition)Class.forName(stepTransitionClassName).newInstance();
                    stepTransition.init(step);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            transitionCache.put(stepTransitionClassName, stepTransition);

            return stepTransition;
        }

        return new DefaultStepTransition();
    }
}
