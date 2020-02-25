package com.xinyu.newdiggtest.net;

import com.xinyu.newdiggtest.adapter.VipListReturnBean;
import com.xinyu.newdiggtest.adapter.viewhelper.TreeBean;
import com.xinyu.newdiggtest.bean.AccountBean;
import com.xinyu.newdiggtest.bean.AfairBean;
import com.xinyu.newdiggtest.bean.AskRoomIdBean;
import com.xinyu.newdiggtest.bean.ChanelBean;
import com.xinyu.newdiggtest.bean.ChannelRetunBean;
import com.xinyu.newdiggtest.bean.CheckRetBean;
import com.xinyu.newdiggtest.bean.CreatFeedBackBean;
import com.xinyu.newdiggtest.bean.CreatRoomId;
import com.xinyu.newdiggtest.bean.CreateGroupBean;
import com.xinyu.newdiggtest.bean.CreateTargetBean;
import com.xinyu.newdiggtest.bean.DakaInfoBean;
import com.xinyu.newdiggtest.bean.DakaSelectBean;
import com.xinyu.newdiggtest.bean.DashangBean;
import com.xinyu.newdiggtest.bean.DeletTodoBean;
import com.xinyu.newdiggtest.bean.DingRetBean;
import com.xinyu.newdiggtest.bean.DingReturnBean;
import com.xinyu.newdiggtest.bean.EditGroupBean;
import com.xinyu.newdiggtest.bean.FeedBackBean;
import com.xinyu.newdiggtest.bean.FileLoadBean;
import com.xinyu.newdiggtest.bean.FilmBean;
import com.xinyu.newdiggtest.bean.FocusBean;
import com.xinyu.newdiggtest.bean.FocusPersonBean;
import com.xinyu.newdiggtest.bean.FocusRetuBean;
import com.xinyu.newdiggtest.bean.GroupDashangBean;
import com.xinyu.newdiggtest.bean.GroupListBean;
import com.xinyu.newdiggtest.bean.GroupMemberBean;
import com.xinyu.newdiggtest.bean.HomeDakaBean;
import com.xinyu.newdiggtest.bean.ImParentBean;
import com.xinyu.newdiggtest.bean.InsertTodoBean;
import com.xinyu.newdiggtest.bean.JoinBean;
import com.xinyu.newdiggtest.bean.LeaveInfoBean;
import com.xinyu.newdiggtest.bean.LoginBean;
import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.bean.MileStoneBean;
import com.xinyu.newdiggtest.bean.MobileBean;
import com.xinyu.newdiggtest.bean.MonitorListBean;
import com.xinyu.newdiggtest.bean.MonitorSpanBean;
import com.xinyu.newdiggtest.bean.MsgBean;
import com.xinyu.newdiggtest.bean.MsgRedBean;
import com.xinyu.newdiggtest.bean.MsgRetBean;
import com.xinyu.newdiggtest.bean.NaotuCountBean;
import com.xinyu.newdiggtest.bean.OrderReturnBean;
import com.xinyu.newdiggtest.bean.PayBean;
import com.xinyu.newdiggtest.bean.PermissionBean;
import com.xinyu.newdiggtest.bean.PersonChatBean;
import com.xinyu.newdiggtest.bean.RecentMsgBean;
import com.xinyu.newdiggtest.bean.RedMsg;
import com.xinyu.newdiggtest.bean.RewardReturnBean;
import com.xinyu.newdiggtest.bean.RoomIdBean;
import com.xinyu.newdiggtest.bean.RoomInfoBean;
import com.xinyu.newdiggtest.bean.RoomRtrBean;
import com.xinyu.newdiggtest.bean.SectionRetBean;
import com.xinyu.newdiggtest.bean.SelfBean;
import com.xinyu.newdiggtest.bean.ShareUrlBean;
import com.xinyu.newdiggtest.bean.SpaceDakaReturnBean;
import com.xinyu.newdiggtest.bean.SpaceTargetRetunBean;
import com.xinyu.newdiggtest.bean.TargetInfo;
import com.xinyu.newdiggtest.bean.TargetListBean;
import com.xinyu.newdiggtest.bean.TiaozhanReturnBean;
import com.xinyu.newdiggtest.bean.TodoRetBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.bean.UserInfoBean;
import com.xinyu.newdiggtest.bean.VersionBean;
import com.xinyu.newdiggtest.bean.WalletDashangBean;
import com.xinyu.newdiggtest.bean.WxBean;
import com.xinyu.newdiggtest.net.bean.BaseBean;
import com.xinyu.newdiggtest.net.bean.CommBean;
import com.xinyu.newdiggtest.net.bean.Info;
import com.xinyu.newdiggtest.net.bean.InfoData;
import com.xinyu.newdiggtest.net.bean.InfoStr;
import com.xinyu.newdiggtest.net.bean.LeagueBean;
import com.xinyu.newdiggtest.net.bean.ShenHeLoginBean;
import com.xinyu.newdiggtest.net.bean.ZfbInfo;
import com.xinyu.newdiggtest.ui.Digg.fragment.GroupListDotBean;
import com.xinyu.newdiggtest.utils.NetApiUtil;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface AppUrl {


    //get 请求
    @GET("onebox/football/league")
    Observable<LeagueBean> getleagueList(@Query("key") String key, @Query("league") String league);


    /**
     * TODO
     * <p>
     * get请求用Map传参数
     *
     * @param options
     * @return
     */

    @GET("onebox/football/league")
    Observable<LeagueBean> getSearchBooks(@QueryMap Map<String, String> options);


    /**
     * 账户登录(用于平台审核)
     *
     * @param map
     * @return
     */

    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<ShenHeLoginBean> accountLogin(@FieldMap Map<String, String> map);


    //发送短信验证码/登录

    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<MsgBean> sendPhoneMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<CreateGroupBean> creatGroup(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 私聊
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<PersonChatBean> personChatList(@FieldMap Map<String, String> map);

    /**
     * @param map
     * @return 获取群组列表
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<GroupListBean> getGroupList(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 修改群名称
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<EditGroupBean> editGroupName(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 获取群成员
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<GroupMemberBean> getGroupMember(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanDetailByUuid")
    Observable<TargetInfo> getTargetInfo(@FieldMap Map<String, String> map);


    /**
     * 获取目标详情 关注人列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByUuid")
    Observable<FocusBean> getTargetFocusList(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 获取目标列表
     */
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByUserid")
    Observable<TargetListBean> getMyTargetList(@FieldMap Map<String, String> map);


    /**
     * 获取目标列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByUserid")
    Observable<TargetListBean> getTargetListGet(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<TargetListBean> getTargetListPost(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 获取分享链接
     */
    @FormUrlEncoded
    @POST("ok/GetWxOAuthUrl")
    Observable<ShareUrlBean> getShareUrl(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 目标打分
     */
    @FormUrlEncoded
    @POST("excitation/score/create")
    Observable<CheckRetBean> checkTargetFinish(@FieldMap Map<String, String> map);


    //创建打卡
    @FormUrlEncoded
    @POST("diggplus/target/insertTargetExecute")
    Observable<MsgBean> createDaka(@FieldMap Map<String, String> map);


    //获取目标列表
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByUserId")
    Observable<DakaSelectBean> getTargetList(@FieldMap Map<String, String> map);


    //获取钱包账户信息
    @FormUrlEncoded
    @POST("account/target/getMyAccount")
    Observable<AccountBean> getAccountInfo(@FieldMap Map<String, String> map);


    //添加手机号和支付密码
    @FormUrlEncoded
    @POST("account/target/updatePhoneAndPayPwd")
    Observable<Info> setPayPwd(@FieldMap Map<String, String> map);


    //目标支付挑战金
    @FormUrlEncoded
    @POST("diggplus/target/updateTargetFine")
    Observable<Info> targetPayFinish(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("account/target/addBalanceChange")
    Observable<Info> payMoney(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 消息列表
     */
    @FormUrlEncoded
    @POST("nt/nt/query")
    Observable<MsgRetBean> getMsgList(@FieldMap Map<String, String> map);

    /**
     * @param map
     * @return 消息列表
     */
    @FormUrlEncoded
    @POST("notices/nt/query")
    Observable<MsgRetBean> getMsgList1(@FieldMap Map<String, String> map);


    //获取我的打卡和关注的打卡
    @FormUrlEncoded
    @POST("diggplus/target/listTargetExecuteAndFollow")
    Observable<HomeDakaBean> getDaka2tList(@FieldMap Map<String, String> map);


    //单独参数
    @FormUrlEncoded
    @POST("posts/test")
    Call<String> createCommit(@Field("mobile") String mobile);

    /**
     * @param map
     * @return 新建目标
     */
    @FormUrlEncoded
    @POST("diggplus/target/insertTarget")
    Observable<CreateTargetBean> createTarget(@FieldMap Map<String, String> map);

    /**
     * @param map
     * @return 新建目标(0325新)
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<CreateTargetBean> createTarget1(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 获取监督人列表
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<MonitorListBean> getMonitorList(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 获取监督人列表
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<MonitorSpanBean> getMonitorList1(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return检查验证码
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<Info> checkCode(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 打赏, 奖励金支付
     */
    @FormUrlEncoded
    @POST("excitation/hearten/create")
    Observable<DashangBean> targetDashagn(@FieldMap Map<String, String> map);


    /**
     * TODO 数据上传模板
     * 上传头像
     */
    @Multipart
    @POST("diggplus/uploadfile")
    Observable<UploadUrlBean> uploadImgs(@Part List<MultipartBody.Part> partList);


    @Multipart
    @POST("api2e/UploadImage")
    Observable<FileLoadBean> uploadFile(@Part List<MultipartBody.Part> partList);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.InsertTodoAboutCheck")
    Observable<InsertTodoBean> commitInsertTodo(@FieldMap Map<String, String> map);


    //打卡
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<MsgBean> dakaUpload(@FieldMap Map<String, String> map);


    /**
     * TODO 数据上传模板
     * 上传头像
     */
    @Multipart
    @POST("ok/uploadfile")
    Observable<UploadUrlBean> uploadOkImgs(@Part List<MultipartBody.Part> partList);

    /**
     * 添加评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/insertTargetComment")
    Observable<Info> addComments1(@FieldMap Map<String, String> map);

    /**
     * 添加评论
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> addComments(@FieldMap Map<String, String> map);


    /**
     * 用户点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> addZan(@FieldMap Map<String, String> map);


    /**
     * 用户点赞
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/insertTargetLikes")
    Observable<Info> addZan1(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("diggplus/target/deleteTargetLikesByUuid")
    Observable<Info> cancelZan(@FieldMap Map<String, String> map);


    //----------------IM 相关--------------------------

    /**
     * @param map
     * @return IM 聊天信息查询
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<ImParentBean> getImMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 聊天信息查询
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.QueryMsg")
    Observable<ImParentBean> getHistoryIMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 阅读聊天消息
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.ReadChatMsg")
    Observable<Info> redMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 发送聊天信息
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> sendImMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 发送聊天信息
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet")
    Observable<Info> sendCompanyMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 关注别人的目标
     */
    @FormUrlEncoded
    @POST("diggplus/target/addMyfollowTargetPlan")
    Observable<Info> focusTarget(@FieldMap Map<String, String> map);

    /**
     * @param map
     * @return 是否是关注目标
     */
    @FormUrlEncoded
    @POST(" diggplus/target/getCountMyfollowTarget")
    Observable<Info> checkisfocusTarget(@FieldMap Map<String, String> map);


    /**
     * @return IM 轮询即时消息
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<RecentMsgBean> getLatestMsg(@FieldMap Map<String, String> map);


    //------------------群组相关（群目标/群打卡）---------------------------

    /**
     * @return 获取群目标
     */

    @FormUrlEncoded
    @POST("diggplus/target/listGroupTargetPlanByGroupId")
    Observable<GroupDashangBean> getGroupTarget(@FieldMap Map<String, String> map);


    //------------------钱包明细相关---------------------------

    @FormUrlEncoded
    @POST("diggplus/target/listTargetFineByUserId")
    Observable<WalletDashangBean> getWalletTiaozhan(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<TiaozhanReturnBean> getTiaozhanInfo(@FieldMap Map<String, String> map);


    /**
     * @return 钱包打卡打赏明细
     */
    @FormUrlEncoded
    @POST("account/target/listTargetPlanReward")
    Observable<WalletDashangBean> getWalletDashang(@FieldMap Map<String, String> map);

    /**
     * @return 目标奖励金明细
     */
    @FormUrlEncoded
    @POST("account/target/listTargetReward")
    Observable<WalletDashangBean> getWalletReward(@FieldMap Map<String, String> map);

    //------------------个人空间相关---------------------------


    /**
     * 个人空间目标列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByMaxDay")
    Observable<SpaceTargetRetunBean> getSpaceTargetList(@FieldMap Map<String, String> map);

    //个人空间打卡列表
    @FormUrlEncoded
    @POST("diggplus/target/listTargetExecuteByMaxDay")
    Observable<SpaceDakaReturnBean> getSpaceDakatList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("diggplus/target/addMyfollowUser")
    Observable<Info> addFavorPerson(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("diggplus/target/delMyfollowUser")
    Observable<Info> cancelFavorPerson(@FieldMap Map<String, String> map);

    /**
     * 是否是自己的关注人
     *
     * @return
     */

    @FormUrlEncoded
    @POST("diggplus/target/getCountMyfollowUser")
    Observable<Info> checkifFavorPerson(@FieldMap Map<String, String> map);


    //我的关注目标
    @FormUrlEncoded
    @POST("diggplus/target/listTargetPlanByFollow")
    Observable<GroupDashangBean> getSelfFocusTargetList(@FieldMap Map<String, String> map);


    /**
     * @return 取消关注目标
     */
    @FormUrlEncoded
    @POST("diggplus/target/delMyfollowTarget")
    Observable<Info> cancelFocusTarget(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 获取分析分享链接
     */
    @FormUrlEncoded
    @POST("ok/GetWxOAuthUrl")
    Observable<InfoData> getLinkUrl(@FieldMap Map<String, String> map);


    //---------xhit-------


    /**
     * 轮训最新消息
     *
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<RedMsg> getXhintCount(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 群列表读消息
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> readGroupMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 设置已读消息（生产）
     */
    @FormUrlEncoded
    @POST("nt/nt/read")
    Observable<MsgRedBean> readMsg(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return IM 设置已读消息（测试）
     */
    @FormUrlEncoded
    @POST("notices/nt/read")
    Observable<MsgRedBean> readMsg1(@FieldMap Map<String, String> map);

    /**
     * 获取群组红点
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<GroupListDotBean> readGroupRedDotList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("diggplus/target/insertDeviceLog")
    Observable<Info> upDeviceToken(@FieldMap Map<String, String> map);


    /**
     * 结束目标
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/endTarget")
    Observable<Info> finishTarget(@FieldMap Map<String, String> map);


    /**
     * 目标奖励金明细
     *
     * @return
     */
    @FormUrlEncoded
    @POST("excitation/hearten/list")
    Observable<RewardReturnBean> rewardFineList(@FieldMap Map<String, String> map);


    //-------------------------第三方支付-----------------------------------------


    /**
     * 第三发支付
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<PayBean> wxZfbPay(@FieldMap Map<String, String> map);


    /**
     * 第三发支付
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<ZfbInfo> ZfbPay(@FieldMap Map<String, String> map);


    /**
     * 查询订单列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<OrderReturnBean> queryOrderList(@FieldMap Map<String, String> map);


    //验证支付密码
    @FormUrlEncoded
    @POST("account/target/validatePayPwd")
    Observable<Info> checkPwd(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<UserInfoBean> getUserInfo(@FieldMap Map<String, String> map);


//    //修改目标
//    @FormUrlEncoded
//    @POST("diggplus/target/updateTarget")
//    Observable<Info> editTarget(@FieldMap Map<String, String> map);

    //修改目标
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> editTarget(@FieldMap Map<String, String> map);


    //我的页面
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<SelfBean> getSelfInfo(@FieldMap Map<String, String> map);


    //我关注的人
    @FormUrlEncoded
    @POST("diggplus/target/listTargetLikesByUser")
    Observable<FocusPersonBean> getFocusPersonList(@FieldMap Map<String, String> map);


    //我的推荐
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<VipListReturnBean> getRecomendList(@FieldMap Map<String, String> map);


    //用户反馈的提示
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<FeedBackBean> getAdviceInfo(@FieldMap Map<String, String> map);


    //新增用户反馈

    @FormUrlEncoded
    @POST("diggplus/feedback/insertFeedback")
    Observable<CreatFeedBackBean> creatFeedBack(@FieldMap Map<String, String> map);


    /**
     * 取消目标
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> cancelTarget(@FieldMap Map<String, String> map);


    /**
     * vip 消息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("nt/nt/create")
    Observable<NetApiUtil.VipMsg> creatVipMissMsg(@FieldMap Map<String, String> map);


    /**
     * TODO
     * 测试环境
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("notices/nt/create")
    Observable<NetApiUtil.VipMsg> creatVipMissMsg1(@FieldMap Map<String, String> map);


    /**
     * 查询目标人所在的聊天室
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<RoomIdBean> queryChatRoom(@FieldMap Map<String, String> map);


    /**
     * 新建聊天室
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<CreatRoomId> creatChatRoom(@FieldMap Map<String, String> map);


    /**
     * 私信消息红点数
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<InfoData> spaceRedCount(@FieldMap Map<String, String> map);


    /**
     * 获取版本信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<VersionBean> getAppVersion(@FieldMap Map<String, String> map);


    /**
     * 获取打卡详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/getTargetExecuteByPlanId")
    Observable<DakaInfoBean> getDakaInfo(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("diggplus/target/listFollowByUserAndTarget")
    Observable<DingRetBean> getFoucsPerson2Target(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<DingReturnBean> getTodoList(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 根据roomid查询群信息
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<RoomInfoBean> getRoomInfo(@FieldMap Map<String, String> map);


    /**
     * @param map
     * @return 加入群聊
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<JoinBean> joinRoom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<FilmBean> getFilm(@FieldMap Map<String, String> map);


    /**
     * 删除好友
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api/ESBServlet")
    Observable<Info> deletMembers(@FieldMap Map<String, String> map);


    /**
     * 查询群组成员
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=chat.QueryChatRoomMember")
    Observable<GroupMemberBean> queryZuMembers(@FieldMap Map<String, String> map);


    /**
     * 查询群组架构
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.GetOrgListWithOutPeople")
    Observable<TreeBean> queryCompanyTotal(@FieldMap Map<String, String> map);


    /**
     * 查询群组成员
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.QuerySingleOrgUser")
    Observable<MemberRetBean> queryLevel2Member(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetTodoById")
    Observable<TodoRetBean> getDodoInById(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateTodo")
    Observable<CommBean> upDateTodo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.DeleteTodo")
    Observable<DeletTodoBean> deletTodo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.InsertFinishNote")
    Observable<DeletTodoBean> addTodoNote(@FieldMap Map<String, String> map);


    /**
     * 更新
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateFinishNote")
    Observable<DeletTodoBean> upLoadTodoNote(@FieldMap Map<String, String> map);


    /**
     * 创建子待办
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.InsertTodo")
    Observable<BaseBean> creatChildTodo(@FieldMap Map<String, String> map);


    //发送短信验证码/登录

    /**
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("ok/ESBServlet")
    Observable<WxBean> getWxInfo(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.getUserInfo")
    Observable<MobileBean> getWxMobile(@FieldMap Map<String, String> map);


    /**
     * 更新用户信息（绑定手机号）
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.UpdateUserInfo")
    Observable<Info> bindMobile(@FieldMap Map<String, String> map);


    /**
     * 获取公司的个数
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetCompanyAndUserList")
    Observable<Object> getCompanyList(@FieldMap Map<String, String> map);


    /**
     * 获取首页待办列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetTodoByDay")
    Observable<TodoRetBean> getTodoListByDay(@FieldMap Map<String, String> map);


    /**
     * 更新邀请状态
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateInviteState")
    Observable<Info> updateInviteState(@FieldMap Map<String, String> map);


    /**
     * 获取(新建)聊天室
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetOrCreateChatRoomAboutTodo")
    Observable<RoomRtrBean> getChatRoomId(@FieldMap Map<String, String> map);


    /**
     * 发送短信验证码
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=user.SendSmsVerificationCodeBase")
    Observable<Info> sendPhoneCode(@FieldMap Map<String, String> map);


    /**
     * 用户注册
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.NewUserRegister")
    Observable<Info> regeister(@FieldMap Map<String, String> map);


    /**
     * 用户登录
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.NewUserLogin")
    Observable<LoginBean> loginNew(@FieldMap Map<String, String> map);


    /**
     * 创建聊天室
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetOrCreateChatRoom")
    Observable<AskRoomIdBean> getOrCreateChatRoom(@FieldMap Map<String, String> map);


    /**
     * 更新完成状态
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateFinishState")
    Observable<InfoStr> updateFinishState(@FieldMap Map<String, String> map);


    /**
     * 查询所在部门
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=user.GetAllOrgBySelfAction")
    Observable<SectionRetBean> getSection(@FieldMap Map<String, String> map);


    /**
     * 批量删除好友
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=chat.BatchDelChatRoomMember")
    Observable<BaseBean> delectGroupMember(@FieldMap Map<String, String> map);


    /**
     * 批量新增好友
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=chat.BatchAddChatRoomMember")
    Observable<BaseBean> addGroupMember(@FieldMap Map<String, String> map);


    /**
     * 上传设备信息(友盟token
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.InsertDeviceLogAction")
    Observable<BaseBean> insertDeviceLog(@FieldMap Map<String, String> map);


    /**
     * 请假流程待办
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.QueryLeaveProcessAction")
    Observable<LeaveInfoBean> getLeaveInfo(@FieldMap Map<String, String> map);


    /**
     * 请假审批
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateLeaveProcessAction")
    Observable<BaseBean> checkProcess(@FieldMap Map<String, String> map);


    /**
     * 请求事务群组
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.QueryAffair")
    Observable<AfairBean> checkAffair(@FieldMap Map<String, String> map);


    /**
     * 请求里程碑
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.OwnerRedirectAction&target=enterprise.check.queryMileStone")
    Observable<MileStoneBean> qureyMileStone(@FieldMap Map<String, String> map);


    /**
     * 权限
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UserPluginPermission")
    Observable<PermissionBean> getPremission(@FieldMap Map<String, String> map);


    /**
     * 删除事务
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.DeleteAffair")
    Observable<BaseBean> deletAffair(@FieldMap Map<String, String> map);


    /**
     * 退出事务
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.DeleteAffairUser")
    Observable<BaseBean> logOutAffair(@FieldMap Map<String, String> map);


    /**
     * 按月查询待办日历红点
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=enterprise.todo.queryStatisticByMonth")
    Observable<BaseBean> queryMsgNote(@FieldMap Map<String, String> map);


    /**
     * a查询我所关注的人
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetWatchedUserList")
    Observable<FocusRetuBean> queryRelationFocus(@FieldMap Map<String, String> map);


    /**
     * 更新群组名称
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.UpdateAffair")
    Observable<BaseBean> updateAffair(@FieldMap Map<String, String> map);


    /**
     * 操作群
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GroupOperationRecord")
    Observable<BaseBean> notify(@FieldMap Map<String, String> map);


    /**
     * 查询频道
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.getPublicChannel")
    Observable<ChanelBean> getPublicChanel(@FieldMap Map<String, String> map);


    /**
     * 与我相关待办
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetSelfTodo")
    Observable<TodoRetBean> getSelfTodo(@FieldMap Map<String, String> map);


    /**
     * 表单详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.SelectLeaveListByIdsAction")
    Observable<Object> getBiaoInfo(@FieldMap Map<String, String> map);


    /**
     * 根据Id找到表单节点详情
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.GetNodeById")
    Observable<NaotuCountBean> getNoteInfo(@FieldMap Map<String, String> map);


    /**
     * 主题列表
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("api2e/ESBServlet?command=api2e.generalRedirectAction&target=enterprise.channel_topic.get_channel_topic")
    Observable<ChannelRetunBean> topicChanel(@FieldMap Map<String, String> map);


}
