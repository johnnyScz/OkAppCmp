package com.xinyu.newdiggtest.inter;

import android.view.View;

import com.xinyu.newdiggtest.bean.FollowListBean;

public interface onPopClick {

    void onZanClick(FollowListBean item, String content);

    void onDaShangClick(FollowListBean item);

    void onCommentClick(FollowListBean item, View view);


}
