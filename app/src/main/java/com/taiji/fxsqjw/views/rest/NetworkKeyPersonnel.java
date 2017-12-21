package com.taiji.fxsqjw.views.rest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.taiji.fxsqjw.views.adapter.KeyPersonnelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lightkin on 17-10-20.
 */

public class NetworkKeyPersonnel extends AsyncTask<String, Void, byte[]> {
    private Context context;
    private ProgressDialog pDialog;
    private List totalList = null;
    private KeyPersonnelAdapter adapter = null;

    public NetworkKeyPersonnel(Context context, List totalList, KeyPersonnelAdapter adapter) {
        this.context = context;
        this.totalList = totalList;
        this.adapter = adapter;
        pDialog = new ProgressDialog(context);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.show();
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("rowid", "8642");
        item.put("personName", "邱旭");
        item.put("idNumber", "2109154864651458");
        item.put("domicile", "阜新市细河区人民大街58号翔宇一品小区14-23-4");
        item.put("address", "辽宁省阜新市细河区人民大街");
        totalList.add(item);
        //totalList.addAll(list);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected byte[] doInBackground(String... params) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL(params[0]);
            HttpURLConnection httpConn = (HttpURLConnection) url
                    .openConnection();
            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                byte[] buffer = new byte[8 * 1024];
                int c = 0;
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
                return baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override


    protected void onPostExecute(byte[] result) {
        super.onPostExecute(result);
        if (result != null) {
            // 开始行json解析
            try {
                String data = new String(result, "utf-8");
                // 将异步任务访问到的字节数组转成字符串，再通过json解析成list集合
                //List<Map<String, String>> list = jsonToList(data);
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("rowid", "8642");
                item.put("personName", "张三");
                item.put("idNumber", "2109154864651468");
                item.put("domicile", "阜新市细河区人民大街58号翔宇一品小区14-23-2");
                item.put("address", "辽宁省阜新市细河区人民大街");
                totalList.add(item);
                //totalList.addAll(list);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "网络异常，加载失败！", Toast.LENGTH_SHORT)
                    .show();
        }
        pDialog.dismiss();
    }

    // 解析json字符串，生成list集合


    private List<Map<String, String>> jsonToList(String jsonString) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String data = jsonObject2.getString("position");
                map.put("position", data);
                list.add(map);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }
}
