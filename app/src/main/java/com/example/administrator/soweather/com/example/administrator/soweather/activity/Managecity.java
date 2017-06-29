package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.fragment.MainFragment;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.ManageCity;
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
 * Created by Administrator on 2016/11/28.
 */

public class Managecity extends BaseActivity implements ResponseListenter<NowWeather>, View.OnClickListener {
    private GridView list;
    private SoWeatherDB cityDB;
    private List<ManageCity> citylist = new ArrayList<>();//数据库添加的城市
    private List<NowWeather> date = new ArrayList<>();
    private Handler mHandler;
    private NowWeather mNowWeather = new NowWeather();
    private GalleryAdapter mDailyAdapter;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private TextView topTv;
    private ImageView topButton;
    private TextView topRight;
    //    private Boolean isDelete = false;
    private LinearLayout bg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_city;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        cityDB = SoWeatherDB.getInstance(this);
        initView();
        getData(citylist);
        getHandleMessge();
    }


    private void initView() {
        list = (GridView) findViewById(R.id.list);
        topTv = (TextView) findViewById(R.id.topTv);
        topRight = (TextView) findViewById(R.id.topRight);
        topRight.setVisibility(View.GONE);
        topTv.setText("多城市管理");
        topButton = (ImageView) findViewById(R.id.topButton);
        bg = (LinearLayout) findViewById(R.id.bg);
        topButton.setOnClickListener(this);
        topRight.setOnClickListener(this);
        citylist = cityDB.getAllManagecity();
        weathimgs = cityDB.getAllWeatherImg();
        mDailyAdapter = new GalleryAdapter(this, date);
        list.setAdapter(mDailyAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Managecity.this, MainActivity.class);
                intent.putExtra("cityid", citylist.get(position).getCityId());
                intent.putExtra("city", citylist.get(position).getCityName());
                startActivity(intent);
                finish();
            }
        });
    }

    private void getData(List<ManageCity> citylist) {
        if (citylist != null && citylist.size() > 0) {
            for (int i = 0; i < citylist.size(); i++) {
                WeatherService service = new WeatherService();
                service.getNowWeatherData(this, citylist.get(i).getCityId());
            }
        }
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    setDate();
                }
            }
        };
    }

    private void setDate() {
        mDailyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReceive(Result<NowWeather> result) {
        if (result.isSuccess()) {
            mNowWeather = result.getData();
            date.add(mNowWeather);
            mHandler.sendMessage(mHandler.obtainMessage(1, date));
        } else {
            result.getErrorMessage();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.topRight:
                break;
        }
    }


    public class GalleryAdapter extends BaseAdapter {
        private List<NowWeather> mData = new ArrayList<NowWeather>();
        private LayoutInflater inflater;

        public GalleryAdapter(Context context, List<NowWeather> datats) {
            inflater = LayoutInflater.from(context);
            mData = datats;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_manage_city, parent,
                        false);
                vh = new ViewHolder();

                vh.address = (TextView) convertView.findViewById(R.id.address);
                vh.weather_img = (ImageView) convertView.findViewById(R.id.weather_img);
                vh.tmp = (TextView) convertView.findViewById(R.id.tmp);
                vh.cond_txt = (TextView) convertView.findViewById(R.id.cond_txt);
                vh.bg = (LinearLayout) convertView.findViewById(R.id.bg);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final NowWeather mNowWeather = mData.get(position);
            vh.address.setText(mNowWeather.cnty + mNowWeather.city);
            vh.tmp.setText(mNowWeather.tmp + "℃");
            String code = null;
            try {
                code = new JSONObject(mNowWeather.cond).optString("code");
                vh.cond_txt.setText(new JSONObject(mNowWeather.cond).optString("txt"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setItemBg(mNowWeather.city, vh.cond_txt.getText().toString(), vh.bg);
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    vh.weather_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
//            if (isDelete) {
//                vh.delete.setVisibility(View.VISIBLE);
//                Animation anim1 = AnimationUtils.loadAnimation(Managecity.this, R.anim.dialog_out);
//                vh.delete.startAnimation(anim1);
//                Animation anim2 = AnimationUtils.loadAnimation(Managecity.this, R.anim.dialog_in);
//                vh.delete.startAnimation(anim2);
//                vh.delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        cityDB.deleteManagecity(mNowWeather.id);//删除数据库数据
//                        citylist = cityDB.getAllManagecity();
//                        date.clear();
//                        getData(citylist);
//                    }
//                });
//            }
//            if (!isDelete) {
//                vh.delete.setVisibility(View.GONE);
//            }
            return convertView;
        }

        class ViewHolder {
            ImageView weather_img;
            TextView address;
            TextView tmp;
            TextView cond_txt;
            LinearLayout bg;
        }
    }

    private void setItemBg(String city, String cond_txt, LinearLayout bg) {
        Animation anim1 = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        bg.startAnimation(anim1);
        if (city.contains("北京")) {
            if (cond_txt.contains("云")) {
                bg.setBackgroundResource(R.mipmap.city_beijing_cloudy);
            } else if (cond_txt.contains("雨")) {
                bg.setBackgroundResource(R.mipmap.city_beijing_rainy);
            } else {
                bg.setBackgroundResource(R.mipmap.city_beijing_sunny);
            }
        } else if (city.contains("上海")) {
            if (cond_txt.contains("云")) {
                bg.setBackgroundResource(R.mipmap.city_shanghai_cloudy);
            } else if (cond_txt.contains("雨")) {
                bg.setBackgroundResource(R.mipmap.city_shanghai_rainy);
            } else {
                bg.setBackgroundResource(R.mipmap.city_shanghai_sunny);
            }
        } else if (city.contains("苏州")) {
            if (cond_txt.contains("云")) {
                bg.setBackgroundResource(R.mipmap.city_suzhou_cloudy);
            } else if (cond_txt.contains("雨")) {
                bg.setBackgroundResource(R.mipmap.city_suzhou_rain);
            } else {
                bg.setBackgroundResource(R.mipmap.city_suzhou_sunny);
            }
        } else {
            if (cond_txt.contains("云")) {
                bg.setBackgroundResource(R.mipmap.city_other_cloudy);
            } else if (cond_txt.contains("雨")) {
                bg.setBackgroundResource(R.mipmap.city_other_rainy);
            } else {
                bg.setBackgroundResource(R.mipmap.city_other_sunny);
            }
        }
    }

}
