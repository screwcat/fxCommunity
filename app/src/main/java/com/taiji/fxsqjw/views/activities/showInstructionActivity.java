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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.InstructionListViewAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class showInstructionActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private TextView noticeId;
    private TextView senderName;
    private TextView noticeTitle;
    private TextView noticeContent;
    private TextView signingStatus;
    private TextView createTime;
    private TextView signTime;
    private TextView tvSignTime;
    private Button btnSign;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_instruction);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        pDialog.show();
        btnSign = (Button) findViewById(R.id.btnSign);
        noticeId = (TextView) findViewById(R.id.noticeId);
        senderName = (TextView) findViewById(R.id.senderName);
        noticeTitle = (TextView) findViewById(R.id.noticeTitle);
        noticeContent = (TextView) findViewById(R.id.noticeContent);
        signingStatus = (TextView) findViewById(R.id.signingStatus);
        createTime = (TextView) findViewById(R.id.createTime);
        signTime = (TextView) findViewById(R.id.signTime);
        tvSignTime = (TextView) findViewById(R.id.tvSignTime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        String strNoticeId = getIntent().getStringExtra("noticeId");
        noticeId.setText(strNoticeId);

        JojtApiUtils.waitProcessDetail(strNoticeId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                try {
                    senderName.setText(map.get("senderName").toString());
                    noticeTitle.setText(map.get("noticeTitle").toString());
                    noticeContent.setText(map.get("noticeContent").toString());
                    //signingStatus.setText(map.get("signingStatus").equals("0") ? "未签收" : "已签收");
                    if (map.get("signingStatus").equals("0")) {
                        signingStatus.setBackground(getResources().getDrawable(R.drawable.sioaimi));
                        signTime.setVisibility(View.GONE);
                        tvSignTime.setVisibility(View.GONE);
                    } else {
                        signingStatus.setBackground(getResources().getDrawable(R.drawable.sioaisumi));
                        signTime.setText(sdf.format(new Date()));
                        btnSign.setVisibility(View.GONE);
                    }
                    createTime.setText(map.get("createTime").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(showInstructionActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });

    }

    public void signInstruction(View view) {
        String taskId = ((TextView) findViewById(R.id.noticeId)).getText().toString();
        //String currentUser = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERNAME, null);
        String currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        JojtApiUtils.waitProcessSign(currentUserId, taskId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (map.get("success").toString().equals("true")) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                } else {
                    Toast toast = Toast.makeText(showInstructionActivity.this, "签收失败！", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(showInstructionActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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
