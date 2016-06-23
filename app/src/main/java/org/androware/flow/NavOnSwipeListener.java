package org.androware.flow;


import org.androware.androbeans.utils.FilterLog;

/**
 * Created by jkirkley on 6/7/16.
 */
public class NavOnSwipeListener extends OnSwipeListener {
    FlowContainerActivity activity;

    Nav rightNav;
    Nav leftNav;

    public NavOnSwipeListener(FlowContainerActivity activity, Nav rightNav, Nav leftNav) {
        super(activity);
        this.activity = activity;
        this.rightNav = rightNav;
        this.leftNav = leftNav;
        l(toString());
    }

    @Override
    public void onSwipeLeft() {
        if(leftNav != null) {
            l(toString());

            activity.loadStep(leftNav);
        }
    }

    @Override
    public void onSwipeRight() {
        if(rightNav != null) {
            l(toString());
            activity.loadStep(rightNav);
        }
    }

    public void setRightNav(Nav rightNav) {
        this.rightNav = rightNav;
        l(toString());
    }

    public void setLeftNav(Nav leftNav) {
        this.leftNav = leftNav;
        l(toString());
    }


    public String toString() {
        return  leftNav + " <> " + rightNav;
    }

    public void l(String s) {
        FilterLog.inst().log(Constants.TAG, s);
    }


}
