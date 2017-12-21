package com.taiji.fxsqjw.views.activities;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.OfflineMapCopy;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;
import com.ycgis.pclient.CrashHandler;

/**
 * Created by lightkin on 17-6-29.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OfflineMapCopy.copyFilesByassets(this,"files",this.getBaseContext().getExternalFilesDir("/").getPath());
        SDKInitializer.initialize(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    private static BaseApplication mInstance;

    public static BaseApplication getInstance(){
        if(mInstance == null){
            mInstance = new BaseApplication();
        }
        return mInstance;
    }
}
