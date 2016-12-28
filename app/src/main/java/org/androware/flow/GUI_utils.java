package org.androware.flow;

import android.app.Activity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.widget.Adapter;
import android.widget.AdapterView;

import org.androware.androbeans.utils.ResourceUtils;
import org.androware.flow.base.AdapterViewSpec;
import org.androware.flow.base.PagerSpec;
import org.androware.flow.base.UI;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jkirkley on 5/10/16.
 */
public class GUI_utils {

    public abstract static class AdapterItemWrapper {
        Object item;

        public AdapterItemWrapper(Object item) {
            this.item = item;
        }

        public boolean equals(Object otherItem) {
            return item.equals(otherItem);
        }

        public abstract void setSelected(AdapterView adapterView, int position, boolean selected);
        public abstract AdapterItemWrapper newInstance(Object item);
    }

    public static List getAdapterItems(Adapter adapter) {
        List list = new ArrayList();
        for (int i = 0; i < adapter.getCount(); ++i) {
            list.add(adapter.getItem(i));
        }
        return list;
    }

    public static int getAdapterItemPosition(Adapter adapter, Object item) {
        for (int i = 0; i < adapter.getCount(); ++i) {
            if (item.equals(adapter.getItem(i))) {
                return i;
            }
        }
        return -1;
    }

    public static void setAdapterViewSelectedItem(AdapterView adapterView, Object item) {
        AdapterItemWrapper adapterItemWrapper = (item instanceof AdapterItemWrapper) ? (AdapterItemWrapper) item : null;
        //item = adapterItemWrapper != null? adapterItemWrapper

        int i = getAdapterItemPosition(adapterView.getAdapter(), item);
        if (i != -1) {
            if (adapterItemWrapper == null) {
                adapterView.setSelection(i);
            } else {
                adapterItemWrapper.setSelected(adapterView, i, true);
            }
            //adapterView.get
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


    public static AdapterView buildAdapterView(Activity activity, AdapterViewSpec adapterViewSpec, View view, Step step) {

        final AdapterView adapterView = (AdapterView) view.findViewById(ResourceUtils.getResId("id", adapterViewSpec.viewId));

        if (adapterViewSpec.adapterConstructorSpec != null) {  // ESGAAAAAAAxdz

            adapterViewSpec.adapterConstructorSpec.plugInValue(activity, "context");
            adapterViewSpec.adapterConstructorSpec.plugInValue(adapterViewSpec.getItems(step), "items");
            adapterViewSpec.adapterConstructorSpec.plugInValue(step, "step");
            adapterViewSpec.adapterConstructorSpec.plugInValue(adapterViewSpec, "adapter_spec");

            Adapter adapter = (Adapter) adapterViewSpec.adapterConstructorSpec.build();

            adapterView.setAdapter(adapter);

        }

        return adapterView;

    }


    public static void buildUI(Activity activity, Step step, View view) {
        UI ui = step.ui;

        if (ui != null) {
            if (ui.adapterViews != null) {
                for (String k : ui.adapterViews.keySet()) {
                    AdapterViewSpec adapterViewSpec = ui.adapterViews.get(k);
                    AdapterView adapterView = buildAdapterView(activity, adapterViewSpec, view, step);

                    if (step.navMap != null && step.navMap.containsKey(k)) {
                        Nav nav = (Nav) step.navMap.get(k);
                        nav.setItems(getAdapterItems(adapterView.getAdapter()));
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
