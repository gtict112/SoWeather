package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.AppGlobal;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.core.SoWeatherApplication;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.BeautyFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MainFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.TodayTopNewFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.CityAndWeatherImgService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import cn.waps.AppConnect;

/**
 * Created by Administrator on 2016/10/10.
 * MainActivity 界面切换,初识定位,获取城市数据,保存至数据库
 */
public class MainActivity extends BaseActivity implements ResponseListenter<Integer> {
    private String city = null;
    private String cityid = null;
    private String county = null;
    private LocationClient mLocationClient;//定位SDK的核心类
    private static final String FRAGMENT_TAG_WEATHER = "weeather";
    private static final String FRAGMENT_TAG_GANK = "img";
    private static final String FRAGMENT_TAG_READING = "reading";


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private String currentFragmentTag;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initView();
        initNavigationViewHeader();
        initFragment(savedInstanceState);
        getCityDate();
        getWeather();
    }


    /**
     * 定位城市
     */
    private void getLocationAdress() {
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
                } else {
                    mLocationClient.stop();
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
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (intent != null) {
            city = intent.getStringExtra("city");
            cityid = intent.getStringExtra("cityid");
            county = intent.getStringExtra("County");
        }
        if (cityid == null) {
            getLocationAdress();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Snackbar.make(MainActivity.this.getWindow().getDecorView().findViewById(android.R.id.content), "确认退出应用吗 ?", Snackbar.LENGTH_LONG)
                    .setAction("退出", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Appconfiguration.getInstance().closeAllActivities();
                            AppConnect.getInstance(MainActivity.this).close();
                            finish();
                        }
                    })
                    .show();
        }
        return false;

    }

    @Override
    public void onReceive(Result<Integer> result) {
    }


    /**
     * 初始化加载抽屉里头部布局
     */
    private void initNavigationViewHeader() {
        navigationView = (NavigationView) findViewById(R.id.content_frame);
        navigationView.inflateHeaderView(R.layout.header_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelected());
    }


    class NavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(final MenuItem menuItem) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()) {
                case R.id.home:
                    menuItem.setChecked(true);
                    switchContent(FRAGMENT_TAG_WEATHER);//天气
                    break;
                case R.id.welfare_association:
                    menuItem.setChecked(true);
                    switchContent(FRAGMENT_TAG_GANK);//图片
                    break;
                case R.id.today_top_new:
                    menuItem.setChecked(true);
                    switchContent(FRAGMENT_TAG_READING);//阅读
                    break;
                case R.id.setting:
                    startActivity(new Intent(MainActivity.this, SettingActivity.class));//设置界面
                    break;
                case R.id.navigation_item_about:
                    startActivity(new Intent(MainActivity.this, AboutMoreActivity.class));//关于界面
                    break;
            }
            return false;
        }
    }


    /**
     * 初始化抽屉里的fragment
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        if (county != null) {
            switchContent(FRAGMENT_TAG_WEATHER);
        } else {
            if (savedInstanceState == null) {
                switchContent(FRAGMENT_TAG_WEATHER);
            } else {
                currentFragmentTag = savedInstanceState.getString(AppGlobal.CURRENT_INDEX);
                switchContent(currentFragmentTag);
            }
        }
    }

    /**
     * 选中切换fragment
     *
     * @param name
     */
    public void switchContent(String name) {
        if (currentFragmentTag != null && currentFragmentTag.equals(name))
            return;

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment foundFragment = fragmentManager.findFragmentByTag(name);

        if (foundFragment == null) {
            switch (name) {
                case FRAGMENT_TAG_WEATHER:
                    foundFragment = new MainFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("city", city);
                    bundle.putString("cityId", cityid);
                    bundle.putString("county", county);
                    foundFragment.setArguments(bundle);
                    break;
                case FRAGMENT_TAG_GANK:
                    foundFragment = new BeautyFragment();
                    break;
                case FRAGMENT_TAG_READING:
                    foundFragment = new TodayTopNewFragment();
                    break;
            }
        }

        if (foundFragment == null) {

        } else if (foundFragment.isAdded()) {
            ft.show(foundFragment);
        } else {
            ft.add(R.id.contentLayout, foundFragment, name);
        }
        ft.commit();
        currentFragmentTag = name;
        invalidateOptionsMenu();
    }


    public void initDrawer(Toolbar toolbar) {
        if (toolbar != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            mDrawerToggle.syncState();
            mDrawerLayout.addDrawerListener(mDrawerToggle);
        }
    }

}