package com.xinyu.newdiggtest.net;

import com.xinyu.newdiggtest.bean.CardFilterBean;
import com.xinyu.newdiggtest.bean.CheckFinshReturnBean;
import com.xinyu.newdiggtest.bean.CommentReturnBean;
import com.xinyu.newdiggtest.bean.ContactSearchBean;
import com.xinyu.newdiggtest.bean.DingReturnBean;
import com.xinyu.newdiggtest.bean.FileChildRetBean;
import com.xinyu.newdiggtest.bean.GroupDashangBean;
import com.xinyu.newdiggtest.bean.HomeMsgCountBean;
import com.xinyu.newdiggtest.bean.HomeMsgRetBean;
import com.xinyu.newdiggtest.bean.MobieCompanyBean;
import com.xinyu.newdiggtest.bean.MsgCountRetBean;
import com.xinyu.newdiggtest.bean.PersonContactBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.bean.TargetKindRetBean;
import com.xinyu.newdiggtest.bean.TodoMsgRetBean;
import com.xinyu.newdiggtest.bean.UnreadMsgListBean;
import com.xinyu.newdiggtest.bean.XhintMsgBean;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.net.bean.BasePraiseBean;
import com.xinyu.newdiggtest.net.bean.BingBean;
import com.xinyu.newdiggtest.net.bean.CardInsertBean;
import com.xinyu.newdiggtest.net.bean.CmpBeanCard;
import com.xinyu.newdiggtest.net.bean.CmpCardBean;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.net.bean.LeagueBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface NetApi {


    //get 请求
    @GET("onebox/football/league")
    Observable<LeagueBean> getleagueList(@Query("key") String key, @Query("league") String league);


    /**
     * TODO
     * Get 请求 map传参
     */

    @GET("onebox/football/league")
    Observable<LeagueBean> getSearchBooks(@QueryMap Map<String, String> options);


    /**
     * 新建待办
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> commitTodo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<TargetInfo> getTargetInfo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<DingReturnBean> getTargetInfoDakaList(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<CheckFinshReturnBean> checkMyTargetList(@FieldMap Map<String, String> map);


    /**
     * 目标分类
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<TargetKindRetBean> targetKind(@FieldMap Map<String, String> map);


    /**
     * 关注目标
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<InfoStr> targetFocus(@FieldMap Map<String, String> map);


    /**
     * 删除代办
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<InfoStr> deletTodo(@FieldMap Map<String, String> map);


    /**
     * 删除代办打卡 内容
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<InfoStr> deletTodoDaka(@FieldMap Map<String, String> map);


    //我的关注目标
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<GroupDashangBean> getSelfFocusTargetList1(@FieldMap Map<String, String> map);


    //编辑待办
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> editTodu(@FieldMap Map<String, String> map);


    //首页消息(需要轮训)
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<HomeMsgRetBean> getHomeMsg(@FieldMap Map<String, String> map);


    //编辑待办
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> quitGroup(@FieldMap Map<String, String> map);


    //通过手机号查询公司信息
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<MobieCompanyBean> getCompanyInfoByMobile(@FieldMap Map<String, String> map);

    /**
     * 所有消息置为已读
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("notices/nt/readAll")
    Observable<InfoStr> readAllMsg(@FieldMap Map<String, String> map);


    /**
     * 修改用户资料
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<InfoStr> updateUserInfo(@FieldMap Map<String, String> map);


    /**
     * 查询评论列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.commentRedirectAction&target=enterprise.comment.plugin.queryByTypeAndId")
    Observable<CommentReturnBean> queryCommentList(@FieldMap Map<String, String> map);


    /**
     * 新增评论
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.commentRedirectAction&target=enterprise.comment.plugin.create")
    Observable<BaseBean> insetComments(@FieldMap Map<String, String> map);


    /**
     * 是否点赞过
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.plugin.getDetail")
    Observable<BasePraiseBean> getPraiseInfo(@FieldMap Map<String, String> map);


    /**
     * 点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.plugin.do")
    Observable<BaseBean> doPraise(@FieldMap Map<String, String> map);


    /**
     * 取消点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.plugin.undo")
    Observable<BaseBean> unDoPraise(@FieldMap Map<String, String> map);


    /**
     * 飞信列表
     *
     * @param map
     *
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetXhintCountByDay")
    Observable<XhintMsgBean> getXhintList(@FieldMap Map<String, String> map);


    /**
     * 名片列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetUserJoinCard")
    Observable<Object> getCardList(@FieldMap Map<String, String> map);

    /**
     * 名片上传
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=crm.ocr.business-card")
    Observable<CardFilterBean> queryCardInfo(@FieldMap Map<String, String> map);


    /**
     * 拆入名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.InsertCustomer")
    Observable<CardInsertBean> insertCard(@FieldMap Map<String, String> map);


    /**
     * 公司分组查询
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=crm.customer.queryByOwnerGroupByOrg")
    Observable<CmpCardBean> queryCardGroup(@FieldMap Map<String, String> map);


    /**
     * 名片按公司分组查询
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetCardAndUserId")
    Observable<CmpBeanCard> queryCardDetail(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.AttachmentGroupRedirectAction&target=enterprise.attachment.group..getByGroup")
    Observable<FileChildRetBean> queryFileComment(@FieldMap Map<String, String> map);


    /**
     * 文件类型评论详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.commentRedirectAction&target=enterprise.comment.queryByTypeAndId")
    Observable<CommentReturnBean> getFileComment(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.getDetail")
    Observable<BasePraiseBean> getFilePraiseInfo(@FieldMap Map<String, String> map);


    /**
     * 文件表单 点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.do")
    Observable<BaseBean> doFilePraise(@FieldMap Map<String, String> map);


    /**
     * 文件表单 取消点赞
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.CommentRedirectAction&target=enterprise.like.undo")
    Observable<BaseBean> undoFilePraise(@FieldMap Map<String, String> map);


    /**
     * 文件表单 新增回复
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.commentRedirectAction&enterprise.comment.create")
    Observable<Object> insertFileComment(@FieldMap Map<String, String> map);


    /**
     * 新增文件评论
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.commentRedirectAction&target=enterprise.comment.create")
    Observable<BaseBean> insetFileComments(@FieldMap Map<String, String> map);


    /**
     * 新增文件评论
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.AttachmentGroupRedirectAction&target=enterprise.attachment.group.group.create")
    Observable<BaseBean> creatFolder(@FieldMap Map<String, String> map);


    /**
     * 查询首页消息类别(推送而得到)
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.QueryNotice")
    Observable<TodoMsgRetBean> queryHomeMsgList(@FieldMap Map<String, String> map);

    /**
     * 查询首页未读消息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.AttachmentGroupRedirectAction&target=enterprise.msg.queryUnreadedByOwner")
    Observable<MsgCountRetBean> queryMsgUnreadMun(@FieldMap Map<String, String> map);


    /**
     * 查询首页未读消息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.AttachmentGroupRedirectAction&target=enterprise.msg.queryByOwner")
    Observable<UnreadMsgListBean> queryMsgUnread(@FieldMap Map<String, String> map);

    /**
     * 消息置为已读
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GeneralRedirectAction&target=enterprise.msg.read")
    Observable<Object> readMsg(@FieldMap Map<String, String> map);


    /**
     * 微信绑定手机号
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.bindingPhone")
    Observable<Info> bindingPhone(@FieldMap Map<String, String> map);


    /**
     * 手机号绑定微信
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.bindingWx")
    Observable<BingBean> phonebindingWx(@FieldMap Map<String, String> map);


    /**
     * 微信绑定手机号
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.bindingPhone")
    Observable<Info> wxBangPhone(@FieldMap Map<String, String> map);


    /**
     * 首页最新消息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetHomePageMsg")
    Observable<Object> topicChanel(@FieldMap Map<String, String> map);


    /**
     * 查询首页-群组和待办的未读数量
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetHomePageMsg&target=enterprise.msg.queryUnreadedByOwner")
    Observable<HomeMsgCountBean> queryMsgCount(@FieldMap Map<String, String> map);


    /**
     * 查询联系人-名片
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=crm.customer.queryByIds")
    Observable<Object> queryCardMsg(@FieldMap Map<String, String> map);


    /**
     * 个人通讯录
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UserFriendAndCardList")
    Observable<PersonContactBean> getPersonContact(@FieldMap Map<String, String> map);


    /**
     * 首页搜索
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetContactsAndCard")
    Observable<ContactSearchBean> searchPerson(@FieldMap Map<String, String> map);


    /**
     * 首页搜索
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.ReadChatMsg")
    Observable<Object> redChatMsg(@FieldMap Map<String, String> map);


}
