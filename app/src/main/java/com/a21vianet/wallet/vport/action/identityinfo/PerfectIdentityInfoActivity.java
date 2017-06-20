package com.a21vianet.wallet.vport.action.identityinfo;


import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a21vianet.wallet.vport.R;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;
import com.littlesparkle.growler.core.utility.TimeUtility;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PerfectIdentityInfoActivity extends BaseActivity {
    @BindView(R.id.title_bar_back_btn)
    AppCompatImageButton titleBarBackBtn;
    @BindView(R.id.title_bar_add)
    AppCompatImageButton titleBarAdd;
    @BindView(R.id.edit_name_value)
    EditText editNameValue;
    @BindView(R.id.edit_id_value)
    EditText editIdValue;
    @BindView(R.id.relative_time_begin)
    RelativeLayout relativeTimeBegin;
    @BindView(R.id.relative_time_end)
    RelativeLayout relativeTimeEnd;
    @BindView(R.id.edit_issued_value)
    EditText editIssuedValue;
    @BindView(R.id.tv_time_begin)
    TextView tvTimeBegin;
    @BindView(R.id.tv_time_end)
    TextView tvTimeEnd;

    private TimePickerView timeBeginPickerView;
    private TimePickerView timeEndPickerView;

    @Override
    protected void initData() {

        Calendar selectedDateBegin = Calendar.getInstance();
        Calendar startDateBegin = Calendar.getInstance();
        Calendar endDateBegin = Calendar.getInstance();
        startDateBegin.set(1949, 0, 1);

        Calendar selectedDateEnd = Calendar.getInstance();
        Calendar startDateEnd = Calendar.getInstance();
        Calendar endDateEnd = Calendar.getInstance();
        startDateBegin.set(1949, 0, 1);
        endDateEnd.set(2049, 0, 1);

        timeBeginPickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeBegin.setText(TimeUtility.formatDate(date));
            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(0xFF06bebd)//标题文字颜色
                .setSubmitColor(0xFF06bebd)//确定按钮文字颜色
                .setCancelColor(0xFF06bebd)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDateBegin)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDateBegin, endDateBegin)//起始终止年月日设定
                .setLabel("", "", "", "", "", "")
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式.
                .setDividerType(WheelView.DividerType.FILL)
                .build();

        timeEndPickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeEnd.setText(TimeUtility.formatDate(date));
            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(0xFF06bebd)//标题文字颜色
                .setSubmitColor(0xFF06bebd)//确定按钮文字颜色
                .setCancelColor(0xFF06bebd)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDateEnd)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDateEnd, endDateEnd)//起始终止年月日设定
                .setLabel("", "", "", "", "", "")
                .setType(new boolean[]{true, true, true, false, false, false})
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式.
                .setDividerType(WheelView.DividerType.FILL)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_perfect_identity_info;
    }

    @OnClick({R.id.title_bar_back_btn, R.id.title_bar_add, R.id.relative_time_begin, R.id.relative_time_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back_btn:
                finish();
                break;
            case R.id.title_bar_add:
                break;
            case R.id.relative_time_begin:
                timeBeginPickerView.show();
                break;
            case R.id.relative_time_end:
                timeEndPickerView.show();
                break;
        }
    }
}
