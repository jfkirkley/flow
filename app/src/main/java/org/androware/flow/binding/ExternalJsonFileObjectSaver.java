package org.androware.flow.binding;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.flow.JsonObjectSaver;
import org.androware.flow.ObjectSaverSpec;

import static org.androware.flow.JsonObjectLoader.EXT_FILE_PATH;

/**
 * Created by jkirkley on 8/5/16.
 */

public class ExternalJsonFileObjectSaver extends JsonObjectSaver {

    @Override
    public void save(ObjectSaverSpec objectSaverSpec, Object target) {

        String fileName = (String)ReflectionUtils.checkNullGet(objectSaverSpec, "properties", "ext_file_name");

        objectSaverSpec.addProp(EXT_FILE_PATH, fileName);

        super.save(objectSaverSpec, target);
    }

}
