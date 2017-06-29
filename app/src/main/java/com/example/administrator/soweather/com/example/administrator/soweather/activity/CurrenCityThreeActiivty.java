package com.example.administrator.soweather.com.example.administrator.soweather.activity;

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
import com.example.administrator.soweather.com.example.administrator.soweather.mode.County;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */

public class CurrenCityThreeActiivty extends BaseActivity implements View.OnClickListener {
    private ListView mCountyList;
    private TextView topTv;
    private ImageView topButton;
    private String cityName;
    private CityAdapter mCityAdapter;
    private SoWeatherDB cityDB;
    private List<County> cities = new ArrayList<>();
    private String cityId;
    private String type = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_current_city_three;
    }

    @Override
    protected int getMenuId() {
        return 0;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            cityName = intent.getStringExtra("cityName");
            cityId = intent.getStringExtra("cityId");
            type = intent.getStringExtra("type");
        }
        mCountyList = (ListView) findViewById(R.id.county_list);
        topTv = (TextView) findViewById(R.id.topTv);
        topTv.setText("选择城市");
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(this);
        cityDB = SoWeatherDB.getInstance(this);
        cities = cityDB.getAllCountry(cityName);
        if (cities.size() > 0) {
            mCityAdapter = new CityAdapter(getApplicationContext(), this.cities);
            mCountyList.setAdapter(mCityAdapter);
            mCityAdapter.notifyDataSetChanged();
            mCountyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (type != null && type.equals(CurrentCityActivity.TYPE)) {
                        saveDB(cityId, cityName);
                    } else if (type == null) {
                        Intent intent = new Intent(CurrenCityThreeActiivty.this, MainActivity.class);
                        intent.putExtra("cityid", cityId);
                        intent.putExtra("city", cityName);
                        startActivity(intent);
                    }
                    CurrentCityActivity.instance.finish();
                    CurrenCityTwoActivity.instance.finish();
                    finish();
                    overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                }
            });
        }
    }

    private class CityAdapter extends BaseAdapter {
        private List<County> mData = new ArrayList<County>();
        private LayoutInflater inflater;

        public CityAdapter(Context context, List<County> datas) {
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
            final CurrenCityThreeActiivty.CityAdapter.ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_city, parent,
                        false);
                vh = new CurrenCityThreeActiivty.CityAdapter.ViewHolder();
                vh.mCityName = (TextView) convertView.findViewById(R.id.city_name);
                convertView.setTag(vh);
            } else {
                vh = (CurrenCityThreeActiivty.CityAdapter.ViewHolder) convertView.getTag();
            }
            County mCountyData = mData.get(position);
            vh.mCityName.setText(mCountyData.getCountyName());
            return convertView;
        }

        class ViewHolder {
            private TextView mCityName;
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

    private void saveDB(String cityId, String cityName) {
        final SoWeatherDB cityDB = SoWeatherDB.getInstance(getApplicationContext());
        cityDB.savaManagecity(cityId, cityName);
    }
}
