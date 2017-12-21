package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.fx.GuideFxActivity;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import org.json.JSONObject;

import java.util.Map;

public class FxLoginActivity extends AppCompatActivity {
    private static final String TAG = "FxLoginActivity";
    private EditText userId,passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fx_login);
        userId = (EditText)findViewById(R.id.userId);
        passwd = (EditText)findViewById(R.id.passwd);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void userLogin(View view){
        String userId_str = userId.getText().toString();
        String passwd_str = passwd.getText().toString();
        if(StringUtil.isEmpty(userId_str)||StringUtil.isEmpty(passwd_str)){
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        JojtApiUtils.userLogin(userId_str,passwd_str, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map<String,String> map = (Map<String,String>)obj;
                String state = map.get("success");
                if("true".equals(state)){
                    try {
                        String userInfo = map.get("userInfo");
                        JSONObject jobj = new JSONObject(userInfo);
                        SharedPreferencesUtils.putValue(FxLoginActivity.this, Constants.USER_SETTING_USERID,jobj.get("userId").toString());
                        SharedPreferencesUtils.putValue(FxLoginActivity.this, Constants.USER_SETTING_USERNAME,jobj.get("userName").toString());
                        SharedPreferencesUtils.putValue(FxLoginActivity.this, Constants.USER_SETTING_UNITID,jobj.get("unitId").toString());
                        SharedPreferencesUtils.putValue(FxLoginActivity.this, Constants.USER_SETTING_USERIDNUMBER,jobj.get("userIdnumber").toString());
                        Toast.makeText(FxLoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(FxLoginActivity.this, GuideFxActivity.class);
                        startActivity(intent);
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(FxLoginActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FxLoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(FxLoginActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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
