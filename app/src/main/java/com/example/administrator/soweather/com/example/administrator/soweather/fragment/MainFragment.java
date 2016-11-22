package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.AqiActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.LifeActivity;
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
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HorizontalRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView date;//更新时间
    private TextView mTmp;//温度
    private TextView code_txt;//天气描述
    private TextView tem_min_max;//最高温度和最低温度
    private TextView pm;//pm2.5
    private TextView qlty;//空气质量描述
    private TextView date2;//时间
    private ImageView code_img;//天气图标
    private TextView code_txt2;//天气描述
    private LinearLayout life;
    private String cityid;
    private String city;
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
    private FrameLayout bg_img;
    private HorizontalRecyclerView day_weather;
    private GalleryAdapter mDailyAdapter;
    private Aqi mAqi = new Aqi();
    private LinearLayout aqi;
    private ImageView up;
    private FrameLayout frame_layout;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private FrameLayout tip;
    private Boolean istip = false;
    private HorizontalRecyclerView time_weather;
    private TimeAdapter mTimeAdapter;

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
        getWeather(cityid);
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
                    config.dismissProgressDialog();
                    mAqi = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mNowWeather));
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
                    config.dismissProgressDialog();
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
                    config.dismissProgressDialog();
                    mHourlyforecast = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(4, mHourlyforecast));
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
                    config.dismissProgressDialog();
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
                        setAqi(mAqi);
                        break;
                    case 2:
                        setNowWeatherView(mNowWeather);
                        break;
                    case 3:
                        setOntherView(mDailyforecast);
                    case 4:
                        setHourWeatherView(mHourlyforecast);
                        break;
                }
            }
        };
    }

    private void setHourWeatherView(List<Hourlyforecast> mHourlyforecast) {
        mTimeAdapter = new TimeAdapter(getActivity(), mHourlyforecast);
        time_weather.setAdapter(mTimeAdapter);
    }

    private void setAqi(Aqi mAqi) {
        pm.setText(mAqi.aqi);
        qlty.setText(mAqi.qlty);
    }

    private void setNowWeatherView(NowWeather mNowWeather) {
        try {
            date.setText(new JSONObject(mNowWeather.update).optString("loc"));
            date2.setText(new JSONObject(mNowWeather.update).optString("loc").substring(5, 10));
            mTmp.setText(mNowWeather.tmp + "℃");
            String code = new JSONObject(mNowWeather.cond).optString("code");
            String txt = new JSONObject(mNowWeather.cond).optString("txt");
            code_txt.setText(txt);
            code_txt2.setText(txt);
            weathimgs = cityDB.getAllWeatherImg();
            for (int i = 0; i < weathimgs.size(); i++) {
                if (code.equals(weathimgs.get(i).getCode())) {
                    code_img.setImageBitmap(weathimgs.get(i).getIcon());
                }
            }
            dir = mNowWeather.dir;
            sc = mNowWeather.sc;
            fl = mNowWeather.fl;
            hum = mNowWeather.hum;
            pcpn = mNowWeather.pcpn;
            pres = mNowWeather.pres;
            vis = mNowWeather.vis;
            Constans.WeatherBgImg citys[] = Constans.WeatherBgImg.values();
            for (Constans.WeatherBgImg cu : citys) {
                if (cu.getName().contains(txt)) {
                    bg_img.setBackgroundResource(cu.getimgId());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }

    private void setOntherView(List<Dailyforecast> mDailyforecast) {
        try {
            String max = new JSONObject(mDailyforecast.get(0).tmp).optString("max");
            String min = new JSONObject(mDailyforecast.get(0).tmp).optString("min");
            tem_min_max.setText(min + "℃" + "~" + max + "℃");
            mDailyAdapter = new GalleryAdapter(getActivity(), mDailyforecast);
            day_weather.setAdapter(mDailyAdapter);
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }


    private void initView(View view) {
        date = (TextView) view.findViewById(R.id.date);
        code_img = (ImageView) view.findViewById(R.id.code_img);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        code_txt = (TextView) view.findViewById(R.id.code_txt);
        tem_min_max = (TextView) view.findViewById(R.id.tem_min_max);
        pm = (TextView) view.findViewById(R.id.pm);
        qlty = (TextView) view.findViewById(R.id.qlty);
        date2 = (TextView) view.findViewById(R.id.date2);
        code_txt2 = (TextView) view.findViewById(R.id.code_txt2);
        life = (LinearLayout) view.findViewById(R.id.life);
        bg_img = (FrameLayout) view.findViewById(R.id.bg_img);
        day_weather = (HorizontalRecyclerView) view.findViewById(R.id.day_weather);
        time_weather = (HorizontalRecyclerView) view.findViewById(R.id.time_weather);
        up = (ImageView) view.findViewById(R.id.up);
        frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        tip = (FrameLayout) view.findViewById(R.id.tip);
        aqi = (LinearLayout) view.findViewById(R.id.aqi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        day_weather.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        time_weather.setLayoutManager(linearLayoutManager1);
        life.setOnClickListener(this);
        aqi.setOnClickListener(this);
        up.setOnClickListener(this);
        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life:
                Intent intent3 = new Intent();
                intent3.setClass(getActivity(), LifeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("dir", dir);
                bundle.putString("sc", sc);
                bundle.putString("fl", fl);
                bundle.putString("hum", hum);
                bundle.putString("pcpn", pcpn);
                bundle.putString("pres", pres);
                bundle.putString("vis", vis);
                bundle.putString("tem_max_min", tem_min_max.getText().toString());
                bundle.putString("cityid", cityid);
                bundle.putString("city", city);
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    bundle.putString("cond", mDailyforecast.get(0).cond);
                }
                intent3.putExtras(bundle);
                getActivity().startActivity(intent3);
                break;
            case R.id.aqi:
                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), AqiActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("city", city);
                bundle1.putString("date", date.getText().toString());
                bundle1.putString("cityid", cityid);
                if (mAqi != null) {
                    bundle1.putString("aqi", mAqi.aqi);
                    bundle1.putString("co", mAqi.co);
                    bundle1.putString("no2", mAqi.no2);
                    bundle1.putString("o3", mAqi.o3);
                    bundle1.putString("pm10", mAqi.pm10);
                    bundle1.putString("pm25", mAqi.pm25);
                    bundle1.putString("qlty", mAqi.qlty);
                    bundle1.putString("so2", mAqi.so2);
                }
                intent1.putExtras(bundle1);
                getActivity().startActivity(intent1);
                break;
            case R.id.up:
                //弹出趋势图布局
                frame_layout.setVisibility(View.GONE);
                break;
            case R.id.linearLayout1:
                //弹出今日小时预报
                if (!istip) {
                    istip = true;
                    tip.setVisibility(View.VISIBLE);
                } else {
                    istip = false;
                    tip.setVisibility(View.GONE);
                }
                break;
            case R.id.linearLayout2:
                //弹出今日小时预报
                if (!istip) {
                    istip = true;
                    tip.setVisibility(View.VISIBLE);
                } else {
                    istip = false;
                    tip.setVisibility(View.GONE);
                }

                break;
        }
    }

    public class GalleryAdapter extends
            HorizontalRecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private List<Dailyforecast> mData = new ArrayList<Dailyforecast>();
        private LayoutInflater inflater;

        public GalleryAdapter(Context context, List<Dailyforecast> datats) {
            inflater = LayoutInflater.from(context);
            mData = datats;
        }

        public class ViewHolder extends HorizontalRecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView txt_img;
            TextView date;
            TextView tmp;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.item_daily_forecst,
                    viewGroup, false);
            GalleryAdapter.ViewHolder viewHolder = new GalleryAdapter.ViewHolder(view);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.txt_img = (ImageView) view.findViewById(R.id.txt_img);
            viewHolder.tmp = (TextView) view.findViewById(R.id.tmp);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder viewHolder, final int i) {
            Dailyforecast mDailyForecastData = mData.get(i);
            if (i == 0) {
                viewHolder.date.setText(mDailyForecastData.date.substring(5, mDailyForecastData.date.length()) + "(今)");
                viewHolder.date.setTextColor(getResources().getColor(R.color.gred));
            } else if (i == 1) {
                viewHolder.date.setText(mDailyForecastData.date.substring(5, mDailyForecastData.date.length()) + "(明)");
                viewHolder.date.setTextColor(getResources().getColor(R.color.gred));
            } else {
                viewHolder.date.setText(mDailyForecastData.date.substring(5, mDailyForecastData.date.length()));
            }
            String code = null;
            try {
                String min = new JSONObject(mDailyForecastData.tmp).optString("min");
                String max = new JSONObject(mDailyForecastData.tmp).optString("max");
                viewHolder.tmp.setText(min + "~" + max + "℃");
                code = new JSONObject(mDailyForecastData.cond).optString("code_d");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    viewHolder.txt_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
        }
    }

    public class TimeAdapter extends
            HorizontalRecyclerView.Adapter<TimeAdapter.ViewHolder> {
        private List<Hourlyforecast> mData = new ArrayList<Hourlyforecast>();
        private LayoutInflater inflater;

        public TimeAdapter(Context context, List<Hourlyforecast> datats) {
            inflater = LayoutInflater.from(context);
            mData = datats;
        }

        public class ViewHolder extends HorizontalRecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView txt_img;
            TextView date;
            TextView tmp;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.item_daily_forecst,
                    viewGroup, false);
            TimeAdapter.ViewHolder viewHolder = new TimeAdapter.ViewHolder(view);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.txt_img = (ImageView) view.findViewById(R.id.txt_img);
            viewHolder.tmp = (TextView) view.findViewById(R.id.tmp);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final TimeAdapter.ViewHolder viewHolder, final int i) {
            Hourlyforecast mTimeWeatherData = mData.get(i);
            viewHolder.date.setText(mTimeWeatherData.date.substring(10, mTimeWeatherData.date.length()));
            String code = null;
            try {
                String min = mTimeWeatherData.tmp;
                viewHolder.tmp.setText(min);
                code = new JSONObject(mTimeWeatherData.cond).optString("code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    viewHolder.txt_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
        }
    }
}
