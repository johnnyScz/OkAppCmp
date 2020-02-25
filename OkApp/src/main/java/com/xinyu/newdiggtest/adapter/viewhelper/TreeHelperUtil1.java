package com.xinyu.newdiggtest.adapter.viewhelper;

import android.content.Context;
import android.util.Log;

import com.xinyu.newdiggtest.bean.MemberRetBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TreeHelperUtil1 {

    private volatile static TreeHelperUtil1 mSingleton = null;


    List<String> fids = new ArrayList<>();
    Map<String, List<MemberRetBean.MemberOutBean>> allDatas = new HashMap<>();

    List<TreeBean.TreeListBean> firstLevelDatas = new ArrayList<>();


    List<MemberRetBean.MemberOutBean> totolMembers;

    ExecutorService excutor = Executors.newCachedThreadPool();


    int count = 0;

    int requstCount = 0;

    public List<TreeBean.TreeListBean> getFirstLevelDatas() {

        return firstLevelDatas;
    }


    private TreeHelperUtil1() {
        fids.clear();
        allDatas.clear();
        firstLevelDatas.clear();
    }


    public static TreeHelperUtil1 getInstance(Context mcx) {

        if (mSingleton == null) {
            synchronized (TreeHelperUtil1.class) {
                if (mSingleton == null) {
                    mSingleton = new TreeHelperUtil1();
                }
            }
        }
        return mSingleton;

    }

    public List<MemberRetBean.MemberOutBean> getTotolMembers() {
        return totolMembers;
    }

    public void requestGroupDatas(final Context context, String cmpnyId) {

        fids.clear();
        allDatas.clear();
        firstLevelDatas.clear();
        requstCount = 0;

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(context).getSessonId());
        map.put("f_company_id", cmpnyId);

        url.queryCompanyTotal(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TreeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(TreeBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            firstLevelDatas = msg.getData();

                            if (firstLevelDatas == null || firstLevelDatas.size() < 1)
                                return;


                            for (TreeBean.TreeListBean item : firstLevelDatas) {
                                if (!MyTextUtil.isEmpty(item.getF_id())) {
                                    fids.add(item.getF_id());
                                }

                                if (item.getChild() != null && item.getChild().size() > 0) {

                                    List<TreeBean.TreeListBean.ChildBean> chCC = item.getChild();

                                    for (TreeBean.TreeListBean.ChildBean cC : chCC) {

                                        if (!MyTextUtil.isEmpty(cC.getF_id())) {
                                            fids.add(cC.getF_id());
                                        }
                                    }
                                }
                            }
                        }


                        if (fids.size() > 0) {

                            count = fids.size();

                            excutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    askAllFidSave();
                                }
                            });
                        }
                    }
                });
    }


    private void askAllFidSave() {

        for (String item : fids) {

            requestMembers(item);

        }

    }


    public void requestMembers(final String fid) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        AppUrl url = manager.create(AppUrl.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        map.put("f_company_id", PreferenceUtil.getInstance(App.mContext).getCompanyId());
        map.put("orgId", fid);

        url.queryLevel2Member(map).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MemberRetBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", "服务onError群组查询请求异常:" + e.getMessage());
                    }

                    @Override
                    public void onNext(MemberRetBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            requstCount++;

                            if (msg.getData() != null && msg.getData().size() > 0) {

                                if (fid.equals("-2")) {
                                    totolMembers = msg.getData();
                                }

                                saveDatas(fid, msg.getData());

                            } else {
                                Log.e("amtf", "fid-->" + fid + "未请求到数据");
                            }


                        } else {
                            Log.e("amtf", "群组查询请求异常");
                        }


                    }
                });
    }


    private void saveDatas(String fid, List<MemberRetBean.MemberOutBean> data) {

        allDatas.put(fid, data);

        if (requstCount == count) {

            EventBus.getDefault().postSticky(new XshellEvent(EventConts.Contact_Finish));
        }


    }


    public Map<String, List<MemberRetBean.MemberOutBean>> getMapList() {
        return allDatas;
    }

}
