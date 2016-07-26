package org.androware.flow.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;

import android.widget.CompoundButton;
import android.widget.DatePicker;

import android.widget.ImageView;
import android.widget.NumberPicker;

import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.TimePicker;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.GUI_utils;
import org.androware.flow.Step;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;


/**
 * Created by jkirkley on 7/6/16.
 */

public class EventCatcher  {
    BindEngine bindEngine;

    public EventCatcher(BindEngine bindEngine) {
        this.bindEngine = bindEngine;
    }

    protected void sendEvent(WidgetEventInfo widgetEventInfo) {
        bindEngine.handleEvent(widgetEventInfo);
    }

    public void l(String s) {
        FilterLog.inst().log(BindEngine.TAG, s);
    }


    public class TextViewCatcher implements TextWatcher {

        Pivot pivot;
        CharSequence oldValue;

        public TextViewCatcher(TextView textView, Pivot pivot, BeanBinder beanBinder){
            this.pivot = pivot;
            oldValue = (CharSequence)beanBinder.get(pivot.beanField);
            textView.setText(oldValue);
            textView.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newValue = s.toString();
            if(!oldValue.equals(newValue)) {
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public void catchTextView(TextView textView, Pivot pivot, BeanBinder beanBinder) {
        new TextViewCatcher(textView, pivot, beanBinder);
    }

    public void catchNumberPicker(NumberPicker numberPicker, Pivot pivot) {

    }

    public void catchTimePicker(TimePicker timePicker, Pivot pivot) {

    }

    public class AdapterViewCatcher implements AdapterView.OnItemSelectedListener {
        Pivot pivot;
        Object oldValue;

        public AdapterViewCatcher(AdapterView adapterView, Pivot pivot, BeanBinder beanBinder){
            this.pivot = pivot;
            oldValue = beanBinder.get(pivot.beanField);
            GUI_utils.setAdapterViewSelectedItem(adapterView, oldValue);

            adapterView.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Object newValue = parent.getAdapter().getItem(position);
            if(!oldValue.equals(newValue)) {
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class CalendarViewCatcher implements CalendarView.OnDateChangeListener {
        Pivot pivot;
        long oldValue;

        public CalendarViewCatcher(CalendarView calendarView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (long)beanBinder.get(pivot.beanField);
            calendarView.setDate(oldValue);
            calendarView.setOnDateChangeListener(this);
        }

        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            if(oldValue != view.getDate()) {
                sendEvent(new WidgetEventInfo(oldValue, view.getDate(), pivot));
                oldValue = view.getDate();
            }
        }
    }

    public void catchCalendarView(CalendarView calendarView, Pivot pivot, BeanBinder beanBinder) {
        new CalendarViewCatcher(calendarView, pivot, beanBinder);
    }

    public class CompoundButtonCatcher implements CompoundButton.OnCheckedChangeListener {
        Pivot pivot;
        boolean oldValue;

        public CompoundButtonCatcher(CompoundButton compoundButton, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (boolean)beanBinder.get(pivot.beanField);
            compoundButton.setChecked(oldValue);
            compoundButton.setOnCheckedChangeListener(this);
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(oldValue != isChecked) {
                sendEvent(new WidgetEventInfo(oldValue, isChecked, pivot));
                oldValue = isChecked;
            }
        }
    }

    // radio buttons and checkboxes
    public void catchCompoundButton(CompoundButton compoundButton , Pivot pivot, BeanBinder beanBinder) {
        new CompoundButtonCatcher(compoundButton, pivot, beanBinder);
    }


    public class RadioGroupCatcher implements RadioGroup.OnCheckedChangeListener {
        Pivot pivot;
        int oldValue;

        public RadioGroupCatcher(RadioGroup radioGroup, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (int)beanBinder.get(pivot.beanField);
            radioGroup.setId(oldValue);
            radioGroup.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            sendEvent(new WidgetEventInfo(oldValue, checkedId, pivot));
            oldValue = checkedId;
        }
    }

    public void catchRadioGroup(RadioGroup radioGroup, Pivot pivot, BeanBinder beanBinder) {
        new RadioGroupCatcher(radioGroup, pivot, beanBinder);
    }

    public class DatePickerCatcher implements DatePicker.OnDateChangedListener {
        Pivot pivot;

        GregorianCalendar oldCalendar = new GregorianCalendar();

        public DatePickerCatcher(DatePicker datePicker, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            long oldValue = (long)beanBinder.get(pivot.beanField);
            oldCalendar.setTime(new Date(oldValue));
            datePicker.init(oldCalendar.get(Calendar.YEAR), oldCalendar.get(Calendar.MONTH), oldCalendar.get(Calendar.DAY_OF_MONTH), this);
        }

        @Override
        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            GregorianCalendar newCalendar = new GregorianCalendar();
            newCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            sendEvent(new WidgetEventInfo(oldCalendar, newCalendar, pivot));
            oldCalendar = newCalendar;
        }
    }

    public void catchDatePicker(DatePicker datePicker, Pivot pivot, BeanBinder beanBinder) {
        new DatePickerCatcher(datePicker, pivot, beanBinder);
    }

    public void catchAdapterView(AdapterView adapterView, Pivot pivot, BeanBinder beanBinder) {
        new AdapterViewCatcher(adapterView, pivot, beanBinder);
    }

    public void setImageView(ImageView imageView, Pivot pivot, BeanBinder beanBinder) {
        imageView.setImageResource(ResourceUtils.getResId("drawable", (String)beanBinder.get(pivot.beanField)));
    }

    public void setTextView(TextView textView, Pivot pivot, BeanBinder beanBinder) {
        textView.setText((String)beanBinder.get(pivot.beanField));
    }

    public void catchWidget(View widget, Pivot pivot, BeanBinder beanBinder) {
        if (widget instanceof TextView) {

            catchTextView((TextView) widget, pivot, beanBinder);

            //} else if (widget instanceof TextView) {
            //  setTextView((TextView) widget, pivot, beanBinder);

        } else if (widget instanceof DatePicker) {
            catchDatePicker((DatePicker) widget, pivot, beanBinder);
        } else if (widget instanceof CompoundButton) {
            catchCompoundButton((CompoundButton) widget, pivot, beanBinder);
        } else if (widget instanceof CalendarView) {
            catchCalendarView((CalendarView) widget, pivot, beanBinder);
        } else if (widget instanceof RadioGroup) {
            catchRadioGroup((RadioGroup) widget, pivot, beanBinder);
        } else if (widget instanceof AdapterView) {
            catchAdapterView((AdapterView) widget, pivot, beanBinder);
        } else if (widget instanceof ImageView) {
            setImageView((ImageView) widget, pivot, beanBinder);
        }

    }

    public void setAll(Step step, BeanBinder beanBinder, View rootView, View fragmentView) {
        TwoWayMapper twoWayMapper = step.twoWayMapper;

        Map<String, Pivot> pivots = twoWayMapper.getPivots();

        for(String beanKey: pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);

            if(pivot.matches(beanBinder)) {

                l("setall pivot: " + pivot);
                View view = fragmentView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));

                if( view == null ) {
                    if(!pivot.isWidgetConnected()) {
                        view = rootView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));
                    } else {
                        continue;   // global widget already connected
                    }
                }

                catchWidget(view, pivot, beanBinder);
                pivot.setWidgetConnected(true);
            }
        }

    }

    private static EventCatcher inst = null;

    public static EventCatcher inst(BindEngine bindEngine) {
        if(inst == null) {
            inst = new EventCatcher(bindEngine);
        }
        return inst;
    }

}
