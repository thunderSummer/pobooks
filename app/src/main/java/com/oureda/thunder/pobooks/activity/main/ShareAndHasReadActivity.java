package com.oureda.thunder.pobooks.activity.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAndHasReadActivity extends BaseActivity {

    @BindView(R.id.toolbar_share_has_read)
    Toolbar toolbarShareHasRead;
    @BindView(R.id.recycle_share_has_read)
    RecyclerView recycleShareHasRead;
    @BindView(R.id.all_choose_share_has_read)
    CheckBox allChooseShareHasRead;
    @BindView(R.id.add_share_has_read)
    ImageView addShareHasRead;
    private int type;
    private List<Books> booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_and_has_read);
        ButterKnife.bind(this);
        this.type = getIntent().getIntExtra("style_has_read_share", 0);
        if (this.type == 1) {
            initToolbar(R.id.toolbar_share_has_read, "分享清单",R.drawable.back_icon);
        }else{
            initToolbar(R.id.toolbar_share_has_read, "已读清单",R.drawable.back_icon);
        }
        allChooseShareHasRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
                {
                    if (paramAnonymousBoolean)
                    {
                        Iterator<Books> iterator = booksList.iterator();
                        while (iterator.hasNext())
                        {
                            Books books =  iterator.next();
                            if (ShareAndHasReadActivity.this.type == 1) {
                                books.setShare(true);
                            } else {
                                books.setHasRead(true);
                            }
                        }
                        initList();
                        initRecycleView();
                    }
                    else
                    {
                        Iterator<Books> iterator = booksList.iterator();
                        while (iterator.hasNext())
                        {
                            Books books =iterator.next();
                            if (ShareAndHasReadActivity.this.type == 1) {
                                books.setShare(false);
                            } else {
                                books.setHasRead(false);
                            }
                        }
                        initList();
                        initRecycleView();
                    }
                }
            });
            this.addShareHasRead.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    Iterator<Books> iterator = ShareAndHasReadActivity.this.booksList.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().save();
                    }
                    initList();
                    ShareAndHasReadActivity.this.initRecycleView();
                    ToastUtil.showToast("添加成功");
                }
            });
            initList();
            initRecycleView();
        }
        private void initRecycleView(){
            recycleShareHasRead.setAdapter(new Adapter(booksList));
            recycleShareHasRead.setLayoutManager(new LinearLayoutManager(ShareAndHasReadActivity.this));

        }
        private void initList(){
            if (this.type == 1) {
                booksList = DataSupport.where(new String[] { "isShare == ?", "0" }).find(Books.class);
            }else {
                this.booksList = DataSupport.where(new String[] { "isHasRead == ?", "0" }).find(Books.class);
            }

        }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        private List<Books> booksList;

        public Adapter(List<Books> booksList) {
            this.booksList = booksList;
        }

        public int getItemCount() {
            return this.booksList.size();
        }

        public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
            paramViewHolder.bookNameShareHasRead.setText(booksList.get(paramInt).getBookName());
        }

        public ViewHolder onCreateViewHolder(final ViewGroup paramViewGroup, int paramInt) {
            final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_share_has_read, null));
            viewHolder.chooseShareHasRead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean) {
                    int i = viewHolder.getAdapterPosition();
                    if (paramAnonymousBoolean) {
                        if (ShareAndHasReadActivity.this.type == 1) {
                            booksList.get(i).setShare(true);
                        }else{
                            booksList.get(i).setHasRead(true);
                        }
                    }
                    else{
                        if (ShareAndHasReadActivity.this.type == 1) {
                            booksList.get(i).setShare(false);
                        } else {
                            booksList.get(i).setHasRead(false);
                        }
                    }
                }
            });
            return viewHolder;
        }

//        static
//
//        public class ViewHolder
//                extends RecyclerView.ViewHolder {
//            CheckBox checkBox;
//            TextView textView;
//            View view;
//
//            public ViewHolder(View paramView) {
//                super();
//                this.view = paramView;
//                this.textView = ((TextView) this.view.findViewById(2131558869));
//                this.checkBox = ((CheckBox) this.view.findViewById(2131558870));
//            }
//        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.book_name_share_has_read)
            TextView bookNameShareHasRead;
            @BindView(R.id.choose_share_has_read)
            CheckBox chooseShareHasRead;
            View view;

            public ViewHolder(View view) {
                super(view);
                this.view=view;
                ButterKnife.bind(this, view);
            }
        }
    }
}
