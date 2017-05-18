package com.oureda.thunder.pobooks.Data;

import com.oureda.thunder.pobooks.manager.SettingManager;
import com.oureda.thunder.pobooks.utils.SharedPreferenceUtil;

import org.litepal.crud.DataSupport;

/**
 * Created by thunder on 17-5-13.
 */

public class Books extends DataSupport {
        private String BookId;
        private int ImageId;
        private String author;
        private String bookName;
        private boolean isFromSd = false;
        private boolean isHasRead = false;
        private boolean isIsCheck = false;
        private boolean isShare = false;
        private boolean isTemp = false;
        private String link;
        private int progress;
        private int readTimes = 0;
        private String time = "尚未阅读";

        public Books() {}

        public Books(String bookId, String bookName)
        {
            this.BookId = bookId;
            saveBookName(bookName);
            this.bookName = bookName;
        }

        private String getBookString(String paramString)
        {
            return this.BookId + paramString;
        }

        public String getAuthor()
        {
            return this.author;
        }

        public String getBookId()
        {
            return this.BookId;
        }

        public String getBookName()
        {
            return this.bookName;
        }

        public int getImageId()
        {
            return this.ImageId;
        }

        public String getLink()
        {
            return this.link;
        }

    public boolean isTemp() {
        return isTemp;
    }

    public void setTemp(boolean temp) {
        isTemp = temp;
    }

    public int getProgress()
        {
            int[] arrayOfInt = SettingManager.getInstance().getReadProgress(this.BookId);
            this.progress = (arrayOfInt[0] - 1);
            return arrayOfInt[0];
        }

        public int getReadTimes()
        {
            return this.readTimes;
        }

        public String getTime()
        {
            return this.time;
        }

        public boolean isCheck()
        {
            return this.isIsCheck;
        }

        public boolean isFromSd()
        {
            return this.isFromSd;
        }

        public boolean isHasRead()
        {
            return this.isHasRead;
        }

        public boolean isShare()
        {
            return this.isShare;
        }

        public void saveBookName(String paramString)
        {
            SharedPreferenceUtil.getInstance().putString(getBookString(paramString), paramString);
        }

        public void setAuthor(String author)
        {
            this.author = author;
        }

        public void setBookId(String bookId)
        {
            this.BookId = bookId;
        }

        public void setBookName(String bookName)
        {
            this.bookName = bookName;
        }

        public void setFromSd(boolean isFromSd)
        {
            this.isFromSd = isFromSd;
        }

        public void setHasRead(boolean paramBoolean)
        {
            this.isHasRead = paramBoolean;
        }

        public void setImageId(int paramInt)
        {
            this.ImageId = paramInt;
        }

        public void setIsCheck(boolean paramBoolean)
        {
            this.isIsCheck = paramBoolean;
        }

        public void setLink(String paramString)
        {
            this.link = paramString;
        }

        public void setProgress(int paramInt)
        {
            this.progress = paramInt;
        }

        public void setReadTimes(int paramInt)
        {
            this.readTimes = paramInt;
        }

        public void setShare(boolean paramBoolean)
        {
            this.isShare = paramBoolean;
        }

        public void setTime(String paramString)
        {
            this.time = paramString;
        }
    }

