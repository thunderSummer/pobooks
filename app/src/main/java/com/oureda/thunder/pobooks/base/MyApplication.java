package com.oureda.thunder.pobooks.base;

import android.app.Application;
import android.content.Context;

import com.oureda.thunder.pobooks.utils.SharedPreferenceUtil;

import org.litepal.LitePal;

/**
 * Created by thunder on 17-4-28.
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext()
    {
        return context;
    }

    protected void initPrefs()
    {
        SharedPreferenceUtil.init(context, getPackageName() + "_preference");
    }

    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        initPrefs();
    }
}
