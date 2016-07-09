package org.androware.flow;


import org.androware.androbeans.utils.ConstructorSpec;

import java.util.List;


/**
 * Created by jkirkley on 5/7/16.
 */
public class AdapterViewSpec {

    public String viewId;
    public String itemLayoutId;
    public boolean useDefault;

    public ConstructorSpec adapterConstructorSpec;

    public List<Object> items;

    public AdapterViewSpec() {
    }


    public Object getDefaultItemSpec(){
        return getItemSpec(0);
    }

    public Object getItemSpec(int position){
        if(useDefault) {
            return items.get(0);
        }
        return items.get(position);
    }


}

