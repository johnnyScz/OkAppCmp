package com.xinyu.newdiggtest.utils;

import android.content.Context;
import android.content.Intent;

import com.xinyu.newdiggtest.ui.Digg.fragment.FocusSuccedActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.QcodeInviteActivity;
import com.xinyu.newdiggtest.ui.Digg.fragment.SxSwiptActivity;

public class HandleQcodeUtil {


    Context mctx;


    public HandleQcodeUtil(Context mcontex) {
        this.mctx = mcontex;
    }


    public void parseData(String codeStr) {
        if (!MyTextUtil.isEmpty(codeStr)) {
            if (codeStr.contains("/group/")) {
                String[] shuzu = codeStr.split("/");
                String roomid = shuzu[4];
                String formUserId = shuzu[5];
                Intent intent1 = new Intent(mctx, QcodeInviteActivity.class);
                intent1.putExtra("roomId", roomid);
                intent1.putExtra("formUserId", formUserId);
                mctx.startActivity(intent1);
            } else if (codeStr.contains("myself/")) {


                String[] shuzu = codeStr.split("/");

                String hisUserId = shuzu[4];

                Intent mintent = new Intent(mctx, SxSwiptActivity.class);
                mintent.putExtra("userId", hisUserId);
                mctx.startActivity(mintent);


            } else if (codeStr.contains("target/")) {

                //  "http://testok.xinyusoft.com/target/80b51b4298ab45a89fda5116c7f62d71/230"

                String[] shuzu = codeStr.split("/");

                if (shuzu == null || shuzu.length < 5)
                    return;

                String targetId = shuzu[4];
                String formUserId = shuzu[5];

                if (MyTextUtil.isEmpty(targetId) || MyTextUtil.isEmpty(formUserId))
                    return;

                Intent mItent = new Intent(mctx, FocusSuccedActivity.class);
                mItent.putExtra("targetId", targetId);
                mItent.putExtra("formUserId", formUserId);
                mctx.startActivity(mItent);
            }


        }


    }


}
