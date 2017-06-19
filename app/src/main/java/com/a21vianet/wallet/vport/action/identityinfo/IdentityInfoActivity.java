package com.a21vianet.wallet.vport.action.identityinfo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.action.identityinfo.bean.IdentityInfo;
import com.bumptech.glide.Glide;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.adapter.RecyclerBaseAdapter;

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

    private IdCardAdapter mIdCardAdapter;
    private List<IdentityInfo> mIdentityInfoList = new ArrayList<>();

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

    private void initIdInfo() {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setId("123456200808081234");
        mIdentityInfoList.add(identityInfo);

        mIdCardAdapter = new IdCardAdapter(this);
        mIdCardAdapter.setDataList(mIdentityInfoList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mIdCardAdapter);
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
                break;
        }
    }

    static class IdCardAdapter extends RecyclerBaseAdapter<ViewHolder, IdentityInfo> {

        public IdCardAdapter(Context context) {
            super(context);
        }

        @Override
        protected void onBindViewHolderItem(ViewHolder holder, IdentityInfo item, int position) {
            holder.mCertificationStatusTextView.setText("待审核");
            holder.mIdCardTextView.setText(item.getId());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_identity_info,
                    null, false);
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
