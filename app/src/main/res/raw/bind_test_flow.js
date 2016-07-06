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
            "transitionClassName": "org.androware.flow.test_objs.TestBindStepTransition",
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
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "properties": {
                    "objectClassName": "org.androware.flow.test_objs.TestWrapper"
                }
            },
            "parentContainer": "bottomContainer",
            "processor": "org.androware.flow.BoundStepFragment",
            "transitionClassName": "org.androware.flow.test_objs.TestBindStepTransition",

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
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "properties": {
                    "objectClassName": "org.androware.flow.test_objs.TestWrapper"
                }
            },
            "parentContainer": "fragmentContainer",
            "processor": "org.androware.flow.BoundStepFragment",
            "transitionClassName": "org.androware.flow.test_objs.TestBindStepTransition",

            "navMap": {
                "s2": {
                    "compName": "prev",
                    "event": "onClick"
                },
                "save": {
                    "compName": "save",
                    "event": "onClick"
                }

            }
        },
        "save":        
        {
            "layout": "bind_test_saved",
            "objectSaverSpec": {
                "objectSaverClassName": "org.androware.flow.JsonObjectSaver",
                "properties": {
                    "extFilePath": "saveme.txt",
                    "cachedObjectName": "org.androware.flow.test_objs.TestWrapper"
                }
            },
            "transitionClassName": "org.androware.flow.ObjectSaverStepTransition",
            "parentContainer": "fragmentContainer",

            "navMap": {
                "s1": {
                    "compName": "restart",
                    "event": "onClick"
                }
            }
        }
    }

};
