package com.taiji.fxsqjw.views.activities.openlayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.taiji.fxsqjw.views.R;
import com.taiji.fxsqjw.views.adapter.marker.ClusterItem;
import com.taiji.fxsqjw.views.adapter.marker.ClusterManager;
import com.taiji.fxsqjw.views.dao.SerializableHashMap;
import com.taiji.fxsqjw.views.dao.UserPolygons;
import com.taiji.fxsqjw.views.rest.JojtApiUtils;
import com.taiji.fxsqjw.views.utils.Constants;
import com.taiji.fxsqjw.views.utils.SharedPreferencesUtils;
import com.taiji.fxsqjw.views.utils.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OfflineBaiduMapActivity extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback {

    // 定位相关
    LocationClient mLocClient;
    private static final String TAG = "OfflineBaiduMapActivity";
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    public MyLocationListenner myListener = new MyLocationListenner();
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    // UI相关
    private MyLocationData locData;
    private ClusterManager<MyItem> mClusterManager;
    MapStatus ms;
    private Button offline_baidumap_kqdk,offline_baidumap_dkqk;
    private boolean isInPolygons;
    private String userId;
    private JSONArray signList = null;
    private List<UserPolygons> polygonList = new ArrayList<UserPolygons>();
    private HashMap<String,UserPolygons> currentPolygons_map = new HashMap<String,UserPolygons>();
    private ArrayList<UserPolygons> dakaPolygons = new ArrayList<UserPolygons>();
    private JSONArray dakaList = null;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_layer);
        initOfflineMap();
        mMapView = (MapView) findViewById(R.id.bmapView);
        offline_baidumap_kqdk = (Button) findViewById(R.id.offline_baidumap_kqdk);
        offline_baidumap_dkqk = (Button) findViewById(R.id.offline_baidumap_dkqk);
        userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null);
        if(StringUtil.isEmpty(userId)) Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
        initUi();
//        initClusters();
        //初始化责任区
        initCheckingSignMap();
        offline_baidumap_kqdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isInPolygons = true;
                if(isInPolygons){
                    Intent intent = new Intent();
                    StringBuilder address = new StringBuilder();
                    StringBuilder dutynums = new StringBuilder();
                    String dutynum = null;
                    if(currentPolygons_map!=null&&currentPolygons_map.size()>0){
                        Collection collection = currentPolygons_map.values();
                        Iterator it = collection.iterator();
                        while(it.hasNext()) {
                            UserPolygons up = (UserPolygons)it.next();
                            address.append(up.getZrqmc()).append(" ");
                            dutynums.append(up.getZrqdm()).append(",");
                        }
                        dutynum = dutynums.length()>0?dutynums.substring(0,dutynums.lastIndexOf(",")):"";
                    }
                    intent.setClass(OfflineBaiduMapActivity.this, QkqdOfflineMapActivity.class);
                    intent.putExtra("longitude",mCurrentLon);
                    intent.putExtra("latitude",mCurrentLat);
                    intent.putExtra("address",address.toString());
                    intent.putExtra("dutynums",dutynum);
                    startActivity(intent);
                }else{
                    Toast.makeText(OfflineBaiduMapActivity.this,
                            "当前位置不在责任区", Toast.LENGTH_SHORT).show();
                }

            }
        });
        offline_baidumap_dkqk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(OfflineBaiduMapActivity.this);
                pDialog.setTitle("提示：");
                pDialog.setMessage("数据加载中...");
                pDialog.show();
                initDakaInfo();
                pDialog.dismiss();
            }
        });

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar =  getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

    }

    private void initCheckingSignMap() {
        JojtApiUtils.checkingSignMap(userId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
//                Log.i(TAG,"updateCurrentLocation is success "+ obj.toString());
                List list = (List)obj;
                signList = new JSONArray(list);
                initLines();
            }
            @Override
            public void onError() {
                Log.e(TAG,"initCheckingSignMap is error");
            }
        });
    }

    private void initClusters() {
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        // 添加Marker点
//        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
//            @Override
//            public boolean onClusterItemClick(MyItem item) {
//                Toast.makeText(OfflineBaiduMapActivity.this,
//                        "点击单个Item", Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        });
    }

    public void clearMarkers(){
        mClusterManager.clearItems();
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        for(UserPolygons up:dakaPolygons){
            List<LatLng> lts =  up.getLatlons();
            List<MyItem> items = new ArrayList<MyItem>();
            for(LatLng ll:lts){
                items.add(new MyItem(ll));
            }
            mClusterManager.addItems(items);
        }
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(17.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    //绘制责任区多边形区域
    private void initLines() {
        if(signList==null) return;
        //清除地图上的其他点
        for(int i =0;i<signList.length();i++){
            try {
                JSONObject jitem_obj = signList.getJSONObject(i);
                String baiduPolygon_arr = jitem_obj.get("baiduPolygon").toString().replace("[","").replace("]","");
                String[] baiduPolygon_arrs = baiduPolygon_arr.split(";");
                List<LatLng> pts = new ArrayList<LatLng>();
                for(int j=0;j<baiduPolygon_arrs.length;j++){
                    String baiduPolygon_jarray_item = baiduPolygon_arrs[j];
                    String[] baiduPolygon_jarray_items = baiduPolygon_jarray_item.split(",");
                    if(baiduPolygon_jarray_items==null||baiduPolygon_jarray_items.length!=2) continue;
                    // 添加多边形
                    LatLng pt = new LatLng(Double.parseDouble(baiduPolygon_jarray_items[1]),Double.parseDouble(baiduPolygon_jarray_items[0]));
                    pts.add(pt);
                }
                if(pts.size()==0) continue;
                //开始绘制
                OverlayOptions ooPolygon = new PolygonOptions().points(pts)
                        .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
                mBaiduMap.addOverlay(ooPolygon);
                //绘制结束
                UserPolygons userPolygon = new UserPolygons();
                userPolygon.setLatlons(pts);
                userPolygon.setZrqdm(jitem_obj.get("zrqdm").toString());
                userPolygon.setZrqmc(jitem_obj.get("zrqmc").toString());
                polygonList.add(userPolygon);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }
        }

    }

    private void initUi(){
        // 地图初始化
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        //开启查看本地城市
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        LatLng ll = new LatLng(42.0192501071,121.660822129);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//        LatLng latLng = new LatLng(42.0192501071,121.660822129);
//        OverlayOptions ooPolygon = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_gcoding));
//        mBaiduMap.addOverlay(ooPolygon);


        initClusters();
    }

    //判断是否在责任区内
    public void isInPolygon(){
        if(polygonList==null) return;
        for(UserPolygons userPolygon:polygonList){
            List<LatLng> pts = userPolygon.getLatlons();
            if(pts.size()==0) continue;
            //判断是否在责任区
            LatLng currentPt = new LatLng(mCurrentLat, mCurrentLon);
            boolean isInPolygon = SpatialRelationUtil.isPolygonContainsPoint(pts,currentPt);

            Log.i(TAG,mCurrentLat+"-"+mCurrentLon+" `s inPolygon is "+isInPolygon+"");
            if(isInPolygon){
                currentPolygons_map.put(userPolygon.getZrqdm(),userPolygon);
                isInPolygons = isInPolygon;
            }else{
                currentPolygons_map.remove(userPolygon.getZrqdm());
            }
        }
    }

    public void dakaInfo(){
        if(dakaList==null||dakaList.length()==0){
            Toast.makeText(OfflineBaiduMapActivity.this,
                    "暂无打卡记录", Toast.LENGTH_SHORT).show();
            return;
        }
        //清除地图上的其他点
        List<LatLng> pts = new ArrayList<LatLng>();
        for(int i =0;i<dakaList.length();i++){
            try {
                JSONObject jitem_obj = dakaList.getJSONObject(i);
                String latitude = jitem_obj.get("latitude").toString();
                String longitude = jitem_obj.get("longitude").toString();
                LatLng pt = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                pts.add(pt);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }
        }
        if(pts.size()==0) return;
        //画线
        OverlayOptions ooPolygon = new PolylineOptions().points(pts).color(0xAAC80000);
        mBaiduMap.addOverlay(ooPolygon);

        UserPolygons userPolygon = new UserPolygons();
        userPolygon.setLatlons(pts);
        dakaPolygons.add(userPolygon);
        clearMarkers();
        addMarkers();

    }

    public void initOfflineMap(){
        MKOfflineMap mOffline = null;
        mOffline = new MKOfflineMap();
        mOffline.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int type, int state) {
                switch (type) {
                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:

                        break;
                    case MKOfflineMap.TYPE_NEW_OFFLINE:

                        break;

                    case MKOfflineMap.TYPE_VER_UPDATE:
                        break;
                }

            }
        });
    }

    public void initDakaInfo(){
        JojtApiUtils.checkingSignList(userId, new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
//                Log.i(TAG,"updateCurrentLocation is success "+ obj.toString());
                List list = (List)obj;
                dakaList = new JSONArray(list);
                dakaInfo();
            }
            @Override
            public void onError() {
                Log.e(TAG,"initCheckingSignMap is error");
            }
        });
    }

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(14).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
//            location.setLatitude(42.0192501071);
//            location.setLongitude(121.660822129);
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            Toast.makeText(OfflineBaiduMapActivity.this,mCurrentLat+"-"+mCurrentLon, Toast.LENGTH_SHORT).show();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (String.valueOf(mCurrentLat).contains("4.9E-324")||String.valueOf(mCurrentLon).contains("4.9E-324")) {
                //阜新市中心点经纬度
                LatLng ll = new LatLng(42.0192501071,121.660822129);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            Log.i(TAG,mCurrentLat+"--"+mCurrentLon);
            isInPolygon();
            updateCurrentLocation();
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void updateCurrentLocation() {
        String userId = SharedPreferencesUtils.getValue(this, Constants.USER_SETTING_USERID,null);
        JojtApiUtils.updateCurrentLocation(userId, mCurrentLat, mCurrentLon , System.currentTimeMillis()/1000,new JojtApiUtils.ApiCallBack() {
            @Override
            public void onSuccess(Object obj) {
                Log.i(TAG,"updateCurrentLocation is success "+ obj.toString());
            }
            @Override
            public void onError() {
                Log.e(TAG,"updateCurrentLocation is error");
            }
        });
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO 自动生成的方法存根
        if(pDialog!=null){
            pDialog.dismiss();
        }
        super.onDestroy();
        mLocClient.unRegisterLocationListener(myListener);
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
