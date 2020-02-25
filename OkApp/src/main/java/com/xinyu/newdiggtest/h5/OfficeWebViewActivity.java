package com.xinyu.newdiggtest.h5;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.utils.MyTextUtil;


public class OfficeWebViewActivity extends Activity {

    WebView urlWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.office_layout);

        initView();

    }

    private void initView() {
        urlWebView = this.findViewById(R.id.webview);

        urlWebView.setWebViewClient(new AppWebViewClients());
        urlWebView.getSettings().setJavaScriptEnabled(true);
        urlWebView.getSettings().setUseWideViewPort(true);


        String url = getIntent().getStringExtra("path");

        String encodeUrl = MyTextUtil.getUrl1Encoe(url);

        Log.e("amtf", "编码：" + encodeUrl);

        urlWebView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src=" + encodeUrl);

    }


    public class AppWebViewClients extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
        }
    }


}
