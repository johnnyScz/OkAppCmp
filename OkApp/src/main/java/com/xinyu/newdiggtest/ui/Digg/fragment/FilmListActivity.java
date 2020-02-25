package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.FilmAdapter;
import com.xinyu.newdiggtest.bean.FilmBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.BaseNoEventActivity;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 电影列表
 */
public class FilmListActivity extends BaseNoEventActivity {

    FilmAdapter adapter;

    @BindView(R.id.recyclerView)
    public RecyclerView recyView;


    @BindView(R.id.emputview)
    public ImageView emptyView;


    @Override
    protected int getLayoutResouce() {
        return R.layout.activity_praise_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器
    }


    @Override
    protected void onStart() {
        super.onStart();
        quetyList();
    }


    public void quetyList() {
        //TODO 电影页面后面要把Useid放开


        loadingDailog.show();
        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("command", "ok-api.SelectMyMovie");
        map.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());
        map.put("user_id", /*PreferenceXshellUtil.getInstance(mContext).getUserId()*/"228");

        url.getFilm(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FilmBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDailog.dismiss();
                        Log.e("amtf", "onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(FilmBean msg) {
                        loadingDailog.dismiss();
                        if (msg.getOp().getCode().equals("Y")) {
                            List<FilmBean.FilmListBean> datas = msg.getData();
                            if (datas != null && datas.size() > 0) {
                                showEmpty(datas, false);
                            } else {
                                showEmpty(datas, true);
                            }


                        } else {
                            ToastUtils.getInstanc(mContext).showToast("服务异常");
                        }

                    }
                });
    }


    /**
     * 显示与隐藏
     *
     * @param datalist
     * @param show
     */
    private void showEmpty(List<FilmBean.FilmListBean> datalist, boolean show) {
        if (show) {
            emptyView.setVisibility(View.VISIBLE);
            recyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyView.setVisibility(View.VISIBLE);
            showData(datalist);
        }


    }

    /**
     * 显示数据
     *
     * @param datalist
     */
    private void showData(final List<FilmBean.FilmListBean> datalist) {
        adapter = new FilmAdapter(R.layout.item_film, datalist);
        recyView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                //TODO 点击事件
                FilmBean.FilmListBean data = datalist.get(position);

            }
        });
    }


    @OnClick(R.id.icon_back)
    public void goBack() {
        finish();
    }


}




