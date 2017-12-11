package org.androware.flow;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import org.androware.flow.binding.BeanBinder;
import org.androware.flow.binding.EventCatcher;
import org.androware.flow.binding.Pivot;
import org.androware.flow.binding.TwoWayMapper;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.value;


/**
 * Created by jkirkley on 7/2/16.
 */

public class BoundStepFragment extends StepFragment {
    List<BeanBinder> binderList;
    boolean needsUpdate = false;

    Map<Pivot, Set<View>> pivot2ViewSetMap;

    public boolean checkSetViewByPivot(Pivot pivot, View view) {
        Set<View> set = pivot2ViewSetMap.get(pivot);
        if( set == null) {
            set = new HashSet<>();
            pivot2ViewSetMap.put(pivot, set);
        }
        if(!set.contains(view)) {
            set.add(view);
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pivot2ViewSetMap = new HashMap<>();

        final View view = super.onCreateView(inflater, container, savedInstanceState);

        if (binderList == null) {

            binderList = step.getBeanBinderList();
            if( binderList == null ) {
                binderList = new ArrayList();
            }

        } else {

            needsUpdate = true;
        }

        // need the root view, as some components are higher in the hierarchy than the current fragment
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this.getActivity().findViewById(android.R.id.content)).getChildAt(0);

        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                // at this point layout is complete
                step.getFlow().getBindEngine().getEventCatcher().setAll(step, viewGroup, view, false);
            }
        });

        if(false) {
            for (BeanBinder beanBinder : binderList) {
                step.getFlow().getBindEngine().getEventCatcher().setAll(step, beanBinder, viewGroup, view);
            }
        } else {
            //step.getFlow().getBindEngine().getEventCatcher().setAll(step, viewGroup, view);
        }

        return view;
    }


    @Override
    public void onResume() {
         super.onResume();

        if (needsUpdate) {

            View view = getView();

            if (view != null) {
                TwoWayMapper twoWayMapper = step.getTwoWayMapper();
                EventCatcher eventCatcher = step.getFlow().getBindEngine().getEventCatcher();
                if (true) {

                    for (BeanBinder beanBinder : binderList) {
                        twoWayMapper.refresh(beanBinder);
                        //eventCatcher.updateWidgetGroup(beanBinder.getPivotGroup(), value, view);
                    }

                } else {

                    // TODO more to the point
                    Map<String, Pivot> pivots = twoWayMapper.getPivots();

                    for (BeanBinder beanBinder : binderList) {

                        eventCatcher.updateWidgetGroup(beanBinder.getPivotGroup(), value, view);

                        for (String beanKey : pivots.keySet()) {
                            Pivot pivot = pivots.get(beanKey);
                            if (pivot.matches(beanBinder)) {
                                eventCatcher.updateWidget(pivot, value, view);
                            }
                        }
                    }


                }

                needsUpdate = false;
            }
        }
    }


    public void updateWidget(String componentId, Object value) {
        View view = getView();

        if (view != null) {

            step.getFlow().getBindEngine().getEventCatcher().updateWidget(componentId, value, view);

        } else {

            // view == null means not visible, so flag update for when onResume is called
            needsUpdate = true;
        }

    }

    public void invalidate() {
        needsUpdate = true;
    }

    public void addBeanBinder(BeanBinder beanBinder) {
        for (BeanBinder b : binderList) {
            //if(b)
        }
        binderList.add(beanBinder);
    }


}
