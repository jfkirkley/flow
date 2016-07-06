package org.androware.flow.test_objs;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.DataObjectBinder;


import org.androware.flow.ObjectBindingObeservableArrayMap;
import org.androware.flow.Step;


/**
 * Created by jkirkley on 7/3/16.
 */

public class TestBinder implements DataObjectBinder {
    @Override
    public View bind(LayoutInflater layoutInflater, ViewGroup container, Step step) {
        TestWrapper testBindObject = new TestWrapper();
/*
        if(step.layout.equals("bind_test_1")) {
            ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, ResourceUtils.getLayoutId(step.layout), container, false);
            if(ReflectionUtils.hasMethod(binding.getClass(), "setTestWrapper", ObjectBindingObeservableArrayMap.class)) {
                ReflectionUtils.callMethod(binding, "setTestWrapper", new ObjectBindingObeservableArrayMap(testBindObject));
                return binding.getRoot();
            }


            BindTest1Binding binding = DataBindingUtil.inflate(layoutInflater, ResourceUtils.getLayoutId(step.layout), container, false);
            binding.setTestWrapper(new ObjectBindingObeservableArrayMap(testBindObject));
            return binding.getRoot();

        }

        else if(step.layout.equals("bind_test_2")) {

            BindTest2Binding binding = DataBindingUtil.inflate(layoutInflater, ResourceUtils.getLayoutId(step.layout), container, false);
            //binding.set(ObervableArrayMapFactory.build(testBindObject));
            binding.setTestWrapper(new ObjectBindingObeservableArrayMap(testBindObject));

            return binding.getRoot();

        } else if(step.layout.equals("bind_test_3")) {

            BindTest3Binding binding = DataBindingUtil.inflate(layoutInflater, ResourceUtils.getLayoutId(step.layout), container, false);
            //binding.setTestWrapper(ObervableArrayMapFactory.build(testBindObject);

            binding.setTestWrapper(new ObjectBindingObeservableArrayMap(testBindObject));
            return binding.getRoot();

        }
            */
         /*
*/
        return null;
    }
}
