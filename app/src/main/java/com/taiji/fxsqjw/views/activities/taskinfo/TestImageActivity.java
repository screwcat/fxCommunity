package com.taiji.fxsqjw.views.activities.taskinfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.taiji.fxsqjw.views.R;


public class TestImageActivity extends Activity {

    private LinearLayout test_image_ll;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
        test_image_ll = (LinearLayout)findViewById(R.id.test_image_ll);
        type = getIntent().getIntExtra("type",-1);
        switch (type){
            case 1:
                test_image_ll.setBackgroundResource(R.drawable.zhongdianduixiang);
                break;
            case 2:
                test_image_ll.setBackgroundResource(R.drawable.shujucaiji);
                break;
            default:

                break;
        }
    }

    public void changeImage(View view){
        switch (type){
            case 1:
                test_image_ll.setBackgroundResource(R.drawable.xieshi);
                break;
            case 2:
                test_image_ll.setBackgroundResource(R.drawable.renkoujibenxinxi);
                break;
            default:

                break;
        }
    }
    public void changeImageTwo(View view){
        switch (type){
            case 1:
                break;
            case 2:
                test_image_ll.setBackgroundResource(R.drawable.fangwujibenxinxi);
                break;
            default:

                break;
        }
    }
}
