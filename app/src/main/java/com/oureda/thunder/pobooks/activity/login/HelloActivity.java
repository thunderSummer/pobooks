package com.oureda.thunder.pobooks.activity.login;

import android.content.Intent;
import android.os.BadParcelableException;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.oureda.thunder.pobooks.CustomView.readView.BaseReadView;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;

public class HelloActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello2);
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                HelloActivity.this.enterHomeActivity();
            }
        }, 1500L);
    }
    private void enterHomeActivity()
    {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
