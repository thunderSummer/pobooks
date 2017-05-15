package com.oureda.thunder.pobooks.utils;

import com.google.gson.Gson;
import com.oureda.thunder.pobooks.manager.CacheManager;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by thunder on 17-5-14.
 */

public class HttpUtils {
        public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private static HttpUtils httpUtils;
        private Gson gson = new Gson();
        private OkHttpClient okHttpClient = new OkHttpClient();

        private Response _get(String url) throws IOException {
            Request request= new Request.Builder().url(url).build();
            return this.okHttpClient.newCall(request).execute();
        }

        private void _getBook(String url, Callback paramCallback)
        {
            Request request = new Request.Builder().url(url).build();
            this.okHttpClient.newCall(request).enqueue(paramCallback);
        }

        private Response _post(String paramString, Param... paramVarArgs)
                throws IOException
        {
            CacheManager.getInstance().getCookie();
            RequestBody requestBody= RequestBody.create(JSON, paramsTOJson(paramVarArgs));
            Request request= new Request.Builder().url(paramString).post(requestBody).build();
            return new OkHttpClient().newCall(request).execute();
        }

        private void _postAsyn(String url, Callback paramCallback, Param... paramVarArgs)
        {
            String str = CacheManager.getInstance().getCookie();
           RequestBody requestBody = RequestBody.create(JSON, paramsTOJson(paramVarArgs));
            Request request = new Request.Builder().url(url).addHeader("cookie", str).post(requestBody).build();
            this.okHttpClient.newCall(request).enqueue(paramCallback);
        }

        public static void autoLoginPost(String paramString, Callback paramCallback)
        {
            getInstance()._postAsyn(paramString, paramCallback, new Param[0]);
        }

        public static void downloadBookAsyn(final String bookId, final int chapter, String url, Param... paramVarArgs)
        {
            getInstance()._postAsyn(bookId, new Callback()
            {
                public void onFailure(Call paramAnonymousCall, IOException paramAnonymousIOException)
                {
                    paramAnonymousIOException.printStackTrace();
                }

                public void onResponse(Call paramAnonymousCall, Response response)
                        throws IOException
                {
                    if (response != null)
                    {
                        String content = response.body().string();
                        FileUtil.writeFile(FileUtil.getChapterFile(bookId, chapter).getAbsolutePath(), content);
                    }
                }
            }, paramVarArgs);
        }

        public static Response get(String paramString)
                throws IOException
        {
            return getInstance()._get(paramString);
        }

        public static void getBook(String paramString, Callback paramCallback)
        {
            getInstance()._getBook(paramString, paramCallback);
        }

        public static HttpUtils getInstance()
        {
            if(httpUtils==null){
                synchronized (HttpUtils.class){
                    httpUtils=new HttpUtils();
                }

            }return httpUtils;
        }

        private String paramsTOJson(Param[] paramArrayOfParam)
        {
            HashMap localHashMap = new HashMap();
            int j = paramArrayOfParam.length;
            for (int i = 0; i < j; i++)
            {
                Param localParam = paramArrayOfParam[i];
                localHashMap.put(localParam.key, localParam.value);
            }
            String json = this.gson.toJson(localHashMap);
            LogUtil.d("paramsTOJson:", " " + json);
            return json;
        }

        public static Response post(String paramString, Param... paramVarArgs)
                throws IOException
        {
            return getInstance()._post(paramString, paramVarArgs);
        }

        public static void postAsyn(String paramString, Callback paramCallback, Param... paramVarArgs)
        {
            getInstance()._postAsyn(paramString, paramCallback, paramVarArgs);
        }

        public static class Param
        {
            String key;
            String value;

            public Param() {}

            public Param(String key, String value)
            {
                this.key = key;
                this.value = value;
            }
        }
}
