package org.androware.flow;

import android.app.Activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.view.WindowManager;
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

    public static class SwipeDetector {

        private static SwipeDetector instance = null;
        public static float DX_THRESHOLD = 20;
        private float swipeThreshold;

        private float touchDownX;
        private float touchDownY;

        public static SwipeDetector inst() {
            return inst(null);
        }

        public static SwipeDetector inst(ContextWrapper contextWrapper) {
            if(instance == null) {
                instance = new SwipeDetector(contextWrapper);
            }
            return instance;
        }

        public SwipeDetector(ContextWrapper contextWrapper) {
            WindowManager wm = (WindowManager) contextWrapper.getSystemService(Context.WINDOW_SERVICE);

            float screenWidth = wm.getDefaultDisplay().getWidth();
            float screenHeight = wm.getDefaultDisplay().getHeight();

            swipeThreshold = (screenHeight>screenWidth)? screenHeight/10: screenWidth/10;
            DX_THRESHOLD = swipeThreshold/2;
        }

        public void setTouchDown(float x, float y) {
            touchDownX = x;
            touchDownY = y;
        }

        public boolean isHorizontalSwipe(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return Math.abs(xdiff) > Math.abs(ydiff) && Math.abs(xdiff) > swipeThreshold;
        }

        public boolean isVerticalSwipe(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return Math.abs(xdiff) < Math.abs(ydiff) && Math.abs(ydiff) > swipeThreshold;
        }

        public boolean isSwipeToRight(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return xdiff < 0 && Math.abs(xdiff) > Math.abs(ydiff) && Math.abs(xdiff) > swipeThreshold;
        }

        public boolean isSwipeToLeft(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return xdiff > 0 && Math.abs(xdiff) > Math.abs(ydiff) && Math.abs(xdiff) > swipeThreshold;
        }

        public boolean isSwipeToTop(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return ydiff > 0 && Math.abs(xdiff) < Math.abs(ydiff) && Math.abs(ydiff) > swipeThreshold;
        }

        public boolean isSwipeToBottom(float x, float y) {
            float xdiff = touchDownX-x;
            float ydiff = touchDownY-y;
            return ydiff < 0 && Math.abs(xdiff) < Math.abs(ydiff) && Math.abs(ydiff) > swipeThreshold;
        }

        public boolean isInRect(RectF rect) {
            return rect.contains(touchDownX, touchDownY);
        }
    }

    public static void centerTextInRect(Canvas canvas, RectF rectF, String text, Paint foreGround, Paint backGround) {
        canvas.drawRect(rectF, backGround);

        Paint resetPaint = null;

        if( text != null) {
            Rect textBounds = getFontBounds(foreGround, text);

            // resize font to make it fit in rectF
            while(textBounds.width() >= (rectF.width() - rectF.width() * 0.2f) ) {
                if(resetPaint == null) {
                    foreGround = resetPaint = new Paint(foreGround);
                }
                float tsize = resetPaint.getTextSize();
                resetPaint.setTextSize( tsize - tsize*0.1f );
                textBounds = getFontBounds(resetPaint, text);
            }

            Paint.FontMetrics fontMetrics = foreGround.getFontMetrics();
            float fontBottom = fontMetrics.bottom;

            canvas.drawText(text, rectF.centerX() - textBounds.width() / 2, rectF.centerY() + fontBottom, foreGround);
        }
    }


    public static Rect getFontBounds(Paint paint, String text) {
        Rect textBounds = new Rect();

        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds;
    }



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
