package org.androware.flow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;


import org.androware.androbeans.utils.FilterLog;


/**
 * Created by jkirkley on 5/28/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityTracker implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = "act_track";

    public void l(String s) {
        FilterLog.inst().log(TAG, s);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        l( "Created: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivityStarted(Activity activity) {
        l( "Started: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivityResumed(Activity activity) {
        l( "Resumed: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivityPaused(Activity activity) {
        l( "Paused: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivityStopped(Activity activity) {
        l( "Stopped: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        l( "SaveState: " + activity.getLocalClassName() );
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        l( "Destroy: " + activity.getLocalClassName() );
    }
}
