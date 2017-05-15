package com.oureda.thunder.pobooks.manager;

import android.graphics.Color;

import com.oureda.thunder.pobooks.utils.SharedPreferenceUtil;

/**
 * Created by thunder on 17-5-2.
 */

public class SettingManager  {
    private static volatile SettingManager manager;
    private int backgroundBitmap;
    private boolean isCreatBoot;
    private boolean isNight;

    private String getChapterKey(String paramString)
    {
        return paramString + "-chapter";
    }

    private String getEndPosKey(String paramString)
    {
        return paramString + "-endPos";
    }

    public static SettingManager getInstance()
    {
        if (manager == null) {
            manager = new SettingManager();
        }
        return manager;
    }

    private String getStartPosKey(String paramString)
    {
        return paramString + "-startPos";
    }

    public int getFontSize()
    {
        return SharedPreferenceUtil.getInstance().getInt("textSize", 30);
    }

    public int getFontSizeProgress()
    {
        return SharedPreferenceUtil.getInstance().getInt("sizeProgress", 1);
    }

    public int getPaddingSize()
    {
        return SharedPreferenceUtil.getInstance().getInt("paddingSize", 6);
    }

    public int getPaddingSizeProgress()
    {
        return SharedPreferenceUtil.getInstance().getInt("paddingProgress", 1);
    }

    public int getReadColor()
    {
        return SharedPreferenceUtil.getInstance().getInt("readColor", Color.WHITE);
    }

    public int[] getReadProgress(String bookId)
    {
        return new int[] { SharedPreferenceUtil.getInstance().getInt(getChapterKey(bookId), 1), SharedPreferenceUtil.getInstance().getInt(getStartPosKey(bookId), 0), SharedPreferenceUtil.getInstance().getInt(getEndPosKey(bookId), 0) };
    }

    public int getTextColor()
    {
        return SharedPreferenceUtil.getInstance().getInt("textColor", Color.BLACK);
    }

    public void saveFontSize(int fontSize)
    {
        SharedPreferenceUtil.getInstance().putInt("textSize", fontSize);
    }

    public void saveFontSizeProgress(int fontSize)
    {
        SharedPreferenceUtil.getInstance().putInt("sizeProgress",fontSize);
    }

    public void savePaddingSize(int paddingSize)
    {
        SharedPreferenceUtil.getInstance().putInt("paddingSize", paddingSize);
    }

    public void savePaddingSizeProgress(int paddingSizeProgress)
    {
        SharedPreferenceUtil.getInstance().putInt("paddingProgress", paddingSizeProgress);
    }

    public void saveReadColor(int readColor)
    {
        SharedPreferenceUtil.getInstance().putInt("readColor", readColor);
    }

    public void saveReadProgress(String bookId, int chapter, int end, int start)
    {
        SharedPreferenceUtil.getInstance().putInt(getChapterKey(bookId), chapter).putInt(getEndPosKey(bookId), end).putInt(getStartPosKey(bookId), start);
    }

    public void saveTextColor(int textColor)
    {
        SharedPreferenceUtil.getInstance().putInt("textColor", textColor);
    }

    public void setIsNight() {}
}
