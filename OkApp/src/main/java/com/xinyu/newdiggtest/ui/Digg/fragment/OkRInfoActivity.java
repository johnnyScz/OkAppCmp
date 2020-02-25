package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.adapter.OkRInfoAdapter;
import com.xinyu.newdiggtest.bean.AfairBean;
import com.xinyu.newdiggtest.bean.FunctionBean;
import com.xinyu.newdiggtest.bean.PermissionBean;
import com.xinyu.newdiggtest.bean.Plugins;
import com.xinyu.newdiggtest.bean.PugRigester;
import com.xinyu.newdiggtest.h5.WebViewUrlActivity;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseActivity;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.ui.chat.ChatCompanyActivity;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKR 详情页
 */
public class OkRInfoActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;


    @BindView(R.id.ids)
    public TextView tXids;

    @BindView(R.id.fill_net)
    public LinearLayout fill_net;

    @BindView(R.id.top2)
    public LinearLayout top2;


    @BindView(R.id.title)
    public TextView title;


    Gson gson = new Gson();

    OkRInfoAdapter adapter;

    List<Plugins> pluginList;


    List<FunctionBean> functionList;

    List<String> pluginPermiss;//对应 function

    List<PermissionBean.FormPremissionBean> formPermiss;//对应plugins

    String isOkr = "false";

    AfairBean.AfairChildBean data;

    StringBuffer funIds;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_okr_info;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fid = getIntent().getStringExtra("f_id");


        initView();

    }

    String fid = "";

    private void initView() {


        getPermission(fid);

        GridLayoutManager mgr = new GridLayoutManager(mContext, 6);

        recyclerView.setLayoutManager(mgr);
        recyclerView.setNestedScrollingEnabled(false);


    }


    public void queryAffairInfo(String f_id) {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(App.mContext).getUserId());
        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("f_id", f_id);


        url.checkAffair(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AfairBean>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(AfairBean msg) {


                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                List<AfairBean.AfairChildBean> total = msg.getData();

                                if (total != null && total.size() > 0) {

                                    showInfo(total);

                                }
                            }
                        }
                    }
                });
    }


    private void showInfo(List<AfairBean.AfairChildBean> total) {
        data = total.get(0);

        groupId = data.getF_id();

        tXids.setText("群组ID:" + data.getF_id());

        if (data.getGroup_info() != null && data.getGroup_info().size() > 0) {

            adapter = new OkRInfoAdapter(R.layout.item_member, data.getGroup_info());
            recyclerView.setAdapter(adapter);
        }

        Object okr = data.getOkr();
        if (okr instanceof String) {
            isOkr = "false";
        } else {
            String okrStr = new Gson().toJson(okr);
            if (okrStr.contains("f_id")) {
                isOkr = "true";
            }
        }

        title.setText(data.getF_title());

        String funciton = new Gson().toJson(data.getFunctions());


        if (funciton.contains("f_id")) {

            functionList = new ArrayList<>();

            try {
                JSONArray fucc = new JSONArray(funciton);
                int len = fucc.length();

                funIds = new StringBuffer();

                for (int i = 0; i < len; i++) {
                    funIds.append(fucc.getJSONObject(i).getString("f_function_id")).append(",");
                }

                for (int i = 0; i < len; i++) {
                    String object = fucc.getJSONObject(i).toString();
                    FunctionBean ffc = gson.fromJson(object, FunctionBean.class);
                    functionList.add(ffc);
                }

                if (functionList.size() > 0) {


                    List<FunctionBean> result = checkIfFuncion();//检查权限的插件

                    if (result == null || result.size() < 1) {
                        top2.setVisibility(View.GONE);
                    } else {
                        top2.setVisibility(View.VISIBLE);
                        fillTop(result);
                    }

                } else {
                    top2.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            top2.setVisibility(View.GONE);
        }


        String plugin = new Gson().toJson(data.getPlugins());

        if (plugin.contains("f_id")) {

            pluginList = new ArrayList<>();

            try {

                JSONArray jsonArray = new JSONArray(plugin);
                int len = jsonArray.length();

                for (int i = 0; i < len; i++) {
                    String object = jsonArray.getJSONObject(i).toString();
                    Plugins user = gson.fromJson(object, Plugins.class);
                    pluginList.add(user);
                }

                if (pluginList.size() > 0) {

                    List<Plugins> mResut = checkResult();
                    if (mResut == null || mResut.size() < 1) {
                        fill_net.setVisibility(View.GONE);
                    } else {
                        fill_net.setVisibility(View.VISIBLE);
                        fillFormDatas(mResut);
                    }
                } else {
                    fill_net.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            fill_net.setVisibility(View.GONE);
        }


    }

    private List<Plugins> checkResult() {

        List<Plugins> ppList = new ArrayList<>();

        if (formPermiss == null || formPermiss.size() < 1)
            return null;

        for (PermissionBean.FormPremissionBean it : formPermiss) {

            for (Plugins item : pluginList) {
                if (item.getF_plugin_type_id().equals(it.getPlugin_type_id())) {
                    int addPre = Integer.parseInt(it.getAdd_permission());
                    int lookPer = Integer.parseInt(it.getLook_permission());

                    item.setLookPermision(it.getLook_permission());

                    if ((addPre + lookPer) > 0) {
                        ppList.add(item);
                    }
                }
            }
        }

        return ppList;
    }

    private List<FunctionBean> checkIfFuncion() {
        List<FunctionBean> result = new ArrayList<>();

        if (pluginPermiss == null || pluginPermiss.size() < 1) {
            return null;
        }
        for (String item : pluginPermiss) {
            for (FunctionBean tt : functionList) {
                if (tt.getF_function_id().equals(item)) {
                    result.add(tt);
                }
            }
        }

        return result;
    }


    private void fillTop(List<FunctionBean> result) {

        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 45));

        for (final FunctionBean item : result) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_item_okr1, null);
            view.setTag(item);
            TextView content = view.findViewById(R.id.content);


            content.setText(checkId(item));


            if (item.getF_function_id().equals("1")) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goNaotu(item);
                    }
                });
            } else {
                if (MyTextUtil.isEmpty(item.getF_url())) {
                    view.setOnClickListener(this);
                } else {
                    view.setOnClickListener(urlListner);
                }

            }

            top2.addView(view, pp);
        }
    }


    View.OnClickListener urlListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FunctionBean item = (FunctionBean) v.getTag();
            Intent intent = new Intent(mContext, WebViewUrlActivity.class);
            intent.putExtra("fid", data.getCheck_id());

            String url = item.getF_url();
            String title = checkId(item);

            if (url.contains("http")) {
                intent.putExtra("newBrowserUrl", url);
            } else {
                String httpUrl = "http://" + url;
                intent.putExtra("newBrowserUrl", httpUrl);
            }

            intent.putExtra("title", title);
            startActivity(intent);
        }
    };


    /**
     * 去脑图
     *
     * @param item
     */
    private void goNaotu(FunctionBean item) {

        String nTUrl = ApiConfig.NaoTuUrl;

        String left = "?id=" + data.getCheck_id() + "&user_id=" + PreferenceUtil.getInstance(mContext).getUserId()
                + "&sid=" + PreferenceUtil.getInstance(mContext).getSessonId() + "&fatherTitle=" + MyTextUtil.getUrl1Encoe(data.getF_title())
                + "&title=" + MyTextUtil.getUrl1Encoe(data.getF_title());

        Intent intent = new Intent(mContext, WebViewUrlActivity.class);

        intent.putExtra("enter", "naotu");
        intent.putExtra("fid", data.getCheck_id());
        intent.putExtra("newBrowserUrl", nTUrl + left);

        startActivity(intent);
    }

    private String checkId(FunctionBean data) {

        String fuId = data.getF_function_id();

        if (fuId.equals("1")) {
            return "脑图";
        } else if (fuId.equals("2")) {
            return "项目管理";
        } else {
            return data.getF_function_name();
        }
    }


    Gson mJson = new Gson();

    /**
     * 填充form数据
     *
     * @param mResut
     */
    private void fillFormDatas(List<Plugins> mResut) {

        LinearLayout ll_parent = fill_net.findViewById(R.id.ll_parent);

        LinearLayout.LayoutParams pp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 45));

        for (Plugins item : mResut) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_item_okr, null);
            view.setTag(item);

            view.setOnClickListener(lookListner);

            TextView content = view.findViewById(R.id.content);
            String json = mJson.toJson(item.getPluginregister());

            try {
                JSONObject object = new JSONObject(json);
                content.setText(object.getString("f_title"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


            ll_parent.addView(view, pp);
        }


    }


    View.OnClickListener lookListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Plugins item = (Plugins) v.getTag();

            if (item.getLookPermision().equals("1")) {

                Intent intent = new Intent(mContext, BiaoInfoActivity.class);

                intent.putExtra("f_plugin_type_id", item.getF_plugin_type_id());
                intent.putExtra("f_group_id", item.getF_group_id());
                intent.putExtra("f_title", item.getF_plugin_type_name());

                Object json = item.getPluginregister();
                PugRigester ffc;
                if (json instanceof String) {
                    return;
                } else {
                    String object = new Gson().toJson(json);
                    ffc = gson.fromJson(object, PugRigester.class);
                }


                if (ffc == null || MyTextUtil.isEmpty(ffc.getF_scope())) {
                    ToastUtils.getInstanc().showToast("表单类型缺失!");
                    return;
                }

                intent.putExtra("f_scope", ffc.getF_scope());

                startActivity(intent);

            } else {
                ToastUtils.getInstanc().showToast("请下载PC版体验");
            }
        }
    };


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.more)
    public void goMore() {
        Intent intent = getIntent();

        if (funIds != null) {
            intent.putExtra("f_function_ids", funIds.toString());
        } else {
            intent.putExtra("f_function_ids", "");
        }

        intent.putExtra("f_plugins", getPluginStr());
        intent.putExtra("isokr", isOkr);

        intent.putExtra("f_start_date", data.getF_start_date());
        intent.putExtra("f_end_date", data.getF_end_date());


        intent.setClass(mContext, OkSecondActivity.class);
        startActivity(intent);
    }


    String groupId = "";


    private String getPluginStr() {

        if (pluginList == null || pluginList.size() < 1) {
            return "";
        }

        JSONArray array = new JSONArray();

        try {
            for (Plugins item : pluginList) {
                JSONObject object = new JSONObject();
                object.put("f_group_id", item.getF_id());


                object.put("f_plugin_type_id", item.getF_plugin_type_id());
                object.put("f_plugin_type_name", item.getF_plugin_type_name());
                array.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return array.toString();
    }


    @OnClick(R.id.ll_file)
    public void goFile() {

        Intent intent = getIntent();
        intent.setClass(mContext, FileActivity.class);
        intent.putExtra("group_id", groupId);
        startActivity(intent);


    }


    @OnClick(R.id.ll_feixin)
    public void goFeixin() {
        AfairBean.AfairChildBean.ChatInfoBean chat = data.getChat_info();

        if (chat != null) {
            Intent mIntent = new Intent(mContext, ChatCompanyActivity.class);
            mIntent.putExtra("room_id", chat.getRoom_id());
            mIntent.putExtra("room_name", chat.getRoom_name());
            mIntent.putExtra("room_type", chat.getRoom_type());
            mIntent.putExtra("creater", chat.getMain_object());

            startActivity(mIntent);
        }


    }


    @Override
    public void onClick(View v) {
        ToastUtils.getInstanc().showToast("请下载PC版体验");
    }


    public void getPermission(final String fid) {
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("user_id", PreferenceUtil.getInstance(App.mContext).getUserId());
        requsMap.put("group_id", fid);

        url.getPremission(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PermissionBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(PermissionBean msg) {


                        if (msg.getOp().getCode().equals("Y")) {

                            pluginPermiss = msg.getPlugin();
                            formPermiss = msg.getForm();
                            queryAffairInfo(fid);
                        }
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.MSG_Refresh_GName) {
            title.setText(event.msg);
        } else if (event.what == 0x66) {
            finish();
        }

    }


}




