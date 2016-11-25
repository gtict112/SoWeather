package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.AqiActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.LifeActivity;
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
import com.example.administrator.soweather.com.example.administrator.soweather.view.WeatherChartView;

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
    private HorizontalRecyclerView day_weather;
    private GalleryAdapter mDailyAdapter;
    private Aqi mAqi = new Aqi();
    private LinearLayout aqi;
    private ImageView up;
    private FrameLayout frame_layout;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private DisplayMetrics dm = new DisplayMetrics();
    private FrameLayout frame_layout2;//弹出曲线图 详情
    private WeatherChartView line_char;//曲线图
    private ImageView bg;
    private ImageView down;
    private TextView day_1;
    private TextView day_2;
    private TextView day_3;
    private TextView day_4;
    private TextView day_5;
    private TextView day_6;

    private TextView wether_1;
    private TextView wether_2;
    private TextView wether_3;
    private TextView wether_4;
    private TextView wether_5;
    private TextView wether_6;


    private ImageView day_img_1;
    private ImageView day_img_2;
    private ImageView day_img_3;
    private ImageView day_img_4;
    private ImageView day_img_5;
    private ImageView day_img_6;

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
            tem_min_max.setText(min + "℃" + "~" + max + "℃");
            mDailyAdapter = new GalleryAdapter(getActivity(), mDailyforecast);
            day_weather.setAdapter(mDailyAdapter);
            int[] tempDay = new int[mDailyforecast.size()];

            int[] tempNight = new int[mDailyforecast.size()];
            for (int i = 0; i < mDailyforecast.size(); i++) {
                tempDay[i] = Integer.parseInt(new JSONObject(mDailyforecast.get(i).tmp).optString("max"));
                tempNight[i] = Integer.parseInt(new JSONObject(mDailyforecast.get(i).tmp).optString("min"));
                String code_n = new JSONObject(mDailyforecast.get(i).cond).optString("code_n");//夜间
                weathimgs = cityDB.getAllWeatherImg();
                if (i > 0) {
                    String code_d = new JSONObject(mDailyforecast.get(i).cond).optString("code_d");//白天
                    for (int j = 0; j < weathimgs.size(); j++) {
                        if (code_d.equals(weathimgs.get(j).getCode())) {
                            if (i == 1) {
                                day_img_1.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                            if (i == 2) {
                                day_img_2.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                            if (i == 3) {
                                day_img_3.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                            if (i == 4) {
                                day_img_4.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                            if (i == 5) {
                                day_img_5.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                            if (i == 6) {
                                day_img_6.setImageBitmap(weathimgs.get(j).getIcon());
                            }
                        }
                    }
                }
            }

            if (mDailyforecast != null && mDailyforecast.size() > 0) {
                day_1.setText(mDailyforecast.get(1).date.substring(5, mDailyforecast.get(1).date.length()) + "明");
                day_2.setText(mDailyforecast.get(2).date.substring(5, mDailyforecast.get(1).date.length()));
                day_3.setText(mDailyforecast.get(3).date.substring(5, mDailyforecast.get(1).date.length()));
                day_4.setText(mDailyforecast.get(4).date.substring(5, mDailyforecast.get(1).date.length()));
                day_5.setText(mDailyforecast.get(5).date.substring(5, mDailyforecast.get(1).date.length()));
                day_6.setText(mDailyforecast.get(6).date.substring(5, mDailyforecast.get(1).date.length()));

                wether_1.setText(new JSONObject(mDailyforecast.get(1).cond).optString("txt_d"));
                wether_2.setText(new JSONObject(mDailyforecast.get(2).cond).optString("txt_d"));
                wether_3.setText(new JSONObject(mDailyforecast.get(3).cond).optString("txt_d"));
                wether_4.setText(new JSONObject(mDailyforecast.get(4).cond).optString("txt_d"));
                wether_5.setText(new JSONObject(mDailyforecast.get(5).cond).optString("txt_d"));
                wether_6.setText(new JSONObject(mDailyforecast.get(6).cond).optString("txt_d"));
            }
            // 设置当天最高温度曲线
            line_char.setTempDay(tempDay);
            // 设置当天最低温度曲线
            line_char.setTempNight(tempNight);
            line_char.invalidate();


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
        up = (ImageView) view.findViewById(R.id.up);
        frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        linearLayout2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
        day_weather = (HorizontalRecyclerView) view.findViewById(R.id.day_weather);
        aqi = (LinearLayout) view.findViewById(R.id.aqi);
        frame_layout2 = (FrameLayout) view.findViewById(R.id.frame_layout2);
        line_char = (WeatherChartView) view.findViewById(R.id.line_char);
        down = (ImageView) view.findViewById(R.id.down);
        bg = (ImageView) view.findViewById(R.id.bg);
        day_1 = (TextView) view.findViewById(R.id.day_1);
        day_2 = (TextView) view.findViewById(R.id.day_2);
        day_3 = (TextView) view.findViewById(R.id.day_3);
        day_4 = (TextView) view.findViewById(R.id.day_4);
        day_5 = (TextView) view.findViewById(R.id.day_5);
        day_6 = (TextView) view.findViewById(R.id.day_6);
        wether_1 = (TextView) view.findViewById(R.id.weather_1);
        wether_2 = (TextView) view.findViewById(R.id.weather_2);
        wether_3 = (TextView) view.findViewById(R.id.weather_3);
        wether_4 = (TextView) view.findViewById(R.id.weather_4);
        wether_5 = (TextView) view.findViewById(R.id.weather_5);
        wether_6 = (TextView) view.findViewById(R.id.weather_6);

        day_img_1 = (ImageView) view.findViewById(R.id.day_img_1);
        day_img_2 = (ImageView) view.findViewById(R.id.day_img_2);
        day_img_3 = (ImageView) view.findViewById(R.id.day_img_3);
        day_img_4 = (ImageView) view.findViewById(R.id.day_img_4);
        day_img_5 = (ImageView) view.findViewById(R.id.day_img_5);
        day_img_6 = (ImageView) view.findViewById(R.id.day_img_6);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        day_weather.setLayoutManager(linearLayoutManager);
        life.setOnClickListener(this);
        aqi.setOnClickListener(this);
        up.setOnClickListener(this);
        down.setOnClickListener(this);
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
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
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
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.up:
                //弹出趋势图布局
                frame_layout.setVisibility(View.GONE);
                frame_layout2.setVisibility(View.VISIBLE);
                Animation anim1 = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_out);
                frame_layout.startAnimation(anim1);
                Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
                frame_layout2.startAnimation(anim2);
                break;
            case R.id.linearLayout1:
                //弹出今日小时预报
                TimeDialogFragment f = new TimeDialogFragment();
                Bundle bundle2 = new Bundle();
                if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                    bundle2.putSerializable("date", (Serializable) mHourlyforecast);
                }
                f.setArguments(bundle2);
                f.show(getChildFragmentManager(), "小时预报");
                break;
            case R.id.linearLayout2:
                //弹出今日小时预报
                TimeDialogFragment f1 = new TimeDialogFragment();
                Bundle bundle3 = new Bundle();
                if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                    bundle3.putSerializable("date", (Serializable) mHourlyforecast);
                }
                f1.setArguments(bundle3);
                f1.show(getFragmentManager(), "小时预报");
                break;
            case R.id.down:
                frame_layout.setVisibility(View.VISIBLE);
                frame_layout2.setVisibility(View.GONE);
                Animation anim3 = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_out);
                frame_layout2.startAnimation(anim3);
                Animation anim4 = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
                frame_layout.startAnimation(anim4);
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
                viewHolder.date.setTextColor(getResources().getColor(R.color.red));
            } else if (i == 1) {
                viewHolder.date.setText(mDailyForecastData.date.substring(5, mDailyForecastData.date.length()) + "(明)");
                viewHolder.date.setTextColor(getResources().getColor(R.color.red));
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
}
