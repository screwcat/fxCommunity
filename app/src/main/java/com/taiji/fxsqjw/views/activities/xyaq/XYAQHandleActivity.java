package com.taiji.fxsqjw.views.activities.xyaq;

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
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.util.Map;

public class XYAQHandleActivity extends AppCompatActivity {

    private static final String TAG = "XYAQHandleActivity";
    private Spinner xyaq_handle_spinner_chulijieguo,xyaq_handle_spinner_chulixiangmu;
    private String item_id = null;
    private TextView xyaq_details_tv_yinhuanleibie,xyaq_details_tv_yinhuanmiaoshu,xyaq_handle_tv_zelingshijian,xyaq_handle_tv_zuihouqixian,xyaq_add_tv_one,xyaq_add_tv_two,xyaq_add_tv_three;
    private ImageView qkqd_add_iv_item;
    private String dwmc = null,address = null,dwlb = null,unitCode = null,createTime = null,dealProject = null,dealResult = null, filePath = null, id = null, remark = null,type=null,endTime=null;
    private Resources res = null;
    private LinearLayout xyaq_zgqx_ll,xyaq_handle_ll_project;
    private Button xyaq_details_one_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyaq_handle);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        item_id = getIntent().getStringExtra("item_id");
        Intent intent = getIntent();
        dwmc = intent.getStringExtra("dwmc");
        address = intent.getStringExtra("address");
        dwlb = intent.getStringExtra("dwlb");
        unitCode = intent.getStringExtra("unitCode");
        res = getResources();
        xyaq_handle_spinner_chulijieguo = (Spinner)findViewById(R.id.xyaq_handle_spinner_chulijieguo);
        xyaq_handle_spinner_chulixiangmu = (Spinner)findViewById(R.id.xyaq_handle_spinner_chulixiangmu);
        qkqd_add_iv_item = (ImageView)findViewById(R.id.xyaq_xczp);
        xyaq_zgqx_ll = (LinearLayout)findViewById(R.id.xyaq_zgqx_ll);
        xyaq_handle_ll_project = (LinearLayout)findViewById(R.id.xyaq_handle_ll_project);

        xyaq_details_tv_yinhuanleibie = (TextView)findViewById(R.id.xyaq_details_tv_yinhuanleibie);
        xyaq_details_tv_yinhuanmiaoshu = (TextView)findViewById(R.id.xyaq_details_tv_yinhuanmiaoshu);
        xyaq_handle_tv_zelingshijian = (TextView)findViewById(R.id.xyaq_handle_tv_zelingshijian);
        xyaq_handle_tv_zuihouqixian = (TextView)findViewById(R.id.xyaq_handle_tv_zuihouqixian);
        xyaq_add_tv_one = (TextView)findViewById(R.id.xyaq_add_tv_one);
        xyaq_add_tv_two = (TextView)findViewById(R.id.xyaq_add_tv_two);
        xyaq_add_tv_three = (TextView)findViewById(R.id.xyaq_add_tv_three);
        xyaq_details_one_Button = (Button) findViewById(R.id.xyaq_details_one_Button);
        xyaq_add_tv_one.setText(dwmc);
        xyaq_add_tv_two.setText(address);
        xyaq_add_tv_three.setText(dwlb);

        xyaq_handle_spinner_chulijieguo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if(!StringUtil.isEmpty(dealResult)) {
                        int dealResult_int = Integer.parseInt(dealResult) - 1;
                        if (dealResult_int == 0 && position != 0) {
                            Toast.makeText(XYAQHandleActivity.this, "你已选择过整改合格", Toast.LENGTH_SHORT).show();
                            //xyaq_handle_spinner_chulixiangmu.setVisibility(view.GONE);
                            return;
                        } else if (dealResult_int == 1 && position == 1) {
                            Toast.makeText(XYAQHandleActivity.this, "你已选择过限期整改", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    xyaq_handle_ll_project.setVisibility(View.GONE);
                    if (position == 2) {
                        xyaq_handle_ll_project.setVisibility(View.VISIBLE);
                    }
                }catch(Exception e){
                    Toast.makeText(XYAQHandleActivity.this,"onItemSelected-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        xyaq_details_one_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xyaq_handle_spinner_chulijieguo_str = String.valueOf(xyaq_handle_spinner_chulijieguo.getSelectedItemPosition()+1);
                String xyaq_handle_spinner_chulixiangmu_str = String.valueOf(xyaq_handle_spinner_chulixiangmu.getSelectedItemPosition()+1);
                JojtApiUtils.campusSecurityDealResult(item_id,xyaq_handle_spinner_chulixiangmu_str , xyaq_handle_spinner_chulijieguo_str, new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map = (Map)obj;
                        Log.i(TAG,map.toString());
                        Toast.makeText(XYAQHandleActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(XYAQHandleActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent=new Intent(XYAQHandleActivity.this,XYAQMangerActivity.class);
                startActivity(intent);
            }
        });

        JojtApiUtils.campusSecurityDetailInfo(item_id, new JojtApiUtils.ApiCallBack() {
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
                        String[] checkbox_xyaq = res.getStringArray(R.array.checkbox_xyaq);
                        String type_show = "";
                        if(type.contains("|")){
                            String[] types = type.split("\\|");
                            for(String tp:types){
                                type_show = type_show + checkbox_xyaq[Integer.parseInt(tp)-1] + " ";
                            }
                        }else{
                            type_show = checkbox_xyaq[Integer.parseInt(type)-1];
                        }
                        try {
                            xyaq_details_tv_yinhuanleibie.setText(type_show);
                            xyaq_details_tv_yinhuanmiaoshu.setText(remark);
                            xyaq_handle_tv_zelingshijian.setText(createTime);
                            xyaq_handle_tv_zuihouqixian.setText(endTime);
                            if(!StringUtil.isEmpty(dealResult)) {
                                xyaq_handle_spinner_chulijieguo.setSelection(Integer.parseInt(dealResult) - 1);
                            }
                            if(!StringUtil.isEmpty(dealProject)) {
                                xyaq_handle_spinner_chulixiangmu.setSelection(Integer.parseInt(dealProject) - 1);
                            }
                            String url = JojtApiUtils.BASEURL + filePath;//"weizhi.png";
                            Picasso.with(XYAQHandleActivity.this).load(url).into(qkqd_add_iv_item);
                        }catch(Exception e){
                            Toast.makeText(XYAQHandleActivity.this,"onSuccess-"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        //修改隐患处理状态
                        changeHandleState();
                    }else{
                        Toast.makeText(XYAQHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(XYAQHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(XYAQHandleActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void changeHandleState() {
        try {
            if(!StringUtil.isEmpty(dealResult)) {
                int dealResult_int = Integer.parseInt(dealResult) - 1;
                if (dealResult_int == 1) {
                    xyaq_zgqx_ll.setVisibility(View.VISIBLE);
                }
            }
        }catch(Exception e){
            Toast.makeText(XYAQHandleActivity.this,"changeHandleState-"+e.getMessage(),Toast.LENGTH_SHORT).show();
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
