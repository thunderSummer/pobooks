package com.oureda.thunder.pobooks.listener;

/**
 * Created by thunder on 17-5-15.
 */

public  interface DownloadListener
{
     void onCancel();

     void onFailStart();

     void onFailed();

     void onProgress(int paramInt);

     void onStart();

     void onSuccess();

     void onWait();
}
