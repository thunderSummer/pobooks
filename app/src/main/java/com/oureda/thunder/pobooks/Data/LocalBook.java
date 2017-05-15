package com.oureda.thunder.pobooks.Data;

import com.oureda.thunder.pobooks.R;

import java.util.Date;

/**
 * Created by thunder on 17-5-14.
 */

public class LocalBook {
        private String HumanSize;
        private String Title;
        private String chaset;
        private Date date;
        private int imageId;
        private boolean isCheck = false;
        private String path;
        private Long size;

        public String getChaset()
        {
            return this.chaset;
        }

        public Date getDate()
        {
            return this.date;
        }

        public String getHumanSize()
        {
            return this.HumanSize;
        }

        public int getImageId()
        {
            return this.imageId;
        }

        public String getPath()
        {
            return this.path;
        }

        public Long getSize()
        {
            return this.size;
        }

        public String getTitle()
        {
            return this.Title;
        }

        public boolean isCheck()
        {
            return this.isCheck;
        }

        public void setChaset(String paramString)
        {
            this.chaset = paramString;
        }

        public void setCheck(boolean paramBoolean)
        {
            this.isCheck = paramBoolean;
        }

        public void setDate(Date paramDate)
        {
            this.date = paramDate;
        }

        public void setImageId(int paramInt)
        {
            switch (paramInt){
                case 1:
                    imageId= R.drawable.txt_icon;
                    break;
                case 2:
                    imageId=R.drawable.ekb2_icon;
                    break;
                case 3:
                    imageId=R.drawable.umd_icon;
                    break;
            }
        }

        public void setPath(String paramString)
        {
            this.path = paramString;
        }

        public void setSize(Long paramLong)
        {
            this.size = paramLong;
            int i = 0;
            float f = (float)paramLong * 1.0F;
            while (paramLong / 1024L > 1L)
            {
                paramLong = paramLong/1024;
                f /= 1024.0F;
                i++;
            }
            f = Math.round(10.0F * f) / 100.0F;
            switch (i)
            {
                case 0:
                    this.HumanSize = (f + "  B");
                    break;
                case 1:
                    this.HumanSize = (f + " KB");
                   break;
                case 2:
                    this.HumanSize = (f + " MB");
                    break;
                case 3:
                    this.HumanSize = (f + " GB");
                    break;

            }
        }

        public void setTitle(String paramString)
        {
            this.Title = paramString;
        }

}
