package org.androware.flow.binding;

/**
 * Created by jkirkley on 7/6/16.
 */
 public class Pivot {

    public String beanId;
    public String beanField;
    public String componentId;

    public Pivot(String beanFieldSpec, String componentId) {
        if(beanFieldSpec!=null && beanFieldSpec.indexOf('.') != -1) {
            String tks[] = beanFieldSpec.split("\\.");
            beanId = tks[0];
            beanField = tks[1];
        } else {
            beanField = beanFieldSpec;
        }
        this.componentId = componentId;
    }

    public String getKey() {
        return beanId + ":" + beanField; // + ":" + componentId;
    }

    public String toString() {
        return beanId + ":" + beanField + ":" + componentId;
    }
}
