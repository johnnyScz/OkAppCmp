package com.xinyu.newdiggtest.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.WalletMoneyBean;
import com.xinyu.newdiggtest.utils.DateUtil;
import com.xinyu.newdiggtest.utils.MyTextUtil;


import java.util.List;

/**
 * 钱包明细
 */
public class WalletAdapter extends BaseQuickAdapter<WalletMoneyBean, BaseViewHolder> {


    String fragmentType = "";


    public void setFragmentType(String type) {
        this.fragmentType = type;
    }


    public WalletAdapter(int layoutResId, @Nullable List<WalletMoneyBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, WalletMoneyBean item) {

        String startTime = item.getF_start_date();
        String endTime = item.getF_end_date();
        if (MyTextUtil.isEmpty(startTime)) {
            startTime = "";
        } else {
            startTime = DateUtil.timeOnlyDot(item.getF_start_date());
        }
        if (MyTextUtil.isEmpty(endTime)) {
            endTime = "";
        } else {
            endTime = DateUtil.timeOnlyDot(item.getF_end_date());
        }
        helper.setText(R.id.tv_time, DateUtil.longToHm(item.getF_createtime()));

        String font = item.getF_inorout_type().equals("1") ? "+" : "-";

        String targetName = "";
        if (fragmentType.equals("2")) {
            targetName = MyTextUtil.isEmpty(item.getF_target_name()) ? MyTextUtil.getDecodeStr(item.getF_title()) : MyTextUtil.getDecodeStr(item.getF_target_name());
        } else {
            targetName = MyTextUtil.isEmpty(item.getF_target_name()) ? MyTextUtil.getDecodeStr(item.getF_name()) : MyTextUtil.getDecodeStr(item.getF_target_name());
        }

        if (fragmentType.equals("1")) {
            helper.setText(R.id.tv_title, targetName + startTime + "-" +
                    endTime);
            helper.setText(R.id.tv_money, font + item.getF_fine());
        } else {
            helper.setText(R.id.tv_money, font + item.getF_money());

            if (MyTextUtil.isEmpty(startTime)) {
                helper.setText(R.id.tv_title, targetName);
            } else {
                helper.setText(R.id.tv_title, targetName + startTime + "-" +
                        endTime);
            }


        }


    }


}
