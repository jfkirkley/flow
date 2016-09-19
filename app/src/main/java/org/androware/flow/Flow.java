package org.androware.flow;

import android.util.Log;

import org.androware.flow.base.FlowBase;
import org.androware.flow.base.ObjectLoaderSpecBase;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.BindEngine;
import org.androware.flow.binding.ObjectLoaderSpec;
import org.androware.flow.binding.TwoWayMapper;


/**
 * Created by jkirkley on 5/7/16.
 */
public class Flow extends FlowBase {



    BindEngine bindEngine = new BindEngine();

    public void setBoundObject(BeanBinder beanBinder) {
        setBoundObject(beanBinder, false);
    }

    public void setBoundObject(BeanBinder beanBinder, boolean setGlobal) {

        bindEngine.addBeanBinder((BeanBinder) beanBinder);

        if(setGlobal) {
            JsonFlowEngine.inst().addGlobalBeanBinder((BeanBinder) beanBinder);
        }
    }

    public Object getBoundObject(String name) {
        return bindEngine.getBeanBinder(name);
    }


    public StepGenerator stepGenerator;

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
        return getStep((Nav)startNav);
    }

    public Step getStep(String name) {
        return (Step)steps.get(name);
    }

    public Step getStep(Nav nav) {
        Step step = null;

        if(nav.usesStepGenerator()) {
            step = generateStep(nav);
            if(step != null) {
                steps.put(step.getName(), step);
            }
        } else {
            step = (Step)steps.get(nav.target);
            step.setName(nav.target);
        }
        if(step != null) {
            step.setCurrNav(nav);
        }
        return step;
    }

    public void l(String s) {
        Log.d(Constants.TAG, s);
    }

    public Flow() {
    }

    public Nav getStartNav() {
        return (Nav)startNav;
    }

    public void loadBoundObject(String phase, Step step) {
        if(objectLoaderSpecs != null ) {
            for(ObjectLoaderSpecBase objectLoaderSpecBase: objectLoaderSpecs) {
                ObjectLoaderSpec objectLoaderSpec = (ObjectLoaderSpec) objectLoaderSpecBase;
                if(objectLoaderSpec.isWhen(phase)) {
                    objectLoaderSpec.buildAndLoad(this, step);
                }
            }
        }
    }

    public void __init__() {
        loadBoundObject(ObjectLoaderSpecBase.ON_FLOW_INIT, null);

        for(String k: steps.keySet()) {
            Step step = (Step)steps.get(k);
            step.setName(k);
            step.setFlow(this);
            step.setStepTransition(StepTransitionFactory.getInstance().makeStepTransition(step));
            if( step.twoWayMapper != null) {
                bindEngine.addTwoWayMapper((TwoWayMapper)step.twoWayMapper);
            }
        }

        if (stepGeneratorSpec != null) {

            // TODO,  may need multiple step generators, these can be referred
            // from navs and may be parameterized from there
            stepGenerator = (StepGenerator) stepGeneratorSpec.build();
        }

        // step post init
        for(String k: steps.keySet()) {
            Step step = (Step)steps.get(k);
            step.postInit();
        }

    }

    public BindEngine getBindEngine() {
        return bindEngine;
    }
}
