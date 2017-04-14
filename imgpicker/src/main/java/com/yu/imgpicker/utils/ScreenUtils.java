package com.yu.imgpicker.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 屏幕显示相关的工具类
 */
public final class ScreenUtils {

    /**
     * 屏幕宽度
     */
    public static int width(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 屏幕高度
     */
    public static int height(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * dp转px
     */
    public static int dp2px(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将px转换成dp
     */
    public static int px2dp(float px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

}
