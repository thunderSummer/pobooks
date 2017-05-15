package com.oureda.thunder.pobooks.utils;

import android.content.Context;

import com.oureda.thunder.pobooks.base.MyApplication;

/**
 * Created by thunder on 17-5-2.
 * 获取屏幕宽度和高度
 * 转化 px dp sp
 */

public class ScreenUtils {
    public static float dpToPx(float dp)
    {
        return MyApplication.getContext().getResources().getDisplayMetrics().density * dp;
    }

    public static int dpToPxInt(float dp)
    {
        return (int)(dpToPx(dp) + 0.5F);
    }

    public static int getScreenHeight()
    {
        return MyApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth()
    {
        return MyApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getStatusBarHeight(Context paramContext)
    {
        int i = 0;
        int j = paramContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (j > 0) {
            i = paramContext.getResources().getDimensionPixelSize(j);
        }
        return i;
    }

    public static float pxToDp(float px)
    {
        return px / MyApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static int pxToDpInt(float px)
    {
        return (int)(pxToDp(px) + 0.5F);
    }
    public static float pxToSp( float px) {
        return px/MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
    }
    public static int pxToSpInt(float px){
        return (int)(pxToSp(px)+0.5f);
    }
    public static float spToPx(float sp) {
        return sp*MyApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
    }
    public static int  spToPxInt(float sp){
        return (int) (spToPx(sp)+0.5f);
    }

}
