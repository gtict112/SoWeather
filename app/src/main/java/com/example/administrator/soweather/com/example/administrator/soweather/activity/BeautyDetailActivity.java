package com.example.administrator.soweather.com.example.administrator.soweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.soweather.R;
import com.example.administrator.soweather.com.example.administrator.soweather.BaseActivity;
import com.example.administrator.soweather.com.example.administrator.soweather.core.Appconfiguration;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.BeautyDetail;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.service.BeautyService;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class BeautyDetailActivity extends BaseActivity implements ResponseListenter<BeautyDetail> {
    private TextView date;
    private RecyclerView mList;
    private String id = "1";
    private Handler mHandler;
    private BeautyDetail mDate = new BeautyDetail();
    private BeautyDetailAdapter mBeautyAdapter;
    private Appconfiguration config = Appconfiguration.getInstance();
    private TextView topTv;
    private ImageView topButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_detail);
        initDate();
        initView();
        getDate();
        getHandleMessge();
    }

    private void getHandleMessge() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    setView(mDate);
                }
            }
        };
    }

    private void setView(BeautyDetail mDate) {
        config.dismissProgressDialog();
        date.setText(mDate.time);
        topTv.setText(mDate.title);
        mBeautyAdapter = new BeautyDetailAdapter(this, mDate.gallerys);
        mList.setAdapter(mBeautyAdapter);
    }


    private void initDate() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
    }

    private void initView() {
        date = (TextView) findViewById(R.id.date);
        mList = (RecyclerView) findViewById(R.id.beauty_detail_list);
        topTv = (TextView) findViewById(R.id.topTv);
        topButton = (ImageView) findViewById(R.id.topButton);
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    private void getDate() {
        config.showProgressDialog("正在加载..", this);
        BeautyService mBeautyService = new BeautyService();
        mBeautyService.getBeautyDetail(this, id);
    }

    @Override
    public void onReceive(Result<BeautyDetail> result) {
        if (result.isSuccess()) {
            mDate = result.getData();
            mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
        } else {
            result.getErrorMessage();
        }
    }

    public class BeautyDetailAdapter extends RecyclerView.Adapter<BeautyDetailAdapter.ViewHolder> {
        private List<BeautyDetail.Gallery> mData = new ArrayList<>();
        private Context context;

        public BeautyDetailAdapter(Context context, List<BeautyDetail.Gallery> datas) {
            this.mData = datas;
            this.context = context;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
            }

            ImageView src;
        }


        @Override
        public BeautyDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_beauty_detail, viewGroup, false);
            BeautyDetailAdapter.ViewHolder viewHolder = new BeautyDetailAdapter.ViewHolder(view);
            viewHolder.src = (ImageView) view.findViewById(R.id.src);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BeautyDetailAdapter.ViewHolder holder, int position) {
            holder.itemView.setTag(position);
            BeautyDetail.Gallery mGallery = mData.get(position);
            Glide.with(context).load(mGallery.gallery_img).animate(R.anim.img_loading).diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true)
                    .placeholder(R.drawable.bg_loading_eholder).fitCenter().thumbnail(0.5f).into(holder.src);
        }

        @Override
        public int getItemCount() {
            return mData.size();
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
