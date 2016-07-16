package org.androware.flow;

import android.util.Log;
import java.util.HashMap;
import org.androware.androbeans.utils.ConstructorSpec;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.BindEngine;


/**
 * Created by jkirkley on 5/7/16.
 */
public class Flow  {

    public String fragmentContainer;
    public HashMap<String, Step> steps;
    public String layout;
    public String processor;

    BindEngine bindEngine = new BindEngine();

    private HashMap<String, Object> boundObjects =new HashMap<>();

    public void setBoundObject(String name, Object object) {
        boundObjects.put(name, object);

        if( object instanceof BeanBinder){
            bindEngine.addBeanBinder(name, (BeanBinder)object);
        }
    }

    public Object getBoundObject(String name) {
        return boundObjects.get(name);
    }

    public ConstructorSpec stepGeneratorSpec;

    public StepGenerator stepGenerator;

    public Nav startNav;    // navigates to the first step

    public Step generateStep(Nav nav) {

        if(nav.target == null /* default is next */
                || nav.target.equals(Nav.GEN_NEXT)) {
            if(!stepGenerator.atEnd()) {
                return stepGenerator.next();
            } else {
                return null;
            }
        } else if(nav.target.equals(Nav.GEN_PREV)) {
            if(!stepGenerator.atStart()) {
                return stepGenerator.prev();
            } else {
                return null;
            }
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
            step.setFlow(this);
            step.setStepTransition(StepTransitionFactory.getInstance().makeStepTransition(step));
            if( step.twoWayMapper != null) {
                bindEngine.addTwoWayMapper(step.twoWayMapper);
            }
        }

        if (stepGeneratorSpec != null) {

            // TODO,  may need multiple step generators, these can be referred
            // from navs and may be parameterized from there
            stepGenerator = (StepGenerator) stepGeneratorSpec.build();
        }
    }

    public BindEngine getBindEngine() {
        return bindEngine;
    }


}
