package org.androware.flow.base;

import java.util.HashMap;

import static org.androware.flow.R.id.save;

/**
 * Created by jkirkley on 8/16/16.
 */

public class ObjectSaverSpecBase {

    public final static String TRANSITION_TRIGGER = "TRANSITION";
    public final static String CHANGE_TRIGGER = "CHANGE";
    public final static String FLOW_END_TRIGGER = "FLOW_END";


    public String objectSaverClassName;
    public String objectId;
    public String saveTrigger = TRANSITION_TRIGGER;
    public HashMap<String, Object> properties;
}
