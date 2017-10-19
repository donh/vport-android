package com.littlesparkle.growler.core.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.RelativeLayout;

import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.ui.mvp.BasePresenter;

/*
 * Copyright (C) 2016-2016, The Little-Sparkle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class BaseTitleBarFragmentActivity<P extends BasePresenter> extends
        BaseFragmentActivity<P>
        implements IFragmentOperation {
    protected RelativeLayout mBarContainer;
    protected AppCompatImageButton mBack;
    protected AppCompatTextView mTitle;
    protected String mTitleString;

    @Override
    protected void initView() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTitle.setText(mTitleString);
    }

    @Override
    protected void initData() {
        mBarContainer = (RelativeLayout) findViewById(R.id.title_bar);
        if (mBarContainer == null) {
            throw new RuntimeException("Could not find title bar!!!");
        }

        mBack = (AppCompatImageButton) findViewById(R.id.title_bar_back_btn);
        if (mBack == null) {
            throw new RuntimeException("Could not find back button in title bar!!!");
        }


        mTitle = (AppCompatTextView) findViewById(R.id.title_bar_text);
        if (mTitle == null) {
            throw new RuntimeException("Could not find title text in title bar!!!");
        }

        Intent it = getIntent();
        if (it != null) {
            mTitleString = it.getStringExtra("title");
            if (null == mTitleString | "".equals(mTitleString)) {
                if (selfTitleResId() != 0) {
                    mTitleString = getString(selfTitleResId());
                }
            }
        }
        if (setTitleBackgroundColor() != 0) {
            mBarContainer.setBackgroundColor(getResources().getColor(setTitleBackgroundColor()));
        }
        if (setBackButtonImage() != 0) {
            mBack.setImageResource(setBackButtonImage());
        }
        if (setTitleColor() != 0) {
            mTitle.setTextColor(setTitleColor());
        }
    }

    protected int selfTitleResId() {
        return 0;
    }

    protected int setTitleBackgroundColor() {
        return 0;
    }

    protected int setBackButtonImage() {
        return 0;
    }

    protected int setTitleColor() {
        return 0;
    }
}
