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
 * Created by Administrator on 2017/10/25.
 */

public class HiddenDangerAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list = null;

    public HiddenDangerAdapter(Context context, List<Map<String, Object>> list) {
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
        ViewHolder mHolder = null;
        if (view == null) {
            mHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.hiddendangerlistview_item, viewGroup, false);
            mHolder.courierName = (TextView) view.findViewById(R.id.courierName);
            mHolder.reportTitle = (TextView) view.findViewById(R.id.reportTitle);
            mHolder.sendTime = (TextView) view.findViewById(R.id.sendTime);
            mHolder.uuid = (TextView) view.findViewById(R.id.uuid);
            mHolder.handleStatus = (TextView) view.findViewById(R.id.handleStatus);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        try {
            mHolder.courierName.setText(list.get(i).get("courierName").toString());
            mHolder.reportTitle.setText(list.get(i).get("reportTitle").toString());
            mHolder.sendTime.setText(list.get(i).get("sendTime").toString());
            mHolder.uuid.setText(list.get(i).get("uuid").toString());
            //mHolder.handleStatus.setText(list.get(i).get("status").equals("0") ? "未处理" : "已处理");
            switch (list.get(i).get("reportFeedbackStatus").toString()) {
                case "0": {
                    mHolder.handleStatus.setText("未处理");
                    break;
                }
                case "1": {
                    mHolder.handleStatus.setText("有效");
                    break;
                }
                case "2": {
                    mHolder.handleStatus.setText("无效");
                    break;
                }
                case "3": {
                    mHolder.handleStatus.setText("位置错误");
                    break;
                }
                default: {

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class ViewHolder {
        private TextView uuid;
        private TextView courierName;
        private TextView reportTitle;
        private TextView sendTime;
        private TextView handleStatus;
    }
}
