package com.xinyu.newdiggtest.office;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.tencent.smtt.sdk.TbsReaderView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;


import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

public class OfficeX5CoreActivity extends BaseNoEventActivity implements TbsReaderView.ReaderCallback {

    TbsReaderView mTbsReaderView;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    Context mctx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mctx = this;
        initView();

    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.core_layout;
    }


    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTbsReaderView = new TbsReaderView(this, this);
        RelativeLayout rootRl = (RelativeLayout) findViewById(R.id.rl_root);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            } else {
                displayFile();
            }
        } else {
            displayFile();
        }
    }

    ExecutorService excutor = Executors.newCachedThreadPool();


    private void displayFile() {

        final String path = getIntent().getStringExtra("path");

        if (!MyTextUtil.isEmpty(path)) {

            if (path.contains("http")) {
                final String type = getFileType(path);
                excutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        downloadFile(path, "officeFile." + type);
                    }
                });
            }
        }


    }

    private void downloadFile(String path, String fileName) {

        String folderStr = FileUtil.getInstance().getSDCardRoot() + "xshell";

        File folder = new File(folderStr);
        if (!folder.exists()) {
            folder.mkdir();
        }
        OkHttpUtils.get().url(path).build().execute(new FileCallBack(FileUtil.getInstance().getSDCardRoot() + "xshell", fileName) {
            @Override
            public void onError(Call call, Exception e, int i) {
                Toast.makeText(OfficeX5CoreActivity.this, "下载失败，请稍候再试！", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }

            @Override
            public void onResponse(File file, int i) {

                if (file.exists()) {
                    String temp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OKTemp";
                    File file1 = new File(temp);
                    if (!file1.exists()) {
                        file1.mkdir();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", file.getAbsolutePath());
                    bundle.putString("tempPath", temp);

                    boolean result = mTbsReaderView.preOpen(getFileType(file.getPath()), false);
                    if (result) {
                        mTbsReaderView.openFile(bundle);
                    }
                }

            }
        });


    }


    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }

        str = paramString.substring(i + 1);
        return str;
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    boolean isOk = true;
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            isOk = false;
                            Toast.makeText(this, "您拒绝了文件读写权限,将文法", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (isOk) {
                        displayFile();
                    }
                }
                break;
            default:
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTbsReaderView != null)
            mTbsReaderView.onStop();

    }


}
