package org.androware.flow;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;


import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;

/**
 * Created by jkirkley on 5/8/16.
 *
 * Base class for Activities that are started as part of a flow.
 * Not to be confused with the FlowContainerActivity
 * that contains and manages a flow of fragments and Activities.
 * A FlowContainerActivity can start a BaseFlowActivity and manage
 * it like a StepFragment,   giving it some degree of control over the Activity.
 */
public class BaseFlowActivity extends FragmentActivity {

    Step step;

    public void l(String s) {
        FilterLog.inst().log(Constants.TAG, s);
    }

    public void doPreInit(Step step) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String stepName = Utils.getStringFromExtra(getIntent(), "stepName");
        step = JsonFlowEngine.inst().getFlowStep(stepName);

        setContentView(ResourceUtils.getResId("layout", step.layout));

        step.preTransition(this);
        doPreInit(step);

        ViewGroup rootView = (ViewGroup) ((ViewGroup) this
            .findViewById(android.R.id.content)).getChildAt(0);

        GUI_utils.buildUI(this, step, rootView);

        step.setNavHandlers(rootView);


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        step.postTransition(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
