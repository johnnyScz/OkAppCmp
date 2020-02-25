package com.xshell.xshelllib.plugin;

import android.os.Environment;
import android.util.Log;

import com.xshell.xshelllib.application.AppConstants;
import com.xshell.xshelllib.utils.FileUtil;
import com.xshell.xshelllib.utils.XshellEvent;
import com.xshell.xshelllib.utils.XshellConsts;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class FileOperationPlugin extends CordovaPlugin {
    private static final String TAG = "FileOperationPlugin";

    @Override
    public boolean execute(String action, CordovaArgs args,
                           CallbackContext callbackContext) throws JSONException {
        // TODO Auto-generated method stub

        String result = args.getString(0);

        FileUtil fileUtil = FileUtil.getInstance();

        try {
            if ("createFile".equals(action)) {// 添加文件

                File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + AppConstants.APP_ROOT_DIR);
                if (!mFile.exists()) {
                    mFile.mkdir();
                }

                String fileName = args.getString(0);
                String data = args.getString(1);
                int isAppend = args.getInt(2);
                int rt = 2;
                try {
                    fileUtil.write2SDFromString(AppConstants.APP_ROOT_DIR + "/" + fileName, data, isAppend);
                    rt = 1;
                } catch (Exception e) {
                    rt = 0;
                }
                JSONObject json = new JSONObject();
                json.put("result", rt);
                callbackContext.success(json.toString());
                return true;
            } else if ("deleteFile".equals(action)) {// 删除文件
                String fileName = args.getString(0);

                boolean tmpSign = fileUtil.delete(
                        new File(fileUtil.getPathSDCard()
                                + AppConstants.APP_ROOT_DIR), fileName);
                Log.d("dd", "被删除");
                int rt = tmpSign ? 1 : 0;

                JSONObject json = new JSONObject();
                json.put("result", rt);
                callbackContext.success(json.toString());
                return true;

            } else if ("isfileexist".equals(action)) {//文件是否存在
                String fileName = args.getString(0);
                boolean isExist = fileUtil.isFileExist(AppConstants.APP_ROOT_DIR + "/" + fileName);
                int rt = isExist ? 1 : 0;
                JSONObject json = new JSONObject();
                json.put("result", rt);
                callbackContext.success(json.toString());
                return true;
            } else if ("readFile".equals(action)) {//读文件
                String filePath = args.getString(0);
                return true;
            } else if ("openFile".equals(action)) {//读文件
                String filePath = args.getString(0);
                XshellEvent event = new XshellEvent(XshellConsts.Office_Open_file);
                event.msg = filePath;
                event.extra = args.getString(1);//文件名称
                event.object = callbackContext;
                EventBus.getDefault().post(event);
                return true;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return false;
    }

}
