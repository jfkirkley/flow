package org.androware.flow;

import java.util.Map;


/**
 * Created by jkirkley on 5/7/16.
 */
public class ListSpec extends AdapterViewSpec {


    public String adapterClass;

    public ListSpec() {

    }

    public void __get_type_overrides__(Map map) {
        map.put(ItemSpec.class, ListItemSpec.class);
    }

}

