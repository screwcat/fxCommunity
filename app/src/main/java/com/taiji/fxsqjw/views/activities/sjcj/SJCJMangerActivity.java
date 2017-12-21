package com.taiji.fxsqjw.views.activities.sjcj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;


public class SJCJMangerActivity extends AppCompatActivity {

    private Button sjcj_manage_one_Button;

    private LinearLayout sjcj_LinearLayout;

    private Button sjcj_manage_two_Button;

    private Button sjcj_manage_three_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcjmanger);
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
        sjcj_manage_one_Button=(Button)findViewById(R.id.sjcj_manage_one_Button);
        sjcj_LinearLayout=(LinearLayout)findViewById(R.id.sjcj_LinearLayout);
        sjcj_manage_two_Button=(Button)findViewById(R.id.sjcj_manage_two_Button);
        sjcj_manage_three_Button=(Button)findViewById(R.id.sjcj_manage_three_Button);

        sjcj_manage_one_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sjcj_LinearLayout.setVisibility(v.VISIBLE);
            }
        });
        sjcj_manage_two_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SJCJMangerActivity.this,SJCJPersonnelMessageActivity.class);
                startActivity(intent);
            }
        });
        sjcj_manage_three_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SJCJMangerActivity.this,SJCJCHouseMessageActivity.class);
                startActivity(intent);
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
