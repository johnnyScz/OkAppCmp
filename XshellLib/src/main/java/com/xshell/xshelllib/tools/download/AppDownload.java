package com.xshell.xshelllib.tools.download;//package com.xinyusoft.xshell.tools.download;
//
//import java.io.File;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.DialogInterface.OnClickListener;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.Toast;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.xinyusoft.xshell.application.AppConstants;
//import com.xinyusoft.xshell.ui.LoadingActivity;
//import com.xinyusoft.xshell.utils.OfficeFileUtil;
//import com.xinyusoft.xshell.utils.PreferenceXshellUtil;
//
//public class AppDownload {
//	/**
//	 * 检查app更新
//	 */
//	public static void updateApp(String url, final Context context , final Handler mHandler) {
//		HttpUtils http = new HttpUtils();
//		if (url == null || PreferenceXshellUtil.getInstance().getAppUpdateTime() == null
//				|| "".equals(PreferenceXshellUtil.getInstance().getAppUpdateTime()) || "".equals(url)) {
//			checkH5();
//			return;
//		}
//		http.send(HttpRequest.HttpMethod.GET, url + PreferenceXshellUtil.getInstance().getAppUpdateTime(),
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						Toast.makeText(context, "检查app失败，请下次网络好的情况下检查", Toast.LENGTH_SHORT).show();
//						checkH5();
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> res) {
//						JSONObject json;
//						try {
//							json = new JSONObject(res.result);
//							JSONObject op = json.getJSONObject("op");
//							String code = op.getString("code");
//							int count = json.getInt("count");
//							final String changezip = json.getString("changezip");
//							if (code.equals("Y")) {// 检查更新成功
//								if (count != 0) {// 有更新
//									showAppDialog(changezip, context, mHandler);
//								} else {
//									checkH5();
//								}
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//
//				});
//
//	}
//
//	/**
//	 * 显示更新dialog
//	 */
//	private static void showappDialog(final String zip, final Context context,final Handler mHandler) {
//		AlertDialog.Builder builder = new Builder(context);
//		builder.setMessage("需要更新吗？");
//		builder.setTitle("提示");
//		builder.setCancelable(false);
//		builder.setPositiveButton("立即更新", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				// LoadingActivity.this.finish();
//				// startDownloadService();
//				downloadApp(zip,mHandler,context);
//			}
//		});
//		builder.setNegativeButton("下次再说", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				checkH5();
//			}
//		});
//		builder.create().show();
//	}
//
//	private void openApp(Context context) {
//		Intent openIntent = new Intent();
//		openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		openIntent.setAction(android.content.Intent.ACTION_VIEW);
//		Uri uri = Uri.fromFile(new File(PreferenceXshellUtil.getInstance().getDownAppDir()));
//		openIntent.setDataAndType(uri, "application/vnd.android.package-archive");
//		context.startActivity(openIntent);
//	}
//
//	/**
//	 * 下载app
//	 */
//	private void downloadApp(final String zipName, final Handler mHandler,final Context context) {
//		mHandler.sendEmptyMessage(SHOW_MESSAGE);
//		HttpUtils http = new HttpUtils();
//		PreferenceXshellUtil.getInstance().setDownAppDir(
//				OfficeFileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME));
//		String file = OfficeFileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE,
//				AppConstants.APP_APK_NAME);
//		String url = configInfo.get("app_url_download");
//		http.download(url, file, false, true, new RequestCallBack<File>() {
//
//			@Override
//			public void onSuccess(ResponseInfo<File> responseInfo) {
//				// 设置xversion的最新更新的时间
//				String time = zipName.split("-")[1].split("\\.")[0];
//				// 设置应当升级
//				PreferenceXshellUtil.getInstance().setNextToInstall(true);
//				// 下载状态 标志位置为初始化状态
//				PreferenceXshellUtil.getInstance().setDownloadingApp(false);
//				PreferenceXshellUtil.getInstance().setAppUpdateTime(time);
//				openApp(context);
//			}
//
//			@Override
//			public void onFailure(com.lidroid.xutils.exception.HttpException error, String msg) {
//				Toast.makeText(context, "下载失败，请重试！", Toast.LENGTH_SHORT).show();
//				checkH5();
//			}
//
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//				float allFileSize = (float) (Math.round(((float) (total * 1.0 / 1000000)) * 10) / 10.0);
//				float currentFileSize = (float) (Math.round(((float) (current * 1.0 / 1000000)) * 10) / 10.0);
//				showMessageUsehandler("APP下载进度：" + currentFileSize + "M /" + allFileSize + "M", mHandler);
//			}
//		});
//	}
//	
//	
//	private void showMessageUsehandler(String showString ,Handler mHandler) {
//		Message msg = Message.obtain();
//		msg.what = UPDATE_MESSAGE;
//		msg.obj = showString;
//		mHandler.sendMessage(msg);
//
//	}
//
//	private static void checkH5() {
//		Html5Download.checkH5();
//	}
//}
