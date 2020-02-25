//package com.xshell.xshelllib.sqlite;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest;
//import com.xshell.xshelllib.tools.socketutil.OnResultMessage;
//import com.xshell.xshelllib.tools.socketutil.SocketUtil;
//
//import org.apache.cordova.CordovaWebView;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class UserService {
//
//	private DBOpenHelper dbOpenHelper;
//	private HttpUtils http;
//
//	public UserService(Context context,String tablename) {
//		dbOpenHelper = new DBOpenHelper(context,tablename);
//		http = new HttpUtils();
//
//	}
//
//
//	public void websocketrequest(final String requeststr, final String callbackConttext, final UserService us, final UserBean ub, final CordovaWebView webView){
//		final String[] content = {requeststr};
//		final String[] resultstr = {""};
//		OnResultMessage mResultMessage = new OnResultMessage() {
//			@Override
//			public void resultMessage(String result) {
//				Log.i("zzy", "result:" + result);
//				resultstr[0] = result;
//				try {
//					ub.setUrl(requeststr);
//					ub.setRequestcontent(result);
//					//ub = us.jsonToUserBean(new JSONObject(result),ub);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				us.update(ub);
//				webView.loadUrl("javascript:" + callbackConttext + "('"+result+"')");
//			}
//		};
//		SocketUtil.sendPushMessage(content[0],mResultMessage );
//
//	}
//
//	public String getMd5(String requesturl){
//		final String[] md5 = {""};
//		http.send(HttpRequest.HttpMethod.GET, requesturl, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				JSONObject json;
//				try {
//					json = new JSONObject(responseInfo.result);
//					md5[0] = json.getString("md5");
//				}catch (Exception e){
//
//				}
//			}
//
//			@Override
//			public void onFailure(HttpException e, String s) {
//
//			}
//		});
//		return md5[0];
//	}
//
//	public String getData(String requesturl){
//		String md5 = "";
//		http.send(HttpRequest.HttpMethod.GET, requesturl, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				JSONObject json;
//				try {
//					json = new JSONObject(responseInfo.result);
//
//				}catch (Exception e){
//
//				}
//			}
//
//			@Override
//			public void onFailure(HttpException e, String s) {
//
//			}
//		});
//		return md5;
//	}
//
//	public void save(UserBean ub) {
//		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//		ContentValues contentValues = new ContentValues();
//
//		contentValues.put("url", ub.getUrl());
//		contentValues.put("requestcontent", ub.getRequestcontent());
//		contentValues.put("md5", ub.getMd5());
//		database.insert("datamanage", null, contentValues);
//		database.close();
//	}
//
//
//	public int updateFramUrl(String taskGoal){
//		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("unReadMessageNum", 0);
//		int i = database.update("user", contentValues, "taskGoal=?", new String[]{taskGoal});
//		database.close();
//		return i;
//	}
//
//	public void update(UserBean ub) {
//		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("url", ub.getUrl());
//		contentValues.put("requestcontent", ub.getRequestcontent());
//		contentValues.put("md5", ub.getMd5());
//		database.update("datamanage", contentValues, "url=?",
//				new String[]{ub.getUrl()});
//		database.close();
//	}
//
//	public UserBean find(String url) {
//		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
//		Cursor cursor = database.query("datamanage", new String[]{"url",
//						"requestcontent", "md5"}, "url=?", new String[]{url},
//				null, null, null);
//
//		if (cursor.moveToNext()) {
//			UserBean mi = new UserBean();
//			mi.setUrl(cursor.getString(0));
//			mi.setRequestcontent(cursor.getString(1));
//			mi.setMd5(cursor.getString(2));
//			database.close();
//			return mi;
//		}
//		database.close();
//		return null;
//	}
//
//	public int delete(String url) {
//		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
//		int i = database.delete("datamanage", "url=?", new String[]{url});
//		database.close();
//		return i;
//	}
//
//	public UserBean jsonToUserBean(JSONObject json, UserBean ub) {
//		try {
//			ub.setUrl(json.getString("url"));
//			ub.setRequestcontent(json.getString("requestcontent"));
//			ub.setMd5(json.getString("md5"));
//			return ub;
//		} catch (JSONException e) {
//
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//
//	}
//
//
//}
