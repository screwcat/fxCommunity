package com.taiji.fxsqjw.views.activities;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.HiddenDangerAdapter;
import com.taiji.fxsqjw.views.adapter.KeyPersonnelAdapter;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.rest.NetworkHiddenDanger;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class Tab1Fragment extends Fragment {

    private ListView mListView;
    private ImageButton next_magnifier_image;
    private int curPage = 0;
    private boolean isBottom = false;
    private List<Map<String, Object>> totalList = null;
    private HiddenDangerAdapter adapter = null;
    private ProgressDialog pDialog;
    private String isLast;
    private EditText etSearchContent;
    private String searchContent = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab1, null);

        next_magnifier_image = (ImageButton) view.findViewById(R.id.moreDanger);

        SystemBarTintManager tintManager = new SystemBarTintManager(this.getActivity());
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色

        mListView = (ListView) view.findViewById(R.id.instructListView);
        etSearchContent = (EditText) view.findViewById(R.id.top_task_search_edit);
        etSearchContent.addTextChangedListener(searchChange);
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");

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
                Log.i("carManage", "onItemClick:");
                String uuid = ((TextView) view.findViewById(R.id.uuid)).getText().toString();
                Intent intent = new Intent(Tab1Fragment.this.getActivity(), HiddenDanDtlActivity.class);
                intent.putExtra("uuid", uuid);
                //startActivity(intent);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);

            }
        });

        next_magnifier_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLast == "false") {
                    getList();
                    mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                } else {
                    Toast toast = Toast.makeText(Tab1Fragment.this.getActivity(), "已经到最后一页", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            totalList = null;
            curPage = 0;
            getList();
        }
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
        pDialog.show();
        searchContent = etSearchContent.getText().toString();

        String currentUserId = SharedPreferencesUtils.getValue(Tab1Fragment.this.getContext(), Constants.USER_SETTING_USERID, null);
        JojtApiUtils.getInstruct(curPage, searchContent, currentUserId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Map map = (Map) obj;
                if (totalList == null) {
                    totalList = ((List<Map<String, Object>>) map.get("result"));
                } else {
                    totalList.addAll((List<Map<String, Object>>) map.get("result"));
                }
                adapter = new HiddenDangerAdapter(Tab1Fragment.this.getActivity(), totalList);
                adapter.notifyDataSetChanged();
                mListView.setAdapter(adapter);
                curPage++;
                isLast = map.get("lastPage").toString();
                pDialog.dismiss();
            }

            @Override
            public void onError() {
                Toast.makeText(Tab1Fragment.this.getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }


}
