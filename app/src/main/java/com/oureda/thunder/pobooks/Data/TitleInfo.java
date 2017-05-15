package com.oureda.thunder.pobooks.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by thunder on 17-5-9.
 */

public class TitleInfo extends DataSupport {
    private String path;
    private int number;
    private String title;
    private long startIndex;
    private long endIndex;
    private long contentLength;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public TitleInfo(String path, String title, long startIndex ,int number) {
        this.path = path;
        this.title = title;
        this.startIndex = startIndex;
        this.number=number;
    }

    public TitleInfo() {
    }

    @Override
    public String toString() {
        return "第"+number+"章"+title+"   "+startIndex+"   "+contentLength;
    }
}
