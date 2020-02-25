package com.xshell.xshelllib.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 把assets中数据写入到data中
 */
public class Assets2DataCardUtil {
	private static final String TAG = "Assets2DataCardUtil";

	/**
	 * 将一个asset里面的文件数据写入到Data中
	 * 
	 * @param CopyfileName
	 *            源文件名
	 * @param stickFileName
	 *            拷贝后的文件名
	 * @param context
	 *            上下文
	 * @return 拷贝后的文件
	 */
	public static File write2DataFromInput(String CopyfileName, String stickFileName, Context context) {
		Log.d(TAG, "进入了write2DataFromInput");
		File file = null;
		OutputStream output = null;
		InputStream inputStream = null;
		try {
			inputStream = context.getAssets().open(CopyfileName);
			Log.e(TAG, " inputStream" + inputStream.toString());
			// 拥有可读可写权限，并且有足够的容量
			file = new File(context.getFilesDir().getAbsoluteFile(), stickFileName);
			output = new BufferedOutputStream(new FileOutputStream(file));
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (output != null) {
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.e(TAG, file.toString());
		return file;
	}
}
