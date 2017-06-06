package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.AqiActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MoreInfoActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.TipActivity;
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
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Zodiac;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.service.ZodiacService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.DateToWeek;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HorizontalRecyclerView;
import com.example.administrator.soweather.com.example.administrator.soweather.view.MarqueeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.base.BaseSkinFragment;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends BaseSkinFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
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

    private ImageView wind_img;
    private MarqueeView wind_txt;
    private List<String> items = new ArrayList<>();
    private String mDate;
    private Zodiac mZodiac = new Zodiac();
    private TextView ji;
    private TextView yi;
    private ImageView aqi_img;
    private LinearLayout today_detail;
    private Bitmap bmp = null;
    private SwipeRefreshLayout mSwipeLayout;
    private ImageView tip;
    private int flag = 0;
    private String current;

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
    }

    private void getDate() {
        config.showProgressDialog("正在加载...", getActivity());
        getDailyforecastData(cityid);
        getHourlyforecastData(cityid);
        getNowWeatherData(cityid);
        getWeather(cityid);
    }

    private void getZodiac(String date) {
        ZodiacService mZodiacService = new ZodiacService();
        mZodiacService.getZodiacService(new ResponseListenter<Zodiac>() {
            @Override
            public void onReceive(Result<Zodiac> result) {
                if (result.isSuccess()) {
                    config.dismissProgressDialog();
                    mZodiac = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(4, mZodiac));
                } else {
                    config.dismissProgressDialog();
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, date);
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
                        break;
                    case 4:
                        setZodiac(mZodiac);
                }
            }
        };
    }

    private void setZodiac(Zodiac mZodiac) {
        yi.setText(mZodiac.yi);
        ji.setText(mZodiac.ji);
        visitityzZodiac();
    }

    private void setAqi(Aqi mAqi) {
        pm.setText(mAqi.aqi);
        if (mAqi.qlty.equals("优")) {
            aqi_img.setImageResource(R.mipmap.aqi_1);
            pm.setTextColor(getResources().getColor(R.color.gred));
            qlty.setTextColor(getResources().getColor(R.color.gred));

        } else if (mAqi.qlty.equals("良")) {
            aqi_img.setImageResource(R.mipmap.aqi_2);
            pm.setTextColor(getResources().getColor(R.color.yellow));
            qlty.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            aqi_img.setImageResource(R.mipmap.aqi_3);
            pm.setTextColor(getResources().getColor(R.color.red));
            qlty.setTextColor(getResources().getColor(R.color.red));
        }
        qlty.setText(mAqi.qlty);
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
            if (bmp != null) {
                bmp.recycle();
            }
            for (Constans.WeatherBgImg cu : citys) {
                if (txt.equals(cu.getName())) {
                    if (cu.getimgId().length > 0) {
                        int rand = (int) Math.round(Math.random() * (cu.getimgId().length - 1));
                        bmp = BitmapFactory.decodeResource(this.getResources(), cu.getimgId()[rand]);
                        flag = rand;
                        bg.setImageBitmap(bmp);
                    } else {
                        bmp = BitmapFactory.decodeResource(this.getResources(), cu.getimgId()[0]);
                        bg.setImageBitmap(bmp);
                    }
                    break;
                }
            }
            visitityToday();
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
        getZodiac(mDate);
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
                    intent.putExtra("cityid", cityid != null ? city : "CN101210101");
                    intent.putExtra("date", (Serializable) mDailyforecast);
                    intent.putExtra("qlty", qlty.getText().toString());
                    intent.putExtra("time", data.date);
                    startActivity(intent);
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
                    animation.setFillAfter(true);
                    view.startAnimation(animation);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();//异常
        }
    }

    private void initView(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小

        tip = (ImageView) view.findViewById(R.id.tip);
        tip.setOnClickListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = tada(tip);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.start();

//                ObjectAnimator nopeAnimator = nope(tip);
//                nopeAnimator.setRepeatCount(ValueAnimator.INFINITE);
//                nopeAnimator.start();
            }
        }, 2000);

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
        day_weather = (HorizontalRecyclerView) view.findViewById(R.id.day_weather);
        aqi_img = (ImageView) view.findViewById(R.id.aqi_img);
        aqi = (LinearLayout) view.findViewById(R.id.aqi);
        bg = (ImageView) view.findViewById(R.id.bg);
        yi = (TextView) view.findViewById(R.id.yi);
        ji = (TextView) view.findViewById(R.id.ji);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        day_weather.setLayoutManager(linearLayoutManager);
        life.setOnClickListener(this);
        aqi.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life:
                toActivty();
                break;
            case R.id.aqi:
                //弹出所在地当天的生活指数和相关信息
                toActivty();
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
                toActivty();
                break;
            case R.id.tip:
                Intent intent2 = new Intent(getActivity(), TipActivity.class);
                if (mHourlyforecast != null && mHourlyforecast.size() > 0) {
                    intent2.putExtra("city", city != null ? city : "杭州");
                    intent2.putExtra("cityid", cityid != null ? city : "CN101210101");
                }
                intent2.putExtra("flag", flag);
                intent2.putExtra("txt", current);
                startActivity(intent2);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
        }
    }

    private void toActivty() {
        Intent intent1 = new Intent();
        intent1.setClass(getActivity(), AqiActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("city", city != null ? city : "杭州");
        bundle1.putString("date", date.getText().toString());
        bundle1.putString("cityid", cityid != null ? city : "CN101210101");
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
            TextView date;
            TextView tmp;
            TextView cond_txt;
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
            viewHolder.cond_txt = (TextView) view.findViewById(R.id.cond_txt);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
            animation.setFillAfter(true);
            view.startAnimation(animation);
            view.setOnClickListener(this);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.itemView.setTag(mData.get(i));
            Dailyforecast mDailyForecastData = mData.get(i);
            viewHolder.date.setText(DateToWeek.getWeek(mDailyForecastData.date) + " (" +
                    mDailyForecastData.date.substring(5, mDailyForecastData.date.length()) + ")");
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
            viewHolder.itemView.setTag(mDailyForecastData);
            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
            animation.setFillAfter(true);
            viewHolder.itemView.startAnimation(animation);
            viewHolder.itemView.setOnClickListener(this);
        }
    }

    private void visitityToday() {
        TranslateAnimation down = new TranslateAnimation(0, 0, -300, 0);//位移动画，从button的上方300像素位置开始
        down.setFillAfter(true);
        down.setInterpolator(new BounceInterpolator());//弹跳动画,要其它效果的当然也可以设置为其它的值
        down.setDuration(2000);//持续时间
        date.startAnimation(down);
        today_detail.startAnimation(down);
        aqi.startAnimation(down);
        life.startAnimation(down);
    }


    private void visitityAqi() {
        TranslateAnimation down = new TranslateAnimation(0, 0, -300, 0);//位移动画，从button的上方300像素位置开始
        down.setFillAfter(true);
        down.setInterpolator(new BounceInterpolator());//弹跳动画,要其它效果的当然也可以设置为其它的值
        down.setDuration(2000);//持续时间
        aqi.startAnimation(down);
    }

    private void visitityzZodiac() {
        TranslateAnimation down = new TranslateAnimation(0, 0, -300, 0);//位移动画，从button的上方300像素位置开始
        down.setFillAfter(true);
        down.setInterpolator(new BounceInterpolator());//弹跳动画,要其它效果的当然也可以设置为其它的值
        down.setDuration(2000);//持续时间
        linearLayout2.startAnimation(down);
    }


    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

//    public static ObjectAnimator nope(View view) {
//        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);
//
//        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
//                Keyframe.ofFloat(0f, 0),
//                Keyframe.ofFloat(.10f, -delta),
//                Keyframe.ofFloat(.26f, delta),
//                Keyframe.ofFloat(.42f, -delta),
//                Keyframe.ofFloat(.58f, delta),
//                Keyframe.ofFloat(.74f, -delta),
//                Keyframe.ofFloat(.90f, delta),
//                Keyframe.ofFloat(1f, 0f)
//        );
//
//        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
//                setDuration(500);
//    }

}
