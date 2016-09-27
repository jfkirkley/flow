package org.androware.flow.base;

import java.util.Map;

/**
 * Created by jkirkley on 8/16/16.
 */

public class ObjectLoaderSpecBase {
    public final static String ON_FLOW_INIT = "onFlowInit";
    public final static String ON_PRE_PRE_STEP_TRANS = "onPrePreStepTrans";
    public final static String ON_POST_PRE_STEP_TRANS = "onPostPreStepTrans";
    public final static String ON_PRE_POST_STEP_TRANS = "onPrePostStepTrans";
    public final static String ON_POST_POST_STEP_TRANS = "onPostPostStepTrans";
    public final static String ON_DEMAND = "onDemand";

    public final static String STEP_SCOPE = "stepScope";
    public final static String GLOBAL_SCOPE = "globalScope";
    public final static String FLOW_SCOPE = "flowScope";

    public String objectLoaderClassName;
    public String objectClassName;
    public String objectId;
    public String alias;
    public String when = ObjectLoaderSpecBase.ON_FLOW_INIT;
    public Boolean autoCreate = false;
    public Map<String, Object> properties;

    public String scope = GLOBAL_SCOPE;
}
