package com.lhlSo.soweather.module.weather.mvp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.lhlSo.soweather.R;
import com.lhlSo.soweather.base.BaseFragment;
import com.lhlSo.soweather.bean.City;
import com.lhlSo.soweather.bean.Province;
import com.lhlSo.soweather.bean.WeathImg;
import com.lhlSo.soweather.bean.WeatherDate;
import com.lhlSo.soweather.core.Appconfiguration;
import com.lhlSo.soweather.core.Constans;
import com.lhlSo.soweather.db.SoWeatherDB;
import com.lhlSo.soweather.general.LifeIndexDialogFragment;
import com.lhlSo.soweather.module.activity.CurrentCityActivity;
import com.lhlSo.soweather.module.activity.MainActivity;
import com.lhlSo.soweather.module.activity.Managecity;
import com.lhlSo.soweather.module.weather.MoreInfoActivity;
import com.lhlSo.soweather.utils.DateToWeek;
import com.lhlSo.soweather.utils.ShareUtils;
import com.lhlSo.soweather.widget.HorizontalRecyclerView;
import com.lhlSo.soweather.widget.MarqueeView;
import com.lhlSo.soweather.widget.WeatherChartView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LHLin on 2016/10/10.
 */
public class WeatherFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, WeatherContract.IWeatherView {
    WeatherPresenter mWeatherPresenter;
    private Appconfiguration config = Appconfiguration.getInstance();
    private String cityid;
    private String city;
    private String county;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private SoWeatherDB cityDB;
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private GalleryAdapter mDailyAdapter;
    private DisplayMetrics dm = new DisplayMetrics();
    private Boolean isShowWeatherChart = false;
    private List<String> items = new ArrayList<>();
    private String current;
    private TimeAdapter mTimeAdapter;
    private Boolean isShowTimeLayout = true;
    private Boolean isShowDayLayout = true;
    private String currentWeather = null;
    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private List<WeatherDate.DailyForecastBean> mDailyforecast;
    private List<WeatherDate.HourlyForecastBean> mHourlyforecast;
    private WeatherDate.SuggestionBean mSuggestion;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton fabutton;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.gif)
    ImageView gif;
    @BindView(R.id.contentLayout)
    LinearLayout dailyForecast;
    @BindView(R.id.day_weather_chart)
    LinearLayout day_weather_chart;
    @BindView(R.id.day_weather_content)
    LinearLayout day_weather_content;
    @BindView(R.id.time_weather_chart)
    LinearLayout time_weather_chart;
    @BindView(R.id.is_show_day_layout)
    ImageView is_show_day_layout;
    @BindView(R.id.is_show_time_layout)
    ImageView is_show_time_layout;
    @BindView(R.id.time_weather)
    RecyclerView time_weather;
    @BindView(R.id.city_name)
    TextView city_name;
    @BindView(R.id.flubrf)
    TextView flubrf;
    @BindView(R.id.drsgbrf)
    TextView drsgbrf;
    @BindView(R.id.travbrf)
    TextView travbrf;
    @BindView(R.id.sportbrf)
    TextView sportbrf;
    @BindView(R.id.today_detail)
    LinearLayout today_detail;
    @BindView(R.id.wind_img)
    ImageView wind_img;
    @BindView(R.id.wind_txt)
    MarqueeView wind_txt;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.tmp)
    TextView mTmp;
    @BindView(R.id.code_txt)
    TextView code_txt;
    @BindView(R.id.pm)
    TextView pm;
    @BindView(R.id.qlty)
    TextView qlty;
    @BindView(R.id.life)
    LinearLayout life;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.day_weather)
    RecyclerView day_weather;
    @BindView(R.id.aqi_img)
    ImageView aqi_img;
    @BindView(R.id.aqi)
    LinearLayout aqi;
    @BindView(R.id.bg)
    ImageView bg;
    @BindView(R.id.yi)
    TextView yi;
    @BindView(R.id.ji)
    TextView ji;
    @BindView(R.id.cunty_name)
    TextView cunty_name;
    @BindView(R.id.time_weather_tip)
    TextView time_weather_tip;
    @BindView(R.id.pm10)
    TextView pm10;
    @BindView(R.id.pm25)
    TextView pm25;
    @BindView(R.id.no2)
    TextView no2;
    @BindView(R.id.so2)
    TextView so2;
    @BindView(R.id.co)
    TextView co;
    @BindView(R.id.o3)
    TextView o3;
    @BindView(R.id.circle_pm10)
    ProgressBar circle_pm10;
    @BindView(R.id.circle_pm25)
    ProgressBar circle_pm25;
    @BindView(R.id.circle_no2)
    ProgressBar circle_no2;
    @BindView(R.id.circle_so2)
    ProgressBar circle_so2;
    @BindView(R.id.circle_co)
    ProgressBar circle_co;
    @BindView(R.id.circle_o3)
    ProgressBar circle_o3;
    @BindView(R.id.sport_layout)
    LinearLayout sport_layout;
    @BindView(R.id.trav_layout)
    LinearLayout trav_layout;
    @BindView(R.id.drsg_layout)
    LinearLayout drsg_layout;
    @BindView(R.id.flu_layout)
    LinearLayout flu_layout;
    @BindView(R.id.hour_layout)
    LinearLayout hour_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews() {
        cityDB = SoWeatherDB.getInstance(getActivity());
        initView();
        getAdress();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void lazyFetchData() {

    }

    /**
     * 获取地址  再获取天气相关
     * 1 获得修改定位城市   为null则为初次进入程序
     */
    private void getAdress() {
        if (getArguments() != null) {
            cityid = getArguments().getString("cityId");
            city = getArguments().getString("city");
            county = getArguments().getString("county");
            provinces = cityDB.getAllProvince();
            if (city != null) {
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

    @Override
    public Context getCurContext() {
        return getActivity();
    }

    @Override
    public void showProgress() {
        config.showProgressDialog("正在加载...", getActivity());
    }

    @Override
    public void hideProgress() {
        config.dismissProgressDialog();
    }

    @Override
    public void showData(WeatherDate weatherDate) {
        //将数据解析后加载到界面
        mDailyforecast = weatherDate.getDaily_forecast();
        mHourlyforecast = weatherDate.getHourly_forecast();
        mSuggestion = weatherDate.getSuggestion();
        setSuggestion(weatherDate.getSuggestion());
        setAqi(weatherDate.getAqi());
        setNowWeatherView(weatherDate.getNow(), weatherDate.getBasic());
        setHourWeather(weatherDate.getHourly_forecast());
        setOntherView(weatherDate.getDaily_forecast());
    }

    @Override
    public void showInfo(String info) {
        Snackbar.make(fabutton, info, Snackbar.LENGTH_LONG).show();
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
                city_name.setText("杭州");
                getDate();
            }
            mLocationClient.stop();
        }


        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }


    //定位设置相关参数
    private void setLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
        getDate();
    }

    private void getDate() {
        if (city != null && !city.equals("")) {
            for (int i = 0; i < provinces.size(); i++) {
                String provin = provinces.get(i).getProvinceName();
                cities = cityDB.getAllCity(provin);
                for (int j = 0; j < cities.size(); j++) {
                    if (cities.get(j).getCityName().contains(city)) {
                        cityid = cities.get(j).getCityId();
                    }
                }
            }
        } else {
            cityid = "CN101210101";
        }
        mWeatherPresenter = new WeatherPresenter(this);
        mWeatherPresenter.geWeather(cityid);
    }


    private void setSuggestion(WeatherDate.SuggestionBean mSuggestion) {
        flubrf.setText(mSuggestion.getFlu().getBrf());
        drsgbrf.setText(mSuggestion.getDrsg().getBrf());
        travbrf.setText(mSuggestion.getTrav().getBrf());
        sportbrf.setText(mSuggestion.getSport().getBrf());
    }


    private void setAqi(WeatherDate.AqiBean mAqi) {
        pm.setText(mAqi.getCity().getAqi());
        if (mAqi.getCity().getAqi().equals("优")) {
            aqi_img.setImageResource(R.mipmap.aqi_1);
            pm.setTextColor(getResources().getColor(R.color.gred));
            qlty.setTextColor(getResources().getColor(R.color.gred));
            mTmp.setTextColor(getResources().getColor(R.color.gred));
            code_txt.setTextColor(getResources().getColor(R.color.gred));

        } else if (mAqi.getCity().getAqi().equals("良")) {
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
        qlty.setText(mAqi.getCity().getQlty());
        pm10.setText(mAqi.getCity().getPm10());
        pm25.setText(mAqi.getCity().getPm25());
        co.setText(mAqi.getCity().getCo());
        no2.setText(mAqi.getCity().getNo2());
        o3.setText(mAqi.getCity().getO3());
        so2.setText(mAqi.getCity().getSo2());
        circle_pm10.setProgress(Integer.parseInt(mAqi.getCity().getPm10()));
        circle_pm25.setProgress(Integer.parseInt(mAqi.getCity().getPm25()));
        circle_co.setProgress(Integer.parseInt(mAqi.getCity().getCo()));
        circle_no2.setProgress(Integer.parseInt(mAqi.getCity().getNo2()));
        circle_o3.setProgress(Integer.parseInt(mAqi.getCity().getO3()));
        circle_so2.setProgress(Integer.parseInt(mAqi.getCity().getSo2()));
        visitityAqi();
    }

    private void setNowWeatherView(WeatherDate.NowBean mNowWeather, WeatherDate.BasicBean basic) {
        Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.wind);
        wind_img.startAnimation(anim1);
        String wind = mNowWeather.getWind().getDir() + mNowWeather.getWind().getSc() + "级";
        String temp = "当前温度" + mNowWeather.getTmp() + "℃  " + "  体感温度" + mNowWeather.getFl() + "℃";
        String desc1 = "降水量" + mNowWeather.getPcpn() + "mm  " + "  能见度" + mNowWeather.getVis() + "km";
        String dedc2 = "相对湿度" + mNowWeather.getHum() + "%  " + "  气压" + mNowWeather.getPres();
        items.add(wind);
        items.add(temp);
        items.add(desc1);
        items.add(dedc2);
        wind_txt.startWithList(items);
        date.setText("更新于 " + basic.getUpdate().getLoc());
        mTmp.setText(mNowWeather.getTmp() + "℃");
        current = mNowWeather.getCond().getTxt();
        code_txt.setText(current);
        Constans.WeatherBgImg citys[] = Constans.WeatherBgImg.values();
        for (Constans.WeatherBgImg cu : citys) {
            if (current.equals(cu.getName())) {
                if (cu.getimgId().length > 0) {
                    int rand = (int) Math.round(Math.random() * (cu.getimgId().length - 1));
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
        setShareWeather(mNowWeather, basic);
    }


    private void setHourWeather(List<WeatherDate.HourlyForecastBean> mHourlyforecast) {
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


    private void setOntherView(final List<WeatherDate.DailyForecastBean> mDailyforecast) {
        mDailyAdapter = new GalleryAdapter(getActivity(), mDailyforecast);
        day_weather.setAdapter(mDailyAdapter);
        mDailyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, WeatherDate.DailyForecastBean data) {
                //展示界面
                Intent intent = new Intent(getActivity(), MoreInfoActivity.class);
                intent.putExtra("city", city != null ? city : "杭州");
                intent.putExtra("county", county != null ? county : "");
                intent.putExtra("cityid", cityid != null ? city : "CN101210101");
                intent.putExtra("date", (Serializable) mDailyforecast);
                intent.putExtra("qlty", qlty.getText().toString());
                intent.putExtra("time", data.getDate());
                startActivity(intent);
            }
        });
        if (!isShowWeatherChart) {
            dailyForecast.setVisibility(View.GONE);
        } else {
            dailyForecast.setVisibility(View.VISIBLE);
            dailyForecast.setPadding(0, dp2px(getContext(), 16), 0, dp2px(getContext(), 16));
            dailyForecast.removeAllViews();
            dailyForecast.addView(getChartView(mDailyforecast));
        }
    }

    private void initView() {
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
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        sport_layout.setOnClickListener(this);
        trav_layout.setOnClickListener(this);
        drsg_layout.setOnClickListener(this);
        flu_layout.setOnClickListener(this);
        today_detail.setOnClickListener(this);
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
                    intent8.putExtra("time", mDailyforecast.get(0).getDate());
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
                    intent9.putExtra("time", mDailyforecast.get(0).getDate());
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
                    intent.putExtra("time", mDailyforecast.get(0).getDate());
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
                    intent10.putExtra("time", mDailyforecast.get(0).getDate());
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
        void onItemClick(View view, WeatherDate.DailyForecastBean data);
    }

    public class GalleryAdapter extends
            HorizontalRecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
        private List<WeatherDate.DailyForecastBean> mData = new ArrayList<WeatherDate.DailyForecastBean>();
        private LayoutInflater inflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public GalleryAdapter(Context context, List<WeatherDate.DailyForecastBean> datats) {
            inflater = LayoutInflater.from(context);
            mData = datats;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (WeatherDate.DailyForecastBean) v.getTag());
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
            WeatherDate.DailyForecastBean mDailyForecastData = mData.get(i);
            if (i == 0) {
                viewHolder.week.setText("今天");
            } else if (i == 1) {
                viewHolder.week.setText("明天");
            } else {
                viewHolder.week.setText(DateToWeek.getWeek(mDailyForecastData.getDate()));
            }
            String code = null;
            String min = mDailyForecastData.getTmp().getMin();
            String max = mDailyForecastData.getTmp().getMax();
            viewHolder.tmp.setText(min + "~" + max + "℃");
            code = mDailyForecastData.getCond().getCode_d();
            viewHolder.cond_txt.setText(mDailyForecastData.getCond().getTxt_d());
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
        private List<WeatherDate.HourlyForecastBean> mData = new ArrayList<WeatherDate.HourlyForecastBean>();

        public void notifyDataSetChanged(List<WeatherDate.HourlyForecastBean> date) {
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
            WeatherDate.HourlyForecastBean mTimeWeatherData = mData.get(i);
            viewHolder.date.setText(mTimeWeatherData.getDate().substring(10, mTimeWeatherData.getDate().length()));
            String code = null;
            String min = mTimeWeatherData.getTmp();
            viewHolder.tmp.setText(min + "℃");
            code = mTimeWeatherData.getCond().getCode();
            String txt = mTimeWeatherData.getCond().getTxt();
            viewHolder.tmp.setText(txt);
            viewHolder.decs.setText("温度" + min + "℃ " + "降水率为" + mTimeWeatherData.getPop());
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

    private void setShareWeather(WeatherDate.NowBean mNowWeather, WeatherDate.BasicBean basic) {
        StringBuffer message = new StringBuffer();
        message.append(city_name.getText().toString());
        message.append("天气：");
        message.append("\r\n");
        message.append(basic.getUpdate().getLoc());
        message.append(" 发布：");
        message.append("\r\n");
        message.append(mNowWeather.getCond().getTxt());
        message.append("，");
        message.append(mNowWeather.getTmp() + "℃");
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
        if (mSuggestion != null) {
            if (view.getId() == R.id.sport_layout) {
                title = "运动指数";
                brf = mSuggestion.getSport().getBrf();
                tex = mSuggestion.getSport().getTxt();
            }
            if (view.getId() == R.id.trav_layout) {
                title = "旅游指数";
                brf = mSuggestion.getTrav().getBrf();
                tex = mSuggestion.getTrav().getTxt();
            }
            if (view.getId() == R.id.flu_layout) {
                title = "感冒指数";
                brf = mSuggestion.getFlu().getBrf();
                tex = mSuggestion.getFlu().getTxt();
            }
            if (view.getId() == R.id.drsg_layout) {
                title = "穿衣指数";
                brf = mSuggestion.getDrsg().getBrf();
                tex = mSuggestion.getDrsg().getTxt();
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

    private WeatherChartView getChartView(List<WeatherDate.DailyForecastBean> mDailyforecast) {
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
