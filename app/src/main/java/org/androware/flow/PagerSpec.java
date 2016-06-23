package org.androware.flow;

import java.util.Map;


/**
 * Created by jkirkley on 5/19/16.
 */
public class PagerSpec extends ViewSpec {

    public String adapterClass;

    public PagerSpec() {
    }

    public void __get_type_overrides__(Map map) {
        map.put(ItemSpec.class, PagerSpec.class);
    }


}
