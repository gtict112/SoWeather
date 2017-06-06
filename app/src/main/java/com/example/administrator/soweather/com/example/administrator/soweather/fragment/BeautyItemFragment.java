package com.example.administrator.soweather.com.example.administrator.soweather.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyListDate;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.TopNew;
import com.example.administrator.soweather.com.example.administrator.soweather.service.BeautyService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.base.BaseSkinFragment;

/**
 * Created by Administrator on 2017/6/6.
 */

public class BeautyItemFragment extends BaseSkinFragment {
    private String id = "1";
    private List<BeautyListDate> mDate = new ArrayList<>();
    private Handler mHandler;
    private ListView mList;
    private BeautyAdapter mBeautyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item_beauty, container, false);
        initDate();
        initView(contextView);
        getDate(id);
        getHandleMessge();
        return contextView;
    }

    private void initDate() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            id = mBundle.getString("id");
        }
    }

    private void initView(View view) {
        mList = (ListView) view.findViewById(R.id.beauty_list);
    }

    private void getDate(String Id) {
        final BeautyService mNews = new BeautyService();
        mNews.getBeautyList(new ResponseListenter<List<BeautyListDate>>() {
            @Override
            public void onReceive(Result<List<BeautyListDate>> result) {
                if (result.isSuccess()) {
                    mDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
                } else {
                    Toast.makeText(getActivity(), result.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, Id);
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        setNews(mDate);
                        break;
                }
            }
        };
    }


    private void setNews(List<BeautyListDate> mDate) {
        mBeautyAdapter = new BeautyAdapter(getActivity(), mDate);
        mList.setAdapter(mBeautyAdapter);
    }

    public class BeautyAdapter extends BaseAdapter {
        private List<BeautyListDate> mData = new ArrayList<BeautyListDate>();
        private LayoutInflater inflater;

        public BeautyAdapter(Context context, List<BeautyListDate> datas) {
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
            final BeautyAdapter.ViewHolder vh;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_news, parent,
                        false);
                vh = new BeautyAdapter.ViewHolder();
                vh.title = (TextView) convertView.findViewById(R.id.content);
                vh.date = (TextView) convertView.findViewById(R.id.date);
                vh.author_name = (TextView) convertView.findViewById(R.id.author_name);
                vh.img = (ImageView) convertView.findViewById(R.id.img);
                convertView.setTag(vh);
            } else {
                vh = (BeautyAdapter.ViewHolder) convertView.getTag();
            }
            BeautyListDate mNew = mData.get(position);
            vh.img.setImageBitmap(mNew.bg);
            vh.title.setText(mNew.title);
            return convertView;
        }

        class ViewHolder {
            private TextView title;
            private ImageView img;
            private TextView date;
            private TextView author_name;
        }
    }
}
