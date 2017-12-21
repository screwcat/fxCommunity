package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.activities.HiddenDanDtlActivity;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;

import java.util.List;
import java.util.Map;

import static com.taiji.fxsqjw.views.R.id.photoPath;

/**
 * Created by Administrator on 2017/10/25.
 */

public class InstructQueryPicAdapter extends BaseAdapter {

    private Context context;
    private List<String> list = null;

    public InstructQueryPicAdapter(Context context, List<String> list) {
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
            view = LayoutInflater.from(context).inflate(R.layout.instruct_pic_item, viewGroup, false);
            //mHolder.instructPic = (ImageView) view.findViewById(R.id.instructPic);
            mHolder.attachment = (TextView) view.findViewById(R.id.attachment);
            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }
        try {
            String url = JojtApiUtils.BASEURL + list.get(i);
            //Picasso.with(view.getContext()).load(url).into(mHolder.instructPic);
            String strAtt = "<A HREF=\"" + url + "\">查看附件" + (i + 1) + "</A>";
            CharSequence charsequence = Html.fromHtml(strAtt);
            mHolder.attachment.setText(charsequence);
            mHolder.attachment.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    class ViewHolder {
        private ImageView instructPic;
        private TextView attachment;
    }
}
