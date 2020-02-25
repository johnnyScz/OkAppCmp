//package com.xshell.xshelllib.tools.weixin;
//
//import android.annotation.SuppressLint;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.os.Handler;
//import android.os.Message;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//@SuppressLint("HandlerLeak")
//public class DownLoadImage {
//
//	private String imagePath;
//
//	public DownLoadImage(String imagePath) {
//		this.imagePath = imagePath;
//	}
//
//	public void loadImage(final ImageCallBack callBack) {
//		final Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				callBack.getDrawable((Drawable) msg.obj);
//			};
//		};
//
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Drawable drawable = Drawable.createFromStream(new URL(imagePath).openStream(), null);
//					Message msg = Message.obtain();
//					msg.obj = drawable;
//					handler.sendMessage(msg);
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//	public void loadImageByte(final ImageByteCallBack callBack) {
//		final Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				callBack.getImageByte((byte[]) msg.obj);
//			};
//		};
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// 默认的连接客服端
//				HttpClient httpClient = new DefaultHttpClient();
//				// 获取远程数据
//				HttpGet httpGet = new HttpGet(imagePath);
//				try {
//					// 执行调用 返回
//					HttpResponse httpResponse = httpClient.execute(httpGet);
//					byte[] b = EntityUtils.toByteArray(httpResponse.getEntity());
//
//					Message msg = Message.obtain();
//					msg.obj = b;
//					handler.sendMessage(msg);
//
//				} catch (ClientProtocolException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//	public void loadBitmap(final BitmapCallBack callback){
//		final Handler handler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				callback.getBitmap((Bitmap) msg.obj);
//			};
//		};
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Bitmap bitmap = BitmapFactory.decodeStream(new URL(imagePath).openStream());
//					Message msg = Message.obtain();
//					msg.obj = bitmap;
//					handler.sendMessage(msg);
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//	/**
//	 * 接口回调
//	 *
//	 * @author fulz
//	 */
//	public interface ImageCallBack {
//		public void getDrawable(Drawable drawable);
//
//	}
//
//	/**
//	 * 接口回调
//	 *
//	 * @author fulz
//	 */
//	public interface ImageByteCallBack {
//		public void getImageByte(byte[] b);
//	}
//
//	/**
//	 * 接口回调
//	 *
//	 * @author fulz
//	 */
//	public interface BitmapCallBack {
//		public void getBitmap(Bitmap bitmap);
//	}
//
//}
