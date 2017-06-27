package com.a21vianet.wallet.vport.action.identityinfo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.dao.IdentityCardManager;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.bumptech.glide.Glide;
import com.littlesparkle.growler.core.am.ActivityUtility;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.adapter.RecyclerBaseAdapter;
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
    @BindView(R.id.btn_addid)
    Button mButtonAddid;

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

        initIdInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initIdDate();
    }

    private void initIdDate() {
        mIdentityInfoList.clear();
        mIdentityInfoList.addAll(IdentityCardManager.load());
        if (mIdentityInfoList.size() == 0) {
            mButtonAddid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtility.startActivityWithAnim(IdentityInfoActivity.this,
                            PerfectIdentityInfoActivity.class);
                }
            });
            mButtonAddid.setVisibility(View.VISIBLE);
        } else {
            mButtonAddid.setVisibility(View.GONE);
        }
    }

    private void initIdInfo() {
        mIdCardAdapter = new IdCardAdapter(this);
        mIdCardAdapter.setDataList(mIdentityInfoList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mIdCardAdapter);
        mIdCardAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_identity_info;
    }

    private void showImage() {
        Glide.with(this).load(R.drawable.icon_header).dontAnimate().into(mImgHead);
        Glide.with(this).load(R.drawable.icon_header).dontAnimate().bitmapTransform(new
                BlurTransformation(this, 5, 3)).into(mImgHeadBackground);
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

    static class IdCardAdapter extends RecyclerBaseAdapter<ViewHolder, IdentityCard> {
        private final int strokeWidth;
        private final int roundRadius;

        public IdCardAdapter(Context context) {
            super(context);
            strokeWidth = DensityUtility.dp2px(context, 1);
            roundRadius = DensityUtility.dp2px(context, 4);
        }

        @Override
        protected void onBindViewHolderItem(ViewHolder holder, IdentityCard item, int position) {

            holder.mIdCardTextView.setText(item.getNumber());

            String staetStr = "";
            @ColorInt
            int typeColor;
            switch (item.getState()) {
                case 1:
                    //待认证
                    staetStr = "待认证";
                    typeColor = 0xFF7d7d7d;
                    break;
                case 2:
                    //认证成功
                    staetStr = "认证成功";
                    typeColor = 0xFF1b93ef;
                    break;
                case 3:
                default:
                    //认证失败
                    staetStr = "认证失败";
                    typeColor = 0xFFeb212e;
            }

            holder.mCertificationStatusTextView.setText(staetStr);

            GradientDrawable gradientDrawableType = new GradientDrawable();
            gradientDrawableType.setCornerRadius(roundRadius);
            gradientDrawableType.setStroke(strokeWidth, typeColor);
            holder.mCertificationStatusTextView.setBackground(gradientDrawableType);
            holder.mCertificationStatusTextView.setTextColor(typeColor);
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
