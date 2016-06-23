package org.androware.flow;


import java.util.List;


/**
 * Created by jkirkley on 5/7/16.
 */
public class ViewSpec  {

    public String viewId;
    public String itemLayoutId;
    public boolean useDefault;

    public List<ItemSpec> items;

    public ViewSpec() {
    }


    public ItemSpec getDefaultItemSpec(){
        return getItemSpec(0);
    }

    public ItemSpec getItemSpec(int position){
        if(useDefault) {
            return items.get(0);
        }
        return items.get(position);
    }


}

