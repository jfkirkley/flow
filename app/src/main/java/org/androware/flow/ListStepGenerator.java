package org.androware.flow;

import java.util.List;

/**
 * Created by jkirkley on 6/9/16.
 */
public class ListStepGenerator extends EnumeratedStepGenerator {
    List<Step> list;

    public ListStepGenerator(List<Step> list) {
        super(list.size());
        list = list;
    }

    @Override
    protected Step getStepInternal() {
        return list.get(currIndex);
    }
}
