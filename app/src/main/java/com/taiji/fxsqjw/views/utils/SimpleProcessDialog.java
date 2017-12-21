package com.taiji.fxsqjw.views.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by lightkin on 17-10-24.
 */

public class SimpleProcessDialog {

    private static ProgressDialog pDialog;
    private static SimpleProcessDialog simpleProcessDialog = new SimpleProcessDialog();

    public static SimpleProcessDialog getInstance(Context context){
        pDialog = new ProgressDialog(context);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        if(simpleProcessDialog==null){
            simpleProcessDialog = new SimpleProcessDialog();
        }
        return simpleProcessDialog;
    }

    public void startDialog(){
        pDialog.show();
    }

    public void stopDialog(){
        pDialog.dismiss();
    }

}
