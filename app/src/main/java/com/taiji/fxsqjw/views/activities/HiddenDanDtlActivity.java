package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.sjxf.SJXFHandleActivity;
import com.taiji.fxsqjw.views.adapter.InstructQueryPicAdapter;
import com.taiji.fxsqjw.views.layout.GuideGridLayout;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.ImageUrlFullScreen;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiddenDanDtlActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private TextView insUuid;
    private TextView courierName;
    private TextView reportTitle;
    private TextView sendTime;
    private TextView reportContent;
    private TextView tvIsValid;
    private TextView tvFeedbackContent;
    private Button btnSubmit;
    private LinearLayout llIsValid;
    private LinearLayout llRadioGroup;
    private String strUuid;
    private List picList;


    private GridView gvInstruct;
    InstructQueryPicAdapter instructQueryPicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_dan_dtl);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        pDialog.show();
        insUuid = (TextView) findViewById(R.id.insUuid);
        courierName = (TextView) findViewById(R.id.courierName);
        reportTitle = (TextView) findViewById(R.id.reportTitle);
        sendTime = (TextView) findViewById(R.id.sendTime);
        reportContent = (TextView) findViewById(R.id.reportContent);
        gvInstruct = (GridView) findViewById(R.id.gvInstruct);
        tvIsValid = (TextView) findViewById(R.id.tvIsValid);
        llIsValid = (LinearLayout) findViewById(R.id.llIsValid);
        llRadioGroup = (LinearLayout) findViewById(R.id.llRadioGroup);
        tvFeedbackContent = (TextView) findViewById(R.id.feedbackContent);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        strUuid = getIntent().getStringExtra("uuid");
        insUuid.setText(strUuid);
        String currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        JojtApiUtils.instructQueryDetail(strUuid, currentUserId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                picList = (List) ((Map) obj).get("picList");
                courierName.setText(map.get("courierName").toString());
                reportTitle.setText(map.get("reportTitle").toString());
                sendTime.setText(map.get("sendTime").toString());
                reportContent.setText(map.get("reportContent").toString());
                switch (map.get("reportFeedbackStatus").toString()) {
                    case "0": {
                        llIsValid.setVisibility(View.GONE);
                        break;
                    }
                    case "1": {
                        tvIsValid.setText("有效");
                        tvFeedbackContent.setText(map.get("reportFeedbackContent").toString());
                        tvFeedbackContent.setEnabled(false);
                        btnSubmit.setVisibility(View.GONE);
                        llRadioGroup.setVisibility(View.GONE);
                        break;
                    }
                    case "2": {
                        tvIsValid.setText("无效");
                        tvFeedbackContent.setText(map.get("reportFeedbackContent").toString());
                        tvFeedbackContent.setEnabled(false);
                        btnSubmit.setVisibility(View.GONE);
                        llRadioGroup.setVisibility(View.GONE);
                        break;
                    }
                    case "3": {
                        tvIsValid.setText("位置错误");
                        tvFeedbackContent.setText(map.get("reportFeedbackContent").toString());
                        tvFeedbackContent.setEnabled(false);
                        btnSubmit.setVisibility(View.GONE);
                        llRadioGroup.setVisibility(View.GONE);
                        break;
                    }
                    default: {
                        tvIsValid.setText("");
                        break;
                    }
                }
                instructQueryPicAdapter = new InstructQueryPicAdapter(HiddenDanDtlActivity.this, picList);
                instructQueryPicAdapter.notifyDataSetChanged();
                gvInstruct.setAdapter(instructQueryPicAdapter);
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(HiddenDanDtlActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
        gvInstruct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String image_url = JojtApiUtils.BASEURL + picList.get(i);
                ImageUrlFullScreen.smallImgClickFull(HiddenDanDtlActivity.this, image_url);
            }
        });
    }

    public void instructSubmit(View view) {

        //setResult(RESULT_OK);


        String strFeedbackContent = ((TextView) findViewById(R.id.feedbackContent)).getText().toString();
        RadioButton rbEffective = (RadioButton) findViewById(R.id.effective);
        RadioButton rbInvalid = (RadioButton) findViewById(R.id.invalid);
        String status;
        if (StringUtil.isEmpty(strFeedbackContent)) {
            Toast toast = Toast.makeText(this, "请填写反馈内容！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        if (rbEffective.isChecked()) {
            status = "1";
        } else if (rbInvalid.isChecked()) {
            status = "2";
        } else {
            status = "3";
        }
        pDialog.show();
        JojtApiUtils.instructSign(strUuid, strFeedbackContent, status, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (map.get("success").toString().equals("true")) {
                    Toast toast = Toast.makeText(HiddenDanDtlActivity.this, "提交成功！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    setResult(RESULT_OK, new Intent());
                    HiddenDanDtlActivity.this.finish();
                } else {
                    Toast toast = Toast.makeText(HiddenDanDtlActivity.this, "提交失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(HiddenDanDtlActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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
