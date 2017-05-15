package com.oureda.thunder.pobooks.utils;

import android.widget.Toast;

import com.oureda.thunder.pobooks.base.MyApplication;

/**
 * Created by thunder on 17-4-28.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showToast(String paramString)
    {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getContext(), paramString, Toast.LENGTH_SHORT);
        }
        else{
            toast.setText(paramString);
        }
        toast.show();
    }
}
