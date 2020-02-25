package com.xshell.xshelllib.utils;

import android.util.Log;

/**
 * linux命令工具类
 * @author zzy
 *
 */
public class LinuxCommandUtil {
	public static boolean runCommand(String command) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			Log.i("command", "The Command is : " + command);
			process.waitFor();
		} catch (Exception e) {
			Log.w("Exception ", "Unexpected error - " + e.getMessage());
			return false;
		} finally {
			try {
				process.destroy();
			} catch (Exception e) {
				Log.w("Exception ", "Unexpected error - " + e.getMessage());
			}
		}
		return true;
	}
}
