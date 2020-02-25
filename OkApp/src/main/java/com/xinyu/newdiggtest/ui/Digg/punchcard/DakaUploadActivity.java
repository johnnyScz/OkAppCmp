package com.xinyu.newdiggtest.ui.Digg.punchcard;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.Nullable;

import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import com.nanchen.compresshelper.CompressHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ImImgBean;

import com.xinyu.newdiggtest.bean.MsgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppUtils;

import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PhotoDialog;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.zhouzhuo.zzimagebox.ZzImageBox;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DakaUploadActivity extends BaseActivity {

    @BindView(R.id.tv_daka)
    public TextView dakaName;

    @BindView(R.id.et_input_moon)
    public EditText inputMoom;//输入心情


    private final int PICT_RESULT = 1;

    private final int CAMARA = 0;

    List<LocalMedia> upDatas;

    ZzImageBox imageBox;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_daka_upload_img;
    }

    private void initView() {
        addImg();


        String name = MyTextUtil.getDecodeStr(getIntent().getStringExtra(IntentParams.SELECT_Target_Name));
        String showName = TextUtils.isEmpty(name) ? "今天打卡" : name;

        if (showName.equals("今天打卡")) {
            dakaName.setText(showName);
        } else {
            if (getIntent().hasExtra(IntentParams.SELECT_Target_TIME)) {
                dakaName.setText("#" + showName + " " + getIntent().getStringExtra(IntentParams.SELECT_Target_TIME) + "#");
            } else {
                dakaName.setText("#" + showName + "#");
            }

        }

    }


    @OnClick(R.id.tv_cancel)
    public void goBack() {
        finish();
    }

    @OnClick(R.id.tv_save)
    public void goSave() {
        if (upDatas == null || upDatas.size() == 0) {
            loadingDailog.show();
            requestDaka("", inputMoom.getText().toString().trim());
            return;
        } else {
            goUpLoad();
        }


    }

    private void goUpLoad() {
        loadingDailog.show();
        List<String> pathList = getList();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名
        //多张图片
        for (int i = 0; i < pathList.size(); i++) {
            File file = new File(pathList.get(i));//filePath 图片地址
            File newFile = CompressHelper.getDefault(App.mContext).compressToFile(file);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
            builder.addFormDataPart("imgfile" + i, file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        url.uploadImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(DakaUploadActivity.this).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(DakaUploadActivity.this).showToast("文件上传失败");
                            return;
                        }
                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }
                        doSave(data);
                    }
                });
    }

    /**
     * 开始打卡
     */
    private void doSave(List<ImImgBean> imageList) {

        String urlMultiStr = AppUtils.getMultiStr(imageList);
        requestDaka(urlMultiStr, inputMoom.getText().toString().trim());
    }


    private void requestDaka(String mUrls, String des) {
        loadingDailog.show();

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl api = manager.create(AppUrl.class);

        Map<String, String> map = new HashMap<>();
        map.put("plan_id", getIntent().getStringExtra(IntentParams.DAKA_Target_Id));
        map.put("state", getIntent().getStringExtra(IntentParams.DAKA_State));
        map.put("url", mUrls);

        map.put("desc", MyTextUtil.getUrl3Encoe(des));

        map.put("command", "ok-api.updateTargetExecuteFinish");
        map.put("sid", PreferenceUtil.getInstance(this).getSessonId());
        api.dakaUpload(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MsgBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.getInstanc(DakaUploadActivity.this).showToast(e.getMessage());
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onNext(MsgBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc(DakaUploadActivity.this).showToast("打卡成功！");

                            EventBus.getDefault().post(new XshellEvent(EventConts.HomeFresh));

                            finish();
                        }

                    }
                });

    }


    private List<String> getList() {
        List<String> datas = new ArrayList<>();
        for (LocalMedia item : upDatas) {
            datas.add(item.getCompressPath());
        }
        return datas;
    }


    private void addImg() {

        imageBox = (ZzImageBox) findViewById(R.id.zz_image_box);
        //如果需要加载网络图片，添加此监听。
        imageBox.setOnImageClickListener(new ZzImageBox.OnImageClickListener() {
            @Override
            public void onImageClick(int position, String filePath, ImageView iv) {
                Log.e("amtf", "onImageClick 点击了");
            }

            @Override
            public void onDeleteClick(int position, String filePath) {
                imageBox.removeImage(position);
                int len = upDatas.size();
                for (int i = len - 1; i >= 0; i--) {
                    LocalMedia bean = upDatas.get(i);
                    if (bean.getCompressPath().equals(filePath)) {
                        upDatas.remove(bean);
                        break;
                    }

                }
            }

            @Override
            public void onAddClick() {

                new PhotoDialog(DakaUploadActivity.this);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICT_RESULT:
                    // 图片、视频、音频选择结果回调

                    List<LocalMedia> latest = PictureSelector.obtainMultipleResult(data);

                    if (upDatas == null) {
                        upDatas = latest;
                    } else {
                        upDatas.addAll(latest);
                    }


                    if (latest != null || latest.size() > 0) {
                        int lent = latest.size();
                        for (int i = 0; i < lent; i++) {
                            imageBox.addImage(latest.get(i).getCompressPath());
                        }
                    }
                    break;
                case CAMARA:

                    LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);
                    imageBox.addImage(phto.getCompressPath());
                    if (upDatas == null) {
                        upDatas = new ArrayList<>();
                        upDatas.add(phto);
                    } else {
                        upDatas.add(phto);
                    }

//                    File newFile = CompressHelper.getDefault(this).compressToFile(new File(phto.getCompressPath()));

                    break;

            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.MSG_OpenCamera) {
//            checkPemisson();
        }

    }

    private void openCamera() {

        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        if (!outputImage.exists()) {
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            outputImage.delete();
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = FileProvider.getUriForFile(this, "com.xinyu.newdiggtest", outputImage);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMARA);


    }


    private void checkPemisson() {

        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
//                            takePhoto();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 下次再次启动时，还会提示请求权限的对话框
                            Log.e("amtf", permission.name + " is denied. More info should be provided.用户拒绝了该权限，没有选中『不再询问』");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });

    }


}
