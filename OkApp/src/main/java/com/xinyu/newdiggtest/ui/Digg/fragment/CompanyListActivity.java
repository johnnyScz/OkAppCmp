package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录公司列表
 */
public class CompanyListActivity extends BaseNoEventActivity {

//    CompListAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_company_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();

        initData();
    }

    private void initData() {

//        final List<CompBean> data = creatDatas();

//        adapter = new CompListAdapter(R.layout.item_company, AppContacts.myCompanyList);
//        recyView.setAdapter(adapter);
//
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                JSONObject bean = AppContacts.myCompanyList.get(position);
//
//                Intent intent = new Intent(mContext, HomeAppActivity.class);
//
//                try {
//
//                    String cmpType = bean.getString("company_type");
//                    intent.putExtra("company_type", cmpType);//0 个人 1 公司
//                    PreferenceUtil.getInstance(mContext).setCompanyType(cmpType);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                startActivity(intent);
//
//                saveInfo(bean);
//
//                finish();
//
//            }
//        });


    }

    private void saveInfo(JSONObject data) {

        try {
            PreferenceUtil.getInstance(mContext).setCompanyId(data.getString("id"));//公司Id


            JSONObject outer = data.getJSONArray("userinfo").getJSONObject(0);

            String sid = outer.getString("session_id");

            JSONObject myself = outer.getJSONObject("user");

            if (myself == null)
                return;

            PreferenceUtil.getInstance(mContext).setWxSession(sid);
            PreferenceUtil.getInstance(mContext).setUserId(myself.getString("user_id"));
            PreferenceUtil.getInstance(mContext).setNickName(myself.getString("name"));
            PreferenceUtil.getInstance(mContext).setMobilePhone(myself.getString("mobile"));
            PreferenceUtil.getInstance(mContext).setHeadUrl(myself.getString("head"));


            if (data.toString().contains("province")) {
                PreferenceUtil.getInstance(mContext).setAdress(myself.getString("province") + " " + myself.getString("city"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


}




