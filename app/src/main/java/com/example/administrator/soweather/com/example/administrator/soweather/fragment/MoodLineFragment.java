package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MainActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MoodLineContentActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.LocationApplication;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.MoodLineDate;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.view.HeartLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/28.
 */

public class MoodLineFragment extends Fragment implements View.OnClickListener {
    private HeartLayout heart_layout;
    private Random mRandom = new Random();
    private Timer mTimer = new Timer();
    private ListView list_address;
    private ListView list_content;
    private LocationClient mLocationClient;//定位SDK的核心类
    private String mAddress;
    private List<MoodLineDate> mDate = new ArrayList<>();
    private List<MoodLineDate.Content> mContentDate = new ArrayList<>();
    private AddressAdapter mAddressAdapter;
    private Boolean isClick = false;
    private PopupWindow popupwindow;
    private Boolean isColse = true;

    //内容列表数据点击,进入另一个界面,不同于增加界面,类似于修改
    //城市列表数据,只有重新获取定位城市才可以手动选择修改城市,其它不可以,进来,只有位置发生了改变了,才会重新定位,区间"市"
    //       private TextureMapView map;
    //     private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.place);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_line, null);
        initView(view);
        getLocationAdress();
        getCallbackadress();
        return view;
    }

    private void initView(View view) {
        heart_layout = (HeartLayout) view.findViewById(R.id.heart_layout);
        list_address = (ListView) view.findViewById(R.id.list_address);
        list_content = (ListView) view.findViewById(R.id.list_content);
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
        isColse = getArguments().getBoolean("isClose");
    }


    /**
     * 定位城市  并设置到列表(如果数据库里没有此定位的城市,则新增,如果有则找到指定的,并判断是否有便签,新增默认为无)
     * 定位城市失败默认杭州,可点击选择
     */
    private void getLocationAdress() {
        mLocationClient = ((LocationApplication) getActivity().getApplication()).mLocationClient;
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
        ((LocationApplication) getActivity().getApplication()).setCallbackadress(new LocationApplication.Callbackadress() {
            @Override
            public void finish(String address) {
                if (address != null) {
                    mLocationClient.stop();
                    mAddress = address;
                    MoodLineDate moodLineDate = new MoodLineDate();
                    moodLineDate.setCityname(mAddress);
                    mDate.add(moodLineDate);
                    if (mDate.size() > 0) {
                        mAddressAdapter = new AddressAdapter(getActivity().getApplicationContext(), mDate);
                    }
                    list_address.setAdapter(mAddressAdapter);
                } else {
                    mLocationClient.stop();
                    mAddress = "杭州";
                    MoodLineDate moodLineDate = new MoodLineDate();
                    moodLineDate.setCityname(mAddress);
                    mDate.add(moodLineDate);
                    if (mDate.size() > 0) {
                        mAddressAdapter = new AddressAdapter(getActivity().getApplicationContext(), mDate);
                    }
                    list_address.setAdapter(mAddressAdapter);
                    Toast.makeText(getActivity(), "定位失败,已默认城市为杭州,请手动修改城市!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mLocationClient.stop();
    }

    private int randomColor() {
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }

    //            double lat = Double.parseDouble(mNowWeather.lat);
    //            double lon = Double.parseDouble(mNowWeather.lon);
    //            final BaiduMap baiduMap = map.getMap();
    //            LatLng latLng = null;
    //            OverlayOptions overlayOptions = null;
    //            Marker marker = null;
    //            latLng = new LatLng(lat, lon);
    //            // 图标
    //            overlayOptions = new MarkerOptions().position(latLng)
    //                    .icon(bitmap).zIndex(5);
    //            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
    //            MapStatusUpdate cenptmsu = MapStatusUpdateFactory.newLatLng(new LatLng(lat, lon));
    //            baiduMap.setMapStatus(cenptmsu);
    //            baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
    //                @Override
    //                public boolean onMarkerClick(Marker marker) {
    //                    InfoWindow mInfoWindow;
    //                    TextView location = new TextView(getActivity().getApplicationContext());
    //                    location.setPadding(10, 10, 5, 20);
    //                    location.setTextColor(getActivity().getResources().getColor(R.color.background_progress));
    //                    location.setTextSize(12);
    //                    location.setText("小背熊提醒您:待真机测试,模拟器无法定位到精确位置!");
    //                    final LatLng ll = marker.getPosition();
    //                    android.graphics.Point p = baiduMap.getProjection().toScreenLocation(ll);
    //                    p.y -= 47;
    //                    LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
    //                    mInfoWindow = new InfoWindow(location, llInfo, 2);
    //                    //                    mInfoWindow = new InfoWindow(location, llInfo, 2, new InfoWindow.OnInfoWindowClickListener() {
    //                    //                        @Override
    //                    //                        public void onInfoWindowClick() {
    //                    //                            baiduMap.hideInfoWindow();
    //                    //                        }
    //                    //                    });
    //                    baiduMap.showInfoWindow(mInfoWindow);
    //                    return true;
    //                }
    //            });
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

    private void getCallbackadress() {
        MainActivity mMainActivity = (MainActivity) getActivity();
        mMainActivity.setCallbackadress(new MainActivity.CallbackMoodline() {
            @Override
            public void finish(Boolean title, View view) {
                isClick = title;
                if (isClick && view != null) {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(view, 0, 30);
                } else if (!isClick && view != null) {
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        popupwindow = null;
                    }
                } else if (!isColse) {
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        popupwindow = null;
                        mTimer.cancel();
                        mLocationClient.stop();
                    }
                    isClick = false;
                    return;
                }
            }
        });
    }

    public void initmPopupWindowView() {
        View customView = getActivity().getLayoutInflater().inflate(R.layout.popview_item,
                null, false);
        popupwindow = new PopupWindow(customView, 150, 200);
        popupwindow.setAnimationStyle(R.style.AnimationFade);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editor:
                //编辑功能,删除想要删除的数据,没有数据提示当前暂没有可编辑的数据,删除成功,刷新一次数据库的数据
                Toast.makeText(getActivity(), "当前没有可编辑的内容", Toast.LENGTH_LONG).show();
                mTimer.cancel();
                mLocationClient.stop();
                break;
            case R.id.add_content:
                //增加便签  暂时这样吧,后面用startforresultactivity,在增加界面保存到数据库回来,刷新一次数据
                Intent intent = new Intent(getActivity(), MoodLineContentActivity.class);
                startActivity(intent);
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                    isClick = false;
                }
                mTimer.cancel();
                mLocationClient.stop();
                break;
            default:
                break;
        }
    }
}
