{
    "layout": "flow_layout",
    "processor": "FlowActivity",
    "fragmentContainer": "fragmentContainer",
    "firstStep": "s1",
    "steps":
    {
        "s1":        
        {
            "layout": "new_activity_playback",

            "parentContainer": "fragmentContainer",

            "processor": "StepFragment",
            "meta": {
                "m1": 129
            },
            "data": {
                "d1": 3129
            },
            "ui": {
                "pagers": {
                    "testPager": {

                        "viewId": "viewpager",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.talk.TalkTextPagerAdapter",

                        "items": [
                            { 
                                "props": {
                                    "text": "a"
                                }
                            },
                            { 
                                "props": {
                                    "text": "b"
                                }
                            },
                            { 
                                "props": {
                                    "text": "c"
                                }
                            },
                            { 
                                "props": {
                                    "text": "d"
                                }
                            }
                        ]
                    }
                }
            },
            "navMap": {
                
            }
        }
    }
};
