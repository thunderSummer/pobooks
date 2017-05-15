package com.oureda.thunder.pobooks.CustomView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.oureda.thunder.pobooks.activity.main.ScanLocalBook;
import com.oureda.thunder.pobooks.activity.person.FeelReadEditActivity;
import com.oureda.thunder.pobooks.R;

/**
 * Created by thunder on 17-4-26.
 */

public class CombineFAB extends FrameLayout{
    private FloatingActionButton baseBar;
    private Context context;
    private boolean isOpen = false;
    private FloatingActionButton middleBar;
    private FloatingActionButton topBar;
    private float y;
    public CombineFAB(@NonNull Context context) {
        super(context);
    }

    public CombineFAB(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.fab_view,this);
        this.baseBar= (FloatingActionButton) findViewById(R.id.the_base_fab);
        this.middleBar= (FloatingActionButton) findViewById(R.id.the_middle_fab);
        this.topBar= (FloatingActionButton) findViewById(R.id.the_top_fab);
        baseBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    stopAnimation();
                    isOpen=false;
                }else{
                    startAnimation();
                    isOpen=true;
                }
            }
        });
        topBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FeelReadEditActivity.class));
            }
        });
        middleBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ScanLocalBook.class));
            }
        });
    }
    private void changeVisible(){
        if(topBar.getVisibility()==View.VISIBLE){
            topBar.setVisibility(View.GONE);
            middleBar.setVisibility(View.GONE);
        }else{
            topBar.setVisibility(View.VISIBLE);
            middleBar.setVisibility(View.VISIBLE);
        }
    }
    private void startAnimation(){
        Object localObject1 = ObjectAnimator.ofFloat(this.middleBar, "translationY", new float[] { this.y, this.y - 200.0F });
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this.middleBar, "alpha", new float[] { 1.0F, 0.0F, 1.0F });
        Object localObject2 = new AnimatorSet();
        ((AnimatorSet)localObject2).play((Animator)localObject1).with(localObjectAnimator);
        ((AnimatorSet)localObject2).setDuration(200L);
        changeVisible();
        ((AnimatorSet)localObject2).start();
        localObject2 = ObjectAnimator.ofFloat(this.topBar, "translationY", new float[] { this.y, this.y - 400.0F });
        localObjectAnimator = ObjectAnimator.ofFloat(this.topBar, "alpha", new float[] { 1.0F, 0.0F, 1.0F });
        localObject1 = new AnimatorSet();
        ((AnimatorSet)localObject1).play((Animator)localObject2).with(localObjectAnimator);
        ((AnimatorSet)localObject1).setDuration(400L);
        ((AnimatorSet)localObject1).start();

    }
    private void stopAnimation(){

        Object localObject1 = ObjectAnimator.ofFloat(this.middleBar, "translationY", new float[] { this.y - 200.0F, this.y });
        ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this.middleBar, "alpha", new float[] { 1.0F, 0.0F, 1.0F });
        Object localObject2 = new AnimatorSet();
        ((AnimatorSet)localObject2).play((Animator)localObject1).with(localObjectAnimator);
        ((AnimatorSet)localObject2).start();
        localObject2 = ObjectAnimator.ofFloat(this.topBar, "translationY", new float[] { this.y - 400.0F, this.y });
        localObjectAnimator = ObjectAnimator.ofFloat(this.topBar, "alpha", new float[] { 1.0F, 0.0F, 1.0F });
        localObject1 = new AnimatorSet();
        ((AnimatorSet)localObject1).play((Animator)localObject2).with(localObjectAnimator);
        ((AnimatorSet)localObject1).setDuration(400L);
        ((AnimatorSet)localObject1).start();
        ((AnimatorSet)localObject1).addListener(new AnimatorListenerAdapter()
        {
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
                CombineFAB.this.changeVisible();
            }
        });
    }
    public CombineFAB(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    }
