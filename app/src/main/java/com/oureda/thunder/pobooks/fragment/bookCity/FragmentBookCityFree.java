package com.oureda.thunder.pobooks.fragment.bookCity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.oureda.thunder.pobooks.CustomView.WaitView;
import com.oureda.thunder.pobooks.Data.BookInfo;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.SearchResult;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.adapter.BookCityAdapter;
import com.oureda.thunder.pobooks.activity.main.BookCityActivity;
import com.oureda.thunder.pobooks.activity.main.BookDetailActivity;
import com.oureda.thunder.pobooks.activity.main.SearchResultActivity;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.manager.StringManager;
import com.oureda.thunder.pobooks.utils.HttpUtils;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by thunder on 17-5-15.
 */

public class FragmentBookCityFree extends Fragment {
    private static String TAG="FragmentBookCityFree" ;
    @BindView(R.id.wait_free_city)
    WaitView waitFreeCity;
    @BindView(R.id.recycle_book_city_free)
    RecyclerView recycleBookCityFree;
    Unbinder unbinder;
    private List<SearchResult.Data> data;
    private List<BookInfo.Data> datas;
    private BookCityAdapter adapter;
     private View view;
    private BookCityActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_city_free, null);
        unbinder = ButterKnife.bind(this, view);
        activity= (BookCityActivity) getActivity();
        waitFreeCity.start();
        HttpUtils.postAsyn(StringManager.URL + "freelist", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast("服务器暂时出现了故障");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String content = response.body().string();
                Log.d(TAG, "onResponse: "+content);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data =new Gson().fromJson(content,SearchResult.class).data;
                        initRecycleView();
                        waitFreeCity.stop();
                        waitFreeCity.setVisibility(View.GONE);
                    }
                });


            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void initRecycleView(){
        adapter=new BookCityAdapter(data);
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.getContext());
        recycleBookCityFree.setLayoutManager(manager);

        adapter.setBookCityListen(new BookCityAdapter.BookCityListen() {
            @Override
            public void intentActivity(final int position) {
                final String bookId = data.get(position).getBook_id();
                HttpUtils.postAsyn("http://123.206.71.182:3000/action=readbook", new Callback()
                {
                    public void onFailure(Call paramAnonymous2Call, IOException paramAnonymous2IOException)
                    {
                        activity.runOnUiThread(new Runnable()
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
                        LogUtil.d(TAG, "onResponse: " + content);
                        final boolean isExit;

                        BookInfo bookInfo = new Gson().fromJson(content, BookInfo.class);
                        if (bookInfo.data.size() == 0) {
                           activity.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    ToastUtil.showToast("书籍好像走丢了");
                                }
                            });
                        }
                        else {

                            datas = bookInfo.data;
                            activity.runOnUiThread(new Runnable()
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
                                        LogUtil.d("ssss", DataSupport.where("bookId=?",chapterInfo.getBookId()).find(ChapterInfo.class).get(0).isTemp()+" ");
                                    }
                                }
                            });
                            Intent intent = new Intent(activity, BookDetailActivity.class);
                            intent.putExtra("book_name", data.get(position).getBook_name());
                            intent.putExtra("book_price", data.get(position).getPrice());
                            intent.putExtra("book_author", data.get(position).getWriter());
                            intent.putExtra("book_info", data.get(position).getIntro());
                            intent.putExtra("book_id", data.get(position).getBook_id());
                            activity.startActivity(intent);
                        }
                    }
                }, new HttpUtils.Param[] { new HttpUtils.Param("book_id", bookId) });
            }
        });
        recycleBookCityFree.setAdapter(adapter);
    }
}
