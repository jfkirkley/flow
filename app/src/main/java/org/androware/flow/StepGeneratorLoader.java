package org.androware.flow;

/**
 * Created by jkirkley on 7/13/16.
 */

public class StepGeneratorLoader implements ObjectLoader {

    @Override
    public Object load(ObjectLoaderSpec spec, Step step) {
        return step.getFlow().stepGenerator;
    }

}
