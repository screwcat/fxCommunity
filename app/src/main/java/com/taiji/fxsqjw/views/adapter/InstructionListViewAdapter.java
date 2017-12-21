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
 * Created by Administrator on 2017/10/21.
 */

public class InstructionListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list = null;

    public InstructionListViewAdapter(Context context, List<Map<String, Object>> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.instructionlistview_item, parent, false);
            mHolder.noticeId = (TextView) convertView.findViewById(R.id.noticeId);
            mHolder.senderName = (TextView) convertView.findViewById(R.id.senderName);
            mHolder.noticeTitle = (TextView) convertView.findViewById(R.id.noticeTitle);
            mHolder.createTime = (TextView) convertView.findViewById(R.id.createTime);
            mHolder.signingStatus = (TextView) convertView.findViewById(R.id.signingStatus);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        try {
            mHolder.noticeId.setText(list.get(position).get("noticeId").toString());
            mHolder.senderName.setText(list.get(position).get("senderName").toString());
            mHolder.noticeTitle.setText(list.get(position).get("noticeTitle").toString());
            mHolder.createTime.setText(list.get(position).get("createTime").toString());
            //mHolder.signingStatus.setText(list.get(position).get("signingStatus").equals("0") ? "未签收" : "已签收");
            if(list.get(position).get("signingStatus").equals("0")){
                mHolder.signingStatus.setBackground(convertView.getResources().getDrawable(R.drawable.sioaimi));
            }else{
                mHolder.signingStatus.setBackground(convertView.getResources().getDrawable(R.drawable.sioaisumi));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        private TextView noticeId;
        private TextView senderName;
        private TextView noticeTitle;
        private TextView createTime;
        private TextView signingStatus;
    }
}
