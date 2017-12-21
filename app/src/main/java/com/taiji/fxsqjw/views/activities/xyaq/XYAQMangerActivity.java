package com.taiji.fxsqjw.views.activities.xyaq;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.sjxf.SJXFMangerActivity;
import com.taiji.fxsqjw.views.adapter.SJXFManageScrollAdapter;
import com.taiji.fxsqjw.views.adapter.XYAQManageScrollAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.rest.SJXFManageNetworkTask;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XYAQMangerActivity extends AppCompatActivity {

    private EditText top_appoint_search_edit;
    private ImageButton top_appoint_search,next_btn_rl_image_xyaq_manager;
    private ListView listview_xyaq_manager;
    private XYAQManageScrollAdapter adapter = null;
    private List<Map<String, String>> totalList = null;
    private boolean isBottom = false;
    private ProgressDialog pDialog;
    private int curPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyaqmanger);
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
        top_appoint_search_edit = (EditText) findViewById(R.id.top_task_search_edit);
        top_appoint_search_edit.addTextChangedListener(searchContentXYAQ);
        top_appoint_search = (ImageButton) findViewById(R.id.top_appoint_search);
        next_btn_rl_image_xyaq_manager = (ImageButton) findViewById(R.id.next_btn_rl_image_xyaq_manager);
        listview_xyaq_manager = (ListView) findViewById(R.id.listview_item_sjxfmanager);
        totalList = new ArrayList<Map<String, String>>();
        adapter = new XYAQManageScrollAdapter(this, totalList);
        //new SJXFManageNetworkTask(this,totalList,adapter).execute("https://www.baidu.com?pageNo="+curPage);
        // 自定义适配器。
        listview_xyaq_manager.setAdapter(adapter);
        listview_xyaq_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom) {
                    next_btn_rl_image_xyaq_manager.setVisibility(View.VISIBLE);
                } else {
                    next_btn_rl_image_xyaq_manager.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });
        listview_xyaq_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("carManage", "onItemClick: ");
                TextView sjxf_manage_tv_one = (TextView) view.findViewById(R.id.sjxf_manage_tv_one);
                TextView sjxf_manage_tv_two = (TextView) view.findViewById(R.id.sjxf_manage_tv_two);
                TextView sjxf_manage_tv_three = (TextView) view.findViewById(R.id.sjxf_manage_tv_three);
                TextView sjxf_manage_tv_unitCode = (TextView) view.findViewById(R.id.sjxf_manage_tv_unitCode);

                Intent intent = new Intent();
                intent.setClass(XYAQMangerActivity.this, XYAQDetailsActivity.class);

                intent.putExtra("dwmc",sjxf_manage_tv_one.getText().toString());
                intent.putExtra("address",sjxf_manage_tv_two.getText().toString());
                intent.putExtra("dwlb",sjxf_manage_tv_three.getText().toString());
                intent.putExtra("unitCode",sjxf_manage_tv_unitCode.getText().toString());
                startActivity(intent);
            }
        });
        next_btn_rl_image_xyaq_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBottom){
                    getManageDataList();
                    listview_xyaq_manager.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    curPage++;
                }else{
                    Toast.makeText(XYAQMangerActivity.this, "已经到最后一页", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        getManageDataList();

    }

    public void getManageDataList(){
        pDialog.show();
        JojtApiUtils.campusSecurityQuery(String.valueOf(curPage), SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null), new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map)obj;
                if (totalList == null) {
                    totalList = ((List<Map<String, String>>) map.get("result"));
                } else {
                    totalList.addAll((List<Map<String, String>>) map.get("result"));
                }
                adapter.notifyDataSetChanged();
                curPage++;
                isBottom = Boolean.valueOf(map.get("lastPage").toString());
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(XYAQMangerActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }
    private TextWatcher searchContentXYAQ = new TextWatcher() {
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
            String searchContent_str=top_appoint_search_edit.getText().toString();
            JojtApiUtils.campusSecurityQueryFrame(String.valueOf(curPage),String.valueOf(searchContent_str), new JojtApiUtils.ApiCallBack() {
                @Override
                public void onSuccess(Object obj) {
                    Map map = (Map)obj;
                    if (totalList == null) {
                        totalList = ((List<Map<String, String>>) map.get("result"));
                    } else {
                        totalList.addAll((List<Map<String, String>>) map.get("result"));
                    }
                    adapter = new XYAQManageScrollAdapter(XYAQMangerActivity.this, totalList);
                    adapter.notifyDataSetChanged();
                    listview_xyaq_manager.setAdapter(adapter);
                    curPage++;
                    isBottom = Boolean.valueOf(map.get("lastPage").toString());
                    pDialog.dismiss();
                }

                @Override
                public void onError() {
                    Toast.makeText(XYAQMangerActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            });
        }
    };
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
