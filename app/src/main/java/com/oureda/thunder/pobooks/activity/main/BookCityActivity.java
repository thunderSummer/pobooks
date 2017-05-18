package com.oureda.thunder.pobooks.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.oureda.thunder.pobooks.R;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.MyViewPager;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.base.ColorChange;
import com.oureda.thunder.pobooks.fragment.bookCity.FragmentBookCityCreate;
import com.oureda.thunder.pobooks.fragment.bookCity.FragmentBookCityFree;
import com.oureda.thunder.pobooks.fragment.bookCity.FragmentBookCityListen;
import com.oureda.thunder.pobooks.fragment.bookCity.FragmentBookCityNew;
import com.oureda.thunder.pobooks.fragment.bookCity.FragmentBookCitySort;
import com.oureda.thunder.pobooks.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BookCityActivity extends BaseActivity {

    @BindView(R.id.back_book_city)
    ImageView backBookCity;
    @BindView(R.id.search_book_city_et)
    EditText searchBookCityEt;
    @BindView(R.id.search_book_city_iv)
    ImageView searchBookCityIv;
    @BindView(R.id.sort_book_city)
    TextView sortBookCity;
    @BindView(R.id.new_book_city)
    TextView newBookCity;
    @BindView(R.id.free_book_city)
    TextView freeBookCity;
    @BindView(R.id.create_book_city)
    TextView createBookCity;
    @BindView(R.id.listen_book_city)
    TextView listenBookCity;
    @BindView(R.id.underline_book_city)
    ImageView underlineBookCity;
    @BindView(R.id.viewPage_book_city)
    MyViewPager viewPageBookCity;
    private List<Fragment> fragmentList;
    private List<TextView> textViews;
    private FragmentBookCityCreate fragmentBookCityCreate;
    private FragmentBookCityFree fragmentBookCityFree;
    private FragmentBookCityNew fragmentBookCityNew;
    private FragmentBookCitySort fragmentBookCitySort;
    private FragmentBookCityListen fragmentBookCityListen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_city2);
        ButterKnife.bind(this);
        init();
        initViewPage();

    }

    @OnClick({R.id.back_book_city, R.id.search_book_city_iv,R.id.search_book_city_et})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_book_city:
                finish();
                break;
            case R.id.search_book_city_iv:
                Intent intent =new Intent(BookCityActivity.this,SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.SEARCH,searchBookCityEt.getText().toString());
                LogUtil.d("string == ",searchBookCityEt.getText().toString()+"haha");
                startActivity(intent);
                break;
            case R.id.search_book_city_et:
                searchBookCityEt.setFocusableInTouchMode(true);
                searchBookCityEt.setFocusable(true);
                searchBookCityEt.requestFocus();
                break;
        }
    }
    private void init(){
        searchBookCityEt.setFocusableInTouchMode(false);
        searchBookCityEt.setFocusable(false);
        fragmentList=new ArrayList<>();
        fragmentBookCityCreate=new FragmentBookCityCreate();
        fragmentBookCityFree=new FragmentBookCityFree();
        fragmentBookCityNew=new FragmentBookCityNew();
        fragmentBookCitySort=new FragmentBookCitySort();
        fragmentBookCityListen=new FragmentBookCityListen();
        fragmentList.add(fragmentBookCitySort);
        fragmentList.add(fragmentBookCityNew);
        fragmentList.add(fragmentBookCityFree);
        fragmentList.add(fragmentBookCityCreate);
        fragmentList.add(fragmentBookCityListen);
        textViews=new ArrayList<>();
        textViews.add(sortBookCity);
        textViews.add(newBookCity);
        textViews.add(freeBookCity);
        textViews.add(createBookCity);
        textViews.add(listenBookCity);
    }
    private void initViewPage(){
        viewPageBookCity.initAll(5,R.id.underline_book_city,fragmentList,getSupportFragmentManager(),getWindow().getDecorView());
        viewPageBookCity.setAdapter(viewPageBookCity.myAdapter);
        viewPageBookCity.setOnPageChangeListener(viewPageBookCity.listener);
        viewPageBookCity.setCurrentItem(0);
        viewPageBookCity.setColorChange(new ColorChange() {
            @Override
            public void OnColorChange() {
                for(int i=0;i<textViews.size();i++){
                    if(i==viewPageBookCity.getCurrentItem()){
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
                    }else{
                        textViews.get(i).setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                }
            }
        });
    }
}
