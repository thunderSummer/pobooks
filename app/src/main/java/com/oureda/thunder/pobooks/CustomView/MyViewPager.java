package com.oureda.thunder.pobooks.CustomView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.ColorChange;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ScreenUtils;

import java.util.List;

/**
 * Created by thunder on 17-5-13.
 */

public class MyViewPager extends ViewPager {
    private static String TAG="MyViewPager";

        private int bmpW;
        private ColorChange colorChange;
        private int current;
        private List<Fragment> fragmentList;
        private FragmentManager fragmentManager;
        private int imageCount;
        private int imageId;
        private ImageView imageView;
        public MyOnPageChangeListener listener;
        public MyAdapter myAdapter;
        private int offset;
        private View view;
        private int viewPageId;

        public MyViewPager(Context context, AttributeSet attributeSet)
        {
            super(context, attributeSet);
        }

    private void initImage() {
        this.imageView = ((ImageView) this.view.findViewById(this.imageId));
        if (this.imageCount == 2) {
            this.bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle2).getWidth();
        } else if (this.imageCount == 3) {
            this.bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle3).getWidth();
        } else if (this.imageCount == 4) {
            this.bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle4).getWidth();
        } else {
            this.bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.rectangle5).getWidth();
        }
        this.offset = ((ScreenUtils.getScreenWidth() / this.imageCount - this.bmpW) / 2);
        Matrix localMatrix = new Matrix();
        localMatrix.postTranslate(this.offset, 0.0F);
        imageView.setImageMatrix(localMatrix);
        return;
    }


        public int getBmpW()
        {
            return this.bmpW;
        }

        public int getCurrent()
        {
            return this.current;
        }

        public int getOffset()
        {
            return this.offset;
        }

        public void initAll(int imageCount, int imageId, List<Fragment> fragmentList, FragmentManager fragmentManager)
        {
            this.imageCount = imageCount;
            this.imageId = imageId;
            this.fragmentList = fragmentList;
            this.fragmentManager = fragmentManager;
            this.myAdapter = new MyAdapter(fragmentManager, fragmentList);
            initImage();
            this.listener = new MyOnPageChangeListener();
        }

        public void initAll(int paramInt1, int paramInt2, List<Fragment> paramList, FragmentManager paramFragmentManager, View paramView)
        {
            this.imageCount = paramInt1;
            this.imageId = paramInt2;
            this.fragmentList = paramList;
            this.fragmentManager = paramFragmentManager;
            this.myAdapter = new MyAdapter(paramFragmentManager, paramList);
            this.view = paramView;
            initImage();
            this.listener = new MyOnPageChangeListener();
        }

        public void setAdapter(PagerAdapter pagerAdapter)

        {
            super.setAdapter(pagerAdapter);
        }

        public void setColorChange(ColorChange paramColorChange)
        {
            this.colorChange = paramColorChange;
        }

        class MyAdapter extends FragmentPagerAdapter
        {
            List<Fragment> fragmentList;
            FragmentManager fragmentManager;

            public MyAdapter(FragmentManager fm,List<Fragment> fragmentList) {
                super(fm);
                this.fragmentList=fragmentList;
                this.fragmentManager=fm;

            }


            public int getCount()
            {
                return this.fragmentList.size();
            }

            public Fragment getItem(int paramInt)
            {
                return fragmentList.get(paramInt);
            }
        }

        private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
        {
            int one =offset * 2 + bmpW;

            private MyOnPageChangeListener() {}

            public void onPageScrollStateChanged(int paramInt) {}

            public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}

            public void onPageSelected(int paramInt)
            {
                TranslateAnimation localTranslateAnimation = new TranslateAnimation(this.one * current, this.one * paramInt, 0.0F, 0.0F);
                setCurrentItem(paramInt);
                LogUtil.d(TAG,"the current img == "+current);
                localTranslateAnimation.setFillAfter(true);
                localTranslateAnimation.setDuration(300L);
                imageView.startAnimation(localTranslateAnimation);
                colorChange.OnColorChange();
            }
        }

}
