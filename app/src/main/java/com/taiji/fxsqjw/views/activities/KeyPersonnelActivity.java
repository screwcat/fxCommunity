package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.KeyPersonnelAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyPersonnelActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageButton next_magnifier_image;
    private int curPage = 0;
    private boolean isBottom = false;
    private List<Map<String, Object>> totalList = null;
    private KeyPersonnelAdapter adapter = null;
    private ProgressDialog pDialog;
    private String isLast;
    private EditText etSearchContent;
    private String searchContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_personnel);

        next_magnifier_image = (ImageButton) findViewById(R.id.morePersonnel);
        etSearchContent = (EditText) findViewById(R.id.top_task_search_edit);
        etSearchContent.addTextChangedListener(searchChange);

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
        mListView = (ListView) findViewById(R.id.personnelListview);


        getList();


        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (isBottom) {
                    next_magnifier_image.setVisibility(View.VISIBLE);
                } else {
                    next_magnifier_image.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("carManage", "onItemClick: ");
                String objectId = ((TextView) view.findViewById(R.id.objectId)).getText().toString();
                String objectName = ((TextView) view.findViewById(R.id.objectName)).getText().toString();
                String documentNumber = ((TextView) view.findViewById(R.id.documentNumber)).getText().toString();
                String cityCodeAddress = ((TextView) view.findViewById(R.id.cityCodeAddress)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
                String photoPath = ((TextView) view.findViewById(R.id.photoPath)).getText().toString();
                String personType = ((TextView) view.findViewById(R.id.personType)).getText().toString();
                Intent intent = new Intent(KeyPersonnelActivity.this, RealisticActivity.class);
                intent.putExtra("objectId", objectId);
                intent.putExtra("objectName", objectName);
                intent.putExtra("photoPath", objectName);
                intent.putExtra("documentNumber", documentNumber);
                intent.putExtra("cityCodeAddress", cityCodeAddress);
                intent.putExtra("address", address);
                intent.putExtra("photoPath", photoPath);
                intent.putExtra("personType", personType);
                startActivity(intent);

            }
        });

        next_magnifier_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast == "false") {
                    getList();
                    mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                } else {
                    Toast toast = Toast.makeText(KeyPersonnelActivity.this, "已经到最后一页", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

    private TextWatcher searchChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            totalList = null;
            curPage = 0;
            getList();
        }
    };

    protected void getList() {
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        pDialog.show();
        searchContent = etSearchContent.getText().toString();
        String currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        JojtApiUtils.getStressObject(curPage, searchContent, currentUserId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (totalList == null) {
                    totalList = ((List<Map<String, Object>>) map.get("ret"));
                } else {
                    totalList.addAll((List<Map<String, Object>>) map.get("ret"));
                }
                adapter = new KeyPersonnelAdapter(KeyPersonnelActivity.this, totalList);
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
                curPage++;
                isLast = map.get("lastPage").toString();
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(KeyPersonnelActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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