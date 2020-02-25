package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.CreatFeedBackBean;
import com.xinyu.newdiggtest.bean.FeedBackBean;
import com.xinyu.newdiggtest.bean.FeedBean;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyMiddleDialog;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PhotoDialog;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.zhouzhuo.zzimagebox.ZzImageBox;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FeedBackActivity extends BaseNoEventActivity {

    private final int PICT_RESULT = 1;

    private final int CAMARA = 0;

    @BindView(R.id.rg_type)
    public RadioGroup groupBtn;

    List<FeedBean> datalist;

    @BindView(R.id.edt_feedback)
    public EditText edt_feedback;


    @BindView(R.id.input_count)
    public TextView input_count;


    @BindView(R.id.zz_image_box)
    public ZzImageBox imagebox;

    @BindView(R.id.btn_commit)
    public Button btn_commit;

    int select = 0;//默认选择是第一个


    List<LocalMedia> upDatas;
    List<ImImgBean> uploadList;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        requestAdviceType();
    }

    /**
     * 动态显示意见内容
     */

    public void requestAdviceType() {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("command", "ok-api.GetFeedbackType");
        requsMap.put("dict_no", "A01");
        url.getAdviceInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeedBackBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(FeedBackBean msg) {
                        if (msg.getOp().getCode().equals("Y")) {
                            showDongtai(msg);

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }
                    }
                });
    }

    private void showDongtai(FeedBackBean data) {
        if (!MyTextUtil.isEmpty(data.getIllustration())) {
            edt_feedback.setHint(data.getIllustration());
        }
        List<FeedBean> list = data.getData();
        if (list == null || list.size() == 0) {
            return;
        }
        setGroupbox(list);
    }


    /**
     * groupRadio 设置 数据
     *
     * @param list
     */
    private void setGroupbox(List<FeedBean> list) {
        datalist = list;
        int len = list.size();
        for (int i = 0; i < len; i++) {
            RadioButton child = (RadioButton) groupBtn.getChildAt(i);
            child.setText(list.get(i).getF_sub_name());
        }
    }

    private void initView() {
        groupBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_one:
                        select = 0;
                        break;

                    case R.id.rb_two:
                        select = 1;
                        break;

                    case R.id.rb_thrid:
                        select = 2;
                        break;
                }

            }
        });

        edt_feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int len = s.toString().trim().length();

                input_count.setText(len + "/" + 500);

                if (len < 1) {
                    btn_commit.setBackgroundResource(R.drawable.shaper_orange_noanable);
                    btn_commit.setEnabled(false);
                } else {
                    btn_commit.setBackgroundResource(R.drawable.shaper_bg_orange);
                    btn_commit.setEnabled(true);
                }
                if (len > 500) {
                    edt_feedback.setText(s.toString().substring(0, 500));
                }


            }
        });

        imagebox.setOnImageClickListener(new ZzImageBox.OnImageClickListener() {
            @Override
            public void onImageClick(int position, String filePath, ImageView iv) {
                Log.e("amtf", "onImageClick 点击了");
            }

            @Override
            public void onDeleteClick(int position, String filePath) {
                imagebox.removeImage(position);
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
                if (upDatas != null && upDatas.size() >= 4) {
                    ToastUtils.getInstanc().showToast("最多只能上传4张图片");
                    return;
                }

                new PhotoDialog(mContext);
            }
        });


    }

    @OnClick(R.id.icon_back)
    public void goback() {
        finish();
    }

    @OnClick(R.id.btn_commit)
    public void goCommit() {

        String content = edt_feedback.getText().toString().trim();

        if (MyTextUtil.isEmpty(content)) {
            ToastUtils.getInstanc().showToast("请提交文字!");
            return;
        }
        if (upDatas != null && upDatas.size() > 0) {
            goUpLoad();
        } else {
            creatFeedBack();
        }


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
                            imagebox.addImage(latest.get(i).getCompressPath());
                        }
                    }
                    break;
                case CAMARA:

                    LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);
                    imagebox.addImage(phto.getCompressPath());

                    if (upDatas == null) {
                        upDatas = new ArrayList<>();
                        upDatas.add(phto);
                    } else {
                        upDatas.add(phto);
                    }


                    break;

            }
        }
    }


    /**
     * 新增用户反馈
     */

    public void creatFeedBack() {
        FeedBean data = datalist.get(select);
        loadingDailog.show();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("dic_main", data.getF_dict_no());
        requsMap.put("dic_sub", data.getF_sub_no());
        requsMap.put("platform", "android");
        requsMap.put("createuser", PreferenceUtil.getInstance(mContext).getUserId());
        requsMap.put("content", edt_feedback.getText().toString().trim());

        //TODO 没有图片就不传


//        URLEncoder.encode(nickname, "UTF-8")//编码
        if (uploadList != null && uploadList.size() > 0) {
            String imgJson = creatJson(uploadList);
            requsMap.put("imglist", imgJson);
        }

        url.creatFeedBack(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreatFeedBackBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(CreatFeedBackBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {

                            showDialog();

                        } else {
                            ToastUtils.getInstanc(mContext).showToast("提交失败, 请稍后重试");
                        }
                    }
                });
    }

    private String creatJson(List<ImImgBean> uploadList) {
        JSONArray jsonArray = new JSONArray();
        try {
            for (ImImgBean item : uploadList) {
                JSONObject object = new JSONObject();
                object.put("img", item.getOriginal());
                object.put("thumbnail", item.getThumbnail());
                jsonArray.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


    MyMiddleDialog myMiddleDialog;

    private void showDialog() {
        if (myMiddleDialog == null) {
            myMiddleDialog = new MyMiddleDialog(this, R.style.MyMiddleDialogStyle) {
                @Override
                protected View getView() {
                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_feedback, null);
                    initDialogView(view);
                    return view;
                }
            };
        }
        myMiddleDialog.show();
    }

    private void initDialogView(View view) {
        view.findViewById(R.id.conform).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myMiddleDialog.dismiss();
                finish();

            }
        });

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
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
                        ToastUtils.getInstanc(mContext).showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {
                        loadingDailog.dismiss();
                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }
                        uploadList = res.getData();
                        if (uploadList == null || uploadList.size() < 1) {
                            return;
                        }
                        creatFeedBack();
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


}
