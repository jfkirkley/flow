package org.androware.flow.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;

import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import android.widget.RadioGroup;
import android.widget.Spinner;

import android.widget.TimePicker;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.Constants;
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


    public class EditTextCatcher implements TextWatcher {

        Pivot pivot;
        CharSequence oldValue;

        public EditTextCatcher(EditText editText, Pivot pivot, BeanBinder beanBinder){
            this.pivot = pivot;
            oldValue = (CharSequence)beanBinder.get(pivot.beanField);
            editText.setText(oldValue);
            editText.addTextChangedListener(this);
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

    public void catchEditText(EditText editText, Pivot pivot, BeanBinder beanBinder) {
        new EditTextCatcher(editText, pivot, beanBinder);
    }

    public void catchNumberPicker(NumberPicker numberPicker, Pivot pivot) {

    }

    public void catchTimePicker(TimePicker timePicker, Pivot pivot) {

    }

    public class SpinnerCatcher implements AdapterView.OnItemSelectedListener {
        Pivot pivot;
        Object oldValue;

        public SpinnerCatcher(Spinner spinner, Pivot pivot, BeanBinder beanBinder){
            this.pivot = pivot;
            oldValue = beanBinder.get(pivot.beanField);
            GUI_utils.setAdapterViewSelectedItem(spinner, oldValue);

            spinner.setOnItemSelectedListener(this);
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

    public void catchSpinner(Spinner spinner, Pivot pivot, BeanBinder beanBinder){
        new SpinnerCatcher(spinner, pivot, beanBinder);
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
            oldValue = compoundButton.isChecked();
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

    public void catchListView(ListView listView, Pivot pivot) {

    }

    public void catchWidget(View widget, Pivot pivot, BeanBinder beanBinder) {
        if (widget instanceof EditText) {
            catchEditText((EditText)widget, pivot, beanBinder);
        } else if (widget instanceof DatePicker) {
            catchDatePicker((DatePicker) widget, pivot, beanBinder);
        } else if (widget instanceof CompoundButton) {
            catchCompoundButton((CompoundButton) widget, pivot, beanBinder);
        } else if (widget instanceof CalendarView) {
            catchCalendarView((CalendarView) widget, pivot, beanBinder);
        } else if (widget instanceof Spinner) {
            catchSpinner((Spinner)widget, pivot, beanBinder);
        } else if (widget instanceof RadioGroup) {
            catchRadioGroup((RadioGroup) widget, pivot, beanBinder);
        }

    }

    public void setAll(Step step, BeanBinder beanBinder, View rootView) {
        TwoWayMapper twoWayMapper = step.twoWayMapper;

        Map<String, Pivot> pivots = twoWayMapper.getPivots();

        for(String beanKey: pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);
            l("setall pivot: " + pivot);
            View view = rootView.findViewById(ResourceUtils.getResId("id", pivot.componentId));
            catchWidget(view, pivot, beanBinder);
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
