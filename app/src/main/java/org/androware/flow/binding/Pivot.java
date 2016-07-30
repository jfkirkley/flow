package org.androware.flow.binding;

/**
 * Created by jkirkley on 7/6/16.
 */
 public class Pivot {

    public String beanId;
    public String beanField;
    public String widgetId;

    private boolean widgetConnected = false;

    public Pivot(String beanFieldSpec, String widgetId) {
        if(beanFieldSpec!=null && beanFieldSpec.indexOf('.') != -1) {
            String tks[] = beanFieldSpec.split("\\.");
            beanId = tks[0];
            beanField = tks[1];
        } else {
            beanField = beanFieldSpec;
        }
        this.widgetId = widgetId;
    }

    public String getKey() {
        return beanId + ":" + beanField + ":" + widgetId;
    }

    public boolean matches(BeanBinder beanBinder) {
        return beanBinder.beanId.equals(beanId);
    }

    public String toString() {
        return getKey();
    }

    public boolean isWidgetConnected() {
        return widgetConnected;
    }

    public void setWidgetConnected(boolean widgetConnected) {
        this.widgetConnected = widgetConnected;
    }

}
