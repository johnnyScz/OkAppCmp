package com.xinyu.newdiggtest.h5;

import android.content.Context;

/**
 * Created by Administrator on 2017/11/28.
 */

public class UiHelper extends Thread {

    private Context context;


    public UiHelper(Context mCtx) {
        this.context = mCtx;

    }


    @Override
    public void run() {
        super.run();
        doTask();
    }

    private void doTask() {
//        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "upgradeinfo.html");
//        if (!PreferenceXshellUtil.getInstance().hadFirstRun()) {
//            if (PreferenceXshellUtil.getInstance().isHtmlUpdate()) {
//                PreferenceXshellUtil.getInstance().setHtmlUpdate(false);
//                if (file.exists()) {
//                    String updateUrl = "file:///" + context.getFilesDir().getAbsolutePath() + File.separator + "upgradeinfo.html";
//                    UpdateContentDialog dialog = new UpdateContentDialog(context, R.style.xinyusoft_dialog_fullscreen, updateUrl);
//                    dialog.show();
//                }
//            }
//        } else {
//            PreferenceXshellUtil.getInstance().setFirstRun(false);
//        }

    }
}
