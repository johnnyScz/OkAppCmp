package com.xshell.xshelllib.application;

public class AppConstants {
    public static final String TAG_INDEX = "index";// 引导页参数

    /**
     * 缓存目录
     */
    public static final String XINYUSOFT_CACHE = "xshell";

    /**
     * 下载的临时名字
     */
    public static final String APP_APK_NAME = "xshell.apk";

    /**
     * 初始html5更新时间 当app上传的时候，你应该需要看看file的最新上传版本，应该要比它多点时间。
     * 比如，file的最新上传版本为201507201008 你应该写成201507201009，这样 就避免下载 201510081050
     */
    // public static final String FILE_UPDATE_START_TIME = "201510082350";

    /**
     * 初始app的更新时间，应该与最新的当地时间同步，并且，你需要多加一点时间，来弥补发布的时间。
     * 如：在本地版本(即下面的数值)为201507201106，那么，你应该在这个时间之前上传（xversion）你的版本，但是不宜过多。
     * 最好控制在201507201101~201507201104 （在xversion上传的成功的时间）之间上传成功，这样避免重复下载app
     */
    // public static final String APP_UPDATE_START_TIME = "201510101330";//
    // 初始app应用更新时间

    /**
     * 下载更新文件失败最大重试次数（非网络原因）
     */

    // app的log存放地址
    public static final String APP_ROOT_DIR_LOG = "xshell/Log/";
    // 开启一个新的Activity
    public static final String ACTION_NEW_BROSER = "ACTION_NEW_BROSER";
    // 关闭一个新的Activity
    public static final String ACTION_CLOSE_BROSER = "ACTION_CLOSE_BROSER";

    public static final String APP_ROOT_DIR = "xshell"; // app应用文件SD卡主目录
    // 播放音乐
    public static final String ACTION_WEBVIEW_PLAY_SOUND = "com.vriteam.android.ke.ACTION_WEBVIEW_PLAY_SOUND";
    // 录音begin
    public static final String RECORD = "com.xinyusoft.ACTION_RECORD";
    /**
     * 录音上传地址
     */
    public static final String RECORD_UPLODING_URL = "http://58.56.93.137:8081/qft/uploadVoice";
    public static final String APP_AUDIO_DIR = "audio";// 录音保存文件夹

    // 信息提示
    public static final String ACTION_REMINDER = "ACTION_REMINDER";

    // 跳转新的activity
//	public static final String XLOG_URL = "http://test.xinyusoft.com:8080/uuFinancialPlanner/ESBServlet?command=xlog.AddLogAction";
    public static final String XLOG_URL = "http://xlog.xinyusoft.com/uuFinancialPlanner/ESBServlet?command=xlog.AddLogAction";
    //上传用户行为数据
//	public static final String XMessage_URL = "http://58.247.47.154:85/uuFinancialPlanner/ESBServlet?command=cms.p_UserBehaviorRecord";
//	public static final String XMessage_URL = "http://uat.xinyusoft.com:90/cctvgtb/ESBServlet?command=cms.p_UserBehaviorRecord";
    public static final String XMessage_URL = "http://uat.xinyusoft.com:90/cctvgtb/ESBServlet?command=user.p_UserBehaviorRecord";
    //用户登入与登出情况
    public static final String APP_LOGIN_URL = "http://uat.xinyusoft.com:90/cctvgtb/ESBServlet?command=user.LoginTurnover";

    // 关闭当前activity
    public static final String CLOSE_THIS = "CLOSE_THIS";

    // 关闭一个新的Activity
    public static final String ACTION_PUSH_MSG = "UMengPush";
    // webSocket!! 发送给服务
    public static final String WSC_SEND_SERVICE = "WSC_SEND_SERVICE";
    // 发送给webSocket里面的插件 接收消息
    public static final String WSC_SEND_PLUGIN = "WSC_SEND_PLUGIN";

    // 发送给webSocket里面的插件,websocket链接成功
    public static final String WSC_SEND_PLUGIN_LOAGIN_SUCCESS = "WSC_SEND_PLUGIN_LOAGIN_SUCCESS";
    // 发送给webSocket里面的插件,websocket链接失败
    public static final String WSC_SEND_PLUGIN_LOAGIN_FAILUER = "WSC_SEND_PLUGIN_LOAGIN_FAILUER";
    // 刷新首页
    public static final String RELOAD_HOME_PAGE = "RELOAD_HOME_PAGE";
    // 刷新新开的NewBrowser页面
    public static final String RELOAD_NEW_BROWSER_PAGE = "RELOAD_NEW_BROWSER_PAGE";

    public static final String ACTION_UPLOAD = "com.vriteam.android.ke.ACTION_UPLOAD";

    // /** 测试环境 */
    // public static final String ENV = "TEST";
    /**
     * 生产环境
     */
    public static final String ENV = "PRODUCT";
    // 银联End
    /**
     * chat message Notification ID
     */
    public static int NOTIFICATION_ID = 1;

    /**
     * 引导页跳转
     */
    public static final String GUIDE_BROADCAST_NAME = "guide_broadcast_name";// 引导页面guideactivity的广播接收者

    // progress changed
    public static final int MESSAGE_PROGRESS_CHANGED = 200;
    public static final int MESSAGE_STEPPING_PROGRESS = 201;

    // 手势解锁的标识
    public static boolean IS_SUCCESS = false;

    // 忘记手势密码的标识
    public static boolean FORGET_LOCK = false;
}