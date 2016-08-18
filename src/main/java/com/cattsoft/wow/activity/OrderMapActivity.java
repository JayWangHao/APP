package com.cattsoft.wow.activity;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.cattsoft.framework.base.BaseActivity;
import com.cattsoft.wow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderMapActivity extends BaseActivity {
    //    // 地图相关
    MapView mMapView;
    BaiduMap mBaiduMap;
    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    // 多颜色折线，点击时消失
    Polyline mColorfulPolyline;
    // 纹理折线，点击时获取折线上点数及width
    Polyline mTexturePolyline;
    BitmapDescriptor mRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_red_arrow.png");
    BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
    BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_track_navi_end);
    BitmapDescriptor bd2 = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_card_foot_blue);
    BitmapDescriptor bd3 = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_track_navi_start);
    private LatLng p111;
    private LatLng p211;
    private LatLng p311;
    private LatLng p411;
    private TextView back_text;
    private LatLng p511;
    private LatLng p611;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_map);

        back_text = (TextView) findViewById(R.id.back_text);//返回
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        p111 = new LatLng(29.586891,106.497949);
        p211 = new LatLng(29.578913,106.508225);
        p311 = new LatLng(29.577091,106.526551);
        p411 = new LatLng(29.583185,106.537905);
        p511 = new LatLng(29.5862,106.54581);
        p611 = new LatLng(29.592481,106.549763);
//         初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
//设定中心点坐标
        LatLng cenpt = p111;
//定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(12).build();
//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder().accuracy(100).direction(90.0f).latitude(29.592481).longitude(106.549763).build();
        float f = mBaiduMap.getMaxZoomLevel();//19.0 最小比例尺
        //float m = mBaiduMap.getMinZoomLevel();//3.0 最大比例尺
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(29.592481,106.549763);
        //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll,f);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 7);//设置缩放比例
        mBaiduMap.animateMapStatus(u);

        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);

        // 界面加载时添加绘制图层
        initOverlay();
        start(p211);
        start1(p311);
        start2(p411);
        start3(p511);
        start4(p611);

    }

    private void start(final LatLng p22) {
        final LatLng p2 = p22;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addCustomElementsDemo(p2);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,4000);
    }

    private void start1(final LatLng p22) {
        final LatLng p2 = p22;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addCustomElementsDemo(p2);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,5000);
    }

    private void start2(final LatLng p22) {
        final LatLng p2 = p22;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addCustomElementsDemo(p2);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,6000);
    }

    private void start3(final LatLng p22) {
        final LatLng p2 = p22;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addCustomElementsDemo(p2);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,7000);
    }

    private void start4(final LatLng p22) {
        final LatLng p2 = p22;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addCustomElementsDemo(p2);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,8000);
    }


    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo(LatLng p) {

        // 添加多纹理分段的折线绘制

        List<LatLng> points11 = new ArrayList<LatLng>();
        points11.add(p111);
        points11.add(p);
//        points11.add(p311);
//        points11.add(p411);
//        points11.add(p511);
//        points11.add(p611);
        OverlayOptions ooPolyline = new PolylineOptions().width(13).color(Color.RED).points(points11);
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        if (!p.equals(p611)){
            MarkerOptions ooB = new MarkerOptions().position(p).icon(bd2).zIndex(2).draggable(true);
            mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        }

        p111 = p;
    }

    public void initOverlay() {
        // add marker overlay
        LatLng llA = p111;
        LatLng llC = p611;

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bd).zIndex(9).draggable(true);
        if (true) {
            // 掉下动画
            ooA.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));

        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bd3).zIndex(7);
        if (true) {
            // 生长动画
            ooC.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mRedTexture.recycle();
        mBlueTexture.recycle();
        mGreenTexture.recycle();
        timer.cancel();
        super.onDestroy();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void registerListener() {

    }
}
