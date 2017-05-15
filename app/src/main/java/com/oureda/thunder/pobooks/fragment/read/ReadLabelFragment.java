package com.oureda.thunder.pobooks.fragment.read;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static org.litepal.LitePalBase.TAG;

/**
 * Created by thunder on 17-5-11.
 */

public class ReadLabelFragment extends Fragment {
    @BindView(R.id.time_label_read)
    TextView timeLabelRead;
    @BindView(R.id.edit_label_read)
    ImageView editLabelRead;
    @BindView(R.id.content_edit_label_read)
    EditText contentEditLabelRead;
    @BindView(R.id.fragment_read_label)
    LinearLayout fragmentReadLabel;
    Unbinder unbinder;
    private View view;
    private String bookId;
    private int chapter;
    private ReadActivity readActivity;
    private boolean isChange;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read_label, null);
        unbinder = ButterKnife.bind(this, view);
        readActivity= (ReadActivity) getActivity();
        chapter=readActivity.getChapter();
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
        chapter=readActivity.getChapter();
        Log.d(TAG, "onResume: "+chapter);
        bookId = readActivity.getBookId();
        List<LabelFeel> localList = DataSupport.where(new String[] { "BookId = ? and chapter = ?", this.bookId,String.valueOf(chapter) }).find(LabelFeel.class);
        if (localList.size() == 0) {
            Log.d(TAG, "onResume: pass");
            LabelFeel labelfeel =new LabelFeel(bookId,chapter);
            labelfeel.save();
        }else{
            timeLabelRead.setText(localList.get(0).getLabelTime());
        }
        this.contentEditLabelRead.setText(getContent());
        this.contentEditLabelRead.setFocusable(false);
        this.contentEditLabelRead.setFocusableInTouchMode(false);
        this.isChange = false;
    }

    @OnClick(R.id.edit_label_read)
    public void onViewClicked() {
        readActivity.showEditRead();
        contentEditLabelRead.setFocusableInTouchMode(true);
        contentEditLabelRead.setFocusable(true);
        contentEditLabelRead.requestFocus();
        editLabelRead.setVisibility(View.GONE);
        isChange=true;
    }
    private String getContent(){
        List<LabelFeel> list = DataSupport.select(new String[]{"bookId", "labelContent","chapter"}).where(new String[]{"bookId = ? and chapter = ? ", this.bookId,String.valueOf(chapter)}).find(LabelFeel.class);
        if (list.size() == 0) {
            new LabelFeel(bookId).save();
            return "";
        }else{
            return list.get(0).getLabelContent();
        }

    }
    public boolean getIsChanged() {
        return this.isChange;
    }

    public void saveContent() {
        this.bookId = readActivity.getBookId();
        LabelFeel labelFeel = new LabelFeel(bookId);
        labelFeel.setDate(System.currentTimeMillis());
        labelFeel.setLabelContent(contentEditLabelRead.getText().toString());
        labelFeel.setLabelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        labelFeel.setBookAuthor((DataSupport.where(new String[]{"BookId = ?", this.bookId}).find(Books.class).get(0)).getAuthor());
        labelFeel.updateAll("bookId = ? and chapter = ?", this.bookId,String.valueOf(chapter));
        Log.d(TAG, "saveContent: onRes"+chapter+DataSupport.where("bookId =? and chapter =?",bookId,String.valueOf(chapter)).find(LabelFeel.class).get(0).getLabelContent());
        this.isChange = false;
    }
}
