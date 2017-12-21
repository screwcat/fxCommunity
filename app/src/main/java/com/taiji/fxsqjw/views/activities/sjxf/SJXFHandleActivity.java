package com.taiji.fxsqjw.views.activities.sjxf;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.ImageUrlFullScreen;
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.util.Map;

public class SJXFHandleActivity extends AppCompatActivity {
    private static final String TAG = "SJXFHandleActivity";
    private Spinner sjxf_handle_spinner_chulijieguo,sjxf_handle_spinner_chulixiangmu;
    private String item_id = null;
    private TextView sjxf_details_tv_yinhuanleibie,sjxf_details_tv_yinhuanmiaoshu,sjxf_handle_tv_zelingshijian,sjxf_handle_tv_zuihouqixian,sjxf_add_tv_one,sjxf_add_tv_two,sjxf_add_tv_three;
    private ImageView qkqd_add_iv_item;
    private String dwmc = null,address = null,dwlb = null,unitCode = null,createTime = null,dealProject = null,dealResult = null, filePath = null, id = null, remark = null,type=null,endTime=null;
    private Resources res = null;
    private LinearLayout sjxf_zgqx_ll,sjxf_handle_ll_project;
    private Button sjxf_details_one_Button;
    private String image_url;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjxf_handle);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        item_id = getIntent().getStringExtra("item_id");
        Intent intent = getIntent();
        dwmc = intent.getStringExtra("dwmc");
        address = intent.getStringExtra("address");
        dwlb = intent.getStringExtra("dwlb");
        unitCode = intent.getStringExtra("unitCode");
        res = getResources();
        sjxf_handle_spinner_chulijieguo = (Spinner)findViewById(R.id.sjxf_handle_spinner_chulijieguo);
        sjxf_handle_spinner_chulixiangmu = (Spinner)findViewById(R.id.sjxf_handle_spinner_chulixiangmu);
        qkqd_add_iv_item = (ImageView)findViewById(R.id.qkqd_add_iv_item);
        sjxf_zgqx_ll = (LinearLayout)findViewById(R.id.sjxf_zgqx_ll);
        sjxf_handle_ll_project = (LinearLayout)findViewById(R.id.sjxf_handle_ll_project);

        sjxf_details_tv_yinhuanleibie = (TextView)findViewById(R.id.sjxf_details_tv_yinhuanleibie);
        sjxf_details_tv_yinhuanmiaoshu = (TextView)findViewById(R.id.sjxf_details_tv_yinhuanmiaoshu);
        sjxf_handle_tv_zelingshijian = (TextView)findViewById(R.id.sjxf_handle_tv_zelingshijian);
        sjxf_handle_tv_zuihouqixian = (TextView)findViewById(R.id.sjxf_handle_tv_zuihouqixian);
        sjxf_add_tv_one = (TextView)findViewById(R.id.sjxf_add_tv_one);
        sjxf_add_tv_two = (TextView)findViewById(R.id.sjxf_add_tv_two);
        sjxf_add_tv_three = (TextView)findViewById(R.id.sjxf_add_tv_three);
        sjxf_details_one_Button = (Button) findViewById(R.id.sjxf_details_one_Button);
        sjxf_add_tv_one.setText(dwmc);
        sjxf_add_tv_two.setText(address);
        sjxf_add_tv_three.setText(dwlb);

        sjxf_handle_spinner_chulijieguo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!StringUtil.isEmpty(dealResult)) {
                        int dealResult_int = Integer.parseInt(dealResult) - 1;
                        if (dealResult_int == 0 && position != 0) {
                            Toast.makeText(SJXFHandleActivity.this, "你已选择过整改合格", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (dealResult_int == 1 && position == 1) {
                            Toast.makeText(SJXFHandleActivity.this, "你已选择过限期整改", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(!StringUtil.isEmpty(dealProject)) {
                        int dealProject_int = Integer.parseInt(dealProject) - 1;
                        if (dealProject_int == 3) {
                            sjxf_handle_spinner_chulijieguo.setSelection(1);
                            Toast.makeText(SJXFHandleActivity.this, "处罚为三停，结果只能为合格", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    sjxf_handle_ll_project.setVisibility(View.GONE);
                    if (position == 2) {
                        sjxf_handle_ll_project.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    Toast.makeText(SJXFHandleActivity.this,"onItemSelected-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sjxf_details_one_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sjxf_handle_spinner_chulijieguo_str = String.valueOf(sjxf_handle_spinner_chulijieguo.getSelectedItemPosition()+1);
                String sjxf_handle_spinner_chulixiangmu_str = String.valueOf(sjxf_handle_spinner_chulixiangmu.getSelectedItemPosition()+1);
                JojtApiUtils.hiddenDangerDealResult(item_id,sjxf_handle_spinner_chulixiangmu_str , sjxf_handle_spinner_chulijieguo_str, new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map = (Map)obj;
                        Log.i(TAG,map.toString());
                    }

                    @Override
                    public void onError() {

                    }
                });
                Intent intent=new Intent(SJXFHandleActivity.this,SJXFMangerActivity.class);
                startActivity(intent);
            }
        });

        JojtApiUtils.threeLevelFireDetailInfo(item_id, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map)obj;
                Log.i(TAG,map.toString());
                if(!map.isEmpty()){
                    String success = map.get("success").toString();
                    if("true".equals(success)){
                        Map<String,String> result = (Map<String,String>)map.get("result");
                        createTime = result.get("createTime");
                        endTime = result.get("endTime");
                        dealProject = result.get("dealProject");
                        dealResult = result.get("dealResult");
                        filePath = result.get("filePath");
                        id = result.get("id");
                        remark = result.get("remark");
                        type = result.get("type");
                        String[] checkbox_sjxf = res.getStringArray(R.array.checkbox_sjxf);
                        String type_show = "";
                        if(type.contains("|")){
                            String[] types = type.split("\\|");
                            for(String tp:types){
                                type_show = type_show + checkbox_sjxf[Integer.parseInt(tp)-1] + " ";
                            }
                        }else{
                            type_show = checkbox_sjxf[Integer.parseInt(type)-1];
                        }
                        try {
                            sjxf_details_tv_yinhuanleibie.setText(type_show);
                            sjxf_details_tv_yinhuanmiaoshu.setText(remark);
                            sjxf_handle_tv_zelingshijian.setText(createTime);
                            sjxf_handle_tv_zuihouqixian.setText(endTime);
                            if(!StringUtil.isEmpty(dealResult)) {
                                sjxf_handle_spinner_chulijieguo.setSelection(Integer.parseInt(dealResult) - 1);
                            }
                            if(!StringUtil.isEmpty(dealProject)) {
                                sjxf_handle_spinner_chulixiangmu.setSelection(Integer.parseInt(dealProject) - 1);
                            }
                            image_url = JojtApiUtils.BASEURL + filePath;//"weizhi.png";
                            Picasso.with(SJXFHandleActivity.this).load(image_url).into(qkqd_add_iv_item);
                        }catch(Exception e){
                            Toast.makeText(SJXFHandleActivity.this,"onSuccess-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        //修改隐患处理状态
                        changeHandleState();
                    }else{
                        Toast.makeText(SJXFHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SJXFHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(SJXFHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        qkqd_add_iv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUrlFullScreen.smallImgClickFull(SJXFHandleActivity.this,image_url);
            }
        });

    }

    private void changeHandleState() {
        try {
            if(!StringUtil.isEmpty(dealResult)) {
                int dealResult_int = Integer.parseInt(dealResult) - 1;
                if (dealResult_int == 1) {
                    sjxf_zgqx_ll.setVisibility(View.VISIBLE);
                }
            }
            if(!StringUtil.isEmpty(dealProject)) {
                int dealProject_int = Integer.parseInt(dealProject) - 1;
                if (dealProject_int == 3) {
                    sjxf_handle_spinner_chulijieguo.setSelection(0);
                }
            }
        }catch(Exception e){
            Toast.makeText(SJXFHandleActivity.this,"changeHandleState-"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break ;
        }
        return true;
    }

}
