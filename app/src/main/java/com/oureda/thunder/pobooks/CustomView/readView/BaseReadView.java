package com.oureda.thunder.pobooks.CustomView.readView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.listener.OnBookStatusChangeListen;
import com.oureda.thunder.pobooks.manager.SettingManager;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ScreenUtils;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.util.List;

/**
 * Created by thunder on 17-5-9.
 */

public abstract class BaseReadView extends View {
    private static String TAG="BaseReadView---->>";
    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值

    protected Bitmap mCurrentPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pageFactory = null;

    protected OnBookStatusChangeListen listener;
    protected String bookId;
    public boolean isPrepared = false;



    private boolean isFromSD=false;


    Scroller mScroller;

    public BaseReadView(Context context, String bookId, List<ChapterInfo> chaptersList,
                        OnBookStatusChangeListen listener) {
        super(context);
        this.listener = listener;
        this.bookId = bookId;

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mCurrentPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        pageFactory = new PageFactory(bookId,mScreenWidth,mScreenHeight,listener,chaptersList);
        pageFactory.setBookStatusChangeListener(listener);
    }

    public synchronized void init(int theme) {
        pageFactory.setIsFromSd(isFromSD);
        if (!isPrepared) {
            try {
               // pageFactory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
                // 自动跳转到上次阅读位置
                int pos[] = SettingManager.getInstance().getReadProgress(bookId);
                boolean ret = pageFactory.openBook(pos[0], new int[]{pos[1], pos[2]});
                LogUtil.d(TAG+"init","上次阅读位置：chapter=" + pos[0] + " startPos=" + pos[1] + " endPos=" + pos[2]);
                if (!ret) {
                    listener.onLoadChapterFailure(pos[0]);
                    return;
                }
                pageFactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            } catch (Exception e) {
            }
            isPrepared = true;
        }
    }

    private int dx, dy;
    private long et = 0;
    private boolean cancel = false;
    private boolean center = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                et = System.currentTimeMillis();
                dx = (int) e.getX();
                dy = (int) e.getY();
                mTouch.x = dx;
                mTouch.y = dy;
                actiondownX = dx;
                actiondownY = dy;
                touch_down = 0;
                pageFactory.onDraw(mCurrentPageCanvas);
                if (actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3) {
                    center = true;
                } else {
                    center = false;
                    calcCornerXY(actiondownX, actiondownY);
                    if (actiondownX < mScreenWidth / 2) {// 从左翻
                        BookStatus status = pageFactory.prePage();
                        if (status == BookStatus.NO_PRE_PAGE) {
                            ToastUtil.showToast("没有上一页啦");
                            return false;
                        } else if (status == BookStatus.LOAD_SUCCESS) {
                            abortAnimation();
                            pageFactory.onDraw(mNextPageCanvas);
                        } else {
                            return false;
                        }
                    } else if (actiondownX >= mScreenWidth / 2) {// 从右翻
                        BookStatus status = pageFactory.nextPage();
                        if (status == BookStatus.NO_NEXT_PAGE) {
                            ToastUtil.showToast("没有下一页啦");
                            return false;
                        } else if (status == BookStatus.LOAD_SUCCESS) {
                            abortAnimation();
                            pageFactory.onDraw(mNextPageCanvas);
                        } else {
                            return false;
                        }
                    }
                    listener.onFlip();
                    setBitmaps(mCurrentPageBitmap, mNextPageBitmap);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (center)
                    break;
                int mx = (int) e.getX();
                int my = (int) e.getY();
                cancel = (actiondownX < mScreenWidth / 2 && mx < mTouch.x) || (actiondownX > mScreenWidth / 2 && mx > mTouch.x);
                mTouch.x = mx;
                mTouch.y = my;
                touch_down = mTouch.x - actiondownX;
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                long t = System.currentTimeMillis();
                int ux = (int) e.getX();
                int uy = (int) e.getY();

                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
                    resetTouchPoint();
                    if (Math.abs(ux - actiondownX) < 5 && Math.abs(uy - actiondownY) < 5) {
                        listener.onCenterClick();
                        return false;
                    }
                    break;
                }

                if ((Math.abs(ux - dx) < 10) && (Math.abs(uy - dy) < 10)) {
                    if ((t - et < 1000)) { // 单击
                        startAnimation();
                    } else { // 长按
                        pageFactory.cancelPage();
                        restoreAnimation();
                    }
                    postInvalidate();
                    return true;
                }
                if (cancel) {
                    pageFactory.cancelPage();
                    restoreAnimation();
                    postInvalidate();
                } else {
                    startAnimation();
                    postInvalidate();
                }
                cancel = false;
                center = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();

    protected abstract void calcCornerXY(float x, float y);

    /**
     * 开启翻页
     */
    protected abstract void startAnimation();

    /**
     * 停止翻页动画（滑到一半调用停止的话  翻页效果会卡住 可调用#{restoreAnimation} 还原效果）
     */
    protected abstract void abortAnimation();

    /**
     * 还原翻页
     */
    protected abstract void restoreAnimation();

    protected abstract void setBitmaps(Bitmap mCurrentPageBitmap, Bitmap mNextPageBitmap);

    public abstract void setTheme(int theme);

    /**
     * 复位触摸点位
     */
    protected void resetTouchPoint() {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        touch_down = 0;
        calcCornerXY(mTouch.x, mTouch.y);
    }

    public void jumpToChapter(int chapter) {
        resetTouchPoint();
        pageFactory.openBook(chapter, new int[]{0, 0});
        pageFactory.onDraw(mCurrentPageCanvas);
        pageFactory.onDraw(mNextPageCanvas);
        postInvalidate();
    }

    public void nextPage() {
        BookStatus status = pageFactory.nextPage();
        if (status == BookStatus.NO_NEXT_PAGE) {
            ToastUtil.showToast("没有下一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pageFactory.onDraw(mCurrentPageCanvas);
                pageFactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }

    }

    public void prePage() {
        BookStatus status = pageFactory.prePage();
        if (status == BookStatus.NO_PRE_PAGE) {
            ToastUtil.showToast("没有上一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pageFactory.onDraw(mCurrentPageCanvas);
                pageFactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }
    }
    public synchronized void setTextColor(int textColor, int titleColor) {
        resetTouchPoint();
        pageFactory.setTextColor(textColor, titleColor);
        if (isPrepared) {
            pageFactory.onDraw(mCurrentPageCanvas);
            pageFactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public void setBattery(int battery) {
        pageFactory.setBattery(battery);
        if (isPrepared) {
            pageFactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        }
    }

    public void setTime(String time) {
        pageFactory.setTime(time);
    }

    public void setPosition(int[] pos) {
        boolean ret = pageFactory.openBook(pos[0], new int[]{pos[1], pos[2]});
        if (!ret) {
            listener.onLoadChapterFailure(pos[0]);
            return;
        }
        pageFactory.onDraw(mCurrentPageCanvas);
        postInvalidate();
    }

    public int[] getReadPos() {
        return pageFactory.getPosition();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pageFactory != null) {
            pageFactory.recycle();
        }

        if (mCurrentPageBitmap != null && !mCurrentPageBitmap.isRecycled()) {
            mCurrentPageBitmap.recycle();
            mCurrentPageBitmap = null;
            LogUtil.d(TAG,"mCurrentPageBitmap recycle");
        }

        if (mNextPageBitmap != null && !mNextPageBitmap.isRecycled()) {
            mNextPageBitmap.recycle();
            mNextPageBitmap = null;
            LogUtil.d(TAG,"mNextPageBitmap recycle");
        }
    }
    public boolean isFromSD() {
        return isFromSD;
    }

    public void setFromSD(boolean fromSD) {
        isFromSD = fromSD;
    }
    public synchronized void setSize(int paramInt1, int paramInt2)
    {

            resetTouchPoint();
            this.pageFactory.setFontSize(paramInt1);
            this.pageFactory.setMLineSpace(paramInt2);
            if (this.isPrepared){
                this.pageFactory.onDraw(this.mCurrentPageCanvas);
                this.pageFactory.onDraw(this.mNextPageCanvas);
                SettingManager.getInstance().saveFontSize(paramInt1);
                SettingManager.getInstance().savePaddingSize(paramInt2);
                LogUtil.d("sdsd", "sdafsfd");
                postInvalidate();
            }
            return;
    }
    public synchronized void setReadColor(int readColor)
    {
        this.pageFactory.setColor(readColor);
        Log.d(TAG, "setReadColor: "+isPrepared);
        if (this.isPrepared)
        {
            this.pageFactory.onDraw(this.mCurrentPageCanvas);
            this.pageFactory.onDraw(this.mNextPageCanvas);
            postInvalidate();
        }
    }

}
