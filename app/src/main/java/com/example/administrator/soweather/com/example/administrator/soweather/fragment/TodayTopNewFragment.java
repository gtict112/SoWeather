package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.NewsDetailActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.service.News;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.base.BaseSkinFragment;

/**
 * Created by Administrator on 2017/5/27.
 */

public class TodayTopNewFragment extends BaseSkinFragment implements SwipeRefreshLayout.OnRefreshListener {
    private Appconfiguration config = Appconfiguration.getInstance();
    private ListView recommended;
    private List<TopNew> mNewDate = new ArrayList<>();
    private Handler mHandler;
    private NewsAdapter mNewsAdapter;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_topnew, null);
        initView(view);
        getDate();
        getHandleMessge();
        return view;
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
        mNewsAdapter = new NewsAdapter(getActivity(), mNewDate);
        recommended.setAdapter(mNewsAdapter);
        config.dismissProgressDialog();
    }

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
        getDate();
    }

    @Override
    public void onRefresh() {
        refresh();
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
            final TodayTopNewFragment.NewsAdapter.ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, parent,
                        false);
                vh = new TodayTopNewFragment.NewsAdapter.ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.content);
                vh.date = (TextView) convertView.findViewById(R.id.date);
                vh.author_name = (TextView) convertView.findViewById(R.id.author_name);
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            } else {
                vh = (TodayTopNewFragment.NewsAdapter.ViewHolder) convertView.getTag();
            }
            TopNew mNew = mData.get(position);
            vh.img.setImageBitmap(mNew.img);
            vh.title.setText(mNew.title);
            vh.date.setText(mNew.date);
            vh.author_name.setText(mNew.author_name);
            return convertView;
        }

        class ViewHolder {
            private TextView title;
            private ImageView img;
            private TextView date;
            private TextView author_name;
        }
    }

    private void initView(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
        recommended = (ListView) view.findViewById(R.id.recommended);
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
                    recommended.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}