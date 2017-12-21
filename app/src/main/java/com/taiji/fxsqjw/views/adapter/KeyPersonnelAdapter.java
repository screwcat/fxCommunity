package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.RealisticActivity;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;

import java.util.List;
import java.util.Map;

import static com.taiji.fxsqjw.views.R.id.photoPath;

/**
 * Created by Administrator on 2017/10/25.
 */

public class KeyPersonnelAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list = null;

    public KeyPersonnelAdapter(Context context, List<Map<String, Object>> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.keypersonnellistview_item, viewGroup, false);
            mHolder.objectId = (TextView) view.findViewById(R.id.objectId);
            mHolder.objectName = (TextView) view.findViewById(R.id.objectName);
            mHolder.ivStressObj = (ImageView) view.findViewById(R.id.ivStressObj);
            mHolder.documentNumber = (TextView) view.findViewById(R.id.documentNumber);
            mHolder.cityCodeAddress = (TextView) view.findViewById(R.id.cityCodeAddress);
            mHolder.address = (TextView) view.findViewById(R.id.address);
            mHolder.photoPath = (TextView) view.findViewById(photoPath);
            mHolder.personType = (TextView) view.findViewById(R.id.personType);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        try {
            mHolder.objectId.setText(list.get(i).get("objectId").toString());
            mHolder.objectName.setText(list.get(i).get("objectName").toString());
            //mHolder.ivStressObj.setImageURI(Uri.parse(JojtApiUtils.BASEURL + list.get(i).get("photoPath")));
            String url = JojtApiUtils.BASEURL + list.get(i).get("photoPath");
            Picasso.with(KeyPersonnelAdapter.this.context).load(url).into(mHolder.ivStressObj);
            mHolder.documentNumber.setText(list.get(i).get("documentNumber").toString());
            mHolder.cityCodeAddress.setText(list.get(i).get("cityCodeAddress").toString());
            mHolder.address.setText(list.get(i).get("address").toString());
            mHolder.photoPath.setText(list.get(i).get("photoPath").toString());
            mHolder.personType.setText(list.get(i).get("personType").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    class ViewHolder {
        private TextView objectId;
        private TextView objectName;
        private ImageView ivStressObj;
        private TextView documentNumber;
        private TextView cityCodeAddress;
        private TextView address;
        private TextView photoPath;
        private TextView personType;
    }
}
