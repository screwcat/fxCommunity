package com.taiji.fxsqjw.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;

public class BaseActivity extends AppCompatActivity {
    protected String[] planetTitles;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected FrameLayout frameLayout;

    private int[] pics = {R.drawable.slide_lv_pic_1,R.drawable.slide_lv_pic_2,R.drawable.slide_lv_pic_3,R.drawable.slide_lv_pic_4,R.drawable.slide_lv_pic_5,R.drawable.slide_lv_pic_6,R.drawable.slide_lv_pic_7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 重写setContentView，以便于在保留侧滑菜单的同时，让子Activity根据需要加载不同的界面布局
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        // 将传入的layout加载到activity_base的content_frame里面
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(drawerLayout);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        setUpNavigation();
    }

    private void setUpNavigation() {
        planetTitles = getResources().getStringArray(R.array.planets_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        LinearLayout header_ll = (LinearLayout)getLayoutInflater().inflate(R.layout.slide_header_view, null);
        LinearLayout end_ll = (LinearLayout)getLayoutInflater().inflate(R.layout.slide_end_view, null);
        drawerList.addHeaderView(header_ll);
        drawerList.addFooterView(end_ll);
        //drawerList.setAdapter(new ArrayAdapter<>(BaseActivity.this,R.layout.list_item_drawer,R.id.slide_tv_item_text, planetTitles));
        drawerList.setAdapter(new SlideSettingsAdapter());
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        if(position==7){
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(BaseActivity.this, "点击了：" + planetTitles[position - 1],
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    class SlideSettingsAdapter extends BaseAdapter {


        //得到listView中item的总数
        @Override
        public int getCount() {
            return planetTitles.length;
        }


        @Override
        public String getItem(int position) {
            return planetTitles[position];
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        //简单来说就是拿到单行的一个布局，然后根据不同的数值，填充主要的listView的每一个item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //拿到ListViewItem的布局，转换为View类型的对象
            View layout = View.inflate(BaseActivity.this, R.layout.list_item_drawer, null);
            ImageView ivThumb = (ImageView) layout.findViewById(R.id.slide_tv_item_pic);
            TextView tvName = (TextView) layout.findViewById(R.id.slide_tv_item_text);
            ivThumb.setImageResource(pics[position]);
            tvName.setText(planetTitles[position]);

            return layout;
        }
    }
}
