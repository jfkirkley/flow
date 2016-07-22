package org.androware.flow;

import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;

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
        String objectClassName = spec.objectClassName;

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

            else {
                // just instantiate the object from default constructor
                object = ReflectionUtils.newInstance(objectClass);
            }

            BeanBinder beanBinder = new BeanBinder(object, spec.objectId, step);
            step.getFlow().setBoundObject(spec.objectId, beanBinder);

            return beanBinder;
        }

        return null;

    }
}
