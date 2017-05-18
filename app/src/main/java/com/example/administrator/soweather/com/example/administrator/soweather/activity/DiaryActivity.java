package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.LocationApplication;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.MoodLineDate;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HeartLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/1.
 */
//        //内容列表数据点击,进入另一个界面,不同于增加界面,类似于修改
//        //城市列表数据,只有重新获取定位城市才可以手动选择修改城市,其它不可以,进来,只有位置发生了改变了,才会重新定位,区间"市"
public class DiaryActivity extends BaseActivity implements View.OnClickListener {
    private HeartLayout heart_layout;
    private Timer mTimer = new Timer();
    private Random mRandom = new Random();
    private TextView topTv;
    private ListView list_address;
    private ListView list_content;
    private LocationClient mLocationClient;//定位SDK的核心类
    private String mAddress;
    private List<MoodLineDate> mDate = new ArrayList<>();
    private List<MoodLineDate.Content> mContentDate = new ArrayList<>();
    private AddressAdapter mAddressAdapter;
    private Boolean isClick = false;
    private PopupWindow popupwindow;
    private TextView topRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_diary);
        initView();
        getLocationAdress();
        setHeart();
    }

    private void initView() {
        heart_layout = (HeartLayout) findViewById(R.id.heart_layout);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("我的日记");
        topRight = (TextView) findViewById(R.id.topRight);
        topRight.setVisibility(View.VISIBLE);
        topRight.setOnClickListener(this);
        findViewById(R.id.topButton).setOnClickListener(this);
        list_address = (ListView) findViewById(R.id.list_address);
        list_content = (ListView) findViewById(R.id.list_content);
    }

    private void setHeart() {
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                heart_layout.post(new Runnable() {
                    @Override
                    public void run() {
                        heart_layout.addHeart(randomColor());
                    }
                });
            }
        }, 500, 200);
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                break;
            case R.id.editor:
                //编辑功能,删除想要删除的数据,没有数据提示当前暂没有可编辑的数据,删除成功,刷新一次数据库的数据
                Toast.makeText(DiaryActivity.this, "当前没有可编辑的内容", Toast.LENGTH_LONG).show();
                mTimer.cancel();
                mLocationClient.stop();
                break;
            case R.id.add_content:
                //增加便签  暂时这样吧,后面用startforresultactivity,在增加界面保存到数据库回来,刷新一次数据
                Intent intent = new Intent(DiaryActivity.this, MoodLineContentActivity.class);
                startActivity(intent);
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                    isClick = false;
                }
                break;
            case R.id.topRight:
                if (!isClick) {
                    initmPopupWindowView();
                    isClick = true;
                    popupwindow.showAsDropDown(topRight, 5, 30);
                } else {
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        popupwindow = null;
                    }
                    isClick = false;
                    break;
                }
        }
    }

    /**
     * 定位城市  并设置到列表(如果数据库里没有此定位的城市,则新增,如果有则找到指定的,并判断是否有便签,新增默认为无)
     * 定位城市失败默认杭州,可点击选择
     */

    private void getLocationAdress() {
        mLocationClient = ((LocationApplication) getApplication()).mLocationClient;
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
        ((LocationApplication) getApplication()).setCallbackadress(new LocationApplication.Callbackadress() {
            @Override
            public void finish(String address) {
                if (address != null) {
                    mLocationClient.stop();
                    mAddress = address;
                    MoodLineDate moodLineDate = new MoodLineDate();
                    moodLineDate.setCityname(mAddress);
                    mDate.add(moodLineDate);
                    if (mDate.size() > 0) {
                        mAddressAdapter = new AddressAdapter(getApplicationContext(), mDate);
                    }
                    list_address.setAdapter(mAddressAdapter);
                } else {
                    mLocationClient.stop();
                    mAddress = "杭州";
                    MoodLineDate moodLineDate = new MoodLineDate();
                    moodLineDate.setCityname(mAddress);
                    mDate.add(moodLineDate);
                    if (mDate.size() > 0) {
                        mAddressAdapter = new AddressAdapter(getApplicationContext(), mDate);
                    }
                    list_address.setAdapter(mAddressAdapter);
                    Toast.makeText(DiaryActivity.this, "定位失败,已默认城市为杭州,请手动修改城市!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mLocationClient.stop();
    }

    private class AddressAdapter extends BaseAdapter {
        private List<MoodLineDate> mData = new ArrayList<MoodLineDate>();
        private LayoutInflater inflater;

        public AddressAdapter(Context context, List<MoodLineDate> datas) {
            this.mData = datas;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (mData != null && mData.size() > 0) {
                return mData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_moodline_city, parent,
                        false);
                vh = new ViewHolder();
                vh.mCityName = (TextView) convertView.findViewById(R.id.place);
                vh.mPlace_tag = (ImageView) convertView.findViewById(R.id.place_tag);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            MoodLineDate mMoodLineDate = mData.get(position);
            vh.mCityName.setText(mMoodLineDate.getCityname());
            return convertView;
        }

        class ViewHolder {
            private TextView mCityName;
            private ImageView mPlace_tag;
        }
    }

    public void initmPopupWindowView() {
        View customView = getLayoutInflater().inflate(R.layout.popview_item,
                null, false);
        popupwindow = new PopupWindow(customView, 150, 200);
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        /** 在这里可以实现自定义视图的功能 */
        Button editor = (Button) customView.findViewById(R.id.editor);
        Button add_content = (Button) customView.findViewById(R.id.add_content);
        editor.setOnClickListener(this);
        add_content.setOnClickListener(this);
    }
}
