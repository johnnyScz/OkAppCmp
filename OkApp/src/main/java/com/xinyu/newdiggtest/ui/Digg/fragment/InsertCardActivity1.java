package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nanchen.compresshelper.CompressHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CardFilterBean;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CardInsertBean;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 插入名片
 */
public class InsertCardActivity1 extends BaseNoEventActivity {


    @BindView(R.id.scroll)
    ScrollView scrollView;


    @BindView(R.id.input_name)
    EditText input_name;

    @BindView(R.id.input_pos)
    EditText input_pos;

    @BindView(R.id.input_phone)
    EditText input_phone;

    @BindView(R.id.input_mail)
    EditText input_mail;

    @BindView(R.id.input_company)
    EditText input_company;

    @BindView(R.id.input_host)
    EditText input_host;

    @BindView(R.id.input_adress)
    EditText input_adress;


    @BindView(R.id.add)
    ImageView add;


    @BindView(R.id.add1)
    ImageView nativePost;

    @BindView(R.id.delet_pos)
    ImageView delet_pos;

    @BindView(R.id.delet_negtive)
    ImageView delet_negtive;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_card;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermiss();
    }


    boolean isPermisison = false;

    private void checkPermiss() {
        RxPermissions rxPermission = new RxPermissions(mContext);
        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            isPermisison = true;
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            isPermisison = false;
                            // 下次再次启动时，还会提示请求权限的对话框
                        } else {

                            isPermisison = false;
                            // 用户拒绝了该权限，并且选中『不再询问』
                        }
                    }
                });
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.add)
    public void goUploadZheng() {

        if (isPermisison) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");//相片类型
            startActivityForResult(intent, 0x11);
        } else {
            ToastUtils.getInstanc().showToast("请开启相册读写权限");
        }


    }


    @OnClick(R.id.add1)
    public void goUploadFan() {


        if (isPermisison) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");//相片类型
            startActivityForResult(intent, 0x12);
        } else {
            ToastUtils.getInstanc().showToast("请开启相册读写权限");
        }


    }

    @OnClick(R.id.filt_tx)
    public void filtTx() {

        if (zhengFile == null) {
            ToastUtils.getInstanc().showToast("请上传名片");
            return;
        }

        uploadZhengPhoto(true);

    }

    File zhengFile, fanFile;
    String upLoadUrl = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 0x11://正面

                if (-1 == Activity.RESULT_OK) {
                    try {
                        Uri uri = data.getData();

                        add.setImageURI(uri);

                        delet_pos.setVisibility(View.VISIBLE);

                        zhengmian = 1;

                        String path = getRealPathFromURI(uri);

                        File file = new File(path);//filePath 图片地址

                        zhengFile = CompressHelper.getDefault(this).compressToFile(file);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("amtf", "失败");
                }


                break;


            case 0x12://反面

                if (-1 == Activity.RESULT_OK) {
                    try {

                        Uri uri = data.getData();

                        nativePost.setImageURI(uri);
                        delet_negtive.setVisibility(View.VISIBLE);
                        fanmian = 1;


                        String path = getRealPathFromURI(uri);
                        File file = new File(path);//filePath 图片地址
                        fanFile = CompressHelper.getDefault(this).compressToFile(file);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("amtf", "失败");
                }

                break;


        }


    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    int zhengmian = 0, fanmian = 0;

    @OnClick(R.id.delet_pos)
    public void delet_pos() {
        add.setImageResource(R.drawable.add);
        this.findViewById(R.id.delet_pos).setVisibility(View.GONE);
        zhengmian = 0;
    }

    @OnClick(R.id.delet_negtive)
    public void delet_Nagetive() {

        nativePost.setImageResource(R.drawable.add);
        this.findViewById(R.id.delet_negtive).setVisibility(View.GONE);
        fanmian = 0;
    }


    @OnClick(R.id.conform)
    public void conform() {


        if (zhengmian + fanmian > 0) {

            if (zhengmian + fanmian == 2) {

                if (!MyTextUtil.isEmpty(upLoadUrl)) {
                    if (fanmian == 1) {
                        uploadFanPhoto(upLoadUrl);
                    } else {
                        check2CommitParams(upLoadUrl, "");
                    }

                } else {
                    uploadZhengPhoto(false);
                }

            } else if (zhengmian == 1) {
                if (!MyTextUtil.isEmpty(upLoadUrl)) {
                    check2CommitParams(upLoadUrl, "");
                } else {
                    uploadZhengPhoto(false);
                }

            } else if (fanmian == 1) {
                uploadFanPhoto("");
            }

        } else {
            check2CommitParams("", "");
        }

    }


    private void check2CommitParams(String zheng, String fan) {

        if (MyTextUtil.isEmpty(input_name.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入姓名");
            return;
        }

        if (MyTextUtil.isEmpty(input_pos.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入职位");
            return;
        }

        if (TextUtils.isEmpty(input_phone.getText()) && TextUtils.isEmpty(input_mail.getText())) {
            ToastUtils.getInstanc().showToast("请输入联系方式!");
            return;
        }

        if (MyTextUtil.isEmpty(input_company.getText().toString())) {

            ToastUtils.getInstanc().showToast("请输入公司名称");
            return;
        }

        doCommitParams(zheng, fan);

    }


    /**
     * 提交参数
     * <p>
     * TODO
     *
     * @param zheng
     * @param fan
     */
    private void doCommitParams(String zheng, String fan) {

        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi netApi = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        requsMap.put("f_name", input_name.getText().toString());
        requsMap.put("f_organization_name", input_company.getText().toString());
        requsMap.put("f_mobile", input_phone.getText().toString());
        requsMap.put("f_position", input_pos.getText().toString());
        requsMap.put("f_origin_pic_front", zheng);
        requsMap.put("f_origin_pic_back", fan);
        requsMap.put("f_email", input_mail.getText().toString());
        requsMap.put("f_addredss", input_adress.getText().toString());
        requsMap.put("f_homepage", input_host.getText().toString());

        requsMap.put("f_plugin_type_id", "103");


        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        netApi.insertCard(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CardInsertBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CardInsertBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("名片插入成功");
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("名片插入失败!");
                        }


                    }
                });
    }


    /**
     * 是否点击录入
     *
     * @param isFilter
     */
    private void uploadZhengPhoto(final boolean isFilter) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), zhengFile);
        builder.addFormDataPart("imgfile", zhengFile.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(2);
        AppUrl url = manager.create(AppUrl.class);
        url.uploadOkImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {

                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }

                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }

                        upLoadUrl = data.get(0).getOriginal();

                        if (isFilter) {
                            filterImg(upLoadUrl);
                        } else {
                            if (zhengmian + fanmian == 2) {
                                uploadFanPhoto(upLoadUrl);
                            } else {
                                check2CommitParams(upLoadUrl, "");
                            }
                        }


                    }
                });
    }


    /**
     * 过滤图片信息
     *
     * @param zhengFile
     */
    private void filterImg(String zhengFile) {
        queryInfo(zhengFile);
    }


    private void uploadFanPhoto(final String zhengFileUrl) {
        loadingDailog.show();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fanFile);
        builder.addFormDataPart("imgfile", fanFile.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(2);
        AppUrl url = manager.create(AppUrl.class);
        url.uploadOkImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {

                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }

                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }

                        String FanUrl = data.get(0).getOriginal();

                        if (!MyTextUtil.isEmpty(FanUrl)) {
                            check2CommitParams(zhengFileUrl, FanUrl);
                        }
                    }
                });
    }


    /**
     * 过滤名片
     */
    public void queryInfo(String fname) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi netApi = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("fname", fname);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        netApi.queryCardInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CardFilterBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CardFilterBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null) {

                                filterCard(msg.getData());

                            }


                        } else {
                            ToastUtils.getInstanc().showToast("文字提取失败!");
                        }


                    }
                });
    }


    private void filterCard(CardFilterBean.CardBean data) {

        if (!MyTextUtil.isEmpty(data.getF_name())) {
            input_name.setText(data.getF_name());
        }

        if (!MyTextUtil.isEmpty(data.getF_organization_name())) {
            input_company.setText(data.getF_organization_name());
        }

        if (!MyTextUtil.isEmpty(data.getF_mobile())) {
            input_phone.setText(data.getF_mobile());
        }

        if (!MyTextUtil.isEmpty(data.getF_email())) {
            input_mail.setText(data.getF_email());
        }

        if (!MyTextUtil.isEmpty(data.getF_addredss())) {
            input_adress.setText(data.getF_addredss());
        }

        if (!MyTextUtil.isEmpty(data.getF_homepage())) {
            input_host.setText(data.getF_homepage());
        }


        if (!MyTextUtil.isEmpty(data.getF_position())) {
            input_pos.setText(data.getF_position());
        }
        scrollView.smoothScrollTo(0, 0);
    }


    /**
     * 过滤名片
     */


    @OnClick(R.id.clear)
    public void clear() {

        input_name.setText("");
        input_pos.setText("");

        input_phone.setText("");
        input_mail.setText("");
        input_company.setText("");
        input_host.setText("");
        input_adress.setText("");


        add.setImageResource(R.drawable.add);
        this.findViewById(R.id.delet_pos).setVisibility(View.GONE);
        zhengmian = 0;


        nativePost.setImageResource(R.drawable.add);
        this.findViewById(R.id.delet_negtive).setVisibility(View.GONE);
        fanmian = 0;

    }


}




