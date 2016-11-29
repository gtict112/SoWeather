package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Suggestion;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.InstrumentView;

/**
 * Created by Administrator on 2016/11/22.
 */

public class AqiActivity extends Activity implements View.OnClickListener {
    private Suggestion mSuggestion = new Suggestion();
    private InstrumentView aqi;
    private TextView pm10;
    private TextView pm25;
    private TextView no2;
    private TextView so2;
    private TextView co;
    private TextView o3;
    private TextView tip_title;
    private TextView tip_content;
    private TextView date;
    private TextView topTv;
    private ImageView topButton;
    private Handler mHandler;
    private Appconfiguration config = Appconfiguration.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqi);
        initView();
        getData();
        getHandleMessge();
    }

    private void initView() {
        aqi = (InstrumentView) findViewById(R.id.aqi);
        pm10 = (TextView) findViewById(R.id.pm10);
        pm25 = (TextView) findViewById(R.id.pm25);
        no2 = (TextView) findViewById(R.id.no2);
        so2 = (TextView) findViewById(R.id.so2);
        co = (TextView) findViewById(R.id.co);
        o3 = (TextView) findViewById(R.id.o3);
        tip_title = (TextView) findViewById(R.id.tip_title);
        tip_content = (TextView) findViewById(R.id.tip_content);
        date = (TextView) findViewById(R.id.date);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("今日空气质量");
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
    }

    private void getData() {
        String cityid =null;
        Intent intent = getIntent();
        if (intent != null) {
            date.setText("发布时间 " + intent.getStringExtra("date"));
            aqi.setProgress(Integer.parseInt(intent.getStringExtra("aqi")));
            pm10.setText(intent.getStringExtra("pm10"));
            pm25.setText(intent.getStringExtra("pm25"));
            co.setText(intent.getStringExtra("co"));
            no2.setText(intent.getStringExtra("no2"));
            o3.setText(intent.getStringExtra("o3"));
            so2.setText(intent.getStringExtra("so2"));
            cityid = intent.getStringExtra("cityid");
            tip_title.setText(intent.getStringExtra("city") + "的空气质量" + intent.getStringExtra("qlty"));
        }
        getSuggestionData(cityid);
    }
 private void getSuggestionData(String cityid){
     config.showProgressDialog("正在加载", this);
     WeatherService mWeatherService = new WeatherService();
     mWeatherService.getSuggestionData(new ResponseListenter<Suggestion>() {
         @Override
         public void onReceive(Result<Suggestion> result) {
             if (result.isSuccess()) {
                 config.dismissProgressDialog();
                 mSuggestion = result.getData();
                 mHandler.sendMessage(mHandler.obtainMessage(1, mSuggestion));
             } else {
                 config.dismissProgressDialog();
                 Toast.makeText(getApplicationContext(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
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
                        setView(mSuggestion);
                        break;
                }
            }
        };
    }
    private void setView(Suggestion mSuggestion){
        tip_content.setText(mSuggestion.comftex);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                this.overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
        }
    }
}
