package org.androware.flow;

import android.app.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.widget.ListAdapter;
import android.widget.ListView;

import org.androware.androbeans.utils.ResourceUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * Created by jkirkley on 5/10/16.
 */
public class GUI_utils {

    public static ViewPager buildPager(Activity activity, PagerSpec pagerSpec, View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(ResourceUtils.getResId("id", pagerSpec.viewId));

        try {
            Class adapterClass = pagerSpec.adapterClass != null ? Class.forName(pagerSpec.adapterClass) : FlowPagerAdapter.class;
            Constructor c = adapterClass.getConstructor(Context.class, PagerSpec.class);
            PagerAdapter pagerAdapter = (PagerAdapter) c.newInstance(activity, pagerSpec);

            viewPager.setAdapter(pagerAdapter);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return viewPager;
    }


    public static ListView buildFlowList(Activity activity, ListSpec listSpec, View view) {

        final ListView listView = (ListView) view.findViewById(ResourceUtils.getResId("id", listSpec.viewId));

        int listItemLayoutId = ResourceUtils.getResId("layout", listSpec.itemLayoutId);

        if (listSpec.adapterClass != null) {
            try {
                Class adapterClass = Class.forName(listSpec.adapterClass);
                Constructor c = adapterClass.getConstructor(Activity.class, int.class, List.class);
                ListAdapter listAdapter = (ListAdapter) c.newInstance(activity, listItemLayoutId, listSpec.items);

                listView.setAdapter(listAdapter);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return listView;

    }


    public static void buildUI(Activity activity, Step step, View view) {
        UI ui = step.ui;

        if( ui != null ) {
            if (ui.lists != null) {
                for (String k : ui.lists.keySet()) {
                    ListSpec listSpec = ui.lists.get(k);
                    buildFlowList(activity, listSpec, view);
                    Nav nav = step.navMap.get(k);
                    nav.setItems(listSpec.items);
                }
            }
            if (ui.pagers != null) {
                for (String k : ui.pagers.keySet()) {
                    PagerSpec pagerspec = ui.pagers.get(k);
                    buildPager(activity, pagerspec, view);
                    //Nav nav = step.navMap.get(k);
                    //nav.setItems(pagerspec.items);
                }
            }
        }
    }

}
