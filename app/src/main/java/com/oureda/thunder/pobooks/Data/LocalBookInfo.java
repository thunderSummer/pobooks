package com.oureda.thunder.pobooks.Data;

import android.util.Log;

import com.oureda.thunder.pobooks.utils.FileUtil;
import com.oureda.thunder.pobooks.utils.LogUtil;
import com.oureda.thunder.pobooks.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.litepal.LitePalBase.TAG;

/**
 * Created by thunder on 17-5-9.
 */

public class LocalBookInfo {
    public static class LocalBookPhrase {
        private String charset;
        private int count = 0;
        private String name;
        private String pathIsId;
        private List<TitleInfo> titleInfos;

        public LocalBookPhrase(String bookId, String bookName) {
            this.charset = "GBK";
            this.pathIsId = bookId;
            this.name = bookName;
        }

        public boolean startPhrase() {
            FileInputStream fileInputStream = null;
            BufferedReader bufferedReader = null;
            titleInfos = new ArrayList<>();
            if (DataSupport.select("path").where("path = ?", pathIsId).find(TitleInfo.class).size() != 0) {
                return false;
            }
            try {
                fileInputStream = new FileInputStream(pathIsId);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, charset));
                String line;
                TitleInfo titleInfo = new TitleInfo();
                titleInfo.setTitle(name);
                titleInfo.setStartIndex(0);
                titleInfo.setContentLength(0);
                titleInfo.setNumber(0);
                titleInfos.add(titleInfo);
                titleInfo.save();

                long parseLength = 0;
                int i = 0;
                StringBuilder builder = new StringBuilder();
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        String line1;
                        line1 = line.trim();
                        if (line1.equals("")) {
                            parseLength += 2;
                            continue;
                        }
                        if (line.trim().length() > 0) {
                            if (TitleMatch.isChapter(line.trim())) {
                                if (builder.toString().length() != 0) {
                                    parseLength += builder.toString().getBytes(charset).length;
                                    titleInfos.get(count).setContentLength(parseLength);
                                    TitleInfo titleInfo1 = new TitleInfo();
                                    titleInfo1.setContentLength(parseLength);
                                    titleInfo1.updateAll("path = ? and number = ?", pathIsId, String.valueOf(count));
                                    System.out.println(titleInfos.get(count));
                                    builder.setLength(0);
                                }
                                if (builder.length() == 0) {
                                    count++;
                                    TitleInfo titleInfo1 = new TitleInfo(pathIsId, line, parseLength, count);
                                    titleInfo1.save();
                                    titleInfos.add(titleInfo1);
                                    LogUtil.d(TAG,titleInfo1.toString());
                                    parseLength += line.getBytes(charset).length;
                                }
                            }
                        }
                        if (!TitleMatch.isChapter(line.trim())) {
                            builder.append(line);
                            parseLength += 2;
                        }

                    }
                    parseLength += builder.toString().getBytes(charset).length;
                    TitleInfo titleInfo1 = new TitleInfo();
                    titleInfo1.setContentLength(parseLength);
                    titleInfos.get(count).setContentLength(parseLength);
                    titleInfo1.updateAll("path = ? and number = ?", pathIsId, String.valueOf(count));
                    builder.delete(0, builder.length() - 1);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return false;
        }
        private void showChapter(){
            for(TitleInfo titleInfo:titleInfos){
                Log.d(TAG, "showChapter: "+titleInfo.toString());
            }
        }


        public static class TitleMatch {
            public static final String[] key = {"部", "卷", "章", "节", "集", "回", "幕", "计"};
            public static final Pattern p = Pattern.compile("^[0-9零一二三四五六七八九十百千]+$");

            public static boolean isChapter(String line) {
                if (!line.startsWith("第")) {
                    return false;
                }
                int index = -1;
                for (int i = 0; i < key.length; i++) {
                    index = line.indexOf(key[i]);
                    if (index != -1) {
                        break;
                    }
                }
                if (index == -1) {
                    return false;
                }
                String zhang = line.substring(1, index);
                return p.matcher(zhang).matches();
            }

            public static final String[] extra_key = {"序"};
            public static final String[] extra_key_start = {"前言", "后记", "楔子", "附录", "外传"};

            public static boolean isExtra(String line) {
                if (line.length() > 3) {
                    return false;
                }
                int index = line.indexOf(extra_key[0]);
                if (index != -1) {
                    return (index == 0 || index == 1) && line.length() <= 2;
                } else {
                    for (int i = 0; i < extra_key_start.length; i++) {
                        if (line.startsWith(extra_key_start[i])) {
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
    }
}
