package com.oureda.thunder.pobooks.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_book_detail)
    Toolbar toolbarBookDetail;
    @BindView(R.id.photo_book_details)
    ImageView photoBookDetails;
    @BindView(R.id.book_name_details)
    TextView bookNameDetails;
    @BindView(R.id.book_author_details)
    TextView bookAuthorDetails;
    @BindView(R.id.book_price_details)
    TextView bookPriceDetails;
    @BindView(R.id.pay_book_details)
    ImageView payBookDetails;
    @BindView(R.id.read_book_detail)
    ImageView readBookDetail;
    @BindView(R.id.introduce_book_detail)
    TextView introduceBookDetail;
    @BindView(R.id.book_picture1_details)
    ImageView bookPicture1Details;
    @BindView(R.id.book_name1_details)
    TextView bookName1Details;
    @BindView(R.id.book_picture2_details)
    ImageView bookPicture2Details;
    @BindView(R.id.book_name2_details)
    TextView bookName2Details;
    @BindView(R.id.book_picture3_details)
    ImageView bookPicture3Details;
    @BindView(R.id.book_name3_details)
    TextView bookName3Details;
    @BindView(R.id.book_time_book_information)
    TextView bookTimeBookInformation;
    @BindView(R.id.book_price_book_information)
    TextView bookPriceBookInformation;
    @BindView(R.id.book_sort_book_information)
    TextView bookSortBookInformation;
    @BindView(R.id.book_number_book_information)
    TextView bookNumberBookInformation;
    private String bookId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        Intent intent =getIntent();
        this.bookId = intent.getStringExtra("book_id");
        this.bookNameDetails.setText(intent.getStringExtra("book_name"));
        this.introduceBookDetail.setText(intent.getStringExtra("book_info"));
        this.bookAuthorDetails.setText(intent.getStringExtra("book_author"));
        initToolbar(R.id.toolbar_book_detail, "书籍信息", R.drawable.back_icon);
    }

    @OnClick({R.id.pay_book_details, R.id.read_book_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_book_details:
                ToastUtil.showToast("支付版pobooks，即将上市，敬请期待");
                break;
            case R.id.read_book_detail:
                Intent intent = new Intent(BookDetailActivity.this, ReadActivity.class);
                intent.putExtra("bookId",  bookId);
                intent.putExtra("isFromSd", false);
                Books books =new Books(bookId,intent.getStringExtra("book_name"));
                books.setFromSd(false);
                books.setTemp(true);
                books.setImageId(R.drawable.book);
                books.save();
                LogUtil.d("ssss", DataSupport.where("BookId=?",books.getBookId()).find(Books.class).get(0).isTemp()+" ");
                startActivity(intent);
                break;
        }
    }
}
