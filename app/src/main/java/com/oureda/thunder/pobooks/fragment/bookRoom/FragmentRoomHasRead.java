package com.oureda.thunder.pobooks.fragment.bookRoom;

import android.app.FragmentManager;
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

import com.oureda.thunder.pobooks.Data.BookInfo;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
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

public class FragmentRoomHasRead extends Fragment {
    @BindView(R.id.recycle_has_read_book_room)
    RecyclerView recycleHasReadBookRoom;
    @BindView(R.id.fab_has_read_book_room)
    FloatingActionButton fabHasReadBookRoom;
    Unbinder unbinder;
    private View view;
    private List<Books> booksList;
    private Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_room_has_read, null);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_has_read_book_room)
    public void onViewClicked() {
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
        List<Books> booksList;

        public Adapter(List<Books> booksList) {
            this.booksList = booksList;
        }

        public int getItemCount() {
            return this.booksList.size();
        }

        public void onBindViewHolder(ViewHolder paramViewHolder, int position) {
            Books localBooks = booksList.get(position);
            paramViewHolder.authorHasReadBookRoom.setText(localBooks.getAuthor());
            paramViewHolder.nameHasReadBookRoom.setText(localBooks.getBookName());
            paramViewHolder.timeHasReadBookRoom.setText(localBooks.getTime());
            paramViewHolder.readTimesHasReadBookRoom.setText("已读" + localBooks.getReadTimes() + "几次");
        }

        public ViewHolder onCreateViewHolder(final ViewGroup paramViewGroup, int paramInt) {
            final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_has_read_list, null));
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(MyApplication.getContext(), ReadActivity.class);
                    int i = viewHolder.getAdapterPosition();
                    intent.putExtra("bookId", booksList.get(i).getBookId());
                    intent.putExtra("isFromSd", booksList.get(i).isFromSd());
                    startActivity(intent);
                }
            });
            viewHolder.addOneHasReadBookRoom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    int i = viewHolder.getAdapterPosition();
                    Books books = booksList.get(i);
                    books.setReadTimes(books.getReadTimes() + 1);
                    books.save();
                    initRecycle();

                }
            });
            return viewHolder;
        }

//        static class ViewHolder
//                extends RecyclerView.ViewHolder {
//            ImageView addOne;
//            TextView bookAuthor;
//            TextView bookName;
//            ImageView imageView;
//            TextView readTimes;
//            TextView time;
//            View view;
//
//            public ViewHolder(View paramView) {
//                super();
//                this.view = paramView;
//                this.imageView = ((ImageView) paramView.findViewById(2131558839));
//                this.bookAuthor = ((TextView) paramView.findViewById(2131558841));
//                this.bookName = ((TextView) paramView.findViewById(2131558840));
//                this.time = ((TextView) paramView.findViewById(2131558843));
//                this.addOne = ((ImageView) paramView.findViewById(2131558844));
//                this.readTimes = ((TextView) paramView.findViewById(2131558842));
//            }
//        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.picture_has_read_book_room)
            ImageView pictureHasReadBookRoom;
            @BindView(R.id.name_has_read_book_room)
            TextView nameHasReadBookRoom;
            @BindView(R.id.author_has_read_book_room)
            TextView authorHasReadBookRoom;
            @BindView(R.id.read_times_has_read_book_room)
            TextView readTimesHasReadBookRoom;
            @BindView(R.id.time_has_read_book_room)
            TextView timeHasReadBookRoom;
            @BindView(R.id.add_one_has_read_book_room)
            ImageView addOneHasReadBookRoom;
            View view;

            public ViewHolder(View view) {
                super(view);
                this.view=view;
                ButterKnife.bind(this, view);
            }
        }
    }
    private void initRecycle(){
        booksList=  DataSupport.where(new String[] { "isHasRead == ?", "1" }).find(Books.class);
        adapter=new Adapter(booksList);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext());
        recycleHasReadBookRoom.setLayoutManager(linearLayoutManager);
        recycleHasReadBookRoom.setAdapter(adapter);
    }
}
