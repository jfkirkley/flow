package org.androware.flow.base;

import org.androware.flow.Step;
import org.androware.flow.binding.BeanBinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jkirkley on 12/18/16.
 */

public class BeanListFieldItemGenerator implements  ItemGenerator {
    private String beanId;
    private String fieldName;
    public BeanListFieldItemGenerator(String beanId, String fieldName) {
        this.beanId = beanId;
        this.fieldName = fieldName;
    }

    @Override
    public List getItems(Object spec) {
        if(spec instanceof Step) {
            Step step = (Step)spec;
            BeanBinder beanBinder = step.getBeanBinder(beanId);
            Object target = beanBinder.get(fieldName);
            if(target instanceof Map) {
                return new ArrayList(((Map)target).values());
            }
            return (List) beanBinder.get(fieldName);
        }
        return null;
    }
}
