package org.androware.flow.binding;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.AdapterViewSpec;
import org.androware.flow.Step;

import java.util.List;
import java.util.Map;


/**
 * Created by jkirkley on 5/11/16.
 */
public class BindingArrayAdapter extends ArrayAdapter {

    public static  final String CURR_ITEM_NAME = "currItem";

    List<Object> rowDataList;
    Step step;
    Activity activity;

    AdapterViewSpec adapterViewSpec;
    public BindingArrayAdapter(Activity activity, List<Object> rowDataList, Step step, AdapterViewSpec adapterViewSpec) {
        super(activity, adapterViewSpec.getItemLayoutId(), rowDataList);
        this.adapterViewSpec = adapterViewSpec;
        this.activity = activity;
        this.rowDataList = rowDataList;

        this.step = step;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //creating LayoutInflator for inflating the row layout.

        BindEngine bindEngine = step.getFlow().getBindEngine();

        BeanBinder beanBinder = new BeanBinder(rowDataList.get(position), getItemBinderName(step.getName(), position), CURR_ITEM_NAME, bindEngine, step);

        String matchId = adapterViewSpec.beanIds.get(0);   // TODO hack alert! needs work ...

        bindEngine.addBeanBinder(beanBinder);   // add with beanId

        beanBinder.setPivotGroup(new PositionalPivotGroup(matchId, step.twoWayMapper.pivots, position));

        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // inflating the row layout we defined earlier.
        convertView = inflator.inflate(adapterViewSpec.getItemLayoutId(), null);

        step.getFlow().getBindEngine().getEventCatcher().setAll(step, null, convertView);

        return convertView;
    }

    public static String getItemBinderName(String stepName, int position){
        return stepName + "_item_" + position;
    }
}
