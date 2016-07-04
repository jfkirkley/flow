{
    "layout": "flow_layout",
    "processor": "FlowActivity",
    "fragmentContainer": "fragmentContainer",
    "startNav": {
        "target": "s1"
    },
    "steps":
    {
        "s1":        
        {
            "layout": "model_test",
            "boundMapVariableName": "testWrapper",
            "parentContainer": "fragmentContainer",
            "processor": "org.androware.flow.BoundStepFragment"
        }
    }

};
