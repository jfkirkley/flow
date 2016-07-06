package org.androware.flow;



import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;


import static org.androware.flow.JsonObjectLoader.OBJECT_CLASSNAME;

/**
 * Created by jkirkley on 7/2/16.
 */

public class BoundStepFragment extends StepFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Class objectClass = ReflectionUtils.getClass((String) step.objectLoaderSpec.properties.get(OBJECT_CLASSNAME));
        Object target = step.objectLoaderSpec.buildAndLoad(step);
        String methodName = "set" + objectClass.getSimpleName();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, ResourceUtils.getLayoutId(step.layout), container, false);

        //if(ReflectionUtils.hasMethod(binding.getClass(), methodName, ObjectBindingObeservableArrayMap.class)) {
        if(ReflectionUtils.hasMethod(binding.getClass(), methodName, objectClass)) {
            ReflectionUtils.callMethod(binding, methodName, target);
            return binding.getRoot();
        }
        throw new IllegalArgumentException("Generated binding for layout  '" + step.layout  + "' must have a method with signature: public void " + methodName + "( ObjectBindingObeservableArrayMap map )");
    }



}
