package com.oureda.thunder.pobooks.fragment.bookRoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
import com.oureda.thunder.pobooks.activity.main.ShareAndHasReadActivity;
import com.oureda.thunder.pobooks.base.MyApplication;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by thunder on 17-5-14.
 */

public class FragmentRoomShare extends Fragment {
    @BindView(R.id.recycle_share_book_room)
    RecyclerView recycleShareBookRoom;
    @BindView(R.id.fab_share_book_room)
    FloatingActionButton fabShareBookRoom;
    Unbinder unbinder;
    private View view;
    private Adapter adapter;
    private List<Books> booksList;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room_share, null);
        unbinder = ButterKnife.bind(this, view);
        context= MyApplication.getContext();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycle();
    }

    private void initList() {
        booksList = DataSupport.where(new String[]{"isShare == ?", "1"}).find(Books.class);
    }

    private void initRecycle() {
        initList();
        this.adapter = new Adapter(this.booksList);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(getContext());
        recycleShareBookRoom.setAdapter(this.adapter);
        recycleShareBookRoom.setLayoutManager(localLinearLayoutManager);
    }

    @OnClick(R.id.fab_share_book_room)
    public void onViewClicked() {
        Intent intent = new Intent(context, ShareAndHasReadActivity.class);
        intent.putExtra("style_has_read_share",1);
        startActivity(intent);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        List<Books> booksList;

        public Adapter(List<Books> booksList) {
            this.booksList = booksList;
        }

        public int getItemCount() {
            return this.booksList.size();
        }

        public void onBindViewHolder(ViewHolder paramViewHolder, int paramInt) {
            Books localBooks = (Books) this.booksList.get(paramInt);
            paramViewHolder.bookAuthorShareBookRoom.setText(localBooks.getAuthor());
            paramViewHolder.bookNameShareBookRoom.setText(localBooks.getBookName());
//            paramViewHolder.timeAuthorShareBookRoom.setText(localBooks.getTime());
        }

        public ViewHolder onCreateViewHolder(final ViewGroup paramViewGroup, int paramInt) {
            final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_share_list, null));
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(context, ReadActivity.class);
                    int i = viewHolder.getAdapterPosition();
                    intent.putExtra("bookId", booksList.get(i).getBookId());
                    intent.putExtra("isFromSd", booksList.get(i).isFromSd());
                    startActivity(intent);
                }
            });
            return viewHolder;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.picture_share_book_room)
            ImageView pictureShareBookRoom;
            @BindView(R.id.book_name_share_book_room)
            TextView bookNameShareBookRoom;
            @BindView(R.id.book_author_share_book_room)
            TextView bookAuthorShareBookRoom;
            @BindView(R.id.time_author_share_book_room)
            TextView timeAuthorShareBookRoom;
            private View view;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                ButterKnife.bind(this, view);
            }
        }
    }
}
