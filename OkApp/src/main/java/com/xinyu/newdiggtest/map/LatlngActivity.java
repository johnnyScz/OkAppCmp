package com.xinyu.newdiggtest.map;


import android.app.Activity;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.xinyu.newdiggtest.R;


public class LatlngActivity extends Activity implements LocationSource, AMapLocationListener {

    private MapView mapView;

    private AMap aMap;
    private UiSettings mUiSettings;
    private CameraUpdate mUpdata;

    LatLng location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.mapView1);
        mapView.onCreate(savedInstanceState);// 必须要写
        init();
    }

    private void init() {

        location = getIntentData();

        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mUpdata = CameraUpdateFactory.newCameraPosition(
//15是缩放比例，0是倾斜度，30显示比例
                    new CameraPosition(location, 15, 0, 30));//这是地理位置，就是经纬度。
            aMap.moveCamera(mUpdata);//定位的方法
            drawMarkers();
        }
    }

    private LatLng getIntentData() {

        double lat = getIntent().getDoubleExtra("lat", 0);
        double longtu = getIntent().getDoubleExtra("long", 0);
        LatLng data = new LatLng(lat, longtu);

        return data;
    }


    public void drawMarkers() {

        Marker marker = aMap.addMarker(new MarkerOptions()
                .position(location).title("目的地").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true));

//        Marker marker = aMap.addMarker(new MarkerOptions()
//                .position(location).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .draggable(true));


        marker.showInfoWindow();// 设置默认显示一个infowinfow
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
// TODO Auto-generated method stub

    }

    @Override
    public void activate(OnLocationChangedListener arg0) {
// TODO Auto-generated method stub

    }

    @Override
    public void deactivate() {
// TODO Auto-generated method stub

    }
}



