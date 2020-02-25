package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FriendAdapter;
import com.xinyu.newdiggtest.bean.PersonContactBean;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 个人通讯录
 */
public class PersonFriendListActivity extends BaseNoEventActivity {

    FriendAdapter adapter;

    FriendAdapter searchAdapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;

    @BindView(R.id.search_recycle)
    public RecyclerView search_recycle;

    @BindView(R.id.input)
    public EditText editText;

    List<PersonContactBean.FriendBean> totalDatas;

    List<PersonContactBean.FriendBean> searchList = new ArrayList<>();


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_friend_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
        queryFriend();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        //搜搜的适配器
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        search_recycle.setLayoutManager(llm);//给RecyclerView设置适配器

        searchAdapter = new FriendAdapter(R.layout.item_img_tx, searchList);
        search_recycle.setAdapter(searchAdapter);

        searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                PersonContactBean.FriendBean data = (PersonContactBean.FriendBean) adapter.getData().get(position);

                Intent mintent = new Intent(mContext, MyselfTreeActivity.class);


                if (PreferenceUtil.getInstance(mContext).getUserId().equals(data.getUser_id()))
                    return;
                mintent.putExtra("userId", data.getUser_id());

                mintent.putExtra("headUrl", data.getHead());

                startActivity(mintent);

            }
        });
        ;


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence key, int start, int before, int count) {

                if (!MyTextUtil.isEmpty(key.toString().trim())) {
                    searchList.clear();
                    checkSearchKey(key);
                } else {
                    recyView.setVisibility(View.VISIBLE);
                    search_recycle.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void checkSearchKey(CharSequence key) {

        for (PersonContactBean.FriendBean user : totalDatas) {

            String nameTx = user.getName();

            if (MyTextUtil.isEmpty(nameTx))
                continue;

            if (!MyTextUtil.isEmpty(nameTx) && nameTx.contains(key.toString())) {
                searchList.add(user);
            }
        }
        if (searchList.size() > 0) {
            recyView.setVisibility(View.GONE);
            search_recycle.setVisibility(View.VISIBLE);
            searchAdapter.setNewData(searchList);

        } else {
            recyView.setVisibility(View.VISIBLE);
            search_recycle.setVisibility(View.GONE);
        }
    }


    public void queryFriend() {

        loadingDailog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("user_id", getIntent().getStringExtra("userId"));

        url.getPersonContact(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PersonContactBean>() {
                    @Override
                    public void onCompleted() {

                        loadingDailog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PersonContactBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getFriend() != null && msg.getFriend().size() > 0) {
                                freshUI(msg.getFriend());
                            }

                        } else {
                            ToastUtils.getInstanc().showToast("获取好友服务异常!");
                        }
                    }
                });
    }


    /**
     * @param friend
     */
    private void freshUI(List<PersonContactBean.FriendBean> friend) {

        totalDatas = friend;

        adapter = new FriendAdapter(R.layout.item_img_tx, friend);
        recyView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                PersonContactBean.FriendBean data = (PersonContactBean.FriendBean) adapter.getData().get(position);



                if (PreferenceUtil.getInstance(mContext).getUserId().equals(data.getUser_id()))
                    return;

                Intent mintent = new Intent(mContext, MyselfTreeActivity.class);
                mintent.putExtra("userId", data.getUser_id());

                mintent.putExtra("headUrl", data.getHead());

                startActivity(mintent);

            }
        });
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


    @OnClick(R.id.doserch)
    public void doSearch() {

        if (MyTextUtil.isEmpty(editText.toString().trim())) {
            return;
        }

        checkSearchKey(editText.toString().trim());
    }


}




