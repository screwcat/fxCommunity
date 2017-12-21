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

public class ScrollAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String, String>> list = null;

    public ScrollAdapter(Context context, List<Map<String, String>> list) {
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
                    R.layout.listview_item, parent, false);
            mHolder.position = (TextView) convertView
                    .findViewById(R.id.position);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.position.setText(list.get(position)
                .get("position").toString());
        return convertView;
    }

    class ViewHolder {
        private TextView position;
    }
}
