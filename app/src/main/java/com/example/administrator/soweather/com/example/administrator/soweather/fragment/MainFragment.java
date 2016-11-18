package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.model.inner.Point;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.CustomerServiceActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.DayWeatherActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.HourWeatherActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.GifView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView date;//更新时间
    private TextView dir;//风向
    private TextView dir_sc;//风级
    private ImageView weatherImg;//天气图标
    private TextView mTmp;//温度
    private TextView tmp_max;//最高温度
    private TextView tmp_min; //最低温度
    private TextView code_txt;//天气描述
    private TextView hum;//相对湿度
    private TextView vis;//能见度
    private ImageView time;
    private ImageView day;
    private GifView add_mood_line;
    private TextView flubrf;
    private TextView flu_txt;
    private TextView drsgbrf;
    private TextView drsg_txt;
    private TextView travbrf;
    private TextView trav_txt;
    private TextView sportbrf;
    private TextView sport_txt;
    private String cityid;
    private String city;
    private Handler mHandler;
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();
    private NowWeather mNowWeather = new NowWeather();
    private Suggestion mSuggestion = new Suggestion();
    private List<WeathImg> weathimgs = new ArrayList<>();
    private TextureMapView map;
    private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.place);
    private SoWeatherDB cityDB;
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        cityDB = SoWeatherDB.getInstance(getActivity());
        initView(view);
        getAdress();
        getDate();
        getHandleMessge();
        return view;
    }


    /**
     * 获取传递的city和cityid
     */
    private void getAdress() {
        if (getArguments() != null) {
            cityid = getArguments().getString("cityId");
            city = getArguments().getString("city");
            if (cityid == null && (city == null || city.equals("获取位置失败") || city.equals("获取位置异常"))) {
                Toast.makeText(getActivity(), "当前定位城市失败,请手动选择", Toast.LENGTH_LONG).show();
            } else if (cityid == null && (city != null && (!city.equals("获取位置失败")) || !city.equals("获取位置异常"))) {
                //根据城市名找到城市Id
                provinces = cityDB.getAllProvince();
                for (int i = 0; i < provinces.size(); i++) {
                    String provin = provinces.get(i).getProvinceName();
                    cities = cityDB.getAllCity(provin);
                    for (int j = 0; j < cities.size(); j++) {
                        if (cities.get(j).getCityName().contains(city)) {
                            cityid = cities.get(j).getCityId();
                        }
                    }
                }
            }
        }
    }

    private void getDate() {
        config.showProgressDialog("正在加载...", getActivity());
        getDailyforecastData(cityid);
        getHourlyforecastData(cityid);
        getNowWeatherData(cityid);
        getSuggestionData(cityid);
    }

    /**
     * 获取生活指数
     *
     * @param cityid
     */
    private void getSuggestionData(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getSuggestionData(new ResponseListenter<Suggestion>() {
            @Override
            public void onReceive(Result<Suggestion> result) {
                if (result.isSuccess()) {
                    mSuggestion = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mSuggestion));
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    /**
     * 获取天气实况
     *
     * @param cityid
     */
    private void getNowWeatherData(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getNowWeatherData(new ResponseListenter<NowWeather>() {
            @Override
            public void onReceive(Result<NowWeather> result) {
                if (result.isSuccess()) {
                    mNowWeather = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(2, mNowWeather));
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    /**
     * 获取小时预报
     *
     * @param cityid
     */
    private void getHourlyforecastData(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getHourlyforecastData(new ResponseListenter<List<Hourlyforecast>>() {
            @Override
            public void onReceive(Result<List<Hourlyforecast>> result) {
                if (result.isSuccess()) {
                    mHourlyforecast = result.getData();
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    /**
     * 获取日期预报
     *
     * @param cityid
     */
    private void getDailyforecastData(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getDailyforecastData(new ResponseListenter<List<Dailyforecast>>() {
            @Override
            public void onReceive(Result<List<Dailyforecast>> result) {
                if (result.isSuccess()) {
                    mDailyforecast = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(3, mDailyforecast));
                } else {
                    config.dismissProgressDialog();
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
                        setSuggestionView(mSuggestion);
                        break;
                    case 2:
                        setNowWeatherView(mNowWeather);
                        break;
                    case 3:
                        setOntherView(mDailyforecast);
                        break;
                }
            }
        };
    }

    private void setSuggestionView(Suggestion mSuggestion) {
        config.dismissProgressDialog();
        flubrf.setText("感冒指数---" + mSuggestion.flubrf);
        flu_txt.setText(mSuggestion.flutex);
        drsgbrf.setText("穿衣指数---" + mSuggestion.drsgbrf);
        drsg_txt.setText(mSuggestion.drsgtex);
        travbrf.setText("旅游指数---" + mSuggestion.travbrf);
        trav_txt.setText(mSuggestion.travtex);
        sportbrf.setText("运动指数---" + mSuggestion.sportbrf);
        sport_txt.setText(mSuggestion.sporttex);
    }

    private void setNowWeatherView(NowWeather mNowWeather) {
        config.dismissProgressDialog();
        try {
            date.setText(new JSONObject(mNowWeather.update).optString("loc"));
            dir.setText(mNowWeather.dir);
            dir_sc.setText(mNowWeather.sc + "级");
            mTmp.setText(mNowWeather.tmp + "℃");
            hum.setText("相对湿度:  " + mNowWeather.hum + "%");
            vis.setText("能见度:  " + mNowWeather.vis + "km");
            String code = new JSONObject(mNowWeather.cond).optString("code");
            String txt = new JSONObject(mNowWeather.cond).optString("txt");
            code_txt.setText(txt);
            weathimgs = cityDB.getAllWeatherImg();
            for (int i = 0; i < weathimgs.size(); i++) {
                if (code.equals(weathimgs.get(i).getCode())) {
                    weatherImg.setImageBitmap(weathimgs.get(i).getIcon());
                }
            }
            double lat = Double.parseDouble(mNowWeather.lat);
            double lon = Double.parseDouble(mNowWeather.lon);
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
                    TextView location = new TextView(getActivity().getApplicationContext());
                    location.setBackgroundResource(R.drawable.table_shape);
                    location.setPadding(10, 10, 5, 20);
                    location.setTextColor(getActivity().getResources().getColor(R.color.background_progress));
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
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }

    private void setOntherView(List<Dailyforecast> mDailyforecast) {
        config.dismissProgressDialog();
        try {
            String max = new JSONObject(mDailyforecast.get(0).tmp).optString("max");
            String min = new JSONObject(mDailyforecast.get(0).tmp).optString("min");
            tmp_max.setText(max + "℃");
            tmp_min.setText(min + "℃");
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }


    private void initView(View view) {
        date = (TextView) view.findViewById(R.id.date);
        weatherImg = (ImageView) view.findViewById(R.id.weatherImg);
        dir = (TextView) view.findViewById(R.id.dir);
        dir_sc = (TextView) view.findViewById(R.id.dir_sc);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        tmp_max = (TextView) view.findViewById(R.id.tmp_max);
        tmp_min = (TextView) view.findViewById(R.id.tmp_min);
        code_txt = (TextView) view.findViewById(R.id.code_txt);
        time = (ImageView) view.findViewById(R.id.time);
        day = (ImageView) view.findViewById(R.id.day);
        flubrf = (TextView) view.findViewById(R.id.flubrf);
        drsgbrf = (TextView) view.findViewById(R.id.drsgbrf);
        travbrf = (TextView) view.findViewById(R.id.travbrf);
        sportbrf = (TextView) view.findViewById(R.id.sportbrf);
        flu_txt = (TextView) view.findViewById(R.id.flu_txt);
        drsg_txt = (TextView) view.findViewById(R.id.drsg_txt);
        trav_txt = (TextView) view.findViewById(R.id.trav_txt);
        sport_txt = (TextView) view.findViewById(R.id.sport_txt);
        add_mood_line = (GifView) view.findViewById(R.id.add_mood_line);
        map = (TextureMapView) view.findViewById(R.id.map);
        hum = (TextView) view.findViewById(R.id.hum);
        vis = (TextView) view.findViewById(R.id.vis);
        add_mood_line.setMovieResource(R.mipmap.git);
        time.setOnClickListener(this);
        day.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time:
                //小时预报
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), HourWeatherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(HourWeatherActivity.DATA, (Serializable) mHourlyforecast);
                intent3.putExtras(bundle);
                getActivity().startActivity(intent3);
                break;
            case R.id.day:
                //七天-十天预报
                Intent intent2 = new Intent();
                intent2.setClass(getActivity(), DayWeatherActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable(DayWeatherActivity.DATA, (Serializable) mDailyforecast);
                bundle2.putString("cityname", city);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.add_mood_line:
                Intent intent = new Intent(getActivity(), CustomerServiceActivity.class);
                startActivity(intent);
                break;
        }
    }
}
