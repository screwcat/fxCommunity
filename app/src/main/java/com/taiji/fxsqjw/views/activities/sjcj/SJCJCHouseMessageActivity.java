package com.taiji.fxsqjw.views.activities.sjcj;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.Map;

public class SJCJCHouseMessageActivity extends AppCompatActivity {

    private Button sjcj_house_button,sjcj_house_message_Button,house_next_step,sjcj_house_message_continue_save;
    private EditText room_number,square,owner_credential_code,owner_phone;
    private Spinner house_character,house_type,house_use,is_rent,owner_credential_type;
    private TextView address;
    private  String mldzid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcj_house_message);
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
        sjcj_house_button=(Button)findViewById(R.id.sjcj_house_button);
        sjcj_house_message_Button=(Button)findViewById(R.id.sjcj_house_message_Button);
        address=(TextView)findViewById(R.id.address);
        room_number=(EditText)findViewById(R.id.room_number);
        square=(EditText)findViewById(R.id.square);
        owner_credential_code=(EditText)findViewById(R.id.owner_credential_code);
        owner_phone=(EditText)findViewById(R.id.owner_phone);

        house_character=(Spinner)findViewById(R.id.house_character);
        house_type=(Spinner)findViewById(R.id.house_type);
        house_use=(Spinner)findViewById(R.id.house_use);
        is_rent=(Spinner)findViewById(R.id.is_rent);
        owner_credential_type=(Spinner)findViewById(R.id.owner_credential_type);
        house_next_step=(Button)findViewById(R.id.house_next_step);
        sjcj_house_message_continue_save=(Button)findViewById(R.id.sjcj_house_message_continue_save);
        final String userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null);



        sjcj_house_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SJCJCHouseMessageActivity.this,SJCJAddressWriteActivity.class);
                intent.putExtra("from",2);
                //startActivity(intent);
                startActivityForResult(intent, 2);
            }
        });
        is_rent.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    sjcj_house_message_Button.setVisibility(view.INVISIBLE);
                    sjcj_house_message_continue_save.setVisibility(view.INVISIBLE);
                    house_next_step.setVisibility(view.VISIBLE);
                } else if(position==1){
                    //Intent intent=new Intent(SJCJCHouseMessageActivity.this,SJCJMangerActivity.class);
                    sjcj_house_message_Button.setVisibility(view.VISIBLE);
                    sjcj_house_message_continue_save.setVisibility(view.VISIBLE);
                    house_next_step.setVisibility(view.INVISIBLE);
                   // startActivity(intent);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        house_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String house_character_str = String.valueOf(house_character.getSelectedItemPosition()+1);
                String house_type_str = String.valueOf(house_type.getSelectedItemPosition()+1);
                String house_use_str = String.valueOf(house_use.getSelectedItemPosition()+1);
                String is_rent_str = String.valueOf(is_rent.getSelectedItemPosition()+1);
                String owner_credential_type_str = String.valueOf(owner_credential_type.getSelectedItemPosition()+1);

                String address_str=address.getText().toString();
                String room_number_str=room_number.getText().toString();
                String square_str=square.getText().toString();
                String owner_credential_code_str=owner_credential_code.getText().toString();
                String owner_phone_str=owner_phone.getText().toString();

                JojtApiUtils.houseMessageAdd(userId,mldzid,house_character_str,house_type_str,house_use_str,is_rent_str,owner_credential_type_str,address_str,room_number_str,square_str,owner_credential_code_str,owner_phone_str,new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map=(Map)obj;
                        Toast.makeText(SJCJCHouseMessageActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SJCJCHouseMessageActivity.this,SJCHireHouseActivity.class);
                        intent.putExtra("from",1);
                        intent.putExtra("houseId",map.get("houseId").toString());
                        startActivity(intent);
                    }
                    @Override
                    public void onError() {
                        Toast.makeText(SJCJCHouseMessageActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        sjcj_house_message_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String house_character_str = String.valueOf(house_character.getSelectedItemPosition()+1);
                String house_type_str = String.valueOf(house_type.getSelectedItemPosition()+1);
                String house_use_str = String.valueOf(house_use.getSelectedItemPosition()+1);
                String is_rent_str = String.valueOf(is_rent.getSelectedItemPosition()+1);
                String owner_credential_type_str = String.valueOf(owner_credential_type.getSelectedItemPosition()+1);

                String address_str=address.getText().toString();
                String room_number_str=room_number.getText().toString();
                String square_str=square.getText().toString();
                String owner_credential_code_str=owner_credential_code.getText().toString();
                String owner_phone_str=owner_phone.getText().toString();

                JojtApiUtils.houseMessageAdd(userId,mldzid,house_character_str,house_type_str,house_use_str,is_rent_str,owner_credential_type_str,address_str,room_number_str,square_str,owner_credential_code_str,owner_phone_str,new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map=(Map)obj;
                        Toast.makeText(SJCJCHouseMessageActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("from",1);
                        intent.putExtra("houseId",map.get("houseId").toString());
                    }
                    @Override
                    public void onError() {
                        Toast.makeText(SJCJCHouseMessageActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        sjcj_house_message_continue_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SJCJCHouseMessageActivity.this,SJCJCHouseMessageActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            String dzmc = data.getStringExtra("dzmc");
            mldzid = data.getStringExtra("mldzid");
            address.setText(dzmc);
        }
    }
}
