package com.oureda.thunder.pobooks.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.oureda.thunder.pobooks.activity.main.MainActivity;
import com.oureda.thunder.pobooks.utils.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by thunder on 17-5-1.
 */

public class BaseActivity extends AppCompatActivity {

    public static List<Activity> activityList = new ArrayList();
    protected final String TAG = getClass().getSimpleName();
    protected boolean isSetActionBar = true;
    protected boolean isSetStatusBar = true;
    protected View statusBarView = null;

    public static void addActivity(Activity paramActivity)
    {
        activityList.add(paramActivity);
    }

    public static void finishAll()
    {
        Iterator localIterator = activityList.iterator();
        while (localIterator.hasNext())
        {
            Activity localActivity = (Activity)localIterator.next();
            if (!localActivity.isFinishing()) {
                localActivity.finish();
            }
        }
    }

    public static void finishLast()
    {
        Activity localActivity = (Activity)activityList.get(activityList.size() - 1);
        if ((!localActivity.isFinishing()) && (localActivity.getClass() != MainActivity.class)) {
            localActivity.finish();
        }
    }

    public static void removeActivity(Activity paramActivity)
    {
        activityList.remove(paramActivity);
    }

//    protected void exitTrue()
//    {
//        final MyDialog localMyDialog = new MyDialog();
//        localMyDialog.initAll("������������������������", "������", "������������", "������", new MyDialog.ButtonListener()
//        {
//            public void negativeListener()
//            {
//                localMyDialog.dismiss();
//            }
//
//            public void positiveListener() {}
//        });
//        localMyDialog.show(getFragmentManager(), "exit");
//    }

    protected void gone(View... paramVarArgs)
    {
        if ((paramVarArgs != null) && (paramVarArgs.length > 0))
        {
            int j = paramVarArgs.length;
            for (int i = 0; i < j; i++)
            {
                View localView = paramVarArgs[i];
                if (localView != null) {
                    localView.setVisibility(View.GONE);
                }
            }
        }
    }


    protected void initToolbar(int toolbar, String paramString, int homeId)
    {
        Toolbar localToolbar = (Toolbar)findViewById(toolbar);
        localToolbar.setTitle(paramString);
        setSupportActionBar(localToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(homeId);
        }
    }

    protected boolean isVisible(View paramView)
    {
        if (paramView.getVisibility() == View.VISIBLE){
            return true;
        }else{
            return false;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        addActivity(this);
        LogUtil.d(this.TAG, "-------->Create");
    }

    protected void onDestroy()
    {
        super.onDestroy();
        LogUtil.d(this.TAG, "-------->Destroy");
        removeActivity(this);
    }

    protected void onPause()
    {
        super.onPause();
        LogUtil.d(this.TAG, "-------->Pause");
    }

    protected void onRestart()
    {
        super.onRestart();
        LogUtil.d(this.TAG, "-------->Restart");
    }

    protected void onStart()
    {
        super.onStart();
        LogUtil.d(this.TAG, "-------->Start");
    }

    protected void onStop()
    {
        super.onStop();
        LogUtil.d(this.TAG, "-------->Stop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(this.TAG, "-------->Resume");
    }

    protected void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if(statusBarView != null){
            statusBarView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    protected void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        if(statusBarView != null){
            statusBarView.setBackgroundColor(0);
        }
    }

    protected void visible(View... paramVarArgs)
    {
        if ((paramVarArgs != null) && (paramVarArgs.length > 0))
        {
            int j = paramVarArgs.length;
            for (int i = 0; i < j; i++)
            {
                View localView = paramVarArgs[i];
                if (localView != null) {
                    localView.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
