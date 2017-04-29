package com.example.thunder.pobooks.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by thunder on 17-4-28.
 */

public class ToastUtil {
    private static Toast toast;

    public static void ShowToast(Context paramContext, String paramString)
    {
        if (toast == null) {
            toast = Toast.makeText(paramContext, paramString, Toast.LENGTH_SHORT);
        }
        for (;;)
        {
            toast.show();
            return;
            toast.setText(paramString);
        }
    }
}
