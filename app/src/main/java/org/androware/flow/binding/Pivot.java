package org.androware.flow.binding;

/**
 * Created by jkirkley on 7/6/16.
 */
 public class Pivot {

    public String beanId;
    public String beanField;
    public String widgetId;
    public String bindTriggeringWidgetId;   // another widget (like a button) triggers the bind, not the data containing widget (widgetId)

    private boolean widgetConnected = false;

    public Pivot(String beanFieldSpec, String widgetId) {
        if(beanFieldSpec!=null ) {

            int dotIndex = beanFieldSpec.indexOf('.');
            int arrowIndex = beanFieldSpec.indexOf("->");

            if(arrowIndex!=-1) {

                String tks[] = beanFieldSpec.split("\\->");
                beanFieldSpec = tks[1];
                bindTriggeringWidgetId = tks[0];
            }

            if(dotIndex != -1) {

                String tks[] = beanFieldSpec.split("\\.");
                beanId = tks[0];
                beanField = tks[1];

            }

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

    public boolean hasOtherTrigger() {
        return bindTriggeringWidgetId != null;
    }
}
