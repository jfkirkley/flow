package org.androware.flow.binding;

import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;

import android.widget.CompoundButton;
import android.widget.DatePicker;

import android.widget.ImageView;
import android.widget.NumberPicker;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.MultiListenerUtils;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.BoundStepFragment;
import org.androware.flow.GUI_utils;
//import org.androware.flow.R;
import org.androware.flow.R;
import org.androware.flow.Step;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;


/**
 * Created by jkirkley on 7/6/16.
 */

public class EventCatcher {
    BindEngine bindEngine;

    boolean suppressEvents = false;

    public EventCatcher(BindEngine bindEngine) {
        this.bindEngine = bindEngine;
    }

    protected void sendEvent(WidgetEventInfo widgetEventInfo) {
        if(!suppressEvents) {
            bindEngine.handleEvent(widgetEventInfo);
        }
    }

    public void l(String s) {
        FilterLog.inst().log(BindEngine.TAG, s);
    }

    public class Button2AdapterViewUpdater implements Button.OnClickListener {
        Pivot pivot;
        Object oldValue;
        ArrayAdapter adapter;
        BeanBinder beanBinder;

        public Button2AdapterViewUpdater(View baseView, AdapterView adapterView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            this.beanBinder = beanBinder;
            if (adapterView.getAdapter() instanceof ArrayAdapter) {
                adapter = (ArrayAdapter) adapterView.getAdapter();
            } else {
                throw new IllegalArgumentException("Adapter for adapterView must be and ArrayAdapter!");
            }

            oldValue = beanBinder.get(pivot.beanField);
            GUI_utils.setAdapterViewSelectedItem(adapterView, oldValue);

            Button button = (Button) baseView.findViewById(ResourceUtils.getResId("id", pivot.bindTriggeringWidgetId));

            MultiListenerUtils.MultiOnClickListener.setListener(button, this);
        }

        @Override
        public void onClick(View v) {
            // it is assumed that another binding has updated the bean, so now update the adapter
            beanBinder.update(adapter);
        }
    }

    public class Button2TextViewCatcher implements Button.OnClickListener {
        Pivot pivot;
        CharSequence oldValue;
        TextView textView;

        public Button2TextViewCatcher(View baseView, TextView textView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            this.textView = textView;
            oldValue = (CharSequence) beanBinder.get(pivot.beanField);

            Button button = (Button) baseView.findViewById(ResourceUtils.getResId("id", pivot.bindTriggeringWidgetId));

            MultiListenerUtils.MultiOnClickListener.setListener(button, this, 0);
        }

        @Override
        public void onClick(View v) {
            CharSequence newValue = textView.getText();
            if (!oldValue.equals(newValue)) {
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
        }
    }

    public class TimePickerCatcher implements TimePicker.OnTimeChangedListener {
        Pivot pivot;
        String oldTime;

        public TimePickerCatcher(TimePicker timePicker, Pivot pivot, BeanBinder beanBinder) {

            this.pivot = pivot;
            Object oldValue = beanBinder.get(pivot.beanField);
            if(oldValue instanceof String) {
                oldTime = (String)oldValue;
                String tks [] = oldTime.split(":");
                int hours = Integer.parseInt(tks[0]);
                int mins = Integer.parseInt(tks[1]);

                timePicker.setCurrentHour(hours);
                timePicker.setCurrentMinute(mins);
            }
        }

        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            String t = (hourOfDay<10? "0" + hourOfDay: hourOfDay + "") + ":" + (minute<10? "0" + minute: minute + "");

            if(!t.equals(oldTime)) {
                sendEvent(new WidgetEventInfo(oldTime, t, pivot));

                oldTime = t;
            }

        }

    }

    public class CustomViewCatcher  {

        Pivot pivot;
        Object oldValue;

        public CustomViewCatcher(CustomView customView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (CharSequence) beanBinder.get(pivot.beanField);

            customView.setValue(oldValue);

            BindingArrayAdapter.l(pivot + " oldValue: " + oldValue);
        }
    }

    public void catchCustomView(CustomView customView, Pivot pivot, BeanBinder beanBinder) {
        new CustomViewCatcher(customView, pivot, beanBinder);
    }


    public class TextViewCatcher implements TextWatcher {

        Pivot pivot;
        CharSequence oldValue;

        public TextViewCatcher(TextView textView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (CharSequence) beanBinder.get(pivot.beanField);

            if (oldValue instanceof Spannable) {
                textView.setText(oldValue, TextView.BufferType.SPANNABLE);
            } else if (oldValue instanceof Editable) {
                textView.setText(oldValue, TextView.BufferType.EDITABLE);
            } else {
                textView.setText(oldValue);
            }

            textView.addTextChangedListener(this);

            BindingArrayAdapter.l(pivot + " oldValue: " + oldValue);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String newValue = s.toString();
            BindingArrayAdapter.l(pivot + " newValue: " + newValue);

            if (pivot.isForceUpdate() || !oldValue.equals(newValue)) {
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    public void catchTextView(View baseView, TextView textView, Pivot pivot, BeanBinder beanBinder) {

        if (pivot.hasOtherTrigger()) {
            // TODO we only support buttons as other trigger now
            new Button2TextViewCatcher(baseView, textView, pivot, beanBinder);

        } else {
            new TextViewCatcher(textView, pivot, beanBinder);
        }
    }

    public void catchNumberPicker(NumberPicker numberPicker, Pivot pivot) {

    }

    public void catchTimePicker(TimePicker timePicker, Pivot pivot, BeanBinder beanBinder) {
        new TimePickerCatcher(timePicker, pivot, beanBinder);
    }

    public class AdapterViewCatcher implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
        Pivot pivot;
        Object oldValue;

        public AdapterViewCatcher(AdapterView adapterView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = beanBinder.get(pivot.beanField);
            GUI_utils.setAdapterViewSelectedItem(adapterView, oldValue);

            adapterView.setOnItemSelectedListener(this);

            if(oldValue instanceof GUI_utils.AdapterItemWrapper) {
                adapterView.setOnItemClickListener(this);
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Object newValue = parent.getAdapter().getItem(position);
            if (!oldValue.equals(newValue)) {
                if(oldValue instanceof GUI_utils.AdapterItemWrapper) {
                    GUI_utils.AdapterItemWrapper oldWrapper = (GUI_utils.AdapterItemWrapper)oldValue;
                    GUI_utils.AdapterItemWrapper newWrapper = oldWrapper.newInstance(newValue);
                    //oldWrapper.setSelected(parent, -1, false);
                    newWrapper.setSelected(parent, position, true);
                    newValue = newWrapper;
                }
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object newValue = parent.getAdapter().getItem(position);
            if (!oldValue.equals(newValue)) {
                if(oldValue instanceof GUI_utils.AdapterItemWrapper) {
                    GUI_utils.AdapterItemWrapper oldWrapper = (GUI_utils.AdapterItemWrapper)oldValue;
                    GUI_utils.AdapterItemWrapper newWrapper = oldWrapper.newInstance(newValue);
                    //oldWrapper.setSelected(parent, -1, false);
                    //newWrapper.setSelected(parent, position, true);
                    newValue = newWrapper;
                }
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }

        }
    }

    public class CalendarViewCatcher implements CalendarView.OnDateChangeListener {
        Pivot pivot;
        long oldValue;

        public CalendarViewCatcher(CalendarView calendarView, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (long) beanBinder.get(pivot.beanField);
            l( (new Date(oldValue).toString()));

            calendarView.setDate(oldValue);
            calendarView.setOnDateChangeListener(this);
        }

        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            if (oldValue != view.getDate()) {
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
            oldValue = (boolean) beanBinder.get(pivot.beanField);
            compoundButton.setChecked(oldValue);
            compoundButton.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (oldValue != isChecked) {
                sendEvent(new WidgetEventInfo(oldValue, isChecked, pivot));
                oldValue = isChecked;
            }
        }
    }

    // radio buttons and checkboxes
    public void catchCompoundButton(CompoundButton compoundButton, Pivot pivot, BeanBinder beanBinder) {
        new CompoundButtonCatcher(compoundButton, pivot, beanBinder);
    }


    public class RadioGroupCatcher implements RadioGroup.OnCheckedChangeListener {
        Pivot pivot;
        String oldValue;

        public RadioGroupCatcher(RadioGroup radioGroup, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            oldValue = (String) beanBinder.get(pivot.beanField);
            l("old value: " + oldValue);
            radioGroup.check(ResourceUtils.getResId("id", oldValue));
            radioGroup.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String newValue = ResourceUtils.getViewIdName(group.findViewById(checkedId));
            l("new value: " + newValue);
            if(!oldValue.equals(newValue)) {
                sendEvent(new WidgetEventInfo(oldValue, newValue, pivot));
                oldValue = newValue;
            }
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
            long oldValue = (long) beanBinder.get(pivot.beanField);
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

    public class SeekBarCatcher implements SeekBar.OnSeekBarChangeListener {
        Pivot pivot;

        int value;
        int changingProgress;
        public SeekBarCatcher(SeekBar seekBar, Pivot pivot, BeanBinder beanBinder) {
            this.pivot = pivot;
            value = (int) beanBinder.get(pivot.beanField);
            seekBar.setOnSeekBarChangeListener(this);
            seekBar.setProgress(value);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            changingProgress = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if(changingProgress != value){
                sendEvent(new WidgetEventInfo(value, changingProgress, pivot));
                value = changingProgress;
            }

        }
    }


    public void catchDatePicker(DatePicker datePicker, Pivot pivot, BeanBinder beanBinder) {
        new DatePickerCatcher(datePicker, pivot, beanBinder);
    }

    public void catchSeekBar(SeekBar seekBar, Pivot pivot, BeanBinder beanBinder) {
        new SeekBarCatcher(seekBar, pivot, beanBinder);
    }

    public void catchAdapterView(View baseView, AdapterView adapterView, Pivot pivot, BeanBinder beanBinder) {
        if (pivot.hasOtherTrigger()) {
            new Button2AdapterViewUpdater(baseView, adapterView, pivot, beanBinder);
        } else {
            new AdapterViewCatcher(adapterView, pivot, beanBinder);
        }
    }

    public void setImageView(ImageView imageView, Pivot pivot, BeanBinder beanBinder) {
        imageView.setImageResource(ResourceUtils.getResId("drawable", (String) beanBinder.get(pivot.beanField)));
        //imageView.setImageDrawable(ResourceUtils.getDrawableResource((String) beanBinder.get(pivot.beanField)));
    }

    public void setTextView(TextView textView, Pivot pivot, BeanBinder beanBinder) {
        textView.setText((String) beanBinder.get(pivot.beanField));
    }

    public void updateWidgetGroup(PivotGroup pivotGroup, Object value, View view) {
        if(pivotGroup != null) {
            Map<String, Pivot> pivots = pivotGroup.getPivots();
            for (String k : pivots.keySet()) {
                updateWidget(pivots.get(k), value, pivotGroup.getView(view));
            }
        }
    }

    public void updateWidget(Pivot pivot, Object value, View view) {
        updateWidget(pivot.widgetId, value, view);
    }

    public void updateWidget(String componentId, Object value, View view) {

        View widget = view.findViewById(ResourceUtils.getResId("id", componentId));

        if (widget != null) {
            suppressEvents = true;

            if (widget instanceof CompoundButton) { // note textview is a super class of CompoundButton
                ((CompoundButton) widget).setChecked((boolean) value);
            } else if (widget instanceof TextView) {
                ((TextView) widget).setText((CharSequence) value);
            } else if (widget instanceof DatePicker) {
                Calendar calendar = (Calendar) value;
                ((DatePicker) widget).updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            } else if (widget instanceof CalendarView) {
                ((CalendarView) widget).setDate((long) value);
            } else if (widget instanceof RadioGroup) {
                ((RadioGroup) widget).check(ResourceUtils.getResId("id",(String)value));
            } else if (widget instanceof AdapterView) {
                GUI_utils.setAdapterViewSelectedItem((AdapterView) widget, value);
            } else if (widget instanceof ImageView) {
                ((ImageView)widget).setImageResource(ResourceUtils.getResId("drawable", (String) value));
                // setImageView((ImageView) widget, pivot, beanBinder);

            } else if (widget instanceof SeekBar) {
                ((SeekBar)widget).setProgress((int)value);

            } else if (widget instanceof CustomView) {
                ((CustomView)widget).setValue(value);

            }

            suppressEvents = false;

        }
    }


    public void catchWidget(View baseView, View widget, Pivot pivot, BeanBinder beanBinder) {

        if (widget instanceof CompoundButton) {  // note textview is a super class of CompoundButton
            catchCompoundButton((CompoundButton) widget, pivot, beanBinder);
        } else if (widget instanceof TextView) {
            catchTextView(baseView, (TextView) widget, pivot, beanBinder);
        } else if (widget instanceof DatePicker) {
            catchDatePicker((DatePicker) widget, pivot, beanBinder);
        } else if (widget instanceof CalendarView) {
            catchCalendarView((CalendarView) widget, pivot, beanBinder);
        } else if (widget instanceof RadioGroup) {
            catchRadioGroup((RadioGroup) widget, pivot, beanBinder);
        } else if (widget instanceof AdapterView) {
            catchAdapterView(baseView, (AdapterView) widget, pivot, beanBinder);
        } else if (widget instanceof ImageView) {
            setImageView((ImageView) widget, pivot, beanBinder);
        } else if (widget instanceof TimePicker) {
            catchTimePicker((TimePicker)widget, pivot, beanBinder);
        } else if (widget instanceof SeekBar) {
            catchSeekBar((SeekBar) widget, pivot, beanBinder);
        } else if (widget instanceof CustomView) {
            catchCustomView((CustomView)widget, pivot, beanBinder);
        }
    }

    public void setAll(Step step, BeanBinder beanBinder, View rootView, View fragmentView) {
        TwoWayMapper twoWayMapper = step.getTwoWayMapper();

        Map<String, Pivot> pivots = twoWayMapper.getPivots();

        for (String beanKey : pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);

            if (pivot.matches(beanBinder)) {

                l("setall pivot: " + pivot);
                View widget = fragmentView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));

                if (widget == null) {
                    if (rootView != null && !pivot.isWidgetConnected()) {
                        widget = rootView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));
                        catchWidget(rootView, widget, pivot, beanBinder);
                    } else {
                        continue;   // global widget already connected
                    }
                } else {
                    catchWidget(fragmentView, widget, pivot, beanBinder);
                }

                pivot.setWidgetConnected(true);
            }
        }

    }

    public void setAll(Step step, View rootView, View fragmentView, boolean allowTemporary) {
        TwoWayMapper twoWayMapper = step.getTwoWayMapper();

        BoundStepFragment boundStepFragment = (BoundStepFragment)step.getStepFragment();

        Map<String, Pivot> pivots = twoWayMapper.getPivots();

        for (String beanKey : pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);

            if(!allowTemporary && pivot.isTemporary()) {
                continue;
            }

            BeanBinder beanBinder = bindEngine.getBeanBinder(pivot);


            l("setall pivot: " + pivot);
            View widget = fragmentView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));

            if (widget == null) {
                if (rootView != null && !pivot.isWidgetConnected()) {
                    widget = rootView.findViewById(ResourceUtils.getResId("id", pivot.widgetId));
                    catchWidget(rootView, widget, pivot, beanBinder);
                } else {
                    continue;   // global widget already connected
                }

            } else if(!boundStepFragment.checkSetViewByPivot(pivot, widget)){
                catchWidget(fragmentView, widget, pivot, beanBinder);
            }

            pivot.setWidgetConnected(true);

        }
    }

/*
    private static EventCatcher inst = null;

    public static EventCatcher inst(BindEngine bindEngine) {
        if(inst == null) {
            inst = new EventCatcher(bindEngine);
        }
        return inst;
    }
*/
}
