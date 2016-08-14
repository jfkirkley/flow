package org.androware.flow.binding;

import org.androware.flow.BoundStepFragment;
import org.androware.flow.Step;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jkirkley on 7/6/16.
 */

public class TwoWayMapper {


    Step step;

    public HashMap<String, String> componentId2BeanFieldMap;

    protected Map<String, Pivot> pivots;

    public void __init__() {
        pivots = new HashMap<>();

        for(String componentId: componentId2BeanFieldMap.keySet()) {
            Pivot pivot = new Pivot(componentId2BeanFieldMap.get(componentId), componentId);
            pivots.put( pivot.getKey(), pivot );
        }
    }

    public void update(Pivot pivot, Object newValue) {

        BoundStepFragment stepFragment = (BoundStepFragment)step.getStepFragment();
        if(stepFragment != null) {
            if (pivots.containsKey(pivot.getKey())) {
                Pivot p = pivots.get(pivot.getKey());
                stepFragment.updateWidget(p.widgetId, newValue);
            }
        }
    }

    public void refresh(BeanBinder beanBinder) {

        BoundStepFragment stepFragment = (BoundStepFragment)step.getStepFragment();

        for (String beanKey : pivots.keySet()) {
            Pivot pivot = pivots.get(beanKey);
            if( pivot.matches(beanBinder) ) {
                stepFragment.updateWidget(pivot.widgetId, beanBinder.get(pivot.beanField));
            }
        }

    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Map<String, Pivot> getPivots() {
        return pivots;
    }

    public void addPivot(Pivot pivot) {
        pivots.put(pivot.getKey(), pivot);
    }

}
