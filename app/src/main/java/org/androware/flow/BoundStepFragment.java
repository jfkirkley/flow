package org.androware.flow;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.EventCatcher;
import org.androware.flow.binding.TwoWayMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by jkirkley on 7/2/16.
 */

public class BoundStepFragment extends StepFragment {
    List<BeanBinder> binderList;
    boolean needsUpdate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(binderList == null) {

            Object beanObj = step.objectLoaderSpec.buildAndLoad(step);

            if (beanObj instanceof List) {
                binderList = (List) beanObj;
            } else {
                binderList = new ArrayList<>();
                binderList.add((BeanBinder) beanObj);
            }

            // need the root view, as some components are higher in the hierarchy than the current fragment
            final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.getActivity().findViewById(android.R.id.content)).getChildAt(0);

            for(BeanBinder beanBinder: binderList) {
                EventCatcher.inst(step.getFlow().getBindEngine()).setAll(step, beanBinder, viewGroup, view);
            }
        } else {

            needsUpdate = true;
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if(needsUpdate) {

            TwoWayMapper twoWayMapper = step.twoWayMapper;

            for(BeanBinder beanBinder: binderList) {
                twoWayMapper.refresh(beanBinder);
            }

            needsUpdate = false;
        }
    }


    public void updateWidget(String componentId, Object value) {
        View view = getView();

        if(view != null) {

            EventCatcher.inst(null).updateWidget(componentId, value, view);

        } else {

            // view == null means not visible, so flag update for when onResume is called
            needsUpdate = true;
        }

    }


}
