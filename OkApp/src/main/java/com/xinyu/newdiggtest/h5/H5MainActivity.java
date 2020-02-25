package com.xinyu.newdiggtest.h5;


import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import com.xinyu.newdiggtest.utils.AppContacts;
import com.xshell.xshelllib.ui.XinyuHomeActivity;
import com.xshell.xshelllib.utils.RequestWebViewURL;

import com.xshell.xshelllib.utils.XshellConsts;
import com.xshell.xshelllib.utils.XshellEvent;


import org.apache.cordova.CallbackContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * 企业版主页面
 */
public class H5MainActivity extends XinyuHomeActivity {

    private H5MainActivity mContext;


    private static final int CROP_PHOTO = 2;


    CallbackContext callbackContext;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        checkPemisson();

        mContext = this;
        String url = "file:///" + getFilesDir().getAbsolutePath() + File.separator + "index.html";
        loadUrl(url);

        new UiHelper(this).start();
        // appView.getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        RequestWebViewURL.getInstance(mContext).requestURl(appView);


    }


    public int getContentViewRes() {
        return com.xshell.xshelllib.R.layout.xinyusoft_main;
    }

    @Override
    public int getLinearLayoutId() {
        return com.xshell.xshelllib.R.id.linearLayout;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {

        callbackContext = event.object;

        if (event.what == XshellConsts.BackNative) {
            AppContacts.BackFromH5 = 1;
            finish();
        } else if (event.what == XshellConsts.Card_Camera) {
            takePhoto();
        } else if (event.what == XshellConsts.IMG_album) {
            takeAlbum();
        }
    }


    boolean isPermisson = false;

    private void checkPemisson() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            isPermisson = true;

                        } else if (permission.shouldShowRequestPermissionRationale) {

                            isPermisson = false;
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            isPermisson = false;
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });
    }

    /**
     * 相册
     */
    private void takeAlbum() {

        if (isPermisson) {

            PictureSelector.create(mContext)
                    .openGallery(PictureMimeType.ofImage())
                    .forResult(PictureConfig.CHOOSE_REQUEST);


        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {

        if (isPermisson) {
            PictureSelector.create(this)
                    .openCamera(PictureMimeType.ofImage())
                    .compress(true)
                    .forResult(CROP_PHOTO);

        }
    }


    @Override
    protected void onActivityResult(int req, int responseCode, Intent data) {
        super.onActivityResult(req, responseCode, data);

        switch (req) {
            case CROP_PHOTO:

                if (-1 == Activity.RESULT_OK) {
                    try {
                        LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);

                        String path = phto.getCompressPath();

                        sendTakePhoto(path);


                    } catch (Exception e) {
                    }
                } else {
                    Log.d("amtf", "失败");
                }

                break;
            case PictureConfig.CHOOSE_REQUEST:

                if (-1 == Activity.RESULT_OK) {

                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                    if (selectList == null || selectList.size() < 1) {

                    } else {
                        sendH5Album(selectList);
                    }


                } else {
                    Log.d("amtf", "失败");
                }

                break;

            default:
                break;
        }


    }

    private void sendH5Album(List<LocalMedia> selectList) {

        JSONArray array = new JSONArray();

        try {
            for (LocalMedia item : selectList) {
                JSONObject bb = new JSONObject();
                String path = item.getPath();
                bb.put("path", path);
                bb.put("type", getDix(path));
                array.put(bb);
            }


            JSONObject data = new JSONObject();
            data.put("list", array);
            data.put("resultCode", "9000");
            data.put("resultStr", "获取成功");
            callbackContext.success(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 返回h5拍照
     *
     * @param path
     */
    private void sendTakePhoto(String path) {

        JSONObject obj = new JSONObject();

        try {
            obj.put("resultCode", "9000");
            obj.put("resultStr", "拍照成功");
            obj.put("path", path);
            obj.put("type", getDix(path));

            callbackContext.success(obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getDix(String path) {

        if (!TextUtils.isEmpty(path)) {

            int dex = path.lastIndexOf(".");

            if (dex != -1) {
                return path.substring(dex, path.length());
            }
        }

        return "";
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
