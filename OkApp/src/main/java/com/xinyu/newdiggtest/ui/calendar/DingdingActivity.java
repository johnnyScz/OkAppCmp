//package com.xinyu.newdigg.ui.calendar;
//
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//
//import com.codbking.calendar.CaledarAdapter;
//import com.codbking.calendar.CalendarBean;
//import com.codbking.calendar.CalendarDateView;
//import com.codbking.calendar.CalendarLayout;
//import com.codbking.calendar.CalendarMyUtil;
//import com.codbking.calendar.CalendarView;
//
//import com.xinyu.newdigg.adapter.DingdingAdapter;
//import com.xinyu.newdigg.adapter.MyDakaAdapter;
//import com.xinyu.newdigg.bean.DingRetBean;
//import com.xinyu.newdigg.bean.DingdingBean;
//import com.xinyu.newdigg.bean.FollowListBean;
//import com.xinyu.newdigg.bean.HomeMsgBean;
//import com.xinyu.newdigg.bean.PersonChatBean;
//import com.xinyu.newdigg.bean.RoomPersonListBean;
//import com.xinyu.newdigg.inter.onPopClick;
//import com.xinyu.newdigg.ui.BaseNoEventActivity;
//import com.xinyu.newdigg.ui.Digg.fragment.RewardActivity;
//import com.xinyu.newdigg.ui.Digg.fragment.TodoAddActivity;
//import com.xinyu.newdigg.utils.DateUtil;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.BiFunction;
//import io.reactivex.schedulers.Schedulers;
//
//
//import com.xinyu.newdigg.R;
//import com.xinyu.newdigg.utils.DisplayUtils;
//import com.xinyu.newdigg.utils.IntentParams;
//import com.xinyu.newdigg.utils.NetApiUtil;
//import com.xinyu.newdigg.utils.PreferenceXshellUtil;
//import com.xinyu.newdigg.utils.ToastUtils;
//import com.xinyu.newdigg.widget.SoftInputPopUtil;
//
//
//public class DingdingActivity extends BaseNoEventActivity {
//
//    @BindView(R.id.calendarDateView)
//    CalendarDateView mCalendarDateView;
//
//    @BindView(R.id.list)
//    RecyclerView mList;
//    @BindView(R.id.week_day)
//    TextView mTitle;
//
//    @BindView(R.id.arrow)
//    ImageView img;
//
//
//    @BindView(R.id.add)
//    ImageView share;
//
//
//    @BindView(R.id.clipse_ll)
//    CalendarLayout clipLayout;
//
//    HomeCalendarNetUtil dateListner;
//
//    List<DingdingBean> allTypeData = new ArrayList<>();
//
//    DingdingAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mCalendarDateView.setAdapter(new CaledarAdapter() {
//            @Override
//            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
//                TextView view, chinaDay;
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
//                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
//                    convertView.setLayoutParams(params);
//                }
//
//                view = (TextView) convertView.findViewById(R.id.text);
//                chinaDay = (TextView) convertView.findViewById(R.id.chinaday);
//
//                if (bean.mothFlag != 0) {
//                    view.setTextColor(0xff9299a1);
//                } else {
//                    view.setTextColor(Color.parseColor("#cc000000"));
//                }
//
//                view.setTag(bean.mothFlag);
//                view.setText("" + bean.day);
//                chinaDay.setText(bean.chinaDay);
//                checkIfToday(bean, view, chinaDay);
//
//                return convertView;
//            }
//        });
//
//        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int postion, CalendarBean bean) {
//                mTitle.setText(getDisPlayNumber(bean.moth) + "月" + getDisPlayNumber(bean.day) + "日" + "  " + getWeekStr(bean.week));
//
//                getNetData(getDisPlayNumber(bean.year) + "-" + getDisPlayNumber(bean.moth) + "-" + getDisPlayNumber(bean.day));
//            }
//
//            @Override
//            public void onItemColorSelect(View view, CalendarBean bean) {
//                TextView text = view.findViewById(R.id.text);
//                TextView chinaday = view.findViewById(R.id.chinaday);
//                text.setTextColor(getResources().getColor(R.color.white));
//                chinaday.setTextColor(getResources().getColor(R.color.white));
//
//            }
//
//            @Override
//            public void onItemColorReset(View view, CalendarBean bean) {
//                TextView text = view.findViewById(R.id.text);
//                TextView chinaday = view.findViewById(R.id.chinaday);
//                chinaday.setTextColor(getResources().getColor(R.color.gray_color99));
//
//                int tag = (int) text.getTag();
//                if (tag != 0) {
//                    text.setTextColor(0xff9299a1);
//                } else {
//                    text.setTextColor(Color.parseColor("#cc000000"));
//                }
//            }
//
//
//        });
//
//        int[] data = CalendarMyUtil.getYMD(new Date());
//        mTitle.setText(data[1] + "月" + data[2] + "日" + "  " + getWeekStr(data[3]));
//
//        clipLayout.setSwitchListner(new CalendarLayout.SlidListner() {
//            @Override
//            public void onOpen() {
//                img.setImageResource(R.drawable.arrow_u);
//            }
//
//            @Override
//            public void onFold() {
//                img.setImageResource(R.drawable.arrow_b);
//            }
//        });
//
//        initRecycleView();
//
//        initDataListner();
//
//        initPop();
//    }
//
//    private void getNetData(String date) {
//        dateListner.getFocusList(date);
//    }
//
//
//    private void initDataListner() {
//
//        dateListner = new HomeCalendarNetUtil().getInstance(mContext);
//
//        dateListner.setDataListner(new HomeCalendarNetUtil.OnPuchDataListner() {
//            @Override
//            public void onSxData(List<RoomPersonListBean> list) {
//                allTypeData.clear();
//                if (list != null && list.size() > 0) {
//                    DingdingBean item = new DingdingBean();
//                    item.setItemType(5);
//                    item.setFrom_name("私信");
//                    allTypeData.add(item);
//                    for (RoomPersonListBean kk : list) {
//                        DingdingBean type1 = new DingdingBean();
//                        type1.setItemType(1);
//                        type1.setSxBean(kk);
//                        allTypeData.add(type1);
//                    }
//                }
//
//                dateListner.getFocusList("2019-02-28");
//
//            }
//
//            @Override
//            public void onMsgData(List<HomeMsgBean> dataList) {
//
//            }
//
//
//            @Override
//            public void onFocusData(List<FollowListBean> list) {
//
//                if (list != null && list.size() > 0) {
//                    DingdingBean item = new DingdingBean();
//                    item.setItemType(5);
//                    item.setFrom_name("关注");
//                    allTypeData.add(item);
//                    for (FollowListBean kk : list) {
//                        DingdingBean ff = new DingdingBean();
//                        ff.setItemType(4);
//                        ff.setFocusBean(kk);
//                        allTypeData.add(ff);
//                    }
//                }
//
//                refreshDatas(allTypeData);
//
//            }
//        });
//        requestAllData("2019-02-25");
//    }
//
//
//    private void requestAllData(String dateStr) {
//
//        dateListner.queryWxList("1", "20");
//
//
//    }
//
//
//    private void refreshDatas(List<DingdingBean> dataList) {
//        if (dataList.size() > 0) {
//            adapter = new DingdingAdapter(dataList);
//            mList.setAdapter(adapter);
//
//            adapter.setPopListner(new onPopClick() {
//                @Override
//                public void onZanClick(FollowListBean item, String content) {
//                    ToastUtils.getInstanc().showToast("点赞");
//
//                }
//
//                @Override
//                public void onDaShangClick(FollowListBean item) {
//                    ToastUtils.getInstanc().showToast("打赏");
//                }
//
//                @Override
//                public void onCommentClick(final FollowListBean item, View view) {
//
//                    SoftInputPopUtil.liveCommentEdit(mContext, view, new SoftInputPopUtil.liveCommentResult() {
//                        @Override
//                        public void onResult(boolean confirmed, String comment) {
//                            ToastUtils.getInstanc().showToast("评论");
//                        }
//                    });
//
//
//                }
//            });
//
//
//        }
//
//    }
//
//
//    private String getWeekStr(int datum) {
//
//        String str = "";
//        switch (datum) {
//            case 1:
//
//                return "周日";
//
//            case 2:
//                return "周一";
//
//            case 3:
//                return "周二";
//
//            case 4:
//                return "周三";
//
//            case 5:
//                return "周四";
//
//            case 6:
//                return "周五";
//
//            case 7:
//                return "周六";
//
//
//        }
//        return str;
//    }
//
//    /**
//     * 检查是否是今天
//     *
//     * @param bean
//     * @param view
//     */
//    private void checkIfToday(CalendarBean bean, TextView view, TextView chinaDay) {
//
//        String month = bean.moth > 9 ? bean.moth + "" : "0" + bean.moth;
//        String dayStr = bean.day > 9 ? bean.day + "" : "0" + bean.day;
//        String dateStr = bean.year + "-" + month + "-" + dayStr;
//
//        String today = DateUtil.getTodayStr();
//        if (dateStr.equals(today)) {
//            view.setText("今");
//            view.setTextColor(getResources().getColor(R.color.white));
//            chinaDay.setTextColor(getResources().getColor(R.color.white));
//        }
//
//
//    }
//
//
//    private void initRecycleView() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mList.setLayoutManager(layoutManager);//给RecyclerView设置适配器
//
//
//    }
//
//
//    @Override
//    protected int getLayoutResouce() {
//        return R.layout.activity_dingding1;
//    }
//
//    private String getDisPlayNumber(int num) {
//        return num < 10 ? "0" + num : "" + num;
//    }
//
//
//    public int px(float dipValue) {
//        Resources r = Resources.getSystem();
//        final float scale = r.getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
//
//
//    @OnClick(R.id.ll_slide)
//    public void onSlide() {
//        if (clipLayout.getType() == 0) {
//            clipLayout.flod();
//            clipLayout.setType(1);
//            img.setImageResource(R.drawable.arrow_b);
//
//        } else if (clipLayout.getType() == 1) {
//            clipLayout.open();
//            clipLayout.setType(0);
//            img.setImageResource(R.drawable.arrow_u);
//        }
//
//    }
//
//    PopupWindow popupWindow;
//
//    private void initPop() {
//        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_dingding, null);
//
//        setListner(view);
//
//        popupWindow = new PopupWindow(view, DisplayUtils.dp2px(this, 150),
//                DisplayUtils.dp2px(this, 250));
//
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!popupWindow.isShowing()) {
//
//                    popupWindow.showAsDropDown(share, (DisplayUtils.dp2px(mContext, 30)),
//                            DisplayUtils.dp2px(mContext, 30));
//
//                } else {
//                    popupWindow.dismiss();
//                }
//
//            }
//        });
//    }
//
//    private void setListner(LinearLayout view) {
//
//        int childCount = view.getChildCount();
//
//        for (int i = 0; i < childCount; i++) {
//            view.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int viewId = v.getId();
//                    switch (viewId) {
//
//                        case R.id.ll_creat_todo:
//                            startActivity(new Intent(mContext, TodoAddActivity.class));
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_suishou:
//                            ToastUtils.getInstanc().showToast("随手记");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_vote:
//                            ToastUtils.getInstanc().showToast("投票");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_join:
//                            ToastUtils.getInstanc().showToast("报名");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_pk:
//                            ToastUtils.getInstanc().showToast("创建PK");
//                            popupWindow.dismiss();
//                            break;
//
//                        case R.id.ll_swipt:
//                            ToastUtils.getInstanc().showToast("扫一扫");
//                            popupWindow.dismiss();
//                            break;
//
//
//                    }
//
//                }
//            });
//        }
//
//
//    }
//
//
//}
