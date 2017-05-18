package com.oureda.thunder.pobooks.activity.person;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.Dialog.MyDialog;
import com.oureda.thunder.pobooks.Data.LabelFeel;
import com.oureda.thunder.pobooks.Data.LocalBook;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteReadEditActivity extends BaseActivity {
    private boolean isChange =false;

    @BindView(R.id.content_feel_read_edit)
    EditText contentFeelReadEdit;
    @BindView(R.id.finish_feel_read_edit)
    TextView finishFeelReadEdit;
    private String bookId;
    private int chapter;
    private String content;
    private int fact_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_read_edit);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar_feel_read_edit,"批注",R.drawable.back_icon);
        Intent intent =getIntent();
        bookId = intent.getStringExtra("bookId");
        chapter=intent.getIntExtra("chapter",0);
        fact_edit=intent.getIntExtra("fact_edit",0);
        LogUtil.d("fact_edit == ",String.valueOf(fact_edit));
        if(fact_edit==0){
            ToastUtil.showToast("内部数据访问出错");
            finish();
        }else if(fact_edit==1){
            content = DataSupport.where("bookId = ? and chapter =?",bookId,String.valueOf(chapter)).find(LabelFeel.class).get(0).getLabelContent();
            LogUtil.d("edit content =",content);
            contentFeelReadEdit.setText(content);
            isChange=false;

        }else {
            contentFeelReadEdit.setText(DataSupport.where("bookId = ?").find(LabelFeel.class).get(0).getLabelContent());
            isChange=false;
        }
        contentFeelReadEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isChange=true;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChange=true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                isChange=true;
            }
        });
    }
    @OnClick(R.id.finish_feel_read_edit)
    public void onViewClicked() {
        save();
    }
    private void exitSure(){
        if (this.isChange)
        {
            final MyDialog localMyDialog = new MyDialog();
            localMyDialog.initAll("你尚未保存，是否退出", "提示", "保存后退出", "直接退出", new MyDialog.ButtonListener()
            {
                public void negativeListener()
                {
                    localMyDialog.dismiss();
                    finish();

                }

                public void positiveListener()
                {
                    localMyDialog.dismiss();
                    save();
                    finish();

                }
            });
            localMyDialog.show(getFragmentManager(), "exitAndSave");
        }
        else{
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        exitSure();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                exitSure();
                break;
        }
        return true;
    }

    private void save(){
        if(fact_edit!=0){
            if(fact_edit==1){
                LabelFeel labelFeel = new LabelFeel();
                labelFeel.setLabelContent(contentFeelReadEdit.getText().toString());
                labelFeel.setLabelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                labelFeel.updateAll("bookId = ? and chapter = ?",bookId,String.valueOf(chapter));
            }else{
                LabelFeel labelFeel = new LabelFeel();
                labelFeel.setFeelContentAll(contentFeelReadEdit.getText().toString());
                labelFeel.setFeelTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                labelFeel.updateAll("booId = ?",bookId);
            }
        }else{
            ToastUtil.showToast("内部数据访问出错");
        }
        isChange=false;


    }

}
