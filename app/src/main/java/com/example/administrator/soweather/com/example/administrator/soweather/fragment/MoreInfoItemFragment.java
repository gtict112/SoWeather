package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Constellation;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Dailyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Zodiac;
import com.example.administrator.soweather.com.example.administrator.soweather.service.ConstellationService;
import com.example.administrator.soweather.com.example.administrator.soweather.service.ZodiacService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.OnItemSelectedListener;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.LoopRotarySwitchView;

/**
 * Created by Administrator on 2016/12/9.
 *
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.width;

public class MoreInfoItemFragment extends Fragment {
    private String time;
    private String today;
    private String tomorrow;
    private Dailyforecast mDailyforecast;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private String qlty_txt;
    private String week_txt;
    private ImageView weather_img;
    private TextView tmp;
    private TextView cond;
    private TextView qlty;
    private TextView sc;
    private TextView dir;
    private TextView sunrise;
    private TextView sunset;
    private TextView vis;
    private TextView hum;
    private TextView pop;
    private TextView pcpn;
    private TextView pres;
    private TextView week;
    private TextView yangli;
    private TextView yingli;
    private TextView ji;
    private TextView yi;
    private Zodiac mZodiac = new Zodiac();
    private Constellation mConstellation = new Constellation();
    private Handler mHandler;
    private LoopRotarySwitchView mLoopRotarySwitch;
    private TextView consname;
    private String type;
    private String consname_txt = "巨蟹座";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_more_info_item, container, false);
        cityDB = SoWeatherDB.getInstance(getActivity());
        initDate();
        initView(contextView);
        getHandleMessge();
        return contextView;
    }


    private void initDate() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            time = mBundle.getString("type");
            tomorrow = mBundle.getString("tomorrow");
            today = mBundle.getString("today");
            mDailyforecast = (Dailyforecast) mBundle.getSerializable("dailyforecast");
            qlty_txt = mBundle.getString("qlty");
            week_txt = mBundle.getString("week");
        }
        weathimgs = cityDB.getAllWeatherImg();

        if (today.equals(time)) {
            type = "today";
        } else if (tomorrow.equals(time)) {
            type = "tomorrow";
        } else {
            type = "week";
        }
    }

    private void initView(View view) {
        weather_img = (ImageView) view.findViewById(R.id.weather_img);
        tmp = (TextView) view.findViewById(R.id.tmp);
        cond = (TextView) view.findViewById(R.id.cond);
        qlty = (TextView) view.findViewById(R.id.qlty);
        dir = (TextView) view.findViewById(R.id.dir);
        sc = (TextView) view.findViewById(R.id.sc);
        sunrise = (TextView) view.findViewById(R.id.sunrise);
        sunset = (TextView) view.findViewById(R.id.sunset);
        vis = (TextView) view.findViewById(R.id.vis);
        hum = (TextView) view.findViewById(R.id.hum);
        pop = (TextView) view.findViewById(R.id.pop);
        pcpn = (TextView) view.findViewById(R.id.pcpn);
        pres = (TextView) view.findViewById(R.id.pres);
        week = (TextView) view.findViewById(R.id.week);
        yangli = (TextView) view.findViewById(R.id.yangli);
        yingli = (TextView) view.findViewById(R.id.yingli);
        ji = (TextView) view.findViewById(R.id.ji);
        yi = (TextView) view.findViewById(R.id.yi);
        consname = (TextView) view.findViewById(R.id.consname);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        mLoopRotarySwitch = (LoopRotarySwitchView) view.findViewById(R.id.loop_rotary_switch);
        mLoopRotarySwitch
                .setR(width / 3)//设置R的大小
                .setAutoRotation(false)//是否自动切换
                .setAutoScrollDirection(LoopRotarySwitchView.AutoScrollDirection.left)
                .setAutoRotationTime(1500);//自动切换的时间  单位毫秒
        mLoopRotarySwitch.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void selected(int item, View view) {
                consname_txt = getConstellationName(item);
                consname.setText(consname_txt);
            }
        });


        if (qlty_txt != null) {
            qlty.setText(qlty_txt);
            if (qlty_txt.equals("优")) {
                qlty.setTextColor(getResources().getColor(R.color.gred));

            } else if (qlty_txt.equals("良")) {
                qlty.setTextColor(getResources().getColor(R.color.yellow));
            } else {
                qlty.setTextColor(getResources().getColor(R.color.red));
            }
        }
        week.setText(week_txt);
        if (mDailyforecast != null) {
            String code = "";
            String tmp_txt = "";
            String cond_txt = "";
            String dir_txt = "";
            String sc_txt = "";
            String sunrise_txt = "";
            String sunset_txt = "";
            try {
                code = new JSONObject(mDailyforecast.cond).optString("code_d");
                cond_txt = new JSONObject(mDailyforecast.cond).optString("txt_d");
                dir_txt = new JSONObject(mDailyforecast.wind).optString("dir");
                sc_txt = new JSONObject(mDailyforecast.wind).optString("sc");
                tmp_txt = new JSONObject(mDailyforecast.tmp).optString("min") + "/" + new JSONObject(mDailyforecast.tmp).optString("max");
//                sunrise_txt = new JSONObject(mDailyforecast.astro).optString("sr");//日出日落时间字段暂时为空
//                sunset_txt = new JSONObject(mDailyforecast.astro).optString("ss");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    weather_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
            tmp.setText(tmp_txt);
            cond.setText(cond_txt);
            dir.setText(dir_txt);
            sc.setText(sc_txt);
            vis.setText(mDailyforecast.vis + " KM");
            hum.setText(mDailyforecast.hum + "%");
            pop.setText(mDailyforecast.pop);
            pcpn.setText(mDailyforecast.pcpn + "mm");
            pres.setText(mDailyforecast.pres + "hPa");
            yangli.setText(mDailyforecast.date.substring(mDailyforecast.date.length() - 2, mDailyforecast.date.length()));
            getZodiac(mDailyforecast.date);
            getConstellationService();
//            sunrise.setText(sunrise_txt);
//            sunset.setText(sunset_txt);
        }
    }


    private void getConstellationService() {
        ConstellationService mConstellationService = new ConstellationService();
        mConstellationService.getConstellationService(new ResponseListenter<Constellation>() {
            @Override
            public void onReceive(Result<Constellation> result) {
                if (result.isSuccess()) {
                    mConstellation = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(3, mConstellation));
                } else {
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, consname_txt, type);
    }


    private void getZodiac(String date) {
        ZodiacService mZodiacService = new ZodiacService();
        mZodiacService.getZodiacService(new ResponseListenter<Zodiac>() {
            @Override
            public void onReceive(Result<Zodiac> result) {
                if (result.isSuccess()) {
                    mZodiac = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(4, mZodiac));
                } else {
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, date);
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 4:
                        setZodiac(mZodiac);
                        break;
                    case 3:
                        setConstellation(mConstellation);
                        break;
                }
            }
        };
    }

    private void setZodiac(Zodiac mZodiac) {
        yi.setText(mZodiac.yi);
        ji.setText(mZodiac.ji);
        yingli.setText(mZodiac.yinli);
    }

    private void setConstellation(Constellation mConstellation) {

    }


    private String getConstellationName(int item) {
        String mConstellationName = "巨蟹座";
        switch (item) {
            case 0:
                mConstellationName = "巨蟹座";
                break;
            case 1:
                mConstellationName = "狮子座";
                break;
            case 2:
                mConstellationName = "处女座";
                break;
            case 3:
                mConstellationName = "天秤座";
                break;
            case 4:
                mConstellationName = "天蝎座";
                break;
            case 5:
                mConstellationName = "射手座";
                break;
            case 6:
                mConstellationName = "摩羯座";
                break;
            case 7:
                mConstellationName = "水瓶座";
                break;
            case 8:
                mConstellationName = "双鱼座";
                break;
            case 9:
                mConstellationName = "白羊座";
                break;
            case 10:
                mConstellationName = "金牛座";
                break;
            case 11:
                mConstellationName = "双子座";
                break;
        }
        return mConstellationName;
    }

}