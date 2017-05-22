package com.oureda.thunder.pobooks.activity.main;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oureda.thunder.pobooks.CustomView.CombineFAB;
import com.oureda.thunder.pobooks.CustomView.FlowLayout;
import com.oureda.thunder.pobooks.Data.BookInfo;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.TitleInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.adapter.BookAdapter;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.baseInterface.AdapterChangedListener;
import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    FlowLayout flowLayout;
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    @BindView(R.id.book_list_main)
    RecyclerView bookListMain;
    @BindView(R.id.swift_refresh)
    LinearLayout swiftRefresh;
    @BindView(R.id.combine_main)
    CombineFAB combineMain;
    @BindView(R.id.check_all_main)
    CheckBox checkAllMain;
    @BindView(R.id.share_main)
    ImageView shareMain;
    @BindView(R.id.delete_main)
    ImageView deleteMain;
    @BindView(R.id.bottom_main)
    LinearLayout bottomMain;
    @BindView(R.id.drawerLayout_main)
    DrawerLayout drawerLayoutMain;
    @BindView(R.id.search_main_layout)
    LinearLayout searchMainLayout;
    private BookAdapter bookAdapter;
    private List<Books> booksList;
    private boolean normal=true;


    private void cancelChooseAllBook() {
        HashSet localHashSet = new HashSet();
        Iterator localIterator = this.booksList.iterator();
        while (localIterator.hasNext()) {
            ((Books) localIterator.next()).setIsCheck(false);
        }
        this.bookAdapter = new BookAdapter(false, this.booksList, new MyAdapterChangedListener());
        this.bookAdapter.setChooseAll(true);
        this.bookAdapter.setChooseSet(localHashSet);
        this.bookListMain.setAdapter(this.bookAdapter);
    }

    private void chooseAllBook() {
        normal=true;
        HashSet localHashSet = new HashSet();
        Iterator localIterator = this.booksList.iterator();
        while (localIterator.hasNext()) {
            Books localBooks = (Books) localIterator.next();
            localBooks.setIsCheck(true);
            localHashSet.add(localBooks.getBookId());
        }
        this.bookAdapter = new BookAdapter(false, this.booksList, new MyAdapterChangedListener());
        this.bookAdapter.setChooseAll(true);
        this.bookAdapter.setChooseSet(localHashSet);
        this.bookListMain.setAdapter(this.bookAdapter);
        invalidateOptionsMenu();

    }

    private void delete() {
        Iterator localIterator = this.bookAdapter.getChooseSet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            LogUtil.d("dd", "" + str);
            DataSupport.deleteAll(Books.class, new String[]{"BookId = ?", str});
            DataSupport.deleteAll(TitleInfo.class, new String[]{"path = ?", str});
            DataSupport.deleteAll(ChapterInfo.class,"bookId = ?",str);
            try {
                FileUtil.deleteFile(FileUtil.getBookDir(str));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getBookList();
        this.bookAdapter.clearChooseSet();
        this.bookAdapter.setChooseAll(false);
        initRecycleView();
        this.combineMain.setVisibility(View.GONE);
        this.bottomMain.setVisibility(View.VISIBLE);
    }

    private void getBookList() {
        this.booksList = DataSupport.where("isTemp =?","0").find(Books.class);
        LogUtil.d(TAG, "the book amount == " + this.booksList.size() + ""+DataSupport.findAll(Books.class).size());
        DataSupport.deleteAll(Books.class,"isTemp = ?","1");
        DataSupport.deleteAll(ChapterInfo.class,"isTemp = ?","1");

    }


    private void initRecycleView() {
        GridLayoutManager localGridLayoutManager = new GridLayoutManager(this, 3);
        this.bookListMain.setLayoutManager(localGridLayoutManager);
        this.bookAdapter = new BookAdapter(false, this.booksList, new MyAdapterChangedListener());
        this.bookListMain.setAdapter(this.bookAdapter);
    }


    private void virturlData() {
        Books localBooks1 = new Books("5", "真武世界");
        localBooks1.setImageId(R.drawable.book);
        localBooks1.setAuthor("蚕茧里的牛");
        localBooks1.setFromSd(false);
        localBooks1.setHasRead(false);
        localBooks1.save();
        Books localBooks2 = new Books("2", "武极天下");
        localBooks2.setImageId(R.drawable.book);
        localBooks1.setAuthor("参见里的牛");
        localBooks2.setFromSd(false);
        localBooks1.setHasRead(false);
        localBooks2.save();
        localBooks2 = new Books("3", "邪气凛然");
        localBooks2.setImageId(R.drawable.book);
        localBooks2.setFromSd(false);
        localBooks1.setHasRead(false);
        localBooks2.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Connector.getDatabase();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            LogUtil.d("ssss","hshs");
        }else{
            LogUtil.d("ssss","ssss");
        }
       // virturlData();
        initTool();
        checkAllMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chooseAllBook();
                } else {
                    cancelChooseAllBook();
                }
            }
        });


    }
    private void initTool(){
        setSupportActionBar(toolbarMain);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        actionBar.setTitle("阅读");
    }

    @OnClick({R.id.share_main, R.id.delete_main, R.id.bottom_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_main:
                break;
            case R.id.delete_main:
                delete();
                break;
            case R.id.bottom_main:
                break;
        }
    }


    private class MyAdapterChangedListener
            implements AdapterChangedListener {
        private MyAdapterChangedListener() {
        }

        public void OnAdapterRefresh(boolean isChooseAll) {
            bookAdapter.notifyDataSetChanged();
            if(isChooseAll){
                normal=false;
                invalidateOptionsMenu();
                combineMain.setVisibility(View.GONE);
                bottomMain.setVisibility(View.VISIBLE);
            }else{
                normal=true;
                invalidateOptionsMenu();
                visible(combineMain);
                gone(bottomMain);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookList();
        bookListMain= (RecyclerView) findViewById(R.id.book_list_main);
        initRecycleView();
        drawerLayoutMain.closeDrawer(Gravity.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayoutMain.openDrawer(GravityCompat.START);
                break;
            case R.id.search_main:
                visible(searchMainLayout);
                gone(swiftRefresh);
                break;
            case R.id.cancel_main:
                normal=true;
                invalidateOptionsMenu();
                visible(combineMain);
                gone(bottomMain);
                initRecycleView();
                break;

        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(normal){
            menu.findItem(R.id.search_main).setVisible(true);
            menu.findItem(R.id.cancel_main).setVisible(false);
        }else{
            menu.findItem(R.id.search_main).setVisible(false);
            menu.findItem(R.id.cancel_main).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(searchMainLayout.getVisibility()==View.VISIBLE){
            visible(swiftRefresh);
            gone(searchMainLayout);
        }
        if(normal){
            finish();
        }else{
            normal=true;
            invalidateOptionsMenu();
            visible(combineMain);
            gone(bottomMain);
            initRecycleView();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 2:
                if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    ToastUtil.showToast("授权成功");
                }else{
                    ToastUtil.showToast("拒绝权限无法使用此功能");
                    finish();
                }
                break;
        }
    }
    private void request(){

    }
}
