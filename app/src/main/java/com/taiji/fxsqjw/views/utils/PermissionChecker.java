package com.taiji.fxsqjw.views.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/10/21.
 */

public class PermissionChecker {
    private final Context mContext;

    public PermissionChecker(Context context){
        mContext = context.getApplicationContext();
    }

    public boolean lacksPermission(String permission){
        return ContextCompat.checkSelfPermission(mContext,permission) == PackageManager.PERMISSION_DENIED;
    }
    public boolean lacksPermission(String... permissions){
        for(String permission:permissions){
            if(lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }
}
