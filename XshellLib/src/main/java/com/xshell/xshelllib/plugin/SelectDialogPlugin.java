package com.xshell.xshelllib.plugin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zzy on 2016/10/13.
 * 单选弹出框
 */
public class SelectDialogPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {

        if ("showSelectDialog".equals(action)) {
            String lastCode = args.getString(0);
            int lastCodeIndex = 0;
            JSONArray dataArray = new JSONArray(args.getString(1));

            final String[] choice = new String[dataArray.length()];
            final String[] bankCodeArray = new String[dataArray.length()];

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                choice[i] = object.getString("bankname");
                String bankCode = object.getString("bankcode");
                bankCodeArray[i] = bankCode;
                if (lastCode.equals(bankCode)) {
                    lastCodeIndex = i;
                }
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(cordova.getActivity());
            builder.setTitle("请选择");
            builder.setSingleChoiceItems(choice, lastCodeIndex,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(cordova.getActivity(), "您选择了" + choice[which], Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject res = new JSONObject();
                                res.put("bankcode", bankCodeArray[which]);
                                res.put("bankname", choice[which]);
                                callbackContext.success(res);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss(); // 让窗口消失
                        }
                    });
            builder.create().show();

            return true;
        }

        return false;


    }
}
