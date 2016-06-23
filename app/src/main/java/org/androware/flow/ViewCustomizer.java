package org.androware.flow;


import android.app.Activity;
import android.view.View;


/**
 * Created by jkirkley on 6/2/16.
 */
public interface ViewCustomizer {

    public void customize(Activity activity, Object target, View page, Object config);
    public void setData(Object data);

}
