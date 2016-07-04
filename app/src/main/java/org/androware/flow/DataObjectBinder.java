package org.androware.flow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jkirkley on 7/3/16.
 */

public interface DataObjectBinder {

    public View bind(LayoutInflater layoutInflater, ViewGroup container, Step step);
}
