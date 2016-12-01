package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Intent;
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
import com.example.administrator.soweather.com.example.administrator.soweather.activity.CurrentCityActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.Managecity;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.NowWeather;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
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
    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private String city = null;
    private String cityid;
    private TextView add_city;//添加城市
    private TextView manage_city;//管理城市

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
        getAdress();
        getData();
        getHandlerMessage();
        return view;
    }

    private void getAdress() {
        if (getArguments() != null) {
            cityid = getArguments().getString("cityId");
            city = getArguments().getString("city");
            if (cityid != null && city != null) {
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

    private void getData() {
        WeatherService service = new WeatherService();
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
        mSetting = (RelativeLayout) view.findViewById(R.id.setting);
        mLogout = (RelativeLayout) view.findViewById(R.id.logout);
        tmp = (TextView) view.findViewById(R.id.tmp);
        tmp_txt = (TextView) view.findViewById(R.id.tmp_txt);
        tem_img = (ImageView) view.findViewById(R.id.tem_img);
        dress = (TextView) view.findViewById(R.id.dress);
        service_assistant = (RelativeLayout) view.findViewById(R.id.service_assistant);
        add_city = (TextView) view.findViewById(R.id.add_city);
        manage_city = (TextView) view.findViewById(R.id.manage_city);
        add_city.setOnClickListener(this);
        manage_city.setOnClickListener(this);
        mHome.setOnClickListener(this);
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
                MainFragment mMainFragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                bundle.putString("cityId", cityid);
                mMainFragment.setArguments(bundle);
                newContent = mMainFragment;
                title = "首页";
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
            case R.id.add_city:
                //添加城市 //传递一个参数过去  判断是首页的选择城市还是这里,在将选择的城市增加到数据库
                Intent intent1 = new Intent(getActivity(), CurrentCityActivity.class);
                intent1.putExtra("type", CurrentCityActivity.TYPE);
                startActivity(intent1);
                break;
            case R.id.manage_city:
                //管理城市   添加的城市,包括当前位置城市,卡片显示天气
                Intent intent = new Intent(getActivity(), Managecity.class);
                startActivity(intent);
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
