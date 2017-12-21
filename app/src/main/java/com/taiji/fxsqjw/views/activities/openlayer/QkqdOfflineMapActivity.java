package com.taiji.fxsqjw.views.activities.openlayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.dao.SerializableHashMap;
import com.taiji.fxsqjw.views.dao.UserPolygons;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.FileAndParamUploadUtil;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QkqdOfflineMapActivity extends AppCompatActivity implements FileAndParamUploadUtil.OnUploadProcessListener {
    private static final String TAG = "QkqdOfflineMapActivity";
    private static final String fileKey = "android_qd_by_gps";
    private static final String kaoqinqiandao_jpg = "kaoqinqiandao.jpg";
    private static final String requestURL = JojtApiUtils.BASEURL+"communityPolice/map.checkingSign.do";
    private TextView qkqd_tv_time,qkqd_tv_address,qkqd_tv_qdr,qkqd_tv_dutynum;
    private ImageButton qkqd_bt_dk_submit;
    private ImageView qkqd_add_iv_item,qkqd_add_iv_btn;
    protected static final int TAKE_PICTURE = 1;
    private Uri tempUri;
    ProgressDialog pDialog;
    private double longitude;
    private double latitude;
    private String address;
    private String dutynums;
    private LinearLayout qkqd_gps_ll_qd_success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qkqd_offline_map);
        qkqd_tv_time = (TextView) findViewById(R.id.qkqd_tv_time);
        qkqd_tv_address = (TextView) findViewById(R.id.qkqd_tv_address);
        qkqd_tv_qdr = (TextView) findViewById(R.id.qkqd_tv_qdr);
        qkqd_tv_dutynum = (TextView) findViewById(R.id.qkqd_tv_dutynum);
        qkqd_bt_dk_submit = (ImageButton) findViewById(R.id.qkqd_bt_dk_submit);
        qkqd_add_iv_item = (ImageView) findViewById(R.id.qkqd_add_iv_item);
        qkqd_add_iv_btn = (ImageView) findViewById(R.id.qkqd_add_iv_btn);
        qkqd_gps_ll_qd_success = (LinearLayout) findViewById(R.id.qkqd_gps_ll_qd_success);
        tempUri = FileProvider.getUriForFile(QkqdOfflineMapActivity.this,"com.taiji.fxsqjw.views.fileProvider",new File(StringUtil.getKaoQinQianDaoPath(QkqdOfflineMapActivity.this), kaoqinqiandao_jpg));
        qkqd_tv_qdr.setText(SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERNAME,null));
        //调用摄像头拍照，并且保存至tempUri
        qkqd_add_iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                //tempUri = Uri.fromFile(new File(StringUtil.getKaoQinQianDaoPath(QkqdOfflineMapActivity.this), "kaoqinqiandao.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });
        initQdText();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initQdText() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date=sdf.format(new java.util.Date());
        qkqd_tv_time.setText(date);
        longitude = getIntent().getDoubleExtra("longitude",0);
        latitude = getIntent().getDoubleExtra("latitude",0);
        address = getIntent().getStringExtra("address");
        dutynums = getIntent().getStringExtra("dutynums");
        qkqd_tv_address.setText(address);
        qkqd_tv_dutynum.setText(dutynums);
    }

    /**
     * 回调区域
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    setImageToView(tempUri); // 让刚才得到的图片显示在界面上
                    break;
            }
        }
    }

    /**
     * 保存图片数据并显示在预览区域
     *
     * @param
     *
     */
    protected void setImageToView(Uri tempUri) {
        File file = new File(StringUtil.getKaoQinQianDaoPath(QkqdOfflineMapActivity.this)+"/"+kaoqinqiandao_jpg);
        if (file.exists()) {
            Bitmap photo = BitmapFactory.decodeFile(file.getAbsolutePath());//extras.getParcelable("data");
            qkqd_add_iv_item.setImageBitmap(photo);
            qkqd_add_iv_item.setTag(true);
        }
    }

    /**
     * 保存签到信息
     * @param view
     */
    public void submitInfo(View view){
        String qkqd_tv_time_str,qkqd_tv_dutynum_str,qkqd_tv_qdr_str,qkqd_add_iv_item_str;
        qkqd_tv_time_str = qkqd_tv_time.getText().toString();
        qkqd_tv_dutynum_str = qkqd_tv_dutynum.getText().toString();
        qkqd_tv_qdr_str = qkqd_tv_qdr.getText().toString();
//        if(longitude==0.0||latitude==0.0){
//            Toast.makeText(this,"无法获取当前位置",Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(StringUtil.isEmpty(qkqd_tv_time_str)||StringUtil.isEmpty(qkqd_tv_dutynum_str)||StringUtil.isEmpty(qkqd_tv_qdr_str)){
            Toast.makeText(this,"请填写完整信息",Toast.LENGTH_SHORT).show();
            return;
        }
        Object obj = qkqd_add_iv_item.getTag();
//        if(obj==null||!(boolean)obj){
//            Toast.makeText(this,"请先拍照",Toast.LENGTH_SHORT).show();
//            return;
//        }
        File file;
        try {
            file = new File(StringUtil.getKaoQinQianDaoPath(QkqdOfflineMapActivity.this)+"/"+kaoqinqiandao_jpg);
            if (file == null || (!file.exists())) {
                Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this,"文件不存在",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("提示：");
        pDialog.setMessage("数据加载中...");
        pDialog.show();
        FileAndParamUploadUtil uploadUtil = FileAndParamUploadUtil.getInstance();;
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("captureTime", qkqd_tv_time_str);
        params.put("dutyNum", qkqd_tv_dutynum_str);
        params.put("longitude", String.valueOf(longitude));
        params.put("latitude",  String.valueOf(latitude));
        params.put("userId", SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null));
        uploadUtil.uploadFile( file,fileKey, requestURL,params);
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        Log.i(TAG,message);
        //qkqd_gps_ll_qd_success.setVisibility(View.VISIBLE);
        pDialog.dismiss();
        if(responseCode==1){
            Looper.prepare();
            Toast.makeText(this,"签到成功",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }else{
            Looper.prepare();
            Toast.makeText(this,"签到失败，请重新上传",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


    }

    @Override
    public void onUploadProcess(int uploadSize) {
        Log.i(TAG,"onUploadProcess:"+uploadSize);
    }

    @Override
    public void initUpload(int fileSize) {
        Log.i(TAG,"initUpload:"+fileSize);
    }

    @Override
    protected void onDestroy() {
        if(pDialog!=null){
            pDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break ;
        }
        return true;
    }
}
