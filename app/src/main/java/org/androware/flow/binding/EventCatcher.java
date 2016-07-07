package org.androware.flow.binding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.Step;
import org.androware.flow.StepFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import static android.R.attr.value;

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

    public void catchSpinner(Spinner spinner, Pivot pivot) {

    }

    public class CalendarViewCatcher implements CalendarView.OnDateChangeListener {
        Pivot pivot;
        long oldValue;

        public CalendarViewCatcher(CalendarView calendarView, Pivot pivot) {
            this.pivot = pivot;
            oldValue = calendarView.getDate();
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

    public void catchCalendarView(CalendarView calendarView, Pivot pivot) {
        new CalendarViewCatcher(calendarView, pivot);
    }

    public class CompoundButtonCatcher implements CompoundButton.OnCheckedChangeListener {
        Pivot pivot;
        boolean oldValue;

        public CompoundButtonCatcher(CompoundButton compoundButton, Pivot pivot) {
            this.pivot = pivot;
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
    public void catchCompoundButton(CompoundButton compoundButton , Pivot pivot) {
        new CompoundButtonCatcher(compoundButton, pivot);
    }


    public class RadioGroupCatcher implements RadioGroup.OnCheckedChangeListener {
        Pivot pivot;
        int oldValue;

        public RadioGroupCatcher(RadioGroup radioGroup, Pivot pivot) {
            this.pivot = pivot;
            oldValue = radioGroup.getCheckedRadioButtonId();
            radioGroup.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            sendEvent(new WidgetEventInfo(oldValue, checkedId, pivot));
            oldValue = checkedId;
        }
    }

    public void catchRadioGroup(RadioGroup radioGroup, Pivot pivot) {
        new RadioGroupCatcher(radioGroup, pivot);
    }

    public class DatePickerCatcher implements DatePicker.OnDateChangedListener {
        Pivot pivot;

        GregorianCalendar oldCalendar = new GregorianCalendar();

        public DatePickerCatcher(DatePicker datePicker, Pivot pivot) {
            this.pivot = pivot;
            oldCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), this);
        }

        @Override
        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            oldCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            GregorianCalendar newCalendar = new GregorianCalendar();
            newCalendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            sendEvent(new WidgetEventInfo(oldCalendar, newCalendar, pivot));
            oldCalendar = newCalendar;
        }
    }

    public void catchDatePicker(DatePicker datePicker, Pivot pivot) {
        new DatePickerCatcher(datePicker, pivot);
    }

    public void catchListView(ListView listView, Pivot pivot) {

    }

    public void catchWidget(View widget, Pivot pivot, BeanBinder beanBinder) {
        if (widget instanceof EditText) {
            catchEditText((EditText)widget, pivot, beanBinder);
        } else if (widget instanceof DatePicker) {
            catchDatePicker((DatePicker) widget, pivot);
        } else if (widget instanceof CompoundButton) {
            catchCompoundButton((CompoundButton) widget, pivot);
        } else if (widget instanceof CalendarView) {
            catchCalendarView((CalendarView) widget, pivot);
        } else if (widget instanceof RadioGroup) {
            catchRadioGroup((RadioGroup) widget, pivot);
        }

    }

    public void setAll(Step step, BeanBinder beanBinder, View rootView) {
        TwoWayMapper twoWayMapper = step.twoWayMapper;

        Map<String, Pivot> pivots = twoWayMapper.getPivots();

        for(String beanKey: pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);
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
