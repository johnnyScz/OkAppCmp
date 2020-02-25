package com.xinyu.newdiggtest.ui.calendar;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

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
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.FollowTestBean;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.DingdPopupWindow;
import com.xinyu.newdiggtest.widget.PraiseListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoDelectAdapter extends BaseMultiItemQuickAdapter<FollowTestBean, BaseViewHolder> {

    Activity context;

    public static final int ITEM_TYPR = 1;//待办


    public NoDelectAdapter(List<FollowTestBean> data, Activity mctx) {
        super(data);
        this.context = mctx;
        addItemType(ITEM_TYPR, R.layout.item_todo_nodelet);

    }


    @Override
    protected void convert(BaseViewHolder helper, final FollowTestBean item) {
        switch (helper.getItemViewType()) {
            case FollowListBean.ITEM_TYPR:
                int askType = item.getAskType();
                List<ImImgBean> imgList = null;

                if (askType == 1) {
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
                }

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


    private void showAsk(int askType, BaseViewHolder helper, final FollowTestBean item) {

        if (askType == 1) {
            // 心情  时间 评论
            helper.setVisible(R.id.iv_common_more, true);


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
                        if (popListner != null) {
                            popListner.onZanClick(item, content);
                        }
                    } else if (content.equals("评论")) {
                        if (popListner != null) {
                            popListner.onCommentClick(item, view);
                        }
                    } else if (content.equals("打赏")) {
                        if (popListner != null) {
                            popListner.onDashagn(item);
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

        if (askType == 1) {

            //时间
            helper.setText(R.id.time, DateUtil.timeOnlyHourMin(item.getAnswerBean().getF_finish_time()));

            if (!MyTextUtil.isEmpty(item.getAnswerBean().getF_comment())) {
                helper.setText(R.id.txt, MyTextUtil.getDecodeStr(item.getAnswerBean().getF_comment()));
            }


            ImageView head = helper.getView(R.id.head);

            String headUrl = item.getAnswerBean().getUser().getHead();

            String userNick = item.getAnswerBean().getUser().getNickname();
            if (!MyTextUtil.isEmpty(userNick)) {
                helper.setText(R.id.tv_target_title, userNick);
            }

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


    }


//    private List<ImageInfo> getList(List<ImImgBean> imgList) {
//        List<ImageInfo> datas = new ArrayList<>();
//        for (ImImgBean item : imgList) {
//            ImageInfo data = new ImageInfo();
//            data.setBigImageUrl(item.getOriginal());
//            data.setThumbnailUrl(item.getThumbnail());
//            datas.add(data);
//        }
//        return datas;
//    }

    private void showImgs(NineGridView netView, List<ImageInfo> imgList) {
        NineGridViewClickAdapter adapter = new NineGridViewClickAdapter(mContext, imgList);
        netView.setAdapter(adapter);
        netView.setSingleImageSize(AppUtils.dp2px(mContext, 150));
    }

    PopClick popListner;

    MyDakaAdapter.onPopClick listner;


    public void setPopListner(PopClick listner) {
        this.popListner = listner;
    }

    public void setDakaPopListner(MyDakaAdapter.onPopClick mlistner) {

        this.listner = mlistner;
    }


    public interface PopClick {

        void onZanClick(FollowTestBean item, String content);


        void onCommentClick(FollowTestBean item, View view);

        void onDashagn(FollowTestBean item);

    }

}
