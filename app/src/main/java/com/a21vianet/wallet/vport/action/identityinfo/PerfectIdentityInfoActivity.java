package com.a21vianet.wallet.vport.action.identityinfo;


import android.app.Activity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a21vianet.wallet.vport.R;
import com.a21vianet.wallet.vport.common.ValidationUtility;
import com.a21vianet.wallet.vport.dao.IdentityCardManager;
import com.a21vianet.wallet.vport.dao.bean.IdentityCardState;
import com.a21vianet.wallet.vport.dao.entity.IdentityCard;
import com.a21vianet.wallet.vport.exception.RegularException;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.littlesparkle.growler.core.ui.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class PerfectIdentityInfoActivity extends BaseActivity {
    public final static String ISEDIT = "edit";
    public final static String IDENTITYID = "mIdentityid";
    public boolean mIsedit;
    public long mIdentityid;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        mIsedit = getIntent().getBooleanExtra(ISEDIT, true);
        mIdentityid = getIntent().getLongExtra(IDENTITYID, 0);
        if (mIsedit) {
            initDatePicker();
        } else {
            initNotEditDate();
        }
    }

    private void initNotEditDate() {
        IdentityCard identityCard = IdentityCardManager.get((int) mIdentityid);
        titleBarAdd.setVisibility(View.GONE);
        editNameValue.setEnabled(false);
        editIdValue.setEnabled(false);
        editIssuedValue.setEnabled(false);

        editNameValue.setText(identityCard.getName());
        editIdValue.setText(identityCard.getNumber());
        editIssuedValue.setText(identityCard.getAgencies());
        tvTimeBegin.setText(identityCard.getBegintime());
        tvTimeEnd.setText(identityCard.getEndtime());
    }

    private void initDatePicker() {
        Calendar selectedDateBegin = Calendar.getInstance();
        Calendar startDateBegin = Calendar.getInstance();
        Calendar endDateBegin = Calendar.getInstance();
        startDateBegin.set(1949, 0, 1);

        Calendar selectedDateEnd = Calendar.getInstance();
        Calendar startDateEnd = Calendar.getInstance();
        Calendar endDateEnd = Calendar.getInstance();
        startDateBegin.set(1949, 0, 1);
        endDateEnd.set(2049, 0, 1);

        timeBeginPickerView = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeBegin.setText(formatter.format(date));
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

        timeEndPickerView = new TimePickerView.Builder(this, new TimePickerView
                .OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tvTimeEnd.setText(formatter.format(date));
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

    @OnClick({R.id.title_bar_back_btn, R.id.title_bar_add, R.id.relative_time_begin, R.id
            .relative_time_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_bar_back_btn:
                finish();
                break;
            case R.id.title_bar_add:
                save();
                break;
            case R.id.relative_time_begin:
                if (mIsedit) {
                    timeBeginPickerView.show();
                }
                hideKeyboard(this);
                break;
            case R.id.relative_time_end:
                if (mIsedit) {
                    timeEndPickerView.show();
                }
                hideKeyboard(this);
                break;
        }
    }

    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void save() {
        String name = editNameValue.getText().toString().trim();
        String number = editIdValue.getText().toString().trim();
        String issued = editIssuedValue.getText().toString().trim();
        String timeBegin = tvTimeBegin.getText().toString().trim();
        String timeEnd = tvTimeEnd.getText().toString().trim();

        try {
            regular(name, "[\\s\\S]{1,16}", "请检查姓名");

            regular(number, "[1-9]\\d{5}[1-9]\\d{3}(" +
                    "(0\\d)|" +
                    "(1[0-2]))(" +
                    "([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)", "身份证号信息不正确");
            reagulerId(number, "身份证号信息不正确");
            regular(timeBegin, "[\\s\\S]{1,64}", "请检查有效开始时间");
            regular(timeEnd, "[\\s\\S]{1,64}", "请检查有效结束时间");
            regular(issued, "[\\s\\S]{1,64}", "请检查签发机关");

            IdentityCardManager.insert(new IdentityCard(null, name, number, timeBegin, timeEnd,
                    issued, IdentityCardState.NONE.state));
            finish();
        } catch (RegularException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void reagulerId(String number, String s) throws RegularException {
        if (!ValidationUtility.reagulerIdEntityNumber(number)) {
            throw new RegularException(s);
        }
    }

    /**
     * 进行正则验证
     *
     * @param value
     * @param regular
     * @param hint
     */
    public void regular(String value, String regular, String hint) throws RegularException {
        Pattern pattern = Pattern.compile(regular);
        Matcher matcher = pattern.matcher(value.trim());
        if (!matcher.matches()) {
            //抛出正则错误提示
            throw new RegularException(hint);
        }
    }
}
