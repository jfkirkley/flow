package org.androware.flow;

/**
 * Created by jkirkley on 7/4/16.
 */

public interface ObjectPersistor {

    public void persist(ObjectPersistorSpec objectPersistorSpec, Object target);
}
