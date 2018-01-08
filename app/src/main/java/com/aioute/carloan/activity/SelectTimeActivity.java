package com.aioute.carloan.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.DaySelectAdapter;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;

import cn.sft.view.DefaultNullRecyclerView;

@EActivity(R.layout.activity_select_time)
public class SelectTimeActivity extends CustomBaseActivity implements DaySelectAdapter.onLastDayClickListener {

    @ViewById
    DefaultNullRecyclerView mRecyclerView;
    @ViewById(R.id.select_time_tip_tv)
    TextView tipTv;
    @ViewById(R.id.select_time_confirm_btn)
    Button confirmBtn;
    private DaySelectAdapter dayAdapter;

    @Override
    protected void afterViews() {
        super.afterViews();
        dayAdapter = new DaySelectAdapter(this);
        mRecyclerView.setAdapter(dayAdapter);
        tipTv.setText("请选择开始时间");
        dayAdapter.clearSelect();
        try {
            Field field = DefaultNullRecyclerView.class.getDeclaredField("mRecyclerView");
            field.setAccessible(true);
            RecyclerView recyclerView = (RecyclerView) field.get(mRecyclerView);
            recyclerView.scrollToPosition(59);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click({R.id.select_time_confirm_btn})
    protected void onClick(View v) {

        if (dayAdapter.getStartDay() == null) {
            return;
        }
        Intent intent = new Intent();

        String value = dayAdapter.getStartDay().toString();
        if (dayAdapter.getLastDay() != null) {
            value += "\n";
            value += dayAdapter.getLastDay().toString();
        }
        intent.putExtra(Contant.IntentKey.BEAN, value);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void clickLastDay() {
        tipTv.setText("选取成功");
        confirmBtn.setEnabled(true);
    }

    @Override
    public void clickStartDay() {
        tipTv.setText("请选择结束日期");
        confirmBtn.setEnabled(true);
    }

    @Override
    public void clickAgain() {
        tipTv.setText("请选择结束日期");
        confirmBtn.setEnabled(true);
    }
}
