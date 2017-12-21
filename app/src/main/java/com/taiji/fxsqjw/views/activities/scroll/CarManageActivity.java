package com.taiji.fxsqjw.views.activities.scroll;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.CarListViewAdapter;
import com.taiji.fxsqjw.views.adapter.ScrollAdapter;
import com.taiji.fxsqjw.views.rest.NetworkTask;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarManageActivity extends Activity {

    private ListView mListView;
    private CarListViewAdapter mAdapter;
    private Context mContext = this;
    private EditText top_appoint_search_edit;
    private ImageButton top_appoint_search,next_btn_rl_image;
    private ScrollAdapter adapter = null;
    private List<Map<String, String>> totalList = null;
    private boolean isBottom = false;

    private int curPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);

        top_appoint_search = (ImageButton) findViewById(R.id.top_appoint_search);
        next_btn_rl_image = (ImageButton) findViewById(R.id.next_btn_rl_image);
        top_appoint_search_edit = (EditText) findViewById(R.id.top_appoint_search_edit);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        mListView = (ListView) findViewById(R.id.listview);
        totalList = new ArrayList<Map<String, String>>();
        adapter = new ScrollAdapter(this, totalList);
        new NetworkTask(this,totalList,adapter).execute("http://10.82.133.147/test1.htm?pageNo="+curPage);
        // 自定义适配器。
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isBottom) {
                    next_btn_rl_image.setVisibility(View.VISIBLE);
                } else {
                    next_btn_rl_image.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                isBottom = ((firstVisibleItem + visibleItemCount) == totalItemCount);
            }
        });
        next_btn_rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkTask(CarManageActivity.this,totalList,adapter).execute("http://10.82.133.147/test1.htm?pageNo="+curPage);
                mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                curPage++;
            }
        });

        //initCarList();
    }


    private void initCarList() {
        String uid = SharedPreferencesUtils.getValue(mContext, Constants.USER_SETTING_USERNAME,null);
//        JojtApiUtils.initCarList(uid, new JojtApiUtils.ApiCallBack() {
//            @Override
//            public void onSuccess(Object obj) {
//                List<Map> carList = (List<Map>)obj;
//                if(carList!=null&&carList.size()<1){
//                    Toast.makeText(mContext, "暂无车辆信息，请添加", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                mAdapter = new CarListViewAdapter(CarManageActivity.this,carList);
//                mListView.setAdapter(mAdapter);
//                mAdapter.setMode(Attributes.Mode.Single);
//                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Log.i("carManage", "onItemClick: ");
//                        ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
//                    }
//                });
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(mContext, "获取车辆信息失败", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
