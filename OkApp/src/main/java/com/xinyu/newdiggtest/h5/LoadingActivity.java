package com.xinyu.newdiggtest.h5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.app.HomeAppActivity;
import com.xinyu.newdiggtest.bean.VersionCheckBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.Digg.fragment.AppUploadFragmentDialog;
import com.xinyu.newdiggtest.ui.Digg.login.AppLoginActivity;
import com.xinyu.newdiggtest.utils.LogUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;
import com.xshell.xshelllib.application.AppConfig;
import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.application.AppContext;
import com.xshell.xshelllib.utils.Assets2DataCardUtil;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.ParseConfig;
import com.xshell.xshelllib.utils.PreferenceXshellUtil;
import com.xshell.xshelllib.utils.VersionUtil;
import com.xshell.xshelllib.utils.ZIPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import okhttp3.Call;

/**
 * 初始界面（包括下载，更新等初始化）
 *
 * @author zzy
 */
public class LoadingActivity extends AppCompatActivity {
    private static final String TAG = "LoadingActivity";
    private Activity context;
    private static Handler mHandler;

    /**   */
    private TextView showMessage;


    private static final int UPDATE_MESSAGE = 222;

    /**
     * 解析的配置文件集合
     */
    private Map<String, String> configInfo;

    // 程序的开始时间

    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mdenyList = new ArrayList<>();

    private void checkMyPermission() {
        mdenyList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mdenyList.add(permissions[i]);
            }
        }
        if (mdenyList.isEmpty()) {//未授予的权限为空，表示都授予了
            checkH5();

        } else {//请求权限方法
            String[] permissions = mdenyList.toArray(new String[mdenyList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(context, permissions, 1);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xinyusoft_activity_splash2);

        initView();

        configInfo = ParseConfig.getInstance(LoadingActivity.this).getConfigInfo();


        // 删除前台的检测更新包
        String[] temp = AppContext.CONTEXT.getPackageName().split("\\.");
        String fileName = FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, temp[temp.length - 1] + "backgroundupdatehtml5.zip");
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }


        new Thread() {
            @Override
            public void run() {
                // -1解析appconfig.xml

                PreferenceUtil.getInstance(App.mContext).setAppUpdateTime("202012201525");

                docheckFile();

            }
        }.start();


        checkMyPermission();

    }


    private void docheckFile() {

        //TODO 判断是否清理缓存了，如果是拿到以前的包进行解压
        try {
            // 判断是否覆盖安装了，是的话，重新解压并且重新设置时间
            if (VersionUtil.getVersionCode(context) > PreferenceXshellUtil.getInstance().getAppThisCode()) {

                // 设置app的code
                PreferenceXshellUtil.getInstance().setAppThisCode(VersionUtil.getVersionCode(context));


                // 只需要第一次设置更新时间（app的还有页面的更新时间）

                String h5Time = changeTime(configInfo.get("html-update-time"));


                PreferenceXshellUtil.getInstance().setFileUpdateTime(h5Time);
                // 保存homeActivity的path，由此获得它的Class
                PreferenceXshellUtil.getInstance().setHomeActivityPath(configInfo.get("class-home"));

                File zip = Assets2DataCardUtil.write2DataFromInput("www.zip", "www.zip", AppContext.CONTEXT);
                ZIPUtils.unzip(zip.getAbsolutePath(), AppContext.CONTEXT.getFilesDir().getAbsolutePath());
                File cordovaFile = Assets2DataCardUtil.write2DataFromInput("cordova_android.zip", "cordova_android.zip", AppContext.CONTEXT);
                //解压html5文件

                //解压cordova需要的文件
                ZIPUtils.unzipTest(cordovaFile.getAbsolutePath(), getFilesDir().getAbsolutePath());

                // 解压之后就删除
                if (zip.exists()) {
                    zip.delete();
                }

                if (AppConfig.DEBUG)
                    Log.e("zzy", "第一次解压完成！!!!!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 把2018-0202-2230 变成201802022230
     *
     * @return
     */
    private String changeTime(String xmlTime) {

        if (!MyTextUtil.isEmpty(xmlTime) && xmlTime.contains("-")) {
            String newStr = xmlTime.replaceAll("-", "");
            return newStr;
        }
        return xmlTime;
    }


    /**
     * 检查html5更新
     */
    private void checkH5() {
        // 没有写下载列表就直接跳过这个
        if (configInfo.get("xversion-html-name") == null || "".equals(configInfo.get("xversion-html-name"))) {
            jump();
            return;
        }
        String url = configInfo.get("xversion-update-url") + "&appname=" + configInfo.get("xversion-html-name") + "&time=" + PreferenceXshellUtil.getInstance().getFileUpdateTime() + "&platform=Android" + "&xshllversion="
                + VersionUtil.getVersionCode(context);

        Log.w("amtf", "zip更新地址:" + url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Toast.makeText(LoadingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                jump();
            }

            @Override
            public void onResponse(String s, int i) {
                try {

                    showMessageUsehandler("请等待...", -1);
                    downloadH5JsonFile(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                    jump();
                }
            }
        });
    }

    /**
     * 下载html5
     *
     * @param res json的字符串
     * @throws JSONException
     */
    private void downloadH5JsonFile(String res) throws JSONException {
        JSONObject json = new JSONObject(res);
        JSONObject op = json.getJSONObject("op");
        String code = op.getString("code");

        int count = json.getInt("count");
        final String changezip = json.getString("changezip");

        JSONArray array = json.getJSONArray("changelist");
        for (int i = 0; i < array.length(); i++) {
            JSONObject html = array.getJSONObject(i);
            String path = html.getString("path");
            String status = html.getString("status");
            if ("DELETE".equals(status)) {  //找到带有删除状态的并且删除
                File file = new File(getFilesDir() + path.replaceAll("\\\\", "/"));
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        //执行完之后，设置可以显示引导页，等待下次安装覆盖的时候来显示
        PreferenceXshellUtil.getInstance().setFirstRun(true);

        if (code.equals("Y")) {// 检查更新成功
            if (count != 0) {// 有更新

                zipUpdateH5(changezip);
                //是第一次安装，不需要改变状态。

            } else {// 没有更新
                PreferenceXshellUtil.getInstance().setHtmlUpdate(false);
                if (AppConfig.DEBUG)
                    Log.e("amtf", "没有更新，直接跳转:" + PreferenceXshellUtil.getInstance().getFileUpdateTime());
                jump();
            }
        } else {

            jump();
        }
    }


    private void showMessageUsehandler(String showString, int progress) {
        Message msg = Message.obtain();
        msg.what = UPDATE_MESSAGE;
        msg.obj = showString;
        msg.arg1 = progress;
        mHandler.sendMessage(msg);

    }

    private void initView() {


        AppContext.CONTEXT = context = LoadingActivity.this;
        showMessage = (TextView) findViewById(R.id.showLoadingMessage);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE_MESSAGE:
                        showMessage.setText((String) msg.obj);

                        break;
                }
            }
        };


    }


    /**
     * 跳转
     */
    private void jump() {

        startJump();

    }

    private void startJump() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (MyTextUtil.isEmpty(PreferenceUtil.getInstance(context).getUserId())) {
                    startActivity(new Intent(context, AppLoginActivity.class));
                } else {
                    startActivity(new Intent(context, HomeAppActivity.class));
                }
                finish();
            }
        }, 2000);

    }


    /**
     * 用zip的方式更新html5
     *
     * @param changezip zip的名字
     */
    private void zipUpdateH5(String changezip) {
        try {
            final String time = changezip.split("-")[1].split("\\.")[0];


            if (!"".equals(changezip)) {
                changezip = changezip.replaceAll("\\\\", "/");
                if (changezip.startsWith("/")) {
                    changezip = changezip.substring(1);
                }
            } else {

                startJump();
                return;
            }
            //拼接下载html的zip包的url
            String url = configInfo.get("xversion-download-url") + changezip;
            Log.e("amtf", "zip包下载链接：" + url);
//            probar.setVisibility(View.VISIBLE);
            OkHttpUtils.get().url(url).build().execute(new FileCallBack(FileUtil.getInstance().getSDCardRoot() + AppConstants.XINYUSOFT_CACHE, "updatehtml5.zip") {
                @Override
                public void onError(Call call, Exception e, int i) {
                    Toast.makeText(context, "下载失败，请稍候再试！", Toast.LENGTH_SHORT).show();
                    PreferenceXshellUtil.getInstance().setHtmlUpdate(false);
                    jump();
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    float allFileSize = (float) (Math.round(((float) (total * 1.0 / 1000000)) * 10) / 10.0);
                    float currentFileSize = (float) (Math.round(((float) (progress * total * 1.0 / 1000000)) * 10) / 10.0);

                    int myProgress = (int) (currentFileSize / allFileSize * 100);

                    showMessageUsehandler("请稍等..." + myProgress + "%", myProgress);
                }

                @Override
                public void onResponse(File file, int i) {
                    try {
                        ZIPUtils.unzipTest(file.getAbsolutePath(), AppContext.CONTEXT.getFilesDir().getAbsolutePath());
                        PreferenceXshellUtil.getInstance().setFileUpdateTime(time);// 设置上次更新时间（设置最新版本号）
                        // 修改所有的文件为只读权限 （zip下载不能单个的设置只读）
                        PreferenceXshellUtil.getInstance().setHtmlUpdate(true);
                        jump();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            PreferenceXshellUtil.getInstance().setHtmlUpdate(false);
            startJump();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context) {
        if (context == null) {
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 0 && requestCode == 153) {

            origInstallApp();
            ;//再次执行安装流程，包含权限判等
        } else {
            ToastUtils.getInstanc().showToast("你没有开启安装apk权限!");
        }
    }

    private void showLocalDialog() {
        AlertDialog.Builder buid = new AlertDialog.Builder(context);
        buid.setMessage("你没有开启该应用的安装apk权限,去系统设置中开启?");
        buid.setPositiveButton(
                "确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {

                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        startActivityForResult(intent, 0x99);

                    }
                }
        );
        buid.setNegativeButton(
                "取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        ToastUtils.getInstanc().showToast("该应用将无法安装apk");
                        checkH5();
                    }
                }
        );

        AlertDialog dialog = buid.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(R.color.bar_grey_90));

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (permissions.length == 0) {
                isAppUpdate();
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isAppUpdate();
            } else {
                Toast.makeText(context, "由于您拒绝了授权，更新将无法进行！", Toast.LENGTH_SHORT).show();
                jump();
            }
        }

    }


    /**
     * 检查app更新
     */
    private void isAppUpdate() {
        // 是否更新app
        String list = configInfo.get("xversion-update-url") + "&appname=" + configInfo.get("xversion-app-name") + "&time=";
        if ("".equals(PreferenceUtil.getInstance(App.mContext).getAppUpdateTime()) || (configInfo.get("xversion-app-name") == null)) {
            checkH5();
            return;
        }
        String url = list + PreferenceUtil.getInstance(App.mContext).getAppUpdateTime();

        LogUtil.e("app的更新地址：" + url);

        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

                checkH5();
            }

            @Override
            public void onResponse(String s, int i) {

                JSONObject json;
                try {
                    json = new JSONObject(s);
                    //Write2SDCard.getInstance().writeMsg("appJson" + json);
                    JSONObject op = json.getJSONObject("op");
                    String code = op.getString("code");
                    if (code.equals("Y")) {// 检查更新成功

                        String changezip = json.getString("changezip");
                        int count = json.getInt("count");

                        if (count != 0) {// 有更新
                            //显示下dialog
//                            showAppDialog(changezip);
                            showUploadDialog(changezip);

                        } else {
                            checkH5();
                        }
                    } else {
                        checkH5();
                    }
                } catch (Exception e) {
                    checkH5();
                }
            }
        });
    }


    /**
     * 下载app
     */
    private void downloadApp(final String zipName) {

        PreferenceUtil.getInstance(App.mContext).setDownAppDir(FileUtil.getInstance().getFilePathInSDCard(AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME));

        String url = configInfo.get("xversion-download-url") + configInfo.get("xversion-app-name") + "/" + configInfo.get("xversion-apk-name");
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory() + "/" + AppConstants.XINYUSOFT_CACHE, AppConstants.APP_APK_NAME) {
            @Override
            public void onError(Call call, Exception e, int i) {

                checkH5();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                //super.inProgress(progress, total, id);

                final float allFileSize = (float) (Math.round(((float) (total * 1.0 / 1000000)) * 10) / 10.0);
                final float currentFileSize = (float) (Math.round(((float) (progress * total * 1.0 / 1000000)) * 10) / 10.0);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessage.setText("APP下载进度：" + currentFileSize + "M /" + allFileSize + "M");
                    }
                });
            }

            @Override
            public void onResponse(File file, int i) {

                // 设置xversion的最新更新的时间
                String time = zipName.split("-")[1].split("\\.")[0];

                PreferenceUtil.getInstance(App.mContext).setAppUpdateTime(time);

                openApp();

            }

        });
    }

    private void openApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
            if (!hasInstallPermission) {
                showLocalDialog();
                return;
            } else {
                origInstallApp();
            }
        } else {
            origInstallApp();
        }
        finish();

    }

    private void origInstallApp() {

        File file = new File(PreferenceUtil.getInstance(App.mContext).getDownAppDir());
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(this, "com.xinyu.newdigg", file);
//添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }


    private void showUploadDialog(final String zip) {

        VersionCheckBean data = new VersionCheckBean();
        List<String> datalist = new ArrayList<>();
        datalist.add("增加app自动更新");
        datalist.add("修复若干bug");
        data.setDatalist(datalist);
        data.setVersion("2.7");


        final AppUploadFragmentDialog dialog = new AppUploadFragmentDialog();
        dialog.setVersionData(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new AppUploadFragmentDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
                checkH5();
            }

            @Override
            public void upload() {
                dialog.dismiss();
                downloadApp(zip);
            }
        });


    }


}
