package com.taiji.fxsqjw.views.activities.sjcj;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.FxLoginActivity;
import com.taiji.fxsqjw.views.activities.fx.GuideFxActivity;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.taiji.fxsqjw.views.R.array.spinner_sjcj_personnel_message2;


public class SJCJPersonnelMessageActivity extends AppCompatActivity {

    private Button sjcj_manage_one_Button;

    private Button sjcj_person_button;

    private Button sjcj_personnel_messageButton;

    private TextView person_message1;

    private Spinner spinner_sjcj_personnel_message2;

    private Spinner spinner_sjcj_personnel_message3;

    private Spinner credential_type;

    private EditText credential_code, phone, work, unit, location;

    private Spinner is_rent, liver_type;

    private Button sjcj_person_message_save, sjcj_person_message_continue_save;

    private String mldzid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcj_personnel_message);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        sjcj_person_button = (Button) findViewById(R.id.sjcj_person_button);
        sjcj_personnel_messageButton = (Button) findViewById(R.id.sjcj_personnel_messageButton);
        person_message1 = (TextView) findViewById(R.id.person_message1);
        spinner_sjcj_personnel_message2 = (Spinner) findViewById(R.id.sjcj_personnel_message2);
        spinner_sjcj_personnel_message3 = (Spinner) findViewById(R.id.sjcj_personnel_message3);

        credential_type = (Spinner) findViewById(R.id.credential_type);
        credential_code = (EditText) findViewById(R.id.credential_code);
        phone = (EditText) findViewById(R.id.phone);
        work = (EditText) findViewById(R.id.work);
        unit = (EditText) findViewById(R.id.unit);
        location = (EditText) findViewById(R.id.location);
        is_rent = (Spinner) findViewById(R.id.sjcj_personnel_message2);
        liver_type = (Spinner) findViewById(R.id.sjcj_personnel_message3);

        sjcj_person_message_save = (Button) findViewById(R.id.sjcj_person_message_save);
        sjcj_person_message_continue_save = (Button) findViewById(R.id.sjcj_person_message_continue_save);

        final String userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        Intent intent=new Intent();
        mldzid = intent.getStringExtra("mldzid");


        credential_type.setVisibility(View.VISIBLE);

        sjcj_person_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SJCJPersonnelMessageActivity.this, SJCJAddressWriteActivity.class);
                intent.putExtra("from", 1);
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        spinner_sjcj_personnel_message2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sjcj_person_message_save.setVisibility(view.INVISIBLE);
                    sjcj_person_message_continue_save.setVisibility(view.INVISIBLE);
                    sjcj_personnel_messageButton.setVisibility(view.VISIBLE);
                } else if (position == 1) {
                    sjcj_personnel_messageButton.setVisibility(view.INVISIBLE);
                    sjcj_person_message_save.setVisibility(view.VISIBLE);
                    sjcj_person_message_continue_save.setVisibility(view.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sjcj_personnel_messageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String credential_type_str = String.valueOf(credential_type.getSelectedItemPosition() + 1);
                String credential_code_str = credential_code.getText().toString();
                String phone_str = phone.getText().toString();
                String work_str = work.getText().toString();
                String unit_str = unit.getText().toString();
                String location_str = location.getText().toString();
                String is_rent_str = String.valueOf(is_rent.getSelectedItemPosition() + 1);
                String liver_type_str = String.valueOf(liver_type.getSelectedItemPosition() + 1);

                JojtApiUtils.personMessageAdd(userId, mldzid, credential_type_str, credential_code_str, phone_str, work_str, unit_str, location_str, is_rent_str, liver_type_str, new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map = (Map) obj;
                        Toast.makeText(SJCJPersonnelMessageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                        int i = spinner_sjcj_personnel_message3.getSelectedItemPosition();
                        if (i == 0) {
                            Intent intent = new Intent(SJCJPersonnelMessageActivity.this, SJCJShackPopulationActivity.class);
                            intent.putExtra("personId", map.get("personId").toString());
                            startActivity(intent);
                        } else if (i == 1) {
                            Intent intent = new Intent(SJCJPersonnelMessageActivity.this, SJCJentrustPopulationActivity.class);
                            intent.putExtra("personId", map.get("personId").toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(SJCJPersonnelMessageActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        sjcj_person_message_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credential_type_str = String.valueOf(credential_type.getSelectedItemPosition() + 1);
                String credential_code_str = credential_code.getText().toString();
                String phone_str = phone.getText().toString();
                String work_str = work.getText().toString();
                String unit_str = unit.getText().toString();
                String location_str = location.getText().toString();
                String is_rent_str = String.valueOf(is_rent.getSelectedItemPosition() + 1);
                String liver_type_str = String.valueOf(liver_type.getSelectedItemPosition() + 1);

                JojtApiUtils.personMessageAdd(userId, mldzid, credential_type_str, credential_code_str, phone_str, work_str, unit_str, location_str, is_rent_str, liver_type_str, new JojtApiUtils.ApiCallBack() {
                    @Override
                    public void onSuccess(Object obj) {
                        Map map = (Map) obj;
                        Toast.makeText(SJCJPersonnelMessageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.putExtra("personId", map.get("personId").toString());

                        // SharedPreferencesUtils.putValue(SJCJPersonnelMessageActivity.this,"abc","123");

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(SJCJPersonnelMessageActivity.this, "服务器异常，请重新添加。", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        sjcj_person_message_continue_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SJCJPersonnelMessageActivity.this, SJCJPersonnelMessageActivity.class);
                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String dzmc = data.getStringExtra("dzmc");
            mldzid = data.getStringExtra("mldzid");
            person_message1.setText(dzmc);
        }
    }
}
