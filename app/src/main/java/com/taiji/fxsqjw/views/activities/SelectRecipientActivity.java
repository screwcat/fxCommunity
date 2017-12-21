package com.taiji.fxsqjw.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.layout.GuideGridLayout;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.FileAndParamUploadUtil;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectRecipientActivity extends AppCompatActivity implements FileAndParamUploadUtil.OnUploadProcessListener {
    GuideGridLayout ggDispatcherGroup;
    private static final String TAG = "SelectRecipientActivity";
    private ProgressDialog pDialog;
    private String strNoticeTitle;
    private String strNoticeContent;
    private String strFilePath;
    private String strReceiveIds;
    private static final String requestURL = JojtApiUtils.BASEURL + "communityPolice/instruction.instructDispatch.do";
    //private static final String requestURL = "http://10.82.181.19:8730/communityPolice/instruction.instructDispatch.do";
    File file;
    private static final String fileKey = "android_qd_by_gps";
    String currentUserId;
    String currentUserName;
    List listGroup;
    int topIndex;
    private HashMap<String, String> picList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        strNoticeTitle = getIntent().getStringExtra("noticeTitle");
        strNoticeContent = getIntent().getStringExtra("noticeContent");
        strFilePath = getIntent().getStringExtra("filePath");
        picList = (HashMap<String, String>) getIntent().getSerializableExtra("picList");
        ggDispatcherGroup = (GuideGridLayout) findViewById(R.id.listDispatcher);
        currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
        JojtApiUtils.instructDispatcherQuery(currentUserId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                listGroup = (List) ((Map) obj).get("result");
                ggDispatcherGroup.setGridAdapter(new GuideGridLayout.GridAdatper() {
                    @Override
                    public View getView(int index) {
                        View view = getLayoutInflater().inflate(R.layout.dispatcher_group_item, null);
                        TextView tvGroupName = (TextView) view.findViewById(R.id.groupName);
                        tvGroupName.setText(((Map) listGroup.get(index)).get("groupName").toString());
                        final GuideGridLayout ggDispatcherGroupDtl = (GuideGridLayout) view.findViewById(R.id.listDispatcherGroupDtl);
                        topIndex = index;
                        ggDispatcherGroupDtl.setGridAdapter(new GuideGridLayout.GridAdatper() {

                            @Override
                            public View getView(int ind) {
                                View viewDtl = getLayoutInflater().inflate(R.layout.instruct_dispatcher_item, null);
                                TextView tvPersonNum = (TextView) viewDtl.findViewById(R.id.personNum);
                                TextView tvDispatcherName = (TextView) viewDtl.findViewById(R.id.dispatcherName);
                                tvPersonNum.setText(((Map) ((List) ((Map) listGroup.get(topIndex)).get("personList")).get(ind)).get("personNum").toString());
                                tvDispatcherName.setText(((Map) ((List) ((Map) listGroup.get(topIndex)).get("personList")).get(ind)).get("name").toString());
                                tvDispatcherName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View viewLabel) {
                                        TextView tvpc = (TextView) viewLabel.findViewById(R.id.dispatcherName);
                                        if (tvpc.getCurrentTextColor() == -1) {
                                            tvpc.setTextColor(Color.parseColor("#349afe"));
                                            tvpc.setBackground(getResources().getDrawable(R.drawable.textview_border));
                                        } else {
                                            tvpc.setTextColor(Color.parseColor("#ffffff"));
                                            tvpc.setBackgroundColor(Color.parseColor("#349afe"));
                                        }
                                    }
                                });
                                return viewDtl;
                            }

                            @Override
                            public int getCount() {
                                return ((List) ((Map) listGroup.get(topIndex)).get("personList")).size();
                            }
                        });


                        return view;
                    }

                    @Override
                    public int getCount() {
                        return listGroup.size();
                    }
                });
            }

            @Override
            public void onError() {
                Toast.makeText(SelectRecipientActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });


    }

    public void confirmRelease(View view) {
        pDialog.show();
        strReceiveIds = "";
        for (int i = 0; i < ggDispatcherGroup.getChildCount(); i++) {
            GuideGridLayout ggDispatcherGroupDtl = (GuideGridLayout) ggDispatcherGroup.getChildAt(i).findViewById(R.id.listDispatcherGroupDtl);
            for (int j = 0; j < ggDispatcherGroupDtl.getChildCount(); j++) {
                if (((TextView) ggDispatcherGroupDtl.getChildAt(j).findViewById(R.id.dispatcherName)).getCurrentTextColor() == -1) {
                    strReceiveIds += ((TextView) ggDispatcherGroupDtl.getChildAt(j).findViewById(R.id.personNum)).getText() + "|";
                }
            }


//            if (((TextView) ggDispatcherGroup.getChildAt(i).findViewById(R.id.dispatcherName)).getCurrentTextColor() == -1) {
//                strReceiveIds += ((TextView) ggDispatcherGroup.getChildAt(i).findViewById(R.id.personNum)).getText() + "|";
//            }
        }
        if (!"".equals(strReceiveIds)) {
            strReceiveIds = strReceiveIds.substring(0, strReceiveIds.length() - 1);
            if (StringUtil.isEmpty(strFilePath)) {
                file = null;
            } else {
                try {
                    file = new File(strFilePath);
                } catch (Exception e) {
                    Toast.makeText(SelectRecipientActivity.this, "文件路径错误！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    file = null;
                }
            }
            currentUserName = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERNAME, null);
            currentUserId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID, null);
            final FileAndParamUploadUtil uploadUtil = FileAndParamUploadUtil.getInstance();
            uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态
            final Map<String, String> params = new HashMap<String, String>();
            params.put("noticeTitle", strNoticeTitle);
            params.put("noticeContent", strNoticeContent);
            params.put("receiveIds", strReceiveIds);
            params.put("senderId", currentUserId);
            params.put("senderName", currentUserName);
            //uploadUtil.uploadFile(file, fileKey, requestURL, params);
//            Toast toast = Toast.makeText(SelectRecipientActivity.this, strReceiveIds, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            new Thread(new Runnable() {  //开启线程上传文件
                @Override
                public void run() {
                    uploadUtil.multiUploadFile(picList, fileKey, requestURL, params);
                }
            }).start();
        } else {
            Toast toast = Toast.makeText(SelectRecipientActivity.this, "请选择接收人！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            pDialog.dismiss();
        }
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        Log.i(TAG, message);
        //qkqd_gps_ll_qd_success.setVisibility(View.VISIBLE);
        pDialog.dismiss();
        if (responseCode == 1) {
            Looper.prepare();
            Toast.makeText(this, "发布成功！", Toast.LENGTH_SHORT).show();
            pDialog.dismiss();
            setResult(RESULT_OK, new Intent());
            SelectRecipientActivity.this.finish();
            Looper.loop();
        } else {
            Looper.prepare();
            Toast.makeText(this, "发布失败！", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Log.i(TAG, "onUploadProcess:" + uploadSize);
    }

    @Override
    public void initUpload(int fileSize) {
        Log.i(TAG, "initUpload:" + fileSize);
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
