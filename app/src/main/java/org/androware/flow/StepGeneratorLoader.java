package org.androware.flow;

import org.androware.flow.binding.BeanBinder;

/**
 * Created by jkirkley on 7/13/16.
 */

public class StepGeneratorLoader implements ObjectLoader {

    @Override
    public Object load(ObjectLoaderSpec spec, Flow flow, Step step) {
        return new BeanBinder(step.getFlow().stepGenerator, spec.objectId, flow.getBindEngine());
    }

}
