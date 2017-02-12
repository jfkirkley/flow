package org.androware.flow;


import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by jkirkley on 5/8/16.
 */

public class StepFragment extends Fragment implements TransitionActor {

    public Step getStep() {
        return step;
    }

    public static void l(String t) {
        FilterLog.inst().log("flow", t);
    }


    @Override
    public void onResume() {
        super.onResume();

        // pretransition happens in FlowContainerActivity loadStepFragment
        step.postTransition(this);

        ((FlowContainerActivity) getActivity()).fragmentStepVisible(step);
    }


    public Step step;

    /**
     * Create a new instance of StepFragment, initialized to the given step name
     */
    public static StepFragment newInstance(Step step) {

        String stepName = step.getName();
        l( "stepanem: " + stepName);
        l( "stepanem: " + step.processor);


        //StepFragment f = new StepFragment();
        StepFragment f = (StepFragment) ReflectionUtils.newInstance(step.processor);

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("stepName", stepName);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field fragmentManager = Fragment.class.getDeclaredField("mFragmentManager");
            fragmentManager.setAccessible(true);
            fragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stepName = getArguments().getString("stepName");

        step = JsonFlowEngine.inst().getCurrFlow().getStep(stepName);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = Utils.inflateView(step.layout, inflater, container);

        this.step.setStepFragment(this);

        GUI_utils.buildUI(getActivity(), step, view);

        step.setNavHandlers(view, (FlowContainerActivity) getActivity());

        step.customizeView(getActivity(), view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

    }


    public View getSubView(String idStr) {
        return getView().findViewById(ResourceUtils.getResId("id", idStr));
    }


    @Override
    public boolean isFragment() {
        return true;
    }

    @Override
    public View getRootView() {
        return getView();
    }
}
