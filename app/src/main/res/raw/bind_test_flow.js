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
            "layout": "mybind_test_1",
            "twoWayMapper": {
                "componentId2BeanFieldMap": {
                    "s1t1": "b1.v1",
                    "s1t2": "b1.v2"
                }
            },
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.JsonObjectLoader",
                "objectClassName": "org.androware.flow.test_objs.TestWrapper",
                "objectId": "b1",
                "properties": {
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
            "layout": "mybind_test_2",

            "ui": {
                "adapterViews": {
                    "planets_spinner": {
                        "viewId": "planets_spinner",
                        "itemLayoutId":  "simple_spinner_item",
                        "adapterConstructorSpec": {
                            "targetClassName": "android.widget.ArrayAdapter",
                            "paramClassNames": [  "android.content.Context", "int", "java.util.List"],
                            "paramObjects": ["__plugin__context", "android.R.layout.simple_spinner_item", "__plugin__items" ]
                        },
                        "items": [
                            "Mercury",
                            "Venus",
                            "Earth",
                            "Mars",
                            "Jupiter",
                            "Saturn",
                            "Uranus",
                            "Neptune",
                            "Pluto"
                        ]
                    }
                }
                
            },
            "twoWayMapper": {
                "componentId2BeanFieldMap": {
                    "s2t1": "b1.v2",
                    "planets_spinner": "b1.v3"
                }
            },
            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "objectId": "b1"
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
            "layout": "mybind_test_3",
            "twoWayMapper": {
                "componentId2BeanFieldMap": {
                    "s3t1": "b1.v4",
                    "s3t2": "b1.v5"
                }
            },

            "objectLoaderSpec": {
                "objectLoaderClassName": "org.androware.flow.CachedObjectLoader",
                "objectId": "b1"
            },

            "parentContainer": "fragmentContainer",
            "processor": "org.androware.flow.BoundStepFragment",

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
                "objectId": "b1",
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
