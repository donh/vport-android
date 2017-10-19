package com.littlesparkle.growler.core.ui.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.littlesparkle.growler.core.R;
import com.littlesparkle.growler.core.ui.listener.OnPopwindowClickListener;
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
public class HeadPopWindow extends PopupWindow {
    private View mContentView;
    private OnPopwindowClickListener mOnPopwindowClickListener;


    private TextView cancleTextView = null;

    private TextView cameraTextView = null;

    private TextView photoTextview = null;


    public HeadPopWindow(Activity infoActivity, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(infoActivity);
        mContentView = inflater.inflate(R.layout.head_popwindow, null);
        int h = infoActivity.getWindowManager().getDefaultDisplay().getHeight();
        int w = infoActivity.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mContentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 + 50);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
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

        photoTextview = (TextView) mContentView.findViewById(R.id.tv_photo);
        cameraTextView = (TextView) mContentView.findViewById(R.id.tv_camera);
        cancleTextView = (TextView) mContentView.findViewById(R.id.tv_cancale_popwindow);
        RxBindingUtility.Click(photoTextview, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_photo);
            }
        });
        RxBindingUtility.Click(cameraTextView, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_camera);
            }
        });
        RxBindingUtility.Click(cancleTextView, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClick(R.id.tv_cancale_popwindow);
            }
        });
    }

    public void setOnPopwindowClickListener(OnPopwindowClickListener onPopwindowClickListener) {
        mOnPopwindowClickListener = onPopwindowClickListener;
    }


    public void onClick(int id) {
        if (id == R.id.tv_cancale_popwindow) {
            this.dismiss();
        } else if (id == R.id.tv_photo) {
            mOnPopwindowClickListener.getPhotoByAlbums();
        } else if (id == R.id.tv_camera) {
            mOnPopwindowClickListener.getPhotoByCamera();
        }
    }


}
