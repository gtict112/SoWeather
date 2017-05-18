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
import com.example.administrator.soweather.com.example.administrator.soweather.core.Constans;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Province;
import com.example.administrator.soweather.com.example.administrator.soweather.view.Xcflowlayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.*;
import static android.view.ViewGroup.LayoutParams.*;

/**
 * Created by Administrator on 2016/10/13.
 */
public class CurrentCityActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mBack;
    private TextView mTitle;
    private Xcflowlayout mFlowLayout;
    private ListView mCityList;
    private CityAdapter mCityAdapter;
    private String cityid;
    private String cityname;
    private String mNames[] = {
            "北京", "上海", "广州",
            "深圳", "杭州", "南京",
            "天津", "武汉", "长沙",
            "重庆", "温州"         //热门城市
    };
    private int mColor[] = {R.color.a1, R.color.a2, R.color.a3, R.color.a4, R.color.a5, R.color.a6, R.color.a7};
    private SoWeatherDB cityDB;
    private List<Province> provinces = new ArrayList<>();
    public static CurrentCityActivity instance;
    private String type = null;
    public static String TYPE = "leftfragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_city);
        instance = this;
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
        }
        initView();
        initHotWord();
    }


    private void initView() {
        mBack = (ImageView) findViewById(R.id.topButton);
        mTitle = (TextView) findViewById(R.id.topTv);
        mTitle.setText("选择城市");
        mBack.setOnClickListener(this);
        mCityList = (ListView) findViewById(R.id.city_list);
        cityDB = SoWeatherDB.getInstance(this);
        provinces = cityDB.getAllProvince();
        if (provinces.size() > 0) {
            mCityAdapter = new CityAdapter(getApplicationContext(), this.provinces);
        }
        mCityList.setAdapter(mCityAdapter);
        mCityAdapter.notifyDataSetChanged();
        mCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CurrentCityActivity.this, CurrenCityTwoActivity.class);
                intent.putExtra("provinceName", provinces.get(position).getProvinceName());
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right, R.anim.activity_left);
            }
        });
    }


    private void initHotWord() {
        mFlowLayout = (Xcflowlayout) findViewById(R.id.flowlayout);
        MarginLayoutParams lp = new MarginLayoutParams(
                WRAP_CONTENT, WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for (int i = 0; i < mNames.length; i++) {
            final TextView view = new TextView(this);
            view.setText(mNames[i]);
            int rand = (int) Math.round(Math.random() * 6);
            view.setTextColor(this.getResources().getColor(mColor[rand]));
            view.setPadding(10, 3, 10, 3);
            view.setBackgroundResource(R.drawable.selector_bg_edittext);
            mFlowLayout.addView(view, lp);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (provinces.size() > 0) {
                        cityname = view.getText().toString();
                        Constans.HotWord citys[] = Constans.HotWord.values();
                        for (Constans.HotWord cu : citys) {
                            if (cityname.equals(cu.getName())) {
                                cityid = cu.getCityId();
                                cityname = cu.getName();
                            }
                        }
                    }
                    if (type != null && type.equals(TYPE)) {
                        saveDB(cityid, cityname);
                    } else if (type == null) {
                        Intent intent = new Intent(CurrentCityActivity.this, MainActivity.class);
                        intent.putExtra("cityid", cityid);
                        intent.putExtra("city", cityname);
                        startActivity(intent);
                    }
                    finish();
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
            default:
                break;
        }

    }

    private class CityAdapter extends BaseAdapter {
        private List<Province> mData = new ArrayList<Province>();
        private LayoutInflater inflater;

        public CityAdapter(Context context, List<Province> datas) {
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
                convertView = inflater.inflate(R.layout.item_city, parent,
                        false);
                vh = new ViewHolder();
                vh.mCityName = (TextView) convertView.findViewById(R.id.city_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            Province mProvinceData = mData.get(position);
            vh.mCityName.setText(mProvinceData.getProvinceName());
            return convertView;
        }

        class ViewHolder {
            private TextView mCityName;
        }
    }

    /**
     * 保存管理城市选择的城市数据
     */
    private void saveDB(String cityid, String cityname) {
        final SoWeatherDB cityDB = SoWeatherDB.getInstance(getApplicationContext());
        cityDB.savaManagecity(cityid, cityname);
    }
}
