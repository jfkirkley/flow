package org.androware.flow.binding;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Map;

/**
 * Created by jkirkley on 8/11/16.
 */

public class PositionalPivotGroup extends PivotGroup {
    int position;

    public PositionalPivotGroup(String matchId, Map<String, Pivot> stepPivots, int position) {
        super(matchId, stepPivots);
        this.position = position;
    }

    @Override
    public View getView(View view) {
        if(view instanceof ViewGroup) {
            return ((ViewGroup)view).getChildAt(position);
        }
        return super.getView(view);
    }
}
