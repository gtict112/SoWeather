package com.lhlSo.soweather.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lhlSo.soweather.R;
import com.lhlSo.soweather.activity.MainActivity;
import com.lhlSo.soweather.activity.PictureActivity;
import com.lhlSo.soweather.core.Appconfiguration;
import com.lhlSo.soweather.mode.BeautyListDate;
import com.lhlSo.soweather.mode.Result;
import com.lhlSo.soweather.service.BeautyService;
import com.lhlSo.soweather.utils.ResponseListenter;
import com.lhlSo.soweather.view.RatioImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2017/6/6.
 */

public class BeautyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar mToolbar;
    private String id = "福利";
    private List<BeautyListDate> mDate = new ArrayList<>();
    private Handler mHandler;
    private RecyclerView mList;
    private BeautyAdapter mBeautyAdapter;
    private Appconfiguration config = Appconfiguration.getInstance();
    private SwipeRefreshLayout mSwipeLayout;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;
    private int width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contextView = inflater.inflate(R.layout.fragment_item_beauty, container, false);
        initView(contextView);
        getDate(id);
        getHandleMessge();
        return contextView;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("福利社");
        ((MainActivity) getActivity()).initDrawer(mToolbar);
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新
        mSwipeLayout.setProgressBackgroundColor(R.color.white); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT); // 设置圆圈的大小
        mList = (RecyclerView) view.findViewById(R.id.beauty_list);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
        mList.setLayoutManager(layoutManager);
        mList.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    private void getDate(String Id) {
        config.showProgressDialog("正在加载", getActivity());
        final BeautyService mNews = new BeautyService();
        mNews.getBeautyList(new ResponseListenter<List<BeautyListDate>>() {
            @Override
            public void onReceive(Result<List<BeautyListDate>> result) {
                if (result.isSuccess()) {
                    mDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
                } else {
                    config.dismissProgressDialog();
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), result.getErrorMessage(), Snackbar.LENGTH_LONG)
                            .setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getDate(id);
                                }
                            })
                            .show();
                }
            }
        }, Id, page);
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

    private void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
        final BeautyService mNews = new BeautyService();
        mNews.getBeautyList(new ResponseListenter<List<BeautyListDate>>() {
            @Override
            public void onReceive(Result<List<BeautyListDate>> result) {
                if (result.isSuccess()) {
                    mDate = result.getData();
                    mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
                } else {
                    config.dismissProgressDialog();
                    Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), result.getErrorMessage(), Snackbar.LENGTH_LONG)
                            .setAction("重试", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getDate(id);
                                }
                            })
                            .show();
                }
            }
        }, id, 1);
        page = 1;
    }

    private void setNews(final List<BeautyListDate> mDate) {
        config.dismissProgressDialog();
        if (page != 1) {
            mBeautyAdapter.notifyDataSetChanged();
            mSwipeLayout.setRefreshing(false);
        } else {
            mBeautyAdapter = new BeautyAdapter(getActivity(), mDate);
            mList.setAdapter(mBeautyAdapter);
        }
        mBeautyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = mDate.get(position).img;
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                intent.putExtra(PictureActivity.EXTRA_IMAGE_URL, id);
                intent.putExtra(PictureActivity.EXTRA_IMAGE_TITLE, System.currentTimeMillis() + "");
                startActivity(intent);
            }
        });
        mList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                        >= recyclerView.computeVerticalScrollRange()) {
                    mSwipeLayout.setRefreshing(true);
                    page = page + 1;
                    final BeautyService mNews = new BeautyService();
                    mNews.getBeautyList(new ResponseListenter<List<BeautyListDate>>() {
                        @Override
                        public void onReceive(Result<List<BeautyListDate>> result) {
                            if (result.isSuccess()) {
                                mDate.addAll(result.getData());
                                mHandler.sendMessage(mHandler.obtainMessage(1, mDate));
                            } else {
                                mSwipeLayout.setRefreshing(false);
                                config.dismissProgressDialog();
                                Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), result.getErrorMessage(), Snackbar.LENGTH_LONG)
                                        .setAction("重试", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getDate(id);
                                            }
                                        })
                                        .show();
                            }
                        }
                    }, id, page);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        refresh();
    }


    public class BeautyAdapter extends
            RecyclerView.Adapter<BeautyAdapter.ViewHolder> implements View.OnClickListener {
        private List<BeautyListDate> mData = new ArrayList<>();
        private Context context;

        public BeautyAdapter(Context context, List<BeautyListDate> datas) {
            this.mData = datas;
            this.context = context;
        }

        private OnItemClickListener mOnItemClickListener = null;

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, (int) v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View arg0) {
                super(arg0);
                img = (RatioImageView) arg0.findViewById(R.id.img);
            }

            private RatioImageView img;

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        public BeautyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.item_beauty, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setOnClickListener(this);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final BeautyAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.itemView.setTag(i);
            BeautyListDate mTimeWeatherData = mData.get(i);
            Glide.with(context).load(mTimeWeatherData.img).animate(R.anim.img_loading).
                    diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.bg_loading_eholder).crossFade(500).into(viewHolder.img);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}