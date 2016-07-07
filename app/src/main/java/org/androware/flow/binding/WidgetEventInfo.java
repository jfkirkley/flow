package org.androware.flow.binding;

/**
 * Created by jkirkley on 7/6/16.
 */

public class WidgetEventInfo {
    Object oldValue;
    Object newValue;
    Pivot pivot;

    public WidgetEventInfo( Object oldValue, Object newValue, Pivot pivot ) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.pivot = pivot;
    }
}
