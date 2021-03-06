package com.oureda.thunder.pobooks.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.oureda.thunder.pobooks.CustomView.Dialog.MyDialog;
import com.oureda.thunder.pobooks.CustomView.Dialog.MyDownloadDialog;
import com.oureda.thunder.pobooks.CustomView.readView.BaseReadView;
import com.oureda.thunder.pobooks.CustomView.readView.PageChangeViewBook;
import com.oureda.thunder.pobooks.Data.Books;
import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.TitleInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.base.BaseActivity;
import com.oureda.thunder.pobooks.fragment.read.ReadFeelFragment;
import com.oureda.thunder.pobooks.fragment.read.ReadLabelFragment;
import com.oureda.thunder.pobooks.listener.DownloadListener;
import com.oureda.thunder.pobooks.listener.OnBookStatusChangeListen;
import com.oureda.thunder.pobooks.manager.SettingManager;
import com.oureda.thunder.pobooks.service.CacheBookService;
import com.oureda.thunder.pobooks.support.DownloadSupport;
import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Iterator;
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
    @BindView(R.id.color1)
    ImageView color1;
    @BindView(R.id.color2)
    ImageView color2;
    @BindView(R.id.color3)
    ImageView color3;
    @BindView(R.id.color4)
    ImageView color4;
    @BindView(R.id.color5)
    ImageView color5;
    @BindView(R.id.catalog_read_frameLayout)
    FrameLayout catalogReadFrameLayout;
    @BindView(R.id.catalog_recycle_read)
    RecyclerView catalogRecycleRead;
    private String bookId = "1";
    //   private List<BookMarks> bookMarksList = new ArrayList();
    private List<ChapterInfo> chapterInfoList = new ArrayList();
    // private CircleView circleView;
    private int currentChapter;
    private CacheBookService.CacheBookBind cacheBookBind1;
    private CacheBookService cacheBookService;
    private int end;
    private int start;
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
    private int cacheEnd = 0;
    private int cacheStart = 0;
    CatalogAdapter catalogAdapter;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            cacheBookBind1 = (CacheBookService.CacheBookBind) service;
            LogUtil.d("run", "run");
            cacheBookService = cacheBookBind1.getService();
            if (cacheBookService == null) {
                LogUtil.e("cacheBookService == null", "error");
            } else {
                LogUtil.d("cacheBookService != null", "debug");
            }
            cacheBookService.setDownloadListener(new DownloadListener() {
                @Override
                public void onCancel(boolean isAuto) {
                }

                @Override
                public void onFailStart(boolean isAuto) {
                    if (!isAuto) {
                        ReadActivity.this.downloadProgressRead.setVisibility(View.VISIBLE);
                        ReadActivity.this.downloadProgressRead.setText("无法开始下载任务已存在");
                        isDownloadIng = false;
                    }

                }

                @Override
                public void onFailed(boolean isAuto) {
                    if (!isAuto) {
                        ToastUtil.showToast("下载失败");
                        ReadActivity.this.downloadProgressRead.setVisibility(View.GONE);
                        isDownloadIng = true;
                    }

                }

                @Override
                public void onProgress(int paramInt, boolean isAuto) {
                    if (!isAuto) {
                        int i = end;
                        int j = start;
                        ReadActivity.this.downloadProgressRead.setText("正在缓存第 " + paramInt + "/" + ReadActivity.this.end + "章  ,总" + (i - j + 1) + "章");
                    }
                }


                @Override
                public void onStart(boolean isAuto) {
                    if (!isAuto) {
                        LogUtil.d("start download", "start");
                        ReadActivity.this.downloadProgressRead.setText("开始下载");
                    }


                }

                @Override
                public void onSuccess(boolean isAuto) {
                    if (!isAuto) {
                        ToastUtil.showToast("下载成功");
                        ReadActivity.this.downloadProgressRead.setVisibility(View.GONE);
                        isDownloadIng = false;
                    } else {
                        pageChangeView.init(1);
                    }

                }

                @Override
                public void onWait(boolean isAuto) {
                    if (!isAuto) {
                        ReadActivity.this.downloadProgressRead.setVisibility(View.VISIBLE);
                        ReadActivity.this.downloadProgressRead.setText("已加入缓存队列");
                    }

                }
            });
            initChapterInfo();
            autoCacheGo();
            setPageChangeView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            bookId = intent.getStringExtra("bookId");
            currentChapter = SettingManager.getInstance().getReadProgress(bookId)[0];
            isFromSd = intent.getBooleanExtra("isFromSd", false);
        } else {
            bookId = "";
        }
        //其实会有问题啊
        if (!isFromSd) {
            Intent bindIntent = new Intent(ReadActivity.this, CacheBookService.class);
            bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
        }
        gone(topBarRead, bottomMainRead, controlRead);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFromSd) {
            chapterInfoList=DataSupport.where("bookId=?",bookId).find(ChapterInfo.class);
            List<TitleInfo> titleInfoList=DataSupport.where("path =?",bookId).find(TitleInfo.class);
            LogUtil.d("ssssssss",titleInfoList.size()+" ");
            for(TitleInfo t:titleInfoList){
                ChapterInfo c = new ChapterInfo(t.getPath(),t.getNumber(),null,0,t.getTitle(),true);
                chapterInfoList.add(c);
            }
            LogUtil.d("ssssssssss",chapterInfoList.size()+"");
            setPageChangeView();

        }
        initData();
        bindSeekBar();
        initFragment();
        chapterTitleRead.setText(DataSupport.where("bookId = ?", bookId).find(Books.class).get(0).getAuthor());

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
        progressRead.setMax(chapterInfoList.size());
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


        pageChangeView = new PageChangeViewBook(this, bookId, chapterInfoList, new pageListen());
        pageChangeView.init(1);
        mainReadView.addView(pageChangeView);
    }

    @OnClick({R.id.color1, R.id.color2, R.id.color3, R.id.color4, R.id.color5,R.id.label_tv_fragment, R.id.feel_tv_fragment, R.id.back_read, R.id.download_read, R.id.catalog_read, R.id.note_read, R.id.listen_read, R.id.setting_read, R.id.finish_edit_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_read:
                finish();
                break;
            case R.id.download_read:
                gone(catalogReadFrameLayout);
                initDownloadDialog();
                break;
            case R.id.catalog_read:
                showCatalog();
                gone(fragmentRead);
                if(catalogReadFrameLayout.getVisibility()==View.VISIBLE){
                    gone(catalogReadFrameLayout);
                    visible(mainReadView);
                }else{
                    gone(mainReadView);
                    visible(catalogReadFrameLayout);
                }
                break;
            case R.id.note_read:
                gone(catalogReadFrameLayout);
                removeFragment(readFeelFragment, readLabelFragment);
                initFragment();
               if(fragmentRead.getVisibility()==View.VISIBLE){
                   gone(fragmentRead);
                   visible(mainReadView);
               }else{
                   gone(mainReadView);
                   visible(fragmentRead);
               }
                gone(controlRead);
                break;
            case R.id.listen_read:

                gone(catalogReadFrameLayout);
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
                gone(catalogReadFrameLayout);
                changeView(controlRead);
                break;
            case R.id.finish_edit_read:
                ToastUtil.showToast("保存成功");
                saveContent();
                break;
            case R.id.label_tv_fragment:
                hideAndShowFragment(readFeelFragment, readLabelFragment);
                break;
            case R.id.feel_tv_fragment:
                hideAndShowFragment(readLabelFragment, readFeelFragment);
                break;
            case R.id.color1:
                pageChangeView.setTextColor(getResources().getColor(R.color.textColorOne),getResources().getColor(R.color.textColorOne));
                pageChangeView.setReadColor(getResources().getColor(R.color.backgroundOne));
                break;
            case R.id.color2:
                pageChangeView.setTextColor(getResources().getColor(R.color.textColorTwo),getResources().getColor(R.color.textColorTwo));
                pageChangeView.setReadColor(getResources().getColor(R.color.backgroundTwo));
                break;
            case R.id.color3:
                pageChangeView.setTextColor(getResources().getColor(R.color.textColorThree),getResources().getColor(R.color.textColorThree));

                pageChangeView.setReadColor(getResources().getColor(R.color.backgroundThree));
                break;
            case R.id.color4:
                pageChangeView.setTextColor(getResources().getColor(R.color.textColorFour),getResources().getColor(R.color.textColorFour));

                pageChangeView.setReadColor(getResources().getColor(R.color.backgroundFour));
                break;
            case R.id.color5:
                pageChangeView.setTextColor(getResources().getColor(R.color.textColorFive),getResources().getColor(R.color.textColorFive));

                pageChangeView.setReadColor(getResources().getColor(R.color.backgroundFive));
                break;
        }
    }



    class pageListen implements OnBookStatusChangeListen {

        @Override
        public void onChapterChanged(int chapter) {
            currentChapter = chapter;
            if (!isFromSd) {
                if (currentChapter == cacheEnd - 1) {
                    autoCacheGo();
                }
            }

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

    private void initFragment() {
        readFeelFragment = new ReadFeelFragment();
        readLabelFragment = new ReadLabelFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.feel_marks_changed, readFeelFragment);
        fragmentTransaction.add(R.id.feel_marks_changed, readLabelFragment);
        fragmentTransaction.hide(readFeelFragment);
        fragmentTransaction.show(readLabelFragment);
        fragmentTransaction.commit();
    }

    private void hideAndShowFragment(Fragment hideFragment, Fragment showFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(hideFragment);
        fragmentTransaction.show(showFragment);
        fragmentTransaction.commit();

    }

    private void saveContent() {
        if (readLabelFragment != null && readLabelFragment != null) {
            if (readLabelFragment.isVisible()) {
                readLabelFragment.saveContent();

            } else if (readFeelFragment.isVisible()) {
                readFeelFragment.saveContent();
            }
        }
    }

    public void showEditRead() {
        visible(editRead);
        gone(bottomMainRead);

    }

    public String getBookId() {
        return bookId;
    }

    public int getChapter() {
        LogUtil.d(TAG, "currentChapter == " + String.valueOf(currentChapter));
        return currentChapter;
    }

    @Override
    public void onBackPressed() {
        gone(catalogReadFrameLayout);
        if (bottomMainRead.getVisibility() == View.GONE) {
            if (fragmentRead.getVisibility() == View.GONE) {
                if (chapterInfoList.size() > 0) {
                    if (chapterInfoList.get(0).isTemp()) {
                        final MyDialog myDialog = new MyDialog();
                        myDialog.initAll("提示", "是否将此书加入书架", "加入书架", "不，谢谢", new MyDialog.ButtonListener() {
                            @Override
                            public void negativeListener() {
                                myDialog.dismiss();
                                FileUtil.deleteBook(bookId);
                                finish();
                            }

                            @Override
                            public void positiveListener() {
                                ChapterInfo chapterInfo = new ChapterInfo();
                                chapterInfo.setToDefault("isTemp");
                                LogUtil.d("ssss", DataSupport.where("bookId=?", bookId).find(ChapterInfo.class).get(0).isTemp() + " ");
                                chapterInfo.updateAll("bookId = ?", bookId);
                                LogUtil.d("ssss", DataSupport.where("bookId=?", bookId).find(ChapterInfo.class).get(0).isTemp() + " ");
                                Books books = new Books();
                                books.setToDefault("isTemp");
//                            LogUtil.d("sssss  ", " "+DataSupport.where("isTemp = ?","0").find(Books.class).size());
//                            LogUtil.d("sssss  ", " "+DataSupport.where("isTemp = ?","1").find(Books.class).size());
                                LogUtil.d("bookId=?", bookId);
                                LogUtil.d("ssss", DataSupport.where("BookId=?", bookId).find(Books.class).get(0).isTemp() + " ");

                                books.updateAll("BookId = ?", bookId);
                                LogUtil.d("ssss", DataSupport.where("BookId=?", bookId).find(Books.class).get(0).isTemp() + " ");

//                            LogUtil.d("sssss  ", " "+DataSupport.where("isTemp = ?","1").find(Books.class).size());
//                            LogUtil.d("sssss  ", " "+DataSupport.where("isTemp = ?","0").find(Books.class).size());
                                myDialog.dismiss();
                                finish();

                            }
                        });
                        myDialog.show(getFragmentManager(), "3");
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }

            } else {
                if (!readFeelFragment.getIsChanged() && !readLabelFragment.getIsChanged()) {
                    hideRead();
                    gone(bottomMainRead, editRead, fragmentRead);
                    visible(mainReadView);
                }
                if (readLabelFragment.getIsChanged()) {
                    ToastUtil.showToast("read click");
                    final MyDialog myDialog = new MyDialog();
                    myDialog.initAll("提示", "你的批注尚未保存，是否退出", "保存后退出", "直接退出", new MyDialog.ButtonListener() {
                        @Override
                        public void negativeListener() {
                            if (readFeelFragment.getIsChanged()) {
                                myDialog.dismiss();
                            } else {
                                hideRead();
                                gone(bottomMainRead, editRead, fragmentRead);
                                visible(mainReadView);
                                myDialog.dismiss();
                            }

                        }

                        @Override
                        public void positiveListener() {
                            if (readFeelFragment.getIsChanged()) {
                                saveContent();
                                myDialog.dismiss();
                            } else {
                                saveContent();
                                hideRead();
                                visible(mainReadView);
                                gone(bottomMainRead, editRead, fragmentRead);
                                myDialog.dismiss();
                            }

                        }
                    });
                    myDialog.show(getFragmentManager(), "1");
                }
                if (readFeelFragment.getIsChanged()) {
                    ToastUtil.showToast("feel click");
                    final MyDialog myDialog = new MyDialog();
                    myDialog.initAll("提示", "你的读后感尚未保存，是否退出", "保存后退出", "直接退出", new MyDialog.ButtonListener() {
                        @Override
                        public void negativeListener() {
                            hideRead();
                            gone(bottomMainRead, editRead, fragmentRead);
                            visible(mainReadView);
                            myDialog.dismiss();
                        }

                        @Override
                        public void positiveListener() {
                            saveContent();
                            hideRead();
                            gone(bottomMainRead, editRead, fragmentRead);
                            visible(mainReadView);
                            myDialog.dismiss();
                        }
                    });
                    myDialog.show(getFragmentManager(), "2");
                } else {

                }
            }
        } else {
            hideRead();
            gone(bottomMainRead, editRead, fragmentRead);
            visible(mainReadView);

        }
    }

    private void removeFragment(Fragment... fragments) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commit();
    }

    private void initDownloadDialog() {
        final MyDownloadDialog localMyDownloadDialog = new MyDownloadDialog();
        localMyDownloadDialog.initAll(new MyDownloadDialog.ButtonListener() {
            public void allListener() {
                start = 1;
                end = chapterInfoList.size();
                cacheBookBind1.startCache(new DownloadSupport.DownloadQueue(ReadActivity.this.start, ReadActivity.this.chapterInfoList, ReadActivity.this.end, ReadActivity.this.bookId, false));
                isDownloadIng = true;
                downloadProgressRead.setText("正在初始化------>");
                downloadProgressRead.setVisibility(View.VISIBLE);
                localMyDownloadDialog.dismiss();
            }

            public void only50Listener() {
                start = currentChapter;
                end = currentChapter + 50;
                if (end > chapterInfoList.size()) {
                    end = chapterInfoList.size();
                }
                cacheBookBind1.startCache(new DownloadSupport.DownloadQueue(1, ReadActivity.this.chapterInfoList, ReadActivity.this.chapterInfoList.size(), bookId, false));
                isDownloadIng = true;
                downloadProgressRead.setText("正在初始化------>");
                downloadProgressRead.setVisibility(View.VISIBLE);
                localMyDownloadDialog.dismiss();
            }

            public void onlyBackListener() {
                start = currentChapter;
                end = chapterInfoList.size();
                ReadActivity.this.cacheBookBind1.startCache(new DownloadSupport.DownloadQueue(ReadActivity.this.start, ReadActivity.this.chapterInfoList, ReadActivity.this.end, ReadActivity.this.bookId, false));
            }
        });
        localMyDownloadDialog.show(getFragmentManager(), "downloadDialog");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isFromSd)
        unbindService(serviceConnection);
    }

    private void autoCacheGo() {

        int start = currentChapter > 1 ? currentChapter - 1 : currentChapter;
        int end = currentChapter + 3 > chapterInfoList.size() ? chapterInfoList.size() : currentChapter + 3;
        cacheEnd = end;
        LogUtil.d("CacheEnd == ", cacheEnd + " start == " + start + " chapterInfoList ==" + chapterInfoList.size() + currentChapter + "= currentChapter");
        cacheBookBind1.startCache(new DownloadSupport.DownloadQueue(start, chapterInfoList, end, bookId, true));
    }

    private void initChapterInfo() {
        if (isFromSd) {
            List<TitleInfo> titleInfos = DataSupport.where("path = ?", bookId).find(TitleInfo.class);
            for (TitleInfo titleInfo : titleInfos) {
                ChapterInfo chapterInfo = new ChapterInfo(bookId, titleInfo.getNumber(), null, 0, titleInfo.getTitle(), true);
                chapterInfoList.add(chapterInfo);
                Log.d(TAG, "setPageChangeView: " + titleInfo.toString());
            }
        } else {
            chapterInfoList = DataSupport.where("bookId = ?", bookId).find(ChapterInfo.class);
        }
    }
    class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder>{
        public CatalogAdapter(List<ChapterInfo> chapterInfoList) {
            this.chapterInfoList = chapterInfoList;
        }

        List<ChapterInfo> chapterInfoList;
        View view;

        @Override
        public CatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog_read,null);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =viewHolder.getAdapterPosition();
                    isJump=true;
                    changeChapter(position+1);
                    catalogReadFrameLayout.setVisibility(View.GONE);
                    visible(mainReadView);
                }
            });
            return viewHolder;

        }

        @Override
        public void onBindViewHolder(CatalogAdapter.ViewHolder holder, int position) {
           ChapterInfo chapterInfo = chapterInfoList.get(position);
            if(currentChapter==position+1){
                holder.imageview.setImageResource(R.drawable.bookmark);
            }else{
                holder.imageview.setImageResource(R.drawable.unread);
            }
            holder.textView.setText("第"+chapterInfo.getChapter()+"章  "+chapterInfo.getChapterName());
        }

        @Override
        public int getItemCount() {
            return chapterInfoList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            ImageView imageview;

            public ViewHolder(View itemView) {
                super(itemView);
                textView= (TextView) itemView.findViewById(R.id.catalog_tv);
                imageview= (ImageView) itemView.findViewById(R.id.catalog_image);
            }
        }
    }
    private void showCatalog(){
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.scrollToPositionWithOffset(currentChapter,1);
        catalogRecycleRead.setLayoutManager(linearLayoutManager);
        catalogAdapter=new CatalogAdapter(chapterInfoList);
        catalogRecycleRead.setAdapter(catalogAdapter);
    }
}
