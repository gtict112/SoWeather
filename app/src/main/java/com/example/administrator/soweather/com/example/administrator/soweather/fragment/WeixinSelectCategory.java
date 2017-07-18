package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.service.News;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.WebUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class WeixinSelectCategory extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private List<TopNew> mNewDate = new ArrayList<>();
    private Appconfiguration config = Appconfiguration.getInstance();
    private ListView recommended;
    private Handler mHandler;
    private NewsAdapter mNewsAdapter;
    private String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weixinselect_category, null);
        initView(view);
        initDate();
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


    private void initView(View view) {
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        recommended = (ListView) view.findViewById(R.id.recommended);
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            title = mBundle.getString("title");
        }
    }

    private void getDate() {
        News mNews = new News();
        mNews.getWeiXinSelect(new ResponseListenter<List<TopNew>>() {
            @Override
            public void onReceive(Result<List<TopNew>> result) {
                if (result.isSuccess()) {
                    mNewDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mNewDate));
                } else {
                    checkError(result.getErrorMessage());
                }
            }
        }, title);
    }

    private void checkError(String errorMessage) {
        config.dismissProgressDialog();
        Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_LONG)
                .setAction("重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initDate();
                    }
                })
                .show();
    }

    private void initDate() {
        config.showProgressDialog("拼命加载中....", getActivity());
        getDate();
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
        private Context context;

        public NewsAdapter(Context context, List<TopNew> datas) {
            this.mData = datas;
            this.context = context;
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
            final NewsAdapter.ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, parent,
                        false);
                vh = new NewsAdapter.ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.content);
                vh.date = (TextView) convertView.findViewById(R.id.date);
                vh.author_name = (TextView) convertView.findViewById(R.id.author_name);
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                vh.item_news_layout = (LinearLayout) convertView.findViewById(R.id.item_news_layout);
                convertView.setTag(vh);
            } else {
                vh = (NewsAdapter.ViewHolder) convertView.getTag();
            }
            TopNew mNew = mData.get(position);

            Glide.with(context).load(mNew.thumbnail_pic_s).animate(R.anim.img_loading)
                    .placeholder(R.drawable.bg_loading_eholder).into(vh.img);
            vh.title.setText(mNew.title);
            vh.date.setText(mNew.date);
            vh.author_name.setText(mNew.author_name);
            vh.item_news_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //资讯详情
                    if (mNewDate != null && mNewDate.size() > 0) {
                        WebUtils.openInternal(getActivity(), mNewDate.get(position).url);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView title;
            private ImageView img;
            private TextView date;
            private TextView author_name;
            private LinearLayout item_news_layout;
        }
    }
}
