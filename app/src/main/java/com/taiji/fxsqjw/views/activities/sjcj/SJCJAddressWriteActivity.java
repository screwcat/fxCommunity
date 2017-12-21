package com.taiji.fxsqjw.views.activities.sjcj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.Tab1Fragment;
import com.taiji.fxsqjw.views.adapter.HiddenDangerAdapter;
import com.taiji.fxsqjw.views.adapter.SJCJAddressWriteScrollAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.rest.SJCJAddressWriteNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SJCJAddressWriteActivity extends AppCompatActivity {

    private Button sjcj_address_button;
    private TextView address;
    private SJCJAddressWriteScrollAdapter adapter = null;
    private List<Map<String, String>> totalList = null;
    private ListView sjcj_address_listview;
    private int from;
    private EditText dzmc_edit;
    private String dzmc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sjcjaddress_write);


        dzmc_edit = (EditText) findViewById(R.id.dzmc_edit);
        dzmc_edit.addTextChangedListener(dzmcEditStr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        sjcj_address_button = (Button) findViewById(R.id.sjcj_address_button);
        sjcj_address_listview = (ListView) findViewById(R.id.sjcj_address_listview);
        totalList = new ArrayList<Map<String, String>>();
        adapter = new SJCJAddressWriteScrollAdapter(this, totalList);
        new SJCJAddressWriteNetworkTask(this, totalList, adapter).execute(JojtApiUtils.BASEURL + "dataColletcion/data.addressQuery.do");
        sjcj_address_listview.setAdapter(adapter);
        address = (TextView) findViewById(R.id.address);
        from = getIntent().getIntExtra("from", 0);
        sjcj_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sjcj_address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView dzmc = (TextView) view.findViewById(R.id.dzmc);
                TextView mldzid = (TextView) view.findViewById(R.id.mldzid);

                Intent intent = new Intent();
                if (from == 1) {
                    intent = new Intent(SJCJAddressWriteActivity.this, SJCJPersonnelMessageActivity.class);
                } else if (from == 2) {
                    intent = new Intent(SJCJAddressWriteActivity.this, SJCJCHouseMessageActivity.class);
                }
                intent.putExtra("dzmc", dzmc.getText().toString());
                intent.putExtra("mldzid", mldzid.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }

        });

    }

    private TextWatcher dzmcEditStr = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            dzmc = dzmc_edit.getText().toString();
            JojtApiUtils.addressSearchTask(dzmc, new JojtApiUtils.ApiCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    List list = (List) obj;
                    adapter = new SJCJAddressWriteScrollAdapter(SJCJAddressWriteActivity.this, list);
                    sjcj_address_listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onError() {
                    Toast.makeText(SJCJAddressWriteActivity.this, "输入有误，请重新添加。", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

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
