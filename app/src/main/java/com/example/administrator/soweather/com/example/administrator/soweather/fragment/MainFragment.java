package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeatherData;
import com.example.administrator.soweather.com.example.administrator.soweather.sertvice.WeatherService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.view.LineGraphicView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class MainFragment extends Fragment implements ResponseListenter<List<WeatherData>> {
    private View view;
    private Appconfiguration config = Appconfiguration.getInstance();
    private LineGraphicView tu;
    private ArrayList<Double> yList;
    private RadioGroup mSelectType;
    private RadioButton mTypeTime;
    private RadioButton mTypeDally;
    private LinearLayout mMoreTime;
    private ListView mListview;
    private TextView mLiftIndex;
    private PopupWindow popupwindow;
    private RelativeLayout mHead1;
    private LinearLayout mHead2;
    private TextView mP25Num;
    private TextView mP25Name;
    private TextView mTmp;
    private TextView tmp_txt;
    private List<WeatherData> mData = new ArrayList<WeatherData>();
    private Handler mHandler;

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
        Bundle bundle = getArguments();
        initView(view);
        getData();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 111) {
                    init(mData);
                }
            }
        };
        return view;
    }

    private void init(List<WeatherData> mData) {
        config.dismissProgressDialog();
        mP25Name.setText(mData.get(0).qlty);
        mP25Num.setText("Pm25: "+mData.get(0).pm25);
        mTmp.setText(mData.get(0).tmp+"℃");
    }

    private void initView(View view) {
        mSelectType = (RadioGroup) view.findViewById(R.id.select_more_type);
        mTypeTime = (RadioButton) view.findViewById(R.id.type_time);
        mTypeDally = (RadioButton) view.findViewById(R.id.type_dally);
//        tu = (LineGraphicView) view.findViewById(R.id.tu);
        mMoreTime = (LinearLayout) view.findViewById(R.id.more_time);
        mListview = (ListView) view.findViewById(R.id.more_dally);
        mLiftIndex = (TextView) view.findViewById(R.id.life_index);
        mHead1 = (RelativeLayout) view.findViewById(R.id.head1);
        mHead2 = (LinearLayout) view.findViewById(R.id.head2);
        mP25Num = (TextView) view.findViewById(R.id.p25_num);
        mP25Name = (TextView) view.findViewById(R.id.p25_name);
        mTmp =(TextView)view.findViewById(R.id.tmp);
        tmp_txt=(TextView)view.findViewById(R.id.tmp_txt);
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
       // tu.setData(yList, xRawDatas, 8, 2);
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
        mLiftIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    //弹出指数popwod;
                 //   initmPopupWindowView();
                  //  popupwindow.showAtLocation(mLiftIndex, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 170);
                }
            }
        });
    }

    private void initmPopupWindowView() {
        View customView = getActivity().getLayoutInflater().inflate(R.layout.popupwindow_lift_index,
                null, false);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        popupwindow = new PopupWindow(customView, (int) (dm.widthPixels * 0.98), mHead1.getHeight() + mHead2.getHeight() + 50);
        popupwindow.setAnimationStyle(R.style.AnimationFade);
    }


    private void getData() {
        config.showProgressDialog("拼命加载中...", getActivity());
        WeatherService service = new WeatherService();
        service.getWeatherData(this);
    }

    @Override
    public void onReceive(Result<List<WeatherData>> result) throws Exception {
        if (result.isSuccess()) {
            mData = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(111, mData));
        } else {
            result.getErrorMessage();
        }
    }
}
