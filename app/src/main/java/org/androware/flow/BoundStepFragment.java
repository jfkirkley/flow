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

import java.util.Calendar;


/**
 * Created by jkirkley on 7/2/16.
 */

public class BoundStepFragment extends StepFragment {
    BeanBinder beanBinder = null;
    boolean needsUpdate = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(beanBinder == null) {
            beanBinder = (BeanBinder) step.objectLoaderSpec.buildAndLoad(step);
            EventCatcher.inst(step.getFlow().getBindEngine()).setAll(step, beanBinder, view);
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        if(needsUpdate) {
            TwoWayMapper twoWayMapper = step.twoWayMapper;

            twoWayMapper.refresh(beanBinder);

            needsUpdate = false;
        }
    }


    public void updateWidget(String componentId, Object value) {
        View view = getView();

        if(view != null) {
            View widget = view.findViewById(ResourceUtils.getResId("id", componentId));

            if (widget != null) {
                if (widget instanceof TextView) {
                    ((TextView) widget).setText((CharSequence) value);
                } else if (widget instanceof DatePicker) {
                    //(() widget).set(() value);
                    Calendar calendar = (Calendar) value;
                    ((DatePicker) widget).updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                } else if (widget instanceof CompoundButton) {
                    ((CompoundButton) widget).setChecked((boolean) value);
                } else if (widget instanceof CalendarView) {
                    ((CalendarView) widget).setDate((long) value);
                } else if (widget instanceof RadioGroup) {
                    ((RadioGroup) widget).check((int) value);
                }
            }
        } else {
            needsUpdate = true;   // vieww == null means not visible, so flag update for when onResume is called
        }

    }


}
