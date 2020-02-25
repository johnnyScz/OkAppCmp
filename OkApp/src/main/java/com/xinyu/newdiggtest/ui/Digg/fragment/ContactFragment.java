package com.xinyu.newdiggtest.ui.Digg.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.xinyu.newdiggtest.R;

import com.xinyu.newdiggtest.adapter.QunzuAdapter;
import com.xinyu.newdiggtest.adapter.SearchContactAdapter;
import com.xinyu.newdiggtest.adapter.viewhelper.TreeActivity;
import com.xinyu.newdiggtest.bean.CardFilterBean;
import com.xinyu.newdiggtest.bean.CommonUserBean;
import com.xinyu.newdiggtest.bean.CompanyUserBean;
import com.xinyu.newdiggtest.bean.ContactBean;
import com.xinyu.newdiggtest.bean.ContactSearchBean;
import com.xinyu.newdiggtest.bean.FocusTodoBean;
import com.xinyu.newdiggtest.bean.ImImgBean;
import com.xinyu.newdiggtest.bean.UploadUrlBean;
import com.xinyu.newdiggtest.net.AppUrl;
import com.xinyu.newdiggtest.net.NetApi;
import com.xinyu.newdiggtest.net.RetrofitServiceManager;
import com.xinyu.newdiggtest.net.bean.CardBean;
import com.xinyu.newdiggtest.net.bean.CardInsertBean;
import com.xinyu.newdiggtest.net.bean.CmpCardBean;
import com.xinyu.newdiggtest.ui.App;
import com.xinyu.newdiggtest.ui.XshellEvent;
import com.xinyu.newdiggtest.utils.AppContacts;
import com.xinyu.newdiggtest.utils.DisplayUtils;
import com.xinyu.newdiggtest.utils.EventConts;
import com.xinyu.newdiggtest.utils.MyTextUtil;
import com.xinyu.newdiggtest.utils.PhotoDialog;
import com.xinyu.newdiggtest.utils.PreferenceUtil;
import com.xinyu.newdiggtest.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class ContactFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyView;

    @BindView(R.id.search_recycle)
    RecyclerView search_recycle;


    @BindView(R.id.insert)
    ImageView insert;

    @BindView(R.id.input)
    EditText input;


    List<FocusTodoBean> companyList;//本地companyList


    QunzuAdapter adapter;

    SearchContactAdapter searchContactAdapter;

    List<ContactBean> searchData = new ArrayList<>();


    private final int PICT_RESULT = 1;

    private final int CAMARA = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        if (companyList == null) {
            String info = PreferenceUtil.getInstance(mContext).getCompanyInfo();
            if (!MyTextUtil.isEmpty(info)) {
                try {
                    JSONArray array = new JSONArray(info);
                    getCompanyData(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(App.mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        initPop();

        initSearch();

    }


    private void initSearch() {


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence key, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!MyTextUtil.isEmpty(s.toString().trim())) {
                    checkSearchKey(s.toString());
                } else {
                    recyView.setVisibility(View.VISIBLE);
                    search_recycle.setVisibility(View.GONE);
                }

            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(App.mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        search_recycle.setLayoutManager(llm);//给RecyclerView设置适配器

        searchContactAdapter = new SearchContactAdapter(searchData);
        search_recycle.setAdapter(searchContactAdapter);

        searchContactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                ContactBean item = (ContactBean) adapter.getData().get(position);

                if (PreferenceUtil.getInstance(mContext).getUserId().equals(item.getUser_id()))
                    return;

                Intent mintent = new Intent(mContext, MyselfTreeActivity.class);
                mintent.putExtra("userId", item.getUser_id());
                mContext.startActivity(mintent);


            }
        });


    }


    /**
     * 请求
     *
     * @param key
     */

    public void checkSearchKey(String key) {

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());
        requsMap.put("name", MyTextUtil.getUrl1Encoe(key));

        url.searchPerson(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContactSearchBean>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContactSearchBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ContactSearchBean.SearchBean datas = msg.getData();

                            if (datas == null) {
                                return;
                            }
                            handleData(datas);


                        } else {
                            ToastUtils.getInstanc().showToast("搜索服务异常!");
                        }
                    }
                });
    }

    private void handleData(ContactSearchBean.SearchBean datas) {

        searchData.clear();
        if ((datas.getUser_friend() == null || datas.getUser_friend().size() < 1) &&
                (datas.getCard() == null || datas.getCard().size() < 1)
                && (datas.getCompany_user() == null || datas.getCompany_user().size() < 1)) {

            return;
        }

        if (datas.getCompany_user() != null && datas.getCompany_user().size() > 0) {

            List<CompanyUserBean> compList = datas.getCompany_user();

            for (CompanyUserBean item : compList) {

                String cmpName = item.getCompany_name();
                ContactBean ccBean = new ContactBean();
                ccBean.setItemType(1);
                ccBean.setName(cmpName);
                searchData.add(ccBean);

                if (item.getUser_list() != null && item.getUser_list().size() > 0) {
                    insertPerson(item.getUser_list());
                }
            }
        }


        if (datas.getUser_friend() != null && datas.getUser_friend().size() > 0) {

            ContactBean ccBean = new ContactBean();
            ccBean.setItemType(1);
            ccBean.setName("好友");
            searchData.add(ccBean);

            List<CommonUserBean> friend = datas.getUser_friend();

            for (CommonUserBean item : friend) {
                ContactBean pp = new ContactBean();
                pp.setItemType(0);
                pp.setNickname(item.getNickname());
                pp.setHead(item.getHead());
                pp.setUser_id(item.getUser_id());
                searchData.add(pp);
            }
        }

        if (datas.getCard() != null && datas.getCard().size() > 0) {

            ContactBean ccBean = new ContactBean();
            ccBean.setItemType(1);
            ccBean.setName("名片");
            searchData.add(ccBean);

            List<ContactSearchBean.SearchBean.CardBean> cardList = datas.getCard();
            for (ContactSearchBean.SearchBean.CardBean card : cardList) {
                ContactBean dd = new ContactBean();
                dd.setItemType(0);
                dd.setNickname(card.getF_name());
                dd.setUser_id(card.getF_card_user_id());
                dd.setF_type("1");
                searchData.add(dd);
            }

        }

        if (searchData.size() > 0) {
            recyView.setVisibility(View.GONE);
            search_recycle.setVisibility(View.VISIBLE);
        } else {
            recyView.setVisibility(View.VISIBLE);
            search_recycle.setVisibility(View.GONE);
        }
        searchContactAdapter.notifyDataSetChanged();


    }

    private void insertPerson(List<CommonUserBean> user_list) {

        for (CommonUserBean tt : user_list) {
            ContactBean ccBean = new ContactBean();
            ccBean.setItemType(0);
            ccBean.setNickname(tt.getNickname());
            ccBean.setUser_id(tt.getUser_id());
            ccBean.setHead(tt.getHead());
            searchData.add(ccBean);
        }

    }


    private void initPop() {

        LinearLayout view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.pop_more, null);
//点击事件
        setPopListner(view);
        addPop = new PopupWindow(view, DisplayUtils.dp2px(mContext, 100),
                DisplayUtils.dp2px(mContext, 75));

        addPop.setTouchable(true);
        addPop.setOutsideTouchable(true);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addPop.isShowing()) {

                    addPop.showAsDropDown(insert, (DisplayUtils.dp2px(mContext, 10)),
                            DisplayUtils.dp2px(mContext, 20));

                } else {
                    addPop.dismiss();
                }

            }
        });


    }


    private void getCompanyData(JSONArray child) {

        List<JSONObject> jsonList = new ArrayList<>();
        jsonList.clear();

        try {
            if (child != null && child.length() > 0) {

                int len = child.length();
                for (int i = 0; i < len; i++) {
                    jsonList.add(child.getJSONObject(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonList != null && jsonList.size() > 0) {

            companyList = new ArrayList<>();

//            companyList.add(createMsgBean());

            for (JSONObject item : jsonList) {
                FocusTodoBean todo = new FocusTodoBean();
                todo.setItemType(1);
                todo.setCmpBean(item);
                companyList.add(todo);
            }


        }


    }


    @Override
    protected void loadData() {
        if (adapter == null || adapter.getData().size() < 1) {
            queryCmpCard(0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        queryCmpCard(1);
        input.getText().clear();
    }

    @Override
    protected void reLoadData() {

    }

    @Override
    protected int getResId() {
        return R.layout.fragment_qunzu;
    }


    public void queryCmpCard(final int tag) {

        if (tag < 1)
            dialog.show();

        final RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi url = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();

        requsMap.put("sid", PreferenceUtil.getInstance(App.mContext).getSessonId());

        url.queryCardGroup(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CmpCardBean>() {
                    @Override
                    public void onCompleted() {

                        if (dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onNext(CmpCardBean msg) {


                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null && msg.getData().size() > 0) {


                                List<CardBean> datas = msg.getData();

                                List<FocusTodoBean> requstDatas = create(datas);

                                requstDatas.addAll(0, companyList);

                                freshDatas(requstDatas);

                            } else {
                                Log.e("amtf", "联系人没有数据");
                            }
                        } else {
                            Log.e("amtf", "联系人服务异常");
                        }
                    }
                });
    }

    private List<FocusTodoBean> create(List<CardBean> datas) {

        List<FocusTodoBean> cardList = new ArrayList<>();

        for (CardBean item : datas) {
            FocusTodoBean child = new FocusTodoBean();
            child.setCardBean(item);
            cardList.add(child);
        }


        return cardList;
    }

    private void freshDatas(List<FocusTodoBean> totalList) {

        recyView.removeAllViews();
        if (totalList.size() < 1)
            return;

        adapter = new QunzuAdapter(totalList);
        recyView.setAdapter(adapter);


        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                FocusTodoBean item = (FocusTodoBean) adapter.getData().get(position);

                if (item.getItemType() == 1) {

                    JSONObject object = item.getCmpBean();

                    try {

                        String type = object.getString("company_type");

                        if (type.equals("1")) {

                            String id = object.getString("id");
                            Intent intent = new Intent(mContext, TreeActivity.class);
                            intent.putExtra("companyId", id);
                            startActivity(intent);

                        } else {

                            String userid = object.getJSONArray("userinfo").getJSONObject(0).getString("user_id");
                            Intent intent = new Intent(mContext, PersonFriendListActivity.class);
                            intent.putExtra("userId", userid);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else if (item.getItemType() == 0) {


                    Intent intent = new Intent(mContext, CardListActivity.class);
                    String fid = item.getCardBean().getId();
                    intent.putExtra("f_id", fid);
                    intent.putExtra("title", item.getCardBean().getName());
                    startActivity(intent);


                } else if (item.getItemType() == 2) {

                    startActivity(new Intent(mContext, FeixinActivity.class));

                }

            }
        });

    }


    /**
     * 每隔20秒轮训一次
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onXshellEvent(XshellEvent event) {
        if (event.what == EventConts.Service_Timer) {
            if (!AppContacts.isBackrond) {
                if (isVisble) {

                    Log.w("amtf", "轮训联系人页面");
                }
            }
        }

    }


    //--------------------------------弹框拍照相关----------------------------------

    PopupWindow addPop;


    private void setPopListner(LinearLayout view) {

        view.findViewById(R.id.create_todo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPop.dismiss();
                startActivity(new Intent(mContext, InsertCardActivity.class));

            }
        });

        view.findViewById(R.id.look_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPop.dismiss();
                new PhotoDialog(mContext);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICT_RESULT:
                    // 图片、视频、音频选择结果回调

                    LocalMedia pictu = PictureSelector.obtainMultipleResult(data).get(0);

                    String imgpath = pictu.getCompressPath();

                    uploadImg(imgpath);


                    break;
                case CAMARA:

                    LocalMedia phto = PictureSelector.obtainMultipleResult(data).get(0);

                    String phtopath = phto.getCompressPath();

                    uploadImg(phtopath);

                    break;

            }
        }


    }


    /**
     * 上传图片
     *
     * @param phtopath
     */
    private void uploadImg(String phtopath) {

        File file = new File(phtopath);

        dialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM).addFormDataPart("TOKEN", PreferenceUtil.getInstance(mContext).getSessonId());
//                .addFormDataPart(TOKEN, token);自定义参数key常量类，即参数名

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), phtopath);
        builder.addFormDataPart("imgfile", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance(2);
        AppUrl url = manager.create(AppUrl.class);
        url.uploadOkImgs(parts).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UploadUrlBean>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());
                    }

                    @Override
                    public void onNext(UploadUrlBean res) {

                        if (res.getErrno() != 0) {
                            ToastUtils.getInstanc(mContext).showToast("文件上传失败");
                            return;
                        }

                        List<ImImgBean> data = res.getData();
                        if (data == null || data.size() < 1) {
                            return;
                        }

                        String upLoadUrl = data.get(0).getOriginal();

                        filterImg(upLoadUrl);


                    }
                });
    }


    /**
     * 过滤名片
     */
    public void filterImg(String fname) {
        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi netApi = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("fname", fname);
        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        netApi.queryCardInfo(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CardFilterBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CardFilterBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {

                            if (msg.getData() != null) {

                                filterCard(msg.getData());

                            }


                        } else {
                            ToastUtils.getInstanc().showToast("文字提取失败!");
                        }


                    }
                });
    }

    private void filterCard(CardFilterBean.CardBean data) {


        if (MyTextUtil.isEmpty(data.getF_name()) && MyTextUtil.isEmpty(data.getF_organization_name())) {
            ToastUtils.getInstanc().showToast("名片录入失败");
            return;

        }

        doCommitParams(data);
    }

    private void doCommitParams(CardFilterBean.CardBean data) {

        RetrofitServiceManager manager = RetrofitServiceManager.getInstance();
        NetApi netApi = manager.create(NetApi.class);
        HashMap<String, String> requsMap = new HashMap<>();
        requsMap.put("f_company_id", PreferenceUtil.getInstance(mContext).getCompanyId());
        requsMap.put("f_name", data.getF_name());
        requsMap.put("f_organization_name", data.getF_organization_name());
        requsMap.put("f_mobile", data.getF_mobile());
        requsMap.put("f_position", data.getF_position());

        requsMap.put("f_email", data.getF_email());
        requsMap.put("f_addredss", data.getF_addredss());
        requsMap.put("f_homepage", data.getF_homepage());

        requsMap.put("f_plugin_type_id", "103");


        requsMap.put("sid", PreferenceUtil.getInstance(mContext).getSessonId());

        netApi.insertCard(requsMap).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CardInsertBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("amtf", e.getMessage());

                    }

                    @Override
                    public void onNext(CardInsertBean msg) {

                        if (msg.getOp().getCode().equals("Y")) {
                            ToastUtils.getInstanc().showToast("名片插入成功");
                        } else {
                            ToastUtils.getInstanc().showToast("名片插入失败!");
                        }

                    }
                });
    }

    //--------------------搜索相关-----------------------

    @OnClick(R.id.doserch)
    public void goSearch() {

    }


}
