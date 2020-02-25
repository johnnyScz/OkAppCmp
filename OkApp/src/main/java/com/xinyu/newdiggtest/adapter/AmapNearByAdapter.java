package com.xinyu.newdiggtest.adapter;


import android.support.annotation.Nullable;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyu.newdiggtest.R;

import java.util.List;

/**
 * 电影列表
 */
public class AmapNearByAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {


    public AmapNearByAdapter(int layoutResId, @Nullable List<PoiItem> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {

        helper.addOnClickListener(R.id.nearby);

        helper.setText(R.id.company, item.getTitle());

        String adress = item.getSnippet();

        String finAdre = adress.length() <= 18 ? adress : "..." + adress.substring(adress.length() - 18, adress.length());
        helper.setText(R.id.company_adress, finAdre);
    }


}
