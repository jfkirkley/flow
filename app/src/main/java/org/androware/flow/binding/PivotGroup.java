package org.androware.flow.binding;

import android.view.View;

import java.util.HashMap;

import java.util.Map;

/**
 * Created by jkirkley on 8/11/16.
 */

public class PivotGroup {

    protected Map<String, Pivot> pivots;

    public PivotGroup(String matchId, Map<String, Pivot> stepPivots) {
        pivots = new HashMap<>();

        for(String k: stepPivots.keySet()) {
            Pivot p = stepPivots.get(k);
            if(p.matches(matchId)) {
                pivots.put(k, p);
            }
        }
    }

    public Map<String, Pivot> getPivots() {
        return pivots;
    }

    public View getView(View view) {
        return view;
    }
}
