package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class CurrenCityTwoActivity extends BaseActivity implements View.OnClickListener {
    private ListView mListView;
    private String provinceName;
    private CityAdapter mCityAdapter;
    private SoWeatherDB cityDB;
    private List<City> cities = new ArrayList<>();
    private TextView topTv;
    private ImageView topButton;
    public static CurrenCityTwoActivity instance;
    private String type = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curren_city_two);
        instance = this;
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            provinceName = intent.getStringExtra("provinceName");
            type = intent.getStringExtra("type");
        }
        mListView = (ListView) findViewById(R.id.city_list);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("选择城市");
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        cityDB = SoWeatherDB.getInstance(this);
        cities = cityDB.getAllCity(provinceName);
        if (cities.size() > 0) {
            mCityAdapter = new CityAdapter(getApplicationContext(), this.cities);
            mListView.setAdapter(mCityAdapter);
            mCityAdapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CurrenCityTwoActivity.this, CurrenCityThreeActiivty.class);
                    intent.putExtra("cityName", cities.get(position).getCityName());
                    intent.putExtra("cityId", cities.get(position).getCityId());
                    intent.putExtra("type", type);
                    startActivity(intent);
                    overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topButton:
                finish();
                overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
        }
    }

    private class CityAdapter extends BaseAdapter {
        private List<City> mData = new ArrayList<City>();
        private LayoutInflater inflater;

        public CityAdapter(Context context, List<City> datas) {
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
            final CurrenCityTwoActivity.CityAdapter.ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_city, parent,
                        false);
                vh = new CurrenCityTwoActivity.CityAdapter.ViewHolder();
                vh.mCityName = (TextView) convertView.findViewById(R.id.city_name);
                convertView.setTag(vh);
            } else {
                vh = (CurrenCityTwoActivity.CityAdapter.ViewHolder) convertView.getTag();
            }
            City mCityData = mData.get(position);
            vh.mCityName.setText(mCityData.getCityName());
            return convertView;
        }

        class ViewHolder {
            private TextView mCityName;
        }
    }
}
