package com.example.thunder.pobooks.CustomView;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by thunder on 17-4-26.
 */

public class CombineFAB extends FrameLayout{
    public CombineFAB(@NonNull Context context) {
        super(context);
    }

    public CombineFAB(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CombineFAB(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CombineFAB(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
