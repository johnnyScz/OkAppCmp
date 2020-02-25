package com.xshell.xshelllib.plugin;

import android.os.Environment;
import android.util.Log;

import com.xshell.xshelllib.utils.FileUtil;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zzy on 2016/9/28.
 * 清理
 */
public class ClearCachePlugin extends CordovaPlugin {


    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        if ("getCacheSize".equals(action)) { //得到缓存大小（专指国海的pdf文件）
            //遍历文件的大小
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + args.getString(0));
            double dirSize = FileUtil.getDirSize(file);
            callbackContext.success(dirSize + "");
            return true;
        } else if ("clearCache".equals(action)) {  //删除指定日期前的数据（专指国海的pdf文件）
            Date newDate = new Date();
            Date dateBefore = getDateBefore(newDate, args.getInt(0));
            List list = getDatesBetweenTwoDate(dateBefore, newDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {

                String stringDate = format.format(list.get(i));
                //切割月和日
                String[] split = stringDate.split("-");
                String year = split[0];
                String month = split[1];
                String day = split[2];
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + args.getString(1) + File.separator + year + File.separator + month + File.separator + day);
                Log.i("zzy", "file:" + file);
                deleteFile(file);
            }
            callbackContext.success();
            return true;
        }

        return false;
    }


    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {  //如果是一个文件
                file.delete();
            } else {  //是一个目录
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
                file.delete();
            }
        }
    }

    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(beginDate);
        //把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);
        //把结束时间加入集合
        return lDate;
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

}
