package com.oureda.thunder.pobooks.activity.person;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.MyViewPager;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.base.ColorChange;
import com.oureda.thunder.pobooks.fragment.mark.FragmentMarkLabel;
import com.oureda.thunder.pobooks.fragment.mark.FragmentMarkFeel;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ReadNoteActivity extends BaseActivity {
    private List<Fragment> fragmentList;
    private FragmentManager fragmentManager;
    private FragmentMarkLabel fragmentMarkLabel;
    private FragmentMarkFeel fragmentMarkFeel;
    private List<TextView> textViewList;
    private TextView feel;
    private TextView label;
    private MyViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_note);
        initToolbar(R.id.toolbar_read_note_person,"读书笔记",R.drawable.ic_menu);
        feel= (TextView) findViewById(R.id.feel_read_note_person);
        label = (TextView) findViewById(R.id.marks__read_note_person);
        init();
        viewPager = (MyViewPager) findViewById(R.id.viewPage_read_note_person);
        viewPager.initAll(2,R.id.underline_read_note_person,fragmentList,fragmentManager,getWindow().getDecorView());
        viewPager.setAdapter(viewPager.myAdapter);
        viewPager.setOnPageChangeListener(viewPager.listener);
        viewPager.setCurrentItem(0);
        viewPager.setColorChange(new ColorChange() {
            @Override
            public void OnColorChange() {
                for(int i = 0;i<textViewList.size();++i){
                    if (i==viewPager.getCurrentItem()){
                        textViewList.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        textViewList.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
            }
        });

    }
    private void init(){
        textViewList=new ArrayList<>();
        fragmentList=new ArrayList<>();
        textViewList.add(label);
        label.setTextColor(getResources().getColor(R.color.colorAccent));
        label.setOnClickListener(new MyClickListen(0));
        feel.setOnClickListener(new MyClickListen(1));
        feel.setTextColor(getResources().getColor(R.color.colorWhite));
        textViewList.add(feel);
        fragmentMarkLabel =new FragmentMarkLabel();
        fragmentMarkFeel=new FragmentMarkFeel();
        fragmentList.add(fragmentMarkLabel);
        fragmentList.add(fragmentMarkFeel);
        fragmentManager=getSupportFragmentManager();

    }
    private class MyClickListen implements View.OnClickListener{
        int position;

        public MyClickListen(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(position);
            ToastUtil.showToast(viewPager.getCurrentItem()+"");
        }
    }
}
