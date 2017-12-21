package com.taiji.fxsqjw.views.activities.sjxf;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.SJXFDetailAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SJXFDetailsActivity extends AppCompatActivity {
    private ImageView sjxfAdd_image;
    private static final String TAG = "SJXFDetailsActivity";
    private Button sjxf_details_one_Button;
    private Button sjxf_details_two_Button;
    private TextView sjxf_add_tv_unitCode,sjxf_add_tv_one,sjxf_add_tv_two,sjxf_add_tv_three;
    private ListView sjxf_details_ll_lv_hiddens;
    private List<Map<String, String>> totalList = new ArrayList<Map<String, String>>();
    private SJXFDetailAdapter sjxfDetailAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjxf_details);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色

        sjxfAdd_image=(ImageView)findViewById(R.id.sjxfAdd_image);

        sjxf_add_tv_unitCode = (TextView)findViewById(R.id.sjxf_add_tv_unitCode);
        sjxf_add_tv_one = (TextView)findViewById(R.id.sjxf_add_tv_one);
        sjxf_add_tv_three = (TextView)findViewById(R.id.sjxf_add_tv_three);
        sjxf_add_tv_two = (TextView)findViewById(R.id.sjxf_add_tv_two);
        sjxf_details_ll_lv_hiddens = (ListView)findViewById(R.id.sjxf_details_ll_lv_hiddens);
        sjxf_details_one_Button=(Button)findViewById(R.id.sjxf_details_one_Button);
        //sjxf_details_two_Button=(Button)findViewById(R.id.sjxf_details_two_Button);
        Intent intent = getIntent();
        String dwmc = intent.getStringExtra("dwmc");
        String address = intent.getStringExtra("address");
        String dwlb = intent.getStringExtra("dwlb");
        String unitCode = intent.getStringExtra("unitCode");
        sjxf_add_tv_one.setText(dwmc);
        sjxf_add_tv_two.setText(address);
        sjxf_add_tv_three.setText(dwlb);
        sjxf_add_tv_unitCode.setText(unitCode);

        sjxfDetailAdapter = new SJXFDetailAdapter(SJXFDetailsActivity.this,totalList);
        sjxf_details_ll_lv_hiddens.setAdapter(sjxfDetailAdapter);

        sjxf_details_one_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(SJXFDetailsActivity.this,SJXFAddActivity.class);
                intent.putExtra("dwmc",sjxf_add_tv_one.getText().toString());
                intent.putExtra("address",sjxf_add_tv_two.getText().toString());
                intent.putExtra("dwlb",sjxf_add_tv_three.getText().toString());
                intent.putExtra("unitCode",sjxf_add_tv_unitCode.getText().toString());
                startActivity(intent);
            }
        });

        sjxf_details_ll_lv_hiddens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item_id = ((TextView)view.findViewById(R.id.sjxf_details_tv_id)).getText().toString();
                Intent intent=new Intent(SJXFDetailsActivity.this,SJXFHandleActivity.class);
                intent.putExtra("item_id",item_id);
                intent.putExtra("dwmc",sjxf_add_tv_one.getText().toString());
                intent.putExtra("address",sjxf_add_tv_two.getText().toString());
                intent.putExtra("dwlb",sjxf_add_tv_three.getText().toString());
                intent.putExtra("unitCode",sjxf_add_tv_unitCode.getText().toString());
                startActivity(intent);
            }
        });

        JojtApiUtils.threeLevelFireDetail(unitCode, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map)obj;
                if(!map.isEmpty()){
                    String success = map.get("success").toString();
                    if("true".equals(success)){
                        List result = (List)map.get("result");
                        totalList.addAll(result);
                        sjxfDetailAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError() {

            }
        });

//        sjxf_details_two_Button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent=new Intent(SJXFDetailsActivity.this,SJXFHandleActivity.class);
//                startActivity(intent);
//            }
//        });
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
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
