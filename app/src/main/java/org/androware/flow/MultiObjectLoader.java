package org.androware.flow;

import org.androware.flow.binding.BeanBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jkirkley on 7/22/16.
 */

public class MultiObjectLoader implements ObjectLoader {

    public final static String LOADER_CLASSES = "loaderClasses";
    @Override
    public Object load(ObjectLoaderSpec spec, Step step) {

        Map properties = spec.properties;
        if (properties != null && properties.containsKey(LOADER_CLASSES)) {

            Map loaderClassesMap = (Map) properties.get(LOADER_CLASSES);

            List<BeanBinder> binderList = new ArrayList<>();

            for(Object k: loaderClassesMap.keySet()) {
                String beanId = (String)k;
                Map classLoadData = (Map)loaderClassesMap.get(k);
                String loaderClassName = (String) classLoadData.get("loaderClass");
                String objectClassName = (String) classLoadData.get("objectClass");
                Map<String, Object> props = (Map) classLoadData.get("properties");

                ObjectLoaderSpec objectLoaderSpec = new ObjectLoaderSpec(loaderClassName, objectClassName, beanId, props);

                binderList.add((BeanBinder)objectLoaderSpec.buildAndLoad(step));
            }

            return binderList;
        }

        return null;
    }
}
