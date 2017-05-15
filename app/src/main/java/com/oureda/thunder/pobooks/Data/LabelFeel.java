package com.oureda.thunder.pobooks.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by thunder on 17-5-13.
 */

public class LabelFeel extends DataSupport {
    private String bookAuthor;
    private String bookId;
    private String bookName;
    private int chapter = 0;
    private long date;
    private String feelContentAll;
    private String feelTime = "暂无时间";
    private String labelContent;
    private String labelTime = "暂无时间";
    private String markFeel;
    private int page;

    public LabelFeel() {
    }

    public LabelFeel(String bookId)
    {
        this.bookId = bookId;
    }

    public LabelFeel(String bookId, int currentChapter)
    {
        this.bookId = bookId;
        this.chapter = currentChapter;
    }

    public LabelFeel(String bookId, String bookName, int chapter, int page)
    {
        this.bookId = bookId;
        this.bookName = bookName;
        this.chapter = chapter;
        this.page = page;
    }

    public String getBookAuthor()
    {
        return this.bookAuthor;
    }

    public String getBookId()
    {
        return this.bookId;
    }

    public String getBookName()
    {
        return this.bookName;
    }

    public int getChapter()
    {
        return this.chapter;
    }

    public long getDate()
    {
        return this.date;
    }

    public String getFeelContentAll()
    {
        return this.feelContentAll;
    }

    public String getFeelTime()
    {
        return this.feelTime;
    }

    public String getLabelContent()
    {
        return this.labelContent;
    }

    public String getLabelTime()
    {
        return this.labelTime;
    }

    public int getPage()
    {
        return this.page;
    }

    public void setBookAuthor(String paramString)
    {
        this.bookAuthor = paramString;
    }

    public void setBookId(String paramString)
    {
        this.bookId = paramString;
    }

    public void setDate(long paramLong)
    {
        this.date = paramLong;
    }

    public void setFeelContentAll(String paramString)
    {
        this.feelContentAll = paramString;
    }

    public void setFeelTime(String paramString)
    {
        this.feelTime = paramString;
    }

    public void setLabelContent(String paramString)
    {
        this.labelContent = paramString;
    }

    public void setLabelTime(String paramString)
    {
        this.labelTime = paramString;
    }
}
