package org.androware.flow;



import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;
import org.androware.flow.databinding.BindTest3Binding;
import org.androware.flow.test_objs.TestWrapper;

/**
 * Created by jkirkley on 7/2/16.
 */

public class BoundStepFragment extends StepFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BindTest3Binding bindTest3Binding;
        Object target = step.objectLoaderSpec.buildAndLoad(step);
        String methodName = "set" + target.getClass().getSimpleName();

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, ResourceUtils.getLayoutId(step.layout), container, false);

        if(ReflectionUtils.hasMethod(binding.getClass(), methodName, ObjectBindingObeservableArrayMap.class)) {
            ReflectionUtils.callMethod(binding, methodName, new ObjectBindingObeservableArrayMap(target));
            return binding.getRoot();
        }
        throw new IllegalArgumentException("Generated binding for layout  '" + step.layout  + "' must have a method with signature: public void " + methodName + "( ObjectBindingObeservableArrayMap map )");
    }



}
