package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.MoreNewsActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.NewsDetailActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.db.SoWeatherDB;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Hourlyforecast;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.WeathImg;
import com.example.administrator.soweather.com.example.administrator.soweather.service.News;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TimeDialogFragment extends Fragment implements View.OnClickListener {
    private List<Hourlyforecast> mHourlyforecast = new ArrayList<>();
    private TimeAdapter mTimeAdapter;
    private NewsAdapter mNewsAdapter;
    private RecyclerView time_weather;
    private SoWeatherDB cityDB;
    private List<WeathImg> weathimgs = new ArrayList<>();
    private ListView recommended;
    private List<TopNew> mNewDate = new ArrayList<>();
    private Handler mHandler;
    private int mDrawable[] = {R.drawable.bg_shape_a1, R.drawable.bg_shape_a2, R.drawable.bg_shape_a3, R.drawable.bg_shape_a4, R.drawable.bg_shape_a5};
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView more;
    private ImageView set;
    private Boolean isOnclick = true;
    private TextView tip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time, null);
        cityDB = SoWeatherDB.getInstance(getActivity());
        weathimgs = cityDB.getAllWeatherImg();
        init(view);
        getDate();
        getHandleMessge();
        return view;
    }

    private void init(View view) {
        time_weather = (RecyclerView) view.findViewById(R.id.time_weather);
        recommended = (ListView) view.findViewById(R.id.recommended);
        more = (TextView) view.findViewById(R.id.more);
        set = (ImageView) view.findViewById(R.id.set);
        tip = (TextView) view.findViewById(R.id.tip);
        more.setOnClickListener(this);
        set.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        time_weather.setLayoutManager(linearLayoutManager);
        time_weather.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        if (getArguments() != null) {
            mHourlyforecast = (List<Hourlyforecast>) getArguments().getSerializable("date");
            mTimeAdapter = new TimeAdapter();
            time_weather.setAdapter(mTimeAdapter);
            mTimeAdapter.notifyDataSetChanged(mHourlyforecast);
        }
        recommended.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //资讯详情
                if (mNewDate != null && mNewDate.size() > 0) {
                    String url = mNewDate.get(position).url;
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }
        });
        if (isOnclick) {
            set.setImageResource(R.mipmap.up);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more:
                //新闻资讯界面
                Intent intent = new Intent(getActivity(), MoreNewsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.dialog_in, R.anim.dialog_out);
                break;
            case R.id.set:
                if (isOnclick) {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
                    animation.setFillAfter(true);
                    isOnclick = false;
                    set.setImageResource(R.mipmap.down);
                    time_weather.setVisibility(View.VISIBLE);
                    time_weather.startAnimation(animation);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.dialog_in);
                    animation.setFillAfter(true);
                    isOnclick = true;
                    set.setImageResource(R.mipmap.up);
                    time_weather.setVisibility(View.GONE);
                    time_weather.startAnimation(animation);
                }
                break;
        }
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


    private void getDate() {
        config.showProgressDialog("拼命加载中....", getActivity());
        News mNews = new News();
        mNews.getTopNews(new ResponseListenter<List<TopNew>>() {
            @Override
            public void onReceive(Result<List<TopNew>> result) {
                if (result.isSuccess()) {
                    mNewDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mNewDate));
                } else {
                    config.dismissProgressDialog();
                    tip.setVisibility(View.VISIBLE);
                    recommended.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        setNews(mNewDate);
                        break;
                }
            }
        };
    }

    private void setNews(List<TopNew> mNewDate) {
        mNewsAdapter = new NewsAdapter(getActivity().getApplicationContext(), mNewDate);
        recommended.setAdapter(mNewsAdapter);
        config.dismissProgressDialog();
    }


    public class NewsAdapter extends BaseAdapter {
        private List<TopNew> mData = new ArrayList<TopNew>();
        private LayoutInflater inflater;

        public NewsAdapter(Context context, List<TopNew> datas) {
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
                convertView = inflater.inflate(R.layout.item_news, parent,
                        false);
                vh = new ViewHolder();
                vh.realtype = (TextView) convertView.findViewById(R.id.realtype);
                vh.tab = (LinearLayout) convertView.findViewById(R.id.tab);
                vh.title = (TextView) convertView.findViewById(R.id.content);
                vh.date = (TextView) convertView.findViewById(R.id.date);
                vh.author_name = (TextView) convertView.findViewById(R.id.author_name);
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            TopNew mNew = mData.get(position);
            if (mNew.realtype != null) {
                vh.realtype.setText(mNew.realtype);
                int rand = (int) Math.round(Math.random() * 4);
                vh.tab.setBackgroundResource(mDrawable[rand]);
            } else {
                vh.realtype.setVisibility(View.GONE);
                vh.tab.setVisibility(View.GONE);
            }
            vh.img.setImageBitmap(mNew.img);
            vh.title.setText(mNew.title);
            vh.date.setText(mNew.date);
            vh.author_name.setText(mNew.author_name);
            return convertView;
        }

        class ViewHolder {
            private TextView realtype;
            private LinearLayout tab;
            private TextView title;
            private ImageView img;
            private TextView date;
            private TextView author_name;
        }
    }
}
