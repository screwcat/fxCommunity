package com.taiji.fxsqjw.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.taiji.fxsqjw.swipe.SimpleSwipeListener;
import com.taiji.fxsqjw.swipe.SwipeLayout;
import com.taiji.fxsqjw.swipe.adapters.BaseSwipeAdapter;
import com.taiji.fxsqjw.views.R;

import java.util.List;
import java.util.Map;

public class CarListViewAdapter extends BaseSwipeAdapter {

    private List<Map> carList;

    private Context mContext;

    public CarListViewAdapter(Context mContext,List<Map> carList) {
        this.mContext = mContext;
        this.carList = carList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        //initCarList();
        final View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(10).delay(10).playOn(layout.findViewById(R.id.trash));
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TextView item_car_id = (TextView)v.findViewById(R.id.item_car_id);
                String car_id = item_car_id.getText().toString();
//                JojtApiUtils.delCar(car_id, new JojtApiUtils.ApiCallBack() {
//                    @Override
//                    public void onSuccess(Object obj) {
//                        Map<String,Object> resultMap = (Map<String,Object>)obj;
//                        if(CommonUtils.RESULT_CODE_SUCCESS.equals((Double)resultMap.get(CommonUtils.RESULT_CODE))){
//                            if(resultMap.get(CommonUtils.STATUS)!=null){
//                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
//                                carList.remove(position);
//                                notifyDatasetChanged();
//
//                            }
//                        }else{
//                            Toast.makeText(mContext, "删除失败，请重试", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });
            }
        });
        return v;
    }


    @Override
    public void fillValues(int position, View convertView) {
        TextView item_car_id = (TextView)convertView.findViewById(R.id.item_car_id);
        item_car_id.setText(carList.get(position).get("carId").toString());
        TextView pt = (TextView)convertView.findViewById(R.id.position);
        pt.setText((position + 1) + ".");
        TextView carno = (TextView)convertView.findViewById(R.id.text_data);
        carno.setText("车牌号为:"+carList.get(position).get("carNo").toString());
    }

    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
