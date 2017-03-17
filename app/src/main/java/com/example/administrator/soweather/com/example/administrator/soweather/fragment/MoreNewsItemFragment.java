package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.activity.NewsDetailActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.New;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.News;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MoreNewsItemFragment extends Fragment {
    private String type = "top";
    private ListView content;
    private List<New> mNewDate = new ArrayList<>();
    private Handler mHandler;
    private NewsAdapter mNewsAdapter;
    private int mDrawable[] = {R.drawable.bg_shape_a1, R.drawable.bg_shape_a2, R.drawable.bg_shape_a3, R.drawable.bg_shape_a4, R.drawable.bg_shape_a5};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item, container, false);
        initView(contextView);
        getHandleMessge();
        return contextView;
    }

    private void initView(View view) {
        Bundle mBundle = getArguments();
        content = (ListView) view.findViewById(R.id.content);
        if (mBundle != null) {
            type = mBundle.getString("type");
            getDate(type);
        }
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void getDate(String type) {
        News mNews = new News();
        mNews.getNews(new ResponseListenter<List<New>>() {
            @Override
            public void onReceive(Result<List<New>> result) {
                if (result.isSuccess()) {
                    mNewDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mNewDate));
                } else {
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, type);
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

    private void setNews(List<New> mNewDate) {
        mNewsAdapter = new NewsAdapter(getActivity().getApplicationContext(), mNewDate);
        content.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();
    }

    public class NewsAdapter extends BaseAdapter {
        private List<New> mData = new ArrayList<>();
        private LayoutInflater inflater;

        public NewsAdapter(Context context, List<New> datas) {
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
            New mNew = mData.get(position);
            vh.realtype.setText(mNew.category);
            vh.title.setText(mNew.title);
            vh.date.setText(mNew.date);
            vh.author_name.setText(mNew.author_name);
            vh.img.setImageBitmap(mNew.img_2);
            int rand = (int) Math.round(Math.random() * 4);
            vh.tab.setBackgroundResource(mDrawable[rand]);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
