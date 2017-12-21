package com.taiji.fxsqjw.views.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationService extends Service {

    // 定位相关
    LocationClient mLocClient;
    private static final String TAG = "LocationService";

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBDLocation();
    }

    private void initBDLocation() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();
        //开启查看本地城市
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Log.i(TAG,location.getLatitude()+"--"+location.getLongitude());
                Intent intent = new Intent();
                intent.setAction("com.fx.views.activities.openlayer.OpenLayerActivity");
                intent.putExtra("lat", location.getLatitude());
                intent.putExtra("lon", location.getLongitude());
                LocationService.this.sendBroadcast(intent);
            }
        }

    };
}
