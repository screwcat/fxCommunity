package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.taiji.fxsqjw.views.R;

import java.util.List;
import java.util.Map;

/**
 * Created by lightkin on 17-10-20.
 */

public class SJCJAddressWriteScrollAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String, String>> list = null;

    public SJCJAddressWriteScrollAdapter(Context context, List<Map<String, String>> list) {
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
    public View getView(int num, View convertView, ViewGroup parent) {
        if (list == null || list.size() < 1) return null;
        Map map = list.get(num);
        if (map.isEmpty() || map.size() < 1) {
            return null;
        }
        TextView dzmc,mldzid;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_item_sjcjaddress, parent, false);
        }

        dzmc = (TextView) convertView
                .findViewById(R.id.dzmc);
        mldzid=(TextView) convertView
                .findViewById(R.id.mldzid);

        dzmc.setText(list.get(num)
                .get("dzmc"));
        mldzid.setText(list.get(num)
                .get("mldzid"));

        return convertView;
    }
}
