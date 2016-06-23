package org.androware.flow;

import android.widget.ArrayAdapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import org.androware.androbeans.utils.ResourceUtils;

/**
 * Created by jkirkley on 5/11/16.
 */
public class  TestListAdapter extends ArrayAdapter {

    List<ListItemSpec> rowDataList;

    Activity activity;
    int listItemLayoutResourceId;
    public TestListAdapter(Activity activity, int listItemLayoutResourceId, List<ListItemSpec> rowDataList) {
        super(activity, listItemLayoutResourceId, rowDataList);
        this.activity = activity;
        this.rowDataList = rowDataList;
        this.listItemLayoutResourceId = listItemLayoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //creating LayoutInflator for inflating the row layout.
        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflating the row layout we defined earlier.
        convertView = inflator.inflate(listItemLayoutResourceId, null);

        ListItemSpec lessonData = rowDataList.get(position);

        TextView titleField = (TextView) convertView.findViewById(ResourceUtils.getResId("id", lessonData.labelFieldId));

        titleField.setText(lessonData.label);

        return convertView;
    }

}
