package com.oureda.thunder.pobooks.listener;

/**
 * Created by thunder on 17-5-15.
 */

public  interface DownloadListener
{
     void onCancel(boolean isAuto);

     void onFailStart(boolean isAuto);

     void onFailed(boolean isAuto);

     void onProgress(int paramInt,boolean isAuto);

     void onStart(boolean isAuto);

     void onSuccess(boolean isAuto);

     void onWait(boolean isAuto);
}
