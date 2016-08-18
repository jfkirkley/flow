package org.androware.flow.binding;

import org.androware.flow.binding.ObjectSaverSpec;

/**
 * Created by jkirkley on 7/4/16.
 */

public interface ObjectSaver {

    public void save(ObjectSaverSpec objectSaverSpec, Object target);
}
