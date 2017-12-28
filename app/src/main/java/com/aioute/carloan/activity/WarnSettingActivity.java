package com.aioute.carloan.activity;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.WarnSettingAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

import cn.sft.util.Util;

/**
 * Created by Administrator on 2017/12/25.
 * 报警信息设置界面
 */

// TODO 没有处理异常恢复
@EActivity(R.layout.activity_warnsetting)
public class WarnSettingActivity extends CustomBaseActivity {

    // 全选接收报警类型
    @ViewById(R.id.warnsetting_ck)
    AppCompatCheckBox allSelectCK;
    // 报警列表
    @ViewById(R.id.warnsetting_rv)
    RecyclerView warnRV;
    //------------------------------------------------------------

    @Override
    protected void afterViews() {
        List<String> warnNameList = Arrays.asList(getResources().getStringArray(R.array.warn_style_array));

        WarnSettingAdapter adapter = new WarnSettingAdapter(this, warnNameList);
        warnRV.setLayoutManager(new GridLayoutManager(this, 1));
        int deviderHeightPx = Util.dp2px(this, 1);
        RecyclerViewItemDecoration decoration = new RecyclerViewItemDecoration(deviderHeightPx);
        warnRV.addItemDecoration(decoration);
        warnRV.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "保存");
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 保存
        return true;
    }

    @CheckedChange(R.id.warnsetting_ck)
    void checkChange(boolean isChecked) {
        ((WarnSettingAdapter) warnRV.getAdapter()).setAllSelect(isChecked);
    }
}
