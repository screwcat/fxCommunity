package com.taiji.fxsqjw.views.activities.sjcj;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.security.PrivateKey;
import java.util.Calendar;

public class SJCJShackPopulationActivity extends AppCompatActivity {

    private Spinner census_register_type;
    private Button sjcj_shack_population;
    private ImageButton sjcj_date_btn;
    private TextView shack_time;
    private int myear, mmonth, mdayOfMonth;
    protected static final int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcjshack_population);
        census_register_type=(Spinner)findViewById(R.id.census_register_type);
        sjcj_shack_population=(Button)findViewById(R.id.sjcj_shack_population);
        sjcj_date_btn=(ImageButton)findViewById(R.id.sjcj_date_btn);
        shack_time=(TextView) findViewById(R.id.shack_time);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        final String userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null);
        Intent intent=getIntent();
        final String person_id= intent.getStringExtra("personId");

        sjcj_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        sjcj_shack_population.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String census_register_type_str = String.valueOf(census_register_type.getSelectedItemPosition()+1);
                String shack_time_str=shack_time.getText().toString();

                JojtApiUtils.shackLiveAdd(userId,person_id,census_register_type_str,shack_time_str,new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {


                        Toast.makeText(SJCJShackPopulationActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SJCJShackPopulationActivity.this,SJCJPersonnelMessageActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(SJCJShackPopulationActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case 1:
                Calendar now = Calendar.getInstance();
                return new DatePickerDialog(this,mdateListener,now.get(Calendar.YEAR),now.get(Calendar.MONTH)-1,now.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myear = year;
            mmonth = month;
            mdayOfMonth = dayOfMonth;
            display();
        }
    };

    public void display(){
        StringBuffer sb = new StringBuffer().append(myear).append("-").append(mmonth).append("-").append(mdayOfMonth);
        shack_time.setText(sb.toString());
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
