package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.core.SoWeatherApplication;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.LeftFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MainFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.CityAndWeatherImgService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.SlidingMenu;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.waps.AppConnect;

/**
 * Created by Administrator on 2016/10/10.
 * MainActivity 界面切换,初识定位,获取城市数据,保存至数据库
 */
public class MainActivity extends SlidingFragmentActivity implements
        View.OnClickListener, ResponseListenter<Integer> {
    private ImageView topButton;
    private Fragment mContent;
    private TextView topTextView;
    private TextView mDresss;
    private String city = null;
    private String cityid = null;
    private LocationClient mLocationClient;//定位SDK的核心类
    private LinearLayout top_right;
    private ImageView top_right_img;
    private Boolean isClick = false;
    private RelativeLayout content_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(Color.parseColor("#e075ADA2"));

        initView();
        if (cityid == null) {
            getLocationAdress();
        }
        getCityDate();
        getWeather();
        initSlidingMenu(savedInstanceState);
        if (mContent == null) {
            MainFragment mMainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", city);
            bundle.putString("cityId", cityid);
            mMainFragment.setArguments(bundle);
            mContent = mMainFragment;
            switchConent(mContent, "天气");
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 定位城市
     */
    private void getLocationAdress() {
        mDresss.setText("正在定位");
        mLocationClient = ((SoWeatherApplication) getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setNeedDeviceDirect(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        ((SoWeatherApplication) getApplication()).setCallbackadress(new SoWeatherApplication.Callbackadress() {
            @Override
            public void finish(String address) {
                if (address != null) {
                    mLocationClient.stop();
                    mDresss.setText(address);
                } else {
                    mLocationClient.stop();
                    mDresss.setText("杭州");
                    Toast.makeText(MainActivity.this, "定位失败,已默认城市为杭州,请手动修改城市!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 获取服务器城市列表数据
     */
    private void getCityDate() {
        CityAndWeatherImgService service = new CityAndWeatherImgService();
        service.getCityData(MainActivity.this, this);
    }

    /**
     * 获取天气状态图标
     */
    private void getWeather() {
        CityAndWeatherImgService service = new CityAndWeatherImgService();
        service.getWeatherImgData(MainActivity.this, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    private void initView() {
        Intent intent = getIntent();
        mDresss = (TextView) findViewById(R.id.dresss);
        top_right = (LinearLayout) findViewById(R.id.top_righgt);
        top_right_img = (ImageView) findViewById(R.id.top_righgt_img);
        content_title = (RelativeLayout) findViewById(R.id.content_title);
        if (intent != null) {
            city = intent.getStringExtra("city");
            cityid = intent.getStringExtra("cityid");
            mDresss.setText(city);
        }
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        topTextView = (TextView) findViewById(R.id.topTv);
    }

    private void initSlidingMenu(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
        setBehindContentView(R.layout.menu_frame_left);
        LeftFragment mLeftFragment = new LeftFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        bundle.putString("cityid", cityid);
        mLeftFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, mLeftFragment).commit();
        SlidingMenu sm = getSlidingMenu();
        sm.setMode(SlidingMenu.LEFT);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(null);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setBehindScrollScale(0.0f);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchConent(Fragment fragment, final String title) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
        getSlidingMenu().showContent();
        topTextView.setText(title);
        if (title.equals("天气")) {
            top_right.setVisibility(View.VISIBLE);
            mDresss.setVisibility(View.VISIBLE);
            content_title.setBackgroundResource(R.color.theme_main_title);
            top_right_img.setImageResource(R.mipmap.place);
            top_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CurrentCityActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            content_title.setBackgroundResource(R.color.theme_title);
            top_right.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                toggle();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout confirmDialog = new DialogLogout(this, "确定要退出吗?", "退出", "取消");
        confirmDialog.show();
        confirmDialog.setClicklistener(new com.example.administrator.soweather.com.example.administrator.soweather.general.DialogLogout.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                confirmDialog.dismiss();
                Appconfiguration.getInstance().closeAllActivities();
                AppConnect.getInstance(MainActivity.this).close();
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
        return false;
    }

    @Override
    public void onReceive(Result<Integer> result) {
    }

}