package org.androware.flow;

import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;

import static org.androware.flow.JsonObjectLoader.EXT_FILE_PATH;
import static org.androware.flow.JsonObjectLoader.OBJECT_CLASSNAME;
import static org.androware.flow.JsonObjectLoader.RAW_RESOURCE_NAME;

/**
 * Created by jkirkley on 7/4/16.
 */

public class CachedObjectLoader implements ObjectLoader {

    public CachedObjectLoader() {}

    @Override
    public Object load(ObjectLoaderSpec spec, Step step) {
        String objectClassName = (String) spec.properties.get(OBJECT_CLASSNAME);

        if (objectClassName != null) {
            return step.getFlow().getBoundObject(objectClassName);
        }

        return null;

    }
}
