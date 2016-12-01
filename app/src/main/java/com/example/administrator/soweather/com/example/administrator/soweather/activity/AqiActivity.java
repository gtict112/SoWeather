package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.CircleChart;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/22.
 */

public class AqiActivity extends Activity implements View.OnClickListener {
    private Suggestion mSuggestion = new Suggestion();
    private CircleChart aqi;
    private TextView pm10;
    private TextView pm25;
    private TextView no2;
    private TextView so2;
    private TextView co;
    private TextView o3;
    private TextView date;
    private TextView topTv;
    private ImageView topButton;
    private TextView wind;
    private TextView desc;
    private TextView index;
    private String cityid;
    private Handler mHandler;
    private Appconfiguration config = Appconfiguration.getInstance();
    private String dir;
    private String sc;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String vis;
    private String tem_max_min;
    private TextView flubrf;
    private TextView flu_txt;
    private TextView drsgbrf;
    private TextView drsg_txt;
    private TextView travbrf;
    private TextView trav_txt;
    private TextView sportbrf;
    private TextView sport_txt;
    private TextureMapView map;
    private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.place);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqi);
        initView();
        getData();
        getHandleMessge();
    }

    private void initView() {
        aqi = (CircleChart) findViewById(R.id.aqi);
        pm10 = (TextView) findViewById(R.id.pm10);
        pm25 = (TextView) findViewById(R.id.pm25);
        no2 = (TextView) findViewById(R.id.no2);
        so2 = (TextView) findViewById(R.id.so2);
        co = (TextView) findViewById(R.id.co);
        o3 = (TextView) findViewById(R.id.o3);
        date = (TextView) findViewById(R.id.date);
        topTv = (TextView) findViewById(R.id.topTv);
        wind = (TextView) findViewById(R.id.wind);
        desc = (TextView) findViewById(R.id.desc);
        index = (TextView) findViewById(R.id.index);
        flubrf = (TextView) findViewById(R.id.flubrf);
        flu_txt = (TextView) findViewById(R.id.flubrf_txt);
        drsgbrf = (TextView) findViewById(R.id.drsgbrf);
        drsg_txt = (TextView) findViewById(R.id.drsgbrf_txt);
        travbrf = (TextView) findViewById(R.id.travbrf);
        trav_txt = (TextView) findViewById(R.id.travbrf_txt);
        sportbrf = (TextView) findViewById(R.id.sportbrf);
        sport_txt = (TextView) findViewById(R.id.sportbrf_txt);
        topButton = (ImageView) findViewById(R.id.topButton);
        map = (TextureMapView) findViewById(R.id.map);
        topButton.setOnClickListener(this);
    }

    private void getData() {
        String cityid = null;
        Intent intent = getIntent();
        if (intent != null) {
            date.setText("发布时间 " + intent.getStringExtra("date"));
            aqi.setProgress(Integer.parseInt(intent.getStringExtra("aqi")));
            aqi.setmTxtHint2(intent.getStringExtra("qlty"));
            pm10.setText(intent.getStringExtra("pm10"));
            pm25.setText(intent.getStringExtra("pm25"));
            co.setText(intent.getStringExtra("co"));
            no2.setText(intent.getStringExtra("no2"));
            o3.setText(intent.getStringExtra("o3"));
            so2.setText(intent.getStringExtra("so2"));
            topTv.setText(intent.getStringExtra("city"));
            cityid = intent.getStringExtra("cityid");
            dir = intent.getStringExtra("dir");
            sc = intent.getStringExtra("sc");
            fl = intent.getStringExtra("fl");
            hum = intent.getStringExtra("hum");
            pcpn = intent.getStringExtra("pcpn");
            pres = intent.getStringExtra("pres");
            vis = intent.getStringExtra("vis");
            tem_max_min = intent.getStringExtra("tem_max_min");
            wind.setText("*  " + "今日风力," + dir + sc + "级");
            desc.setText("*  " + "温度" + tem_max_min + "," + "体感温度" + fl + "℃" + "," + "相对湿度" + hum + "%" + "," + "降水量" + pcpn + "mm" + "," + "气压" + pres + "," + "能见度" + vis + "km");
            String cond = intent.getStringExtra("cond");
            try {
                JSONObject astrodate = new JSONObject(cond);
                index.setText("*  " + "白天" + astrodate.optString("txt_d") + "," + "夜间" + astrodate.optString("txt_n"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setMap(intent.getStringExtra("lat"), intent.getStringExtra("lon"));
        }
        getSuggestionData(cityid);
    }

    private void getSuggestionData(String cityid) {
        config.showProgressDialog("正在加载", this);
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getSuggestionData(new ResponseListenter<Suggestion>() {
            @Override
            public void onReceive(Result<Suggestion> result) {
                if (result.isSuccess()) {
                    config.dismissProgressDialog();
                    mSuggestion = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mSuggestion));
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        setView(mSuggestion);
                        break;
                }
            }
        };
    }

    private void setView(Suggestion mSuggestion) {
        config.dismissProgressDialog();
        flubrf.setText("感冒指数---" + mSuggestion.flubrf);
        flu_txt.setText(mSuggestion.flutex);
        drsgbrf.setText("穿衣指数---" + mSuggestion.drsgbrf);
        drsg_txt.setText(mSuggestion.drsgtex);
        travbrf.setText("旅游指数---" + mSuggestion.travbrf);
        trav_txt.setText(mSuggestion.travtex);
        sportbrf.setText("运动指数---" + mSuggestion.sportbrf);
        sport_txt.setText(mSuggestion.sporttex);
        index.setText(index.getText().toString() + "," + "紫外线强度" + mSuggestion.uvbrf + "," + mSuggestion.cwbrf + "洗车");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                this.overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
        }
    }

    /**
     * 设置地图
     */
    private void setMap(String lat1, String lon1) {
        double lat = Double.parseDouble(lat1);
        double lon = Double.parseDouble(lon1);
        final BaiduMap baiduMap = map.getMap();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        latLng = new LatLng(lat, lon);
        // 图标
        overlayOptions = new MarkerOptions().position(latLng)
                .icon(bitmap).zIndex(5);
        marker = (Marker) (baiduMap.addOverlay(overlayOptions));
        MapStatusUpdate cenptmsu = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lon));
        baiduMap.setMapStatus(cenptmsu);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                InfoWindow mInfoWindow;
                TextView location = new TextView(getApplicationContext());
                location.setPadding(10, 10, 5, 20);
                location.setTextColor(AqiActivity.this.getResources().getColor(R.color.background_progress));
                location.setTextSize(12);
                location.setText("小背熊提醒您:待真机测试,模拟器无法定位到精确位置!");
                final LatLng ll = marker.getPosition();
                android.graphics.Point p = baiduMap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
                mInfoWindow = new InfoWindow(location, llInfo, 2);
                //                    mInfoWindow = new InfoWindow(location, llInfo, 2, new InfoWindow.OnInfoWindowClickListener() {
                //                        @Override
                //                        public void onInfoWindowClick() {
                //                            baiduMap.hideInfoWindow();
                //                        }
                //                    });
                baiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }
}
