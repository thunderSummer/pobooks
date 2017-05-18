package com.oureda.thunder.pobooks.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.utils.LogUtil;

/**
 * Created by thunder on 17-5-18.
 */

public class MyFramLayout extends FrameLayout {
    public MyFramLayout(@NonNull Context context) {
        super(context);
    }

    public MyFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF localRectF = new RectF(0.0F, 0.0F, getWidth(), getHeight());
        LogUtil.d(" MyFramLayout ", "onDraw: " + getWidth() + getHeight());
        Paint localPaint = new Paint();
        localPaint.setColor(getResources().getColor(R.color.colorMain));
        localPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(localRectF, 10.0F, 10.0F, localPaint);
        super.onDraw(canvas);
    }
}
