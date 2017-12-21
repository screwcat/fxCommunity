package com.taiji.fxsqjw.views.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.utils.CheckUpdateUtils;
import com.taiji.fxsqjw.views.utils.DownLoadApk;
import com.taiji.fxsqjw.views.utils.UpdateAppInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

public class MainActivity extends BaseActivity {

    /** 管理fragment */
    private FragmentManager fragmentManager;
    /** 具体内容 */
    private android.support.v4.app.Fragment contentFragment;
    /** 导航 */
    private TextView nav_guide;
    /** 车场 */
    private TextView nav_parks;
    /** 附近 */
    private TextView nav_near;
    /** 预约车位 */
    private TextView nav_appointment;
    /** 我的 */
    private TextView nav_mine;

    private AlertDialog.Builder mDialog;

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色

        //底部导航
        // 导航
        nav_guide = (TextView) findViewById(R.id.nav_guide);
        nav_guide.setOnClickListener(itemClick);
        // 车场
        nav_parks = (TextView) findViewById(R.id.nav_parks);
        nav_parks.setOnClickListener(itemClick);
        // 附近
        nav_near = (TextView) findViewById(R.id.nav_near);
        nav_near.setOnClickListener(itemClick);
        // 预约车位
        nav_appointment = (TextView) findViewById(R.id.nav_appointment);
        nav_appointment.setOnClickListener(itemClick);
        // 我的
        nav_mine = (TextView) findViewById(R.id.nav_mine);
        nav_mine.setOnClickListener(itemClick);

        // fragment管理者
        fragmentManager = getSupportFragmentManager();


        //网络检查版本是否需要更新
        CheckUpdateUtils.checkUpdate("apk", "1.0.0", new CheckUpdateUtils.CheckCallBack() {
            @Override
            public void onSuccess(UpdateAppInfo updateInfo) {
                String isForce=updateInfo.data.getLastForce();//是否需要强制更新
                String downUrl= updateInfo.data.getUpdateurl();//apk下载地址
                String updateinfo = updateInfo.data.getUpgradeinfo();//apk更新详情
                String appName = updateInfo.data.getAppname();
                normalUpdate(MainActivity.this,appName,downUrl,updateinfo);
            }

            @Override
            public void onError() {
                noneUpdate(MainActivity.this);
            }
        });

        nav_guide.callOnClick();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 正常更新
     * @param context
     * @param appName
     * @param downUrl
     * @param updateinfo
     */
    private void normalUpdate(Context context, final String appName, final String downUrl, final String updateinfo) {
        mDialog = new AlertDialog.Builder(context);
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
                DownLoadApk.download(MainActivity.this,downUrl,updateinfo,appName);
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
        mDialog = new AlertDialog.Builder(context);
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
        PackageManager packageManager = getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = this.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

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
     * 点击选项
     */
    private View.OnClickListener itemClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Drawable homeTopDrawable = getResources().getDrawable(R.drawable.home_noclick);
            homeTopDrawable.setBounds(0, 0, 70,70);
            nav_parks.setCompoundDrawables(null, homeTopDrawable, null, null);

            Drawable nearTopDrawable = getResources().getDrawable(R.drawable.near_nolick);
            nearTopDrawable.setBounds(0, 0, 70,70);
            nav_near.setCompoundDrawables(null, nearTopDrawable, null, null);

            Drawable payTopDrawable = getResources().getDrawable(R.drawable.pay_nolick);
            payTopDrawable.setBounds(0, 0, 70,70);
            nav_appointment.setCompoundDrawables(null, payTopDrawable, null, null);

            Drawable settingTopDrawable = getResources().getDrawable(R.drawable.setting_noclick);
            settingTopDrawable.setBounds(0, 0, 70,70);
            nav_mine.setCompoundDrawables(null, settingTopDrawable, null, null);



            /*nav_parks.setBackgroundColor(0xffbfbfbf);
            nav_parks.setTextColor(0XFF5e5e5e);
            nav_near.setBackgroundColor(0xffbfbfbf);
            nav_near.setTextColor(0XFF5e5e5e);
            nav_appointment.setBackgroundColor(0Xffbfbfbf);
            nav_appointment.setTextColor(0XFF5e5e5e);
            nav_mine.setBackgroundColor(0xffbfbfbf);
            nav_mine.setTextColor(0XFF5e5e5e);
            TextView textview = (TextView) v;
            textview.setBackgroundColor(0XFFdfdfdf);
            textview.setTextColor(0xff5e5e5e);*/
            // 开启一个Fragment事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (v.getId()) {
                case R.id.nav_guide:
                    break;
                case R.id.nav_parks:
                    break;
                case R.id.nav_near:
                    TextView neartextview = (TextView) v;
                    Drawable nearcurrentTopDrawable = getResources().getDrawable(R.drawable.near_onlick);
                    nearcurrentTopDrawable.setBounds(0, 0, 70,70);
                    neartextview.setCompoundDrawables(null, nearcurrentTopDrawable, null, null);
                   // contentFragment = new BaiduMapFragment();
                    transaction.replace(R.id.content, contentFragment);
                    break;
                case R.id.nav_appointment:
//                    contentFragment = new AppointLotFragment();
//                    transaction.replace(R.id.content, contentFragment);
                    TextView paytextview = (TextView) v;
                    Drawable paycurrentTopDrawable = getResources().getDrawable(R.drawable.pay_onlick);
                    paycurrentTopDrawable.setBounds(0, 0, 70,70);
                    paytextview.setCompoundDrawables(null, paycurrentTopDrawable, null, null);


                    break;
                case R.id.nav_mine:
                    TextView settextview = (TextView) v;
                    Drawable setcurrentTopDrawable = getResources().getDrawable(R.drawable.setting_onlick);
                    setcurrentTopDrawable.setBounds(0, 0, 70,70);
                    settextview.setCompoundDrawables(null, setcurrentTopDrawable, null, null);
                    contentFragment = new AboutFragment();
                    transaction.replace(R.id.content, contentFragment);


//                    Intent intentcar = new Intent();
//                    intentcar.setClass(MainActivity.this, CarManageActivity.class);
//                    startActivity(intentcar);
                    break;

                default:
                    break;
            }
            transaction.commit();
        }
    };

}
