package com.oureda.thunder.pobooks.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
import com.oureda.thunder.pobooks.activity.main.MainActivity;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.baseInterface.AdapterChangedListener;
import com.oureda.thunder.pobooks.utils.LogUtil;

import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thunder on 17-5-13.
 */


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private static String TAG="BookAdapter---->";
    private AdapterChangedListener adapterChangedListener;
    private List<Books> booksList;
    private HashSet<String> chooseSet = new HashSet();
    private static boolean isChooseAll = false;
    private Context mContext;
    private View view;

    public BookAdapter(List<Books> booksList) {
        this.booksList = booksList;
    }

    public BookAdapter(boolean paramBoolean, List<Books> paramList) {
        this.booksList = paramList;
    }

    public BookAdapter(boolean paramBoolean, List<Books> paramList, AdapterChangedListener paramAdapterChangedListener) {
        this.booksList = paramList;
        this.adapterChangedListener = paramAdapterChangedListener;
    }

    private void enterChooseAll() {
        this.isChooseAll = true;
        this.adapterChangedListener.OnAdapterRefresh(true);
    }

    public void clearChooseSet() {
        this.chooseSet.clear();
    }

    public HashSet<String> getChooseSet() {
        return this.chooseSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_main,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
                viewHolder.bookImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                int i =viewHolder.getAdapterPosition();
                if (!isChooseAll) {
                    Intent intent= new Intent(BookAdapter.this.mContext, ReadActivity.class);
                    intent.putExtra("bookId",  BookAdapter.this.booksList.get(i).getBookId());
                    intent.putExtra("isFromSd", BookAdapter.this.booksList.get(i).isFromSd());
                    LogUtil.d("mainActivity","from SD ="+booksList.get(i).isFromSd());
                    BookAdapter.this.mContext.startActivity(intent);
                }else {
                    if(booksList.get(i).isCheck()){
                        booksList.get(i).setIsCheck(false);
                        viewHolder.checkBoxRead.setChecked(false);
                        chooseSet.remove( booksList.get(i).getBookId());
                    }else{
                        booksList.get(i).setIsCheck(true);
                        viewHolder.checkBoxRead.setChecked(true);
                        chooseSet.add( booksList.get(i).getBookId());
                    }

                }
            }
        });
        viewHolder.bookImage.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View paramAnonymousView) {
                LogUtil.d(TAG,isChooseAll+"== chooseAll");
                if (!isChooseAll) {
                    enterChooseAll();
                    return true;
                }else{
                    cancleChooseAll();
                    return true;
                }
            }
        });
        viewHolder.checkBoxRead.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                int i = viewHolder.getAdapterPosition();
                if (viewHolder.checkBoxRead.isChecked()) {
                    chooseSet.add((BookAdapter.this.booksList.get(i)).getBookId());
                }else{
                    chooseSet.remove(BookAdapter.this.booksList.get(i).getBookId());
                }
            }
        });
        return viewHolder;
    }
    private void cancleChooseAll(){
        isChooseAll=false;
        adapterChangedListener.OnAdapterRefresh(false);

    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Books localBooks = this.booksList.get(position);
        Log.d(TAG, "onBindViewHolder: "+booksList.size());
        holder.bookName.setText(localBooks.getBookName());
        holder.bookProgress.setText("已读" + localBooks.getProgress() + "章");
        holder.checkBoxRead.setChecked(localBooks.isCheck());
        if (localBooks.isFromSd()) {
            holder.bookImage.setImageResource(localBooks.getImageId());
//            if (!this.isChooseAll) {
//                holder.checkboxRead.setVisibility(View.VISIBLE);
//            }
//            holder.checkboxRead.setVisibility(View.GONE);
        }else{
            holder.bookImage.setImageResource(localBooks.getImageId());
        }
        if (!this.isChooseAll) {
            holder.checkBoxRead.setVisibility(View.GONE);
        }else{
            holder.checkBoxRead.setVisibility(View.VISIBLE);
        }


    }

    public int getItemCount() {
        return this.booksList.size();
    }


    public void setChooseAll(boolean paramBoolean) {
        this.isChooseAll = paramBoolean;
    }

    public void setChooseSet(HashSet<String> paramHashSet) {
        this.chooseSet = paramHashSet;
    }
static class ViewHolder extends RecyclerView.ViewHolder {
    TextView bookName;
    TextView bookProgress;
    CheckBox checkBoxRead;
    ImageView bookImage;

    public ViewHolder(View itemView) {
        super(itemView);
        bookName= (TextView) itemView.findViewById(R.id.book_name);
        bookProgress= (TextView) itemView.findViewById(R.id.book_progress);
        checkBoxRead = (CheckBox) itemView.findViewById(R.id.checkbox_read);
        bookImage= (ImageView) itemView.findViewById(R.id.book_image);
    }
}


}
