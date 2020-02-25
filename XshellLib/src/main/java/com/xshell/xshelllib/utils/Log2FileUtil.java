package com.xshell.xshelllib.utils;

import android.os.Environment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log2FileUtil {
    private static Log2FileUtil instance;
    // 用于格式化日期,作为日志文件名的一部分
    private static DateFormat formatter;
    private static File log;

    private Log2FileUtil() {
    }

    public static Log2FileUtil getInstance() {
        if (instance == null) {
            instance = new Log2FileUtil();
        }
        try {
            log = FileUtil.getInstance().createFileInSDCard("QFT.txt");
            formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    public String saveCrashInfo2File(String str) {
        try {
            String time = formatter.format(new Date());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (!log.exists()) {
                    log.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(log, true);
                fos.write(("\n" + time + "*****" + str.toString()).getBytes());
                fos.close();
            }
        } catch (Exception e) {

        }
        return null;
    }
}
