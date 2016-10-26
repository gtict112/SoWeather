package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.sertvice.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.AutoScrollTextView;
import com.example.administrator.soweather.com.example.administrator.soweather.view.LineGraphicView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements ResponseListenter<List<WeatherData>> {
    private View view;
    private Appconfiguration config = Appconfiguration.getInstance();
    private LineGraphicView tu;
    private ArrayList<Double> yList;
    private RadioGroup mSelectType;
    private RadioButton mTypeTime;
    private RadioButton mTypeDally;
    private TextView mLiftIndex;
    private PopupWindow popupwindow;
    private RelativeLayout mHead1;
    private LinearLayout mHead2;
    private TextView mP25Num;
    private TextView mP25Name;
    private TextView mTmp;
    private TextView tmp_txt;
    private TextView sc;
    private TextView dir;
    private TextView deg;
    private TextView flubrf;
    private TextView drsgbrf;
    private TextView travbrf;
    private TextView cwbrf;
    private TextView sportbrf;
    private TextView uvbrf;
    private TextView hum;
    private TextView pcpn;
    private TextView fl;
    private ImageView weatherImg;
    private FrameLayout mMoreWeather;
    private List<WeatherData> mData = new ArrayList<WeatherData>();
    private Handler mHandler;
    private List<WeatherData.HourlyForecast> mHourlyForecast = new ArrayList<>();
    private List<WeatherData.DailyForecase> mDailyForecase = new ArrayList<>();
    private TimeFragment mTimeFragment;
    private DailyforecastFragment mDailyforecastFragment;
    private AutoScrollTextView TextViewNotice;

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
        Bundle bundle = getArguments();
        initView(view);
        String cityid = null;
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

    private void showFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(R.id.more_weather, fragment).commit();
    }

    private void init(List<WeatherData> mData) throws JSONException {
        config.dismissProgressDialog();
        weatherImg.setImageBitmap(mData.get(0).drawable);
        mP25Name.setText(mData.get(0).qlty);
        mP25Num.setText("Pm25: " + mData.get(0).pm25);
        mTmp.setText(mData.get(0).tmp + "℃");
        String mTmpTxt = new JSONObject(mData.get(0).cond).optString("txt");
        tmp_txt.setText(mTmpTxt);
        sc.setText("风力: " + mData.get(0).sc);
        dir.setText(mData.get(0).dir);
        deg.setText("角度: " + mData.get(0).deg);
        flubrf.setText(mData.get(0).flubrf);
        drsgbrf.setText(mData.get(0).drsgbrf);
        travbrf.setText(mData.get(0).travbrf);
        cwbrf.setText(mData.get(0).cwbrf);
        sportbrf.setText(mData.get(0).sportbrf);
        uvbrf.setText(mData.get(0).uvbrf);
        hum.setText("湿度" + mData.get(0).hum + "(%)");
        pcpn.setText("降雨量" + mData.get(0).pcpn + "(mm)");
        fl.setText("体感温度" + mData.get(0).fl + "℃");
        String ss = new JSONObject(mData.get(0).mDailyForecase.get(0).astro).optString("ss");
        String sr = new JSONObject(mData.get(0).mDailyForecase.get(0).astro).optString("sr");
        String min = new JSONObject(mData.get(0).mDailyForecase.get(0).tmp).optString("min");
        String max = new JSONObject(mData.get(0).mDailyForecase.get(0).tmp).optString("max");
        TextViewNotice.setText("日出时间: " + sr + "    日落时间: " + ss + "     当天最低温度: " + min + " ℃" + "     当前最高温度 " + max + " ℃");
        TextViewNotice.init(getActivity().getWindowManager());

        mHourlyForecast = mData.get(0).mHourlyforecast;
        mTimeFragment = new TimeFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putParcelableArrayList(TimeFragment.DATA, (ArrayList<? extends Parcelable>) mHourlyForecast);
        mTimeFragment.setArguments(bundle1);
        showFragment(mTimeFragment);
    }

    private void initView(View view) {
        mSelectType = (RadioGroup) view.findViewById(R.id.select_more_type);
        mTypeTime = (RadioButton) view.findViewById(R.id.type_time);
        mTypeDally = (RadioButton) view.findViewById(R.id.type_dally);
        //        tu = (LineGraphicView) view.findViewById(R.id.tu);
        mLiftIndex = (TextView) view.findViewById(R.id.life_index);
        mHead1 = (RelativeLayout) view.findViewById(R.id.head1);
        mHead2 = (LinearLayout) view.findViewById(R.id.head2);
        mP25Num = (TextView) view.findViewById(R.id.p25_num);
        mP25Name = (TextView) view.findViewById(R.id.p25_name);
        mTmp = (TextView) view.findViewById(R.id.tmp);
        tmp_txt = (TextView) view.findViewById(R.id.tmp_txt);
        sc = (TextView) view.findViewById(R.id.sc);
        dir = (TextView) view.findViewById(R.id.dir);
        deg = (TextView) view.findViewById(R.id.deg);
        flubrf = (TextView) view.findViewById(R.id.flubrf);
        drsgbrf = (TextView) view.findViewById(R.id.drsgbrf);
        travbrf = (TextView) view.findViewById(R.id.travbrf);
        cwbrf = (TextView) view.findViewById(R.id.cwbrf);
        sportbrf = (TextView) view.findViewById(R.id.sportbrf);
        uvbrf = (TextView) view.findViewById(R.id.uvbrf);
        hum = (TextView) view.findViewById(R.id.hum);
        pcpn = (TextView) view.findViewById(R.id.pcpn);
        fl = (TextView) view.findViewById(R.id.fl);
        weatherImg = (ImageView) view.findViewById(R.id.weatherImg);
        mMoreWeather = (FrameLayout) view.findViewById(R.id.more_weather);
        TextViewNotice = (AutoScrollTextView) view.findViewById(R.id.TextViewNotice);
        TextViewNotice.init(getActivity().getWindowManager());
        TextViewNotice.startScroll();
        yList = new ArrayList<Double>();
        yList.add((double) 2.103);
        yList.add(4.05);
        yList.add(6.60);
        yList.add(3.08);
        yList.add(4.32);
        yList.add(2.0);
        yList.add(5.0);

        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("05-19");
        xRawDatas.add("05-20");
        xRawDatas.add("05-21");
        xRawDatas.add("05-22");
        xRawDatas.add("05-23");
        xRawDatas.add("05-24");
        xRawDatas.add("05-25");
        xRawDatas.add("05-26");
        // tu.setData(yList, xRawDatas, 8, 2);
        mSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.type_time) {
                    mTimeFragment = new TimeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(TimeFragment.DATA, (ArrayList<? extends Parcelable>) mHourlyForecast);
                    mTimeFragment.setArguments(bundle);
                    showFragment(mTimeFragment);
                } else if (checkedId == R.id.type_dally) {
                    mDailyforecastFragment = new DailyforecastFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(DailyforecastFragment.DATA, (ArrayList<? extends Parcelable>) mDailyForecase);
                    mDailyforecastFragment.setArguments(bundle);
                    showFragment(mDailyforecastFragment);
                }
            }
        });
        mLiftIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    //弹出指数popwod;
                    //   initmPopupWindowView();
                    //  popupwindow.showAtLocation(mLiftIndex, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 170);
                }
            }
        });
    }

    private void initmPopupWindowView() {
        View customView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_lift_index,
                null, false);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupwindow = new PopupWindow(customView, (int) (dm.widthPixels * 0.98), mHead1.getHeight() + mHead2.getHeight() + 50);
        popupwindow.setAnimationStyle(R.style.AnimationFade);
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
            for (int i = 0; i < mHourlyForecast.size(); i++) {
                mHourlyForecast.get(i).tem_img = mData.get(0).drawable;
            }
            mDailyForecase = result.getData().get(0).mDailyForecase;
            mHandler.sendMessage(mHandler.obtainMessage(111, mData));
        } else {
            result.getErrorMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                Bundle b = data.getExtras();
                String str = b.getString("cityid");
                getData(str);
                break;
            default:
                break;
        }
    }

}
