package com.xinyu.newdiggtest.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MyDakaAdapter;
import com.xinyu.newdiggtest.adapter.TodoInfoAdapter;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.NineImgBean;
import com.xinyu.newdiggtest.bean.RetListBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.PraiseListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

import static android.content.Context.WIFI_SERVICE;

public class AppUtils {

    static OnMsgCode msgSend;

    Activity context;


    public AppUtils(Activity mctx) {
        this.context = mctx;

    }


    @TargetApi(19)
    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();
            //获取自己布局的根视图
            View rootView = ((ViewGroup) (decorViewGroup.findViewById(android.R.id.content))).getChildAt(0);
            //预留状态栏位置
            rootView.setFitsSystemWindows(true);

            //添加状态栏高度的视图布局，并填充颜色
            View statusBarTintView = new View(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    getInternalDimensionSize(activity.getResources(), "status_bar_height"));
            params.gravity = Gravity.TOP;
            statusBarTintView.setLayoutParams(params);
            statusBarTintView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarTintView);
        }
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void rxCountCode(final int count, final TextView llSms) {
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(count + 1) //设置循环次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (msgSend != null) {
                    msgSend.sendMsg();
                }
                //llSms.setTextColor( context.getResources().getColor(R.color.line_color));
                //TODO textView可设为置灰
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        llSms.setText("获取验证码");
                        llSms.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        llSms.setText(aLong + "S后重发");
                        llSms.setEnabled(false);
                    }
                });
    }


    public static void rxCountCode(final int count, final TextView llSms, final Context mctx) {
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take(count + 1) //设置循环次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                if (msgSend != null) {
                    msgSend.sendMsg();
                }
                llSms.setTextColor(mctx.getResources().getColor(R.color.button_vip));
                //TODO textView可设为置灰
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        llSms.setText("获取验证码");
                        llSms.setEnabled(true);
//                        llSms.setTextColor(mctx.getResources().getColor(R.color.button_vip));
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        llSms.setText(aLong + "S");
                        llSms.setEnabled(false);

                    }
                });
    }


    public interface OnMsgCode {
        void sendMsg();
    }

    public static void setOnMsgCode(OnMsgCode msgCallback) {
        msgSend = msgCallback;
    }


    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }


    public void setImmerseLayout(View view) {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(context.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


    /**
     * 返回app 版本名称
     * <p>
     * 对应build.gradle中的versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


    /**
     * 获取版本号
     *
     * @param ctx
     * @return
     */
    public static int getLocalVersionCode(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            LogUtil.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }


    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }

    /**
     * 手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }


    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return "Android" + android.os.Build.VERSION.RELEASE;
    }


    public static String getIpStr() {
        WifiManager wifiManager = (WifiManager) App.mContext.getSystemService(WIFI_SERVICE);
        WifiInfo w = wifiManager.getConnectionInfo();
        return intToIp(w.getIpAddress());
    }

    public static String intToIp(int ipAddress) {
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));

    }


    /**
     * 多图片上传时转成jsonStr
     *
     * @param imageList
     * @return
     */
    public static String getMultiStr(List<ImImgBean> imageList) {
        try {
            List<Map<String, String>> data = new ArrayList<>();
            int len = imageList.size();
            for (int i = 0; i < len; i++) {
                ImImgBean bean = imageList.get(i);
                HashMap<String, String> map = new HashMap<>();
                map.put("type", "1");
                map.put("original", bean.getOriginal());
                map.put("thumbnail", bean.getThumbnail());
                map.put("width", bean.getWidth());
                map.put("height", bean.getHeight());
                data.add(map);
            }
            Gson gson = new Gson();
            return gson.toJson(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }


    static Bitmap bitmap;

    public static Bitmap returnBitMap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }


    public static String getTypeStr(String latstMsg) {

        String content = "";
        if (latstMsg.endsWith(".png") || latstMsg.endsWith(".jpg") || latstMsg.endsWith(".jpeg")
                || latstMsg.endsWith(".JPG") || latstMsg.endsWith(".PNG") || latstMsg.endsWith(".JPEG")
                || latstMsg.contains("original")) {
            content = "[图片]";
        } else if (latstMsg.endsWith(".aac") || latstMsg.endsWith(".AAC")) {
            content = "[语音]";
        } else {
            content = latstMsg;
        }
        if (content == null || content.equals("null")) {
            content = "";
        }

        return content;
    }

    public static String convertMsg(String latstMsg) {
        String content = "";
        if (latstMsg.endsWith(".png") || latstMsg.endsWith(".jpg") || latstMsg.endsWith(".jpeg")
                || latstMsg.endsWith(".JPG") || latstMsg.endsWith(".PNG") || latstMsg.endsWith(".JPEG")
                || latstMsg.contains("original")) {
            content = "[图片]";
        } else if (latstMsg.endsWith(".aac") || latstMsg.endsWith(".AAC")) {
            content = "[语音]";
        } else {
            content = latstMsg;
        }

        return content;
    }


    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static String getShowTime(FollowListBean item) {
        String start = item.getF_target_start_date();
        String end = item.getF_target_end_date();

        if (MyTextUtil.isEmpty(start) || MyTextUtil.isEmpty(end) || start.length() < 5) {
            return "";
        }

        String relax = start.substring(4, 5);
        String nStart = start.replaceAll(relax, ".");
        String nEnd = end.replaceAll(relax, ".");
        return nStart.substring(5, start.length()) + "-" + nEnd.substring(5, start.length());
    }


    public static List<FavortItem> convertPaise(List<DakaBottowItem> likes) {
        List<FavortItem> mdate = new ArrayList<>();
        FavortItem tt;
        for (DakaBottowItem item : likes) {
            tt = new FavortItem();
            tt.setName(item.getF_nick_name());
            tt.setContent(item.getF_comment());
            mdate.add(tt);
        }
        return mdate;
    }

    public static List<CommentItem> conVertCommont(List<DakaBottowItem> targetcomment, String type) {
        int len = targetcomment.size();
        List<CommentItem> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            DakaBottowItem daka = targetcomment.get(i);
            CommentItem item = new CommentItem();
            item.setCommentUser(daka.getF_nick_name());
            item.setType(type);
            try {
                String content = URLDecoder.decode(daka.getF_comment(), "UTF-8");
                item.setContent(content);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            list.add(item);
        }
        return list;
    }


    public void bindMyData(BaseViewHolder helper, final FollowListBean item, final MyDakaAdapter.onPopClick popListner) {
        incoSwich(helper, item);
        helper.addOnClickListener(R.id.iv_target_icon);

        ImageView icon = helper.getView(R.id.iv_target_icon);

        Picasso.with(context).load(item.getUserBean().getHead()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(icon);


        helper.setText(R.id.tv_daka_title, item.getUserBean().getNickname());
        String targetName = TextUtils.isEmpty(item.getF_target_name()) ? "" : item.getF_target_name();
        if (MyTextUtil.isEmpty(targetName)) {
            helper.setText(R.id.tv_descrep, MyTextUtil.getDecodeStr(item.getF_title()));
        } else {
            helper.setText(R.id.tv_descrep, "#" + MyTextUtil.getDecodeStr(targetName) + " " + AppUtils.getShowTime(item) + "#");
        }


        String content = item.getF_comment();
        String showContent = "";
        if (!MyTextUtil.isEmpty(content)) {
            try {
                showContent = URLDecoder.decode(content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            helper.setText(R.id.mycontent, showContent);

        }

        helper.setVisible(R.id.img_check, false);
        helper.setVisible(R.id.commont, true);

        if (TextUtils.isEmpty(item.getF_watch_img())) {
            helper.setVisible(R.id.nineGrid, false);
        } else {
            helper.setVisible(R.id.nineGrid, true);
            NineGridView netView = helper.getView(R.id.nineGrid);

            String temp = item.getF_watch_img();
            List<NineImgBean> imgList = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(temp);
                if (array == null || array.length() < 1)
                    return;
                int len = array.length();

                for (int i = 0; i < len; i++) {
                    JSONObject object = array.getJSONObject(i);
                    NineImgBean nineBean = new NineImgBean(object.getString("original"), object.getString("thumbnail"));
                    imgList.add(nineBean);
                }
                showImgs(netView, imgList);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (MyTextUtil.isEmpty(item.getF_updatetime())) {
            helper.setVisible(R.id.time_tv, false);
        } else {
            helper.setVisible(R.id.time_tv, true);
            String time = item.getF_updatetime();
            String timeStr = DateUtil.longToHm(Long.parseLong(time));

            String nTime = timeStr.substring(timeStr.length() - 5, timeStr.length());
            helper.setText(R.id.time_tv, nTime);
        }


        /**
         *评论和点赞
         */

        boolean noLikes = (item.getTargetlikes() == null || item.getTargetlikes().size() == 0);
        boolean noComment = (item.getTargetcomment() == null || item.getTargetcomment().size() == 0);
        boolean noDashangMoney = (item.getExcitation() == null || item.getExcitation().size() == 0);


        String type = "0";
        if (item.getTargetlikes() != null && item.getTargetlikes().size() > 0) {
            List<DakaBottowItem> data = item.getTargetlikes();
            for (DakaBottowItem kk : data) {
                if (kk.getF_like_user().equals(PreferenceUtil.getInstance(context).getUserId())) {
                    type = "1";
                    break;
                }
            }
        }
        final ImageView view = helper.getView(R.id.iv_common_more);
        final CommentPopupWindow pop = new CommentPopupWindow(context, type, true);
        pop.update();
        pop.setmItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                if (content.equals("赞") || content.equals("取消")) {
                    if (popListner != null) {
                        popListner.onZanClick(item, content);
                    }
                } else if (content.equals("打赏")) {
                    if (popListner != null) {
                        popListner.onDaShangClick(item);
                    }
                } else if (content.equals("评论")) {
                    if (popListner != null) {
                        popListner.onCommentClick(item, view);
                    }
                }
            }
        });


        helper.getView(R.id.iv_common_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.showPopupWindow(v);
            }
        });


        if (noLikes && noComment && noDashangMoney) {
            helper.setVisible(R.id.digCommentBody, false);
        } else {
            helper.setVisible(R.id.digCommentBody, true);
        }

        CommentListView convertView = helper.getView(R.id.commentList);

        PraiseListView praiseView = helper.getView(R.id.praiseListView);

        if (item.getTargetlikes() != null && item.getTargetlikes().size() > 0) {
            praiseView.setVisibility(View.VISIBLE);
            praiseView.setDatas(convertPaise(item.getTargetlikes()));

        } else {
            praiseView.setVisibility(View.GONE);
        }
        DakaDashangListView dashangView = helper.getView(R.id.dashanglistview);
        if (item.getExcitation() == null || item.getExcitation().size() == 0) {
            dashangView.setVisibility(View.GONE);
        } else {
            dashangView.setVisibility(View.VISIBLE);
            dashangView.setDatas(item.getExcitation());
        }
        if (item.getTargetcomment() != null && item.getTargetcomment().size() > 0) {
            convertView.setVisibility(View.VISIBLE);

            convertView.setDatas(AppUtils.conVertCommont(item.getTargetcomment(), "1"));
        } else {
            convertView.setVisibility(View.GONE);
        }
    }


    public void incoSwich(BaseViewHolder helper, FollowListBean item) {
        ImageView icon = helper.getView(R.id.iv_target_icon);
        if (!MyTextUtil.isEmpty(item.getF_class_id())) {
            int drawableId = context.getResources().getIdentifier(item.getF_class_id(), "mipmap", context.getPackageName());
            icon.setImageResource(drawableId);
        }
    }

    private void showImgs(NineGridView netView, List<NineImgBean> imageDetails) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (imageDetails != null) {
            for (NineImgBean imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail.getThumbnail());
                info.setBigImageUrl(imageDetail.getOriginal());
                imageInfo.add(info);
            }
        }
        netView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        //单张图大小
        netView.setSingleImageSize(AppUtils.dp2px(context, 150));
    }


    /**
     * recycleView 滚动到指定位置
     *
     * @param manager
     * @param mRecyclerView
     * @param postion
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int postion) {
        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (postion <= firstItem) {
            mRecyclerView.scrollToPosition(postion);
        } else if (postion <= lastItem) {
            int top = mRecyclerView.getChildAt(postion - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(postion);
        }
    }

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }


    /**
     * 系统是否开启定位服务
     *
     * @return
     */
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    /**
     * @param bmp     获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public static void saveBmp2Gallery(Bitmap bmp, String picName, Context ctx) {

        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;

        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".png");

            // 获得文件相对路径
            fileName = file.toString();

            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.PNG, 90, outStream);
            }

        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
                bmp, fileName, null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        ctx.sendBroadcast(intent);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("resultCode", 9000);
            jsonObject.put("type", "png");
            jsonObject.put("resultStr", "success");
            jsonObject.put("path", file.getAbsolutePath());
//            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject);
//            pluginResult.setKeepCallback(true);
//            APPConstans.mcallBack.sendPluginResult(pluginResult);

            Log.w("amtf", "文件路径：" + jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static int ifInviteNoAccept(RetListBean item) {

        List<RetListBean.InvitesBean> data = item.getInvites();

        if (data != null && data.size() > 0) {

            for (RetListBean.InvitesBean tt : data) {
                if (tt.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(App.mContext).getUserId())) {

                    if (tt.getF_state().equals("1")) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            }

        }
        return 0;
    }


    /**
     * 是否要显示倒计时
     *
     * @param item
     * @return
     */
    public static boolean isCountReback(RetListBean item) {

        if (MyTextUtil.isEmpty(item.getF_process_id())) {
            return false;
        }

        if (MyTextUtil.isEmpty(item.getF_end_date())) {
            return false;
        }

        List<RetListBean.InvitesBean> finsh = item.getFinishes();

        if (finsh == null || finsh.size() < 1) {
            return false;
        }


        RetListBean.InvitesBean mySelf = getMyself(finsh);

        if (mySelf == null)
            return false;

        if (mySelf.getF_state().equals("0")) {
            return true;
        }


        return false;
    }

    private static RetListBean.InvitesBean getMyself(List<RetListBean.InvitesBean> finsh) {

        for (RetListBean.InvitesBean item : finsh) {
            if (item.getOwnermap().getUser_id().equals(PreferenceUtil.getInstance(App.mContext).getUserId())) {
                return item;
            }
        }

        return null;
    }


    /**
     * 处理请假
     */
    public static void handleLeave(BaseViewHolder helper, RetListBean item) {


        showLeave(helper, item.getInvites());

        helper.setText(R.id.title_content, item.getF_title());
        View view = helper.getView(R.id.comit_cance_ll);
        view.setVisibility(View.GONE);

        View jbSx = helper.getView(R.id.rr_jb);
        jbSx.setVisibility(View.GONE);


        View cc = helper.getView(R.id.ll_Cc);
        cc.setVisibility(View.GONE);
    }


    public static boolean isToday(RetListBean item) {
        String creatDate = item.getF_create_date();
        String today = DateUtil.getCurrentData();
        return creatDate.equals(today);
    }


    /**
     * 处理投票报名相关
     */
    public static void handleVote(BaseViewHolder helper, RetListBean item) {

        String time = item.getF_start_date();

        if (isToday(item)) {
            String text = time.substring(10, time.length());
            helper.setText(R.id.top_time, text.substring(0, text.length() - 3));

        } else {
            helper.setText(R.id.top_time, time.substring(0, 10));
        }

        ImageView imgIcon = helper.getView(R.id.icon);

        if (item.getVoteType() == 1) {

            helper.setVisible(R.id.icon, true);
            imgIcon.setImageResource(R.mipmap.vote_todo);

        } else if (item.getVoteType() == 2) {
            helper.setVisible(R.id.icon, true);
            imgIcon.setImageResource(R.mipmap.join);

        } else if (item.getVoteType() == 3) {
            helper.setVisible(R.id.icon, true);
            imgIcon.setImageResource(R.mipmap.meeting);
        } else {
            helper.getView(R.id.icon).setVisibility(View.GONE);
        }


    }


    private static void showLeave(BaseViewHolder helper, List<RetListBean.InvitesBean> invites) {

        RecyclerView recyclerViewTodo = helper.getView(R.id.join_list);

        LinearLayoutManager hoziLayountManager = new LinearLayoutManager(App.mContext);
        hoziLayountManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTodo.setLayoutManager(hoziLayountManager);//给RecyclerView设置适配器
        TodoInfoAdapter joinAdapter = new TodoInfoAdapter(R.layout.item_info_member, invites);

        recyclerViewTodo.setAdapter(joinAdapter);
    }


}
