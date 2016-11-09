package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.CurrentCityActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.DayWeatherActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.TimeWeatherActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.sertvice.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.key;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements ResponseListenter<List<WeatherData>> {
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView mP25Num;
    private TextView mP25Name;
    private TextView mTmp;
    private TextView flubrf;
    private TextView flu_txt;
    private TextView drsgbrf;
    private TextView drsg_txt;
    private TextView travbrf;
    private TextView trav_txt;
    private TextView sportbrf;
    private TextView sport_txt;
    private ImageView weatherImg;
    private List<WeatherData> mData = new ArrayList<WeatherData>();
    private Handler mHandler;
    private List<WeatherData.HourlyForecast> mHourlyForecast = new ArrayList<WeatherData.HourlyForecast>();
    private ImageView time;
    private List<WeatherData.DailyForecase> mDailyForecase = new ArrayList<WeatherData.DailyForecase>();
    private ImageView day;
    private String cityid;
    private TextView dresss;
    private TextView up;
    private TextView down;
    private FrameLayout mCityImg;
    private TextView code_txt;
    private FrameLayout add_mood_line;

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
        initView(view);
        getData(cityid);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 111) {
                    try {
                        init(mData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        return view;
    }

    private void init(List<WeatherData> mData) throws JSONException {
        config.dismissProgressDialog();
        dresss.setText(mData.get(0).cnty + mData.get(0).city);
        Constans.CITY citys[] = Constans.CITY.values();
        for (Constans.CITY cu : citys) {
            if (dresss.getText().toString().equals(cu.getName())) {
                mCityImg.setBackgroundResource(cu.getIcRes());
            }
        }
        weatherImg.setImageBitmap(mData.get(0).drawable);
        mP25Name.setText("空气质量:  " + mData.get(0).qlty);
        mP25Num.setText("PM25:  " + mData.get(0).pm25);
        mTmp.setText(mData.get(0).tmp + "℃");
        flubrf.setText("感冒指数---" + mData.get(0).flubrf);
        flu_txt.setText(mData.get(0).flutex);
        drsgbrf.setText("穿衣指数---" + mData.get(0).drsgbrf);
        drsg_txt.setText(mData.get(0).drsgtex);
        travbrf.setText("旅游指数---" + mData.get(0).travbrf);
        trav_txt.setText(mData.get(0).travtex);
        sportbrf.setText("运动指数---" + mData.get(0).sportbrf);
        sport_txt.setText(mData.get(0).sporttex);
        String min = new JSONObject(mData.get(0).mDailyForecase.get(0).tmp).optString("min");
        String max = new JSONObject(mData.get(0).mDailyForecase.get(0).tmp).optString("max");
        up.setText(max + "℃");
        down.setText(min + "℃");
        String mTmpTxt = new JSONObject(mData.get(0).cond).optString("txt");
        code_txt.setText(mTmpTxt);
    }

    private void initView(View view) {
        add_mood_line = (FrameLayout) view.findViewById(R.id.add_mood_line);
        code_txt = (TextView) view.findViewById(R.id.code_txt);
        mP25Num = (TextView) view.findViewById(R.id.p25_num);
        mP25Name = (TextView) view.findViewById(R.id.p25_name);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        flubrf = (TextView) view.findViewById(R.id.flubrf);
        drsgbrf = (TextView) view.findViewById(R.id.drsgbrf);
        travbrf = (TextView) view.findViewById(R.id.travbrf);
        sportbrf = (TextView) view.findViewById(R.id.sportbrf);
        weatherImg = (ImageView) view.findViewById(R.id.weatherImg);
        dresss = (TextView) view.findViewById(R.id.dresss);
        up = (TextView) view.findViewById(R.id.up);
        down = (TextView) view.findViewById(R.id.down);
        flu_txt = (TextView) view.findViewById(R.id.flu_txt);
        drsg_txt = (TextView) view.findViewById(R.id.drsg_txt);
        trav_txt = (TextView) view.findViewById(R.id.trav_txt);
        sport_txt = (TextView) view.findViewById(R.id.sport_txt);
        time = (ImageView) view.findViewById(R.id.time);
        day = (ImageView) view.findViewById(R.id.day);
        mCityImg = (FrameLayout) view.findViewById(R.id.city_img);
        dresss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrentCityActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), TimeWeatherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TimeWeatherActivity.DATA, (Serializable) mHourlyForecast);
                intent.putExtra("bitmap", mData.get(0).drawable);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DayWeatherActivity.class);
                startActivity(intent);
            }
        });
        add_mood_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void getData(String cityid) {
        config.showProgressDialog("拼命加载中...", getActivity());
        WeatherService service = new WeatherService();
        service.getWeatherData(this, cityid);
    }

    @Override
    public void onReceive(Result<List<WeatherData>> result) throws Exception {
        if (result.isSuccess()) {
            mData = result.getData();
            mHourlyForecast = result.getData().get(0).mHourlyforecast;
            mDailyForecase = result.getData().get(0).mDailyForecase;
            mHandler.sendMessage(mHandler.obtainMessage(111, mData));
        } else {
            result.getErrorMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                Bundle b = data.getExtras();
                cityid = b.getString("cityid");
                getData(cityid);
                break;
            default:
                break;
        }
    }
}
