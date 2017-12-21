package com.taiji.fxsqjw.views.utils;

import android.location.LocationManager;

/**
 * Created by Administrator on 2017/10/23.
 */

public class LocationUtil {

    public static boolean isGPSEnabled(LocationManager locationManager){
        boolean isOpenGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isOpenNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isOpenGPS||isOpenNetwork){
            return true;
        }
        return false;
    }

}
