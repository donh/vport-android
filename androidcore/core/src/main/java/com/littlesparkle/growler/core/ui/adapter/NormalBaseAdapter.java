package com.littlesparkle.growler.core.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

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

public abstract class NormalBaseAdapter<D, H extends NormalBaseAdapter.ViewHolder> extends BaseAdapter {
    protected List<D> mDataList;
    protected final Context mContext;
    protected final LayoutInflater mInflater;
    protected final int mResource;

    public NormalBaseAdapter(Context context, @LayoutRes int resource) {
        this(context, resource, null);
    }

    public NormalBaseAdapter(Context context, @LayoutRes int resource, List<D> dataList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mDataList = dataList;
    }

    public void setDataList(List<D> dataList) {
        mDataList = dataList;
    }

    public List<D> getDataList() {
        return mDataList;
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    @Override
    public D getItem(int position) {
        if (mDataList != null && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    protected boolean isLastItem(int position) {
        if (mDataList != null && (position + 1 == mDataList.size())) {
            return true;
        }
        return false;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResource, parent, false);
        }
        H h = onCreateViewHolder(parent, convertView);
        if (mDataList != null) {
            D d = mDataList.get(position);
            if (d != null) {
                convertView.setTag(d);
                onBindViewHolder(h, d, position);
            }
        }
        return convertView;
    }

    protected abstract H onCreateViewHolder(ViewGroup parent, View convertView);

    protected abstract void onBindViewHolder(H holder, D item, int position);

    public static class ViewHolder {
        protected View mRootView;

        public ViewHolder(View v) {
            mRootView = v;
        }
    }
}
