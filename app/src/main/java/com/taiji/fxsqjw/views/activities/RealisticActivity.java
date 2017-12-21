package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.sjxf.SJXFHandleActivity;
import com.taiji.fxsqjw.views.adapter.KeyPersonnelAdapter;
import com.taiji.fxsqjw.views.layout.GuideGridLayout;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RealisticActivity extends AppCompatActivity {
    private GuideGridLayout gglayout;
    private GuideGridLayout listPersonType;
    private String titles[] = {"治 安", "社 区", "刑 侦", "出入境", "禁 毒", "网 安", "巡 特", "经 侦", "食药侦", "技 侦", "内 保", "凌 河", "消 防", "维 稳", "指挥中心", "国 保"};
    private String jzIds[] = {"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"};
    private TextView objectId;
    private TextView objectName;
    private ImageView personPic;
    private TextView documentNumber;
    private TextView cityCodeAddress;
    private TextView address;
    private String strObjectId;
    private String strObjectName;
    private String strDocumentNumber;
    private String strCityCodeAddress;
    private String strAddress;
    private ProgressDialog pDialog;
    private String currentUserId;
    private String strDescribe;
    private String photoPath;
    private String personType;
    GridView gvRealistic;
    String[] personTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        pDialog.show();
        gvRealistic = (GridView) findViewById(R.id.gvRealistic);
        strObjectId = getIntent().getStringExtra("objectId");
        strObjectName = getIntent().getStringExtra("objectName");
        strDocumentNumber = getIntent().getStringExtra("documentNumber");
        strCityCodeAddress = getIntent().getStringExtra("cityCodeAddress");
        strAddress = getIntent().getStringExtra("address");
        photoPath = getIntent().getStringExtra("photoPath");
        personType = getIntent().getStringExtra("personType");
        personTypes = personType.split(",");
        objectId = (TextView) findViewById(R.id.objectId);
        objectName = (TextView) findViewById(R.id.objectName);
        personPic = (ImageView) findViewById(R.id.personPic);
        documentNumber = (TextView) findViewById(R.id.documentNumber);
        cityCodeAddress = (TextView) findViewById(R.id.cityCodeAddress);
        address = (TextView) findViewById(R.id.address);
        objectId.setText(strObjectId);
        objectName.setText(strObjectName);
        documentNumber.setText(strDocumentNumber);
        cityCodeAddress.setText(strCityCodeAddress);
        address.setText(strAddress);
        //personPic.setImageURI(Uri.parse(JojtApiUtils.BASEURL + photoPath));
        String url = JojtApiUtils.BASEURL + photoPath;
        Picasso.with(RealisticActivity.this).load(url).into(personPic);
        listPersonType = (GuideGridLayout) findViewById(R.id.listPersonType);
        listPersonType.setGridAdapter(new GuideGridLayout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.person_type_item, null);
                TextView personTypeName = (TextView) view.findViewById(R.id.personTypeName);
                personTypeName.setText(personTypes[index]);
                return view;
            }

            @Override
            public int getCount() {
                return personTypes.length;
            }
        });
        gglayout = (GuideGridLayout) findViewById(R.id.listPoliceCategory);
        gglayout.setGridAdapter(new GuideGridLayout.GridAdatper() {
            @Override
            public View getView(int index) {
                View view = getLayoutInflater().inflate(R.layout.stress_object_realistic_item, null);
                TextView tvJzId = (TextView) view.findViewById(R.id.jzId);
                TextView tvpc = (TextView) view.findViewById(R.id.tvPoliceCategory);
                tvJzId.setText(jzIds[index]);
                tvpc.setText(titles[index]);
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
                TextView tvpc = (TextView) v.findViewById(R.id.tvPoliceCategory);
                if (tvpc.getCurrentTextColor() == -1) {
                    tvpc.setTextColor(Color.parseColor("#349afe"));
                    tvpc.setBackground(getResources().getDrawable(R.drawable.textview_border));
                } else {
                    tvpc.setTextColor(Color.parseColor("#ffffff"));
                    tvpc.setBackgroundColor(Color.parseColor("#349afe"));
                }
            }
        });
        pDialog.dismiss();
    }

    public void submitRealistic(View view) {
        String xsjz = "";
        gglayout = (GuideGridLayout) findViewById(R.id.listPoliceCategory);
        for (int i = 0; i < gglayout.getChildCount(); i++) {
            if (((TextView) gglayout.getChildAt(i).findViewById(R.id.tvPoliceCategory)).getCurrentTextColor() == -1) {
                xsjz += ((TextView) gglayout.getChildAt(i).findViewById(R.id.jzId)).getText() + "|";
            }
        }
        if (!"".equals(xsjz)) {
            xsjz = xsjz.substring(0, xsjz.length() - 1);
        }
        currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        strDescribe = ((TextView) findViewById(R.id.describe)).getText().toString();
        JojtApiUtils.stressObjectWrite(currentUserId, strObjectId, strDescribe, xsjz, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (map.get("success").toString().equals("true")) {
                    Toast toast = Toast.makeText(RealisticActivity.this, "提交成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    RealisticActivity.this.finish();
                } else {
                    Toast toast = Toast.makeText(RealisticActivity.this, "提交失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(RealisticActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
