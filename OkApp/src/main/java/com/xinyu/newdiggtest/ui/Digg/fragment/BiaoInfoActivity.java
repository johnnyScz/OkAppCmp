package com.xinyu.newdiggtest.ui.Digg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FormContactAdapter;
import com.xinyu.newdiggtest.adapter.FormInfoAdapter;
import com.xinyu.newdiggtest.adapter.MomAdapter;
import com.xinyu.newdiggtest.adapter.YunAdapter;
import com.xinyu.newdiggtest.bean.AliYunBean;
import com.xinyu.newdiggtest.bean.BiaodanInfoBean;
import com.xinyu.newdiggtest.bean.FormItemBean;
import com.xinyu.newdiggtest.bean.MomBean;
import com.xinyu.newdiggtest.bean.PugRigester;
import com.xinyu.newdiggtest.bean.YunBean;
import com.xinyu.newdiggtest.net.ApiConfig;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 表单详情页
 */
public class BiaoInfoActivity extends BaseNoEventActivity {

    @BindView(R.id.title)
    TextView title;


    @BindView(R.id.parent)
    LinearLayout parent;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_biao_info;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String titleStr = getIntent().getStringExtra("f_title");

        title.setText(titleStr);


        initView();
    }

    private void initView() {


        String pluginId = getIntent().getStringExtra("f_plugin_type_id");
        String groId = getIntent().getStringExtra("f_group_id");


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器

        queryInfo(pluginId, groId);


    }

    @OnClick(R.id.icon_back)
    public void goCommit() {

        finish();
    }


    public void queryInfo(String plugId, String grId) {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("f_plugin_type_id", plugId);
        requsMap.put("f_group_id", grId);

        url.getBiaoInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(Object msg) {


                        String json = new Gson().toJson(msg);

                        try {
                            JSONObject object = new JSONObject(json);

                            String code = object.getJSONObject("op").getString("code");

                            if (code.equals("Y")) {
                                checkMutitypeJson(object);
                            } else {
                                ToastUtils.getInstanc().showToast("服务异常");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    private void checkMutitypeJson(JSONObject json) {


        String scope = getIntent().getStringExtra("f_scope");

        if (scope.equals("market")) {
            if (getIntent().getStringExtra("f_plugin_type_id").equals("100")) {//销售机会

                chance(json);

            } else if (getIntent().getStringExtra("f_plugin_type_id").equals("101")) {//意向

                saleIntent(json);

            } else if (getIntent().getStringExtra("f_plugin_type_id").equals("102")) {//销售签约
                saleSign(json);

            }
        }

        if (scope.equals("mom")) {
            mom(json);
        } else if (scope.equals("other") || scope.equals("custom")) {
            aliRes(json);
        }

    }


    /**
     * Mom 格式的表单
     *
     * @param json
     */
    private void mom(JSONObject json) {


        Gson gson = new Gson();


        final MomBean retudata = gson.fromJson(json.toString(), MomBean.class);

        if (retudata.getData() != null && retudata.getData().size() > 0) {
            MomAdapter adapter = new MomAdapter(R.layout.item_form_mom, retudata.getData());
            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


                    if (retudata.getPlugin_register() == null)
                        return;

                    goCommentH5(retudata.getPlugin_register().getF_id(), retudata.getPlugin_register().getF_plugin_type_id(),
                            retudata.getPlugin_register().getF_create_by(), retudata.getPlugin_register());
                }
            });


        } else {
            showEmpty();
        }


    }

    /**
     * 显示空页面
     */
    private void showEmpty() {
        parent.setVisibility(View.GONE);
    }


    /**
     * cutom跟other类型
     * <p>
     * 编号 创建人  时间
     *
     * @param json
     */
    private void aliRes(JSONObject json) {

        Gson gson = new Gson();
        final AliYunBean retudata = gson.fromJson(json.toString(), AliYunBean.class);

        List<YunBean> yunList = retudata.getData();


        if (yunList != null && yunList.size() > 0) {

            final YunAdapter adapter = new YunAdapter(R.layout.item_form_ali, yunList);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {

                    YunBean yun = adapter.getData().get(position);
                    Intent intent = getIntent();

                    intent.setClass(mContext, BdWebViewActivity.class);

                    intent.putExtra("chanel_plugin_type_id", retudata.getPlugin().getF_plugin_type_id());
                    intent.putExtra("contentId", yun.getF_id());


                    String url = "";

                    String scope = intent.getStringExtra("f_scope");

                    if (scope.equals("other") || scope.equals("custom")) {

                        String f_detail = retudata.getPlugin_register().getF_details_url();
                        String otherParam = f_detail + "&id=" + yun.getF_id() + "&title=" + MyTextUtil.getUrl1Encoe(retudata.getPlugin_register().getF_title());
                        url = otherParam;

                    }

                    Log.e("amtf", "other的url:" + url);

                    intent.putExtra("url", url);

                    startActivity(intent);
                }
            });

        } else {
            showEmpty();
        }


    }

    private void saleSign(JSONObject json) {


        Gson gson = new Gson();
        final BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);

        if (retudata.getData() != null && retudata.getData().size() > 0) {
            FormContactAdapter adapter = new FormContactAdapter(R.layout.item_form_sign, retudata.getData());
            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);

                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), dt.getF_owner());

                }
            });


        } else {
            showEmpty();
        }


    }

    private void saleIntent(JSONObject json) {

        Gson gson = new Gson();
        BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);

        if (retudata.getData() != null && retudata.getData().size() > 0) {
            FormInfoAdapter adapter = new FormInfoAdapter(R.layout.item_form_xiaoshou, retudata.getData());
            recyclerView.setAdapter(adapter);

            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);

                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), dt.getF_owner());

                }
            });

        } else {
            showEmpty();
        }

    }

    private void chance(JSONObject json) {

        Gson gson = new Gson();


        BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);

        if (retudata.getData() != null && retudata.getData().size() > 0) {
            FormInfoAdapter adapter = new FormInfoAdapter(R.layout.item_form_xiaoshou, retudata.getData());
            adapter.setFormType(1);
            recyclerView.setAdapter(adapter);


            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);

                    String owner = dt.getF_owner();

                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), owner);

                }
            });


        } else {
            showEmpty();
        }
    }

    private void goCommentH5(String f_plugin_id, String f_plugin_type_id, String owner, PugRigester plugin_register) {

        Intent intent = getIntent();

        intent.setClass(mContext, BdWebViewActivity.class);

        intent.putExtra("chanel_plugin_type_id", f_plugin_type_id);
        intent.putExtra("contentId", f_plugin_id);

        String object = createData(f_plugin_id, f_plugin_type_id, owner);

        String url = ApiConfig.BDUrl + "from=android" + "&data=" + object;
        intent.putExtra("url", url);


        String scope = intent.getStringExtra("f_scope");

        if (scope.equals("other") || scope.equals("custom")) {
            String otherParam = "&f_details_url=" + plugin_register.getF_details_url() + "&id=" + plugin_register.getF_id() + "&title=" + MyTextUtil.getUrl1Encoe(plugin_register.getF_title());
            intent.putExtra("detail", otherParam);
        }

        startActivity(intent);

    }


    private void goCommentH5(String f_plugin_id, String f_plugin_type_id, String owner) {

        Intent intent = getIntent();

        intent.setClass(mContext, BdWebViewActivity.class);

        intent.putExtra("chanel_plugin_type_id", f_plugin_type_id);
        intent.putExtra("contentId", f_plugin_id);

        String object = createData(f_plugin_id, f_plugin_type_id, owner);

        String url = ApiConfig.BDUrl + "from=android" + "&data=" + object;
        intent.putExtra("url", url);


        String scope = intent.getStringExtra("f_scope");

        if (scope.equals("other") || scope.equals("custom")) {
            intent.putExtra("detail", f_plugin_id);
        }

        startActivity(intent);

    }


    /**
     * @param f_plugin_id
     * @param f_plugin_type_id
     * @return
     */
    private String createData(String f_plugin_id, String f_plugin_type_id, String owner) {

        StringBuffer buffer = new StringBuffer();

        buffer.append("%7B%22").append("sid").append("%22%3A%22").
                append(PreferenceUtil.getInstance(mContext).getSessonId()).append("%22").append(",")
                .append("%22id%22%3A%22").append(f_plugin_id)
                .append("%22,%22uid%22%3A%22").append(PreferenceUtil.getInstance(mContext).getUserId()).append("%22, %22formName%22%3A%22")
                .append(getInType())
                .append("%22,%22owner%22%3A%22").append(owner).append("%22,%22pluginType%22%3A%22").append(f_plugin_type_id).append("%22%7D");

        return buffer.toString();
    }

    private String getInType() {
        String type = getIntent().getStringExtra("f_scope");

        if (type.equals("market")) {

            return "market";

        } else if (type.equals("mom")) {
            return "mom";

        } else if (type.equals("other") || type.equals("custom")) {
            return "other";

        } else {
            return "file";
        }

    }


}






