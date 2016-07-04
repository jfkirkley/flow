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
            "layout": "bind_test_1",
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.JsonObjectLoader",
                "properties": {
                    "objectClassName": "org.androware.flow.test_objs.TestWrapper",
                    "rawResourceName": "test_wrapper"
                }
            },
            "parentContainer": "fragmentContainer",
            "processor": "org.androware.flow.BoundStepFragment",
            "navMap": {
                "s2": {
                    "compName": "next",
                    "event": "onClick"
                }
            }
        },
        "s2":        
        {
            "layout": "bind_test_2",
            "processor": "StepFragment",
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "properties": {
                    "objectClassName": "org.androware.flow.test_objs.TestWrapper"
                }
            },
            "parentContainer": "bottomContainer",
            "processor": "org.androware.flow.BoundStepFragment",

            "navMap": {
                "s1": {
                    "compName": "prev",
                    "event": "onClick"
                },
                "s3": {
                    "compName": "next",
                    "event": "onClick"
                }
            }
        },
        "s3":        
        {
            "layout": "bind_test_3",
            "processor": "StepFragment",
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "properties": {
                    "objectClassName": "org.androware.flow.test_objs.TestWrapper"
                }
            },
            "parentContainer": "fragmentContainer",
            "processor": "org.androware.flow.BoundStepFragment",

            "navMap": {
                "s2": {
                    "compName": "prev",
                    "event": "onClick"
                }
            }
        }
    }

};
