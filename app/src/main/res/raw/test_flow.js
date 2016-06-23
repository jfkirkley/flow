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
            "layout": "lesson_catlist_layout",

            "parentContainer": "fragmentContainer",

            "processor": "StepFragment",
            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "ui": {
                "lists": {
                    "lessonList": {
                        "viewId": "lessonCategoryList",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "org.androware.flow.TestListAdapter",
                        "items": [
                            { 
                                "label": "Alphabet",
                                "target": "s2",
                                "labelFieldId": "labelField"
                            },

                            {
                                "label": "Numbers",
                                "target": "s3",
                                "labelFieldId": "labelField"
                            },

                            {
                                "label": "Nouns",
                                "target": "s4",
                                "labelFieldId": "labelField"
                            }
                        ]
                    }
                }
            },

            "navMap": {
                "lessonList": {
                    "compName": "lessonCategoryList",
                    "event": "onItemClick"
                }
            }
        },
        "s2":        
        {
            "layout": "flow_step2_layout",
            "processor": "StepFragment",
            "parentContainer": "bottomContainer",
            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "navMap": {
                "s1": {
                    "compName": "button",
                    "event": "onClick"
                }
            }
        },
        "s4":        
        {
            "layout": "flow_step2_layout",
            "processor": "StepFragment",
            "parentContainer": "fragmentContainer",
            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "navMap": {
                "s1": {
                    "compName": "button",
                    "event": "onClick"
                }
            }
        },
        "s5":        
        {
            "layout": "flow_step2_layout",
            "processor": "StepFragment",
            "parentContainer": "bottomContainer",
            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "navMap": {
                "s1": {
                    "compName": "button",
                    "event": "onClick"
                }
            }
        },
        "s3":        
        {
            "layout": "flow_step3_layout",
            "processor": "StepFragment",
            "parentContainer": "fragmentContainer",

            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "navMap": {
                "s1": {
                    "compName": "button",
                    "event": "onClick"
                }
            }
        }
    }

};
