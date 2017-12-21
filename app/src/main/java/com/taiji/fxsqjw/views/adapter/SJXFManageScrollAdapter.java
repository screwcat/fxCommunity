package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.taiji.fxsqjw.views.R;

import java.util.List;
import java.util.Map;

/**
 * Created by lightkin on 17-10-20.
 */

public class SJXFManageScrollAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String, String>> list = null;

    public SJXFManageScrollAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_item_sjxfmanager, parent, false);
            mHolder.sjxf_manage_tv_one = (TextView) convertView
                    .findViewById(R.id.sjxf_manage_tv_one);
            mHolder.sjxf_manage_tv_two = (TextView) convertView
                    .findViewById(R.id.sjxf_manage_tv_two);
            mHolder.sjxf_manage_tv_three = (TextView) convertView
                    .findViewById(R.id.sjxf_manage_tv_three);
            mHolder.sjxf_manage_tv_unitCode = (TextView) convertView
                    .findViewById(R.id.sjxf_manage_tv_unitCode);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.sjxf_manage_tv_one.setText(list.get(position)
                .get("dwmc").toString());
        mHolder.sjxf_manage_tv_two.setText(list.get(position)
                .get("address").toString());
        mHolder.sjxf_manage_tv_three.setText(list.get(position)
                .get("dwlb").toString());
        mHolder.sjxf_manage_tv_unitCode.setText(list.get(position)
                .get("unitCode").toString());
        return convertView;
    }

    class ViewHolder {
        private TextView sjxf_manage_tv_unitCode;
        private TextView sjxf_manage_tv_one;
        private TextView sjxf_manage_tv_two;
        private TextView sjxf_manage_tv_three;
    }
}
