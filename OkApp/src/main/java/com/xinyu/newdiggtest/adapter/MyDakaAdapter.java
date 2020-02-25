package com.xinyu.newdiggtest.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.NineImgBean;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.utils.AppUtils;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.view.CommentItem;
import com.xinyu.newdiggtest.view.CommentListView;
import com.xinyu.newdiggtest.widget.CommentPopupWindow;
import com.xinyu.newdiggtest.widget.DakaDashangListView;
import com.xinyu.newdiggtest.widget.PraiseListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MyDakaAdapter extends BaseMultiItemQuickAdapter<FollowListBean, BaseViewHolder> {

    Activity context;

    String dakaDate;

    public void setDakaDate(String mDate) {
        this.dakaDate = mDate;
    }


    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MyDakaAdapter(List<FollowListBean> data, Activity mctx) {
        super(data);
        this.context = mctx;
        addItemType(FollowListBean.ITEM_TYPR, R.layout.item_daka);
        addItemType(FollowListBean.SELECTION_TYPR, R.layout.item_daka_selection);
        //TODO 后期优化
        addItemType(FollowListBean.ITEM_TYPR_Other, R.layout.item_daka1);
        ;
    }

    @Override
    protected void convert(BaseViewHolder helper, final FollowListBean item) {
        switch (helper.getItemViewType()) {
            case FollowListBean.ITEM_TYPR:
                helper.addOnClickListener(R.id.rl_item1);
                helper.addOnClickListener(R.id.tv_daka_title);

                incoSwich(helper, item);

                String targetName = TextUtils.isEmpty(item.getF_target_name()) ? "今日打卡" : item.getF_target_name();
                if (targetName.equals("今日打卡")) {
                    helper.setText(R.id.tv_daka_title, targetName);
                } else {
                    helper.setText(R.id.tv_daka_title, "#" + targetName + " " + AppUtils.getShowTime(item) + "#");
                }
                String content = item.getF_comment();

                if (!MyTextUtil.isEmpty(content)) {
                    String showContent = null;
                    try {
                        showContent = URLDecoder.decode(content, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    helper.setText(R.id.mycontent, showContent);
                }


                try {
                    String dakaContent = MyTextUtil.isEmpty(item.getF_content()) ? "" : item.getF_content();
                    helper.setText(R.id.tv_descrep, URLDecoder.decode(dakaContent, "UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!noToday()) {//只有今天能被打卡
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

                if (TextUtils.isEmpty(item.getF_finish_time()) || item.getF_finish_time() == null) {
                    helper.setVisible(R.id.time_tv, false);
                } else {
                    helper.setVisible(R.id.time_tv, true);
                    String time = item.getF_finish_time();
                    String nTime = time.substring(time.length() - 8, time.length() - 3);
                    helper.setText(R.id.time_tv, nTime);
                }


                /**
                 *评论和点赞
                 */

                boolean noLikes = (item.getTargetlikes() == null || item.getTargetlikes().size() == 0);
                boolean noComment = (item.getTargetcomment() == null || item.getTargetcomment().size() == 0);
                boolean noDashangMoney = (item.getExcitation() == null || item.getExcitation().size() == 0);

                String type = "";
                boolean show;

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
                if (item.getF_toUser().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                    show = false;
                } else {
                    show = true;
                }

                final ImageView view = helper.getView(R.id.iv_common_more);
                final CommentPopupWindow pop = new CommentPopupWindow(mContext, type, show);
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
                break;

            case FollowListBean.SELECTION_TYPR:
                ImageView icon = helper.getView(R.id.iv_head);
                icon.setImageResource(R.drawable.icon_xinagguan);
                helper.setText(R.id.tv_name, "关注的人");
                break;

            case FollowListBean.ITEM_TYPR_Other:
                bindMyData(helper, item);
                break;

        }


    }

    private void bindMyData(BaseViewHolder helper, final FollowListBean item) {
        incoSwich(helper, item);
        helper.addOnClickListener(R.id.iv_target_icon);
        helper.addOnClickListener(R.id.rl_item1);
        helper.addOnClickListener(R.id.tv_descrep);
        ImageView icon = helper.getView(R.id.iv_target_icon);

        Picasso.with(mContext).load(item.getImgUrl()).error(R.drawable.icon_no_download).
                transform(new CircleCornerForm()).into(icon);


        helper.setText(R.id.tv_daka_title, item.getName());
        String targetName = TextUtils.isEmpty(item.getF_target_name()) ? "今日打卡" : item.getF_target_name();
        helper.setText(R.id.tv_descrep, "#" + targetName + " " + AppUtils.getShowTime(item) + "#");

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

        if (TextUtils.isEmpty(item.getF_img())) {
            helper.setVisible(R.id.nineGrid, false);
        } else {
            helper.setVisible(R.id.nineGrid, true);
            NineGridView netView = helper.getView(R.id.nineGrid);

            String temp = item.getF_img();
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


        if (TextUtils.isEmpty(item.getF_finish_time()) || item.getF_finish_time() == null) {
            helper.setVisible(R.id.time_tv, false);
        } else {
            helper.setVisible(R.id.time_tv, true);
            String time = item.getF_finish_time();
            String nTime = time.substring(time.length() - 8, time.length() - 3);
            helper.setText(R.id.time_tv, nTime);

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
        final ImageView view = helper.getView(R.id.iv_common_more);
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
                        popListner.onCommentClick(item, view);
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

    private void incoSwich(BaseViewHolder helper, FollowListBean item) {
        ImageView icon = helper.getView(R.id.iv_target_icon);
        if (!MyTextUtil.isEmpty(item.getF_class_id())) {
            int drawableId = mContext.getResources().getIdentifier(item.getF_class_id(), "mipmap", mContext.getPackageName());
            icon.setImageResource(drawableId);
        }
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

    private void showPop(View v, CommentPopupWindow pop) {
        pop.showPopupWindow(v);
    }


    /**
     * 打卡时间不是当天
     *
     * @return
     */
    private boolean noToday() {

        try {
            long selectTime = DateUtil.stringToDate(dakaDate, "yyyy-MM-dd");

            long date = DateUtil.getTodayLong();
            if (selectTime != date) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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


    onPopClick popListner;


    public void setPopListner(onPopClick listner) {
        this.popListner = listner;
    }

    public interface onPopClick {

        void onZanClick(FollowListBean item, String content);

        void onDaShangClick(FollowListBean item);

        void onCommentClick(FollowListBean item, View view);

    }

}
