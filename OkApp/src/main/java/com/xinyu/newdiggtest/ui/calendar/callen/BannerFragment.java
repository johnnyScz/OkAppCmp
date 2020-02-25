package com.xinyu.newdiggtest.ui.calendar.callen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.bean.BannerData;
import com.xinyu.newdiggtest.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


public class BannerFragment extends Fragment {
    Banner banner;


    private List<String> mBannerImageList;
    private List<String> mBannerTitleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, null);
        banner = view.findViewById(R.id.banner);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                Glide.with(context).load((String) path).into(imageView);
            }
        });

        requestDatas();

    }

    private void requestDatas() {

        /**
         * 模拟网络请求
         */

        BannerData data = new BannerData();

        data.setImgUrl("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3684347718,1825357922&fm=26&gp=0.jpg");
        data.setTitle("1");

        List<BannerData> bannerDataList = new ArrayList<>();

        bannerDataList.add(data);

        handleData(bannerDataList);


    }


    /**
     * 查询列表
     */


    private void handleData(List<BannerData> bannerDataList) {

        fillData(bannerDataList);
        ;

        //设置banner样式
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(mBannerImageList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mBannerTitleList);

        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置间隔时间
        banner.setDelayTime(20 * 200);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e("amtf", "点击了轮播图" + (position + 1));
            }

        });

        //更新图片集和标题
        // mBanner.update(mBannerImageList, mBannerTitleList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    private void fillData(List<BannerData> bannerDataList) {

        mBannerImageList = new ArrayList<String>();
        mBannerTitleList = new ArrayList<String>();

        for (BannerData item : bannerDataList) {
            mBannerImageList.add(item.getImgUrl());
            mBannerTitleList.add(item.getTitle());
        }


    }


}
