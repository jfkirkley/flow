package org.androware.flow;

import android.util.Log;
import java.util.HashMap;
import org.androware.androbeans.utils.ConstructorSpec;


/**
 * Created by jkirkley on 5/7/16.
 */
public class Flow  {

    public String fragmentContainer;
    public HashMap<String, Step> steps;
    public String layout;
    public String processor;

    public ConstructorSpec stepGeneratorSpec;

    public StepGenerator stepGenerator;

    public Nav startNav;    // navigates to the first step

    public Step generateStep(Nav nav) {

        if(nav.target == null /* default is next */
                || nav.target.equals(Nav.GEN_NEXT)) {
            return stepGenerator.next();
        } else if(nav.target.equals(Nav.GEN_PREV)) {
            return stepGenerator.prev();
        }
        try {
            int index = Integer.parseInt(nav.target);
            return stepGenerator.getStep(index);
        } catch( NumberFormatException e) {

        }
        return stepGenerator.getStep(nav.target);
    }


    public Step getFirstStep() {
        return getStep(startNav);
    }

    public Step getStep(String name) {
        return steps.get(name);
    }

    public Step getStep(Nav nav) {
        Step step = null;

        if(nav.usesStepGenerator()) {
            step = generateStep(nav);
            steps.put(step.getName(), step);
        } else {
            step = steps.get(nav.target);
            step.setName(nav.target);
        }
        step.setCurrNav(nav);
        return step;
    }


    public void l(String s) {
        Log.d(Constants.TAG, s);
    }


    public Flow() {
    }

    public Nav getStartNav() {
        return startNav;
    }


    public void __init__() {
        for(String k: steps.keySet()) {
            Step step = steps.get(k);
            step.setStepTransition(StepTransitionFactory.getInstance().makeStepTransition(step));
        }

        if (stepGeneratorSpec != null) {

            // TODO,  may need multiple step generators, these can be referred
            // from navs and may be parameterized from there
            stepGenerator = (StepGenerator) stepGeneratorSpec.build();
        }
    }

}
