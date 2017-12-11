package org.androware.flow;

import org.androware.androbeans.ObjectReadException;
import org.androware.androbeans.ObjectReaderFactory;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.ObjectLoader;
import org.androware.flow.binding.ObjectLoaderSpec;

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
    public Object load(ObjectLoaderSpec spec, Flow flow, Step step) {
        String objectClassName = spec.objectClassName;

        if (objectClassName != null) {

            Object object = null;

            Class objectClass = ReflectionUtils.getClass(objectClassName);

            String rawResource = (String) spec.properties.get(RAW_RESOURCE_NAME);
            String extFilePath = (String) spec.properties.get(EXT_FILE_PATH);

            if ( rawResource != null ) {
                try {

                    object = ObjectReaderFactory.getInstance().makeAndRunJsonReader(
                            rawResource,
                            objectClass,
                            null);

                } catch (ObjectReadException e) {
                    // TODO handle ex
                }
            }

            else if ( extFilePath != null &&
                    Utils.externalFileExists(
                            ObjectReaderFactory.getInstance().contextWrapper, "", "", extFilePath )) {

                try {

                    object = ObjectReaderFactory.getInstance().makeAndRunJsonReader(
                            new FileInputStream(
                                    Utils.getExternalFile(ObjectReaderFactory.getInstance().contextWrapper, "", "",
                                            extFilePath )),
                            objectClass,
                            null);

                } catch (IOException e) {
                } catch (ObjectReadException e) {
                    // TODO handle ex
                }
            }

            else if ( extFilePath != null &&
                    Utils.internalFileExists(
                            ObjectReaderFactory.getInstance().contextWrapper, extFilePath )) {

                try {

                    object = ObjectReaderFactory.getInstance().makeAndRunJsonReader(
                            new FileInputStream(
                                    Utils.getInternalFile(ObjectReaderFactory.getInstance().contextWrapper, extFilePath )), objectClass, null);

                } catch (IOException e) {
                } catch (ObjectReadException e) {
                    // TODO handle ex
                }
            }

            else {
                // just instantiate the object from default constructor
                object = ReflectionUtils.newInstance(objectClass);
            }

            BeanBinder beanBinder = new BeanBinder(object, spec.objectId, spec.alias, flow.getBindEngine(), step);
            flow.setBoundObject(beanBinder);

            return beanBinder;
        }

        return null;

    }
}
