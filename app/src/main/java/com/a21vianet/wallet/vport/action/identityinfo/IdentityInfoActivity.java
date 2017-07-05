package com.a21vianet.wallet.vport.action.identityinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.identityinfo.task.IdCardUtiltiy;
import com.a21vianet.wallet.vport.dao.IdentityCardManager;
import com.a21vianet.wallet.vport.dao.bean.IdentityCardState;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.http.Api;
import com.a21vianet.wallet.vport.library.commom.crypto.bean.Contract;
import com.a21vianet.wallet.vport.library.commom.http.vport.VPortRequest;
import com.a21vianet.wallet.vport.library.commom.http.vport.bean.CertificationResult;
import com.a21vianet.wallet.vport.library.constant.SysConstant;
import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.http.BaseHttpSubscriber;
import com.littlesparkle.growler.core.http.ErrorResponse;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.adapter.RecyclerBaseAdapter;
import com.littlesparkle.growler.core.ui.view.GlideCircleImage;
import com.littlesparkle.growler.core.utility.DensityUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import qiu.niorgai.StatusBarCompat;

public class IdentityInfoActivity extends BaseActivity {
    @BindView(R.id.img_head_background)
    ImageView mImgHeadBackground;
    @BindView(R.id.img_head)
    ImageView mImgHead;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_recycler_view)
    TwinklingRefreshLayout mRefreshRecyclerView;
    @BindView(R.id.layout_hintadd)
    ConstraintLayout mLayoutHintadd;

    private IdCardAdapter mIdCardAdapter;
    private List<IdentityCard> mIdentityInfoList = new ArrayList<>();

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarCompat.translucentStatusBar(this);
        showImage();
        showName();

        initRefreshLayout();
        initIdInfo();
    }

    private void showName() {
        Contract contract = new Contract();
        contract.get();
        mTvUsername.setText(contract.getNickname());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIdDate();
    }

    private void initIdDate() {
        mIdentityInfoList.clear();
        mIdentityInfoList.addAll(IdentityCardManager.load());

        for (int i = 0; i < mIdentityInfoList.size(); i++) {
            uodateIdentityData(i);
        }

        if (mIdentityInfoList.size() == 0) {
            mLayoutHintadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtility.startActivityWithAnim(IdentityInfoActivity.this,
                            PerfectIdentityInfoActivity.class);
                }
            });
            mLayoutHintadd.setVisibility(View.VISIBLE);
        } else {
            mLayoutHintadd.setVisibility(View.GONE);
        }
        mIdCardAdapter.notifyDataSetChanged();
    }

    private void initIdInfo() {
        mIdCardAdapter = new IdCardAdapter(this);
        mIdCardAdapter.setDataList(mIdentityInfoList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mIdCardAdapter);
        mIdCardAdapter.notifyDataSetChanged();
    }

    private void uodateIdentityData(final int i) {
        new VPortRequest().certificate(new BaseHttpSubscriber<CertificationResult>() {
            @Override
            protected void onError(ErrorResponse error) {
                super.onError(error);

            }

            @Override
            protected void onSuccess(CertificationResult certificationResult) {
                super.onSuccess(certificationResult);
                switch (certificationResult.getStatus()) {
                    case "APPROVED":
                        mIdentityInfoList.get(i).setState(IdentityCardState.APPROVED.state);
                        mIdentityInfoList.get(i).setJwt(certificationResult.getAttestation());
                        break;
                    case "PENDING":
                        mIdentityInfoList.get(i).setState(IdentityCardState.PENDING.state);
                        break;
                    case "REJECTED":
                        mIdentityInfoList.get(i).setState(IdentityCardState.REJECTED.state);
                        break;
                }
                mIdCardAdapter.notifyDataSetChanged();
            }
        }, mIdentityInfoList.get(i).getToken());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_identity_info;
    }

    private void showImage() {
        if (!SysConstant.getHradImageUrlHash().equals("")) {
            String api = Api.IPFSWebApi + SysConstant.getHradImageUrlHash();
            Glide.with(this).load(api).dontAnimate().transform(new GlideCircleImage(this)).into
                    (mImgHead);
            Glide.with(this).load(api).dontAnimate().bitmapTransform(new
                    BlurTransformation(this, 3, 3)).into(mImgHeadBackground);
        } else {
            Glide.with(this).load(R.drawable.icon_header).dontAnimate().into(mImgHead);
            Glide.with(this).load(R.drawable.icon_header).dontAnimate().bitmapTransform(new
                    BlurTransformation(this, 3, 3)).into(mImgHeadBackground);
        }
    }

    @OnClick({R.id.img_back, R.id.img_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_add:
                ActivityUtility.startActivityWithAnim(IdentityInfoActivity.this,
                        PerfectIdentityInfoActivity.class);
                break;
        }
    }

    private void initRefreshLayout() {
        mRefreshRecyclerView.setEnableOverScroll(false);
        mRefreshRecyclerView.setEnableLoadmore(false);
        mRefreshRecyclerView.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                initIdDate();
                if (refreshLayout != null) {
                    refreshLayout.finishRefreshing();
                }
            }
        });
    }

    class IdCardAdapter extends RecyclerBaseAdapter<ViewHolder, IdentityCard> {
        private final int strokeWidth;
        private final int roundRadius;

        public IdCardAdapter(Context context) {
            super(context);
            strokeWidth = DensityUtility.dp2px(context, 1);
            roundRadius = DensityUtility.dp2px(context, 4);
        }

        @Override
        protected void onBindViewHolderItem(ViewHolder holder, final IdentityCard item, int
                position) {

            holder.mIdCardTextView.setText(item.getNumber());

            Pair<String, Integer> stringIntegerPair = IdCardUtiltiy.verdictIdState(item.getState());

            String staetStr = stringIntegerPair.first;
            @ColorInt
            int typeColor = stringIntegerPair.second;

            holder.mCertificationStatusTextView.setText(staetStr);

            GradientDrawable gradientDrawableType = new GradientDrawable();
            gradientDrawableType.setCornerRadius(roundRadius);
            gradientDrawableType.setStroke(strokeWidth, typeColor);
            holder.mCertificationStatusTextView.setBackground(gradientDrawableType);
            holder.mCertificationStatusTextView.setTextColor(typeColor);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(IdentityInfoActivity.this,
                            PerfectIdentityInfoActivity.class);
                    intent.putExtra(PerfectIdentityInfoActivity.IDENTITYID, item.getId());
                    intent.putExtra(PerfectIdentityInfoActivity.ISEDIT, false);
                    ActivityUtility.startActivityWithAnim(IdentityInfoActivity.this, intent);
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_identity_info,
                    parent, false);
            return new ViewHolder(inflate);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mCertificationStatusTextView;
        private final TextView mIdCardTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCertificationStatusTextView = (TextView) itemView.findViewById(R.id
                    .tv_certification_status);
            mIdCardTextView = (TextView) itemView.findViewById(R.id.tv_idcard);
        }

    }
}
