package com.aioute.carloan.activity;

import android.content.Intent;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.WarnSettingAdapter;
import com.aioute.carloan.adapter.decoration.RecyclerViewItemDecoration;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.sft.util.Util;

@EActivity(R.layout.activity_warnsetting)
public class AlarmTypeActivity extends CustomBaseActivity {

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
        menu.add(0, 0, 0, R.string.confirm);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 保存
        Map<Integer, Boolean> map = ((WarnSettingAdapter) warnRV.getAdapter()).getSelectMap();
        if (map == null || map.size() == 0) {
            finish();
            return true;
        }

        List<String> warnNameList = Arrays.asList(getResources().getStringArray(R.array.warn_style_array));

        Intent intent = new Intent();
        if (map.size() == 1) {
            intent.putExtra(Contant.IntentKey.BEAN, warnNameList.get(map.keySet().iterator().next()));
        } else {
            intent.putExtra(Contant.IntentKey.BEAN, "");
        }
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    @CheckedChange(R.id.warnsetting_ck)
    void checkChange(boolean isChecked) {
        ((WarnSettingAdapter) warnRV.getAdapter()).setAllSelect(isChecked);
    }
}
