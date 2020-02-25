package com.xinyu.newdiggtest.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xinyu.newdiggtest.R;
import com.xinyu.newdiggtest.adapter.AmapNearByAdapter;
import com.xinyu.newdiggtest.utils.ToastUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AmapActivity extends Activity implements LocationSource {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器
    private LocationUtil locationUtil;

    AMapLocation location;


    AmapNearByAdapter adapter;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        init();

    }


    private void search() {
        if (location != null) {
            LatLonPoint searchLatlonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
            if (searchLatlonPoint != null) {
                doSearchQuery(searchLatlonPoint);
            }
        }

    }


    /**
     * 搜索周边poi
     *
     * @param centerpoint
     */
    private void doSearchQuery(LatLonPoint centerpoint) {

        PoiSearch.Query query = new PoiSearch.Query("", "公司企业", "上海市");
        query.setPageSize(15);
//        query.setPageNum(0);
        PoiSearch poisearch = new PoiSearch(this, query);
        poisearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int resultCode) {
                if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (poiResult != null && poiResult.getPois().size() > 0) {
                        List<PoiItem> poiItems = poiResult.getPois();
                        checkList(poiItems);
                    } else {
                        ToastUtils.getInstanc().showToast("无搜索结果");
                    }
                } else {
                    ToastUtils.getInstanc().showToast("搜索失败");
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poisearch.setBound(new PoiSearch.SearchBound(centerpoint, 500, true));
        poisearch.searchPOIAsyn();
    }


    /**
     * 打印搜索地理信息
     *
     * @param poiItems
     */
    private void checkList(final List<PoiItem> poiItems) {

        if (location == null)
            return;

        adapter = new AmapNearByAdapter(R.layout.item_nearby, poiItems);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showMap(poiItems.get(position));
            }
        });

    }


    /**
     * @param poiItem
     */
    private void showMap(PoiItem poiItem) {
        try {
            JSONObject object = new JSONObject();
            object.put("code", 9000);
            object.put("errMsg", "");
            object.put("longitude", poiItem.getLatLonPoint().getLongitude());
            object.put("latitude", poiItem.getLatLonPoint().getLatitude());
            object.put("province", location.getProvince());
            object.put("city", location.getCity());
            object.put("district", location.getDistrict());
            object.put("address", poiItem.getSnippet());
//            APPConstans.mcallBack.success(object);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void init() {

        recyclerView = this.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AmapActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//给RecyclerView设置适配器


        if (aMap == null) {
            aMap = mapView.getMap();
        }

        setLocationCallBack();

        //设置定位监听
        aMap.setLocationSource(this);
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        //显示定位层并可触发，默认false
        aMap.setMyLocationEnabled(true);
    }

    private void setLocationCallBack() {
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void callBack(String str, double lat, double lgt, AMapLocation aMapLocation) {
                location = aMapLocation;

                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lgt)));
                mListener.onLocationChanged(aMapLocation);
                //添加定位图标
                aMap.addMarker(locationUtil.getMarkerOption(str, lat, lgt));

                search();

            }
        });
    }

    //定位激活回调
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

        locationUtil.startLocate(getApplicationContext());
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //重新绘制加载地图
        mapView.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}