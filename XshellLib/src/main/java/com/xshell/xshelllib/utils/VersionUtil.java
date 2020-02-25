package com.xshell.xshelllib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtil {
	/**
	 * 获取APP当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			// 当前应用的版本名称
			//String versionName = info.versionName;
			// 当前版本的版本号
			int versionCode = info.versionCode;
			// 当前版本的包名
			//String packageNames = info.packageName;
			return versionCode;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * 获取APP当前版本名称
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			// 当前应用的版本名称
			String versionName = info.versionName;
			// 当前版本的包名
			String packageNames = info.packageName;
			// 当前版本的版本号
			int versionCode = info.versionCode;
			return versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取APP安装的更新时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getPackLastUpdataTime(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			// 当前应用的上次更新时间
			return info.lastUpdateTime;
		} catch (Exception e) {
			return 0l;
		}
	}
}
