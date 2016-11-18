package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class LeftFragment extends Fragment implements View.OnClickListener, ResponseListenter<NowWeather> {
    private RelativeLayout mHome;//首页
    private RelativeLayout mLifeindex;//生活指数
    private RelativeLayout mLittlebear;//心情线
    private RelativeLayout service_assistant;//我的助手
    private RelativeLayout mSetting;//设置
    private RelativeLayout mLogout;//退出
    private TextView tmp;
    private TextView tmp_txt;
    private ImageView tem_img;
    private Handler mHandler;
    private NowWeather mData = new NowWeather();
    private TextView dress;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mean, null);
        initView(view);
        cityDB = SoWeatherDB.getInstance(getActivity());
        getData();
        getHandlerMessage();
        return view;
    }

    private void getData() {
        WeatherService service = new WeatherService();
        String city = null;
        service.getNowWeatherData(this, city);
    }

    private void getHandlerMessage() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    try {
                        setView(mData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void setView(NowWeather mData) throws JSONException {
        weathimgs = cityDB.getAllWeatherImg();
        dress.setText(mData.cnty + mData.city);
        tmp.setText(mData.tmp + "℃");
        String mTmpTxt = new JSONObject(mData.cond).optString("txt");
        String code = new JSONObject(mData.cond).optString("code");
        for (int i = 0; i < weathimgs.size(); i++) {
            if (code.equals(weathimgs.get(i).getCode())) {
                tem_img.setImageBitmap(weathimgs.get(i).getIcon());
            }
        }

        tmp_txt.setText(mTmpTxt);
    }

    private void initView(View view) {
        mHome = (RelativeLayout) view.findViewById(R.id.home);
        mLifeindex = (RelativeLayout) view.findViewById(R.id.life_index);
        mLittlebear = (RelativeLayout) view.findViewById(R.id.little_bear);
        mSetting = (RelativeLayout) view.findViewById(R.id.setting);
        mLogout = (RelativeLayout) view.findViewById(R.id.logout);
        tmp = (TextView) view.findViewById(R.id.tmp);
        tmp_txt = (TextView) view.findViewById(R.id.tmp_txt);
        tem_img = (ImageView) view.findViewById(R.id.tem_img);
        dress = (TextView) view.findViewById(R.id.dress);
        service_assistant = (RelativeLayout) view.findViewById(R.id.service_assistant);
        mHome.setOnClickListener(this);
        mLifeindex.setOnClickListener(this);
        mLittlebear.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        service_assistant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment newContent = null;
        String title = null;
        switch (v.getId()) {
            case R.id.home:
                newContent = new MainFragment();
                title = "首页";
                break;
            case R.id.life_index:
                newContent = new LifeIndexFragment();
                title = "图表天气";
                break;
            case R.id.little_bear:
                newContent = new MoodLineFragment();
                title = "心情线";
                break;
            case R.id.setting:
                newContent = new SettingFragment();
                title = "系统设置";
                break;
            case R.id.logout:
                final com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout confirmDialog = new DialogLogout(getActivity(), "确定要退出吗?", "退出", "取消");
                confirmDialog.show();
                confirmDialog.setClicklistener(new com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        confirmDialog.dismiss();
                        getActivity().finish();
                    }

                    @Override
                    public void doCancel() {
                        confirmDialog.dismiss();
                    }
                });
                break;
            case R.id.service_assistant:
                newContent = new CustomerFragment();
                title = "我的助手";
                break;
            default:
                break;
        }
        if (newContent != null) {
            switchFragment(newContent, title);
        }
    }

    private void switchFragment(Fragment fragment, String title) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity) {
            MainActivity fca = (MainActivity) getActivity();
            fca.switchConent(fragment, title);
        }
    }

    @Override
    public void onReceive(Result<NowWeather> result) {
        if (result.isSuccess()) {
            mData = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(1, mData));
        } else {
            result.getErrorMessage();
        }
    }
}
