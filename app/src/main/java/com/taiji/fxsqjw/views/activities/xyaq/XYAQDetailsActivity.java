package com.taiji.fxsqjw.views.activities.xyaq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.XYAQDetailAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XYAQDetailsActivity extends AppCompatActivity {
    private ImageView xyaqAdd_image;
    private static final String TAG = "xyaqDetailsActivity";
    private Button xyaq_details_one_Button;
    private Button xyaq_details_two_Button;
    private TextView xyaq_add_tv_unitCode,xyaq_add_tv_one,xyaq_add_tv_two,xyaq_add_tv_three;
    private ListView xyaq_details_ll_lv_hiddens;
    private List<Map<String, String>> totalList = new ArrayList<Map<String, String>>();
    private XYAQDetailAdapter xyaqDetailAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyaq_details);
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
        xyaqAdd_image=(ImageView)findViewById(R.id.xyaqAdd_image);

        xyaq_add_tv_unitCode = (TextView)findViewById(R.id.xyaq_add_tv_unitCode);
        xyaq_add_tv_one = (TextView)findViewById(R.id.xyaq_add_tv_one);
        xyaq_add_tv_three = (TextView)findViewById(R.id.xyaq_add_tv_three);
        xyaq_add_tv_two = (TextView)findViewById(R.id.xyaq_add_tv_two);
        xyaq_details_ll_lv_hiddens = (ListView)findViewById(R.id.xyaq_details_ll_lv_hiddens);
        xyaq_details_one_Button=(Button)findViewById(R.id.xyaq_details_one_Button);
        //xyaq_details_two_Button=(Button)findViewById(R.id.xyaq_details_two_Button);
        Intent intent = getIntent();
        String dwmc = intent.getStringExtra("dwmc");
        String address = intent.getStringExtra("address");
        String dwlb = intent.getStringExtra("dwlb");
        String unitCode = intent.getStringExtra("unitCode");
        xyaq_add_tv_one.setText(dwmc);
        xyaq_add_tv_two.setText(address);
        xyaq_add_tv_three.setText(dwlb);
        xyaq_add_tv_unitCode.setText(unitCode);

        xyaqDetailAdapter = new XYAQDetailAdapter(XYAQDetailsActivity.this,totalList);
        xyaq_details_ll_lv_hiddens.setAdapter(xyaqDetailAdapter);

        xyaq_details_one_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(XYAQDetailsActivity.this,XYAQAddActivity.class);
                intent.putExtra("dwmc",xyaq_add_tv_one.getText().toString());
                intent.putExtra("address",xyaq_add_tv_two.getText().toString());
                intent.putExtra("dwlb",xyaq_add_tv_three.getText().toString());
                intent.putExtra("unitCode",xyaq_add_tv_unitCode.getText().toString());
                startActivity(intent);
            }
        });

        xyaq_details_ll_lv_hiddens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_id = ((TextView)view.findViewById(R.id.xyaq_details_tv_id)).getText().toString();
                Intent intent=new Intent(XYAQDetailsActivity.this,XYAQHandleActivity.class);
                intent.putExtra("item_id",item_id);
                intent.putExtra("dwmc",xyaq_add_tv_one.getText().toString());
                intent.putExtra("address",xyaq_add_tv_two.getText().toString());
                intent.putExtra("dwlb",xyaq_add_tv_three.getText().toString());
                intent.putExtra("unitCode",xyaq_add_tv_unitCode.getText().toString());
                startActivity(intent);
            }
        });

        JojtApiUtils.campusSecurityDetail(unitCode, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map)obj;
                if(!map.isEmpty()){
                    String success = map.get("success").toString();
                    if("true".equals(success)){
                        List result = (List)map.get("result");
                        totalList.addAll(result);
                        xyaqDetailAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError() {

            }
        });

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
