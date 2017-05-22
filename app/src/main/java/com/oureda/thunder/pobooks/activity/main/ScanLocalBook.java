package com.oureda.thunder.pobooks.activity.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.MyViewPager;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.base.ColorChange;
import com.oureda.thunder.pobooks.fragment.local.FragmentLocalAuto;
import com.oureda.thunder.pobooks.fragment.local.FragmentLocalHand;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanLocalBook extends BaseActivity {

    @BindView(R.id.toolbar_scan_local)
    Toolbar toolbarScanLocal;
    @BindView(R.id.auto_local_scan)
    TextView autoLocalScan;
    @BindView(R.id.hand_local_scan)
    TextView handLocalScan;
    @BindView(R.id.underline_local_scan)
    ImageView underlineLocalScan;
    @BindView(R.id.drawer_scan_local)
    LinearLayout drawerScanLocal;
    @BindView(R.id.viewPage_scan)
    MyViewPager viewPageScan;
    private List<Fragment> fragmentList;
    private FragmentLocalHand fragmentLocalHand;
    private FragmentLocalAuto fragmentLocalAuto;
    private List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_local_book);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_scan_local, "本地导入", R.drawable.back_icon);
        initTextViews();
       initRecycleView();
        autoLocalScan.setOnClickListener(new MyClickListen(0));
        handLocalScan.setOnClickListener(new MyClickListen(1));
        autoLocalScan.setTextColor(getResources().getColor(R.color.colorAccent));
        handLocalScan.setTextColor(getResources().getColor(R.color.colorWhite));
    }
    private void initTextViews(){
        textViews = new ArrayList<>();
        textViews.add(autoLocalScan);
        textViews.add(handLocalScan);
    }
    private void initRecycleView() {
        fragmentList=new ArrayList<>();
        fragmentLocalAuto=new FragmentLocalAuto();
        fragmentLocalHand = new FragmentLocalHand();
        fragmentLocalHand.setOnBackListener(new FragmentLocalHand.OnBackListener() {
            @Override
            public void finish() {
                viewPageScan.setCurrentItem(0);
            }
        });
        fragmentList.add(fragmentLocalAuto);
        fragmentList.add(fragmentLocalHand);
        viewPageScan.initAll(2,R.id.underline_local_scan,fragmentList,getSupportFragmentManager(),getWindow().getDecorView());
        viewPageScan.setAdapter(viewPageScan.myAdapter);
        viewPageScan.setOnPageChangeListener(viewPageScan.listener);
        viewPageScan.setCurrentItem(0);
        viewPageScan.setColorChange(new ColorChange() {
            @Override
            public void OnColorChange() {
                for(int i =0;i<textViews.size();i++){
                    if(i==viewPageScan.getCurrentItem()){
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }

            }
        });

    }
    private class MyClickListen implements View.OnClickListener{
        private int position;

        public MyClickListen(int position) {
            this.position = position;
        }


        @Override
        public void onClick(View v) {
            viewPageScan.setCurrentItem(position);

        }

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case 1:
//                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    ToastUtil.showToast("授权成功");
//                }else{
//                    ToastUtil.showToast("拒绝权限无法使用此功能");
//                    finish();
//                }
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
        if(fragmentLocalAuto.isBusy){
            ToastUtil.showToast("正在添加，请勿进行其他操作");
        }else{
            if(viewPageScan.getCurrentItem()==1){
                fragmentLocalHand.back(2);
            }else{
                finish();
            }
        }
    }
}
