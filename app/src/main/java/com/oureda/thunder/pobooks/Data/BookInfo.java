package com.oureda.thunder.pobooks.Data;

import java.util.List;

/**
 * Created by thunder on 17-5-15.
 */

public class BookInfo {
        public List<Data> data;

        public static class Data
        {
            private String address;
            private int chapter;
            private String id;
            private long size;
            private String book_book_id;

            public String getBook_book_id() {
                return book_book_id;
            }

            public String getAddress()
            {
                return this.address;
            }

            public int getChapter()
            {
                return this.chapter;
            }

            public String getId()
            {
                return this.id;
            }

            public long getSize()
            {
                return this.size;
            }
        }

    }
