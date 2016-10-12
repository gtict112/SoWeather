package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.sertvice.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.LineGraphicView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements ResponseListenter {
    private View view;
    private Appconfiguration config = Appconfiguration.getInstance();
    private LineGraphicView tu;
    private ArrayList<Double> yList;
    private RadioGroup mSelectType;
    private RadioButton mTypeTime;
    private RadioButton mTypeDally;
    private LinearLayout mMoreTime;
    private ListView mListview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        mSelectType = (RadioGroup) view.findViewById(R.id.select_more_type);
        mTypeTime = (RadioButton) view.findViewById(R.id.type_time);
        mTypeDally = (RadioButton) view.findViewById(R.id.type_dally);
        tu = (LineGraphicView) view.findViewById(R.id.tu);
        mMoreTime = (LinearLayout) view.findViewById(R.id.more_time);
        mListview = (ListView) view.findViewById(R.id.more_dally);
        yList = new ArrayList<Double>();
        yList.add((double) 2.103);
        yList.add(4.05);
        yList.add(6.60);
        yList.add(3.08);
        yList.add(4.32);
        yList.add(2.0);
        yList.add(5.0);

        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("05-19");
        xRawDatas.add("05-20");
        xRawDatas.add("05-21");
        xRawDatas.add("05-22");
        xRawDatas.add("05-23");
        xRawDatas.add("05-24");
        xRawDatas.add("05-25");
        xRawDatas.add("05-26");
        tu.setData(yList, xRawDatas, 8, 2);
        mSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.type_time) {
                    mMoreTime.setVisibility(View.VISIBLE);
                    mListview.setVisibility(View.GONE);
                } else if (checkedId == R.id.type_dally) {
                    mMoreTime.setVisibility(View.GONE);
                    mListview.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void getData() {
        config.showProgressDialog("正在加载", getActivity());
        WeatherService service = new WeatherService();
        service.getWeatherData(this);
    }

    @Override
    public void onReceive(Result result) throws Exception {
        config.dismissProgressDialog();
        if (result.isSuccess()) {
            result.getData();
        } else {
            result.getErrorMessage();
        }
    }
}
