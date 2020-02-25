package com.xshell.xshelllib.plugin;

import android.content.Intent;



import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by huang on 2017/11/16.
 */

public class TwoDimensionCode extends CordovaPlugin {

    public static final int QR_CODE = 15;

    private String callbackName;
    private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, CordovaArgs args,
                           CallbackContext callbackContext) throws JSONException {
        mCallbackContext = callbackContext;
        if("scanQRCode".equals(action)) {
//            callbackName = args.getString(0);
//            Intent intent = new Intent(cordova.getActivity(), CaptureActivity.class);
//            cordova.startActivityForResult(this, intent, QR_CODE);
            return true;
        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode == QR_CODE && resultCode ==cordova.getActivity().RESULT_OK) {
//            cordova.getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    webView.loadUrl("javascript:" + callbackName + "('" + intent.getStringExtra("qrCordResult") + "')");
//                }
//            });
            mCallbackContext.success(intent.getStringExtra("qrCordResult"));
        }

    }
}
