package com.xinyu.newdiggtest.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.DakaBottowItem;
import com.xinyu.newdiggtest.bean.FollowListBean;
import com.xinyu.newdiggtest.bean.NineImgBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.circle.FavortItem;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
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
import java.util.ArrayList;
import java.util.List;

public class CommonDakaAdapter extends BaseQuickAdapter<FollowListBean, BaseViewHolder> {


    Activity context;

    String adapterType = "";


    public CommonDakaAdapter(int layoutResId, @Nullable List<FollowListBean> data, Activity ctx) {
        super(layoutResId, data);
        this.context = ctx;

    }


    @Override
    protected void convert(BaseViewHolder helper, final FollowListBean item) {

        helper.addOnClickListener(R.id.tv_daka_title);
        helper.addOnClickListener(R.id.iv_target_icon);
        int pos = helper.getAdapterPosition();
        item.setEditPos(pos);
        helper.setText(R.id.tv_daka_title, item.getNick_name());
        String targetName = TextUtils.isEmpty(item.getF_target_name()) ? item.getF_title() : item.getF_target_name();
        if (!TextUtils.isEmpty(item.getHead())) {
            ImageView hean = helper.getView(R.id.iv_target_icon);

            Picasso.with(mContext).load(item.getHead()).error(R.drawable.icon_no_download).
                    transform(new CircleCornerForm()).into(hean);

        }

        if (MyTextUtil.isEmpty(item.getF_target_name())) {
            helper.setText(R.id.tv_descrep, MyTextUtil.getDecodeStr(targetName));
        } else {
            helper.setText(R.id.tv_descrep, "#" + MyTextUtil.getDecodeStr(targetName) + " " + getShowTime(item) + "#");
        }
        try {
            String content = URLDecoder.decode(item.getF_comment(), "UTF-8");
            helper.setText(R.id.mycontent, content);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (adapterType.equals("daka_info")) {
            helper.setVisible(R.id.img_check, false);
        } else {
            if (item.getF_executor().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
                helper.getView(R.id.img_check).setEnabled(true);
                if (item.getF_state().equals("0")) {
                    helper.setImageResource(R.id.img_check, R.drawable.icon_daka_nocheck);
                    helper.addOnClickListener(R.id.img_check);
                } else {
                    helper.setImageResource(R.id.img_check, R.drawable.daka_check);
                }

            }

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

        if (item.getTargetlikes() != null && item.getTargetlikes().size() > 0) {
            List<DakaBottowItem> dakaitem = item.getTargetlikes();
            for (DakaBottowItem kk : dakaitem) {
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
        boolean isShow;
        if (item.getF_executor().equals(PreferenceUtil.getInstance(mContext).getUserId())) {
            isShow = false;
        } else {
            isShow = true;
        }
        final CommentPopupWindow pop = new CommentPopupWindow(mContext, type, isShow);
        final ImageView view = helper.getView(R.id.iv_common_more);
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

    private String getShowTime(FollowListBean item) {
        //TODO 不同类型 拿时间的字段不同
        String start = item.getF_start_date();
        String end = item.getF_end_date();
        if (start == null || TextUtils.isEmpty(start) || end == null || TextUtils.isEmpty(end)) {
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
        netView.setAdapter(new NineGridViewClickAdapter(App.mContext, imageInfo));
        //单张图大小
        netView.setSingleImageSize(dp2px(context, 150));
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

        void onCommentClick(FollowListBean item, View view);

    }

}
