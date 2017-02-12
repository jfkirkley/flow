package org.androware.flow.binding;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.base.AdapterViewSpec;
import org.androware.flow.Step;

import java.util.List;


/**
 * Created by jkirkley on 5/11/16.
 */
public class BindingArrayAdapter extends ArrayAdapter {

    public static void l(String t) {
        FilterLog.inst().log("binding", t);
    }



    List<Object> rowDataList;
    Step step;
    Activity activity;

    int selectedItem = -1;

    AdapterViewSpec adapterViewSpec;
    public BindingArrayAdapter(Activity activity, List<Object> rowDataList, Step step, AdapterViewSpec adapterViewSpec) {
        super(activity, ResourceUtils.getResId("layout", adapterViewSpec.itemLayoutId), rowDataList);
        this.adapterViewSpec = adapterViewSpec;
        this.activity = activity;
        this.rowDataList = rowDataList;

        this.step = step;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //creating LayoutInflator for inflating the row layout.

        BindEngine bindEngine = step.getFlow().getBindEngine();

        BeanBinder beanBinder = new BeanBinder(rowDataList.get(position), getItemBinderName(step.getName(), position), Pivot.CURR_ITEM_NAME, bindEngine, step);

        //String matchId = adapterViewSpec.beanIds.get(0);   // TODO hack alert! needs work ...

        bindEngine.addBeanBinder(beanBinder);   // add with beanId

        //beanBinder.setPivotGroup(new PositionalPivotGroup(matchId, step.getTwoWayMapper().pivots, position));

        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflating the row layout we defined earlier.
        convertView = inflator.inflate(ResourceUtils.getResId("layout", adapterViewSpec.itemLayoutId), null);


        step.getFlow().getBindEngine().getEventCatcher().setAll(step, null, convertView, true);

        l( position  + " ==  " + selectedItem);
        if(position == selectedItem){
            convertView.setBackgroundColor(Color.LTGRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    public static String getItemBinderName(String stepName, int position){
        return stepName + "_item_" + position;
    }

    public void setSelectedItem(int i){
        selectedItem = i;
    }
}
