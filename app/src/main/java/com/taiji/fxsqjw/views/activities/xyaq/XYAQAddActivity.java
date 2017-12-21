package com.taiji.fxsqjw.views.activities.xyaq;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.FileAndParamUploadUtil;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class XYAQAddActivity extends AppCompatActivity implements FileAndParamUploadUtil.OnUploadProcessListener{
    private static final String TAG="XYAQAddActivity";
    private static final String fileKey = "android_qd_by_gps";
    private static final String xyaqadd_jpg = "xyaqadd.jpg";
    private static final String requestURL= JojtApiUtils.BASEURL+"communityPolice/instruction.hiddenDangerEnter.do";
    private ImageView xyaqAdd_image;
    ProgressDialog pDialog;
    private Button xyaq_add_Button;
    private ImageButton xyaq_date_btn;
    private CheckBox xyaq_checkbox_wfxwt,checkBox1,checkBox2,checkBox3,checkBox4;
    private RelativeLayout xyaq_zgqx_ll;
    private LinearLayout xyaq_add_ll_question,xyaq_handle_ll_project;
    private StringBuffer type_sb;
    private String unitCode;
    private EditText xyaq_manage_tv_five;
    private ImageView qkqd_add_iv_item,qkqd_add_iv_btn;
    private Uri tempUri;
    protected static final int TAKE_PICTURE = 1;
    private TextView xyaq_manage_et_zhzgqx,xyaq_add_tv_one,xyaq_add_tv_two,xyaq_add_tv_three;
    private int myear, mmonth, mdayOfMonth;
    private Spinner xyaq_handle_spinner_chulijieguo,xyaq_handle_spinner_chulixiangmu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyaqadd);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setStatusBarTintResource(R.color.dark_gray);//通知栏所需颜色
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Intent intent = getIntent();
        String dwmc = intent.getStringExtra("dwmc");
        String address = intent.getStringExtra("address");
        String dwlb = intent.getStringExtra("dwlb");
        unitCode = intent.getStringExtra("unitCode");
        xyaqAdd_image=(ImageView)findViewById(R.id.xyaqAdd_image);
        xyaq_zgqx_ll = (RelativeLayout)findViewById(R.id.xyaq_zgqx_ll);
        xyaq_add_ll_question = (LinearLayout) findViewById(R.id.xyaq_add_ll_question);
        xyaq_handle_ll_project = (LinearLayout) findViewById(R.id.xyaq_handle_ll_project);
        xyaq_manage_tv_five = (EditText)findViewById(R.id.xyaq_manage_tv_five);
        xyaq_add_Button=(Button)findViewById(R.id.xyaq_add_Button);
        xyaq_date_btn =(ImageButton)findViewById(R.id.xyaq_date_btn);
        xyaq_checkbox_wfxwt=(CheckBox)findViewById(R.id.xyaq_checkbox_wfxwt);
        checkBox1=(CheckBox)findViewById(R.id.checkBox1);
        checkBox2=(CheckBox)findViewById(R.id.checkBox2);
        checkBox3=(CheckBox)findViewById(R.id.checkBox3);
        checkBox4=(CheckBox)findViewById(R.id.checkBox4);
        qkqd_add_iv_item = (ImageView) findViewById(R.id.qkqd_add_iv_item);
        qkqd_add_iv_btn = (ImageView) findViewById(R.id.qkqd_add_iv_btn);
        xyaq_manage_et_zhzgqx = (TextView) findViewById(R.id.xyaq_manage_et_zhzgqx);
        xyaq_add_tv_one = (TextView) findViewById(R.id.xyaq_add_tv_one);
        xyaq_add_tv_two = (TextView) findViewById(R.id.xyaq_add_tv_two);
        xyaq_add_tv_three = (TextView) findViewById(R.id.xyaq_add_tv_three);
        xyaq_handle_spinner_chulijieguo = (Spinner)findViewById(R.id.xyaq_handle_spinner_chulijieguo);
        xyaq_handle_spinner_chulixiangmu = (Spinner)findViewById(R.id.xyaq_handle_spinner_chulixiangmu);

        xyaq_add_tv_one.setText(dwmc);
        xyaq_add_tv_two.setText(address);
        xyaq_add_tv_three.setText(dwlb);

        //调用摄像头拍照，并且保存至tempUri
        qkqd_add_iv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                //tempUri = Uri.fromFile(new File(StringUtil.getKaoQinQianDaoPath(QkqdOfflineMapActivity.this), "kaoqinqiandao.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                tempUri = FileProvider.getUriForFile(XYAQAddActivity.this,"com.taiji.fxsqjw.views.fileProvider",new File(StringUtil.getKaoQinQianDaoPath(XYAQAddActivity.this), xyaqadd_jpg));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(intent, TAKE_PICTURE);
            }
        });

        xyaq_add_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                type_sb = new StringBuffer();
                File file;
                try {
                    file = new File(StringUtil.getKaoQinQianDaoPath(XYAQAddActivity.this)+"/"+xyaqadd_jpg);
                    if (file == null || (!file.exists())) {
                        Toast.makeText(XYAQAddActivity.this,"文件不存在",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(XYAQAddActivity.this,"文件不存在",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    return;
                }
                pDialog = new ProgressDialog(XYAQAddActivity.this);
                pDialog.setTitle("提示：");
                pDialog.setMessage("数据加载中...");
                pDialog.show();

                if(checkBox1.isChecked()){
                    type_sb.append("1").append("|");
                }
                if(checkBox2.isChecked()){
                    type_sb.append("2").append("|");
                }
                if(checkBox3.isChecked()){
                    type_sb.append("3").append("|");
                }
                if(checkBox4.isChecked()){
                    type_sb.append("4").append("|");
                }
                if(xyaq_checkbox_wfxwt.isChecked()){
                    type_sb.append("5").append("|");
                }
                int xyaq_handle_spinner_chulijieguo_str = xyaq_handle_spinner_chulijieguo.getSelectedItemPosition()+1;
                int xyaq_handle_spinner_chulixiangmu_str = xyaq_handle_spinner_chulixiangmu.getSelectedItemPosition()+1;


                FileAndParamUploadUtil uploadUtil = FileAndParamUploadUtil.getInstance();;
                uploadUtil.setOnUploadProcessListener(XYAQAddActivity.this);  //设置监听器监听上传状态

                Map<String, String> params = new HashMap<String, String>();
                params.put("unitCode", unitCode);
                String type = type_sb.length()>0?type_sb.substring(0,type_sb.lastIndexOf("|")):"";
                params.put("type", type);
                params.put("remark", xyaq_manage_tv_five.getText().toString());
                params.put("createUser", SharedPreferencesUtils.getValue(XYAQAddActivity.this, Constants.USER_SETTING_USERID,null));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String date=sdf.format(new java.util.Date());
                params.put("createTime", date);
                params.put("dealProject", String.valueOf(xyaq_handle_spinner_chulixiangmu_str));
                params.put("dealResult", String.valueOf(xyaq_handle_spinner_chulijieguo_str));
                params.put("unitType", Constants.UNITTYPE_XYAQ);
                params.put("endTime", xyaq_manage_et_zhzgqx.getText().toString());
                params.put("createUnit", SharedPreferencesUtils.getValue(XYAQAddActivity.this, Constants.USER_SETTING_UNITID,null));
                uploadUtil.uploadFile( file,fileKey, requestURL,params);

                Intent intent=new Intent(XYAQAddActivity.this,XYAQMangerActivity.class);
                startActivity(intent);
            }
        });
        xyaq_checkbox_wfxwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox btn = (CheckBox)v;
                if(btn.isChecked()){
                    xyaq_add_ll_question.setVisibility(View.GONE);
                }else{
                    xyaq_add_ll_question.setVisibility(View.VISIBLE);
                }
            }
        });

        xyaq_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });
        xyaq_handle_spinner_chulijieguo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                xyaq_zgqx_ll.setVisibility(View.GONE);
                xyaq_handle_ll_project.setVisibility(View.GONE);
                if(position==1){
                    xyaq_zgqx_ll.setVisibility(View.VISIBLE);
                }else if(position==2){
                    xyaq_handle_ll_project.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case 1:
                Calendar now = Calendar.getInstance();
                return new DatePickerDialog(this,mdateListener,now.get(Calendar.YEAR),now.get(Calendar.MONTH)-1,now.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myear = year;
            mmonth = month;
            mdayOfMonth = dayOfMonth;
            display();
        }
    };

    public void display(){
        StringBuffer sb = new StringBuffer().append(myear).append("-").append(mmonth).append("-").append(mdayOfMonth);
        xyaq_manage_et_zhzgqx.setText(sb.toString());
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
        File file = new File(StringUtil.getKaoQinQianDaoPath(XYAQAddActivity.this)+"/"+xyaqadd_jpg);
        if (file.exists()) {
            Bitmap photo = BitmapFactory.decodeFile(file.getAbsolutePath());//extras.getParcelable("data");
            qkqd_add_iv_item.setImageBitmap(photo);
            qkqd_add_iv_item.setTag(true);
        }
    }

    @Override
    public void onUploadDone(int responseCode, String message) {
        Log.i(TAG,message);
        pDialog.dismiss();
        //qkqd_gps_ll_qd_success.setVisibility(View.VISIBLE);
        String msg = "";
        if(responseCode==1){
            msg = "录入成功";
        }else if(responseCode==3){
            if(message.contains("timeout")){
                msg = "上传超时";
            }
        }else{
            msg = "服务器异常";
        }
        Looper.prepare();
        Toast.makeText(this,"录入成功",Toast.LENGTH_SHORT).show();
        Looper.loop();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break ;
        }
        return true;
    }
}
