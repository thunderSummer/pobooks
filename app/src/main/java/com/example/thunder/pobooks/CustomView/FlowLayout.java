package com.example.thunder.pobooks.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.thunder.pobooks.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by thunder on 17-4-15.
 */

public class FlowLayout extends ViewGroup {
    private List<ViewBeen> viewBeenList;
    private FlowLayoutOnListener flowLayoutOnListener;
    private int MODE=1;
    private boolean isSlide;
    private int moveX;
    private int moveY;
    private List<View> mChildView;
    private  int startView=-1;
    private int endView;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public void setFlowLayoutOnListener(FlowLayoutOnListener flowLayoutOnListener) {
        this.flowLayoutOnListener = flowLayoutOnListener;
    }

    public FlowLayout(Context context) {
        super(context);
        initMyChildView();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlowLayout,defStyleAttr,0);
        MODE=typedArray.getInt(R.styleable.FlowLayout_layout_mode,1);
        initMyChildView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //exactly 情况上测量孩子的大小
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        //最后的视图的宽高
        int width=0;
        int height=0;
        int lineHeight=0;
        int lineWidth=0;

        View cView;

        int cWidth;
        int cHeight;
        int j = -1;
        if(mChildView.size()==0){
            for(int i=0;i<getChildCount();i++)
            mChildView.add(getChildAt(i));
        }
        int count = mChildView.size();
        viewBeenList=new ArrayList<>();
        for(int i = 0;i<count;i++){
            cView = mChildView.get(i);
            Log.d(TAG, "onMeasure: "+cView.toString());



            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) cView.getLayoutParams();
            cWidth = cView.getMeasuredWidth()+marginLayoutParams.leftMargin+marginLayoutParams.rightMargin;
            cHeight = cView.getMeasuredHeight()+marginLayoutParams.bottomMargin+marginLayoutParams.topMargin;

            ViewBeen viewBeen = new ViewBeen();
            if(i>=j){
                lineWidth=0;

                j=i;
                int temp=0 ;
                lineHeight=0;
                while(temp<widthSize&&j<count){
                    View view = mChildView.get(j);
                    MarginLayoutParams marginLayoutParams1 = (MarginLayoutParams) view.getLayoutParams();
                    temp+=view.getMeasuredWidth()+marginLayoutParams.leftMargin+marginLayoutParams.rightMargin;
                    j++;
                    if(temp>widthSize){
                        break;
                    }
                    int tempHeight=  view.getMeasuredHeight()+marginLayoutParams1.bottomMargin+marginLayoutParams1.topMargin;
                    lineHeight = Math.max(lineHeight,tempHeight);


                }
                height+=lineHeight;
                if(j!=count){
                    j--;
                }

            }
                lineWidth += cWidth;
                width = Math.max(lineWidth, width);

            viewBeen.setLeft(lineWidth-cWidth+marginLayoutParams.leftMargin);
            if (MODE==1){
                viewBeen.setTop(height-cHeight+marginLayoutParams.topMargin);
                viewBeen.setBottom(height-marginLayoutParams.bottomMargin);
            }else{
                viewBeen.setTop(height-lineHeight+marginLayoutParams.topMargin);
                viewBeen.setBottom(viewBeen.getTop()+cHeight-marginLayoutParams.bottomMargin);
            }
            viewBeen.setRight(lineWidth-marginLayoutParams.rightMargin);
            viewBeenList.add(viewBeen);
        }
        Log.d(TAG, "onMeasure: "+heightSize+"  "+height);
        Log.d(TAG, "onMeasure: "+widthSize+"  "+lineWidth);
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i <mChildView.size(); i++) {
            mChildView.get(i).layout(viewBeenList.get(i).getLeft(), viewBeenList.get(i).getTop(), viewBeenList.get(i).getRight(), viewBeenList.get(i).getBottom());
        }
        if(isSlide){
            if(startView!=-1)
            mChildView.get(startView).layout(viewBeenList.get(startView).getLeft()+moveX-startX,viewBeenList.get(startView).getTop()+moveY-startY,viewBeenList.get(startView).getRight()+moveX-startX,viewBeenList.get(startView).getBottom()+moveY-startY);
        }

    }
    private void initMyChildView(){
        mChildView = new ArrayList<>();
    }



    public interface FlowLayoutOnListener{
        void move();
        void click();
        void longClick();
    }
    public  class ViewBeen{
        int left;
        int right;
        int top;
        int bottom;

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getRight() {
            return right;
        }

        public void setRight(int right) {
            this.right = right;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getBottom() {
            return bottom;
        }

        public void setBottom(int bottom) {
            this.bottom = bottom;
        }
    }
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }
    /**
     * 判断是否存在子view包含触摸点
     * @param x
     * @param y
     * @return -1代表不包含
     */
    private int containTouch(int x,int y){
        for(int i=0;i<viewBeenList.size();i++){
            ViewBeen viewBeen = viewBeenList.get(i);
            if(x>viewBeen.getLeft()&&x<viewBeen.getRight()&&y<viewBeen.getBottom()&&y>viewBeen.getTop()){
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) ev.getX();
                startY= (int) ev.getY();
                if ((startView=containTouch(startX,startY))!=-1){
                    return true;
                }else{
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) event.getX();
                startY= (int) event.getY();
                if ((startView=containTouch(startX,startY))!=-1){

                }else{
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isSlide=true;
                moveX= (int) event.getX();
                moveY= (int) event.getY();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                int endView;
                if((endView=containTouch((int)event.getX(),(int)event.getY()))!=-1){
                    View tempView = mChildView.get(endView);
                    if(startView!=-1){
                        mChildView.set(endView,mChildView.get(startView));
                        mChildView.set(startView,tempView);
                        requestLayout();
                    }

                }else{
                    moveX=0;moveY=0;
                    requestLayout();
                }
                isSlide=false;
                break;
        }
        return true;
    }
    public void addView(View view){
        mChildView.add(view);
    }
}
