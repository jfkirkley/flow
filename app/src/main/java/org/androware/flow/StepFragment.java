package org.androware.flow;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;

/**
 * Created by jkirkley on 5/8/16.
 */

public class StepFragment extends Fragment {
    public Step getStep() {
        return step;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FlowContainerActivity)getActivity()).fragmentStepVisible(step);
    }

    View view;

    public Step step;
    /**
     * Create a new instance of StepFragment, initialized to the given step name
     */
    public static StepFragment newInstance(String stepName) {

        Log.d("bob", "stepanem: " + stepName);
        StepFragment f = new StepFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("stepName", stepName);
        f.setArguments(args);

        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String stepName = getArguments().getString("stepName");

        step = JsonFlowEngine.inst().getCurrFlow().getStep(stepName);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = Utils.inflateView(step.layout, inflater, container);

        GUI_utils.buildUI(getActivity(), step, view);

        step.setNavHandlers(view, (FlowContainerActivity) getActivity());

        step.customizeView(getActivity(), view);

        // pretransition happens in FlowContainerActivity loadStepFragment
        step.postTransition(this);

        return view;
    }

    public View getSubView(String idStr){
        return getView().findViewById(ResourceUtils.getResId("id", idStr));
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            ((FlowContainerActivity)getActivity()).fragmentStepVisible(step);
        }
        else {
        }
    }

*/

}
