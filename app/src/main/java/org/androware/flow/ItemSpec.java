package org.androware.flow;


import java.util.HashMap;



/**
 * Created by jkirkley on 5/7/16.
 */
public class ItemSpec  {

    public HashMap<String,Object> props;

    public ItemSpec() {
    }

    public String getProp(String key) {
        return (String)props.get(key);
    }

    public boolean hasProp(String key) {
        return props.containsKey(key);
    }
}

