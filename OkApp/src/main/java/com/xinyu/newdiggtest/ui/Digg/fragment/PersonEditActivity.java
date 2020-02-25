package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.nanchen.compresshelper.CompressHelper;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.CircleTransform;
import com.xinyu.newdiggtest.utils.IntentParams;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PhotoDialog;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 详细资料
 */
public class PersonEditActivity extends BaseNoEventActivity {

    @BindView(R.id.head)
    public ImageView header;


    @BindView(R.id.name1)
    public TextView name1;

    @BindView(R.id.phone)
    public TextView phone;

    @BindView(R.id.email)
    public TextView email;

    @BindView(R.id.sex)
    public TextView sex;


    @BindView(R.id.birthday)
    public TextView birthday;


    @BindView(R.id.zone)
    public TextView zone;

    DatePickerDialog dialog;


    private final int PICT_RESULT = 1;

    private final int CAMARA = 0;


    static final int EDIT_Name = 0x11;
    static final int EDIT_Phone = 0x12;
    static final int EDIT_Mail = 0x13;
    static final int EDIT_Sex = 0x14;

    File file;

    String headUrl = "";

    String pricinsStr, cityStr;

    CityPickerView mPicker = new CityPickerView();


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_person_edit;
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.finish_commit)
    public void commit() {

        if (file != null && file.exists()) {
            upLoadImg();
        } else {
            commitParams();
        }
    }


    private void upLoadImg() {
        loadingDailog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(this).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        File newFile = CompressHelper.getDefault(this).compressToFile(file);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
        builder.addFormDataPart("imgfile", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名

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
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }
                        List<ImImgBean> uploadList = res.getData();
                        if (uploadList == null || uploadList.size() < 1) {
                            return;
                        }
                        headUrl = uploadList.get(0).getOriginal();

                        commitParams();


                    }
                });
    }

    private void commitParams() {

        JSONObject object = new JSONObject();
        try {
            object.put("custom_head", headUrl);

            object.put("name", name1.getText().toString());

            if (sex.getText().toString().equals("男")) {
                object.put("sex", "M");
            } else {
                object.put("sex", "F");
            }

            if (!phone.getText().toString().equals("未设置")) {
                object.put("mobile", phone.getText().toString());

            }
            if (!email.getText().toString().equals("未设置")) {
                object.put("email", email.getText().toString());
            }

            if (!birthday.getText().toString().equals("未设置")) {
                String date = birthday.getText().toString();
                object.put("birth_day", date.replaceAll("-", ""));
            }

            if (!zone.getText().toString().equals("未设置")) {
                object.put("province", pricinsStr);
                object.put("city", cityStr);
            }

            updateUser(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void updateUser(JSONObject object) {
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("command", "user.UpdateUserInfo");
        requsMap.put("data_from", "OK");
        requsMap.put("user", object.toString());

        url.updateUserInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InfoStr>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(InfoStr msg) {
                        loadingDailog.dismiss();

                        if (msg.getOp().getCode().equals("Y")) {
                            finish();
                        } else {
                            ToastUtils.getInstanc().showToast("用户更新服务异常");
                        }
                    }
                });


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestUserInfo();
        initCalendar();
    }

    private void initCalendar() {
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mPicker.init(this);
    }


    public void requestUserInfo() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "user.getUserInfo");
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        requsMap.put("user_id", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("flag", "Y");
        url.getUserInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            if (msg.getUser() != null) {
                                upHeadView(msg.getUser());
                            }
                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }


    /**
     * 展示数据
     *
     * @param user
     */
    private void upHeadView(UserInfoBean.UserBean user) {


        name1.setText(user.getNickname());
        headUrl = user.getHead();
        Picasso.with(mContext).load(user.getHead()).error(R.drawable.icon_no_download).
                transform(new CircleTransform()).into(header);

        if (!MyTextUtil.isEmpty(user.getSex()) && user.getSex().equals("M")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        if (!MyTextUtil.isEmpty(user.getProvince())) {
            zone.setText(user.getProvince() + " " + user.getCity());
        } else {
            zone.setText("未设置");
        }

        if (!MyTextUtil.isEmpty(user.getMobile())) {
            phone.setText(user.getMobile());
        } else {
            phone.setText("未设置");
        }

        if (!MyTextUtil.isEmpty(user.getBirth_day())) {
            birthday.setText(addHeng(user.getBirth_day()));
        } else {
            birthday.setText("未设置");
        }

        if (!MyTextUtil.isEmpty(user.getEmail())) {
            email.setText(user.getEmail());
        } else {
            email.setText("未设置");
        }


    }

    private String addHeng(String birth_day) {

        String year = birth_day.substring(0, 4);

        String month = birth_day.substring(4, 6);

        String day = birth_day.substring(6, 8);

        return year + "-" + month + "-" + day;
    }


    @OnClick(R.id.ll_edit_head)
    public void edithead() {
        new PhotoDialog(mContext);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICT_RESULT:
                    // 图片、视频、音频选择结果回调

                    LocalMedia pictu = PictureSelector.obtainMultipleResult(data).get(0);

                    String imgpath = pictu.getCompressPath();

                    showImgView(imgpath);

                    break;
                case CAMARA:

                    LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);

                    String phtopath = phto.getCompressPath();

                    showImgView(phtopath);


                    break;

            }
        }
        if (requestCode == EDIT_Name) {
            if (data != null) {
                if (data.hasExtra("IntentData")) {
                    String nameE = data.getStringExtra("IntentData");
                    name1.setText(nameE);
                }
            }


        } else if (requestCode == EDIT_Phone) {

            if (data != null) {
                if (data.hasExtra("IntentData")) {
                    String PhoneE = data.getStringExtra("IntentData");
                    phone.setText(PhoneE);
                }
            }

        } else if (requestCode == EDIT_Mail) {
            if (data != null) {
                if (data.hasExtra("IntentData")) {
                    String mailE = data.getStringExtra("IntentData");
                    email.setText(mailE);
                }
            }
        } else if (requestCode == EDIT_Sex) {
            if (data != null) {
                if (data.hasExtra("IntentData")) {
                    String sexE = data.getStringExtra("IntentData");
                    sex.setText(sexE);
                }
            }
        }


    }

    /**
     * 显示图片
     *
     * @param phtopath
     */
    private void showImgView(String phtopath) {

        file = new File(phtopath);
        if (file.exists()) {


            Glide.with(this)
                    .load(file)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(header);


        }

//        header.setImageDrawable(null);
//
//        Bitmap camorabitmap = fileToBitmap(phtopath, 150);
//        if (null != camorabitmap) {
//            header.setImageBitmap(camorabitmap);
//        }
    }


    public Bitmap fileToBitmap(String imagePath, int kb) {
        Bitmap bitmap = null;
        try {
            File file = new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            FileInputStream fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
            //将bitmap进行压缩防止内存泄漏
            bitmap = compressImage(bitmap, kb);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public static Bitmap compressImage(Bitmap image, int kb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) { //循环判断如果压缩后图片是否大于设定的kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @OnClick(R.id.ll_name)
    public void editName() {

        Intent intent = new Intent(mContext, PersonCommonEditActity.class);
        intent.putExtra(IntentParams.Edit_Type, "1");
        intent.putExtra(IntentParams.GROUP_NAME, name1.getText().toString());
        startActivityForResult(intent, EDIT_Name);


    }

    @OnClick(R.id.ll_phone)
    public void editPhone() {

        Intent intent = new Intent(mContext, PersonCommonEditActity.class);
        intent.putExtra(IntentParams.Edit_Type, "2");
        intent.putExtra(IntentParams.GROUP_NAME, phone.getText().toString());

        startActivityForResult(intent, EDIT_Phone);

    }

    @OnClick(R.id.ll_mail)
    public void editMail() {

        Intent intent = new Intent(mContext, PersonCommonEditActity.class);
        intent.putExtra(IntentParams.Edit_Type, "3");
        intent.putExtra(IntentParams.GROUP_NAME, email.getText().toString());
        startActivityForResult(intent, EDIT_Mail);
    }

    @OnClick(R.id.ll_sex)
    public void editSex() {
        Intent intent = new Intent(mContext, PersonSexEditActity.class);
        intent.putExtra(IntentParams.Edit_Type, "4");
        intent.putExtra(IntentParams.GROUP_NAME, sex.getText().toString());
        startActivityForResult(intent, EDIT_Sex);
    }


    @OnClick(R.id.ll_birth)
    public void editBirth() {
        if (dialog == null) {
            dialog = new DatePickerDialog(mContext,
                    0, listener, currentYear, month, day);
        }
        dialog.show();

    }


    @OnClick(R.id.ll_zone)
    public void editZone() {
//TODO 可以自定义样式
//        CityConfig cityConfig = new CityConfig.Builder().build();

        CityConfig cityConfig = new CityConfig.Builder()
                .confirTextColor("#333333")//确认按钮文字颜色
                .cancelTextColor("#999999")//取消按钮文字颜色
                .province("上海市")//默认显示的省份
                .district("杨浦区")//默认显示省市下面的区县数据
                .provinceCyclic(false)//省份滚轮是否可以循环滚动
                .build();

        mPicker.setConfig(cityConfig);

        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                pricinsStr = province.getName();

                cityStr = city.getName();

                String showStr = city.getName();

                if (showStr.equals("省直辖县级行政单位")) {
                    showStr = "";
                    cityStr = district.getName();
                }

                zone.setText(pricinsStr + " " + showStr + " " + district.getName());
            }

            @Override
            public void onCancel() {
            }
        });

        //显示
        mPicker.showCityPicker();

    }


    int currentYear, month, day;

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {

            String monStr = "";
            String dayStr = "";
            if (month < 9) {
                monStr = "0" + (month + 1);
            } else {
                monStr = "" + (month + 1);
            }

            if (day < 10) {
                dayStr = "0" + day;
            } else {
                dayStr = "" + day;
            }
            birthday.setText(year + "-" + monStr + "-" + dayStr);
        }

    };

}
