package com.oureda.thunder.pobooks.support;

import com.oureda.thunder.pobooks.Data.ChapterInfo;

import java.util.List;

/**
 * Created by thunder on 17-5-15.
 */

public class DownloadSupport
{
    public static class DownloadQueue
    {
        public String bookId;
        public int end;
        public boolean isCancle;
        public boolean isFinish;
        public List<ChapterInfo> list;
        public int start;

        public DownloadQueue() {}

        public DownloadQueue(int start, List<ChapterInfo> chapterInfos, int end, String bookId)
        {
            this.start = start;
            this.list = chapterInfos;
            this.end = end;
            this.bookId = bookId;
        }
    }
}
