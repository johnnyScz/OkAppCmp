package com.xinyu.newdiggtest.ui.circle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.widget.PraiseListView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yiwei on 16/5/17.
 */
public class CircleAdapter extends BaseRecycleViewAdapter {


    private Context context;


    public CircleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = 0;
        CircleItem item = (CircleItem) datas.get(position);
        if (CircleItem.TYPE_URL.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_TEST;
        } else if (CircleItem.TYPE_IMG.equals(item.getType())) {
            itemType = CircleViewHolder.TYPE_IMAGE;
        }
        return itemType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, parent, false);

        if (viewType == CircleViewHolder.TYPE_TEST) {
            //TODO 后面测试用
            viewHolder = new URLViewHolder(view);
        } else {
            viewHolder = new ImageViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final CircleViewHolder holder = (CircleViewHolder) viewHolder;
        final CircleItem circleItem = (CircleItem) datas.get(position);
        final String circleId = circleItem.getId();
        String name = circleItem.getUser().getName();
        String headImg = circleItem.getUser().getHeadUrl();
        final String content = circleItem.getContent();
        String createTime = circleItem.getCreateTime();
        final List<FavortItem> favortDatas = circleItem.getFavorters();
        final List<CommentItem> commentsDatas = circleItem.getComments();
        boolean hasFavort = circleItem.hasFavort();
        boolean hasComment = circleItem.hasComment();

        Glide.with(context).load(headImg).into(holder.headIv);

        holder.nameTv.setText(name);
        holder.timeTv.setText(createTime);

        if (!TextUtils.isEmpty(content)) {
            holder.contentTv.setExpand(circleItem.isExpand());
            holder.contentTv.setExpandStatusListener(new ExpandTextView.ExpandStatusListener() {
                @Override
                public void statusChange(boolean isExpand) {
                    circleItem.setExpand(isExpand);
                }
            });

            holder.contentTv.setText(content);
        }
        holder.contentTv.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
//            holder.contentTv.setVisibility(View.GONE);

        if (hasFavort || hasComment) {
            if (hasFavort) {//处理点赞列表
                holder.praiseListView.setOnItemClickListener(new PraiseListView.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        String userName = favortDatas.get(position).getName();
                        String userId = favortDatas.get(position).getItmeId();
                        Toast.makeText(App.getContext(), userName + " &id = " + userId, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.praiseListView.setDatas(favortDatas);
                holder.praiseListView.setVisibility(View.VISIBLE);
            } else {
                holder.praiseListView.setVisibility(View.GONE);
            }

            if (hasComment) {//处理评论列表
                holder.commentList.setDatas(commentsDatas);
                holder.commentList.setVisibility(View.VISIBLE);

            } else {
                holder.commentList.setVisibility(View.GONE);
            }
            holder.digCommentBody.setVisibility(View.VISIBLE);
        } else {
            holder.digCommentBody.setVisibility(View.GONE);
        }

        holder.digLine.setVisibility(hasFavort && hasComment ? View.VISIBLE : View.GONE);

        final SnsPopupWindow snsPopupWindow = holder.snsPopupWindow;
        //判断是否已点赞
        String curUserFavortId = circleItem.getCurUserFavortId(PreferenceUtil.getInstance(context).getUserId());
        if (!TextUtils.isEmpty(curUserFavortId)) {
            ArrayList<String> actions = new ArrayList<>();
            actions.add("取消");
            actions.add("打赏");
            actions.add("评论");
            snsPopupWindow.setmActionItems(actions);

        } else {

            ArrayList<String> actions = new ArrayList<>();
            actions.add("赞");
            actions.add("打赏");
            actions.add("评论");
            snsPopupWindow.setmActionItems(actions);
        }
        snsPopupWindow.update();

        snsPopupWindow.setmItemClickListener(new SnsPopupWindow.OnItemClickListener() {
            @Override
            public void onItemClick(String actionitem, int position) {
                switch (position) {
                    case 0://点赞、取消点赞
                        if (presenter != null) {
                            if ("赞".equals(actionitem)) {
                                presenter.addFavort(position);
                            } else {//取消点赞
                                presenter.deleteFavort(position, getFarorId(circleItem));
                            }
                        }
                        break;
                    case 1://打赏
                        if (presenter != null) {
                            presenter.dashang(position);
                        }
                        break;

                    case 2://发布评论
                        if (presenter != null) {
                            presenter.addComment(position);
                        }

                        break;
                    default:
                        break;
                }

            }
        });
        holder.snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });

        holder.urlTipTv.setVisibility(View.GONE);
        Log.e("amtf", "type:" + holder.viewType);
        switch (holder.viewType) {
            case CircleViewHolder.TYPE_TEST:// 后期加上
                break;
            case CircleViewHolder.TYPE_IMAGE:// 处理图片
                if (holder instanceof ImageViewHolder) {
                    final List<PhotoInfo> photos = circleItem.getPhotos();
                    if (photos != null && photos.size() > 0) {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.VISIBLE);
                        showImgs(((ImageViewHolder) holder).multiImageView, photos);

                    } else {
                        ((ImageViewHolder) holder).multiImageView.setVisibility(View.GONE);
                    }
                }
                break;

            default:
                break;
        }


    }

    /**
     * TODO
     * 获得我的当前评论的Favorid
     *
     * @param circleItem
     * @return
     */
    private String getFarorId(CircleItem circleItem) {
        return "favorid";
    }

    private void showImgs(MultiImageView netView, List<PhotoInfo> photos) {

        netView.setVisibility(View.VISIBLE);
        netView.setList(photos);
        netView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //imagesize是作为loading时的图片size
//                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
//
//                    List<String> photoUrls = new ArrayList<String>();
//                    for (PhotoInfo photoInfo : photos) {
//                        photoUrls.add(photoInfo.url);
//                    }
//                    ImagePagerActivity.startImagePagerActivity(((H5MainActivity) context), photoUrls, position, imageSize);
            }
        });
    }


    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    CommentCicleListner presenter;


    public void setListner(CommentCicleListner listner) {
        this.presenter = listner;
    }


    public interface CommentCicleListner {

        //赞
        void addFavort(int pos);

        //取消赞
        void deleteFavort(int pos, String favId);

        //添加评论
        void addComment(int pos);

        //添加打赏
        void dashang(int pos);


    }


}
