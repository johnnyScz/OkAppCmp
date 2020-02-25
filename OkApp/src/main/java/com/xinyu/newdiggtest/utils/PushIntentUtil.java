package com.xinyu.newdiggtest.utils;

import android.content.Intent;

import com.dommy.qrcode.util.Constant;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.Digg.HomeDiggActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.MsgTabActivity;

import com.xinyu.newdiggtest.ui.TargetNewInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息跳转工具类
 */
public class PushIntentUtil {

    public static PushIntentUtil instance;

    private static final Object lockObj = new Object();

    private PushIntentUtil() {
    }

    public static PushIntentUtil getInstance() {
        if (instance == null) {
            synchronized (lockObj) {
                if (instance == null) {
                    instance = new PushIntentUtil();
                }
            }
        }
        return instance;
    }


    public void parchMsg(String costom) {

        try {
            JSONObject object = new JSONObject(costom);
            JSONObject child = object.getJSONObject("aps").getJSONObject("alert");

            PushBean bean = new PushBean();
            bean.setUrltype(child.getString("urltype"));
            if (costom.contains("relevantid"))
                bean.setRelevantid(child.getString("relevantid"));//自己的userid
            if (costom.contains("objectid"))
                bean.setObjectid(child.getString("objectid"));//目标的id
            goIntent(bean);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void goIntent(PushBean bean) {
        Constant.Push_Switch = 0;
        String type = bean.getUrltype();
        switch (type) {

            case "1"://打卡定时提醒
                Intent intent1 = new Intent(App.mContext, HomeDiggActivity.class);
                App.mContext.startActivity(intent1);
                break;

            case "2"://目标
                goTargetDetail(bean);
                break;


            case "3"://打卡
                Intent intent = new Intent(App.mContext, HomeDiggActivity.class);
                App.mContext.startActivity(intent);
                break;

            case "4"://个人被关注
                goFocus(bean);
                break;

            case "5"://群消息
                goCharRoom(bean);
                break;

            case "6"://私信列表
                goPersonChat(bean);
                break;


        }
    }

    /**
     * 跳私信列表
     *
     * @param bean
     */
    private void goPersonChat(PushBean bean) {

        Intent intent = new Intent(App.mContext, HomeDiggActivity.class);
        Constant.Push_Switch = 2;
        App.mContext.startActivity(intent);


    }

    /**
     * 群消息
     * <p>
     * 调到群组列表
     *
     * @param bean
     */
    private void goCharRoom(PushBean bean) {

        Intent intent = new Intent(App.mContext, HomeDiggActivity.class);
        Constant.Push_Switch = 1;
        App.mContext.startActivity(intent);


    }

    /**
     * 被关注 跳消息列表
     *
     * @param bean
     */
    private void goFocus(PushBean bean) {
        Intent intent = new Intent(App.mContext, MsgTabActivity.class);
        App.mContext.startActivity(intent);

    }

    /**
     * 去系统详情
     */
    private void goTargetDetail(PushBean bean) {
        Intent intent = new Intent(App.mContext, TargetNewInfoActivity.class);
        intent.putExtra(IntentParams.DAKA_Target_Id, bean.getObjectid());
        App.mContext.startActivity(intent);
    }

    /**
     * 调到个人空间
     *
     * @param bean
     */
    private void goDakaSpace(PushBean bean) {
//        Intent intent = new Intent(App.mContext, MySpaceActivity.class);
//        intent.putExtra(IntentParams.USER_ID, bean.getRelevantid());
//        App.mContext.startActivity(intent);

    }


    static class PushBean {

        private String urltype;
        private String relevantid;
        private String objectid;

        public String getUrltype() {
            return urltype;
        }

        public void setUrltype(String urltype) {
            this.urltype = urltype;
        }

        public String getRelevantid() {
            return relevantid;
        }

        public void setRelevantid(String relevantid) {
            this.relevantid = relevantid;
        }

        public String getObjectid() {
            return objectid;
        }

        public void setObjectid(String objectid) {
            this.objectid = objectid;
        }


    }


}
