package com.oureda.thunder.pobooks.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.MyTextView;
import com.oureda.thunder.pobooks.CustomView.RoundButton;
import com.oureda.thunder.pobooks.R;
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

public class RegisterActivity extends BaseActivity {
    private static final int FAIL = 0;
    private static final int SUCCESS = 1;

    @BindView(R.id.id_register)
    MyTextView idRegister;
    @BindView(R.id.password_register)
    MyTextView passwordRegister;
    @BindView(R.id.identifying_code_register)
    MyTextView identifyingCodeRegister;
    @BindView(R.id.identifying_count_register)
    RoundButton identifyingCountRegister;
    @BindView(R.id.next_register)
    TextView nextRegister;
    @BindView(R.id.user_agreement_register)
    TextView userAgreementRegister;
    private Handler handler = new Handler()
    {
        public void handleMessage(Message paramAnonymousMessage)
        {
            switch (paramAnonymousMessage.what){
                case FAIL:
                    ToastUtil.showToast( "服务器暂时出现了问题");
                    stopAnimation();
                    break;
                case SUCCESS:
                    ToastUtil.showToast( "注册成功，即将返回登录界面");
                    stopAnimation();
                    RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    RegisterActivity.this.finish();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_register, "注册", R.drawable.back_icon);
    }

    @OnClick({R.id.identifying_count_register, R.id.next_register, R.id.user_agreement_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.identifying_count_register:
                final String account = idRegister.getText().toString();
                if (account.contains("@")) {
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Response response = HttpUtils.post(StringManager.URL + "signUpVerify", new HttpUtils.Param("email", account));
                                Headers headers = response.headers();
                                final String content = response.body().string();
                                Log.d("info_headers", "header " + headers);
                                List<String> cookies = headers.values("Set-Cookie");
                                String str = cookies.get(0);
                                Log.d("info_cookies", "onResponse-size: " + str);
                                String cookie = str.substring(0, str.indexOf(";"));
                                CacheManager.getInstance().saveCookie(cookie);
                                LogUtil.d("asdf", cookie);
                                if (content.equals("OK")) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showToast("验证码已经发送，请注意查收");
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showToast("与服务器连接失败，错误原因" + content);
                                        }
                                    });

                                }
                            } catch (IOException localIOException) {
                                for (; ; ) {
                                    localIOException.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                new CountDownTimer(60000L, 1000L) {
                    public void onFinish() {
                        identifyingCountRegister.setEnabled(true);
                        identifyingCountRegister.setContent("发送验证码");
                    }

                    public void onTick(long paramAnonymous2Long) {
                        identifyingCountRegister.setEnabled(false);
                        identifyingCountRegister.setContent(paramAnonymous2Long / 1000L + "后发送验证码");
                    }
                }.start();
                break;
            case R.id.next_register:
                if (passwordRegister.getAllowDo()) {
                    final String password = passwordRegister.getText().toString();
                    final String account1 = idRegister.getText().toString();
                    final String verify = identifyingCodeRegister.getText().toString();
                    if ((account1.contains("@")) && (password != "")) {
                        new Thread(new Runnable() {
                            public void run() {
                                HttpUtils.postAsyn("http://123.206.71.182:3000/action=signup",
                                        new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Message message = new Message();
                                                message.what = FAIL;
                                                handler.sendMessage(message);
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                if (response != null) {
                                                    String content = response.body().string();
                                                    if (content.equals("OK")) {
                                                        String str = response.headers().values("Set-Cookie").get(0);
                                                        String cookie = str.substring(0, str.indexOf(";"));
                                                        CacheManager.getInstance().saveCookie(cookie);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ToastUtil.showToast("注册成功，即将返回登录界面");
                                                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class) );
                                                            }
                                                        });
                                                    } else {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                ToastUtil.showToast("注册失败，服务器暂时出现了问题");
                                                            }
                                                        });


                                                    }

                                                }
                                            }
                                        }, new HttpUtils.Param[]{new HttpUtils.Param("email", account1), new HttpUtils.Param("password", password), new HttpUtils.Param("verify", verify)});
                            }
                        }).start();
                    }
                } else {
                    ToastUtil.showToast("账号或密码不符合要求，请重试");
                }
                break;
            case R.id.user_agreement_register:
                break;
        }
    }
    private void stopAnimation(){

    }
    private void startAnimation(){

    }
}
