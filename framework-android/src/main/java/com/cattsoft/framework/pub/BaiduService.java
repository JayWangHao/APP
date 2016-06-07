package com.cattsoft.framework.pub;

import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cattsoft.framework.cache.CacheProcess;
import com.cattsoft.framework.cache.MosApp;

/**
 * Created by xumin on 2016/3/2.
 */
public class BaiduService {

    /**
     * 定位SDK的核心类
     */
    private LocationClient mLocClient;
    private String city;
    private Context context;

    public String la="";
    public String lg="";

    private OnLocationListener mLoactionListener = null;

    public interface OnLocationListener {
        public void OnUpdateLocation(String city);
    }
    public void setOnLocationListener(OnLocationListener listener) {
        mLoactionListener = listener;
    }

    public BaiduService(Context context){

        this.context = context;
        //实例化定位服务，LocationClient类必须在主线程中声明
        mLocClient = new LocationClient(MosApp.getInstance());
        //     mLocClient.setAK("GXFsqkfhs7v1tRSyRnSb6yf1");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); // 设置坐标类型

        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        //   option.setServiceName("com.baidu.location.service_v2.9");
        //  option.setPoiExtraInfo(true);

        option.setAddrType("all");

        option.setScanSpan(3000);



//       option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
//
//        option.setPoiNumber(10);
//        option.disableCache(true);


        mLocClient.setLocOption(option);

        mLocClient.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口

    }

    public void startLocation(){


        mLocClient.start();

        mLocClient.requestLocation();


    }

    /**
     * 定位接口，需要实现两个方法
     * @author
     *
     */
    public class BDLocationListenerImpl implements BDLocationListener {

        /**
         * 接收异步返回的定位结果，参数是BDLocation类型参数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {

                city = "北京";

                return;
            }

            city = location.getCity();

            la = location.getLatitude()+"";
            lg = location.getLongitude()+"";

//            double al = location.getAltitude();

            Toast.makeText(context, "经纬度信息：" + la + "," + lg, Toast.LENGTH_SHORT).show();

            CacheProcess.setCacheValueInSharedPreferences(context, "city", city);

            if(mLoactionListener!=null){
                mLoactionListener.OnUpdateLocation(city);
            }


            mLocClient.stop();

        }


    }
}
