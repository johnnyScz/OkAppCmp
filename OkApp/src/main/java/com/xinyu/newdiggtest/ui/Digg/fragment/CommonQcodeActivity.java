//package com.xinyu.newdiggtest.ui.Digg.fragment;
//
//import android.Manifest;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Environment;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//import com.tbruyelle.rxpermissions2.Permission;
//import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.net.ApiConfig;
//import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
//import com.xinyu.newdiggtest.utils.CircleCornerForm;
//import com.xinyu.newdiggtest.utils.IntentParams;
//import com.xinyu.newdiggtest.utils.MyTextUtil;
//import com.xinyu.newdiggtest.utils.PreferenceUtil;
//import com.xinyu.newdiggtest.utils.QRCodeUtil;
//import com.xinyu.newdiggtest.utils.ToastUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.functions.Consumer;
//
//public class CommonQcodeActivity extends BaseNoEventActivity {
//
//    @BindView(R.id.gr_name)
//    public TextView gr_name;
//
//    @BindView(R.id.title)
//    public TextView title;
//
//    @BindView(R.id.info)
//    public TextView info;
//
//
//    @BindView(R.id.gr_head)
//    public ImageView gr_head;
//
//
//    @BindView(R.id.qcode)
//    public ImageView qcode;
//
//    ExecutorService excutor = Executors.newCachedThreadPool();
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        initView();
//    }
//
//    private void initView() {
//        String name = getIntent().getStringExtra("roomName");
//        String headUrl = getIntent().getStringExtra("headUrl");
//        gr_name.setText(name);
//        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("personnal")) {
//            title.setText("我的二维码");
//            info.setText("扫一扫上面的二维码图案，私信我");
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("group")) {
//            title.setText("群二维码名片");
//            info.setText("扫一扫上面的二维码图案，邀请好友进群");
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target")) {
//            title.setText("目标二维码");
//            info.setText("扫一扫上面的二维码图案，关注好友目标");
//            gr_name.setText(MyTextUtil.getDecodeStr(name));
//        }
//
//
//        if (headUrl.endsWith("-1")) {
////            gr_head.setImageResource(R.mipmap.icon_zidingyi);
//        } else {
//            Picasso.with(mContext).load(headUrl).error(R.drawable.icon_no_download).
//                    transform(new CircleCornerForm()).into(gr_head);
//        }
//
//
//        RxPermissions rxPermission = new RxPermissions(this);
//        rxPermission.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .subscribe(new Consumer<Permission>() {
//                    @Override
//                    public void accept(Permission permission) throws Exception {
//                        if (permission.granted) {
//                            goSdcardImg();
//                        } else {
//                            ToastUtils.getInstanc().showToast("请打开读写Sdcard权限");
//                        }
//                    }
//                });
//
//
//    }
//
//    private void goSdcardImg() {
//
//        excutor.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                String path = Environment.getExternalStorageDirectory().getPath();
//
//                File tempFile = new File(path, "qcode.jpeg");
//
//                try {
//                    if (!tempFile.exists()) {
//                        tempFile.createNewFile();
//                    } else {
//                        tempFile.delete();
//                        tempFile.createNewFile();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                String filePath = tempFile.getPath();
//
//                String qcodeUrl = creatStr();
//
//                boolean success = QRCodeUtil.createQRImage(qcodeUrl, 300, 300,
//                        BitmapFactory.decodeResource(mContext.getResources(), 0),
//                        filePath);
//
//
//                /**
//                 * 读取sdcard 图片
//                 */
//                if (success) {
//                    final Bitmap bit = BitmapFactory.decodeFile(filePath); //自定义//路径
//                    if (bit != null) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                qcode.setImageBitmap(bit);
//                            }
//                        });
//                    }
//                }
//
//
//            }
//        });
//
//    }
//
//    private String creatStr() {
//        String code = "";
//        if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("personnal")) {
//
//            code = ApiConfig.BASE_URL + "myself/" + PreferenceUtil.getInstance(mContext).getUserId() + "/"
//                    + PreferenceUtil.getInstance(mContext).getSessonId();
//
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("group")) {
//            //群聊
//            code = ApiConfig.BASE_URL + "group/" +
//                    getIntent().getStringExtra("roomId")
//                    + "/" + PreferenceUtil.getInstance(mContext).getUserId() + "/"
//                    + PreferenceUtil.getInstance(mContext).getSessonId();
//        } else if (getIntent().getStringExtra(IntentParams.Intent_Enter_Type).equals("target")) {
//            code = ApiConfig.BASE_URL + "target/" +
//                    getIntent().getStringExtra("targetId")
//                    + "/" + PreferenceUtil.getInstance(mContext).getUserId();
//        }
//
//
//        return code;
//    }
//
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_group_qcode;
//    }
//
//
//    @OnClick(R.id.icon_back)
//    public void goBack() {
//
//        finish();
//    }
//
//
//}
//
//
//
//
