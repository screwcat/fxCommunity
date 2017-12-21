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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.InstructionListViewAdapter;
import com.taiji.fxsqjw.views.adapter.KeyPersonnelAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.rest.NetworkTodoInstruction;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TodoInstructionActivity extends AppCompatActivity {

    private ListView mListView;
    private ImageButton next_magnifier_image;
    private int curPage = 0;
    private boolean isBottom = false;
    private List<Map<String, Object>> totalList = null;
    private InstructionListViewAdapter adapter = null;
    private ProgressDialog pDialog;
    private String isLast;
    private ImageButton top_task_search;
    private String searchContent = "";
    private EditText etSearchContent;
    private String signingStatus = "";
    private Button isSigned;
    private Button noSigned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_instruction);

        next_magnifier_image = (ImageButton) findViewById(R.id.next_magnifier_image);
        top_task_search = (ImageButton) findViewById(R.id.top_task_search);
        etSearchContent = (EditText) findViewById(R.id.top_task_search_edit);
        isSigned = (Button) findViewById(R.id.isSigned);
        noSigned = (Button) findViewById(R.id.noSigned);


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
        isSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingStatus = "1";
                totalList = null;
                curPage = 0;
                getList();
            }
        });
        noSigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signingStatus = "0";
                totalList = null;
                curPage = 0;
                getList();
            }
        });

        etSearchContent.addTextChangedListener(searchChange);


        mListView = (ListView) findViewById(R.id.task_listview);


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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("carManage", "onItemClick: ");
                String noticeId = ((TextView) view.findViewById(R.id.noticeId)).getText().toString();
                Intent intent = new Intent(TodoInstructionActivity.this, showInstructionActivity.class);
                intent.putExtra("noticeId", noticeId);
                startActivityForResult(intent, 0);

            }
        });

        next_magnifier_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast == "false") {
                    getList();
                    mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                } else {
                    Toast toast = Toast.makeText(TodoInstructionActivity.this, "已经到最后一页", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        top_task_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(TodoInstructionActivity.this, "开始查询。。。", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
        String currentUser = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        searchContent = etSearchContent.getText().toString();

        JojtApiUtils.getWaitProcess(curPage, currentUser, searchContent, signingStatus, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (totalList == null) {
                    totalList = ((List<Map<String, Object>>) map.get("result"));
                } else {
                    totalList.addAll((List<Map<String, Object>>) map.get("result"));
                }
                adapter = new InstructionListViewAdapter(TodoInstructionActivity.this, totalList);
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
                curPage++;
                isLast = map.get("lastPage").toString();
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(TodoInstructionActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            totalList = null;
            curPage = 0;
            getList();
            Toast toast = Toast.makeText(TodoInstructionActivity.this, "签收成功！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
