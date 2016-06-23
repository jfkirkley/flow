{
    "layout": "lesson_flow_layout",
    "processor": "FlowActivity",
    "fragmentContainer": "lessonListContainer",

    "startNav": {
        "target": "lessonCategories",
        "anim_in": "slide_in_left",
        "anim_out": "slide_out_right"
    },

    "steps":
    {
        "lessonCategories":        
        {
            "layout": "lesson_catlist_layout",
            "parentContainer": "lessonListContainer",
            "processor": "StepFragment",
            "meta": {
            },
            "ui": {
                "lists": {
                    "lessonList": {
                        "viewId": "lessonCategoryList",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonListAdapter",
                        "items": [
                            { 
                                "label": "Alphabet",
                                "target": "alphabet",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "a.b.c"
                                }
                            },
                            {
                                "label": "Numbers",
                                "target": "numbers",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": ""
                                }
                            },
                            {
                                "label": "Pronouns",
                                "target": "pronouns",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": ""
                                }
                            },
                            {
                                "label": "Family Relations",
                                "target": "family",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": ""
                                }
                            }
                        ]
                    }
                }
            },
            "data": {
            },
            "navMap": {
                "lessonList": {
                    "compName": "lessonCategoryList",
                    "event": "onItemClick"
                }
            }
        },

        "alphabet":        
        {
            "layout": "lesson_catlist_layout",
            "parentContainer": "lessonListContainer",
            "processor": "StepFragment",
            "meta": {
            },
            "ui": {
                "lists": {
                    "lessonList": {
                        "viewId": "lessonCategoryList",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonListAdapter",
                        "items": [
                            { 
                                "label": "Complete Alphabet",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "a.b.c",
                                    "lessonId": "lesson_alphabet",
                                    "studyIndex": 0
                                }
                            },
                            {
                                "label": "Vowels",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "lessonId": "lesson_alphabet",
                                    "studyIndex": 1
                                }
                            },
                            {
                                "label": "Consonants",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "lessonId": "lesson_alphabet",
                                    "studyIndex": 2

                                }
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
            },
            "data": {
            }
        },

        "numbers":        
        {
            "layout": "lesson_catlist_layout",
            "parentContainer": "lessonListContainer",
            "processor": "StepFragment",
            "meta": {
            },
            "ui": {
                "lists": {
                    "lessonList": {
                        "viewId": "lessonCategoryList",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonListAdapter",
                        "items": [
                            { 
                                "label": "1 to 10",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "a.b.c",
                                    "lessonId": "lesson_numbers",
                                    "studyIndex": 0,
                                    "pagerItemLayout": "large_number_display",
                                    "customViewBuilder": "biz.engezy.www.engezy.talk.NumberPagerViewCustomizer"
                                }
                            },
                            {
                                "label": "10 to 20",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "pagerItemLayout": "large_number_display",
                                    "lessonId": "lesson_numbers",
                                    "customViewBuilder": "biz.engezy.www.engezy.talk.NumberPagerViewCustomizer",
                                    "studyIndex": 1
                                }
                            },
                            {
                                "label": "10 to 100",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "lessonId": "lesson_numbers",
                                    "pagerItemLayout": "large_number_display",
                                    "customViewBuilder": "biz.engezy.www.engezy.talk.NumberPagerViewCustomizer",
                                    "studyIndex": 2
                                }
                            },
                            {
                                "label": "100 to 1000",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "lessonId": "lesson_numbers",
                                    "pagerItemLayout": "large_number_display",
                                    "customViewBuilder": "biz.engezy.www.engezy.talk.NumberPagerViewCustomizer",
                                    "studyIndex": 3
                                }
                            },
                            {
                                "label": "Consonants",
                                "target": "startLesson",
                                "labelFieldId": "labelField",
                                "props": {
                                    "image": "",
                                    "lessonId": "lesson_numbers",
                                    "pagerItemLayout": "large_number_display",
                                    "customViewBuilder": "biz.engezy.www.engezy.talk.NumberPagerViewCustomizer",
                                    "studyIndex": 2

                                }
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
            },
            "data": {
            }
        },

        "startLesson":        
        {
            "layout": "start_lesson_layout",
            "parentContainer": "lessonListContainer",
            "processor": "StepFragment",
            "transitionClassName": "biz.engezy.www.engezy.lesson.LessonActivityStepTransition",
            "meta": {
            },
            "ui": {
            },
            "data": {
            },
            "navMap": {
                "listen": {
                    "compName": "listenButton",
                    "event": "onClick"
                },
                "practice": {
                    "compName": "practiceButton",
                    "event": "onClick"
                },
                "test": {
                    "compName": "testButton",
                    "event": "onClick"
                },
                "help": {
                    "compName": "helpButton",
                    "event": "onClick"
                }
            }
        },
        "listen":        
        {
            "processor": "biz.engezy.www.engezy.NewPagerActivity",
            "layout":  "new_activity_playback",
            "ui": {
                "pagers": {

                    "letterPager": 
                    {

                        "viewId": "viewpager",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonTalkTextPagerAdapter",
                        "useDefault": true,

                        "items": [
                            { 
                                "pageLayoutId": "single_letter_display",
                                "props": {
                                    "text": "a",
                                    "text_view": "wordTextView"
                                }
                            }
                        ]
                    }
                }
            },
            "navMap": {
            },
            "data": {
                "mode": "listen"
            }
        },
        "practice":        
        {
            "processor": "biz.engezy.www.engezy.NewPagerActivity",
            "layout":  "new_activity_playback",
            "ui": {
                "pagers": {

                    "letterPager": 
                    {

                        "viewId": "viewpager",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonTalkTextPagerAdapter",
                        "useDefault": true,

                        "items": [
                            { 
                                "pageLayoutId": "single_letter_display",
                                "props": {
                                    "text": "a",
                                    "text_view": "wordTextView"
                                }
                            }
                        ]
                    }
                }
            },
            "navMap": {
            },
            "data": {
                "mode": "practice"
            }
        },
        "test":        
        {
            "processor": "biz.engezy.www.engezy.NewPagerActivity",
            "layout":  "new_activity_playback",
            "ui": {
                "pagers": {

                    "letterPager": 
                    {

                        "viewId": "viewpager",
                        "itemLayoutId": "lesson_catitem_layout",
                        "adapterClass": "biz.engezy.www.engezy.lesson.LessonTalkTextPagerAdapter",
                        "useDefault": true,

                        "items": [
                            { 
                                "pageLayoutId": "single_letter_display",
                                "props": {
                                    "text": "a",
                                    "text_view": "wordTextView"
                                }
                            }
                        ]
                    }
                }
            },
            "navMap": {
            },
            "data": {
                "mode": "test"
            }
        },
        "help":        
        {
            "processor": "biz.engezy.www.engezy.NewPagerActivity",
            "data": {
                "mode": "help"
            }
        },
        "alphabetold":        
        {
            "processor": "biz.engezy.www.engezy.lesson.LessonActivity",

            "transitionClassName": "biz.engezy.www.engezy.lesson.LessonActivityStepTransition",
            "meta": {
            },
            "data": {
            }
        },
        "testSR":        
        {
            "processor": "biz.engezy.www.engezy.TestSRActivity",
            "transitionClassName": "biz.engezy.www.engezy.lesson.LessonActivityStepTransition",
            "meta": {
            },
            "data": {
            }
        },
        "talk":        
        {
            "processor": "biz.engezy.www.engezy.talk.TalkActivity",
            "transitionClassName": "biz.engezy.www.engezy.lesson.LessonActivityStepTransition",
            "meta": {
            },
            "data": {
                "biz.engezy.www.engezy.LessonPrefix": "lesson_0002"
            }
        }

    }
};
