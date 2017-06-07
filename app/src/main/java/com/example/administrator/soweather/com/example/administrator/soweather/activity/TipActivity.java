package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.WeatherChartView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class TipActivity extends BaseActivity implements View.OnClickListener {
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();
    private String city;
    private String cityid;
    private TextView topTv;
    private ImageView topButton;
    private TimeAdapter mTimeAdapter;
    private RecyclerView time_weather;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private Handler mHandler;
    private ImageView bg;
    /**
     * 天气相关视图
     */
    private TextView date1;
    private TextView dir1;
    private TextView sc1;
    private TextView cond_txt1;
    private ImageView cond_img1;

    private TextView date2;
    private TextView dir2;
    private TextView sc2;
    private TextView cond_txt2;
    private ImageView cond_img2;

    private TextView date3;
    private TextView dir3;
    private TextView sc3;
    private TextView cond_txt3;
    private ImageView cond_img3;

    private TextView date4;
    private TextView dir4;
    private TextView sc4;
    private TextView cond_txt4;
    private ImageView cond_img4;

    private TextView date5;
    private TextView dir5;
    private TextView sc5;
    private TextView cond_txt5;
    private ImageView cond_img5;

    private TextView date6;
    private TextView dir6;
    private TextView sc6;
    private TextView cond_txt6;
    private ImageView cond_img6;

    private TextView date7;
    private TextView dir7;
    private TextView sc7;
    private TextView cond_txt7;
    private ImageView cond_img7;

    private WeatherChartView line_chart;

    private TextView night_cond_txt1;
    private ImageView night_cond_img1;

    private TextView night_cond_txt2;
    private ImageView night_cond_img2;

    private TextView night_cond_txt3;
    private ImageView night_cond_img3;

    private TextView night_cond_txt4;
    private ImageView night_cond_img4;

    private TextView night_cond_txt5;
    private ImageView night_cond_img5;

    private TextView night_cond_txt6;
    private ImageView night_cond_img6;

    private TextView night_cond_txt7;
    private ImageView night_cond_img7;


    private int[] mDayLineChart;//白天曲线图数组
    private int[] mNightLineChart;//夜间曲线图数组
    private List<Bitmap> mDayBitmap = new ArrayList<>();
    private List<Bitmap> mNightBitmap = new ArrayList<>();

    private ImageView hour_rotate;
    private ImageView date_rotate;
    private Boolean isUnfoldHour = false;
    private Boolean isUnfolddate = true;
    private LinearLayout date_linear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dialog_tip);
        cityDB = SoWeatherDB.getInstance(this);
        weathimgs = cityDB.getAllWeatherImg();
        init();
        getDate();
        getHandleMessge();
    }

    private void getDate() {
        getHourDate();
        getDailyDate();
    }

    private void getHourDate() {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getHourlyforecastData(new ResponseListenter<List<Hourlyforecast>>() {
            @Override
            public void onReceive(Result<List<Hourlyforecast>> result) {
                if (result.isSuccess()) {
                    mHourlyforecast = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(2, mHourlyforecast));
                } else {
                    Toast.makeText(TipActivity.this, "获取未来未来时段天气预报失败..", Toast.LENGTH_LONG).show();
                }
            }
        }, cityid);
    }

    private void getDailyDate() {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getDailyforecastData(new ResponseListenter<List<Dailyforecast>>() {
            @Override
            public void onReceive(Result<List<Dailyforecast>> result) {
                if (result.isSuccess()) {
                    mDailyforecast = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mDailyforecast));
                } else {
                    Toast.makeText(TipActivity.this, "获取未来7天天气预报失败..", Toast.LENGTH_LONG).show();
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
                        setDailyDate(mDailyforecast);
                        break;
                    case 2:
                        setHourDate(mHourlyforecast);

                }
            }
        };
    }

    private void setDailyDate(List<Dailyforecast> mDailyforecast) {
        mDayLineChart = new int[mDailyforecast.size()];
        mNightLineChart = new int[mDailyforecast.size()];
        for (int i = 0; i < mDailyforecast.size(); i++) {
            try {
                mDayLineChart[i] = Integer.parseInt(new JSONObject(mDailyforecast.get(i).tmp).optString("max"));
                mNightLineChart[i] = Integer.parseInt(new JSONObject(mDailyforecast.get(i).tmp).optString("min"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.dialog_in);
        animation.setFillAfter(true);
        line_chart.startAnimation(animation);
        line_chart.setTempDay(mDayLineChart);
        line_chart.setTempNight(mNightLineChart);
        line_chart.invalidate();
        date1.setText(mDailyforecast.get(0).date.substring(5, mDailyforecast.get(0).date.length()));
        date2.setText(mDailyforecast.get(1).date.substring(5, mDailyforecast.get(1).date.length()));
        date3.setText(mDailyforecast.get(2).date.substring(5, mDailyforecast.get(2).date.length()));
        date4.setText(mDailyforecast.get(3).date.substring(5, mDailyforecast.get(3).date.length()));
        date5.setText(mDailyforecast.get(4).date.substring(5, mDailyforecast.get(4).date.length()));
        date6.setText(mDailyforecast.get(5).date.substring(5, mDailyforecast.get(5).date.length()));
        date7.setText(mDailyforecast.get(6).date.substring(5, mDailyforecast.get(6).date.length()));
        try {
            dir1.setText(new JSONObject(mDailyforecast.get(0).wind).optString("dir"));
            dir2.setText(new JSONObject(mDailyforecast.get(1).wind).optString("dir"));
            dir3.setText(new JSONObject(mDailyforecast.get(2).wind).optString("dir"));
            dir4.setText(new JSONObject(mDailyforecast.get(3).wind).optString("dir"));
            dir5.setText(new JSONObject(mDailyforecast.get(4).wind).optString("dir"));
            dir6.setText(new JSONObject(mDailyforecast.get(5).wind).optString("dir"));
            dir7.setText(new JSONObject(mDailyforecast.get(6).wind).optString("dir"));

            sc1.setText(new JSONObject(mDailyforecast.get(0).wind).optString("sc"));
            sc2.setText(new JSONObject(mDailyforecast.get(1).wind).optString("sc"));
            sc3.setText(new JSONObject(mDailyforecast.get(2).wind).optString("sc"));
            sc4.setText(new JSONObject(mDailyforecast.get(3).wind).optString("sc"));
            sc5.setText(new JSONObject(mDailyforecast.get(4).wind).optString("sc"));
            sc6.setText(new JSONObject(mDailyforecast.get(5).wind).optString("sc"));
            sc7.setText(new JSONObject(mDailyforecast.get(6).wind).optString("sc"));

            cond_txt1.setText("(日)" + new JSONObject(mDailyforecast.get(0).cond).optString("txt_d"));
            cond_txt2.setText(new JSONObject(mDailyforecast.get(1).cond).optString("txt_d"));
            cond_txt3.setText(new JSONObject(mDailyforecast.get(2).cond).optString("txt_d"));
            cond_txt4.setText(new JSONObject(mDailyforecast.get(3).cond).optString("txt_d"));
            cond_txt5.setText(new JSONObject(mDailyforecast.get(4).cond).optString("txt_d"));
            cond_txt6.setText(new JSONObject(mDailyforecast.get(5).cond).optString("txt_d"));
            cond_txt7.setText(new JSONObject(mDailyforecast.get(6).cond).optString("txt_d"));

            night_cond_txt1.setText("(夜)" + new JSONObject(mDailyforecast.get(0).cond).optString("txt_n"));
            night_cond_txt2.setText(new JSONObject(mDailyforecast.get(1).cond).optString("txt_n"));
            night_cond_txt3.setText(new JSONObject(mDailyforecast.get(2).cond).optString("txt_n"));
            night_cond_txt4.setText(new JSONObject(mDailyforecast.get(3).cond).optString("txt_n"));
            night_cond_txt5.setText(new JSONObject(mDailyforecast.get(4).cond).optString("txt_n"));
            night_cond_txt6.setText(new JSONObject(mDailyforecast.get(5).cond).optString("txt_n"));
            night_cond_txt7.setText(new JSONObject(mDailyforecast.get(6).cond).optString("txt_n"));

            for (int i = 0; i < mDailyforecast.size(); i++) {
                String code_d = new JSONObject(mDailyforecast.get(i).cond).optString("code_d");
                String code_n = new JSONObject(mDailyforecast.get(i).cond).optString("code_n");
                for (int j = 0; j < weathimgs.size(); j++) {
                    if (code_d.equals(weathimgs.get(j).getCode())) {
                        mDayBitmap.add(weathimgs.get(j).getIcon());
                    }
                    if (code_n.equals(weathimgs.get(j).getCode())) {
                        mNightBitmap.add(weathimgs.get(j).getIcon());
                    }
                }
            }
            if (mDayBitmap != null && mDayBitmap.size() > 6) {
                cond_img1.setImageBitmap(mDayBitmap.get(0));
                cond_img2.setImageBitmap(mDayBitmap.get(1));
                cond_img3.setImageBitmap(mDayBitmap.get(2));
                cond_img4.setImageBitmap(mDayBitmap.get(3));
                cond_img5.setImageBitmap(mDayBitmap.get(4));
                cond_img6.setImageBitmap(mDayBitmap.get(5));
                cond_img7.setImageBitmap(mDayBitmap.get(6));
            }
            if (mNightBitmap != null && mNightBitmap.size() > 6) {
                night_cond_img1.setImageBitmap(mNightBitmap.get(0));
                night_cond_img2.setImageBitmap(mNightBitmap.get(1));
                night_cond_img3.setImageBitmap(mNightBitmap.get(2));
                night_cond_img4.setImageBitmap(mNightBitmap.get(3));
                night_cond_img5.setImageBitmap(mNightBitmap.get(4));
                night_cond_img6.setImageBitmap(mNightBitmap.get(5));
                night_cond_img7.setImageBitmap(mNightBitmap.get(6));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void setHourDate(List<Hourlyforecast> mHourlyforecast) {
        time_weather.setAdapter(mTimeAdapter);
        mTimeAdapter.notifyDataSetChanged(mHourlyforecast);
    }

    private void init() {
        String a = null;
        int flag = 0;
        Intent intent = getIntent();
        date_linear = (LinearLayout) findViewById(R.id.date_linear);
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        time_weather = (RecyclerView) findViewById(R.id.time_weather);
        bg = (ImageView) findViewById(R.id.bg);
        line_chart = (WeatherChartView) findViewById(R.id.line_char);
        date1 = (TextView) findViewById(R.id.date_1);
        date2 = (TextView) findViewById(R.id.date_2);
        date3 = (TextView) findViewById(R.id.date_3);
        date4 = (TextView) findViewById(R.id.date_4);
        date5 = (TextView) findViewById(R.id.date_5);
        date6 = (TextView) findViewById(R.id.date_6);
        date7 = (TextView) findViewById(R.id.date_7);

        dir1 = (TextView) findViewById(R.id.dir_1);
        dir2 = (TextView) findViewById(R.id.dir_2);
        dir3 = (TextView) findViewById(R.id.dir_3);
        dir4 = (TextView) findViewById(R.id.dir_4);
        dir5 = (TextView) findViewById(R.id.dir_5);
        dir6 = (TextView) findViewById(R.id.dir_6);
        dir7 = (TextView) findViewById(R.id.dir_7);

        sc1 = (TextView) findViewById(R.id.sc_1);
        sc2 = (TextView) findViewById(R.id.sc_2);
        sc3 = (TextView) findViewById(R.id.sc_3);
        sc4 = (TextView) findViewById(R.id.sc_4);
        sc5 = (TextView) findViewById(R.id.sc_5);
        sc6 = (TextView) findViewById(R.id.sc_6);
        sc7 = (TextView) findViewById(R.id.sc_7);

        cond_txt1 = (TextView) findViewById(R.id.cond_txt1);
        cond_txt2 = (TextView) findViewById(R.id.cond_txt2);
        cond_txt3 = (TextView) findViewById(R.id.cond_txt3);
        cond_txt4 = (TextView) findViewById(R.id.cond_txt4);
        cond_txt5 = (TextView) findViewById(R.id.cond_txt5);
        cond_txt6 = (TextView) findViewById(R.id.cond_txt6);
        cond_txt7 = (TextView) findViewById(R.id.cond_txt7);

        cond_img1 = (ImageView) findViewById(R.id.cond_img1);
        cond_img2 = (ImageView) findViewById(R.id.cond_img2);
        cond_img3 = (ImageView) findViewById(R.id.cond_img3);
        cond_img4 = (ImageView) findViewById(R.id.cond_img4);
        cond_img5 = (ImageView) findViewById(R.id.cond_img5);
        cond_img6 = (ImageView) findViewById(R.id.cond_img6);
        cond_img7 = (ImageView) findViewById(R.id.cond_img7);

        night_cond_txt1 = (TextView) findViewById(R.id.night_cond_txt1);
        night_cond_txt2 = (TextView) findViewById(R.id.night_cond_txt2);
        night_cond_txt3 = (TextView) findViewById(R.id.night_cond_txt3);
        night_cond_txt4 = (TextView) findViewById(R.id.night_cond_txt4);
        night_cond_txt5 = (TextView) findViewById(R.id.night_cond_txt5);
        night_cond_txt6 = (TextView) findViewById(R.id.night_cond_txt6);
        night_cond_txt7 = (TextView) findViewById(R.id.night_cond_txt7);


        night_cond_img1 = (ImageView) findViewById(R.id.night_cond_img1);
        night_cond_img2 = (ImageView) findViewById(R.id.night_cond_img2);
        night_cond_img3 = (ImageView) findViewById(R.id.night_cond_img3);
        night_cond_img4 = (ImageView) findViewById(R.id.night_cond_img4);
        night_cond_img5 = (ImageView) findViewById(R.id.night_cond_img5);
        night_cond_img6 = (ImageView) findViewById(R.id.night_cond_img6);
        night_cond_img7 = (ImageView) findViewById(R.id.night_cond_img7);

        hour_rotate = (ImageView) findViewById(R.id.hour_rotate);
        date_rotate = (ImageView) findViewById(R.id.date_rotate);
        hour_rotate.setOnClickListener(this);
        date_rotate.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        time_weather.setLayoutManager(linearLayoutManager);
        time_weather.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        mTimeAdapter = new TimeAdapter();
        time_weather.setAdapter(mTimeAdapter);
        topButton.setOnClickListener(this);
        if (intent != null) {
            city = intent.getStringExtra("city");
            cityid = intent.getStringExtra("cityid");
            a = intent.getStringExtra("txt");
            flag = intent.getIntExtra("flag", 0);
            topTv.setText(city);
        }
        Constans.WeatherBgImg citys[] = Constans.WeatherBgImg.values();
        for (Constans.WeatherBgImg cu : citys) {
            if (a != null && a.equals(cu.getName())) {
                if (cu.getimgId().length > 0) {
                    Glide.with(this).load(cu.getimgId()[flag]).crossFade()
                            .placeholder(R.drawable.bg_loading_eholder).into(bg);
                } else {
                    Glide.with(this).load(cu.getimgId()[0]).crossFade()
                            .placeholder(R.drawable.bg_loading_eholder).into(bg);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.hour_rotate:
//                Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);//创建动画
//                rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转
//                rotate.setFillAfter(true);
//                hour_rotate.startAnimation(rotate);
//
//                if (isUnfoldHour) {
//                    time_weather.setVisibility(View.GONE);
//                    isUnfoldHour = false;
//                } else {
//                    isUnfoldHour = true;
//                    time_weather.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.date_rotate:
//                Animation rotate1 = AnimationUtils.loadAnimation(this, R.anim.rotate);//创建动画
//                date_rotate.startAnimation(rotate1);
//                if (isUnfolddate) {
//                    date_linear.setVisibility(View.GONE);
//                    isUnfolddate = false;
//                } else {
//                    date_linear.setVisibility(View.VISIBLE);
//                    isUnfolddate = true;
//                }
                break;
        }
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
                viewHolder.decs.setText("温度" + min + "℃ " + "降水率为" + mTimeWeatherData.pop + " 相对湿度" + mTimeWeatherData.hum + "%");
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
}