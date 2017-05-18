package com.oureda.thunder.pobooks.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oureda.thunder.pobooks.CustomView.WaitView;
import com.oureda.thunder.pobooks.Data.BookInfo;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.SearchResult;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.adapter.BookCityAdapter;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.utils.HttpUtils;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchResultActivity extends BaseActivity {
    public static final String SEARCH = "search_content";
    @BindView(R.id.back_result_search)
    ImageView backResultSearch;
    @BindView(R.id.search_result_et)
    EditText searchResultEt;
    @BindView(R.id.search_result_iv)
    ImageView searchResultIv;
    @BindView(R.id.recycle_search_result)
    RecyclerView recycleSearchResult;
    @BindView(R.id.wait_search_result)
    WaitView waitSearchResult;
    private String content;
    private List<SearchResult.Data> data;
    private List<BookInfo.Data> datas;
    private BookCityAdapter bookCityAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        content = intent.getStringExtra(SEARCH);
        LogUtil.d("search content = ",content);
        if (!content.isEmpty() && content != null) {
            startSearch();
        }else{
            LogUtil.d("search content = ",content+"   sss");
        }
    }

    private void startSearch() {
        waitSearchResult.setVisibility(View.VISIBLE);
        waitSearchResult.start();

        if (searchResultEt.getText().toString() != "") {
            content = searchResultEt.getText().toString();
        }
        HttpUtils.postAsyn("http://123.206.71.182:3000/action=search", new Callback() {
            public void onFailure(Call paramAnonymous2Call, IOException paramAnonymous2IOException) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        ToastUtil.showToast("搜索失败");
                        waitSearchResult.stop();
                        waitSearchResult.setVisibility(View.GONE);
                    }
                });
            }

            public void onResponse(Call paramAnonymous2Call, Response response)
                    throws IOException {
                String result = response.body().string();
                LogUtil.d("result == ",result);
                data=new Gson().fromJson(result, SearchResult.class).data;
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        initRecyclerView();
        waitSearchResult.stop();
        waitSearchResult.setVisibility(View.GONE);
    }
});

            }
        }, new HttpUtils.Param[]{new HttpUtils.Param("type", content)});
    }

    @OnClick({R.id.back_result_search, R.id.search_result_et, R.id.search_result_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_result_search:
                finish();
                break;
            case R.id.search_result_et:
                searchResultEt.setFocusableInTouchMode(true);
                searchResultEt.setFocusable(true);
                searchResultEt.requestFocus();
                break;
            case R.id.search_result_iv:
                if(!searchResultEt.getText().toString().isEmpty()){
                    startSearch();
                }else{
                    ToastUtil.showToast("搜索被玩坏了");
                }

                break;
        }
    }
    private void initRecyclerView(){
        bookCityAdapter = new BookCityAdapter(data);

        linearLayoutManager=new LinearLayoutManager(SearchResultActivity.this);
        bookCityAdapter.setBookCityListen(new BookCityAdapter.BookCityListen() {
            @Override
            public void intentActivity(final int position) {
                final String bookId = data.get(position).getBook_id();
                HttpUtils.postAsyn("http://123.206.71.182:3000/action=readbook", new Callback()
                {
                    public void onFailure(Call paramAnonymous2Call, IOException paramAnonymous2IOException)
                    {
                        SearchResultActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                ToastUtil.showToast("书籍信息错误");
                            }
                        });
                    }

                public void onResponse(Call paramAnonymous2Call, Response response) throws IOException
                {
                    String content  = response.body().string();
                    LogUtil.d(SearchResultActivity.this.TAG, "onResponse: " + content);
                    final boolean isExit;

                    BookInfo bookInfo = new Gson().fromJson(content, BookInfo.class);
                    if (bookInfo.data.size() == 0) {
                        SearchResultActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                ToastUtil.showToast("书籍好像走丢了");
                            }
                        });
                    }
                   else {

                        datas = bookInfo.data;
                        SearchResultActivity.this.runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Iterator localIterator = datas.iterator();
                                while (localIterator.hasNext())
                                {
                                    BookInfo.Data localData = (BookInfo.Data) localIterator.next();
//                                    TitleInfo localTitleInfo = new TitleInfo(SearchResultActivity.2.1.this.val$book_id, localData.getAddress().substring(localData.getAddress().lastIndexOf("/") + 1, localData.getAddress().lastIndexOf(".")));
                                    ChapterInfo chapterInfo=new ChapterInfo(localData.getBook_book_id(),
                                            localData.getChapter(),
                                            localData.getAddress(),
                                            localData.getSize(),
                                            localData.getAddress().substring(localData.getAddress().lastIndexOf("/")+1,localData.getAddress().lastIndexOf(".")),false);
                                    LogUtil.d("chapterInfo",chapterInfo.getAddress()+chapterInfo.getBookId());
                                    //if(DataSupport.where("bookId = ?",chapterInfo.getBookId()).find(ChapterInfo.class).size()==0)
                                        chapterInfo.setTemp(true);
                                        chapterInfo.save();
                                    LogUtil.d("ssss",DataSupport.where("bookId=?",chapterInfo.getBookId()).find(ChapterInfo.class).get(0).isTemp()+" ");
                                }
                            }
                        });
                        Intent intent = new Intent(SearchResultActivity.this, BookDetailActivity.class);
                        intent.putExtra("book_name", data.get(position).getBook_name());
                        intent.putExtra("book_price", data.get(position).getPrice());
                        intent.putExtra("book_author", data.get(position).getWriter());
                        intent.putExtra("book_info", data.get(position).getIntro());
                        intent.putExtra("book_id", data.get(position).getBook_id());
                        SearchResultActivity.this.startActivity(intent);
                    }
            }
        }, new HttpUtils.Param[] { new HttpUtils.Param("book_id", bookId) });
            }
        });
        recycleSearchResult.setAdapter(bookCityAdapter);
        recycleSearchResult.setLayoutManager(linearLayoutManager);

    }
}
