package com.a21vianet.wallet.vport.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.littlesparkle.growler.core.utility.RxBindingUtility;

import rx.functions.Action1;

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
public class TimePopWindow extends PopupWindow {
    private View mContentView;
    private OnTimePopupWindowClickListener mOnPopupWindowClickListener;

    private TextView cancelTextView = null;

    private TextView secondTextView = null;

    private TextView firstTextView = null;


    public TimePopWindow(Activity activity, @NonNull String firstText, @NonNull String secondText) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        mContentView = inflater.inflate(R.layout.time_popwindow, null);
        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
        this.setContentView(mContentView);
        this.setWidth(w / 2 + 50);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);

        mContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return true;
            }
        });
        this.setOutsideTouchable(true);

        firstTextView = (TextView) mContentView.findViewById(R.id.tv_first);
        secondTextView = (TextView) mContentView.findViewById(R.id.tv_second);
        cancelTextView = (TextView) mContentView.findViewById(R.id.tv_cancel);

        firstTextView.setText(firstText);
        secondTextView.setText(secondText);

        RxBindingUtility.Click(firstTextView, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_first);
            }
        });
        RxBindingUtility.Click(secondTextView, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_second);
            }
        });
        RxBindingUtility.Click(cancelTextView, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_cancel);
            }
        });
    }

    public void setOnPopupWindowClickListener(OnTimePopupWindowClickListener onPopwindowClickListener) {
        mOnPopupWindowClickListener = onPopwindowClickListener;
    }


    public void onClick(int id) {
        if (id == R.id.tv_cancel) {
            this.dismiss();
        } else if (id == R.id.tv_first) {
            mOnPopupWindowClickListener.onFixedDateChoose();
        } else if (id == R.id.tv_second) {
            mOnPopupWindowClickListener.onLongTermChoose();
        }
    }


}
