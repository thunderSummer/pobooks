package com.oureda.thunder.pobooks.utils;

import android.util.Log;

/**
 * Created by thunder on 17-5-1.
 */

public class LogUtil  {
    public static final int DEBUG = 2;
    public static final int ERROR = 5;
    public static final int INFO = 3;
    public static final int NOTHING = 6;
    public static final int VERBOSE = 1;
    public static final int WARN = 4;
    public static int level = 1;

    public static void d(String paramString1, String paramString2)
    {
        if (level <= 2) {
            Log.d(paramString1, paramString2);
        }
    }

    public static void e(String paramString1, String paramString2)
    {
        if (level <= 5) {
            Log.e(paramString1, paramString2);
        }
    }

    public static void i(String paramString1, String paramString2)
    {
        if (level <= 3) {
            Log.i(paramString1, paramString2);
        }
    }

    public static void v(String paramString1, String paramString2)
    {
        if (level <= 1) {
            Log.v(paramString1, paramString2);
        }
    }

    public static void w(String paramString1, String paramString2)
    {
        if (level <= 4) {
            Log.w(paramString1, paramString2);
        }
    }
}
