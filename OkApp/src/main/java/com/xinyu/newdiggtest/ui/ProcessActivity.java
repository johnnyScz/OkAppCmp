package com.xinyu.newdiggtest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FileProcessAdapter;
import com.xinyu.newdiggtest.adapter.ProcessAdapter;
import com.xinyu.newdiggtest.bean.FUrlBean;
import com.xinyu.newdiggtest.bean.LeaveInfoBean;
import com.xinyu.newdiggtest.h5.WebViewActivity;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.office.OfficeX5CoreActivity;
import com.xinyu.newdiggtest.utils.CircleCornerForm;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 请假详情
 */

public class ProcessActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;


    @BindView(R.id.creat_time)
    TextView creat_time;

    @BindView(R.id.ll_k_value_pair)
    LinearLayout keyValueLL;


    @BindView(R.id.name)
    TextView nameTx;


    @BindView(R.id.head)
    ImageView headImg;

    @BindView(R.id.confirm_cancel)
    View confirm_cancel;


    @BindView(R.id.filelist)
    RecyclerView documentRecycleView;

    @BindView(R.id.step_list)
    RecyclerView stepRecycle;

    String content = "";

    String Event_Type = "";


    LeaveInfoBean.LeaveBean stepLast, stepOne;//最后一步


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_process;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Event_Type = getIntent().getStringExtra("event_type");
        initView();

        getInfo();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        documentRecycleView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);

        layoutManager1.setSmoothScrollbarEnabled(true);
        layoutManager1.setAutoMeasureEnabled(true);
        stepRecycle.setLayoutManager(layoutManager1);//给RecyclerView设置适配器
        stepRecycle.setHasFixedSize(true);
        stepRecycle.setNestedScrollingEnabled(false);


    }


    @OnClick(R.id.iv_back)
    public void goCommit() {
        finish();
    }


    public void getInfo() {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("f_process_id", getIntent().getStringExtra("process_id"));
        map.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());

        url.getLeaveInfo(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LeaveInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(LeaveInfoBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            handleData(msg);
                        }
                    }
                });
    }


    private void handleData(LeaveInfoBean msg) {

        stepOne = msg.getData().get(0);
        int len = msg.getData().size();
        stepLast = msg.getData().get(len - 1);


        String head = stepOne.getExecutorinfo().getHead();
        Picasso.with(mContext).load(head).transform(new CircleCornerForm()).error(R.drawable.icon_no_download).into(headImg);

        nameTx.setText(stepOne.getExecutorinfo().getNickname());

        creat_time.setText(creatTime());

        if (Event_Type.equals("2") || Event_Type.equals("3")) {

            if (Event_Type.equals("2")) {
                tv_title.setText("办公设备申请表 " + msg.getCount() + "/" + (msg.getData().size()));
            } else {
                tv_title.setText("付款申请表" + msg.getCount() + "/" + (msg.getData().size()));
            }

            if (msg.getForm() != null && msg.getForm().size() > 0) {

                fillBlank(msg.getForm());

            }
        } else if (Event_Type.equals("1")) {//请假

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            lp.bottomMargin = DisplayUtils.dp2px(mContext, 10);


            View view = LayoutInflater.from(mContext).inflate(R.layout.frag_leave, null);
            TextView leave_tp = view.findViewById(R.id.leave_tp);
            TextView dure_date = view.findViewById(R.id.dure_date);
            TextView keep_long = view.findViewById(R.id.keep_long);
            TextView name_create = view.findViewById(R.id.name_create);
            TextView for_reason = view.findViewById(R.id.for_reason);


            name_create.setText(stepOne.getExecutorinfo().getNickname());
            String fType = stepOne.getF_form().getF_type();
            String JiaName = "";

            switch (fType) {

                case "1":
                    JiaName = "事假";
                    break;

                case "2":
                    JiaName = "病假";
                    break;

                case "3":
                    JiaName = "年假";
                    break;

                case "4":
                    JiaName = "调休";
                    break;

            }


            tv_title.setText(JiaName + msg.getCount() + "/" + (msg.getData().size() + 1));

            leave_tp.setText(JiaName);
            dure_date.setText(DateUtil.getDotDay(stepOne.getF_form().getF_start_time()) + "-" + DateUtil.getDotDay(stepOne.getF_form().getF_end_time()));
            keep_long.setText("共" + stepOne.getF_form().getF_day() + "天");
            for_reason.setText(stepOne.getF_form().getF_desc());

            keyValueLL.addView(view, lp);

        } else if (Event_Type.equals("5")) {//资金方月度结算

            tv_title.setText("资金月度结算单 " + msg.getCount() + "/" + (msg.getData().size() + 1));
            handleZjFfK(msg);

        } else if (Event_Type.equals("4")) {//推广费
            tv_title.setText("推广费结算单 " + msg.getCount() + "/" + (msg.getData().size() + 1));
            handleTuiGF(msg);
        }


        //步数列表
        if (msg.getData().size() > 1) {

            List<LeaveInfoBean.LeaveBean> dates = new ArrayList<>();

            int kk = msg.getData().size();
            for (int i = 1; i < kk; i++) {
                LeaveInfoBean.LeaveBean dt = msg.getData().get(i);
                dt.setStep(i + 1);
                dates.add(dt);
            }

            ProcessAdapter adapter = new ProcessAdapter(R.layout.item_leave, dates);

            stepRecycle.setAdapter(adapter);

        }


        //附件
        LeaveInfoBean.LeaveBean.FFileBean bean = stepOne.getF_file();

        if (bean != null && bean.getF_url() != null && bean.getF_url().size() > 0) {

            List<FUrlBean> data = bean.getF_url();
            FileProcessAdapter adapter = new FileProcessAdapter(R.layout.item_file, data);
            documentRecycleView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    FUrlBean item = (FUrlBean) adapter.getData().get(position);

                    String path = item.getF_type();

                    if (path.toLowerCase().equals("jpg") || path.toLowerCase().equals("jeg") || path.toLowerCase().equals("jpeg") || path.toLowerCase().equals("png")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("path", item.getF_path());
                        mContext.startActivity(intent);

                    } else {
                        Intent intent = new Intent(mContext, OfficeX5CoreActivity.class);
                        intent.putExtra("path", item.getF_path());
                        mContext.startActivity(intent);
                    }
                }
            });

        }

        //确认或删除相关
        String countBack = "";

        if (getIntent().hasExtra("count_back")) {
            countBack = getIntent().getStringExtra("count_back");
        }

        if (countBack.equals("1")
                && stepLast.getF_create_by().equals(PreferenceUtil.getInstance(mContext).getUserId())
                && stepLast.getF_state().equals("0")) {
            confirm_cancel.setVisibility(View.VISIBLE);

            TextView left = findViewById(R.id.left);

            TextView right = findViewById(R.id.right);

            LeaveInfoBean.LeaveBean.ProcedureBean ppb = stepLast.getProcedure();

            if (ppb != null) {

                content = ppb.getF_desc();
                left.setText(ppb.getF_button_left());
                right.setText(ppb.getF_button_right());
            }

        } else {
            confirm_cancel.setVisibility(View.GONE);
        }


    }

    /**
     * 推广费
     * 推广费
     *
     * @param msg
     */
    private void handleTuiGF(LeaveInfoBean msg) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.bottomMargin = DisplayUtils.dp2px(mContext, 10);

        View view = LayoutInflater.from(mContext).inflate(R.layout.frag_tuiguang, null);

        TextView name_create = view.findViewById(R.id.name_create);
        TextView month_belong = view.findViewById(R.id.month_belong);
        TextView papu_num = view.findViewById(R.id.papu_num);
        TextView total_money = view.findViewById(R.id.total_money);
        TextView chaosong = view.findViewById(R.id.chaosong);

        if (stepOne.getF_form() != null) {
            // 从恶心的map中取值
            chaosong.setText(getCcMapBuffer());
        }

        //---------赋值----------------

        name_create.setText(stepOne.getExecutorinfo().getNickname());

        if (stepOne.getF_form() != null) {
            month_belong.setText(getMonth(stepOne.getF_form().getF_settlement_month()) + "月");
        }

        papu_num.setText(stepOne.getF_form().getF_promotion());

        total_money.setText(stepOne.getF_form().getF_amount());


        keyValueLL.addView(view, lp);
    }

    private String getMonth(String f_settlement_month) {

        if (MyTextUtil.isEmpty(f_settlement_month) || f_settlement_month.length() < 7) {
            return "";
        }

        return f_settlement_month.substring(5, 7);
    }


    private String getCcMapBuffer() {

        if (stepOne.getF_form() == null || stepOne.getF_form().getCcmap() == null) {
            return "";
        }

        String jsonStr = new Gson().toJson(stepOne.getF_form().getCcmap());

        if (MyTextUtil.isEmpty(jsonStr)) {
            return "";
        }

        Map<String, Object> data = json2map(jsonStr);
        StringBuffer buffer = new StringBuffer();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object myData = entry.getValue();
            String ttChild = new Gson().toJson(myData);
            try {
                JSONObject mapChild = new JSONObject(ttChild);
                buffer.append(mapChild.getString("nickname")).append(" ");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }


    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {

            Log.e("amtf", "格式异常：" + e.getMessage());
        }
        return res;
    }


    private String creatTime() {

        String time = stepOne.getF_create_time();

        if (MyTextUtil.isEmpty(time) || time.length() < 11) {
            return "";
        }
        String newTime = time.replace("T", " ");
        return newTime.substring(0, newTime.length() - 3);
    }

    /**
     * 资金方结算
     *
     * @param msg
     */
    private void handleZjFfK(LeaveInfoBean msg) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.bottomMargin = DisplayUtils.dp2px(mContext, 10);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fine_process, null);

        TextView fine_give = view.findViewById(R.id.fine_give);

        TextView fine_get = view.findViewById(R.id.fine_get);

        TextView deal_date = view.findViewById(R.id.deal_date);

        TextView deal_days = view.findViewById(R.id.deal_days);

        TextView benjin = view.findViewById(R.id.benjin);

        TextView current_fine = view.findViewById(R.id.current_fine);

        TextView zhacha = view.findViewById(R.id.zhacha);

        TextView fencheng = view.findViewById(R.id.fencheng);

        TextView pay_mount = view.findViewById(R.id.pay_mount);

        TextView chaosong = view.findViewById(R.id.chaosong);

        LinearLayout ll_use = view.findViewById(R.id.ll_use);


        fine_give.setText(stepOne.getExecutorinfo().getNickname());

        if (stepOne.getF_form() != null) {
            fine_get.setText(stepOne.getF_form().getCustomermap().getF_name());
            deal_date.setText(getDealDate());

            deal_days.setText(stepOne.getF_form().getF_days());

            benjin.setText(stepOne.getF_form().getF_principal());

            current_fine.setText(stepOne.getF_form().getF_current_assets());

            zhacha.setText(stepOne.getF_form().getF_netting());

            fencheng.setText(stepOne.getF_form().getF_interest());

            pay_mount.setText(stepOne.getF_form().getF_request_amount());

            chaosong.setText(getCcMapBuffer());

            checkFineUse(ll_use);

        }

        keyValueLL.addView(view, lp);
    }

    /**
     * 资金用途
     *
     * @param ll_use
     */
    private void checkFineUse(LinearLayout ll_use) {

        List<String> uselist = stepOne.getF_form().getF_request_funds();

        if (uselist == null || uselist.size() < 1) {
            ll_use.setVisibility(View.GONE);
            return;
        } else {
            ll_use.setVisibility(View.VISIBLE);

            int len = uselist.size();

            if (len < 2) {
                TextView chi1 = (TextView) ll_use.getChildAt(0);
                chi1.setText(uselist.get(0));
                ll_use.getChildAt(1).setVisibility(View.GONE);
            } else {
                for (int i = 0; i < len; i++) {
                    TextView textView = (TextView) ll_use.getChildAt(i);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(uselist.get(i));
                }
            }


        }


    }

    /**
     * 结算周期
     *
     * @return
     */
    private String getDealDate() {

        String startTime = stepOne.getF_form().getF_start_time();
        String endTime = stepOne.getF_form().getF_end_time();

        if (MyTextUtil.isEmpty(startTime) || MyTextUtil.isEmpty(endTime)) {

            return "";
        }
        String startDot = startTime.replaceAll("-", ".");
        String endDot = endTime.replaceAll("-", ".");

        return startDot + "-" + endDot;
    }


    /**
     * @param form
     */
    private void fillBlank(List<LeaveInfoBean.FormKVBean> form) {


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        lp.bottomMargin = DisplayUtils.dp2px(mContext, 10);
        lp.topMargin = DisplayUtils.dp2px(mContext, 10);

        for (LeaveInfoBean.FormKVBean kv : form) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_act_leave, null);

            TextView kName = view.findViewById(R.id.k_name);
            TextView Vname = view.findViewById(R.id.v_name);
            kName.setText(kv.getName());
            Vname.setText(kv.getValue());
            keyValueLL.addView(view, lp);
        }


    }


    @OnClick(R.id.left)
    public void cancel() {

        Intent mtent = getIntent();
        mtent.setClass(mContext, CheckActivity.class);
        mtent.putExtra("state", "0");
        mtent.putExtra("title", content);

        mtent.putExtra("f_id", stepLast.getF_id());
        mtent.putExtra("f_process_id", stepLast.getF_process_id());
        mtent.putExtra("f_file_upload", stepLast.getProcedure().getF_upload_file());
        mtent.putExtra("f_procedure_id", stepLast.getF_procedure_id());
        mtent.putExtra("f_todo_id", stepLast.getF_todo_id());


        mtent.putExtra("f_plugin_id", stepOne.getF_plugin_id());
        mtent.putExtra("f_form_id", stepOne.getF_form_id());

        checkDuration(mtent);

        startActivity(mtent);
    }

    private void checkDuration(Intent mtent) {

        if (stepLast != null) {

            String type = stepLast.getProcedure().getF_duration_type();

            String durTt = "";

            switch (type) {

                case "h":
                    durTt = "小时";
                    break;

                case "d":
                    durTt = "天";
                    break;

                case "m":
                    durTt = "分";
                    break;

            }

            String duation = stepLast.getProcedure().getF_duration();
            mtent.putExtra("duration", duation + durTt);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == 0x97) {
            //审核关闭 当前页
            finish();
        }

    }

    @OnClick(R.id.right)
    public void confirm() {
        Intent mtent = getIntent();
        mtent.setClass(mContext, CheckActivity.class);
        mtent.putExtra("state", "1");
        mtent.putExtra("title", content);

        mtent.putExtra("f_id", stepLast.getF_id());
        mtent.putExtra("f_process_id", stepLast.getF_process_id());
        mtent.putExtra("f_file_upload", stepLast.getProcedure().getF_upload_file());
        mtent.putExtra("f_procedure_id", stepLast.getF_procedure_id());
        mtent.putExtra("f_plugin_id", stepOne.getF_plugin_id());
        mtent.putExtra("f_form_id", stepOne.getF_form_id());
        mtent.putExtra("f_todo_id", stepLast.getF_todo_id());


        checkDuration(mtent);

        startActivity(mtent);
    }


}



