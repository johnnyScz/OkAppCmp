package com.xinyu.newdiggtest.net;


public class ApiConfig {


    public static String BDUrl = "";

    public static String BASE_URL = "";

    public static boolean PrintLog = true;

    public static boolean isOnlineUrl = false;

    public static String WXAPP_ID = "";
    public static String WXAPP_APPSecret = "";

    public static String SOCKET_IP = "";

    public static String WxLittle = "";

    public static String OK_ShareUrl = "";

    //TODO 如果推送没有成功，检查IP地址是不是没有配置

    public static String Umeng_AppKey = "";
    public static String Umeng_Message_Secret = "";

    public static String UPLoad_Url = "";

    public static String Wx_LoginUrl = "";


    public static String NaoTuUrl = "";


    static {
        if (isOnlineUrl) {

            BDUrl = "https://ok2e.xinyusoft.com/api2e/bd/login.html#/shareForm?";

            //TODO 后面要改过来
            PrintLog = false;

            BASE_URL = "http://118.178.98.223:8880/";

            UPLoad_Url = "http://47.97.65.69:8080/";

            SOCKET_IP = "http://47.98.20.182:8292/message";

            OK_ShareUrl = "http://ok2e.xinyusoft.com/";
            WxLittle = "gh_f71f408e1f30";

            Umeng_AppKey = "5c049fd0f1f556fffc000082";
            Umeng_Message_Secret = "a30d6e09be9752b90a0a6dd4403e358e";


            WXAPP_ID = "wxfded65b5017d01ae";
            WXAPP_APPSecret = "c46a7a5f82b30f287a117d5665ac3793";


            Wx_LoginUrl = "http://47.97.65.69:8080/";

            NaoTuUrl = "http://ok2e.xinyusoft.com/weShare/#/pages/enjoy/mind";


        } else {

            BDUrl = "https://ok2etest.xinyusoft.com/api2e/bd/login.html#/shareForm?";

            PrintLog = true;

            UPLoad_Url = "http://121.40.177.191:8080/";

            BASE_URL = "http://121.40.177.191:8880/";

            SOCKET_IP = "http://121.40.129.195:8292/message";
            OK_ShareUrl = "http://ok2etest.xinyusoft.com/";
            WxLittle = "gh_00a0eac12783";

            Umeng_AppKey = "5c248453b465f54dc7000012";
            Umeng_Message_Secret = "26846c9d18a42057ee4702b73c8d5c92";

            WXAPP_ID = "wx60a1985fc1cc46e9";
            WXAPP_APPSecret = "310ddff7499ded5ed92aa542a7c26bd7";

            Wx_LoginUrl = "http://121.40.177.191:8080/";

            NaoTuUrl = "http://ok2etest.xinyusoft.com/weShare/#/pages/enjoy/mind";
        }
    }


}
