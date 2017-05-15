package com.oureda.thunder.pobooks.fragment.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.RoundButton;
import com.oureda.thunder.pobooks.CustomView.WaitView;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.LocalBook;
import com.oureda.thunder.pobooks.Data.LocalBookInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static org.litepal.LitePalBase.TAG;

/**
 * Created by thunder on 17-5-14.
 */

public class FragmentLocalAuto extends Fragment {
    @BindView(R.id.recycler_local_scan)
    RecyclerView recyclerLocalScan;
    @BindView(R.id.all_choose)
    CheckBox allChoose;
    Unbinder unbinder;
    @BindView(R.id.add_book_self)
    RoundButton addBookSelf;
    @BindView(R.id.wait_local_auto)
    WaitView waitLocalAuto;
    private View view;
    private Context context;
    private HashSet<String> hashSet;
    private List<LocalBook> localBookList;
    private LocalAdapter localAdapter;
    public boolean isBusy=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_local_auto, null);
        unbinder = ButterKnife.bind(this, view);
        localBookList = new ArrayList<>();
        hashSet = new HashSet<>();
        context = MyApplication.getContext();
        queryFiles();
        initRecycle();
        allChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LocalBook localLocalBook;
                if (isChecked) {
                    Iterator<LocalBook> iterator = localBookList.iterator();
                    while (iterator.hasNext()) {
                        localLocalBook = iterator.next();
                        localLocalBook.setCheck(true);
                        Log.d(TAG, "onCheckedChanged: " + localLocalBook.getPath());
                        hashSet.add(localLocalBook.getPath());
                        localAdapter.notifyDataSetChanged();
                    }
                } else {
                    Iterator<LocalBook> iterator = localBookList.iterator();
                    while (iterator.hasNext()) {
                        localLocalBook = iterator.next();
                        localLocalBook.setCheck(false);
                        hashSet.remove(localLocalBook.getPath());
                        localAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        addBookSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < localBookList.size(); i++) {
                    final LocalBook localBook = localBookList.get(i);
                    if (localBook.isCheck()) {
                        waitLocalAuto.setVisibility(view.VISIBLE);
                        waitLocalAuto.start();
                        isBusy=true;
                        new Thread(new Runnable() {
                            public void run() {
                                Log.d(TAG, "run: isrunning " + localBook.getPath());
                                final boolean success =new LocalBookInfo.LocalBookPhrase(localBook.getPath(), localBook.getTitle()).startPhrase();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(!success){
                                            ToastUtil.showToast("你已经添加此书，请勿重复添加");
                                        }
                                        waitLocalAuto.stop();
                                        waitLocalAuto.setVisibility(View.GONE);
                                    }
                                });
                                isBusy=false;
                                if(success){
                                    Books localBooks = new Books(localBook.getPath(), localBook.getTitle());
                                    localBooks.setImageId(R.drawable.book);
                                    localBooks.setFromSd(true);
                                    localBooks.save();
                                }

                            }
                        }).start();
                    }
                }
            }
        });
        return view;
    }

    private void initRecycle() {
        localAdapter = new LocalAdapter();
        recyclerLocalScan.setAdapter(localAdapter);
        recyclerLocalScan.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void queryFiles() {
        String path = FileUtil.createRootPath(MyApplication.getContext());
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Uri.parse("content://media/external/file");
        path = "%" + path + "%";
        Cursor cursor = contentResolver.query(uri, new String[]{"_id", "_data", "_size", "date_modified"}, "_data not like ? and (_data like ? or _data like ? or _data like ? or _data like ? )", new String[]{path, "%.txt", "%.umb", "%.ekb2"}, "date_added");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalBook localLocalBook = new LocalBook();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));
                localLocalBook.setDate(new Date(new File(filePath).lastModified()));
                localLocalBook.setPath(filePath);
                String name = filePath.substring(filePath.lastIndexOf("/") + 1);
                String str = name;
                String title;
                if (str.lastIndexOf(".") > 0) {
                    str = name.substring(name.lastIndexOf(".") + 1);
                    title = name.substring(0, name.lastIndexOf("."));
                    localLocalBook.setTitle(title);
                }
                localLocalBook.setSize(new File(filePath).length());
                switch (str) {
                    case "txt":
                        localLocalBook.setImageId(1);
                        localBookList.add(localLocalBook);
                        break;
                    case "ekb2":
                        localLocalBook.setImageId(2);
                        localBookList.add(localLocalBook);
                        break;
                    case "umd":
                        localLocalBook.setImageId(3);
                        localBookList.add(localLocalBook);
                        break;
                }

            }
        }
    }

    class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder> {
        public List<LocalBook> localBooks;
        private View view;

        public LocalAdapter() {
            this.localBooks = localBookList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local_auto, null);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.checkLocalBook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = viewHolder.getAdapterPosition();
                    if (isChecked) {
                        localBookList.get(position).setCheck(true);
                        hashSet.add(localBookList.get(position).getPath());
                    } else {
                        localBookList.get(position).setCheck(false);
                        hashSet.remove(localBookList.get(position).getPath());
                    }

                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            LocalBook localBook = localBooks.get(position);
            holder.dateLocalBook.setText(new SimpleDateFormat("yyyy-MM-dd").format(localBook.getDate()));
            holder.sizeLocalBook.setText(localBook.getHumanSize());
            holder.styleLocalBook.setImageResource(localBook.getImageId());
            holder.titleLocalBook.setText(localBook.getTitle());
            holder.checkLocalBook.setChecked(localBook.isCheck());
        }


        public int getItemCount() {
            return this.localBooks.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.style_local_book)
            ImageView styleLocalBook;
            @BindView(R.id.title_local_book)
            TextView titleLocalBook;
            @BindView(R.id.size_local_book)
            TextView sizeLocalBook;
            @BindView(R.id.date_local_book)
            TextView dateLocalBook;
            @BindView(R.id.check_local_book)
            CheckBox checkLocalBook;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
