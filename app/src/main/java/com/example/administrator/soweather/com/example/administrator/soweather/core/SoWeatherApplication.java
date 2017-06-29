package com.example.administrator.soweather.com.example.administrator.soweather.core;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;


/**
 * Created by Administrator on 2016/11/14.
 */

public class SoWeatherApplication extends Application {
    public LocationClient mLocationClient;//定位SDK的核心类
    public MyLocationListener mMyLocationListener;//定义监听类
    private Callbackadress mCallbackadress;//定位回调

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this.getApplicationContext());
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        Appconfiguration config = Appconfiguration.getInstance();
        config.initGeneralPreferences(getApplicationContext());
    }



    /**
     * 实现实位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());//获得当前时间
            sb.append("\nerror code : ");
            sb.append(location.getLocType());//获得erro code得知定位现状
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获得纬度
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());//获得经度
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {//通过GPS定位
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());//获得速度
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                sb.append(location.getDirection());//获得方位
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//通过网络连接定位
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());//获得当前地址
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());//获得经营商？
            }
            mCallbackadress.finish(location.getCity());
        }
    }

    public void setCallbackadress(Callbackadress mCallbackadress) {
        this.mCallbackadress = mCallbackadress;
    }

    public interface Callbackadress {
        void finish(String address);
    }
}
