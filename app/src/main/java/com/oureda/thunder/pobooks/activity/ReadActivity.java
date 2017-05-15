package com.oureda.thunder.pobooks.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.Dialog.MyDialog;
import com.oureda.thunder.pobooks.CustomView.readView.BaseReadView;
import com.oureda.thunder.pobooks.CustomView.readView.PageChangeViewBook;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.TitleInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.person.FeelReadEditActivity;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.fragment.read.ReadFeelFragment;
import com.oureda.thunder.pobooks.fragment.read.ReadLabelFragment;
import com.oureda.thunder.pobooks.listener.OnBookStatusChangeListen;
import com.oureda.thunder.pobooks.manager.SettingManager;
import com.oureda.thunder.pobooks.services.CacheBookService;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReadActivity extends BaseActivity {

    private static final String TAG = "ReadActivity----->";
    @BindView(R.id.main_read_view)
    FrameLayout mainReadView;
    @BindView(R.id.back_read)
    ImageView backRead;
    @BindView(R.id.chapter_title_read)
    TextView chapterTitleRead;
    @BindView(R.id.download_read)
    ImageView downloadRead;
    @BindView(R.id.top_bar_read)
    LinearLayout topBarRead;
    @BindView(R.id.aa)
    LinearLayout aa;
    @BindView(R.id.label_tv_fragment)
    TextView labelTvFragment;
    @BindView(R.id.feel_tv_fragment)
    TextView feelTvFragment;
    @BindView(R.id.feel_marks_changed)
    FrameLayout feelMarksChanged;
    @BindView(R.id.fragment_read)
    FrameLayout fragmentRead;
    @BindView(R.id.download_progress_read)
    TextView downloadProgressRead;
    @BindView(R.id.toast_bar_read)
    TextView toastBarRead;
    @BindView(R.id.size_change_sb_read)
    SeekBar sizeChangeSbRead;
    @BindView(R.id.size_show_tv_read)
    TextView sizeShowTvRead;
    @BindView(R.id.padding_change_sb_read)
    SeekBar paddingChangeSbRead;
    @BindView(R.id.padding_show_tv_read)
    TextView paddingShowTvRead;
    @BindView(R.id.background_read)
    FrameLayout backgroundRead;
    @BindView(R.id.control_read)
    LinearLayout controlRead;
    @BindView(R.id.no_name_read)
    FrameLayout noNameRead;
    @BindView(R.id.catalog_read)
    TextView catalogRead;
    @BindView(R.id.note_read)
    TextView noteRead;
    @BindView(R.id.listen_read)
    TextView listenRead;
    @BindView(R.id.setting_read)
    TextView settingRead;
    @BindView(R.id.bottom_main_read)
    LinearLayout bottomMainRead;
    @BindView(R.id.finish_edit_read)
    TextView finishEditRead;
    @BindView(R.id.edit_read)
    LinearLayout editRead;
    @BindView(R.id.combine_read)
    FrameLayout combineRead;
    @BindView(R.id.progress_read)
    SeekBar progressRead;
    private String bookId = "1";
    //   private List<BookMarks> bookMarksList = new ArrayList();
    private List<ChapterInfo> chapterInfoList = new ArrayList();
    // private CircleView circleView;
    private int currentChapter;
    private CacheBookService.CacheBookBind cacheBookBind1;
    private CacheBookService cacheBookService;
    private int end;
    private List<Fragment> fragmentList;
    private int i;
    private int textSize;
    private boolean isDownloadIng = false;
    private boolean isFromSd;
    private boolean isJump = false;
    private List<ChapterInfo> list = new ArrayList();
    private int paddingSize;
    private BaseReadView pageChangeView;
    private ReadFeelFragment readFeelFragment;
    private ReadLabelFragment readLabelFragment;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            cacheBookBind1=CacheBookService.
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);
        gone(topBarRead, bottomMainRead, controlRead);
        Intent intent = getIntent();
        if(intent!=null){
            bookId=intent.getStringExtra("bookId");
            currentChapter=SettingManager.getInstance().getReadProgress(bookId)[0];
            isFromSd=intent.getBooleanExtra("isFromSd",false);
        }else{
            bookId="";
        }
        setPageChangeView();
        initData();
        bindSeekBar();
        initFragment();
        chapterTitleRead.setText(DataSupport.where("bookId = ?",bookId).find(Books.class).get(0).getAuthor());


    }

    private void bindSeekBar() {
        this.progressRead.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setCurrentChapter(progress + 1);
                setJump(true);
                LogUtil.d(TAG, "progress = " + progress);
                changeChapter(progress + 1);
                showStatusBar();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ToastUtil.showToast("调节滑块来调节章节进度");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SettingManager.getInstance().saveReadProgress(bookId, currentChapter, 0, 0);

            }
        });
        this.paddingChangeSbRead.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                float f = (float) ((1.0D * paramAnonymousInt * 0.6D + 30.0D) * 0.2D);
                setPaddingSize((int) f);
                paddingShowTvRead.setText(String.valueOf(ReadActivity.this.paddingSize));
                pageChangeView.setSize(ReadActivity.this.textSize, ReadActivity.this.paddingSize);
                showRead();
                visible(controlRead);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
                ToastUtil.showToast("滑动滑块来调节间距大小");
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SettingManager.getInstance().savePaddingSizeProgress(paramAnonymousSeekBar.getProgress());
            }
        });
        sizeChangeSbRead.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                float f = (float) (1.0D * paramAnonymousInt * 0.6D + 30.0D);
                setTextSize((int) f);
                sizeShowTvRead.setText(String.valueOf(ReadActivity.this.textSize));
                pageChangeView.setSize(ReadActivity.this.textSize, ReadActivity.this.paddingSize);
                showRead();
                visible(controlRead);
            }

            public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
                ToastUtil.showToast("调整滑块来调节字体大小");
            }

            public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                SettingManager.getInstance().saveFontSizeProgress(paramAnonymousSeekBar.getProgress());
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sizeShowTvRead.setText(String.valueOf(SettingManager.getInstance().getFontSize()));
        paddingShowTvRead.setText(String.valueOf(SettingManager.getInstance().getPaddingSize()));
        sizeChangeSbRead.setProgress(SettingManager.getInstance().getFontSizeProgress());
        paddingChangeSbRead.setProgress(SettingManager.getInstance().getPaddingSizeProgress());
        progressRead.setMax(chapterInfoList.size() - 1);
        progressRead.setProgress(SettingManager.getInstance().getReadProgress(bookId)[0] - 1);
        hideRead();
    }


    /**
     * 添加阅读界面
     */

    private void setPageChangeView() {
//        chapterInfoList.add(new ChapterInfo("1", 1, null, 0, "易云之墓", false));
//        chapterInfoList.add(new ChapterInfo("1", 2, null, 0, "姐姐", false));
//        chapterInfoList.add(new ChapterInfo("1", 3, null, 0, "如果能成为高手", false));
//        chapterInfoList.add(new ChapterInfo("1", 4, null, 0, "谁说家中无男丁", false));
//        chapterInfoList.add(new ChapterInfo("1", 5, null, 0, "连成玉", false));
        if(isFromSd){
            List<TitleInfo> titleInfos = DataSupport.where("path = ?",bookId).find(TitleInfo.class);
            for(TitleInfo titleInfo:titleInfos){
                ChapterInfo chapterInfo=new ChapterInfo(bookId,titleInfo.getNumber(),null,0,titleInfo.getTitle(),true);
                chapterInfoList.add(chapterInfo);
                Log.d(TAG, "setPageChangeView: "+titleInfo.toString());
            }
        }else{



        }

        pageChangeView = new PageChangeViewBook(this, bookId, chapterInfoList, new pageListen());
        pageChangeView.setFromSD(false);
        pageChangeView.init(1);
        mainReadView.addView(pageChangeView);
    }

    @OnClick({R.id.label_tv_fragment, R.id.feel_tv_fragment,R.id.back_read, R.id.download_read, R.id.catalog_read, R.id.note_read, R.id.listen_read, R.id.setting_read, R.id.finish_edit_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_read:
                finish();
                break;
            case R.id.download_read:

                break;
            case R.id.catalog_read:

                break;
            case R.id.note_read:
                removeFragment(readFeelFragment,readLabelFragment);
                initFragment();
                changeView(fragmentRead, mainReadView);
                gone(controlRead);
                break;
            case R.id.listen_read:
                changeView(toastBarRead);
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        gone(toastBarRead);
                    }
                }.start();
                break;
            case R.id.setting_read:
                changeView(controlRead);
                break;
            case R.id.finish_edit_read:

                ToastUtil.showToast("保存成功");
                saveContent();
                break;
            case R.id.label_tv_fragment:
                hideAndShowFragment(readFeelFragment,readLabelFragment);
                break;
            case R.id.feel_tv_fragment:
                hideAndShowFragment(readLabelFragment,readFeelFragment);
                break;
        }
    }


    class pageListen implements OnBookStatusChangeListen {

        @Override
        public void onChapterChanged(int chapter) {
            currentChapter=chapter;

        }

        @Override
        public void onPageChanged(int chapter, int page) {

        }

        @Override
        public void onLoadChapterFailure(int chapter) {

        }

        @Override
        public void onCenterClick() {
            if (isVisible(topBarRead)) {
                gone(bottomMainRead, topBarRead, controlRead);
            } else {
                visible(bottomMainRead, topBarRead);
            }

        }

        @Override
        public void onFlip() {

        }

    }

    private void changeView(View... views) {
        for (View view : views) {
            if (isVisible(view)) {
                gone(view);
            } else {
                visible(view);
            }
        }

    }

    private void changeChapter(int currentChapter) {
        if (isJump) {
            pageChangeView.jumpToChapter(currentChapter);
        }


    }

    public void setPaddingSize(int paddingSize) {
        this.paddingSize = paddingSize;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    private void hideRead() {
        gone(topBarRead, bottomMainRead, controlRead);
        hideStatusBar();
    }

    private void showRead() {
        visible(topBarRead, bottomMainRead);
        showStatusBar();
    }
    private void initFragment(){
        readFeelFragment= new ReadFeelFragment();
        readLabelFragment = new ReadLabelFragment();
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.feel_marks_changed,readFeelFragment);
        fragmentTransaction.add(R.id.feel_marks_changed,readLabelFragment);
        fragmentTransaction.hide(readFeelFragment);
        fragmentTransaction.show(readLabelFragment);
        fragmentTransaction.commit();
        }
    private void hideAndShowFragment(Fragment hideFragment,Fragment showFragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(hideFragment);
        fragmentTransaction.show(showFragment);
        fragmentTransaction.commit();

    }
    private void saveContent(){
        if(readLabelFragment!=null&&readLabelFragment!=null){
            if(readLabelFragment.isVisible()){
                readLabelFragment.saveContent();

            }else if(readFeelFragment.isVisible()){
                readFeelFragment.saveContent();
            }
        }
    }
    public void showEditRead(){
        visible(editRead);
        gone(bottomMainRead);

    }
    public String getBookId(){
        return bookId;
    }
    public int getChapter(){
        LogUtil.d(TAG,"currentChapter == "+String.valueOf(currentChapter));
        return currentChapter;
    }

    @Override
    public void onBackPressed() {
        if(bottomMainRead.getVisibility()==View.GONE){
            if(fragmentRead.getVisibility()==View.GONE){
                finish();
            }else{
                if(!readFeelFragment.getIsChanged()&&!readLabelFragment.getIsChanged()){
                    hideRead();
                    gone(bottomMainRead,editRead,fragmentRead);
                    visible(mainReadView);
                }
                if(readLabelFragment.getIsChanged()){
                    ToastUtil.showToast("read click");
                    final MyDialog myDialog = new MyDialog();
                    myDialog.initAll("提示", "你的批注尚未保存，是否退出", "保存后退出", "直接退出", new MyDialog.ButtonListener() {
                        @Override
                        public void negativeListener() {
                            if(readFeelFragment.getIsChanged()){
                                myDialog.dismiss();
                            }else{
                                hideRead();
                                gone(bottomMainRead,editRead,fragmentRead);
                                visible(mainReadView);
                                myDialog.dismiss();
                            }

                        }

                        @Override
                        public void positiveListener() {
                            if(readFeelFragment.getIsChanged()){
                                saveContent();
                                myDialog.dismiss();
                            }else{
                                saveContent();
                                hideRead();
                                visible(mainReadView);
                                gone(bottomMainRead,editRead,fragmentRead);
                                myDialog.dismiss();
                            }

                        }
                    });
                    myDialog.show(getFragmentManager(),"1");
                }if(readFeelFragment.getIsChanged()){
                    ToastUtil.showToast("feel click");
                    final MyDialog myDialog = new MyDialog();
                    myDialog.initAll("提示", "你的读后感尚未保存，是否退出", "保存后退出", "直接退出", new MyDialog.ButtonListener() {
                        @Override
                        public void negativeListener() {
                            hideRead();
                            gone(bottomMainRead,editRead,fragmentRead);
                            visible(mainReadView);
                            myDialog.dismiss();
                        }

                        @Override
                        public void positiveListener() {
                            saveContent();
                            hideRead();
                            gone(bottomMainRead,editRead,fragmentRead);
                            visible(mainReadView);
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show(getFragmentManager(),"2");
                }else{

                }
            }
        }else{
            hideRead();
            gone(bottomMainRead,editRead,fragmentRead);
            visible(mainReadView);

        }
    }
    private void removeFragment(Fragment ...fragments){
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        for(Fragment fragment:fragments){
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commit();
    }
}
