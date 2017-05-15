package com.oureda.thunder.pobooks.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.oureda.thunder.pobooks.R;

import static android.content.ContentValues.TAG;

/**
 * Created by thunder on 17-5-14.
 */

public class WaitView extends View {
    private int count;
    private Bitmap bitmap;
    private int pictureId;
    private float radius;
    private int mWidth;
    private int mHeight;
    private int pWidth;
    private int pHeight;
    private Paint paint;
    private Bitmap upBitmap;
    private int current;
    private Handler mHandler;
    public WaitView(Context context) {
        super(context);
    }

    public WaitView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public WaitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs!=null){
            TypedArray typedArray =context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaitView,defStyleAttr,0);
            count=typedArray.getInteger(R.styleable.WaitView_numberWait,8);
            pictureId=typedArray.getInteger(R.styleable.WaitView_pictureWait,R.drawable.wait_gray);
            radius=typedArray.getDimension(R.styleable.WaitView_radiusWait,40);
            paint=new Paint();
            bitmap = BitmapFactory.decodeResource(getResources(),pictureId);
            mHandler=new Handler(){

                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case 1:
                            if(current>=0&&current<count){
                                postInvalidate();
                                current++;
                                if(current==count){
                                    current=0;
                                }
                                sendEmptyMessageDelayed(1,500);
                            }
                            break;
                    }

                }
            };
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        float angle = 360/count;

        upBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.wait_black);
        pHeight=bitmap.getHeight();
        pWidth=bitmap.getWidth();
        Log.d(TAG, "onDraw: "+current);
        for(int i=0;i<count;i++) {
            canvas.rotate(angle);
            if (i == current) {
                canvas.drawBitmap(upBitmap,-pWidth/2,-40,paint);
            } else {
                canvas.drawBitmap(bitmap, -pWidth / 2, -40, paint);
            }
        }
    }
    public void start(){
        current=0;
        Message message =new Message();
        message.what=1;
        mHandler.sendMessage(message);
    }
    public void stop(){
        current=-1;
    }
}
