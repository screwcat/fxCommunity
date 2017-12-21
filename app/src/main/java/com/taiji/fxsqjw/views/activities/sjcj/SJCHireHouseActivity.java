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

import java.util.Calendar;

public class SJCHireHouseActivity extends AppCompatActivity {

    private Button SJCJ_hire_house_Button;
    private EditText room_num,rent_square,money,relation,credential_code,phone;
    private Spinner credential_type;
    private TextView rent_time;
    private ImageButton sjcj_hire_date_btn;
    private int myear, mmonth, mdayOfMonth;
    protected static final int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjchire_house);
        SJCJ_hire_house_Button=(Button)findViewById(R.id.SJCJ_hire_house_Button);
        credential_type=(Spinner)findViewById(R.id.credential_type);
        room_num=(EditText)findViewById(R.id.room_num);
        rent_square=(EditText)findViewById(R.id.rent_square);
        rent_time=(TextView) findViewById(R.id.rent_time);
        money=(EditText)findViewById(R.id.money);
        relation=(EditText)findViewById(R.id.relation);
        credential_code=(EditText)findViewById(R.id.credential_code);
        phone=(EditText)findViewById(R.id.phone);
        sjcj_hire_date_btn=(ImageButton)findViewById(R.id.sjcj_hire_date_btn);
        final String userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Intent intent=getIntent();
        final String house_id= intent.getStringExtra("houseId");

        sjcj_hire_date_btn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        SJCJ_hire_house_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SJCHireHouseActivity.this,SJCJCHouseMessageActivity.class);
                startActivity(intent);
                
                String credential_type_str=String.valueOf(credential_type.getSelectedItemPosition()+1);
                String room_num_str=room_num.getText().toString();
                String rent_square_str=rent_square.getText().toString();
                String rent_time_str=rent_time.getText().toString();
                String money_str=money.getText().toString();
                String relation_str=relation.getText().toString();
                String credential_code_str=credential_code.getText().toString();
                String phone_str=phone.getText().toString();
                JojtApiUtils.hireHouseAdd(userId,house_id,credential_type_str,room_num_str,rent_square_str,rent_time_str,money_str,relation_str,credential_code_str,phone_str, new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Toast.makeText(SJCHireHouseActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError() {
                        Toast.makeText(SJCHireHouseActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
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
        rent_time.setText(sb.toString());
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
