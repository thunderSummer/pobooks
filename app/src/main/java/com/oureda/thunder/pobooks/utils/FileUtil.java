package com.oureda.thunder.pobooks.utils;

import android.content.Context;
import android.os.Environment;

import com.oureda.thunder.pobooks.manager.StringManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by thunder on 17-4-29.
 */

public class FileUtil {
    public static String getChapterPath(String bookId, int chapter) {
        return StringManager.PATH_TXT + bookId + File.separator + chapter + ".txt";
    }

    public static File getChapterFile(String bookId, int chapter) {
        File file = new File(getChapterPath(bookId, chapter));
        if (!file.exists())
            createFile(file);
        return file;
    }

    public static File getBookDir(String bookId) {
        return new File(StringManager.PATH_TXT + bookId);
    }
    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }
    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }
    private static boolean isSdCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 将内容写入文件
     *
     * @param filePath eg:/mnt/sdcard/demo.txt
     * @param content  内容
     * @param isAppend 是否追加
     */
    public static void writeFile(String filePath, String content, boolean isAppend) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath, isAppend);
            byte[] bytes = content.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeFile(File file,String content){
        try {
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            try {
                outputStreamWriter.write(content);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void writeFile(String filePathAndName, String fileContent) {
        try {
            OutputStream outstream = new FileOutputStream(filePathAndName);
            OutputStreamWriter out = new OutputStreamWriter(outstream);
            out.write(fileContent);
            out.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCharset(String bookId){
        return "UTF-8";
    }
}
