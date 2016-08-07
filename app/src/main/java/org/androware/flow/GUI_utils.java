package org.androware.flow;

import android.app.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.widget.Adapter;
import android.widget.AdapterView;
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

    public static int getAdapterItemPosition(Adapter adapter, Object item){
        for( int i = 0; i < adapter.getCount(); ++i) {
            if(adapter.getItem(i).equals( item ) ) {
                return i;
            }
        }
        return -1;
    }

    public static void setAdapterViewSelectedItem(AdapterView adapterView, Object item){
        int i = getAdapterItemPosition(adapterView.getAdapter(), item);
        if( i != -1 ) {
            adapterView.setSelection(i);
        }
    }

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


    public static AdapterView buildAdapterView(Activity activity, AdapterViewSpec adapterViewSpec, View view) {

        final AdapterView adapterView = (AdapterView) view.findViewById(ResourceUtils.getResId("id", adapterViewSpec.viewId));

        if ( adapterViewSpec.adapterConstructorSpec != null ) {  // ESGAAAAAAAxdz

            adapterViewSpec.adapterConstructorSpec.plugInValue(activity, "context");
            adapterViewSpec.adapterConstructorSpec.plugInValue(adapterViewSpec.getItems(null), "items");

            Adapter adapter = (Adapter)adapterViewSpec.adapterConstructorSpec.build();

            adapterView.setAdapter(adapter);
        }

        return adapterView;

    }


    public static void buildUI(Activity activity, Step step, View view) {
        UI ui = step.ui;

        if( ui != null ) {
            if (ui.adapterViews != null) {
                for (String k : ui.adapterViews.keySet()) {
                    AdapterViewSpec adapterViewSpec = ui.adapterViews.get(k);
                    buildAdapterView(activity, adapterViewSpec, view);

                    if(step.navMap != null && step.navMap.containsKey(k)) {
                        Nav nav = step.navMap.get(k);
                        nav.setItems(adapterViewSpec.getItems(null));
                    }
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
