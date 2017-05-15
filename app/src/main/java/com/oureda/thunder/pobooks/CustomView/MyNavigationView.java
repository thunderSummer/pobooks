package com.oureda.thunder.pobooks.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.oureda.thunder.pobooks.activity.main.MainActivity;
import com.oureda.thunder.pobooks.activity.main.PersonCenterActivity;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by thunder on 17-5-1.
 */

public class MyNavigationView extends FrameLayout {
    /**
     * 负责整个app的侧滑事件处理
     */
    private TextView account;
    private CircleImageView circleImageView;
    private Context context;
    private View headView;
    private TextView money;
    private TextView name;
    private NavigationView navigationView;
    private TextView readTV;
    private TextView score;
    private View view;


    public MyNavigationView(@NonNull Context context) {
        super(context);
    }

    public MyNavigationView(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.view = LayoutInflater.from(context).inflate(R.layout.navigation_view, this);
        this.navigationView = ((NavigationView) this.view.findViewById(R.id.navigation_all));
        this.readTV = ((TextView) findViewById(R.id.read_menu));
        this.headView = this.navigationView.getHeaderView(0);
        this.circleImageView = ((CircleImageView) this.headView.findViewById(R.id.user_photo_menu));
        this.name = ((TextView) this.headView.findViewById(R.id.user_name_menu));
        this.account = ((TextView) this.headView.findViewById(R.id.account_menu));
//        this.account.setText(CacheManager.getInstance().getUserAccount());
//        this.name.setText(CacheManager.getInstance().getUserAccount());
        this.circleImageView.setImageResource(R.mipmap.ic_launcher);
        this.context = context;
        this.circleImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                context.startActivity(new Intent(context, PersonCenterActivity.class));
                BaseActivity.finishLast();
            }
        });

        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.read_menu:
                        context.startActivity(new Intent(context, MainActivity.class));
                        BaseActivity.finishLast();
                        break;
                    case R.id.user_photo_menu:
                        context.startActivity(new Intent(context, PersonCenterActivity.class));
                        break;
                }
                return false;
            }
        });
    }


    public MyNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
