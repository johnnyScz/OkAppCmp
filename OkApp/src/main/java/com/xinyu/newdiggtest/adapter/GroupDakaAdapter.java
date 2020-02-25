package com.xinyu.newdiggtest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.PraiseListView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class GroupDakaAdapter extends BaseQuickAdapter<FollowListBean, BaseViewHolder> {

    Activity context;

    String dakaDate;

    public void setDakaDate(String mDate) {
        this.dakaDate = mDate;
    }

    public GroupDakaAdapter(int layoutResId, @Nullable List<FollowListBean> data, Activity ctx) {
        super(layoutResId, data);
        this.context = ctx;

    }


    @Override
    protected void convert(BaseViewHolder helper, final FollowListBean item) {

        String targetName = TextUtils.isEmpty(item.getF_target_name()) ? "今日打卡" : item.getF_target_name();

        if (targetName.equals("今日打卡")) {
            helper.setText(R.id.tv_daka_title, targetName);
        } else {
            helper.setText(R.id.tv_daka_title, "#" + targetName + " " + getShowTime(item) + "#");
        }

        if (!TextUtils.isEmpty(item.getHead())) {
            ImageView hean = helper.getView(R.id.iv_target_icon);
            Glide.with(mContext).load(item.getHead()).into(hean);
        }
        try {
            String content = URLDecoder.decode(item.getF_content(), "UTF-8");
            String showContent = TextUtils.isEmpty(content) ? "你今天还没有打卡哦！" : content;
            helper.setText(R.id.tv_descrep, showContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!getDateAfer()) {
            helper.getView(R.id.img_check).setEnabled(true);
            if (item.getF_state().equals("0")) {
                helper.setImageResource(R.id.img_check, R.drawable.icon_daka_nocheck);
                helper.addOnClickListener(R.id.img_check);
            } else {
                helper.setImageResource(R.id.img_check, R.drawable.daka_check);
            }
        } else {
            helper.getView(R.id.img_check).setEnabled(false);
            helper.setImageResource(R.id.img_check, R.color.mall_colorFA);
        }

        helper.setVisible(R.id.commont, true);

        if (TextUtils.isEmpty(item.getF_img())) {
            helper.setVisible(R.id.nineGrid, false);
        } else {
            helper.setVisible(R.id.nineGrid, true);
            NineGridView netView = helper.getView(R.id.nineGrid);
            String temp = item.getF_img();
            String StringTrim = temp.substring(0, temp.length() - 1);
            final String[] imgs = StringTrim.split(",");

            showImgs(netView, imgs);

        }


        /**
         *评论和点赞
         */

        boolean noLikes = (item.getTargetlikes() == null || item.getTargetlikes().size() == 0);
        boolean noComment = (item.getTargetcomment() == null || item.getTargetcomment().size() == 0);
        boolean noDashangMoney = (item.getExcitation() == null || item.getExcitation().size() == 0);


        String type = "";
        if (item.getTargetlikes() != null && item.getTargetlikes().size() > 0) {
            List<DakaBottowItem> data = item.getTargetlikes();
            for (DakaBottowItem kk : data) {
                if (kk.getF_like_user().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    type = "1";
                    break;
                } else {
                    type = "0";
                }
            }

        } else {
            type = "0";
        }
        final CommentPopupWindow pop = new CommentPopupWindow(mContext, type, true);
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
                        popListner.onCommentClick(item);
                    }
                }
            }
        });


        helper.getView(R.id.iv_common_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(v, pop);
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
            convertView.setDatas(conVertData(item.getTargetcomment(), "1"));
        } else {
            convertView.setVisibility(View.GONE);
        }

    }

    private String getShowTime(FollowListBean item) {


        String start = item.getF_start_date();
        String end = item.getF_end_date();
        if (MyTextUtil.isEmpty(start) || MyTextUtil.isEmpty(end)) {
            return "";
        }

        String relax = start.substring(4, 5);

        String nStart = start.replaceAll(relax, ".");
        String nEnd = end.replaceAll(relax, ".");
        return nStart.substring(5, start.length()) + "-" + nEnd.substring(5, start.length());
    }

    private List<FavortItem> convertPaise(List<DakaBottowItem> likes) {
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

    private List<CommentItem> conVertData(List<DakaBottowItem> targetcomment, String type) {
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

    /**
     * 评论
     *
     * @param item
     */
    private void requestCommon(FollowListBean item) {


    }


    private void showPop(View v, CommentPopupWindow pop) {
        pop.showPopupWindow(v);
    }


    /**
     * 打卡时间是否比当前大
     *
     * @return
     */
    private boolean getDateAfer() {

        try {
            long selectTime = DateUtil.stringToDate(dakaDate, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            long date = calendar.getTime().getTime();
            if (selectTime - date > 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showImgs(NineGridView netView, String[] imgs) {
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<String> imageDetails = Arrays.asList(imgs);
        if (imageDetails != null) {
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageDetail);
                info.setBigImageUrl(imageDetail);
                imageInfo.add(info);
            }
        }
        netView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        //单张图大小
        netView.setSingleImageSize(dp2px(context, 150));
    }


    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    onPopClick popListner;


    public void setPopListner(onPopClick listner) {
        this.popListner = listner;
    }

    public interface onPopClick {
        void onZanClick(FollowListBean item, String content);

        void onDaShangClick(FollowListBean item);

        void onCommentClick(FollowListBean item);

    }

}
