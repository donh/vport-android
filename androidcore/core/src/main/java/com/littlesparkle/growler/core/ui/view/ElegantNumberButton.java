package com.littlesparkle.growler.core.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.littlesparkle.growler.core.R;

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

public class ElegantNumberButton extends RelativeLayout implements View.OnFocusChangeListener,
        TextWatcher {
    private Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private OnClickListener mListener;
    private float initialNumber;
    private float lastNumber;
    private float currentNumber;
    private int finalNumber;
    private float stepNumber;
    private EditText mEditText;
    private View view;
    private OnValueChangeListener mOnValueChangeListener;

    public ElegantNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public ElegantNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        this.view = this;
        inflate(context, R.layout.elegant_number_button, this);
        final Resources res = getResources();
        final int defaultColor = res.getColor(R.color.button_request_enabled);
        final int defaultTextColor = res.getColor(R.color.text_color_title);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ElegantNumberButton,
                styleAttr, 0);

        initialNumber = a.getInt(R.styleable.ElegantNumberButton_initialNumber, 0);
        finalNumber = a.getInt(R.styleable.ElegantNumberButton_finalNumber, Integer.MAX_VALUE);
        stepNumber = a.getFloat(R.styleable.ElegantNumberButton_stepNumber, 1f);
        float textSize = a.getDimension(R.styleable.ElegantNumberButton_textSize, 13);
        int color = a.getColor(R.styleable.ElegantNumberButton_backGroundColor, defaultColor);
        int textColor = a.getColor(R.styleable.ElegantNumberButton_textColor, defaultTextColor);
        Drawable drawable = a.getDrawable(R.styleable.ElegantNumberButton_backgroundDrawable);

        Button button1 = (Button) findViewById(R.id.subtract_btn);
        Button button2 = (Button) findViewById(R.id.add_btn);
        mEditText = (EditText) findViewById(R.id.number_counter);
        mEditText.setOnFocusChangeListener(this);
        RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.layout);

        button1.setTextColor(textColor);
        button2.setTextColor(textColor);
        mEditText.setTextColor(textColor);
        button1.setTextSize(textSize);
        button2.setTextSize(textSize);
        mEditText.setTextSize(textSize);

        assert drawable != null;

        if (Build.VERSION.SDK_INT > 16)
            mLayout.setBackground(drawable);
        else
            mLayout.setBackgroundDrawable(drawable);

        mEditText.addTextChangedListener(this);
        mEditText.setText(String.valueOf(initialNumber));
//        mEditText.setEnabled(false);
        currentNumber = initialNumber;
        lastNumber = initialNumber;

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                float num = 0;
                try {
                    num = Float.valueOf(mEditText.getText().toString());
                } catch (NumberFormatException e) {
                }
                setNumber(String.format("%.2f", num - stepNumber), true);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                float num = 0;
                try {
                    num = Float.valueOf(mEditText.getText().toString());
                } catch (NumberFormatException e) {
                }
                setNumber(String.format("%.2f", num + stepNumber), true);
            }
        });
        a.recycle();
    }

    private void callListener(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }

        if (mOnValueChangeListener != null) {
            if (lastNumber != currentNumber) {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
            }
        }
    }

    public String getNumber() {
        checkNumber(mEditText.getText().toString());
        return mEditText.getText().toString();
    }

//    public void unFocus() {
//        mEditText.clearFocus();
//        mEditText.setFocusable(false);
//    }


    public void setNumber(String number) {
        lastNumber = currentNumber;
        this.currentNumber = Float.parseFloat(number);
        if (this.currentNumber > finalNumber) {
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber) {
            this.currentNumber = initialNumber;
        }
        mEditText.setText(String.valueOf(currentNumber));
    }

    public void setNumber(String number, boolean notifyListener) {
        setNumber(number);
        if (notifyListener) {
            callListener(this);
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            checkNumber(mEditText.getText().toString());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!"".equals(s.toString())) {
            try {
                Float aFloat = Float.valueOf(s.toString());
                if (aFloat > finalNumber) {
                    setNumber(String.format("%.2f", finalNumber * 1f), true);
                }
                if (s.length() - s.toString().lastIndexOf(".") > 3) {
                    setNumber(String.format("%.2f", aFloat), true);
                }
            } catch (NumberFormatException e) {
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface OnClickListener {
        void onClick(View view);

    }

    public interface OnValueChangeListener {
        void onValueChange(ElegantNumberButton view, float oldValue, float newValue);
    }

    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mEditText.requestFocus();
        return super.dispatchTouchEvent(ev);
    }

    private void checkNumber(String str) {
        if (str != null) {
            int length = str.length();
            if (length == 0) {
                setNumber("0.00", true);
            }
            try {
                Float aFloat = Float.valueOf(str.toString());
                if (aFloat >= 0 && aFloat <= finalNumber) {
                    setNumber(String.format("%.2f", aFloat), true);
                } else {
                    setNumber("0.00", true);
                }
            } catch (NumberFormatException e) {
                setNumber("0.00", true);
            }
        }
    }
}
