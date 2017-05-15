package com.oureda.thunder.pobooks.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.R;
import com.oureda.thunder.pobooks.activity.ReadActivity;
import com.oureda.thunder.pobooks.listener.DownloadListener;
import com.oureda.thunder.pobooks.support.DownloadSupport;
import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.HttpUtils;
import com.oureda.thunder.pobooks.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by thunder on 17-5-15.
 */


public class CacheBookService extends Service
{
    public static final int TYPE_CANCEL = 2;
    public static final int TYPE_FAIL = 1;
    public static final int TYPE_SUCCESS = 0;
    private static boolean isBusy = false;
    public static boolean isCancel = false;
    private CacheBookBind cacheBookBind = new CacheBookBind();
    private DownloadListener downloadListener;
    private List<DownloadSupport.DownloadQueue> downloadQueues;


    private Notification getNotification(String title, String text)
    {
        PendingIntent localPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, ReadActivity.class), 0);
        NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this);
        localBuilder.setSmallIcon(R.mipmap.ic_launcher);
        localBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        localBuilder.setContentTitle(title);
        localBuilder.setContentIntent(localPendingIntent);
        localBuilder.setContentText(text);
        return localBuilder.build();
    }

    private NotificationManager getNotificationManager()
    {
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    public static void setIsCancel(boolean isCancel)
    {
        CacheBookService.isCancel = isCancel;
    }
    public void addToDownloadQueue(DownloadSupport.DownloadQueue downloadQueue)
    {
        boolean exists = false;
        if(downloadQueue!=null){
            // 判断当前书籍缓存任务是否存在
            for (int i = 0; i < downloadQueues.size(); i++) {
                if (downloadQueues.get(i).bookId.equals(downloadQueue.bookId)) {
                    LogUtil.d("CacheBookService","addToDownloadQueue:exists");
                    exists = true;
                    break;
                }
            }
            if (exists) {
                downloadListener.onFailStart();
                return;
            }

            // 添加到下载队列
            downloadQueues.add(downloadQueue);
            LogUtil.d("CacheBookService","addToDownloadQueue:" + downloadQueue.bookId);
            downloadListener.onStart();
        }
        if (downloadQueues.size() > 0 && !isBusy) {
            isBusy = true;
            new CacheTask(downloadListener,downloadQueues.get(0)).execute();
        }

    }

    @Nullable
    public IBinder onBind(Intent paramIntent)
    {
        return this.cacheBookBind;
    }

    public void onCreate()
    {
        this.downloadQueues = new ArrayList();
        super.onCreate();
    }

    public void setDownloadListener(DownloadListener paramDownloadListener)
    {
        this.downloadListener = paramDownloadListener;
    }

    public class CacheBookBind extends Binder
    {
        public CacheBookBind() {}

        public CacheBookService getService()
        {
            return CacheBookService.this;
        }

        public void startCache(DownloadSupport.DownloadQueue paramDownloadQueue)
        {
            CacheBookService.this.addToDownloadQueue(paramDownloadQueue);
        }
    }

    class CacheTask extends AsyncTask<Integer, Integer, Integer>
    {
        List<ChapterInfo> chapterInfoList;
        private DownloadListener downloadListener;
        private DownloadSupport.DownloadQueue downloadQueue;
        private int end;
        private int failCount = 0;
        private int start;

        public CacheTask(DownloadListener downloadListener, DownloadSupport.DownloadQueue downloadQueue)
        {
            this.downloadListener = downloadListener;
            this.downloadQueue = downloadQueue;
            this.chapterInfoList = downloadQueue.list;
            this.start = downloadQueue.start;
            this.end = downloadQueue.end;
        }

        protected Integer doInBackground(Integer... paramVarArgs) {
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            int failCount =0;

            int i = this.start;
            while (!CacheBookService.isCancel) {
                publishProgress(i);
                if (i <= this.end) {
                    File localFile = FileUtil.getChapterFile((chapterInfoList.get(i - 1)).getBookId(), i);
                    if (localFile.length() > 5L) {
                        i++;
                        continue;
                    } else {
                        try {
                            Response localResponse = HttpUtils.get(chapterInfoList.get(i - 1).getAddress());
                            if (localResponse != null) {
                                inputStream = localResponse.body().byteStream();
                                long countLength = localResponse.body().contentLength();
                                randomAccessFile = new RandomAccessFile(localFile, "rw");
                                byte[] bytes = new byte[1024];
                                long length = 0;
                                int temp;
                                while ((temp = inputStream.read(bytes)) != -1) {
                                    length += temp;
                                    randomAccessFile.write(bytes, 0, temp);
                                }
                                if(length!=countLength){
                                    failCount++;
                                }
                                i++;
                            }
                            localResponse.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(isCancel){
                        return TYPE_CANCEL;
                    }
                }

                if(i>end){
                    break;
                }
            }
            isBusy=false;
            downloadQueues.remove(0);
            if(failCount==start-end+1){
                return TYPE_FAIL;
            }else {
                return TYPE_SUCCESS;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            switch (integer){
                case TYPE_FAIL:
                    downloadListener.onFailed();
                    addToDownloadQueue(null);
                    break;
                case TYPE_CANCEL:
                    downloadListener.onCancel();
                    addToDownloadQueue(null);
                    break;
                case TYPE_SUCCESS:
                    downloadListener.onSuccess();
                    addToDownloadQueue(null);
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            downloadListener.onProgress(values[0]);
        }
    }
}

