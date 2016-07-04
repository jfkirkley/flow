package org.androware.flow.test_objs;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.DataObjectBinder;
import org.androware.flow.ObervableArrayMapFactory;
import org.androware.flow.Step;
import org.androware.flow.databinding.BindTest1Binding;
import org.androware.flow.databinding.ModelTestBinding;

/**
 * Created by jkirkley on 7/3/16.
 */

public class ModelBinder implements DataObjectBinder {
    @Override
    public View bind(LayoutInflater layoutInflater, ViewGroup container, Step step) {
        Model model = new Model();

        ModelTestBinding binding = DataBindingUtil.inflate(layoutInflater, ResourceUtils.getLayoutId(step.layout), container, false);
        binding.setModel(model);
        return binding.getRoot();

    }
}
