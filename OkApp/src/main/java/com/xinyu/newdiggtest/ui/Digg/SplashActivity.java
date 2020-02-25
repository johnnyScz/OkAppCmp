package com.xinyu.newdiggtest.ui.Digg;

import android.Manifest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;


import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.VersionCheckBean;


import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.AppUploadFragmentDialog;


import java.io.IOException;


import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseNoEventActivity {


    SplashActivity mContext;

//    long downloadId = -1;
//    DownloadFinishReceiver mReceiver;

//    DownloadManager downloadManager;
//    File file;

    Handler mhandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initBar();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goIntent();
            }
        }, 2000);

    }

    private void initBar() {

//        initImg();
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    }

    /**
     * 图片自适应，防止拉升变形
     */
//    private void initImg() {
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.launch_image_port);
//        int bwidth = bitmap.getWidth();
//        int bHeight = bitmap.getHeight();
//        int width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
//        int height = bHeight * width / bwidth;
//        ImageView img1 = (ImageView) this.findViewById(R.id.imgview);
//        ViewGroup.LayoutParams para1 = img1.getLayoutParams();
//        para1.height = height;
//        img1.setLayoutParams(para1);
//    }


//    private void rigester() {
//        mReceiver = new DownloadFinishReceiver();
//        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mReceiver);
//    }
    private void goIntent() {

//        if (PreferenceUtil.getInstance(mContext).getAppFun().equals("-1")) {
//            startActivity(new Intent(this, PreLoginActivity.class));
//        } else if (PreferenceUtil.getInstance(mContext).getAppFun().equals("1")) {
//            startActivity(new Intent(mContext, LoadingActivity.class));
//        } else {
//            if (PreferenceUtil.getInstance(mContext).isAppFirstEnter()) {
//                PreferenceUtil.getInstance(mContext).setAppFirstEnter(false);
//                startActivity(new Intent(mContext, GuidActivity.class));
//            } else {
//                startActivity(new Intent(mContext, HomeDiggActivity.class));
//            }
//        }

//        startService(new Intent(this, H5Service.class));
//        startActivity(new Intent(mContext, LoadingActivity.class));

        finish();
    }


//    public void checkMyVersion() {
//
//        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
//        AppUrl url = manager.create(AppUrl.class);
//        HashMap<String, String> maps = new HashMap<>();
//
//        maps.put("command", "ok-api.getNewVersion");
//        maps.put("version", AppUtils.getVersionName(this) + "");
//        maps.put("sid", PreferenceXshellUtil.getInstance(this).getSessonId());
//
//        url.getAppVersion(maps).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<VersionBean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("amtf", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(VersionBean msg) {
//                        if (msg.getOp().getCode().equals("Y")) {
//
//                        } else {
//                            Log.e("amtf", "服务异常");
//                        }
//
//                    }
//                });
//
//    }


    private void showUploadDialog(VersionCheckBean data) {

        final AppUploadFragmentDialog dialog = new AppUploadFragmentDialog();
        dialog.setVersionData(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialog.show(ft, "tag");

        dialog.setOnPopListner(new AppUploadFragmentDialog.OnPopClickListner() {
            @Override
            public void onCancle() {
                dialog.dismiss();
            }

            @Override
            public void upload() {
                dialog.dismiss();
                checkPemisson();
            }
        });


    }

    private void checkPemisson() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            goDownloadManager();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });
    }


    private void goDownloadManager() {

//        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://121.40.148.145/testapp/testApp.apk"));
//        file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.apk");
//        request.setDestinationUri(Uri.fromFile(file));
////添加请求 开始下载
//        downloadId = downloadManager.enqueue(request);
//
//        ToastUtils.getInstanc().showToast("下载进度在通知栏显示!");
//        getProgress(downloadId);

    }


    public void getProgress(long downloadId) {
        //查询进度
//        final DownloadManager.Query query = new DownloadManager.Query()
//                .setFilterById(downloadId);
//
//        NotificationManageUtil.createNotification(ctx);


//        CountDownTimer timer = new CountDownTimer(500 * 1000, 500) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (!isFinish) {
//                    queryCusor(query);
//                } else {
//                    this.cancel();
//                }
//            }
//
//            @Override
//            public void onFinish() {
//
//
//            }
//        }.start();


    }

//    private void queryCusor(DownloadManager.Query query) {
//        Cursor cursor = null;
//        int progress = 0;
//        try {
//            cursor = downloadManager.query(query);//获得游标
//            if (cursor != null && cursor.moveToFirst()) {
//                //当前的下载量
//                int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                //文件总大小
//                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
//
//                Message message = new Message();
//                message.what = progress;
//                NotificationManageUtil.getInstance(mContext).handler.sendMessage(message);
//
//            }
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

    boolean isFinish = false;

//    private class DownloadFinishReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //下载完成的广播接收者
//            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//
//            if (completeDownloadId == downloadId) {
//
//                isFinish = true;
////                setPermission(file.getPath());
//
//                installApp();
//            }
//
//
//        }
//    }

    private void installApp() {

        Log.e("amtf", "安装下载包");

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//        Uri apkUri = FileProvider.getUriForFile(this, "com.xinyu.newdiggtest", file);
////添加这一句表示对目标应用临时授权该Uri所代表的文件
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        startActivity(intent);

    }

    /**
     * 提升读写权限
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
