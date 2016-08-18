package org.androware.flow;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.base.PagerSpec;

/**
 * Created by jkirkley on 5/19/16.
 */
public class FlowPagerAdapter extends PagerAdapter {
    protected Context context;
    protected PagerSpec pagerSpec;

    public FlowPagerAdapter() {
    }

    public FlowPagerAdapter(Context context) {
        super();
        this.context = context;
    }

    public FlowPagerAdapter(Context context, PagerSpec pagerSpec) {
        super();
        this.context = context;
        this.pagerSpec = pagerSpec;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);

        PagerItemSpec pagerItemSpec = getPagerItemSpec(position);

        ViewGroup page = (ViewGroup) inflater.inflate(getItemLayoutId(position), container, false);

        return page;
    }

    public PagerItemSpec getPagerItemSpec(int position){
        PagerItemSpec pagerItemSpec = (PagerItemSpec)pagerSpec.getItemSpec(position);
        return pagerItemSpec;
    }

    public int getItemLayoutId(int position) {
        PagerItemSpec pagerItemSpec = getPagerItemSpec(position);

        return ResourceUtils.getResId("layout", pagerItemSpec.pageLayoutId);
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return pagerSpec.items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
