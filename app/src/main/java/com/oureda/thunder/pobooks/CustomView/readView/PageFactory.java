package com.oureda.thunder.pobooks.CustomView.readView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.oureda.thunder.pobooks.Data.ChapterInfo;
import com.oureda.thunder.pobooks.Data.TitleInfo;
import com.oureda.thunder.pobooks.base.MyApplication;
import com.oureda.thunder.pobooks.listener.OnBookStatusChangeListen;
import com.oureda.thunder.pobooks.manager.SettingManager;
import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ScreenUtils;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import java.util.zip.CheckedOutputStream;

/**
 * Created by thunder on 17-5-2.
 */

public class PageFactory {
    private static String TAG="PageFactory----->";
    private Context context;
    private OnBookStatusChangeListen listener;
    /**
     * 绘图相关
     */
    private Bitmap batteryBitmap;
    private int battery = 0;
    private int color = Color.BLACK;
    private Paint contentPaint;
    private Bitmap pageBitmap;
    private int mFontSize = 50;
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private int mHeight;
    private int mLineSpace;
    private int mNumFontSize = 18;
    private int mPageLineCount;
    private int mVisibleHeight;
    private int mVisibleWidth;
    private int mWidth;
    private int marginHeight;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    private int marginWidth;
    private int percentLen;
    private Rect rect;


    private String time;
    private int timeLen;
    private Paint titlePaint;
    /**
     * 数据相关
     */
    private String charset = "GBK";
    private static boolean isFromSd = false;
    private String bookId;
    private List<ChapterInfo> chapterInfoList;
    private int chapterSize = 0;
    private int currentBeginPos;
    private int currentChapter;
    private int currentEndPos;
    private int currentPage = 1;
    private int tempChapter;
    private int tempEndPos;
    private int tempBeginPos;
    //用于存放每一章的内容
    public Vector<String> contentLines = new Vector();
    //本地书籍的位置指针
    private long localStartPos;
    //上一页文件指针的位置，用于用户往回翻页
    private int lastBeginPos = -1;
    private int lastEndPos = -1;

    /**
     * 用于映射一章的内容，的新io的块
     */
    private MappedByteBuffer mbBuff;
    private int mbBufferLen;

    public PageFactory(String bookId, int mWidth, int mHeight, OnBookStatusChangeListen listener,List<ChapterInfo> chapterInfoList)
    {
        this.bookId = bookId;
        this.chapterInfoList = chapterInfoList;
        this.mHeight = mHeight;
        this.mWidth = mWidth;

        this.listener = listener;
        this.mFontSize = SettingManager.getInstance().getFontSize();
        this.mLineSpace = SettingManager.getInstance().getPaddingSize();
        this.context = MyApplication.getContext();
        this.mNumFontSize = ScreenUtils.dpToPxInt(10f);
        this.marginWidth = ScreenUtils.dpToPxInt(15.0F);
        this.marginHeight = ScreenUtils.dpToPxInt(25.0F);
        this.mVisibleHeight = (int) ((this.mHeight - this.marginHeight * 2 - this.mNumFontSize * 2 - this.mLineSpace * 2));
        this.mVisibleWidth = (this.mWidth - this.marginWidth * 2);
        this.mPageLineCount = (this.mVisibleHeight / (this.mFontSize + this.mLineSpace));
        this.rect = new Rect(0, 0, this.mWidth, this.mHeight);
        this.contentPaint = new Paint(1);
        this.contentPaint.setTextSize(this.mFontSize);
        this.contentPaint.setColor(color);
        this.titlePaint = new Paint(1);
        this.titlePaint.setTextSize(this.mNumFontSize);
        this.timeLen = ((int)this.titlePaint.measureText("00:00"));
        this.color = SettingManager.getInstance().getReadColor();
    }

    private File getFile(int chapter)
    {
        File localFile = FileUtil.getChapterFile(this.bookId, chapter);
     //   this.charset = FileUtil.getCharset(localFile.getAbsolutePath());
        LogUtil.d(TAG, "Charset=" + this.charset);
        return localFile;
    }
    public File getBookFile(int chapter) {
        File file = FileUtil.getChapterFile(bookId, chapter);
        if (file != null && file.length() > 10) {
            // 解决空文件造成编码错误的问题
            //charset = FileUtil.getCharset(file.getAbsolutePath());
        }
        charset="GBK";
        LogUtil.d(TAG,"charset = "+charset);
        return file;
    }
    public void openBook() {
        openBook(new int[]{0, 0});
    }

    public void openBook(int[] position) {
        openBook(1, position);
    }

    /**
     * 打开书籍文件
     *
     * @param chapter  阅读章节
     * @param position 阅读位置
     * @return false：文件不存在或打开失败  true：打开成功
     */
    public boolean openBook(int chapter, int[] position) {
        this.currentChapter = chapter;
        this.chapterSize = chapterInfoList.size();
        if (currentChapter > chapterSize)
            currentChapter = chapterSize;
        String path;
        if(isFromSd) {
            path= getBookFile(currentChapter).getPath();
            LogUtil.d(TAG,"file path =="+path);
            try {
                File file = new File(path);
                long length = file.length();
                if (length > 10) {
                    mbBufferLen = (int) length;
                    Log.d(TAG, "openBook: "+length);
                    // 创建文件通道，映射为MappedByteBuffer
                    mbBuff = new RandomAccessFile(file, "r")
                            .getChannel()
                            .map(FileChannel.MapMode.READ_ONLY, 0, length);
                    currentBeginPos = position[0];
                    currentEndPos = position[1];
                    onChapterChanged(chapter);
                    contentLines.clear();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }else{

            TitleInfo titleInfo = DataSupport.where("number = ? and path = ?",String.valueOf(currentChapter),bookId).find(TitleInfo.class).get(0);

            mbBufferLen= (int) (titleInfo.getContentLength()-titleInfo.getStartIndex());
            LogUtil.d(TAG,titleInfo.toString()+bookId+mbBufferLen);
            try {
                File file = new File(bookId);
                mbBuff=new RandomAccessFile(file,"r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY,titleInfo.getStartIndex(),mbBufferLen);
                Log.d(TAG, "openBook: "+mbBuff.toString());
                currentBeginPos = position[0];
                currentEndPos = position[1];
                onChapterChanged(chapter);
                contentLines.clear();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    public synchronized void onDraw(Canvas canvas) {
        if (contentLines.size() == 0) {
            currentEndPos = currentBeginPos;
            contentLines = pageNext();
        }
        LogUtil.d(TAG,contentLines.toString());
        if (contentLines.size() > 0) {
            int y = marginHeight + (mLineSpace << 1);
            // 绘制背景
            if (pageBitmap != null) {
//                canvas.drawBitmap(pageBitmap, null, rectF, null);
            } else {
                canvas.drawColor(Color.WHITE);
            }
            // 绘制标题
            canvas.drawText("第"+changeDigital(currentChapter)+"章"+" "+chapterInfoList.get(currentChapter - 1).getChapterName(),marginWidth,y/2,titlePaint);
            y += mLineSpace + mNumFontSize;
            // 绘制阅读页面文字
            for (String line : contentLines) {
                y += mLineSpace;
                LogUtil.d("content == ",line+" ");
                if (line.endsWith("@")) {

                    canvas.drawText(line.substring(0, line.length() - 1), marginWidth, y, contentPaint);
                    y += mLineSpace;
                } else {
                    canvas.drawText(line, marginWidth, y, contentPaint);
                }
                y += mFontSize;
            }
            // 绘制提示内容
            if (batteryBitmap != null) {
                canvas.drawBitmap(batteryBitmap, marginWidth + 2,
                        mHeight - marginHeight - ScreenUtils.dpToPxInt(12), titlePaint);
            }
            float percent = currentEndPos*100.0f/mbBufferLen;
            String string1 = this.currentChapter + "/" + this.chapterSize + "  |  ";
            percentLen= (int) titlePaint.measureText(string1+"00.00%");
            canvas.drawText(string1+decimalFormat.format(percent)+"%",(mWidth-percentLen)/2,mHeight-mFontSize,titlePaint);

            //     String mTime = dateFormat.format(new Date());
       //     canvas.drawText(mTime, mWidth - marginWidth - timeLen, mHeight - marginHeight, mTitlePaint);

            // 保存阅读进度
            SettingManager.getInstance().saveReadProgress(bookId, currentChapter, currentEndPos, currentBeginPos);
        }
    }
    /**
     * 指针移到上一页页首
     */
    private void pageUp() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>(); // 页面行
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (currentBeginPos > 0)) {
            Vector<String> paraLines = new Vector<>(); // 段落行
            byte[] parabuffer = readParagraphBack(currentBeginPos); // 1.读取上一个段落

            currentBeginPos -= parabuffer.length; // 2.变换起始位置指针
            try {
                strParagraph = new String(parabuffer, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ");
            strParagraph = strParagraph.replaceAll("\n", " ");

            while (strParagraph.length() > 0) { // 3.逐行添加到lines
                int paintSize =contentPaint.breakText(strParagraph, true, mVisibleWidth, null);
                paraLines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
            }
            lines.addAll(0, paraLines);

            while (lines.size() > mPageLineCount) { // 4.如果段落添加完，但是超出一页，则超出部分需删减
                try {
                    currentBeginPos += lines.get(0).getBytes(charset).length; // 5.删减行数同时起始位置指针也要跟着偏移
                    lines.remove(0);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            currentEndPos = currentBeginPos; // 6.最后结束指针指向下一段的开始处
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace); // 添加段落间距，实时更新容纳行数
        }
    }

    /**
     * 获取最后一页的内容。比较繁琐，待优化
     *
     * @return
     */
    public Vector<String> pageLast() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        currentPage = 0;
        while (currentEndPos < mbBufferLen) {
            int paraSpace = 0;
            mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
            currentBeginPos = currentEndPos;
            while ((lines.size() < mPageLineCount) && (currentEndPos < mbBufferLen)) {
                byte[] parabuffer = readParagraphGo(currentEndPos);
                currentEndPos += parabuffer.length;
                try {
                    strParagraph = new String(parabuffer, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                strParagraph = strParagraph.replaceAll("\r\n", "  ");
                strParagraph = strParagraph.replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行

                while (strParagraph.length() > 0) {
                    int paintSize = contentPaint.breakText(strParagraph, true, mVisibleWidth, null);
                    lines.add(strParagraph.substring(0, paintSize));
                    strParagraph = strParagraph.substring(paintSize);
                    if (lines.size() >= mPageLineCount) {
                        break;
                    }
                }
                lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");

                if (strParagraph.length() != 0) {
                    try {
                        currentEndPos -= (strParagraph).getBytes(charset).length;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                paraSpace += mLineSpace;
                mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
            }
            if (currentEndPos < mbBufferLen) {
                lines.clear();
            }
            currentPage++;
        }
        //SettingManager.getInstance().saveReadProgress(bookId, currentChapter, currentBeginPos, currentEndPos);
        return lines;
    }
    /**
     * 读取上一段落
     *
     * @param currentBeginPos 当前页起始位置指针
     * @return
     */
    private byte[] readParagraphBack(int currentBeginPos) {
        byte b0;
        int i = currentBeginPos - 1;
        while (i > 0) {
            b0 = mbBuff.get(i);
            if (b0 == 0x0a && i != currentBeginPos - 1) {
                i++;
                break;
            }
            i--;
        }
        int nParaSize = currentBeginPos - i;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = mbBuff.get(i + j);
        }
        return buf;
    }

    private Vector<String> pageNext() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        int paraSpace = 0;
        mPageLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while ((lines.size() < mPageLineCount) && (currentEndPos < mbBufferLen)) {
            byte[] parabuffer = readParagraphGo(currentEndPos);
            currentEndPos += parabuffer.length;
            try {
                strParagraph = new String(parabuffer, charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ")
                    .replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
            Log.d(TAG, "pageNext: "+strParagraph.toString());
            Log.d(TAG, "pageNext: "+currentEndPos+" "+mbBufferLen);
            while (strParagraph.length() > 0) {
                int paintSize = contentPaint.breakText(strParagraph, true, mVisibleWidth, null);
                lines.add(strParagraph.substring(0, paintSize));
                strParagraph = strParagraph.substring(paintSize);
                if (lines.size() >= mPageLineCount) {
                    break;
                }
            }

            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");
            if (strParagraph.length() != 0) {
                try {
                    currentEndPos -= (strParagraph).getBytes(charset).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            paraSpace += mLineSpace;
            mPageLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
        }
        return lines;
    }
    private byte[] readParagraphGo(int paramInt)
    {
        byte b0;
        int i = currentEndPos;
        while (i < mbBufferLen) {
            b0 = mbBuff.get(i++);
            if (b0 == 0x0a) {
                break;
            }
        }
        int nParaSize = i - currentEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mbBuff.get(currentEndPos + i);
        }
        return buf;
    }
    /**
     * 跳转下一页
     */
    public BookStatus nextPage() {
        if (!hasNextPage()) { // 最后一章的结束页
            return BookStatus.NO_NEXT_PAGE;
        } else {
            tempChapter = currentChapter;
            tempBeginPos = currentBeginPos;
            tempEndPos = currentEndPos;
            if (currentEndPos >= mbBufferLen) { // 中间章节结束页
                currentChapter++;
                boolean ret = openBook(currentChapter, new int[]{0, 0}); // 打开下一章
                if (!ret) {
                    onLoadChapterFailure(currentChapter);
                    currentChapter--;
                    currentBeginPos = tempBeginPos;
                    currentEndPos = tempEndPos;
                    return BookStatus.NEXT_CHAPTER_LOAD_FAILURE;
                } else {
                    currentPage = 0;
                    onChapterChanged(currentChapter);
                }
            } else {
                currentBeginPos = currentEndPos; // 起始指针移到结束位置
            }
            contentLines.clear();
            contentLines = pageNext(); // 读取一页内容
            onPageChanged(currentChapter, ++currentPage);
        }
        return BookStatus.LOAD_SUCCESS;
    }

    /**
     * 跳转上一页
     */
    public BookStatus prePage() {
        if (!hasPrePage()) { // 第一章第一页
            return BookStatus.NO_PRE_PAGE;
        } else {
            // 保存当前页的值
            tempChapter = currentChapter;
            tempBeginPos = currentBeginPos;
            tempEndPos = currentEndPos;
            if (currentBeginPos <= 0) {
                currentChapter--;
                boolean ret = openBook(currentChapter, new int[]{0, 0});
                if (!ret) {
                    onLoadChapterFailure(currentChapter);
                    currentChapter++;
                    return BookStatus.PRE_CHAPTER_LOAD_FAILURE;
                } else { // 跳转到上一章的最后一页
                    contentLines.clear();
                   contentLines = pageLast();
                    onChapterChanged(currentChapter);
                    onPageChanged(currentChapter, currentPage);
                    return BookStatus.LOAD_SUCCESS;
                }
            }
            contentLines.clear();
            pageUp(); // 起始指针移到上一页开始处
            contentLines = pageNext(); // 读取一页内容
            onPageChanged(currentChapter, --currentPage);
        }
        return BookStatus.LOAD_SUCCESS;
    }

    public void cancelPage() {
        currentChapter = tempChapter;
        currentBeginPos = tempBeginPos;
        currentEndPos = currentBeginPos;

       boolean ret = openBook(currentChapter, new int[]{currentBeginPos, currentEndPos});
        if (!ret) {
            onLoadChapterFailure(currentChapter);
            return;
        }
        contentLines.clear();
        contentLines = pageNext();
    }

    private boolean hasNextPage()
    {
        return currentChapter < chapterInfoList.size() || currentEndPos < mbBufferLen;
    }

    private boolean hasPrePage()
    {
        return currentChapter > 1 || (currentChapter == 1 && currentBeginPos > 0);
    }

    public static boolean isFromSd()
    {
        return isFromSd;
    }

    private void onChapterChanged(int chapter)
    {
        if (this.listener != null) {
            this.listener.onChapterChanged(chapter);
        }
    }

    private void onLoadChapterFailure(int chapter)
    {
        if (this.listener != null) {
            this.listener.onLoadChapterFailure(chapter);
        }
    }

    private void onPageChanged(int chapter, int page)
    {
        if (this.listener != null) {
            this.listener.onPageChanged(chapter, page);
        }
    }
    private String changeDigital(int dateNum)
    {
        String str = "";
        while (dateNum != 0)
        {
            str = str + "十一二三四五六七八九".substring(dateNum % 10, dateNum % 10 + 1);
            dateNum /= 10;
        }
        return new StringBuilder(str).reverse().toString();
    }

    private void convertBatteryBitmap() {}

    public void setBookStatusChangeListener(OnBookStatusChangeListen listener) {
        this.listener = listener;
    }
    public void setMLineSpace(int lineSpace)
    {
        this.mLineSpace = lineSpace;
    }
    public void setTextColor(int contentColor, int titleColor)
    {
        this.contentPaint.setColor(contentColor);
        this.titlePaint.setColor(titleColor);
    }

    public void setTime(String time)
    {
        this.time = time;
    }
    public void setColor(int color)
    {
        this.color = color;
        SettingManager.getInstance().saveReadColor(color);
    }

    public void setFontSize(int fontSize)
    {
        LogUtil.d(TAG, "fontSize=" + fontSize);
        this.mFontSize = fontSize;
        this.contentPaint.setTextSize(this.mFontSize);
        this.mPageLineCount = (this.mVisibleHeight / (this.mFontSize + this.mLineSpace));
        this.currentEndPos = this.currentBeginPos;
        nextPage();
    }
    public void setBattery(int paramInt)
    {
        this.battery = paramInt;
        convertBatteryBitmap();
    }
    public int[] getPosition()
    {
        return new int[] { this.currentChapter, this.currentBeginPos, this.currentEndPos };
    }
    public long getLocalStartPos()
    {
        return this.localStartPos;
    }
    public void recycle()
    {
        if ((this.pageBitmap != null) && (!this.pageBitmap.isRecycled()))
        {
            this.pageBitmap.recycle();
            this.pageBitmap = null;
            LogUtil.d(TAG, "pageBitmap recycle");
        }
        if ((this.batteryBitmap != null) && (!this.batteryBitmap.isRecycled()))
        {
            this.batteryBitmap.recycle();
            this.batteryBitmap = null;
            LogUtil.d(TAG, "batteryBitmap recycle");
        }
    }
    public void setIsFromSd(boolean isFromSd){
        this.isFromSd=isFromSd;
    }
}
