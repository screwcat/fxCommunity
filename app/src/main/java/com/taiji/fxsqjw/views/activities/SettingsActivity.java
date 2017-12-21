package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.utils.AppInnerDownLoder;
import com.taiji.fxsqjw.views.utils.CheckUpdateUtils;
import com.taiji.fxsqjw.views.utils.DownLoadApk;
import com.taiji.fxsqjw.views.utils.UpdateAppInfo;

import java.io.File;
import java.util.List;

public class SettingsActivity extends Activity {

    private final static String iv_personal_pic_path = "/Android/data/com.ingraces.views/icon_bitmap/";
    private final static String iv_personal_pic_name = "myicon.jpg";
    private ImageView iv_personal_pic;
    private ImageView settings_back;
    private android.support.v7.app.AlertDialog.Builder mDialog;

    private RelativeLayout setting_update;
    private ToggleButton settings_toggle_btn_chewei;
    private ToggleButton settings_toggle_btn_koufei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setting_update = (RelativeLayout) findViewById(R.id.setting_update);
        settings_toggle_btn_chewei = (ToggleButton) findViewById(R.id.settings_toggle_btn_chewei);
        settings_toggle_btn_koufei = (ToggleButton) findViewById(R.id.settings_toggle_btn_koufei);
//        settings_back = (ImageView) findViewById(R.id.settings_back);
        setting_update.setOnClickListener(new UpdateListener());

        iv_personal_pic = (ImageView) findViewById(R.id.iv_personal_pic);
        String path=Environment.getExternalStorageDirectory()+iv_personal_pic_path+ iv_personal_pic_name;
        File personalIcon = new File(path);
        if (personalIcon.exists()){
            iv_personal_pic.setImageBitmap(getDiskBitmap(path));
        }

        settings_toggle_btn_chewei.setOnCheckedChangeListener(new ToggleBtnChangeListener());
        settings_toggle_btn_koufei.setOnCheckedChangeListener(new ToggleBtnChangeListener());
//        settings_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SettingsActivity.this.finish();
//            }
//        });

    }


    class UpdateListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //网络检查版本是否需要更新
            CheckUpdateUtils.checkUpdate("apk", "1.0.0", new CheckUpdateUtils.CheckCallBack() {
                @Override
                public void onSuccess(UpdateAppInfo updateInfo) {
                    String isForce=updateInfo.data.getLastForce();//是否需要强制更新
                    String downUrl= updateInfo.data.getUpdateurl();//apk下载地址
                    String updateinfo = updateInfo.data.getUpgradeinfo();//apk更新详情
                    String appName = updateInfo.data.getAppname();
                    if(isForce.equals("1")&& !TextUtils.isEmpty(updateinfo)){//强制更新
                        forceUpdate(SettingsActivity.this,appName,downUrl,updateinfo);
                    }else{//非强制更新
                        //正常升级
                        normalUpdate(SettingsActivity.this,appName,downUrl,updateinfo);
                    }
                }

                @Override
                public void onError() {
                    noneUpdate(SettingsActivity.this);
                }
            });
        }
    }

    class ToggleBtnChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Toast.makeText(SettingsActivity.this, buttonView.getText()+""+isChecked, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 强制更新
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void forceUpdate(final Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle(appName+"又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                //   DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
                AppInnerDownLoder.downLoadApk(SettingsActivity.this,downUrl,appName);
            }
        }).setCancelable(true).create().show();
    }

    /**
     * 正常更新
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle(appName+"又更新咯！");
        mDialog.setMessage(updateinfo);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!canDownloadState()) {
                    showDownloadSetting();
                    return;
                }
                // AppInnerDownLoder.downLoadApk(MainActivity.this,downUrl,appName);
                DownLoadApk.download(SettingsActivity.this,downUrl,updateinfo,appName);
            }
        }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false).create().show();
    }
    /**
     * 无需跟新
     * @param context
     */
    private void noneUpdate(Context context) {
        mDialog = new android.support.v7.app.AlertDialog.Builder(context);
        mDialog.setTitle("版本更新")
                .setMessage("当前已是最新版本无需更新")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false) .create().show();
    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = SettingsActivity.this.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = SettingsActivity.this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从本地获取图片
     * @param pathString 文件路径
     * @return 图片
     */
    public Bitmap getDiskBitmap(String pathString)
    {
        Bitmap bitmap = null;
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return bitmap;
    }
}
