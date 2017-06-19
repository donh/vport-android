package com.a21vianet.wallet.vport.action.historyoperation;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.historyoperation.bean.History;
import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.littlesparkle.growler.core.ui.activity.BaseTitleBarActivity;
import com.littlesparkle.growler.core.ui.adapter.RecyclerBaseAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import qiu.niorgai.StatusBarCompat;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HistoryOperationActivity extends BaseTitleBarActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_refresh)
    TwinklingRefreshLayout mLayoutRefresh;

    private List<History> mHistoryList = new ArrayList<>();
    private MyAdapter mMyAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_history_operation;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary));
        initRecyclerView();
        initRefreshLayout();
        refreshData();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected int selfTitleResId() {
        return R.string.title_history_notice;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initAdapter() {
        mMyAdapter = new MyAdapter(this);
        mMyAdapter.setDataList(mHistoryList);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(R
                .color.common_separate_list).size(1).build());
    }

    private void initRefreshLayout() {
        mLayoutRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                refreshData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadmoreData();
            }
        });
    }

    private void loadmoreData() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHistoryList.add(new History());
                        }
                        subscriber.onNext("");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        hideLoadMoreLayout();
                        mMyAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private void refreshData() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        mHistoryList.clear();
                        for (int i = 0; i < 5; i++) {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mHistoryList.add(new History());
                        }
                        subscriber.onNext("");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        hideRefreshLayout();
                        mMyAdapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private void hideRefreshLayout() {
        if (mLayoutRefresh != null) {
            mLayoutRefresh.finishRefreshing();
        }
    }

    private void hideLoadMoreLayout() {
        if (mLayoutRefresh != null) {
            mLayoutRefresh.finishLoadmore();
        }
    }

    private void disableLoadMore() {
        Toast.makeText(this, "到底了", Toast.LENGTH_SHORT).show();
        if (mLayoutRefresh != null) {
            mLayoutRefresh.setEnableLoadmore(false);
            mLayoutRefresh.setAutoLoadMore(false);
            mLayoutRefresh.setEnableOverScroll(false);
        }
    }

    private void enableLoadMore() {
        if (mLayoutRefresh != null) {
            mLayoutRefresh.setEnableLoadmore(true);
            mLayoutRefresh.setAutoLoadMore(true);
            mLayoutRefresh.setEnableOverScroll(true);
        }
    }

    private static class MyAdapter extends RecyclerBaseAdapter<MyViewHolder, History> {
        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected void onBindViewHolderItem(MyViewHolder holder, History item, int position) {
            holder.tvTitle.setText(item.getTitle());
            holder.tvContent.setText(item.getContent());
            holder.tvTime.setText(item.getTime());
            holder.tvMsg.setText(item.getMsg());
            Glide.with(mContext).load(item.getHeadimg()).into
                    (holder.imgHead);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout
                    .item_history_operation, parent, false);
            return new MyViewHolder(inflate);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgHead;
        private final TextView tvTitle;
        private final TextView tvContent;
        private final TextView tvTime;
        private final TextView tvMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgHead = (ImageView) itemView.findViewById(R.id.img_item_head);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_item_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_item_time);
            tvMsg = (TextView) itemView.findViewById(R.id.tv_item_msg);
        }
    }
}
