package com.oureda.thunder.pobooks.activity.person;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.Dialog.MyDialog;
import com.oureda.thunder.pobooks.CustomView.Dialog.MyEditDialog;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.manager.CacheManager;
import com.oureda.thunder.pobooks.manager.StringManager;
import com.oureda.thunder.pobooks.utils.HttpUtils;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.SharedPreferenceUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonInformationActivity extends BaseActivity {
    @BindView(R.id.picture_information_person)
    LinearLayout pictureInformationPerson;
    @BindView(R.id.account_information)
    TextView accountInformation;
    @BindView(R.id.name_information_person)
    TextView nameInformationPerson;
    @BindView(R.id.sex_information_person)
    TextView sexInformationPerson;
    @BindView(R.id.address_information_person)
    TextView addressInformationPerson;
    @BindView(R.id.my_love_information_person)
    TextView myLoveInformationPerson;
    @BindView(R.id.introduce_information_person)
    TextView introduceInformationPerson;
    @BindView(R.id.save_information_person)
    TextView saveInformationPerson;
    private boolean isSave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_information_person,"个人信息完善",R.drawable.back_icon);
        myLoveInformationPerson.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_LOVE));
        nameInformationPerson.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_NAME));
        accountInformation.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_ACCOUNT));
        introduceInformationPerson.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_INTRODUCE));
        addressInformationPerson.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_ADDRESS));
        sexInformationPerson.setText(SharedPreferenceUtil.getInstance().getString(StringManager.USER_SEX));

    }

    @OnClick({ R.id.picture_information_person, R.id.account_information, R.id.name_information_person, R.id.sex_information_person, R.id.address_information_person, R.id.my_love_information_person, R.id.introduce_information_person, R.id.save_information_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.picture_information_person:
                break;
            case R.id.account_information:
                break;
            case R.id.name_information_person:
                final MyEditDialog myEditDialog = new MyEditDialog();
                myEditDialog.initAll(nameInformationPerson.getText().toString(), "修改自己的名字", "确定", "取消", new MyEditDialog.ButtonListener()
                {
                    public void negativeListener()
                    {
                        myEditDialog.dismiss();
                    }

                    public void positiveListener(String paramAnonymousString)
                    {
                        nameInformationPerson.setText(paramAnonymousString);
                        isSave=false;
                        ToastUtil.showToast("修改成功");
                        myEditDialog.dismiss();
                    }
                });
                myEditDialog.show(getFragmentManager(), "name_edit_fragment");
                break;
            case R.id.sex_information_person:

                final MyEditDialog sex = new MyEditDialog();
                sex.initAll(sexInformationPerson.getText().toString(), "修改自己的性别", "确定", "取消", new MyEditDialog.ButtonListener()
                {
                    public void negativeListener()
                    {
                        sex.dismiss();
                    }

                    public void positiveListener(String paramAnonymousString)
                    {
                        if ((paramAnonymousString.equals("男")) || (paramAnonymousString.equals("女")))
                        {
                            sexInformationPerson.setText(paramAnonymousString);
                            ToastUtil.showToast("保存成功");
                            sex.dismiss();
                        }else{
                            ToastUtil.showToast("修改不符合要求");
                        }
                    }
                });
                sex.show(getFragmentManager(), "sex_edit_fragment");
                break;
            case R.id.address_information_person:
                final MyEditDialog address = new MyEditDialog();
                address.initAll(addressInformationPerson.getText().toString(), "修改自己的收货地址", "确定", "取消", new MyEditDialog.ButtonListener()
                {
                    public void negativeListener()
                    {
                        address.dismiss();
                    }

                    public void positiveListener(String paramAnonymousString)
                    {
                        addressInformationPerson.setText(paramAnonymousString);
                        isSave=false;
                        ToastUtil.showToast("修改成功");
                        address.dismiss();
                    }
                });
                address.show(getFragmentManager(), "address_edit_fragment");
                break;
            case R.id.my_love_information_person:
                final MyEditDialog myLove = new MyEditDialog();
                myLove.initAll(myLoveInformationPerson.getText().toString(), "喜欢读什么书？", "确定", "取消", new MyEditDialog.ButtonListener()
                {
                    public void negativeListener()
                    {
                        myLove.dismiss();
                    }

                    public void positiveListener(String paramAnonymousString)
                    {
                        myLoveInformationPerson.setText(paramAnonymousString);
                        isSave=false;
                        ToastUtil.showToast("修改成功");
                        myLove.dismiss();
                    }
                });
                myLove.show(getFragmentManager(), "myLove_edit_fragment");
                break;
            case R.id.introduce_information_person:
                final MyEditDialog introduce = new MyEditDialog();
                introduce.initAll(introduceInformationPerson.getText().toString(), "介绍自己一下吧", "确定", "取消", new MyEditDialog.ButtonListener()
                {
                    public void negativeListener()
                    {
                        introduce.dismiss();
                    }

                    public void positiveListener(String paramAnonymousString)
                    {
                        introduceInformationPerson.setText(paramAnonymousString);
                        isSave=false;
                        ToastUtil.showToast("修改成功");
                        introduce.dismiss();
                    }
                });
                introduce.show(getFragmentManager(), "introduce_edit_fragment");
                break;
            case R.id.save_information_person:
                saveAll();
                break;
        }
    }
    private void saveAll()
    {
        boolean str;
        if (!this.isSave)
        {
            CacheManager.getInstance().setUserName(nameInformationPerson.getText().toString());
            CacheManager.getInstance().setUserAddress(addressInformationPerson.getText().toString());
            CacheManager.getInstance().setUserSex(sexInformationPerson.getText().toString());
            CacheManager.getInstance().setUserLove(myLoveInformationPerson.getText().toString());
            CacheManager.getInstance().setUserIntroduce(introduceInformationPerson.getText().toString());
            if (sexInformationPerson.getText().toString().equals("男"))
                str = true;
            else
                str=false;
                HttpUtils.postAsyn("http://123.206.71.182:3000/action=user=information", new Callback()
                {
                    public void onFailure(Call paramAnonymousCall, IOException paramAnonymousIOException)
                    {
                        PersonInformationActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                ToastUtil.showToast("操作无效");
                            }
                        });
                    }

                public void onResponse(Call paramAnonymousCall, Response paramAnonymousResponse) throws IOException
                {
                    if (paramAnonymousResponse != null)
                    {
                        String content = paramAnonymousResponse.body().string();
                        LogUtil.d(PersonInformationActivity.this.TAG, "onResponse: " + content);
                    }
                }
        }, new HttpUtils.Param[] { new HttpUtils.Param("nickname", nameInformationPerson.getText().toString()),
                        new HttpUtils.Param("sex", String.valueOf(str)),
                        new HttpUtils.Param("fullintroduction", this.introduceInformationPerson.getText().toString()),
                        new HttpUtils.Param("email", accountInformation.getText().toString()),
                        new HttpUtils.Param("receive_address", this.addressInformationPerson.getText().toString()),
                        new HttpUtils.Param("favourite", this.myLoveInformationPerson.getText().toString()) });
            }else{
            ToastUtil.showToast("你尚未修改");
        }
        isSave=true;
    }
    public void onBackPressed()
    {
        if (!this.isSave)
        {
            final MyDialog localMyDialog = new MyDialog();
            localMyDialog.initAll("你尚未保存，是否退出", "提示", "保存后退出", "直接退出", new MyDialog.ButtonListener()
            {
                public void negativeListener()
                {
                    finish();
                    localMyDialog.dismiss();
                }

                public void positiveListener()
                {
                    saveAll();
                    localMyDialog.dismiss();
                }
            });
            localMyDialog.show(getFragmentManager(), "exitAndSave");
        }
        else{
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
