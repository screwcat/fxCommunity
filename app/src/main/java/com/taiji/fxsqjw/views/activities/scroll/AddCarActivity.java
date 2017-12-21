package com.taiji.fxsqjw.views.activities.scroll;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

public class AddCarActivity extends Activity {

    private EditText carNo ;
    private Button btn_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        carNo = (EditText)findViewById(R.id.car_no );
        btn_done = (Button)findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editCarNo = carNo.getText().toString();
                if(StringUtil.isEmpty(editCarNo)) {
                    Toast.makeText(AddCarActivity.this, "车牌号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String uid = SharedPreferencesUtils.getValue(AddCarActivity.this, Constants.USER_SETTING_USERNAME,null);
                if(StringUtil.isEmpty(uid)) {
                    Toast.makeText(AddCarActivity.this, "用户未登录", Toast.LENGTH_SHORT).show();
                    return;
                }
//                JojtApiUtils.addCar(uid, editCarNo, new JojtApiUtils.ApiCallBack() {
//                    @Override
//                    public void onSuccess(Object obj) {
//                        Map<String,Object> resultMap = (Map<String,Object>)obj;
//                        if(CommonUtils.RESULT_CODE_SUCCESS.equals((Double)resultMap.get(CommonUtils.RESULT_CODE))){
//                            if(resultMap.get(CommonUtils.STATUS)!=null){
//                                Toast.makeText(AddCarActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }else{
//                            Toast.makeText(AddCarActivity.this, "添加失败，请重试", Toast.LENGTH_SHORT).show();
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

    }

}
