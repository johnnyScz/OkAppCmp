package com.xshell.xshelllib.utils;

import android.util.Log;

import org.apache.cordova.CordovaInterface;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtil {

	/**
	 * 返回格式好的时间 如：201408031430 用于服务器文件更新是上次时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getUpdateTime(long time) {
		return new SimpleDateFormat("yyyyMMddHHmm").format(new Date(time));
	}

	/**
	 * 
	 * @param timeStr
	 *            时间字符串 "201408171320"
	 * @return long 毫秒级
	 */
	public static long getUpdataTimeLong(String timeStr) {
		Calendar c = Calendar.getInstance();
		long time = 0l;
		try {
			c.setTime(new SimpleDateFormat("yyyyMMddHHmm").parse(timeStr));
			time = c.getTimeInMillis();
		} catch (ParseException e) {
		}
		return time;
	}
	
	
	/**获取当前时间字符串
	 * @return
	 */
	public static String now(){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS");
		return dateFormat.format(now);
	}

	public static void getImagePathFromSD(CordovaInterface cordova, String oldDate, String finalDeleteAll) {
		// 图片列表
		List<String> imagePathList = new ArrayList<String>();
		//image文件夹的路径   File.separator(/)
		String filePath = cordova.getActivity().getFilesDir() + "/cacheimage/" + "other";
		// 得到该路径文件夹下所有的文件
		File fileAll = new File(filePath);
		File[] files = fileAll.listFiles();
		Log.e("huang", "图片的个数：" + files.length);
		// 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Log.e("huang", "图片的路径：" + file.getPath());
			if("all".equals(finalDeleteAll)) {
				//删除文件
				deleteFile(file);
			}else if("part".equals(finalDeleteAll)) {
				//   if (checkIsImageFile(file.getPath())) {
				Date dateFile1 = new Date(file.lastModified());//文件最后修改时间
				DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
				String str_time = format.format(dateFile1);
				Log.e("huang","dateFile："+dateFile1);
				Log.e("huang","str_time："+str_time);
				String[] split2 = str_time.split(" ");
				String s1 = split2[0];
				String year = s1.substring(0, 4);
				String[] str1 = s1.split("年");
				String str2 = str1[1];
				String month = str2.split("月")[0];
				String day = str2.split("月")[1].split("日")[0];

				Log.e("huang","year："+year+"==month:"+month+"=="+day);
				// imagePathList.add(file.getPath());
				Calendar a = Calendar.getInstance();
				a.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
				//参数为年 月 日 时 分 秒
				a.set(Calendar.MILLISECOND, 0);
				//设置毫秒
				String[] split1 = oldDate.split("-");
				Calendar c = Calendar.getInstance();
				c.set(Integer.valueOf(split1[0]), Integer.valueOf(split1[1]), Integer.valueOf(split1[2]));
				c.set(Calendar.MILLISECOND, 0);
				//  System.out.println(a.compareTo(c));
				//a比c早,返回-1,
				//a与c相同,返回0
				//a比c晚,返回1
				int retrue = a.compareTo(c);
				if("-1".equals(retrue+"")) {
					Log.e("huang","时间的比较：-1");
				}else if("0".equals(retrue+"")){
					Log.e("huang","时间的比较：0");
					//删除文件
					//deleteFile(file);
				}else if("1".equals(retrue+"")){
					Log.e("huang","时间的比较：1");
					//删除文件
					deleteFile(file);
				}
				//    }
			}
		}
	}

	public static void deleteFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {  //如果是一个文件
				Log.e("huang","删除文件");
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

	/**
	 * 获取前n天日期、后n天日期
	 *
	 * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
	 * @return
	 */
	public static String getOldDate(int distanceDay) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		long time = System.currentTimeMillis();
		Date beginDate = new Date(time);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
		Date endDate = null;
		try {
			endDate = (Date) dft.parse(dft.format(date.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("huang", "前7天==" + dft.format(endDate));
		return dft.format(endDate);
	}

	public static String stringToDate(long time){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		Date date = c.getTime();
		String format = formatter.format(date);
		return format;
	}
}
