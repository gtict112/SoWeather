package com.example.administrator.soweather.com.example.administrator.soweather.general;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TimeDialogFragment extends DialogFragment {
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private TimeAdapter mTimeAdapter;
    private RecyclerView time_weather;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private ImageView delete;
    private TextView tip;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomDialog dialog = new CustomDialog(getActivity(), R.style.Dialog);
        View layout = inflater.inflate(R.layout.dialog_time, null);
        cityDB = SoWeatherDB.getInstance(getActivity());
        weathimgs = cityDB.getAllWeatherImg();
        init(layout);
        Window window = dialog.getWindow();
        //window.setGravity(Gravity.BOTTOM);
        window.getAttributes().windowAnimations = R.style.dialog_style;
        dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setContentView(layout);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.98), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void init(View view) {
        time_weather = (RecyclerView) view.findViewById(R.id.time_weather);
        delete = (ImageView) view.findViewById(R.id.delete);
        tip = (TextView) view.findViewById(R.id.tip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        time_weather.setLayoutManager(linearLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        time_weather.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        if (getTag().equals("小时预报") && !(getArguments() == null)) {
            time_weather.setVisibility(View.VISIBLE);
            tip.setVisibility(View.GONE);
            mHourlyforecast = (List<Hourlyforecast>) getArguments().getSerializable("date");
            mTimeAdapter = new TimeAdapter();
            mTimeAdapter.notifyDataSetChanged(mHourlyforecast);
            time_weather.setAdapter(mTimeAdapter);
            mTimeAdapter.notifyDataSetChanged(mHourlyforecast);
        }
        if (getArguments() == null) {
            time_weather.setVisibility(View.GONE);
            tip.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialogFragment.this.dismiss();
            }
        });
    }

    public class TimeAdapter extends
            RecyclerView.Adapter<TimeAdapter.ViewHolder> {
        private List<Hourlyforecast> mData = new ArrayList<Hourlyforecast>();

        public void notifyDataSetChanged(List<Hourlyforecast> date) {
            this.mData.clear();
            this.mData.addAll(date);
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView txt_img;
            TextView date;
            TextView tmp;
            TextView decs;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public TimeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_time_weather, viewGroup, false);
            TimeAdapter.ViewHolder viewHolder = new TimeAdapter.ViewHolder(view);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.txt_img = (ImageView) view.findViewById(R.id.txt_img);
            viewHolder.tmp = (TextView) view.findViewById(R.id.tmp);
            viewHolder.decs = (TextView) view.findViewById(R.id.desc);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final TimeAdapter.ViewHolder viewHolder, final int i) {
            Hourlyforecast mTimeWeatherData = mData.get(i);
            viewHolder.date.setText(mTimeWeatherData.date.substring(10, mTimeWeatherData.date.length()));
            String code = null;
            try {
                String min = mTimeWeatherData.tmp;
                viewHolder.tmp.setText(min + "℃");
                code = new JSONObject(mTimeWeatherData.cond).optString("code");
                viewHolder.decs.setText("降水率为" + mTimeWeatherData.pop + " 相对湿度" + mTimeWeatherData.hum + "%");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            weathimgs = cityDB.getAllWeatherImg();
            for (int j = 0; j < weathimgs.size(); j++) {
                if (code.equals(weathimgs.get(j).getCode())) {
                    viewHolder.txt_img.setImageBitmap(weathimgs.get(j).getIcon());
                }
            }
        }
    }

    /**
     * RecyclerView的间隔问题
     */
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0)
                outRect.top = space;
        }
    }
}
