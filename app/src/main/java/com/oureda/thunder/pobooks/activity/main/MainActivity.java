package com.oureda.thunder.pobooks.activity.main;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oureda.thunder.pobooks.CustomView.CombineFAB;
import com.oureda.thunder.pobooks.CustomView.FlowLayout;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.TitleInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.adapter.BookAdapter;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.baseInterface.AdapterChangedListener;
import com.oureda.thunder.pobooks.utils.LogUtil;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

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
    private BookAdapter bookAdapter;
    private List<Books> booksList;


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
    }

    private void delete() {
        Iterator localIterator = this.bookAdapter.getChooseSet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            LogUtil.d("dd", "" + str);
            DataSupport.deleteAll(Books.class, new String[]{"BookId = ?", str});
            DataSupport.deleteAll(TitleInfo.class, new String[]{"path = ?", str});
        }
        getBookList();
        this.bookAdapter.clearChooseSet();
        this.bookAdapter.setChooseAll(false);
        initRecycleView();
        this.combineMain.setVisibility(View.GONE);
        this.bottomMain.setVisibility(View.VISIBLE);
    }

    private void getBookList() {
        this.booksList = DataSupport.findAll(Books.class);
        LogUtil.d(TAG, "the book amount == " + this.booksList.size() + "");
    }


    private void initRecycleView() {
        GridLayoutManager localGridLayoutManager = new GridLayoutManager(this, 3);
        this.bookListMain.setLayoutManager(localGridLayoutManager);
        this.bookAdapter = new BookAdapter(false, this.booksList, new MyAdapterChangedListener());
        this.bookListMain.setAdapter(this.bookAdapter);
    }


    private void virturlData() {
        Books localBooks1 = new Books("1", "真武世界");
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
      //  virturlData();
        initToolbar(R.id.toolbar_main, "读书", R.drawable.search);
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
                combineMain.setVisibility(View.GONE);
                bottomMain.setVisibility(View.VISIBLE);
            }else{
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
}
