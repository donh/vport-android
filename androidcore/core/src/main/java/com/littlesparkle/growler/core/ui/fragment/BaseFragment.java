package com.littlesparkle.growler.core.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.ui.mvp.BasePresenter;
import com.littlesparkle.growler.core.ui.mvp.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

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

public abstract class BaseFragment<P extends BasePresenter> extends
        Fragment implements BaseView {
    protected FragmentManager mFragmentManager;
    protected IFragmentOperation mOperation;
    protected P mPresenter;
    protected View mView;
    private Unbinder mUnbinder;

    public BaseFragment() {
    }

    @Override
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgress();
    }

    @Override
    public void dismissProgress() {
        ((BaseActivity) getActivity()).dismissProgress();
    }

    @Override
    public void showPrompt(String msg) {
        ((BaseActivity) getActivity()).showPrompt(msg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mOperation = (IFragmentOperation) getActivity();
        mFragmentManager = this.getFragmentManager();
        mView = inflater.inflate(getLayoutResId(), container, false);
        mPresenter = initPresenter();
        mUnbinder = ButterKnife.bind(this, mView);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initData();
        initView(savedInstanceState);
        return mView;
    }


    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    protected void initView(Bundle savedInstanceState) {
    }

    protected abstract void initData();

    protected abstract int getLayoutResId();

    protected P initPresenter() {
        return null;
    }
}
