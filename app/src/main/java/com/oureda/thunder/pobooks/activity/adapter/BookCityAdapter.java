package com.oureda.thunder.pobooks.activity.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.RoundButton;
import com.oureda.thunder.pobooks.Data.SearchResult;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.utils.HttpUtils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by thunder on 17-5-15.
 */

public class BookCityAdapter extends RecyclerView.Adapter<BookCityAdapter.ViewHolder> {
    private BookCityListen bookCityListen;
    private List<SearchResult.Data> searchResults;
    private View view;

    public BookCityAdapter(List<SearchResult.Data> searchResults) {
        this.searchResults = searchResults;
    }

    public void setBookCityListen(BookCityListen bookCityListen) {
        this.bookCityListen = bookCityListen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_city_list, null);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookCityListen.intentActivity(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.authorListBookCity.setText(searchResults.get(position).getWriter());
        holder.introduceListBookCity.setText(searchResults.get(position).getIntro());
        holder.pictureListBookCity.setImageResource(R.drawable.book);
        holder.price.setContent(searchResults.get(position).getPrice()+"");
        holder.titleListBookCity.setText(searchResults.get(position).getBook_name());

    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    static

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }

    public interface BookCityListen {
        void intentActivity(int position);

    }

//    private void intentActivity(int position) {
//        SearchResult.Data localData = searchResults.get(position);
//        HttpUtils.postAsyn("http://123.206.71.182:3000/action=readbook", new Callback() {
//            public void onFailure(Call paramAnonymousCall, IOException paramAnonymousIOException) {
//            }
//
//            public void onResponse(Call paramAnonymousCall, Response paramAnonymousResponse)
//                    throws IOException {
//            }
//        }, new HttpUtils.Param[0]);
//    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.picture_list_book_city)
        ImageView pictureListBookCity;
        @BindView(R.id.title_list_book_city)
        TextView titleListBookCity;
        @BindView(R.id.author_list_book_city)
        TextView authorListBookCity;
        @BindView(R.id.price)
        RoundButton price;
        @BindView(R.id.introduce_list_book_city)
        TextView introduceListBookCity;
        private View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            pictureListBookCity= (ImageView) view.findViewById(R.id.picture_list_book_city);
            titleListBookCity= (TextView) view.findViewById(R.id.title_list_book_city);
            authorListBookCity= (TextView) view.findViewById(R.id.author_list_book_city);
            price= (RoundButton) view.findViewById(R.id.price);
            introduceListBookCity= (TextView) view.findViewById(R.id.introduce_list_book_city);
        }
    }
}
