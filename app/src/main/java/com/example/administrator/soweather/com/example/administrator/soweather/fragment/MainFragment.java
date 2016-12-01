package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.AqiActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.TodayDetailActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.general.TimeDialogFragment;
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

import java.io.Serializable;
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
    private String tem_min_max;//最高温度和最低温度
    private TextView pm;//pm2.5
    private TextView qlty;//空气质量描述
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
    private HorizontalRecyclerView day_weather;
    private GalleryAdapter mDailyAdapter;
    private Aqi mAqi = new Aqi();
    private LinearLayout aqi;
    private LinearLayout linearLayout2;
    private DisplayMetrics dm = new DisplayMetrics();
    private ImageView bg;

    private LinearLayout today_detai;
    private ImageView wind_img;
    private TextView wind_txt;

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
                }
            }
        };
    }

    private void setAqi(Aqi mAqi) {
        pm.setText(mAqi.aqi);
        qlty.setText(mAqi.qlty);
    }

    private void setNowWeatherView(NowWeather mNowWeather) {
        try {
            Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.wind);
            wind_img.startAnimation(anim1);
            wind_txt.setText(mNowWeather.dir + mNowWeather.sc + "级");
            date.setText(new JSONObject(mNowWeather.update).optString("loc"));
            mTmp.setText(mNowWeather.tmp + "℃");
            String txt = new JSONObject(mNowWeather.cond).optString("txt");
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
                    bg.setImageResource(cu.getimgId());
                    break;
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
            tem_min_max = min + "℃" + "~" + max + "℃";
            mDailyAdapter = new GalleryAdapter(getActivity(), mDailyforecast);
            day_weather.setAdapter(mDailyAdapter);
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }


    private void initView(View view) {
        wind_img = (ImageView) view.findViewById(R.id.wind_img);
        wind_txt = (TextView) view.findViewById(R.id.wind_txt);
        date = (TextView) view.findViewById(R.id.date);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        code_txt = (TextView) view.findViewById(R.id.code_txt);
        pm = (TextView) view.findViewById(R.id.pm);
        qlty = (TextView) view.findViewById(R.id.qlty);
        life = (LinearLayout) view.findViewById(R.id.life);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        day_weather = (HorizontalRecyclerView) view.findViewById(R.id.day_weather);
        aqi = (LinearLayout) view.findViewById(R.id.aqi);
        bg = (ImageView) view.findViewById(R.id.bg);
        today_detai = (LinearLayout) view.findViewById(R.id.today_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        day_weather.setLayoutManager(linearLayoutManager);
        life.setOnClickListener(this);
        aqi.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        today_detai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life:
                //弹出今日小时预报
                TimeDialogFragment f1 = new TimeDialogFragment();
                if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putSerializable("date", (Serializable) mHourlyforecast);
                    f1.setArguments(bundle3);
                }
                f1.show(getFragmentManager(), "小时预报");
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
                bundle1.putString("dir", dir);
                bundle1.putString("sc", sc);
                bundle1.putString("fl", fl);
                bundle1.putString("hum", hum);
                bundle1.putString("pcpn", pcpn);
                bundle1.putString("pres", pres);
                bundle1.putString("vis", vis);
                bundle1.putString("tem_max_min", tem_min_max);
                if (mDailyforecast != null && mDailyforecast.size() > 0) {
                    bundle1.putString("cond", mDailyforecast.get(0).cond);
                }
                if (mNowWeather != null) {
                    bundle1.putString("lat", mNowWeather.lat);
                    bundle1.putString("lon", mNowWeather.lon);
                }
                intent1.putExtras(bundle1);
                getActivity().startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.today_detail:
                Intent intent = new Intent(getActivity(), TodayDetailActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
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
            viewHolder.date.setText(mDailyForecastData.date.substring(5, mDailyForecastData.date.length()));
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
}
