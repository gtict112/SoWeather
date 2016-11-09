package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.sertvice.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */

public class LeftFragment extends Fragment implements View.OnClickListener, ResponseListenter<List<WeatherData>> {
    private RelativeLayout mHome;//首页
    private RelativeLayout mLifeindex;//生活指数
    private RelativeLayout mLittlebear;//小笨熊客服
    private RelativeLayout mSetting;//设置
    private RelativeLayout mLogout;//退出
    private TextView tmp;
    private TextView tmp_txt;
    private ImageView tem_img;
    private Appconfiguration config = Appconfiguration.getInstance();
    private Handler mHandler;
    private List<WeatherData> mData = new ArrayList<WeatherData>();
    private TextView dress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mean, null);
        initView(view);
        getData();
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

    private void getData() {
        WeatherService service = new WeatherService();
        String city = null;
        service.getWeatherData(this, city);
    }

    private void init(List<WeatherData> mData) throws JSONException {
        tem_img.setImageBitmap(mData.get(0).drawable);
        dress.setText(mData.get(0).cnty + mData.get(0).city);
        tmp.setText(mData.get(0).tmp + "℃");
        String mTmpTxt = new JSONObject(mData.get(0).cond).optString("txt");
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
        mHome.setOnClickListener(this);
        mLifeindex.setOnClickListener(this);
        mLittlebear.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mLogout.setOnClickListener(this);
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
                LifeIndexFragment mLifeIndexFragment = new LifeIndexFragment();
                Bundle bundle = new Bundle();
                bundle.putString("cw_brf", mData.get(0).cwbrf);
                bundle.putString("cw_txt", mData.get(0).cwtex);
                bundle.putString("flu_brf", mData.get(0).flubrf);
                bundle.putString("flu_txt", mData.get(0).flutex);
                bundle.putString("drsg_brf", mData.get(0).drsgbrf);
                bundle.putString("drsg_txt", mData.get(0).drsgtex);
                bundle.putString("sport_brf", mData.get(0).sportbrf);
                bundle.putString("sport_txt", mData.get(0).sporttex);
                bundle.putString("trav_brf", mData.get(0).travbrf);
                bundle.putString("trav_txt", mData.get(0).travtex);
                bundle.putString("uv_brf", mData.get(0).uvbrf);
                bundle.putString("uv_txt", mData.get(0).uvtex);
                mLifeIndexFragment.setArguments(bundle);
                newContent = mLifeIndexFragment;
                title = "图表天气";
                break;
            case R.id.little_bear:
                newContent = new MoodLineFragment();
                title = "心情线";
                break;
            case R.id.setting:
                SettingFragment mSettingFragment = new SettingFragment();
                Bundle bundle1 = new Bundle();
                String mTmpTxt = null;
                try {
                    mTmpTxt = new JSONObject(mData.get(0).cond).optString("txt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bundle1.putString("天气描述", mTmpTxt);
                bundle1.putString("位置", mData.get(0).cnty + mData.get(0).city);
                bundle1.putString("温度", mData.get(0).tmp);
                bundle1.putParcelable("天气图片",mData.get(0).drawable);
                mSettingFragment.setArguments(bundle1);
                title = "系统设置";
                newContent = mSettingFragment;
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
    public void onReceive(Result<List<WeatherData>> result) throws Exception {
        if (result.isSuccess()) {
            mData = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(111, mData));
        } else {
            result.getErrorMessage();
        }
    }
}
