package org.androware.flow.binding;

import org.androware.flow.Flow;
import org.androware.flow.Step;

/**
 * Created by jkirkley on 7/4/16.
 */

public interface ObjectLoader {

    public Object load(ObjectLoaderSpec spec, Flow flow, Step step);
}
