package com.xinyu.newdiggtest.ui.calendar;

import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.MyDakaAdapter;

import com.xinyu.newdiggtest.bean.AnswerBean;
import com.xinyu.newdiggtest.bean.AskTopBean;
import com.xinyu.newdiggtest.bean.BaseUser;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.FollowTestBean;

import com.xinyu.newdiggtest.bean.ImImgBean;

import com.xinyu.newdiggtest.bean.MsgNewHomeBean;
import com.xinyu.newdiggtest.bean.RoomPersonListBean;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DateUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.view.SwipeMenuLayout;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.DingdPopupWindow;
import com.xinyu.newdiggtest.widget.MarkedImageView;
import com.xinyu.newdiggtest.widget.PraiseListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends BaseMultiItemQuickAdapter<FollowTestBean, BaseViewHolder> {

    Activity context;

    int ifNoDelet = 0;

    public static final int ITEM_TYPR = 1;//待办
    public static final int ITEM_SX = 2;//我的私信
    public static final int ITEM_FOCUS = 3;//我关注的
    public static final int ITEM_MSG = 4;//我的消息
    public static final int SELECTION_Title = 5;//的标题


    public void setIfNoDelet(int tag) {
        this.ifNoDelet = tag;
    }


    public HomeAdapter(List<FollowTestBean> data, Activity mctx) {
        super(data);
        this.context = mctx;
        addItemType(ITEM_TYPR, R.layout.item_todo);
        addItemType(SELECTION_Title, R.layout.item_ding_head);//标题
        addItemType(ITEM_FOCUS, R.layout.item_daka1);//我的关注
        addItemType(ITEM_MSG, R.layout.item_dingd_msg);//消息
        addItemType(ITEM_SX, R.layout.item_dingd_sx);//私信
    }

    //① 问，大家都可以删掉
    //②在答这一块，如果是被指派，只看到自己,所以不存在问题
    //自己是指派人，应答中会有多个，只能删掉 自己的应答
    @Override
    protected void convert(BaseViewHolder helper, final FollowTestBean item) {
        switch (helper.getItemViewType()) {
            case FollowListBean.ITEM_TYPR:

                int askType = item.getAskType();
                List<ImImgBean> imgList = null;

                SwipeMenuLayout parent = helper.getView(R.id.swipe);

                helper.addOnClickListener(R.id.btn_delete);

                if (askType == 0) {
                    helper.setVisible(R.id.ll_have_common, false);
                    showAsk(0, helper, item);
                    AskTopBean top = item.getAskTopBean();
                    if (top != null) {
                        imgList = top.getF_watch_img();
                    }
                    if (imgList == null || imgList.size() < 1) {
                        helper.setVisible(R.id.nineGrid, false);
                    } else {
                        helper.setVisible(R.id.nineGrid, true);
                        NineGridView netView = helper.getView(R.id.nineGrid);
                        showImgs(netView, getList(imgList));

                    }


                } else if (askType == 1) {

                    helper.setVisible(R.id.ll_have_common, true);

                    showAsk(1, helper, item);
                    AnswerBean anser = item.getAnswerBean();
                    String useId = anser.getUser().getUser_id();

                    if (anser != null) {
                        Object obj = anser.getF_img();
                        try {
                            String imageUrl = new Gson().toJson(obj);
                            JSONArray myJsonArray = new JSONArray(imageUrl);
                            List<ImageInfo> datas = getImgData(myJsonArray);

                            if (datas == null || datas.size() < 1) {
                                helper.setVisible(R.id.nineGrid, false);
                            } else {
                                helper.setVisible(R.id.nineGrid, true);
                                NineGridView netView = helper.getView(R.id.nineGrid);
                                showImgs(netView, datas);
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                    }

                    if (!MyTextUtil.isEmpty(useId) && useId.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                        helper.setVisible(R.id.btn_delete, true);
                        parent.setLeftSwipe(true);
                    } else {
                        helper.setVisible(R.id.btn_delete, false);
                        parent.setLeftSwipe(false);
                    }
                }

                break;

            case SELECTION_Title:
                helper.setText(R.id.id_text, item.getTitleName());
                if (item.getTitleName().equals("消息")) {
                    helper.addOnClickListener(R.id.id_text);
                }
                break;


            case ITEM_FOCUS:
                purchFocusData(helper, item);
                break;

            case ITEM_SX:
                purchSXData(helper, item);

                break;

            case ITEM_MSG:
                purchMsgData(helper, item);
                break;


        }


    }

    private List<ImageInfo> getImgData(JSONArray myJsonArray) {

        List<ImageInfo> infos = new ArrayList<>();
        try {
            if (myJsonArray != null && myJsonArray.length() > 0) {
                int len = myJsonArray.length();
                for (int i = 0; i < len; i++) {
                    ImageInfo inf = new ImageInfo();
                    JSONObject obj = myJsonArray.getJSONObject(i);
                    inf.setBigImageUrl(obj.getString("original"));
                    inf.setThumbnailUrl(obj.getString("thumbnail"));

                    infos.add(inf);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return infos;
    }


    /**
     * 消息
     *
     * @param helper
     */
    private void purchMsgData(BaseViewHolder helper, FollowTestBean data) {

        helper.addOnClickListener(R.id.rl_msg);

        MsgNewHomeBean item = data.getMsgBean();

        MarkedImageView iv = helper.getView(R.id.iv_target_icon);

        //TODO 目前设置为0,不显示红点
        iv.setMessageNumber(0);
        Picasso.with(mContext)
                .load(item.getFrom_head())
                .transform(new CircleCornerForm())
                .error(R.mipmap.icon_cat)
                .into(iv);

        if (!MyTextUtil.isEmpty(item.getFrom_nickname())) {
            helper.setText(R.id.tv_target, item.getFrom_nickname());
        }

        if (!MyTextUtil.isEmpty(item.getCreate_time())) {
            helper.setText(R.id.tv_time, DateUtils.getHourMin(item.getCreate_time()));
        }
        helper.setText(R.id.mycontent, item.getName());

        if (data.getMsgBean().getWish() != null && !MyTextUtil.isEmpty(data.getMsgBean().getWish())) {
            helper.setVisible(R.id.commonts_sub, true);
            helper.setText(R.id.commonts_sub, MyTextUtil.getDecodeStr(data.getMsgBean().getWish()));
        }


    }

    /**
     * 私信
     *
     * @param helper
     */
    private void purchSXData(BaseViewHolder helper, FollowTestBean data) {

        RoomPersonListBean item = data.getSxBean();
        if (item == null)
            return;

        helper.addOnClickListener(R.id.rl_sx).setText(R.id.tv_name, item.getRoom_name());
        MarkedImageView logoview = helper.getView(R.id.iv_target_icon);


        String msgCount = MyTextUtil.isEmpty(item.getDetail().getCount()) ? "0" : item.getDetail().getCount();

        logoview.setMessageNumber(Integer.parseInt(msgCount));

        if (!MyTextUtil.isEmpty(item.getLatstMsg())) {
            helper.setText(R.id.tv_latestmsg, MyTextUtil.getDecodeStr(AppUtils.convertMsg(item.getLatstMsg())));
        }
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.icon_no_download);
        Picasso.with(mContext).load(item.getRoom_head()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(logoview);

        helper.setText(R.id.time, item.getCreate_time().substring(5, 16));

    }

    private void purchFocusData(BaseViewHolder helper, FollowTestBean item) {
        FollowListBean bean = item.getFocusBean();


        if (bean != null) {
            AppUtils appUtil = new AppUtils(context);
            appUtil.bindMyData(helper, bean, listner);
        }
    }

    private void showAsk(int askType, BaseViewHolder helper, final FollowTestBean item) {

        if (askType == 0) {
            if (item.getAskTopBean() == null)
                return;
            //  图片 标题 次级 名字 时间
            helper.setVisible(R.id.iv_icon, true);
            helper.setVisible(R.id.iv_head, false);

            helper.setVisible(R.id.txt, true);


            ImageView img = helper.getView(R.id.iv_icon);
            String isNeed = item.getAskTopBean().getF_state();// 1已打卡 0不需要打卡
            if (isNeed.equals("0")) {
                img.setImageResource(R.mipmap.check_new_no);
                helper.addOnClickListener(R.id.rl_item1);
                img.setTag("0");
            } else if (isNeed.equals("1")) {
                img.setImageResource(R.mipmap.check_new);
                img.setTag("1");
            }
            helper.addOnClickListener(R.id.iv_icon);


            helper.setText(R.id.tv_target_title, MyTextUtil.getDecodeStr(item.getAskTopBean().getF_title()));

            if (!MyTextUtil.isEmpty(item.getAskTopBean().getF_target_name())) {
                helper.setVisible(R.id.tv_sub, true);
                helper.setText(R.id.tv_sub, "#" + MyTextUtil.getDecodeStr(item.getAskTopBean().getF_target_name()) + "#");
            } else {
                helper.setVisible(R.id.tv_sub, false);
            }
            //时间
            helper.setText(R.id.time, item.getAskTopBean().getF_reminder_time());

            SwipeMenuLayout parent = helper.getView(R.id.swipe);
            BaseUser user = item.getAskTopBean().getFrom_user();
            if (user == null) {
                helper.setVisible(R.id.btn_delete, true);
                parent.setLeftSwipe(true);
            } else {
                helper.setVisible(R.id.btn_delete, false);
                parent.setLeftSwipe(false);
            }


        } else if (askType == 1) {
            // 心情  时间 评论
            helper.setVisible(R.id.txt, false);
            helper.setVisible(R.id.iv_icon, false);
            helper.setVisible(R.id.iv_head, true);


            helper.setText(R.id.tv_target_title, item.getAnswerBean().getUser().getNickname());
            //时间
            helper.setText(R.id.time, DateUtil.timeOnlyHourMin(item.getAnswerBean().getF_finish_time()));

            if (!MyTextUtil.isEmpty(item.getAnswerBean().getF_comment())) {
                helper.setText(R.id.commont, MyTextUtil.getDecodeStr(item.getAnswerBean().getF_comment()));
            }


            /**
             *评论和点赞,打赏
             */

            boolean noLikes = (item.getAnswerBean().getTargetlikes() == null || item.getAnswerBean().getTargetlikes().size() == 0);
            boolean noComment = (item.getAnswerBean().getTargetcomment() == null || item.getAnswerBean().getTargetcomment().size() == 0);

            boolean noDashangMoney = (item.getAnswerBean().getExcitation() == null || item.getAnswerBean().getExcitation().size() == 0);

            String type = "0";
            List<DakaBottowItem> data = item.getAnswerBean().getTargetlikes();
            if (data != null && data.size() > 0) {
                for (DakaBottowItem kk : data) {
                    if (kk.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                        type = "1";
                        break;
                    }
                }
            }
            final ImageView view = helper.getView(R.id.iv_common_more);

            boolean isShow;

            String userId = item.getAnswerBean().getUser().getUser_id();

            if (!MyTextUtil.isEmpty(userId) && !userId.equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                isShow = true;
            } else {
                isShow = false;
            }

            final DingdPopupWindow pop = new DingdPopupWindow(mContext, type, isShow);
            pop.update();


            pop.setmItemClickListener(new DingdPopupWindow.OnItemClickListener() {
                @Override
                public void onItemClick(String content) {
                    if (content.equals("赞") || content.equals("取消")) {
                        if (listner != null) {
                            listner.onZanClick(convertBean(item), content);
                        }
                    } else if (content.equals("评论")) {
                        if (listner != null) {
                            listner.onCommentClick(convertBean(item), view);
                        }
                    } else if (content.equals("打赏")) {
                        if (listner != null) {
                            listner.onDaShangClick(convertBean(item));
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

                CommentListView convertView = helper.getView(R.id.commentList);


                DakaDashangListView dakaDashang = helper.getView(R.id.dashanglistview);

                PraiseListView praiseView = helper.getView(R.id.praiseListView);


                if (noLikes) {
                    praiseView.setVisibility(View.GONE);
                } else {
                    praiseView.setVisibility(View.VISIBLE);
                    praiseView.setDatas(AppUtils.convertPaise(item.getAnswerBean().getTargetlikes()));
                }

                if (noDashangMoney) {
                    dakaDashang.setVisibility(View.GONE);
                } else {
                    dakaDashang.setVisibility(View.VISIBLE);
                    dakaDashang.setDatas(item.getAnswerBean().getExcitation());
                }

                if (noComment) {
                    convertView.setVisibility(View.GONE);
                } else {
                    convertView.setVisibility(View.VISIBLE);
                    convertView.setDatas(AppUtils.conVertCommont(item.getAnswerBean().getTargetcomment(), "1"));
                }

            }


        }


        if (askType == 0) {

            List<BaseUser> names = item.getAskTopBean().getExecutorlist();
            BaseUser fromUser = item.getAskTopBean().getFrom_user();

            if (fromUser != null && !MyTextUtil.isEmpty(fromUser.getUser_id())) {
                helper.setText(R.id.txt, "from " + fromUser.getNickname());
            } else {
                if (names != null && names.size() > 0) {
                    if (names.size() == 1 && getNames(names).equals(PreferenceUtil.getInstance(mContext).getNickName())) {
                        helper.setVisible(R.id.txt, false);
                    } else {
                        helper.setVisible(R.id.txt, true);
                        helper.setText(R.id.txt, "@ " + getNames(names));
                    }

                }
            }


            int isExcutor = 0;//自己是否是执行人
            for (BaseUser exctutor : names) {
                if (exctutor.getUser_id().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    isExcutor = 1;
                    break;
                }
            }

            if (isExcutor == 1) {
                helper.setVisible(R.id.iv_icon, true);
            } else {
                helper.setVisible(R.id.iv_icon, false);
            }


        } else if (askType == 1) {

            helper.setText(R.id.txt, "by");

            ImageView head = helper.getView(R.id.iv_head);

            String headUrl = item.getAnswerBean().getUser().getHead();

            String userNick = item.getAnswerBean().getUser().getNickname();

            if (!MyTextUtil.isEmpty(headUrl)) {
                Picasso.with(mContext).load(headUrl).transform(new CircleCornerForm()).
                        error(R.drawable.icon_no_download).into(head);
            }
            ;

            if (!MyTextUtil.isEmpty(item.getAnswerBean().getF_target_name())) {
                helper.setVisible(R.id.tv_sub, true);
                helper.setText(R.id.tv_sub, "#" + MyTextUtil.getDecodeStr(item.getAnswerBean().getF_target_name()) + "#");
            } else {
                helper.setVisible(R.id.tv_sub, false);
            }
        }


        SwipeMenuLayout parent = helper.getView(R.id.swipe);

        if (ifNoDelet == -1) {//不让滑动
            Log.e("amtf", "不让滑动");
            helper.setVisible(R.id.btn_delete, false);
            parent.setLeftSwipe(false);
        }

    }

    private FollowListBean convertBean(FollowTestBean item) {

        if (item.getAnswerBean() == null || MyTextUtil.isEmpty(item.getAnswerBean().getF_answer_plan_id()))
            return null;

        FollowListBean bean = new FollowListBean();

        bean.setAnswerId(item.getAnswerBean().getF_answer_plan_id());
        bean.setF_plan_id(item.getAnswerBean().getF_answer_plan_id());
        bean.setF_target_name(item.getAnswerBean().getF_target_name());
        bean.setF_executor(item.getAnswerBean().getUser().getUser_id());
        bean.setTargetlikes(item.getAnswerBean().getTargetlikes());


        return bean;
    }

    private String getNames(List<BaseUser> users) {
        StringBuffer buffer = new StringBuffer();
        for (BaseUser item : users) {
            buffer.append(item.getNickname()).append(",");
        }
        String memebers = buffer.toString();
        String userss = memebers.substring(0, memebers.length() - 1);
        return userss;
    }


    private List<ImageInfo> getList(List<ImImgBean> imgList) {
        List<ImageInfo> datas = new ArrayList<>();
        for (ImImgBean item : imgList) {
            ImageInfo data = new ImageInfo();
            data.setBigImageUrl(item.getOriginal());
            data.setThumbnailUrl(item.getThumbnail());
            datas.add(data);
        }
        return datas;
    }

    private void showImgs(NineGridView netView, List<ImageInfo> imgList) {
        NineGridViewClickAdapter adapter = new NineGridViewClickAdapter(mContext, imgList);
        netView.setAdapter(adapter);
        netView.setSingleImageSize(AppUtils.dp2px(mContext, 150));
    }

//    PopClick popListner;

    MyDakaAdapter.onPopClick listner;


//    public void setPopListner(PopClick listner) {
//        this.popListner = listner;
//    }

    public void setDakaPopListner(MyDakaAdapter.onPopClick mlistner) {

        this.listner = mlistner;
    }


//    public interface PopClick {
//
//        void onZanClick(FollowTestBean item, String content);
//
//
//        void onCommentClick(FollowTestBean item, View view);
//
//        void onDashagn(FollowTestBean item);
//
//    }


}
