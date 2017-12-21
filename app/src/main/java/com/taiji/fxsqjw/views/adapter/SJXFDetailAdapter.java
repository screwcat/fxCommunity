package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taiji.fxsqjw.views.R;

import java.util.List;
import java.util.Map;

import static com.taiji.fxsqjw.views.R.id.sjxf_details_tv_dealProject;

/**
 * Created by Administrator on 2017/10/25.
 */

public class SJXFDetailAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list = null;

    public SJXFDetailAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {


        ViewHolder mHolder = null;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.sjxfdetail_listview_item, viewGroup, false);
            mHolder.sjxf_details_tv_id = (TextView) view.findViewById(R.id.sjxf_details_tv_id);
            mHolder.sjxf_details_tv_dealProject = (TextView) view.findViewById(sjxf_details_tv_dealProject);
            mHolder.sjxf_details_tv_dealResult = (TextView) view.findViewById(R.id.sjxf_details_tv_dealResult);
            mHolder.sjxf_details_tv_createTime = (TextView) view.findViewById(R.id.sjxf_details_tv_createTime);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        Resources res = context.getResources();
        String[] dealResults = res.getStringArray(R.array.spinner_one);
        String[] dealProjects = res.getStringArray(R.array.spinner_two);
        if(list.get(i).get("dealProject").equals(null)){
            }
        else{
            mHolder.sjxf_details_tv_dealProject.setText(dealProjects[Integer.parseInt(list.get(i).get("dealProject"))-1]);
            mHolder.sjxf_details_tv_dealResult.setText(dealResults[Integer.parseInt(list.get(i).get("dealResult"))-1]);
            mHolder.sjxf_details_tv_id.setText(list.get(i).get("id").toString());
            mHolder.sjxf_details_tv_createTime.setText(list.get(i).get("createTime").toString());
        }

        }catch (Exception e){
            e.getStackTrace();
        }finally {
            return view;
        }
    }

    class ViewHolder {
        private TextView sjxf_details_tv_id;
        private TextView sjxf_details_tv_dealProject;
        private TextView sjxf_details_tv_dealResult;
        private TextView sjxf_details_tv_createTime;
    }
}