package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
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
 * Created by Administrator on 2016/12/1.
 */

public class TodayDetailFragment extends Fragment implements View.OnClickListener {
    private TextView topTv;
    private ImageView topButton;

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
    private String city;
    private String cityid;
    private Appconfiguration config = Appconfiguration.getInstance();
    private List<Dailyforecast> mDailyforecast = new ArrayList<>();
    private Handler mHandler;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private SoWeatherDB cityDB;
    private List<Bitmap> mDayBitmap = new ArrayList<>();
    private List<Bitmap> mNightBitmap = new ArrayList<>();

    /**
     * 其实想把View增加到数据,再直接获取,唉,后面再改吧
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_today_detail, null);
        cityDB = SoWeatherDB.getInstance(getActivity());
        weathimgs = cityDB.getAllWeatherImg();
        initView(view);
        getDate();
        getHandleMessge();
        return view;
    }

    private void initView(View view) {
        topTv = (TextView) view.findViewById(R.id.topTv);
        topButton = (ImageView) view.findViewById(R.id.topButton);
        line_chart = (WeatherChartView) view.findViewById(R.id.line_char);
        date1 = (TextView) view.findViewById(R.id.date_1);
        date2 = (TextView) view.findViewById(R.id.date_2);
        date3 = (TextView) view.findViewById(R.id.date_3);
        date4 = (TextView) view.findViewById(R.id.date_4);
        date5 = (TextView) view.findViewById(R.id.date_5);
        date6 = (TextView) view.findViewById(R.id.date_6);
        date7 = (TextView) view.findViewById(R.id.date_7);

        dir1 = (TextView) view.findViewById(R.id.dir_1);
        dir2 = (TextView) view.findViewById(R.id.dir_2);
        dir3 = (TextView) view.findViewById(R.id.dir_3);
        dir4 = (TextView) view.findViewById(R.id.dir_4);
        dir5 = (TextView) view.findViewById(R.id.dir_5);
        dir6 = (TextView) view.findViewById(R.id.dir_6);
        dir7 = (TextView) view.findViewById(R.id.dir_7);

        sc1 = (TextView) view.findViewById(R.id.sc_1);
        sc2 = (TextView) view.findViewById(R.id.sc_2);
        sc3 = (TextView) view.findViewById(R.id.sc_3);
        sc4 = (TextView) view.findViewById(R.id.sc_4);
        sc5 = (TextView) view.findViewById(R.id.sc_5);
        sc6 = (TextView) view.findViewById(R.id.sc_6);
        sc7 = (TextView) view.findViewById(R.id.sc_7);

        cond_txt1 = (TextView) view.findViewById(R.id.cond_txt1);
        cond_txt2 = (TextView) view.findViewById(R.id.cond_txt2);
        cond_txt3 = (TextView) view.findViewById(R.id.cond_txt3);
        cond_txt4 = (TextView) view.findViewById(R.id.cond_txt4);
        cond_txt5 = (TextView) view.findViewById(R.id.cond_txt5);
        cond_txt6 = (TextView) view.findViewById(R.id.cond_txt6);
        cond_txt7 = (TextView) view.findViewById(R.id.cond_txt7);

        cond_img1 = (ImageView) view.findViewById(R.id.cond_img1);
        cond_img2 = (ImageView) view.findViewById(R.id.cond_img2);
        cond_img3 = (ImageView) view.findViewById(R.id.cond_img3);
        cond_img4 = (ImageView) view.findViewById(R.id.cond_img4);
        cond_img5 = (ImageView) view.findViewById(R.id.cond_img5);
        cond_img6 = (ImageView) view.findViewById(R.id.cond_img6);
        cond_img7 = (ImageView) view.findViewById(R.id.cond_img7);

        night_cond_txt1 = (TextView) view.findViewById(R.id.night_cond_txt1);
        night_cond_txt2 = (TextView) view.findViewById(R.id.night_cond_txt2);
        night_cond_txt3 = (TextView) view.findViewById(R.id.night_cond_txt3);
        night_cond_txt4 = (TextView) view.findViewById(R.id.night_cond_txt4);
        night_cond_txt5 = (TextView) view.findViewById(R.id.night_cond_txt5);
        night_cond_txt6 = (TextView) view.findViewById(R.id.night_cond_txt6);
        night_cond_txt7 = (TextView) view.findViewById(R.id.night_cond_txt7);


        night_cond_img1 = (ImageView) view.findViewById(R.id.night_cond_img1);
        night_cond_img2 = (ImageView) view.findViewById(R.id.night_cond_img2);
        night_cond_img3 = (ImageView) view.findViewById(R.id.night_cond_img3);
        night_cond_img4 = (ImageView) view.findViewById(R.id.night_cond_img4);
        night_cond_img5 = (ImageView) view.findViewById(R.id.night_cond_img5);
        night_cond_img6 = (ImageView) view.findViewById(R.id.night_cond_img6);
        night_cond_img7 = (ImageView) view.findViewById(R.id.night_cond_img7);


        topButton.setOnClickListener(this);
        if (getArguments() != null) {
            city = getArguments().getString("city");
            cityid = getArguments().getString("cityid");
            topTv.setText(city);
        }
    }

    private void getDate() {
        WeatherService mWeatherService = new WeatherService();
        mWeatherService.getDailyforecastData(new ResponseListenter<List<Dailyforecast>>() {
            @Override
            public void onReceive(Result<List<Dailyforecast>> result) {
                if (result.isSuccess()) {
                    config.dismissProgressDialog();
                    mDailyforecast = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mDailyforecast));
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
                        setViewData(mDailyforecast);
                        break;
                }
            }
        };
    }


    private void setViewData(List<Dailyforecast> mDailyforecast) {
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

            cond_txt1.setText(new JSONObject(mDailyforecast.get(0).cond).optString("txt_d"));
            cond_txt2.setText(new JSONObject(mDailyforecast.get(1).cond).optString("txt_d"));
            cond_txt3.setText(new JSONObject(mDailyforecast.get(2).cond).optString("txt_d"));
            cond_txt4.setText(new JSONObject(mDailyforecast.get(3).cond).optString("txt_d"));
            cond_txt5.setText(new JSONObject(mDailyforecast.get(4).cond).optString("txt_d"));
            cond_txt6.setText(new JSONObject(mDailyforecast.get(5).cond).optString("txt_d"));
            cond_txt7.setText(new JSONObject(mDailyforecast.get(6).cond).optString("txt_d"));

            night_cond_txt1.setText(new JSONObject(mDailyforecast.get(0).cond).optString("txt_n"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                break;
        }
    }
}
