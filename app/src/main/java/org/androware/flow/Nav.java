package org.androware.flow;


import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import org.androware.androbeans.utils.FilterLog;
import org.androware.androbeans.utils.ReflectionUtils;
import org.androware.androbeans.utils.ResourceUtils;
import org.androware.androbeans.utils.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * Created by jkirkley on 5/7/16.
 */
public class Nav  {
    public final static String TAG = "nav";

    public String compName;
    public String event;
    public String anim_in;
    public String anim_out;

    public static final String ROOT_VIEW = "__ROOT_VIEW__";

    public static final String GEN_NEXT = "__NEXT__";
    public static final String GEN_PREV = "__PREV__";

    public static Set<String> SPECIAL_NAV_TARGETS = new HashSet<>();

    static {
        SPECIAL_NAV_TARGETS.add(GEN_NEXT);
        SPECIAL_NAV_TARGETS.add(GEN_PREV);
    }

    public String target;

    public boolean isPrev(){
        if(target != null) {
            return target.equals(Nav.GEN_PREV);
        }
        return false;
    }

    public boolean isNext(){
        if(target != null) {
            return target.equals(Nav.GEN_NEXT);
        }
        return false;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public boolean useStepGenerator;

    List items;

    public Nav() {

    }
    public Nav(String target, boolean useStepGenerator) {
        this.target = target;
        this.useStepGenerator = useStepGenerator;
    }

    // construct a nav for specific target
    public Nav(String target) {
        this(target, false);
    }

    // construct a nav to invoke step generator
    public Nav(boolean useStepGenerator) {
        this(null, useStepGenerator);
    }


    public void setAnims(FragmentTransaction fragmentTransaction) {
        if (anim_in != null && anim_out != null) {
            fragmentTransaction.setCustomAnimations(ResourceUtils.getResId("anim", anim_in), ResourceUtils.getResId("anim", anim_out));
        }
    }

    public boolean usesStepGenerator() {
        return useStepGenerator;
    }


    public void setTarget(String target) {
        if(this.target == null || !SPECIAL_NAV_TARGETS.contains(this.target) ) {
            this.target = target;
        }
    }

    public void setNavHandler(View view, final FlowContainerActivity activity, final String target) {


        setTarget(target);
        //l(Constants.TAG, target + " ----------------------------------- >> " + this.target);
        final Nav thisNav = this;
        View navCompView = null;

        if(compName == null) {
            // nothing to do, this nav does not set handlers, (e.g. it is a initNav)
            return;
        }

        if(compName.equals(ROOT_VIEW)) {
            navCompView = view;
        } else {
            navCompView = view.findViewById(ResourceUtils.getResId("id", compName));
        }

        FilterLog.inst().activateTag(TAG);
        if (event.equals("onClick")) {
            navCompView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.loadStep(thisNav);
                }
            });
        } else if (event.equals("onItemClick")) {
            ListView listView = (ListView) navCompView;
            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            ListItemSpec listItemSpec = (ListItemSpec) items.get(position);
                            //l("target: " + listItemSpec.target);
                            thisNav.target = listItemSpec.target;
                            activity.loadStep(thisNav, listItemSpec.props);

                        }

                    });
        } else if (event.equals("onSwipeLeft")) {
            NavOnSwipeListener navOnSwipeListener = (NavOnSwipeListener) Utils.getListener(navCompView, "mOnTouchListener");

            if (navOnSwipeListener == null) {
                navCompView.setOnTouchListener(new NavOnSwipeListener(activity, null, thisNav));
            } else {
                navOnSwipeListener.setLeftNav(thisNav);
            }

        } else if (event.equals("onSwipeRight")) {
            NavOnSwipeListener navOnSwipeListener = (NavOnSwipeListener) Utils.getListener(navCompView, "mOnTouchListener");

            if (navOnSwipeListener == null) {
                navCompView.setOnTouchListener(new NavOnSwipeListener(activity, thisNav, null));
            } else {
                navOnSwipeListener.setRightNav(thisNav);
            }

        }

    }
}

