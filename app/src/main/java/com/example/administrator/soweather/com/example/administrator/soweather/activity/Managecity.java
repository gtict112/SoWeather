package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
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

public class Managecity extends Activity implements ResponseListenter<NowWeather>, View.OnClickListener {
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
    private Boolean isDelete = false;
    private LinearLayout bg;
    private int mDrawable[] = {R.drawable.bg_shape_a1, R.drawable.bg_shape_a2, R.drawable.bg_shape_a3, R.drawable.bg_shape_a4, R.drawable.bg_shape_a5};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_city);
        cityDB = SoWeatherDB.getInstance(this);
        initView();
        getData(citylist);
        getHandleMessge();
    }

    private void initView() {
        list = (GridView) findViewById(R.id.list);
        topTv = (TextView) findViewById(R.id.topTv);
        topRight = (TextView) findViewById(R.id.topRight);
        topRight.setVisibility(View.VISIBLE);
        topTv.setText("城市管理");
        topButton = (ImageView) findViewById(R.id.topButton);
        bg = (LinearLayout) findViewById(R.id.bg);
        topButton.setOnClickListener(this);
        topRight.setOnClickListener(this);
        citylist = cityDB.getAllManagecity();
        weathimgs = cityDB.getAllWeatherImg();
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
                    setDate(date);
                }
            }
        };
    }

    private void setDate(List<NowWeather> date) {
        mDailyAdapter = new GalleryAdapter(this, date);
        list.setAdapter(mDailyAdapter);
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
                if (!isDelete) {
                    isDelete = true;
                    mDailyAdapter = new GalleryAdapter(this, date);
                    list.setAdapter(mDailyAdapter);
                    topRight.setText("取消");
                } else if (isDelete) {
                    isDelete = false;
                    mDailyAdapter = new GalleryAdapter(this, date);
                    list.setAdapter(mDailyAdapter);
                    topRight.setText("编辑");
                }
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
                vh.delete = (ImageView) convertView.findViewById(R.id.delete);
                vh.bg = (LinearLayout) convertView.findViewById(R.id.bg);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final NowWeather mNowWeather = mData.get(position);
            vh.address.setText(mNowWeather.cnty + mNowWeather.city);
            vh.tmp.setText(mNowWeather.tmp + "℃");
            int rand = (int) Math.round(Math.random() * 4);
            vh.bg.setBackgroundResource(mDrawable[rand]);
            String code = null;
            try {
                code = new JSONObject(mNowWeather.cond).optString("code");
                vh.cond_txt.setText(new JSONObject(mNowWeather.cond).optString("txt") + "(今)");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    vh.weather_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
            if (isDelete) {
                vh.delete.setVisibility(View.VISIBLE);
                Animation anim1 = AnimationUtils.loadAnimation(Managecity.this, R.anim.dialog_out);
                vh.delete.startAnimation(anim1);
                Animation anim2 = AnimationUtils.loadAnimation(Managecity.this, R.anim.dialog_in);
                vh.delete.startAnimation(anim2);
                vh.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cityDB.deleteManagecity(mNowWeather.id);//删除数据库数据
                        citylist = cityDB.getAllManagecity();
                        date.clear();
                        getData(citylist);
                    }
                });
            }
            if (!isDelete) {
                vh.delete.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView weather_img;
            TextView address;
            TextView tmp;
            TextView cond_txt;
            ImageView delete;
            LinearLayout bg;
        }
    }


}
