package org.androware.flow;

/**
 * Created by jkirkley on 7/4/16.
 */

public interface ObjectLoader {

    public Object load(ObjectLoaderSpec spec, Step step);
}
