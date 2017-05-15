package com.oureda.thunder.pobooks.manager;

import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.SharedPreferenceUtil;

import java.io.File;

/**
 * Created by thunder on 17-5-14.
 */

public class CacheManager
{
    private static CacheManager manager;

    private static String _getCookie()
    {
        return SharedPreferenceUtil.getInstance().getString("cookie", "");
    }

    private void _saveCookie(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("cookie", paramString);
    }

    public static CacheManager getInstance()
    {
        if (manager == null)
        {
            manager = new CacheManager();
        }
        return manager;
    }

    public File getChapterFile(String bookId, int chapter)
    {
        File file = FileUtil.getChapterFile(bookId, chapter);
        if ((file != null) && (file.length() > 50L)) {
            return file;
        }else{
            return null;
        }
    }

    public String getCookie()
    {
        return _getCookie();
    }

    public String getUserAccount()
    {
        return SharedPreferenceUtil.getInstance().getString("userAccount", "admin");
    }

    public String getUserAddress()
    {
        return SharedPreferenceUtil.getInstance().getString("userAddress", "   sss");
    }

    public String getUserIntroduce()
    {
        return SharedPreferenceUtil.getInstance().getString("userIntroduce", "暂无");
    }

    public String getUserLove()
    {
        return SharedPreferenceUtil.getInstance().getString("userLove", "暂无");
    }

    public String getUserName()
    {
        return SharedPreferenceUtil.getInstance().getString("userName", "");
    }

    public String getUserSex()
    {
        return SharedPreferenceUtil.getInstance().getString("user_sex", "男");
    }

    public void saveCookie(String paramString)
    {
        _saveCookie(paramString);
    }

    public void setUserAccount(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("userAccount", paramString);
    }

    public void setUserAddress(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("userAddress", paramString);
    }

    public void setUserIntroduce(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("userIntroduce", paramString);
    }

    public void setUserLove(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("userLove", paramString);
    }

    public void setUserName(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("userName", paramString);
    }

    public void setUserSex(String paramString)
    {
        SharedPreferenceUtil.getInstance().putString("user_sex", paramString);
    }
}
