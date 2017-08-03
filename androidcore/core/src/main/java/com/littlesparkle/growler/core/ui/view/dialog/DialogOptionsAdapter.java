package com.littlesparkle.growler.core.ui.view.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.ui.adapter.NormalBaseAdapter;

import java.util.ArrayList;

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

public class DialogOptionsAdapter extends NormalBaseAdapter<String, DialogOptionsAdapter.ViewHolder> {

    public DialogOptionsAdapter(Context context, @LayoutRes int resource, int[] dataList) {
        super(context, resource);
        mDataList = new ArrayList<>();
        for (int i : dataList) {
            mDataList.add(context.getString(i));
        }
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View convertView) {
        return new ViewHolder(convertView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, String item, int position) {
        holder.text.setText(item);
        if (isLastItem(position)) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder extends NormalBaseAdapter.ViewHolder {
        private TextView text;
        private View divider;

        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.option_text);
            divider = v.findViewById(R.id.option_divider);
        }
    }
}
