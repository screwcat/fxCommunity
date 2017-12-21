package com.taiji.fxsqjw.views.activities.fx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.FxLoginActivity;
import com.taiji.fxsqjw.views.activities.KeyPersonnelActivity;
import com.taiji.fxsqjw.views.activities.GuardNetworkActivity;
import com.taiji.fxsqjw.views.activities.TodoInstructionActivity;
import com.taiji.fxsqjw.views.activities.openlayer.OfflineBaiduMapActivity;
import com.taiji.fxsqjw.views.activities.showInstructionActivity;
import com.taiji.fxsqjw.views.activities.sjcj.SJCJMangerActivity;
import com.taiji.fxsqjw.views.activities.sjxf.SJXFMangerActivity;
import com.taiji.fxsqjw.views.activities.xyaq.XYAQMangerActivity;
import com.taiji.fxsqjw.views.layout.GuideGridLayout;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.LocationUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.Map;

public class GuideFxActivity extends Activity {
    GuideGridLayout gglayout, gglayout_bottom;
    int[] draws = {R.drawable.guard_network, R.drawable.key_personnel, R.drawable.sjxf_manager, R.drawable.xyaq_manager, R.drawable.sjcj_manager, R.drawable.offline_baidu_map};
    String[] titles = {"群防群治", "重点人员", "三级消防", "校园安全", "数据采集", "考勤签到"};
    String[] backColors = {"#E7F8FF", "#FFBE26", "#0789BB", "#01AEF0", "#EF9A19", "#8ED400"};
    String[] textColors = {"#3399FE", "#FFBE26", "#0789BB", "#01AEF0", "#EF9A19", "#8ED400"};
    //int[] backColorsi = {15202559, 16760358, 494011, 110320, 15702553, 9360384};
    int[] draws_bottom = {R.drawable.cheweiyuyue};
    String titles_bottom[] = {"待办任务"};
    private LinearLayout guide_tv_login_ll;
    private TextView guide_tv_login_name;
    private String currentUserId;
    TextView tvWaitProcessNum;
    ImageView headPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_fx);
        if (!LocationUtil.isGPSEnabled((LocationManager) getSystemService(Context.LOCATION_SERVICE))) {
            Toast.makeText(this, "GPS未启动,请开启", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.light_blue);//通知栏所需颜色
        tintManager.setNavigationBarTintColor(R.color.light_blue);
//        OfflineMapCopy.copyFilesByassets(this,"files",this.getBaseContext().getExternalFilesDir("/").getPath());
        gglayout = (GuideGridLayout) findViewById(R.id.list);
        guide_tv_login_ll = (LinearLayout) findViewById(R.id.guide_tv_login_ll);
        gglayout_bottom = (GuideGridLayout) findViewById(R.id.list_bottom);
        guide_tv_login_name = (TextView) findViewById(R.id.guide_tv_login_name);
        guide_tv_login_name.setText(SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERNAME, null));
        currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        headPortrait = (ImageView) findViewById(R.id.headPortrait);
        String url = JojtApiUtils.BASEURL + "/personPhoto/new/" + SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERIDNUMBER, null) + ".png";
        Picasso.with(GuideFxActivity.this).load(url).into(headPortrait);
        gglayout.setGridAdapter(new GuideGridLayout.GridAdatper() {

            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.guidegrid_item,
                        null);
                ImageView iv = (ImageView) view.findViewById(R.id.iv);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                LinearLayout llIconBack = (LinearLayout) view.findViewById(R.id.llIconBack);
                iv.setImageResource(draws[index]);
                tv.setText(titles[index]);
                tv.setTextColor(Color.parseColor(textColors[index]));
                llIconBack.setBackgroundColor(Color.parseColor(backColors[index]));
                return view;
            }

            @Override
            public int getCount() {
                return titles.length;
            }
        });
        gglayout.setOnItemClickListener(new GuideGridLayout.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int index) {
                Intent intent = new Intent();
                switch (index) {
                    case 0: {
                        intent.setClass(GuideFxActivity.this, GuardNetworkActivity.class);
                        break;
                    }
                    case 1: {
                        intent.setClass(GuideFxActivity.this, KeyPersonnelActivity.class);
                        break;
                    }
                    case 2: {
                        intent.setClass(GuideFxActivity.this, SJXFMangerActivity.class);
                        break;
                    }
                    case 3: {
                        intent.setClass(GuideFxActivity.this, XYAQMangerActivity.class);
                        break;
                    }
                    case 4: {
                        intent.setClass(GuideFxActivity.this, SJCJMangerActivity.class);
                        break;
                    }
                    case 5: {
                        intent.setClass(GuideFxActivity.this, OfflineBaiduMapActivity.class);
                        break;
                    }
                    default: {

                    }
                }
                startActivityForResult(intent, 0);
            }
        });


        gglayout_bottom.setGridAdapter(new GuideGridLayout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.guidegrid_item_bottom, null);
                TextView tv = (TextView) view.findViewById(R.id.tv);
                tvWaitProcessNum = (TextView) view.findViewById(R.id.tvWaitProcessNum);
                getWaitProcessNum();
                tv.setText(titles_bottom[index]);
                return view;
            }

            @Override
            public int getCount() {
                return titles_bottom.length;
            }
        });
        gglayout_bottom.setOnItemClickListener(new GuideGridLayout.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int index) {
                v.setAlpha(1f);
//                Toast.makeText(GuideFxActivity.this,"adsaf",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(GuideFxActivity.this, TodoInstructionActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        guide_tv_login_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GuideFxActivity.this, FxLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getWaitProcessNum() {

        JojtApiUtils.waitProcessNum(currentUserId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                try {
                    tvWaitProcessNum.setText(map.get("sum").toString());
                    //Toast.makeText(GuideFxActivity.this,map.get("sum").toString(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(GuideFxActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getWaitProcessNum();
    }
}
