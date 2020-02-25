package com.xshell.xshelllib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;


import com.xshell.xshelllib.sqlite.DataLocalityManager;
import com.xshell.xshelllib.tools.http.MyRequestCallBack;
import com.zhy.http.okhttp.OkHttpUtils;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by huang on 2017/11/3.
 * XMessageUpload上传
 */
public class RequestWebViewURL {

    private static RequestWebViewURL xMessageUpload;
    private static final String TAG = "XMessageUpload";
    private Context mContext;
    private int maxUpload = 10;
    private DataLocalityManager dataLocalityManager;
    private String appName;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private RequestWebViewURL(Context context) {
        this.mContext = context;
    }

    public static RequestWebViewURL getInstance(Context context) {
        if (xMessageUpload == null) {
            synchronized (TAG) {
                if (xMessageUpload == null) {
                    xMessageUpload = new RequestWebViewURL(context.getApplicationContext());
                }
            }
        }
        return xMessageUpload;
    }

    public void requestURl(CordovaWebView appView) {
        SystemWebView view = (SystemWebView) appView.getEngine().getView();
        view.setWebViewClient(new SystemWebViewClient(view.parentEngine) {
            @SuppressLint("NewApi")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, final WebResourceRequest request) {
                String url = request.getUrl().toString();
               // Log.e("huanguu", "url:==" + url);
                String scheme = Uri.parse(url).getScheme().trim();
                if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                    if (url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".jepg")) {
                        //截取图片后面的类型
                      //  Log.e("huanguu", "url:==" + url);
//                        String[] split = url.split("\\.");
//                        String imageType = "." + split[1];
                        String fileName = EncodeUtil.MD5Encode(url);//将图片转换为MD5，原因可以判断是否是同一张图片
                        File file = new File(mContext.getFilesDir() + "/cacheimage/other" + File.separator + fileName);
                        if (file.exists()) {
                            FileInputStream fileInputStream = null;
                            WebResourceResponse webResourceResponse = null;
                            try {
//                                File file1 = new File("/data/user/0/com.xinyusoft.mom.partner.test/files/cacheimage/other/6a1becbc766c769ce3f276a2dec85342");
                                fileInputStream = new FileInputStream(file);//
//                                InputStream open = getAssets().open("dd.jpg");
                                //  Log.e("huanguu", "本url:" + url);
                                webResourceResponse = new WebResourceResponse("image/png", "UTF-8", fileInputStream);
                                //   return webResourceResponse;
//                                return super.shouldInterceptRequest(view, request);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return webResourceResponse;
                        } else {
                            MyRequestCallBack requestCallBack = new MyRequestCallBack(file.getParent(), fileName) {
                                @Override
                                public void inProgress(float progress, long total, int id) {
//                       Log.e("huang","长在下载集合......");
                                }

                                @Override
                                public void onError(Call call, Exception e, int i) {
                              //      Log.e("huang", "00000:" + e.toString());
                                }

                                @Override
                                public void onResponse(File file, int i) {
                                //    Log.e("huang", "下载成功：" + file.getPath());

                                }
                            };

                            try {
                                requestCallBack.setCallbackName(fileName);
                                OkHttpUtils.get().url(url)
                                        .build()
                                        .execute(requestCallBack);//下载自动存放在本地//fileName
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return super.shouldInterceptRequest(view, request);
                        }
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                String scheme = Uri.parse(url).getScheme().trim();
                if (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) {
                    if (url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".jepg")) {
                        //截取图片后面的类型
                        Log.e("huanguu", "url:==" + url);
//                        String[] split = url.split("\\.");
//                        String imageType = "." + split[split.length-1];
                        String fileName = EncodeUtil.MD5Encode(url);//将图片转换为MD5，原因可以判断是否是同一张图片
                        File file = new File(mContext.getFilesDir() + "/cacheimage/other" + File.separator + fileName);
                        if (file.exists()) {
                            FileInputStream fileInputStream = null;
                            WebResourceResponse webResourceResponse = null;
                            try {
                                fileInputStream = new FileInputStream(file);
//                                InputStream open = getAssets().open("dd.jpg");
                               // Log.e("huanguu", "本url:" + url);
//                                webResourceResponse = new WebResourceResponse("image/png", "UTF-8", fileInputStream);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            return null;
                            return webResourceResponse;
                        } else {
                            MyRequestCallBack requestCallBack = new MyRequestCallBack(file.getParent(), fileName) {
                                @Override
                                public void inProgress(float progress, long total, int id) {
//                       Log.e("huang","长在下载集合......");
                                }

                                @Override
                                public void onError(Call call, Exception e, int i) {
                                 //   Log.e("huang", "00000:" + e.toString());
                                }

                                @Override
                                public void onResponse(File file, int i) {
                                   // Log.e("huang", "下载成功：" + file.getPath());

                                }
                            };

                            try {
                                requestCallBack.setCallbackName(fileName);
                                OkHttpUtils.get().url(url)
                                        .build()
                                        .execute(requestCallBack);//下载自动存放在本地//fileName
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return super.shouldInterceptRequest(view, url);
                        }
                    }
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
    }
}
