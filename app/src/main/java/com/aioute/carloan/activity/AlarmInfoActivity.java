package com.aioute.carloan.activity;

import android.graphics.drawable.PaintDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.AlarmInfoAdapter;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.bean.AlarmInfoBean;
import com.aioute.carloan.common.Contant;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.sft.base.adapter.BaseAdapter;
import cn.sft.listener.RecyclerViewItemClickListener;

/**
 * 报警信息
 */
@EActivity(R.layout.activity_alarm_info)
public class AlarmInfoActivity extends CustomBaseActivity implements RecyclerViewItemClickListener {

    @ViewById(R.id.alarm_info_expandablelistview)
    protected ExpandableListView mExpandablelistview;

    // 状态
    @ViewById(R.id.alarm_info_state_tv)
    TextView stateTV;
    // 分组
    @ViewById(R.id.alarm_info_group_tv)
    TextView groupTV;
    // 类型
    @ViewById(R.id.alarm_info_type_tv)
    TextView typeTV;
    // 时间
    @ViewById(R.id.alarm_info_time_tv)
    TextView timeTV;

    PopupWindow mPopWindow;
    //---------------------------------------------------------
    // 报警信息
    private List<AlarmInfoBean> alarmList;
    //
    private int checkPosition;

    @Override
    protected void afterViews() {
        super.afterViews();

        AlarmInfoAdapter adapter = new AlarmInfoAdapter(this, alarmList = new ArrayList<>());
        mExpandablelistview.setAdapter(adapter);

        for (int i = 0; i < 10; i++) {
            AlarmInfoBean bean = new AlarmInfoBean();
            bean.setDescribe("开始时间：2017-10-14\n恢复时间：2017.12.12\n所属分组：不知道\n设备号：1234564\n处理人:不认识\n处理备注：暂无");
            bean.setPlateNumber("鲁FF2222");
            bean.setState("已恢复");
            bean.setType("拆机报警");
            bean.setTime("10天前");
            alarmList.add(bean);
        }
        adapter.notifyDataSetChanged();
    }

    @Click({R.id.alarm_info_state_tv, R.id.alarm_info_group_tv, R.id.alarm_info_type_tv, R.id.alarm_info_time_tv})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_info_state_tv:
                showAsDropDownPopWindow(v);
                break;
            case R.id.alarm_info_group_tv:
//                SelectGroupActivity_.intent(this).start();
                break;
            case R.id.alarm_info_type_tv://类型
                AlarmTypeActivity_.intent(this).startForResult(Contant.RequestCodeKey.WARN);
                break;
            case R.id.alarm_info_time_tv:
                SelectTimeActivity_.intent(this).startForResult(Contant.RequestCodeKey.TIME);
                break;
        }
    }

    /**
     * 显示
     *
     * @param v
     */
    private void showAsDropDownPopWindow(View v) {
        //设置contentView
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, 1));
        mPopWindow = new PopupWindow(recyclerView, getResources().getDisplayMetrics().widthPixels / 4, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(recyclerView);
        mPopWindow.setBackgroundDrawable(new PaintDrawable());
        mPopWindow.setOutsideTouchable(true);
        recyclerView.setAdapter(new BaseAdapter(this, Arrays.asList(getResources().getStringArray(R.array.warn_status_array))) {

            @Override
            public int onCreateViewLayoutID(int i) {
                return R.layout.item_alarm_info_select_state;
            }

            @Override
            public void onBindViewHolder(int i, View view, final int position) {
                TextView textView = view.findViewById(R.id.item_alarm_info_select_state_tv);
                ImageView imageView = view.findViewById(R.id.item_alarm_info_select_state_iv);

                if (checkPosition == position) {
                    imageView.setBackgroundResource(R.mipmap.return_btn_bkground);
                } else {
                    imageView.setBackgroundResource(android.R.color.transparent);
                }
                textView.setText((String) getObjcet(position));
            }
        });

        //显示PopupWindow
        mPopWindow.showAsDropDown(v, 0, 0);
    }

    @OnActivityResult(Contant.RequestCodeKey.GROUP)
    protected void onActivityResultForGroup(@OnActivityResult.Extra(value = Contant.IntentKey.BEAN) String value) {
        updateGroupTV(value);
    }

    @OnActivityResult(Contant.RequestCodeKey.WARN)
    protected void onActivityResultForWarn(@OnActivityResult.Extra(value = Contant.IntentKey.BEAN) String value) {
        updateTypeTV(value);
    }

    @OnActivityResult(Contant.RequestCodeKey.TIME)
    protected void onActivityResultForTime(@OnActivityResult.Extra(value = Contant.IntentKey.BEAN) String value) {
        updateTimeTV(value);
    }

    @Override
    public void onRecyclerViewItemClick(BaseAdapter baseAdapter, View view, int position) {
        checkPosition = position;
        mPopWindow.dismiss();
        updateStatusTV((String) baseAdapter.getObjcet(position));
    }

    /**
     * 更新查询报警状态
     *
     * @param state
     */
    void updateStatusTV(String state) {
        stateTV.setText(state);
    }

    /**
     * 更新分组
     *
     * @param group
     */
    void updateGroupTV(String group) {
        if (TextUtils.isEmpty(group)) {
            timeTV.setText(getString(R.string.group));
            return;
        }
        groupTV.setText(group);
    }

    /**
     * 更新类型
     *
     * @param type
     */
    void updateTypeTV(String type) {
        if (TextUtils.isEmpty(type)) {
            timeTV.setText(getString(R.string.type));
            return;
        }
        typeTV.setText(type);
    }

    /**
     * 更新时间
     *
     * @param time
     */
    void updateTimeTV(String time) {
        if (TextUtils.isEmpty(time)) {
            timeTV.setText(getString(R.string.time));
            return;
        }
        timeTV.setSingleLine(false);
        timeTV.setText(time.replace("20", ""));
    }
}
