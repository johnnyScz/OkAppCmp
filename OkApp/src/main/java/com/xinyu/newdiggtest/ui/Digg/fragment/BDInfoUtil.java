package com.xinyu.newdiggtest.ui.Digg.fragment;


/**
 * 表单详情页
 */
public class BDInfoUtil {

//
//    private void checkMutitypeJson(JSONObject json) {
//
//
//        String scope = getIntent().getStringExtra("f_scope");
//
//        if (scope.equals("market")) {
//            if (getIntent().getStringExtra("f_plugin_type_id").equals("100")) {//销售机会
//
//                chance(json);
//
//            } else if (getIntent().getStringExtra("f_plugin_type_id").equals("101")) {//意向
//
//                saleIntent(json);
//
//            } else if (getIntent().getStringExtra("f_plugin_type_id").equals("102")) {//销售签约
//                saleSign(json);
//
//            }
//        }
//
//        if (scope.equals("mom")) {
//            mom(json);
//        } else if (scope.equals("other") || scope.equals("custom")) {
//            aliRes(json);
//        }
//
//    }
//
//
//    /**
//     * Mom 格式的表单
//     *
//     * @param json
//     */
//    private void mom(JSONObject json) {
//
//
//        Gson gson = new Gson();
//
//
//        final MomBean retudata = gson.fromJson(json.toString(), MomBean.class);
//
//        if (retudata.getData() != null && retudata.getData().size() > 0) {
//            MomAdapter adapter = new MomAdapter(R.layout.item_form_mom, retudata.getData());
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                @Override
//                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//
//                    if (retudata.getPlugin_register() == null)
//                        return;
//
//                    goCommentH5(retudata.getPlugin_register().getF_id(), retudata.getPlugin_register().getF_plugin_type_id(),
//                            retudata.getPlugin_register().getF_create_by(), retudata.getPlugin_register());
//                }
//            });
//
//
//        } else {
//            showEmpty();
//        }
//
//
//    }
//
//
//    /**
//     * cutom跟other类型
//     * <p>
//     * 编号 创建人  时间
//     *
//     * @param json
//     */
//    private void aliRes(JSONObject json) {
//
//        Gson gson = new Gson();
//        final AliYunBean retudata = gson.fromJson(json.toString(), AliYunBean.class);
//
//        List<YunBean> yunList = retudata.getData();
//
//
//        if (yunList != null && yunList.size() > 0) {
//
//            final YunAdapter adapter = new YunAdapter(R.layout.item_form_ali, yunList);
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                @Override
//                public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {
//
//                    YunBean yun = adapter.getData().get(position);
//                    Intent intent = getIntent();
//
//                    intent.setClass(mContext, BdWebViewActivity.class);
//
//                    intent.putExtra("chanel_plugin_type_id", retudata.getPlugin().getF_plugin_type_id());
//                    intent.putExtra("contentId", yun.getF_id());
//
//
//                    String url = "";
//
//                    String scope = intent.getStringExtra("f_scope");
//
//                    if (scope.equals("other") || scope.equals("custom")) {
//
//                        String f_detail = retudata.getPlugin_register().getF_details_url();
//                        String otherParam = f_detail + "&id=" + yun.getF_id() + "&title=" + MyTextUtil.getUrl1Encoe(retudata.getPlugin_register().getF_title());
//                        url = otherParam;
//
//                    }
//
//                    Log.e("amtf", "other的url:" + url);
//
//                    intent.putExtra("url", url);
//
//                    startActivity(intent);
//                }
//            });
//
//        } else {
//            showEmpty();
//        }
//
//
//    }
//
//    private void saleSign(JSONObject json) {
//
//
//        Gson gson = new Gson();
//        final BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);
//
//        if (retudata.getData() != null && retudata.getData().size() > 0) {
//            FormContactAdapter adapter = new FormContactAdapter(R.layout.item_form_sign, retudata.getData());
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                @Override
//                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);
//
//                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), dt.getF_owner());
//
//                }
//            });
//
//
//        } else {
//            showEmpty();
//        }
//
//
//    }
//
//    private void saleIntent(JSONObject json) {
//
//        Gson gson = new Gson();
//        BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);
//
//        if (retudata.getData() != null && retudata.getData().size() > 0) {
//            FormInfoAdapter adapter = new FormInfoAdapter(R.layout.item_form_xiaoshou, retudata.getData());
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                @Override
//                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);
//
//                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), dt.getF_owner());
//
//                }
//            });
//
//        } else {
//            showEmpty();
//        }
//
//    }
//
//    private void chance(JSONObject json) {
//
//        Gson gson = new Gson();
//
//
//        BiaodanInfoBean retudata = gson.fromJson(json.toString(), BiaodanInfoBean.class);
//
//        if (retudata.getData() != null && retudata.getData().size() > 0) {
//            FormInfoAdapter adapter = new FormInfoAdapter(R.layout.item_form_xiaoshou, retudata.getData());
//            adapter.setFormType(1);
//            recyclerView.setAdapter(adapter);
//
//
//            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//                @Override
//                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//
//                    FormItemBean dt = (FormItemBean) adapter.getData().get(position);
//
//                    String owner = dt.getF_owner();
//
//                    goCommentH5(dt.getF_plugin_id(), dt.getF_plugin_type_id(), owner);
//
//                }
//            });
//
//
//        } else {
//            showEmpty();
//        }
//    }
//
//    private void goCommentH5(String f_plugin_id, String f_plugin_type_id, String owner, PugRigester plugin_register) {
//
//        Intent intent = getIntent();
//
//        intent.setClass(mContext, BdWebViewActivity.class);
//
//        intent.putExtra("chanel_plugin_type_id", f_plugin_type_id);
//        intent.putExtra("contentId", f_plugin_id);
//
//        String object = createData(f_plugin_id, f_plugin_type_id, owner);
//
//        String url = ApiConfig.BDUrl + "from=android" + "&data=" + object;
//        intent.putExtra("url", url);
//
//
//        String scope = intent.getStringExtra("f_scope");
//
//        if (scope.equals("other") || scope.equals("custom")) {
//            String otherParam = "&f_details_url=" + plugin_register.getF_details_url() + "&id=" + plugin_register.getF_id() + "&title=" + MyTextUtil.getUrl1Encoe(plugin_register.getF_title());
//            intent.putExtra("detail", otherParam);
//        }
//
//        startActivity(intent);
//
//    }
//
//
//    private void goCommentH5(String f_plugin_id, String f_plugin_type_id, String owner) {
//
//        Intent intent = getIntent();
//
//        intent.setClass(mContext, BdWebViewActivity.class);
//
//        intent.putExtra("chanel_plugin_type_id", f_plugin_type_id);
//        intent.putExtra("contentId", f_plugin_id);
//
//        String object = createData(f_plugin_id, f_plugin_type_id, owner);
//
//        String url = ApiConfig.BDUrl + "from=android" + "&data=" + object;
//        intent.putExtra("url", url);
//
//
//        String scope = intent.getStringExtra("f_scope");
//
//        if (scope.equals("other") || scope.equals("custom")) {
//            intent.putExtra("detail", f_plugin_id);
//        }
//
//        startActivity(intent);
//
//    }
//
//
//    /**
//     * @param f_plugin_id
//     * @param f_plugin_type_id
//     * @return
//     */
//    private String createData(String f_plugin_id, String f_plugin_type_id, String owner) {
//
//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append("%7B%22").append("sid").append("%22%3A%22").
//                append(PreferenceUtil.getInstance(mContext).getSessonId()).append("%22").append(",")
//                .append("%22id%22%3A%22").append(f_plugin_id)
//                .append("%22,%22uid%22%3A%22").append(PreferenceUtil.getInstance(mContext).getUserId()).append("%22, %22formName%22%3A%22")
//                .append(getInType())
//                .append("%22,%22owner%22%3A%22").append(owner).append("%22,%22pluginType%22%3A%22").append(f_plugin_type_id).append("%22%7D");
//
//        return buffer.toString();
//    }
//
//
//    /**
//     * type 即时scope
//     *
//     * @param type
//     * @return
//     */
//    private String getInType(String type) {
//
//
//        if (type.equals("market")) {
//
//            return "market";
//
//        } else if (type.equals("mom")) {
//            return "mom";
//
//        } else if (type.equals("other") || type.equals("custom")) {
//            return "other";
//
//        } else {
//            return "file";
//        }
//
//    }


}






