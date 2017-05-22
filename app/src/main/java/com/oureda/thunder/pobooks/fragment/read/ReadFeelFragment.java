package com.oureda.thunder.pobooks.fragment.read;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.LabelFeel;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
import com.oureda.thunder.pobooks.utils.LogUtil;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by thunder on 17-5-11.
 */

public class ReadFeelFragment extends Fragment {
    @BindView(R.id.time_feel_read)
    TextView timeFeelRead;
    @BindView(R.id.content_edit_feel_read)
    EditText contentEditFeelRead;
    @BindView(R.id.fragment_read_feel)
    LinearLayout fragmentReadFeel;
    Unbinder unbinder;
    @BindView(R.id.edit_feel_read)
    ImageView editFeelRead;
    private View view;
    private String bookId;
    private boolean isChange;
    private ReadActivity readActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read_feel, null);
        unbinder = ButterKnife.bind(this, view);
        readActivity = (ReadActivity) getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        bookId = readActivity.getBookId();
        List <LabelFeel>localList = DataSupport.where(new String[] { "BookId = ? ", this.bookId }).find(LabelFeel.class);
        if (localList.size() == 0) {
            timeFeelRead.setText("暂无时间");
            LabelFeel labelFeel = new LabelFeel(bookId);
            labelFeel.save();
        }else{
            timeFeelRead.setText(localList.get(0).getFeelTime());
        }
        this.contentEditFeelRead.setText(getContent());
        this.contentEditFeelRead.setFocusable(false);
        this.contentEditFeelRead.setFocusableInTouchMode(false);
        this.isChange = false;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.edit_feel_read)
    public void onViewClicked() {
        readActivity.showEditRead();
        contentEditFeelRead.setFocusableInTouchMode(true);
        contentEditFeelRead.setFocusable(true);
        contentEditFeelRead.requestFocus();
        editFeelRead.setVisibility(View.GONE);
        isChange=true;
    }

    public String getContent() {
        List<LabelFeel> list = DataSupport.select(new String[]{"bookId", "feelContentAll"}).where(new String[]{"bookId = ? ", this.bookId}).find(LabelFeel.class);
        if (list.size() == 0) {
            new LabelFeel(bookId).save();
            return "";
        }else{
            return list.get(0).getFeelContentAll();
        }

    }

    public boolean getIsChanged() {
        return this.isChange;
    }

    public void saveContent() {
        this.bookId = readActivity.getBookId();
        LabelFeel labelFeel = new LabelFeel(bookId);
        labelFeel.setDate(System.currentTimeMillis());
        labelFeel.setBookId(bookId);
        LogUtil.d("sss",bookId);
        labelFeel.setFeelContentAll(contentEditFeelRead.getText().toString());
        labelFeel.setFeelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        labelFeel.setBookAuthor((DataSupport.where(new String[]{"BookId = ?", this.bookId}).find(Books.class).get(0)).getAuthor());
        labelFeel.updateAll(new String[]{"bookId = ?", this.bookId});
        this.isChange = false;
    }
}
