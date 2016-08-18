package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.flow.binding.ObjectLoaderSpec;

/**
 * Created by jkirkley on 8/5/16.
 */

public class ExternalJsonFileObjectLoader extends JsonObjectLoader {

    @Override
    public Object load(ObjectLoaderSpec objectLoaderSpec, Flow flow, Step step) {

        String fileName = (String) ReflectionUtils.checkNullGet(objectLoaderSpec, "properties", "ext_file_name");

        objectLoaderSpec.addProp(EXT_FILE_PATH, fileName);

        return super.load(objectLoaderSpec, flow, step);
    }

}
