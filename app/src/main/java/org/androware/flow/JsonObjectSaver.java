package org.androware.flow;

import org.androware.androbeans.ObjectWriteException;
import org.androware.androbeans.ObjectWriterFactory;
import org.androware.flow.binding.ObjectSaver;
import org.androware.flow.binding.ObjectSaverSpec;

import static org.androware.flow.JsonObjectLoader.EXT_FILE_PATH;

/**
 * Created by jkirkley on 7/5/16.
 */

public class JsonObjectSaver implements ObjectSaver {

    @Override
    public void save(ObjectSaverSpec objectSaverSpec, Object target) {

        if (objectSaverSpec.properties.containsKey(EXT_FILE_PATH)) {
            try {
                ObjectWriterFactory.getInstance(null).writeJsonObjectToExternalFile((String) objectSaverSpec.properties.get(EXT_FILE_PATH), target);
            } catch (ObjectWriteException e) {
                // TODO handle exception
            }
        }
    }
}
