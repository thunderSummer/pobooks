package com.oureda.thunder.pobooks.activity.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.manager.CacheManager;
import com.oureda.thunder.pobooks.manager.SettingManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.message_push_setting)
    LinearLayout messagePushSetting;
    @BindView(R.id.feedback_setting)
    LinearLayout feedbackSetting;
    @BindView(R.id.about_our_setting)
    LinearLayout aboutOurSetting;
    @BindView(R.id.version_update_setting)
    LinearLayout versionUpdateSetting;
    @BindView(R.id.exit_setting)
    TextView exitSetting;
    @BindView(R.id.drawer_setting)
    DrawerLayout drawerSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_setting, "设置", R.drawable.ic_menu);
    }
    @OnClick({R.id.message_push_setting, R.id.feedback_setting, R.id.about_our_setting, R.id.version_update_setting, R.id.exit_setting, R.id.drawer_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_push_setting:
                break;
            case R.id.feedback_setting:
                break;
            case R.id.about_our_setting:
                break;
            case R.id.version_update_setting:
                break;
            case R.id.exit_setting:
                CacheManager.getInstance().saveCookie("");
                break;
            case R.id.drawer_setting:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerSetting.openDrawer(Gravity.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
