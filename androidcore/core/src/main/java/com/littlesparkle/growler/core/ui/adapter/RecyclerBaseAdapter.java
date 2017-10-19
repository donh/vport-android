package com.littlesparkle.growler.core.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.littlesparkle.growler.core.R;

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

public abstract class RecyclerBaseAdapter<T extends RecyclerView.ViewHolder, D>
        extends RecyclerView.Adapter<T>
        implements View.OnClickListener {
    protected LayoutInflater mInflater;
    protected Context mContext = null;
    protected List<D> mDataList = null;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecyclerBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) (v.getTag(R.id.tag_first_recycler)), v.getTag(R.id.tag_second_recycler));
        }
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (mDataList != null) {
            D d = mDataList.get(position);
            if (d != null) {
                holder.itemView.setTag(R.id.tag_first_recycler, position);
                holder.itemView.setTag(R.id.tag_second_recycler, d);
                holder.itemView.setOnClickListener(this);
                onBindViewHolderItem(holder, d, position);
            }
        }
    }

    protected abstract void onBindViewHolderItem(T holder, D item, int position);

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }

    public D getItem(int position) {
        if (mDataList != null && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
    }

    public void setDataList(List<D> dataList) {
        mDataList = dataList;
    }

    public List<D> getDataList() {
        return mDataList;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position, Object object);
    }
}
