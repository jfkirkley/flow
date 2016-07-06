package org.androware.flow;

import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.Utils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by jkirkley on 7/4/16.
 */

public class JsonObjectLoader implements ObjectLoader {
    public final static String RAW_RESOURCE_NAME = "rawResourceName";
    public final static String OBJECT_CLASSNAME = "objectClassName";
    public final static String EXT_FILE_PATH = "extFilePath";

    public JsonObjectLoader() {}

    @Override
    public Object load(ObjectLoaderSpec spec, Step step) {
        String objectClassName = (String) spec.properties.get(OBJECT_CLASSNAME);

        if(true) {
            Object o = ReflectionUtils.newInstance(objectClassName);
            step.getFlow().setBoundObject(objectClassName, o);
            return o;
        }

        if (objectClassName != null) {

            Object object = null;

            Class objectClass = ReflectionUtils.getClass(objectClassName);
            if (spec.properties.containsKey(RAW_RESOURCE_NAME)) {
                try {

                    object =  ObjectReaderFactory.getInstance().makeAndRunJsonReader(
                            (String) spec.properties.get(RAW_RESOURCE_NAME),
                            objectClass,
                            null);

                } catch (ObjectReadException e) {
                    // TODO handle ex
                }
            }

            else if (spec.properties.containsKey(EXT_FILE_PATH)) {
                try {

                    object = ObjectReaderFactory.getInstance().makeAndRunJsonReader(
                            new FileInputStream(
                                    Utils.getExternalFile(ObjectReaderFactory.getInstance().activity, "", "",
                                    (String) spec.properties.get(EXT_FILE_PATH))),
                            objectClass,
                            null);

                } catch (IOException e) {
                } catch (ObjectReadException e) {
                    // TODO handle ex
                }
            }

            step.getFlow().setBoundObject(objectClass.getName(), object);

            return object;
        }

        return null;

    }
}
