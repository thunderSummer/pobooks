package com.oureda.thunder.pobooks.Data;

import java.util.List;

/**
 * Created by thunder on 17-5-15.
 */


public class SearchResult
{
    public List<Data> data;

    public static class Data
    {
        private String book_id;
        private String book_name;
        private int chapter_num;
        private String intro;
        private int price;
        private long size;
        private String title_picture;
        private String type;
        private String writer;

        public String getBook_id()
        {
            return this.book_id;
        }

        public String getBook_name()
        {
            return this.book_name;
        }

        public int getChapter_num()
        {
            return this.chapter_num;
        }

        public String getIntro()
        {
            return this.intro;
        }

        public int getPrice()
        {
            return this.price;
        }

        public long getSize()
        {
            return this.size;
        }

        public String getTitle_picture()
        {
            return this.title_picture;
        }

        public String getType()
        {
            return this.type;
        }

        public String getWriter()
        {
            return this.writer;
        }
    }

    public class Status
    {
        int code;
        String msg;

        public Status() {}

        public int getCode()
        {
            return this.code;
        }

        public String getMessage()
        {
            return this.msg;
        }
    }

    public class status
    {
        public status() {}
    }
}

