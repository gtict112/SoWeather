package com.lhlSo.soweather.widget;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lhlSo.soweather.R;
import com.lhlSo.soweather.bean.WeathImg;
import com.lhlSo.soweather.utils.DateToWeek;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherChartView extends LinearLayout {

    private List<Dailyforecast> dailyForecastList = new ArrayList<>();
    private List<WeathImg> weathimgs = new ArrayList<>();
    LinearLayout.LayoutParams cellParams;
    LinearLayout.LayoutParams rowParams;
    LinearLayout.LayoutParams chartParams;

    LayoutTransition transition = new LayoutTransition();

    public WeatherChartView(Context context) {
        this(context, null);
    }

    public WeatherChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeatherChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        transition.enableTransitionType(LayoutTransition.APPEARING);
        this.setLayoutTransition(transition);
        rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        cellParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        chartParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(getContext(), 200));
    }

    private void letItGo() {
        removeAllViews();
        final LinearLayout dateTitleView = new LinearLayout(getContext());
        dateTitleView.setLayoutParams(rowParams);
        dateTitleView.setOrientation(HORIZONTAL);
        dateTitleView.setLayoutTransition(transition);
        dateTitleView.removeAllViews();

        final LinearLayout iconView = new LinearLayout(getContext());
        iconView.setLayoutParams(rowParams);
        iconView.setOrientation(HORIZONTAL);
        iconView.setLayoutTransition(transition);
        iconView.removeAllViews();

        final LinearLayout weatherStrView = new LinearLayout(getContext());
        weatherStrView.setLayoutParams(rowParams);
        weatherStrView.setOrientation(HORIZONTAL);
        weatherStrView.setLayoutTransition(transition);
        weatherStrView.removeAllViews();

        List<Integer> minTemp = new ArrayList<>();
        List<Integer> maxTemp = new ArrayList<>();
        for (int i = 0; i < dailyForecastList.size(); i++) {
            final TextView tvDate = new TextView(getContext());
            tvDate.setGravity(Gravity.CENTER);
            tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tvDate.setTextColor(getResources().getColor(R.color.colorTextDark2nd));
            tvDate.setVisibility(INVISIBLE);
            final TextView tvWeather = new TextView((getContext()));
            tvWeather.setGravity(Gravity.CENTER);
            tvWeather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            tvWeather.setTextColor(getResources().getColor(R.color.colorTextDark2nd));
            tvWeather.setVisibility(INVISIBLE);
            final ImageView ivIcon = new ImageView(getContext());
            ivIcon.setAdjustViewBounds(true);
            ivIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int padding = dp2px(getContext(), 10);
            int width = getScreenWidth(getContext()) / dailyForecastList.size();
            LayoutParams ivParam = new LayoutParams(width, width);
            ivParam.weight = 1;
            ivIcon.setLayoutParams(ivParam);
            ivIcon.setPadding(padding, padding, padding, padding);
            ivIcon.setVisibility(INVISIBLE);
            if (i == 0) {
                tvDate.setText("今天");
            } else if (i == 1) {
                tvDate.setText("明天");
            } else {
                tvDate.setText(DateToWeek.getWeek(dailyForecastList.get(i).date));
            }
            try {
                tvWeather.setText(new JSONObject(dailyForecastList.get(i).cond).optString("txt_d"));
                String code = new JSONObject(dailyForecastList.get(i).cond).optString("code_d");
                for (int j = 0; j < weathimgs.size(); j++) {
                    if (code.equals(weathimgs.get(j).getCode())) {
                        Glide.with(getContext()).load(weathimgs.get(j).getIcon_url()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivIcon);
                    }
                }
                String min = new JSONObject(dailyForecastList.get(i).tmp).optString("min");
                String max = new JSONObject(dailyForecastList.get(i).tmp).optString("max");
                minTemp.add(Integer.valueOf(min));
                maxTemp.add(Integer.valueOf(max));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weatherStrView.addView(tvWeather, cellParams);
            dateTitleView.addView(tvDate, cellParams);
            iconView.addView(ivIcon);
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvDate.setVisibility(VISIBLE);
                    tvWeather.setVisibility(VISIBLE);
                    ivIcon.setVisibility(VISIBLE);
                }
            }, 200 * i);
        }
        addView(dateTitleView);
        addView(iconView);
        addView(weatherStrView);
        final ChartView chartView = new ChartView(getContext());
        chartView.setData(minTemp, maxTemp);
        chartView.setPadding(0, dp2px(getContext(), 16), 0, dp2px(getContext(), 16));
        addView(chartView, chartParams);
    }

    public void setWeather5(List<Dailyforecast> mDailyforecast, List<WeathImg> mWeathImg) {
        dailyForecastList.clear();
        dailyForecastList.addAll(mDailyforecast);
        weathimgs.clear();
        weathimgs.addAll(mWeathImg);
        letItGo();
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int getScreenWidth(Context context) {
        int screenWidth = 0;
        if (screenWidth != 0)
            return screenWidth;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        screenWidth = dm.widthPixels;
        return screenWidth;
    }
}
