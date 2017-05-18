package com.oureda.thunder.pobooks.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oureda.thunder.pobooks.CustomView.MyTextView;
import com.oureda.thunder.pobooks.Data.User;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.main.MainActivity;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.manager.CacheManager;
import com.oureda.thunder.pobooks.manager.StringManager;
import com.oureda.thunder.pobooks.utils.HttpUtils;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar_login)
    Toolbar toolbarLogin;
    @BindView(R.id.id_login)
    MyTextView idLogin;
    @BindView(R.id.password_login)
    MyTextView passwordLogin;
    @BindView(R.id.forget_login)
    TextView forgetLogin;
    @BindView(R.id.enter_login)
    TextView enterLogin;
    @BindView(R.id.register_new_login)
    TextView registerNewLogin;
    private String password;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_login, "登录", R.drawable.back_icon);
        if(CacheManager.getInstance().getCookie()!=""){
           autoLogin();
        }
    }
    private void autoLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.postAsyn(StringManager.URL + "login", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showToast("自动登录失败，请检查网络");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response!=null){
                            String content=response.body().string();
                            String c = response.toString();
                            LogUtil.d("AutoLogin",content);
                            if(c.contains("code=200")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast("自动登录成功即将跳转到主界面");
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                });
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast("自动登录失败，密码可能已经被修改");
                                    }
                                });
                            }
                        }
                    }
                },new HttpUtils.Param("email", CacheManager.getInstance().getUserAccount()));
            }
        }).start();
    }
    private void startAnimation(){

    }
    private void stopAnimation(){

    }

    @OnClick({R.id.forget_login, R.id.enter_login, R.id.register_new_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_login:
                break;
            case R.id.enter_login:
                this.password = passwordLogin.getText().toString();
                this.account = idLogin.getText().toString();
                if ((this.account.contains("@")) && (this.password.length() >= 6))
                {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            HttpUtils.postAsyn("http://123.206.71.182:3000/action=login", new Callback()
                            {
                                public void onFailure(Call paramAnonymous2Call, IOException paramAnonymous2IOException)
                                {
                                    LoginActivity.this.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            ToastUtil.showToast("无法连接到服务器，请检查网络");
                                        }
                                    });
                                }
                                public void onResponse(Call call, Response response) throws IOException {
                                if (response != null)
                                {
                                    Headers headers = response.headers();
                                    String content = response.body().string();
                                    if(content.equals("Internal Server Error")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast("你尚未注册");
                                            }
                                        });

                                    }
                                    else if(content.equals("Not Acceptable")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast("账号或密码错误");
                                            }
                                        });

                                    }
                                    else{
                                        User user=null;
                                    try {
                                       user = new Gson().fromJson(content, User.class);
                                    }catch (Exception e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast("服务器暂时出现了问题，我们将立刻修复");
                                            }
                                        });
                                    }
                                    if(user!=null) {

                                        if (user.status.getMessage().equals("request success!")) {
                                            CacheManager.getInstance().setUserIntroduce(user.data.getFullintroduction());
                                            CacheManager.getInstance().setUserAccount(LoginActivity.this.account);
                                            CacheManager.getInstance().setUserLove(user.data.getFavourite());
                                            CacheManager.getInstance().setUserAddress(user.data.getReceive_address());
                                            Log.d(LoginActivity.this.TAG, "onResponse: " + CacheManager.getInstance().getUserAddress());
                                            if (user.data.isSex() != true) {
                                                CacheManager.getInstance().setUserSex("男");
                                            } else {
                                                CacheManager.getInstance().setUserSex("女");
                                            }
                                            CacheManager.getInstance().setUserName(user.data.getNickname());
                                            Log.d("info_headers", "header " + headers);
                                            List<String> stringList = headers.values("Set-Cookie");
                                            if (stringList.size() > 0) {
                                                String cookie = stringList.get(0);
                                                Log.d("info_cookies", "onResponse-size: " + cookie);
                                                String session = cookie.substring(0, cookie.indexOf(";"));
                                                Log.i("info_s", "session is  :" + session);
                                                CacheManager.getInstance().saveCookie(session);
                                                LoginActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        ToastUtil.showToast("登录成功");
                                                        LoginActivity.this.stopAnimation();
                                                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                        LoginActivity.this.finish();
                                                    }
                                                });
                                            }
                                        }
                                    }


                                }


                                }
                            }
            }, new HttpUtils.Param[] { new HttpUtils.Param("email", LoginActivity.this.account), new HttpUtils.Param("password", LoginActivity.this.password) });
                        }
                    }).start();
                }
                else
                {
                    ToastUtil.showToast("账号或密码不符合要求");
                }
                break;
            case R.id.register_new_login:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }
}
