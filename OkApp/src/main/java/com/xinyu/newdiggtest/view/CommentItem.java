package com.xinyu.newdiggtest.view;

import java.io.Serializable;

/**
 * @author yiw
 * @ClassName: CommentItem
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午3:44:38
 */
public class CommentItem implements Serializable {

    private String id;
    private String commentUser;
    private String toReplyUser;
    private String content;
    private String type = "1";//1 评论  2.喜欢

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    public String getToReplyUser() {
        return toReplyUser;
    }

    public void setToReplyUser(String toReplyUser) {
        this.toReplyUser = toReplyUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
