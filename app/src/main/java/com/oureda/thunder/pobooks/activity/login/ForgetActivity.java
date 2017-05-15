package com.oureda.thunder.pobooks.activity.login;

import android.os.Bundle;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.listener.OnBookStatusChangeListen;

public class ForgetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
    }
    private class bookStatueListen implements OnBookStatusChangeListen{

        @Override
        public void onChapterChanged(int chapter) {

        }

        @Override
        public void onPageChanged(int chapter, int page) {

        }

        @Override
        public void onLoadChapterFailure(int chapter) {

        }

        @Override
        public void onCenterClick() {

        }

        @Override
        public void onFlip() {

        }
    }
}
