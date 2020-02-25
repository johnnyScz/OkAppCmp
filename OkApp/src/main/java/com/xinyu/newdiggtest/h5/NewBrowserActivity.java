//package com.xinyu.newdiggtest.h5;
//
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.xinyu.newdiggtest.APPConstans;
//import com.xshell.xshelllib.R;
//import com.xshell.xshelllib.ui.XinyuHomeActivity;
//import com.xshell.xshelllib.utils.FulStatusBarUtil;
//
//import org.apache.cordova.engine.SystemWebView;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class NewBrowserActivity extends XinyuHomeActivity {
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FulStatusBarUtil.setcolorfulStatusBar(this);
//        String url = null;
//
//
//        url = getIntent().getStringExtra("newBrowserUrl");
//
//        loadUrl(url);
//
//        callback();
//
//
//    }
//
//    private void callback() {
//
//        JSONObject object = new JSONObject();
//
//        try {
//            object.put("resultCode", 9000);
//            object.put("resultStr", "ok");
//            APPConstans.mcallBack.success(object);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initView();
//    }
//
//    private void initView() {
//
//        this.findViewById(R.id.rl_titleBar).setVisibility(View.VISIBLE);
//        this.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//
//    @Override
//    public int getContentViewRes() {
//        return R.layout.xinyusoft_main;
//    }
//
//    @Override
//    public int getLinearLayoutId() {
//        return R.id.linearLayout;
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        SystemWebView view = (SystemWebView) appView.getEngine().getView();
//        if (view != null) {
//            view.clearCache(true);
////        view.removeAllViews();
//            ((ViewGroup) view.getParent()).removeView(view);
//            view.destroy();
//            view = null;
//        }
//        appView.clearHistory();
//        appView.getView().destroyDrawingCache();
//
//    }
//
//
//}
