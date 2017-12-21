package com.taiji.fxsqjw.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.taiji.fxsqjw.views.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuguohui on 2016/11/8.
 */

public class TaskScrollAdapter extends RecyclerView.Adapter<TaskScrollAdapter.MyViewHolder> {

    private static List<String> data = new ArrayList<>();

    static {
        for (int i = 1; i <= 70; i++) {
            data.add(i + "");
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.listview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String title=data.get(position);
        holder.position.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"item"+ title+" 被点击了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView position;

        public MyViewHolder(View itemView) {
            super(itemView);
            position = (TextView) itemView.findViewById(R.id.position);
        }
    }
}
