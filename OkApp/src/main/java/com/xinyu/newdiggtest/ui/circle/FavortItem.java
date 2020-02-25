package com.xinyu.newdiggtest.ui.circle;

import java.io.Serializable;

/**
 * @author yiw
 * @ClassName: FavortItem
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015-12-28 下午3:44:56
 */
public class FavortItem implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //点赞id或者打赏id
    private String itmeId;
    private String userid;
    private String name;
    private String content;


    public String getItmeId() {
        return itmeId;
    }

    public void setItmeId(String itmeId) {
        this.itmeId = itmeId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
