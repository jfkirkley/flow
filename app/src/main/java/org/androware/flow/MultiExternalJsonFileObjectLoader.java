package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.ObjectLoaderSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkirkley on 8/9/16.
 */

public class MultiExternalJsonFileObjectLoader extends ExternalJsonFileObjectLoader {
    public static final  String EXT_FILE_LIST = "ext_file_list";
    public static final  String FILENAME_AS_ID = "filename_as_id";

    @Override
    public Object load(ObjectLoaderSpec objectLoaderSpec, Flow flow, Step step) {

        List<File> files = (List) ReflectionUtils.checkNullGet(objectLoaderSpec, "properties", EXT_FILE_LIST);
        boolean filenameAsId = (boolean) ReflectionUtils.checkNullGet(objectLoaderSpec, "properties", FILENAME_AS_ID);

        List<BeanBinder> beanBinders = new ArrayList<>(files.size());
        for(File file: files) {
            objectLoaderSpec.addProp("ext_file_name", file.getName());
            if(filenameAsId) {
                objectLoaderSpec.objectId = Utils.removePunctuation(file.getName(), "_");
            }
            beanBinders.add((BeanBinder)super.load(objectLoaderSpec, flow, step));
        }

        return beanBinders;
    }
}
