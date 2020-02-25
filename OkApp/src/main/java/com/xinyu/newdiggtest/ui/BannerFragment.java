//package com.xinyu.newdiggtest.ui;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//
//import com.xinyu.newdiggtest.R;
//import com.xinyu.newdiggtest.bean.BannerData;
//import com.xinyu.newdiggtest.helper.GlideImageLoader;
//import com.youth.banner.Banner;
//import com.youth.banner.BannerConfig;
//import com.youth.banner.Transformer;
//import com.youth.banner.listener.OnBannerListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BannerFragment extends Fragment {
//    Banner banner;
//    private List<String> mBannerImageList;
//    private List<String> mBannerTitleList;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_banner, null);
//        banner = view.findViewById(R.id.banner);
//        return view;
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object path, ImageView imageView) {
//                //此处可以自行选择，我直接用的Picasso
////                Glide.with(context).load((String) path).error(R.mipmap.ic_launcher).into(imageView);
//            }
//        });
//        initBannerData(null);
//    }
//
//    private void initBannerData(List<BannerData> bannerDataList) {
////        if (bannerDataList == null || bannerDataList.size() == 0) {
////            return;
////        }
//        mBannerImageList = getUrlList();
//        mBannerTitleList = getTitleList();
//        //设置banner样式
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
//        //设置图片加载器
//        banner.setImageLoader(new GlideImageLoader());
//        //设置图片集合
//        banner.setImages(mBannerImageList);
//        //设置banner动画效果
//        banner.setBannerAnimation(Transformer.DepthPage);
//        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(mBannerTitleList);
//        //设置自动轮播，默认为true
//        banner.isAutoPlay(true);
//        //设置间隔时间
//        banner.setDelayTime(20 * 200);
//        //设置指示器位置（当banner模式中有指示器时）
//        banner.setIndicatorGravity(BannerConfig.CENTER);
//        //设置点击事件
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                Log.e("amtf", "点击了轮播图" + (position + 1));
//            }
//
//        });
//
//        //更新图片集和标题
//        // mBanner.update(mBannerImageList, mBannerTitleList);
//        //banner设置方法全部调用完毕时最后调用
//        banner.start();
//    }
//
//    private List<String> getTitleList() {
//        ArrayList<String> titlelist = new ArrayList<String>();
//        titlelist.add("第一条banner");
//        titlelist.add("我是第二条");
//        titlelist.add("amtf是第3条");
//        titlelist.add("banner测试");
//        return titlelist;
//    }
//
//    private List<String> getUrlList() {
//        ArrayList<String> urlist = new ArrayList<String>();
//        urlist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531715670591&di=394dc3c4b0e2ff0bd579df9cb8f9e10e&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D2788007482%2C739779831%26fm%3D214%26gp%3D0.jpg");
//        urlist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531715737884&di=95fea804ae767f4cb26711f84669bf68&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3De8c436ae552c11dfded1bf2b53266255%2Fb1e5e0b44aed2e7301c977618501a18b86d6faff.jpg");
//        urlist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531715852805&di=1c44e7a545c558a88844df3c246c03ed&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D2739762852%2C2081793192%26fm%3D214%26gp%3D0.jpg");
//        urlist.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531715887889&di=b9ca7e58b43863ad527758ffbf28e9a3&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D75912bbb7b899e51788e3a1c72a6d990%2F20d9ac18367adab40a86e3058bd4b31c8601e467.jpg");
//        return urlist;
//    }
//
//
//}
