package com.oureda.thunder.pobooks.Data;

import org.litepal.crud.DataSupport;

/**
 * Created by thunder on 17-4-29.
 */

public class ChapterInfo extends DataSupport {
    private String bookId;
    private int chapter;
    private String address;
    private long size;
    private int number;
    private String chapterName;
    private boolean isFromSD;

    public ChapterInfo(String bookId, int chapter, String address, long size, String chapterName, boolean isFromSD) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.address = address;
        this.size = size;
        this.chapterName = chapterName;
        this.isFromSD = isFromSD;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public boolean isFromSD() {
        return isFromSD;
    }

    public void setFromSD(boolean fromSD) {
        isFromSD = fromSD;
    }
}
