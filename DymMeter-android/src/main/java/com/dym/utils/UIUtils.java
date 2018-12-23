package com.dym.utils;

import android.content.Context;

/**
 * UIUtils class
 *
 * @author hangwei
 * @date 2018/12/16
 */
public class UIUtils {
    public static int dp2px(Context context, int dip) {
        if (context == null) {
            return 0;
        }

        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    public static int px2dp(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
