package org.androware.flow.binding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkirkley on 7/6/16.
 */
 public class Pivot {

    public static  final String CASCADE_PTR = "==>";
    public static  final String TRIGGER_ARROW = "->";
    public static  final String TRIGGER_ARROW_REGEX = "\\->";
    public static  final String DOT_SEP = ".";
    public static  final String DOT_SEP_REGEX = "\\.";
    public static  final String FORCE_UPDATE_SUFFIX = "!";

    public String beanId;
    public String beanField;
    public String widgetId;
    public String bindTriggeringWidgetId;   // another widget (like a button) triggers the bind, not the data containing widget (widgetId)

    private boolean widgetConnected = false;

    private boolean forceUpdate = false;

    private List<Pivot> cascadePivots;

    /*
    int itemPosition;


    public Pivot(String beanId, String beanField, String widgetId, int itemPosition) {
        this.beanId = beanId;
        this.beanField = beanField;
        this.widgetId = widgetId;
        this.itemPosition = itemPosition;
    }
*/

    public Pivot(String beanFieldSpec, String widgetId) {
        if(beanFieldSpec!=null ) {

            if(beanFieldSpec.endsWith(FORCE_UPDATE_SUFFIX)) {
                forceUpdate = true;
                beanFieldSpec = beanFieldSpec.substring(0, beanFieldSpec.length()-1);
            }

            int dotIndex = beanFieldSpec.indexOf(DOT_SEP);
            int arrowIndex = beanFieldSpec.indexOf(TRIGGER_ARROW);
            int cascadePtrIndex = beanFieldSpec.indexOf(CASCADE_PTR);



            if(cascadePtrIndex!=-1) {

                String tks[] = beanFieldSpec.split(CASCADE_PTR);
                beanFieldSpec = tks[0];

                cascadePivots = new ArrayList<>(tks.length-1);
                for(int i = 1; i < tks.length; ++i) {
                    cascadePivots.add(new Pivot(tks[i], null));
                }
            }
            if(arrowIndex!=-1) {

                String tks[] = beanFieldSpec.split(TRIGGER_ARROW_REGEX);
                beanFieldSpec = tks[1];
                bindTriggeringWidgetId = tks[0];
            }

            if(dotIndex != -1) {

                String tks[] = beanFieldSpec.split(DOT_SEP_REGEX);
                beanId = tks[0];
                beanField = tks[1];
                if(tks.length > 2) {
                    widgetId = tks[2];
                }
            }

        } else {

            beanField = beanFieldSpec;

        }

        this.widgetId = widgetId;
    }

    public String getKey() {
        return beanId + ":" + beanField; //+ ":" + widgetId;
    }

    public boolean matches(BeanBinder beanBinder) {
        return matches(beanBinder.beanId, beanBinder.alias);
    }

    public boolean matches(String id, String alias) {
        return beanId.equals(id) || beanId.equals(alias);
    }

    public boolean matches(String id) {
        return matches(id, null);
    }


    public String toString() {
        return getKey() + ":" + widgetId;
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

    public List<Pivot> getCascadePivots() {
        return cascadePivots;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

}
