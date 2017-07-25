package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.CurrentCityActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.Managecity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MoreInfoActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Aqi;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Zodiac;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.DateToWeek;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ShareUtils;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HorizontalRecyclerView;
import com.example.administrator.soweather.com.example.administrator.soweather.view.MarqueeView;
import com.example.administrator.soweather.com.example.administrator.soweather.view.WeatherChartView;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private Toolbar mToolbar;


    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView date;//更新时间
    private TextView mTmp;//温度
    private TextView code_txt;//天气描述
    private String tem_min_max;//最高温度和最低温度
    private TextView pm;//pm2.5
    private TextView qlty;//空气质量描述
    private LinearLayout life;
    private String cityid;
    private String city;
    private String county;
    private Handler mHandler;
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();
    private NowWeather mNowWeather = new NowWeather();
    private List<WeathImg> weathimgs = new ArrayList<>();
    private SoWeatherDB cityDB;
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private String dir;
    private String sc;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String vis;
    private RecyclerView day_weather;
    private GalleryAdapter mDailyAdapter;
    private Aqi mAqi = new Aqi();
    private LinearLayout aqi;
    private LinearLayout linearLayout2;
    private DisplayMetrics dm = new DisplayMetrics();
    private ImageView bg;
    private ImageView gif;
    private Boolean isShowWeatherChart = false;
    private ImageView wind_img;
    private MarqueeView wind_txt;
    private List<String> items = new ArrayList<>();
    private String mDate;
    private Zodiac mZodiac = new Zodiac();
    private TextView ji;
    private TextView yi;
    private ImageView aqi_img;
    private LinearLayout today_detail;
    private SwipeRefreshLayout mSwipeLayout;
    private int flag = 0;
    private String current;
    private TextView city_name;
    private TimeAdapter mTimeAdapter;
    private RecyclerView time_weather;
    private LinearLayout day_weather_chart;
    private LinearLayout time_weather_chart;
    private LinearLayout day_weather_content;
    private Boolean isShowTimeLayout = true;
    private Boolean isShowDayLayout = true;
    private ImageView is_show_day_layout;
    private ImageView is_show_time_layout;
    private LinearLayout dailyForecast;
    private Suggestion mSuggestion = new Suggestion();

    private TextView flubrf;
    private TextView drsgbrf;
    private TextView travbrf;
    private TextView sportbrf;

    private TextView cunty_name;

    private TextView pm10;
    private TextView pm25;
    private TextView no2;
    private TextView so2;
    private TextView co;
    private TextView o3;
    private ProgressBar circle_pm10;
    private ProgressBar circle_pm25;
    private ProgressBar circle_no2;
    private ProgressBar circle_so2;
    private ProgressBar circle_co;
    private ProgressBar circle_o3;
    private TextView time_weather_tip;
    private int succe = 0;

    private FloatingActionButton fabutton;

    private LinearLayout hour_layout;


    private ArrayList<String> errors = new ArrayList<>();


    private String currentWeather = null;


    private LinearLayout trav_layout;
    private LinearLayout drsg_layout;
    private LinearLayout flu_layout;
    private LinearLayout sport_layout;
    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
            county = getArguments().getString("county");
            //根据城市名找到城市Id
            provinces = cityDB.getAllProvince();
            if (city != null) {
                for (int i = 0; i < provinces.size(); i++) {
                    String provin = provinces.get(i).getProvinceName();
                    cities = cityDB.getAllCity(provin);
                    for (int j = 0; j < cities.size(); j++) {
                        if (cities.get(j).getCityName().contains(city)) {
                            cityid = cities.get(j).getCityId();
                        }
                    }
                }
                city_name.setText(city);
                if (!county.equals(city)) {
                    cunty_name.setText(county);
                } else {
                    cunty_name.setText("");
                }
                getDate();
            } else {
                city = config.getLocationCity();
                county = config.getLocationCounty();
                if (city == null) {
                    getLocationAdress();
                } else {
                    city_name.setText(city);
                    cunty_name.setText(county);
                    getDate();
                }
            }
        }
    }

    /**
     * 定位城市
     */
    private void getLocationAdress() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        setLocationOption();
        mLocationClient.start();
    }


    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Appconfiguration.getInstance().setLocationCity(location.getCity());
            Appconfiguration.getInstance().setLocationCounty(location.getDistrict());
            city = location.getCity();
            county = location.getDistrict();
            if (city != null) {
                city_name.setText(city);
                cunty_name.setText(county);
                getDate();
            } else {
                Snackbar.make(fabutton, "定位失败,已默认城市为杭州,请手动修改城市!", Snackbar.LENGTH_LONG)
                        .setAction("了解", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        })
                        .show();
                city_name.setText("杭州");
            }
            mLocationClient.stop();
        }


        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }


    //设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
        getDailyforecastData(cityid);
        getHourlyforecastData(cityid);
        getNowWeatherData(cityid);
        getWeather(cityid);
        getSuggestionData(cityid);
    }

    private void getDate() {
        config.showProgressDialog("正在加载...", getActivity());
        getDailyforecastData(cityid);
        getHourlyforecastData(cityid);
        getNowWeatherData(cityid);
        getWeather(cityid);
        getSuggestionData(cityid);
    }

    private void getSuggestionData(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getSuggestionData(new ResponseListenter<Suggestion>() {
            @Override
            public void onReceive(Result<Suggestion> result) {
                if (result.isSuccess()) {
                    mSuggestion = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(6, mSuggestion));
                    succe = succe + 1;
                } else {
                    checkError(result.getErrorMessage());
                }
            }
        }, cityid);
    }


    /**
     * 天气预报集合接口获取aqi
     *
     * @param cityid
     */
    private void getWeather(String cityid) {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getWeatherData(new ResponseListenter<Aqi>() {
            @Override
            public void onReceive(Result<Aqi> result) {
                if (result.isSuccess()) {
                    mAqi = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mNowWeather));
                    succe = succe + 1;
                } else {
                    checkError(result.getErrorMessage());
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
                    succe = succe + 1;
                } else {
                    checkError(result.getErrorMessage());
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
                    mHandler.sendMessage(mHandler.obtainMessage(5, mHourlyforecast));
                    succe = succe + 1;
                } else {
                    checkError(result.getErrorMessage());
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
                    succe = succe + 1;
                } else {
                    checkError(result.getErrorMessage());
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
                        setAqi(mAqi);
                        break;
                    case 2:
                        setNowWeatherView(mNowWeather);
                        break;
                    case 3:
                        setOntherView(mDailyforecast);
                        break;
                    case 5:
                        setHourWeather(mHourlyforecast);
                        break;
                    case 6:
                        setSuggestion(mSuggestion);
                        break;
                }
                checkSucce();
            }
        };
    }

    private void checkSucce() {
        if (succe == 5) {
            config.dismissProgressDialog();
        }
    }

    private void checkError(String error) {
        errors.add(error);
        if (errors != null && errors.size() > 0) {
            config.dismissProgressDialog();
            for (int i = 0; i < errors.size(); i++) {
                Snackbar.make(fabutton, errors.get(i), Snackbar.LENGTH_LONG)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDate();
                            }
                        })
                        .show();
            }
        }

    }

    private void setSuggestion(Suggestion mSuggestion) {
        flubrf.setText(mSuggestion.flubrf);
//        flu_txt.setText(mSuggestion.flutex);
        drsgbrf.setText(mSuggestion.drsgbrf);
//        drsg_txt.setText(mSuggestion.drsgtex);
        travbrf.setText(mSuggestion.travbrf);
//        trav_txt.setText(mSuggestion.travtex);
        sportbrf.setText(mSuggestion.sportbrf);
//        sport_txt.setText(mSuggestion.sporttex);
//        wind.setText(wind.getText().toString() + ",  " + "紫外线强度" + mSuggestion.uvbrf + ",  " + mSuggestion.cwbrf + "洗车 !");
    }


    private void setZodiac(Zodiac mZodiac) {
        yi.setText(mZodiac.yi);
        ji.setText(mZodiac.ji);
    }

    private void setAqi(Aqi mAqi) {
        pm.setText(mAqi.aqi);
        if (mAqi.qlty.equals("优")) {
            aqi_img.setImageResource(R.mipmap.aqi_1);
            pm.setTextColor(getResources().getColor(R.color.gred));
            qlty.setTextColor(getResources().getColor(R.color.gred));
            mTmp.setTextColor(getResources().getColor(R.color.gred));
            code_txt.setTextColor(getResources().getColor(R.color.gred));

        } else if (mAqi.qlty.equals("良")) {
            aqi_img.setImageResource(R.mipmap.aqi_2);
            pm.setTextColor(getResources().getColor(R.color.yellow));
            qlty.setTextColor(getResources().getColor(R.color.yellow));
            mTmp.setTextColor(getResources().getColor(R.color.yellow));
            code_txt.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            aqi_img.setImageResource(R.mipmap.aqi_3);
            pm.setTextColor(getResources().getColor(R.color.red));
            qlty.setTextColor(getResources().getColor(R.color.red));
            mTmp.setTextColor(getResources().getColor(R.color.red));
            code_txt.setTextColor(getResources().getColor(R.color.red));
        }
        qlty.setText(mAqi.qlty);

        pm10.setText(mAqi.pm10);
        pm25.setText(mAqi.pm25);
        co.setText(mAqi.co);
        no2.setText(mAqi.no2);
        o3.setText(mAqi.o3);
        so2.setText(mAqi.so2);


        circle_pm10.setProgress(Integer.valueOf(mAqi.pm10));
        circle_pm25.setProgress(Integer.valueOf(mAqi.pm25));
        circle_co.setProgress(Integer.valueOf(mAqi.co));
        circle_no2.setProgress(Integer.valueOf(mAqi.no2));
        circle_o3.setProgress(Integer.valueOf(mAqi.o3));
        circle_so2.setProgress(Integer.valueOf(mAqi.so2));
        visitityAqi();
    }

    private void setNowWeatherView(NowWeather mNowWeather) {
        try {
            Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.wind);
            wind_img.startAnimation(anim1);
            String wind = mNowWeather.dir + mNowWeather.sc + "级";
            String temp = "当前温度" + mNowWeather.tmp + "℃  " + "  体感温度" + mNowWeather.fl + "℃";
            String desc1 = "降水量" + mNowWeather.pcpn + "mm  " + "  能见度" + mNowWeather.vis + "km";
            String dedc2 = "相对湿度" + mNowWeather.hum + "%  " + "  气压" + mNowWeather.pres;
            items.add(wind);
            items.add(temp);
            items.add(desc1);
            items.add(dedc2);
            wind_txt.startWithList(items);
            date.setText("更新于 " + new JSONObject(mNowWeather.update).optString("loc"));
            mDate = new JSONObject(mNowWeather.update).optString("loc").substring(0, 10);
            mTmp.setText(mNowWeather.tmp + "℃");
            String txt = new JSONObject(mNowWeather.cond).optString("txt");
            current = txt;
            code_txt.setText(txt);
            dir = mNowWeather.dir;
            sc = mNowWeather.sc;
            fl = mNowWeather.fl;
            hum = mNowWeather.hum;
            pcpn = mNowWeather.pcpn;
            pres = mNowWeather.pres;
            vis = mNowWeather.vis;
            Constans.WeatherBgImg citys[] = Constans.WeatherBgImg.values();
            for (Constans.WeatherBgImg cu : citys) {
                if (txt.equals(cu.getName())) {
                    if (cu.getimgId().length > 0) {
                        int rand = (int) Math.round(Math.random() * (cu.getimgId().length - 1));
                        flag = rand;
                        Glide.with(getActivity()).load(cu.getimgId()[rand]).crossFade()
                                .placeholder(R.drawable.bg_loading_eholder).into(bg);
                    } else {
                        Glide.with(getActivity()).load(cu.getimgId()[0]).crossFade()
                                .placeholder(R.drawable.bg_loading_eholder).into(bg);
                    }
                    break;
                }
            }
            visitityToday();
            setShareWeather(mNowWeather);
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }


    private void setHourWeather(List<Hourlyforecast> mHourlyforecast) {
        if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
            time_weather.setVisibility(View.VISIBLE);
            time_weather_tip.setVisibility(View.GONE);
            time_weather.setAdapter(mTimeAdapter);
            mTimeAdapter.notifyDataSetChanged(mHourlyforecast);
        } else {
            time_weather.setVisibility(View.GONE);
            time_weather_tip.setVisibility(View.VISIBLE);
        }
    }


    private void setOntherView(final List<Dailyforecast> mDailyforecast) {
        try {
            String max = new JSONObject(mDailyforecast.get(0).tmp).optString("max");
            String min = new JSONObject(mDailyforecast.get(0).tmp).optString("min");
            tem_min_max = min + "℃" + "~" + max + "℃";
            mDailyAdapter = new GalleryAdapter(getActivity(), mDailyforecast);
            day_weather.setAdapter(mDailyAdapter);
            mDailyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, Dailyforecast data) {
                    //展示界面
                    Intent intent = new Intent(getActivity(), MoreInfoActivity.class);
                    intent.putExtra("city", city != null ? city : "杭州");
                    intent.putExtra("county", county != null ? county : "");
                    intent.putExtra("cityid", cityid != null ? city : "CN101210101");
                    intent.putExtra("date", (Serializable) mDailyforecast);
                    intent.putExtra("qlty", qlty.getText().toString());
                    intent.putExtra("time", data.date);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }

        if (!isShowWeatherChart) {
            dailyForecast.setVisibility(View.GONE);
        } else {
            dailyForecast.setVisibility(View.VISIBLE);
            dailyForecast.setPadding(0, dp2px(getContext(), 16), 0, dp2px(getContext(), 16));
            dailyForecast.removeAllViews();
            dailyForecast.addView(getChartView(mDailyforecast));
        }
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        fabutton = (FloatingActionButton) view.findViewById(R.id.fab);
        mToolbar.setTitle("天气");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        mToolbar.inflateMenu(R.menu.menu_weather);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_share) {
                    //分享天气
                    shareWeather();
                    return true;
                } else if (id == R.id.menu_tts) {
                    Intent intent = new Intent(getActivity(), CurrentCityActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        gif = (ImageView) view.findViewById(R.id.gif);
        dailyForecast = (LinearLayout) view.findViewById(R.id.contentLayout);
        day_weather_chart = (LinearLayout) view.findViewById(R.id.day_weather_chart);
        day_weather_content = (LinearLayout) view.findViewById(R.id.day_weather_content);
        time_weather_chart = (LinearLayout) view.findViewById(R.id.time_weather_chart);
        is_show_day_layout = (ImageView) view.findViewById(R.id.is_show_day_layout);
        is_show_time_layout = (ImageView) view.findViewById(R.id.is_show_time_layout);
        time_weather = (RecyclerView) view.findViewById(R.id.time_weather);
        city_name = (TextView) view.findViewById(R.id.city_name);
        flubrf = (TextView) view.findViewById(R.id.flubrf);
        drsgbrf = (TextView) view.findViewById(R.id.drsgbrf);
        travbrf = (TextView) view.findViewById(R.id.travbrf);
        sportbrf = (TextView) view.findViewById(R.id.sportbrf);
        today_detail = (LinearLayout) view.findViewById(R.id.today_detail);
        today_detail.setOnClickListener(this);
        wind_img = (ImageView) view.findViewById(R.id.wind_img);
        wind_txt = (MarqueeView) view.findViewById(R.id.wind_txt);
        date = (TextView) view.findViewById(R.id.date);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        code_txt = (TextView) view.findViewById(R.id.code_txt);
        pm = (TextView) view.findViewById(R.id.pm);
        qlty = (TextView) view.findViewById(R.id.qlty);
        life = (LinearLayout) view.findViewById(R.id.life);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        day_weather = (RecyclerView) view.findViewById(R.id.day_weather);
        aqi_img = (ImageView) view.findViewById(R.id.aqi_img);
        aqi = (LinearLayout) view.findViewById(R.id.aqi);
        bg = (ImageView) view.findViewById(R.id.bg);
        yi = (TextView) view.findViewById(R.id.yi);
        ji = (TextView) view.findViewById(R.id.ji);
        cunty_name = (TextView) view.findViewById(R.id.cunty_name);
        time_weather_tip = (TextView) view.findViewById(R.id.time_weather_tip);
        pm10 = (TextView) view.findViewById(R.id.pm10);
        pm25 = (TextView) view.findViewById(R.id.pm25);
        no2 = (TextView) view.findViewById(R.id.no2);
        so2 = (TextView) view.findViewById(R.id.so2);
        co = (TextView) view.findViewById(R.id.co);
        o3 = (TextView) view.findViewById(R.id.o3);
        circle_pm10 = (ProgressBar) view.findViewById(R.id.circle_pm10);
        circle_pm25 = (ProgressBar) view.findViewById(R.id.circle_pm25);
        circle_no2 = (ProgressBar) view.findViewById(R.id.circle_no2);
        circle_so2 = (ProgressBar) view.findViewById(R.id.circle_so2);
        circle_co = (ProgressBar) view.findViewById(R.id.circle_co);
        circle_o3 = (ProgressBar) view.findViewById(R.id.circle_o3);

        sport_layout = (LinearLayout) view.findViewById(R.id.sport_layout);
        trav_layout = (LinearLayout) view.findViewById(R.id.trav_layout);
        drsg_layout = (LinearLayout) view.findViewById(R.id.drsg_layout);
        flu_layout = (LinearLayout) view.findViewById(R.id.flu_layout);
        sport_layout.setOnClickListener(this);
        trav_layout.setOnClickListener(this);
        drsg_layout.setOnClickListener(this);
        flu_layout.setOnClickListener(this);

        hour_layout = (LinearLayout) view.findViewById(R.id.hour_layout);
        circle_pm10.setMax(400);
        circle_pm25.setMax(400);
        circle_so2.setMax(400);
        circle_no2.setMax(400);
        circle_co.setMax(1000);
        circle_o3.setMax(1000);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        day_weather.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        time_weather.setLayoutManager(linearLayoutManager1);
        time_weather.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mTimeAdapter = new TimeAdapter();
        time_weather.setAdapter(mTimeAdapter);
        day_weather_chart.setOnClickListener(this);
        time_weather_chart.setOnClickListener(this);
        life.setOnClickListener(this);
        aqi.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        fabutton.setOnClickListener(this);
        gif.setOnClickListener(this);
        if (!isShowWeatherChart) {
            Glide.with(this).load(R.mipmap.gif2).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gif);
        } else {
            Glide.with(this).load(R.mipmap.gif3).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gif);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life:
                //展示界面
                Intent intent8 = new Intent(getActivity(), MoreInfoActivity.class);
                intent8.putExtra("city", city != null ? city : "杭州");
                intent8.putExtra("cityid", cityid != null ? city : "CN101210101");
                intent8.putExtra("qlty", qlty.getText().toString());
                intent8.putExtra("county", county != null ? county : "");
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    intent8.putExtra("date", (Serializable) mDailyforecast);
                    intent8.putExtra("time", mDailyforecast.get(0).date);
                }
                startActivity(intent8);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.aqi:
                //展示界面
                Intent intent9 = new Intent(getActivity(), MoreInfoActivity.class);
                intent9.putExtra("city", city != null ? city : "杭州");
                intent9.putExtra("cityid", cityid != null ? city : "CN101210101");
                intent9.putExtra("qlty", qlty.getText().toString());
                intent9.putExtra("county", county != null ? county : "");
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    intent9.putExtra("date", (Serializable) mDailyforecast);
                    intent9.putExtra("time", mDailyforecast.get(0).date);
                }
                startActivity(intent9);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.linearLayout2:
                //展示界面
                Intent intent = new Intent(getActivity(), MoreInfoActivity.class);
                intent.putExtra("city", city != null ? city : "杭州");
                intent.putExtra("cityid", cityid != null ? city : "CN101210101");
                intent.putExtra("qlty", qlty.getText().toString());
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    intent.putExtra("date", (Serializable) mDailyforecast);
                    intent.putExtra("time", mDailyforecast.get(0).date);
                }
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.today_detail:
                Intent intent10 = new Intent(getActivity(), MoreInfoActivity.class);
                intent10.putExtra("city", city != null ? city : "杭州");
                intent10.putExtra("cityid", cityid != null ? city : "CN101210101");
                intent10.putExtra("qlty", qlty.getText().toString());
                intent10.putExtra("county", county != null ? county : "");
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    intent10.putExtra("date", (Serializable) mDailyforecast);
                    intent10.putExtra("time", mDailyforecast.get(0).date);
                }
                startActivity(intent10);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.day_weather_chart:
                isShowDayLayout = !isShowDayLayout;
                Animation rotate1 = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);//创建动画
                rotate1.setInterpolator(new LinearInterpolator());//设置为线性旋转
                rotate1.setFillAfter(!isShowDayLayout);
                is_show_day_layout.startAnimation(rotate1);
                if (!isShowDayLayout) {
                    day_weather_content.setVisibility(View.GONE);
                } else {
                    day_weather_content.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.time_weather_chart:
                isShowTimeLayout = !isShowTimeLayout;
                Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);//创建动画
                rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转
                rotate.setFillAfter(!isShowTimeLayout);
                is_show_time_layout.startAnimation(rotate);
                if (!isShowTimeLayout) {
                    if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                        time_weather.setVisibility(View.GONE);
                    } else {
                        time_weather_tip.setVisibility(View.GONE);
                    }
                } else {
                    if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                        time_weather.setVisibility(View.VISIBLE);
                    } else {
                        time_weather_tip.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.fab:
                //管理城市   添加的城市,包括当前位置城市,卡片显示天气
                Intent intent7 = new Intent(getActivity(), Managecity.class);
                startActivity(intent7);
                break;
            case R.id.sport_layout:
                showLifeIndexFragment(v);
                break;
            case R.id.trav_layout:
                showLifeIndexFragment(v);
                break;
            case R.id.drsg_layout:
                showLifeIndexFragment(v);
                break;
            case R.id.flu_layout:
                showLifeIndexFragment(v);
                break;
            case R.id.gif:
                isShowWeatherChart = !isShowWeatherChart;
                if (!isShowWeatherChart) {
                    dailyForecast.setVisibility(View.GONE);
                    Glide.with(this).load(R.mipmap.gif2).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gif);
                } else {
                    Glide.with(this).load(R.mipmap.gif3).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(gif);
                    dailyForecast.setVisibility(View.VISIBLE);
                    dailyForecast.setPadding(0, dp2px(getContext(), 16), 0, dp2px(getContext(), 16));
                    dailyForecast.removeAllViews();
                    dailyForecast.addView(getChartView(mDailyforecast));
                }

                break;
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Dailyforecast data);
    }

    public class GalleryAdapter extends
            HorizontalRecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
        private List<Dailyforecast> mData = new ArrayList<Dailyforecast>();
        private LayoutInflater inflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public GalleryAdapter(Context context, List<Dailyforecast> datats) {
            inflater = LayoutInflater.from(context);
            mData = datats;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (Dailyforecast) v.getTag());
            }
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        public class ViewHolder extends HorizontalRecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView txt_img;
            TextView week;
            TextView tmp;
            TextView cond_txt;
            View view;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.item_daily_forecst,
                    viewGroup, false);
            GalleryAdapter.ViewHolder viewHolder = new GalleryAdapter.ViewHolder(view);
            viewHolder.week = (TextView) view.findViewById(R.id.week);
            viewHolder.txt_img = (ImageView) view.findViewById(R.id.txt_img);
            viewHolder.tmp = (TextView) view.findViewById(R.id.tmp);
            viewHolder.cond_txt = (TextView) view.findViewById(R.id.cond_txt);
            viewHolder.view = (View) view.findViewById(R.id.view);
            view.setOnClickListener(this);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.itemView.setTag(mData.get(i));
            Dailyforecast mDailyForecastData = mData.get(i);
            if (i == 0) {
                viewHolder.week.setText("今天");
            } else if (i == 1) {
                viewHolder.week.setText("明天");
            } else {
                viewHolder.week.setText(DateToWeek.getWeek(mDailyForecastData.date));
            }
            String code = null;
            try {
                String min = new JSONObject(mDailyForecastData.tmp).optString("min");
                String max = new JSONObject(mDailyForecastData.tmp).optString("max");
                viewHolder.tmp.setText(min + "~" + max + "℃");
                code = new JSONObject(mDailyForecastData.cond).optString("code_d");
                viewHolder.cond_txt.setText(new JSONObject(mDailyForecastData.cond).optString("txt_d"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    viewHolder.txt_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
            if (i == mData.size() - 1) {
                viewHolder.view.setVisibility(View.GONE);
            }
            viewHolder.itemView.setTag(mDailyForecastData);
            viewHolder.itemView.setOnClickListener(this);
        }
    }

    private void visitityToday() {
        Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_view_visible);
        anim1.setDuration(1000);
        date.startAnimation(anim1);
        today_detail.startAnimation(anim1);
        life.startAnimation(anim1);
    }


    private void visitityAqi() {
        Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_view_visible);
        anim1.setDuration(1000);
        aqi.startAnimation(anim1);
    }


    public class TimeAdapter extends
            RecyclerView.Adapter<TimeAdapter.ViewHolder> {
        private List<Hourlyforecast> mData = new ArrayList<Hourlyforecast>();

        public void notifyDataSetChanged(List<Hourlyforecast> date) {
            this.mData.clear();
            this.mData.addAll(date);
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView txt_img;
            TextView date;
            TextView tmp;
            TextView decs;
            View view;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_time_weather, viewGroup, false);
            TimeAdapter.ViewHolder viewHolder = new TimeAdapter.ViewHolder(view);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.txt_img = (ImageView) view.findViewById(R.id.txt_img);
            viewHolder.tmp = (TextView) view.findViewById(R.id.tmp);
            viewHolder.decs = (TextView) view.findViewById(R.id.desc);
            viewHolder.view = (View) view.findViewById(R.id.view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final TimeAdapter.ViewHolder viewHolder, final int i) {
            Hourlyforecast mTimeWeatherData = mData.get(i);
            viewHolder.date.setText(mTimeWeatherData.date.substring(10, mTimeWeatherData.date.length()));
            String code = null;
            try {
                String min = mTimeWeatherData.tmp;
                viewHolder.tmp.setText(min + "℃");
                code = new JSONObject(mTimeWeatherData.cond).optString("code");
                String txt = new JSONObject(mTimeWeatherData.cond).optString("txt");
                viewHolder.tmp.setText(txt);
                viewHolder.decs.setText("温度" + min + "℃ " + "降水率为" + mTimeWeatherData.pop);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    viewHolder.txt_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
            if (i == mData.size() - 1) {
                viewHolder.view.setVisibility(View.GONE);
            }
        }
    }


    /**
     * RecyclerView的间隔问题
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }

    private void setShareWeather(NowWeather mNowWeather) {
        StringBuffer message = new StringBuffer();
        message.append(city_name.getText().toString());
        message.append("天气：");
        message.append("\r\n");
        try {
            message.append(new JSONObject(mNowWeather.update).optString("loc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.append(" 发布：");
        message.append("\r\n");
        try {
            message.append(new JSONObject(mNowWeather.cond).optString("txt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.append("，");
        message.append(mNowWeather.tmp + "℃");
        message.append("。");
        message.append("\r\n");
        message.append("来自：").append("YOYO天气");
        currentWeather = message.toString();
    }

    private void shareWeather() {
        if (currentWeather == null)
            return;
        ShareUtils.shareText(getActivity(), currentWeather);
    }

    private void showLifeIndexFragment(View view) {
        String title = "";
        String brf = "";
        String tex = "";
////        wind.setText(wind.getText().toString() + ",  " + "紫外线强度" + mSuggestion.uvbrf + ",  " + mSuggestion.cwbrf + "洗车 !");
        if (mSuggestion != null) {
            if (view.getId() == R.id.sport_layout) {
                title = "运动指数";
                brf = mSuggestion.sportbrf;
                tex = mSuggestion.sporttex;
            }
            if (view.getId() == R.id.trav_layout) {
                title = "旅游指数";
                brf = mSuggestion.travbrf;
                tex = mSuggestion.travtex;
            }
            if (view.getId() == R.id.flu_layout) {
                title = "感冒指数";
                brf = mSuggestion.flubrf;
                tex = mSuggestion.flutex;
            }
            if (view.getId() == R.id.drsg_layout) {
                title = "穿衣指数";
                brf = mSuggestion.drsgbrf;
                tex = mSuggestion.drsgtex;
            }
        }
        LifeIndexDialogFragment lifeIndexDialogFragment = new LifeIndexDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("brf", brf);
        args.putString("tex", tex);
        lifeIndexDialogFragment.setArguments(args);
        lifeIndexDialogFragment.show(this.getActivity()
                .getSupportFragmentManager(), null);
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private WeatherChartView getChartView(List<Dailyforecast> mDailyforecast) {
        WeatherChartView chartView = new WeatherChartView(getContext());
        if (mDailyforecast != null && mDailyforecast.size() > 0) {
            weathimgs = cityDB.getAllWeatherImg();
            chartView.setWeather5(mDailyforecast, weathimgs);
        } else {
            Snackbar.make(fabutton, "当前暂无数据请重试 !", Snackbar.LENGTH_LONG)
                    .setAction("了解", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    })
                    .show();
        }
        return chartView;
    }


}
