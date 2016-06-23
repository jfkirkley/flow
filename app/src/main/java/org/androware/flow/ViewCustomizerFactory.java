package org.androware.flow;

import org.androware.androbeans.utils.ReflectionUtils;

/**
 * Created by jkirkley on 6/2/16.
 */
public class ViewCustomizerFactory {
    private static ViewCustomizerFactory ourInstance = new ViewCustomizerFactory();

    public static ViewCustomizerFactory getInstance() {
        return ourInstance;
    }

    private ViewCustomizerFactory() {
    }

    public ViewCustomizer buildViewCustomizer(Step step, String customizerClassName) {

        if (customizerClassName != null) {

            Class customizerClass = ReflectionUtils.getClass(customizerClassName);

            ViewCustomizer customizer = (ViewCustomizer) ReflectionUtils.newInstance(customizerClass);

            return customizer;
        }

        return null;
    }
}
