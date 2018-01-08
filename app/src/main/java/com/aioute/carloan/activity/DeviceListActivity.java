package com.aioute.carloan.activity;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.aioute.carloan.R;
import com.aioute.carloan.adapter.GroupTreeAdapter;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.bean.GroupBean;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.sft.base.adapter.ListDropDownAdapter;
import cn.sft.util.MyHandler;
import cn.sft.util.Util;
import cn.sft.view.DropDownMenu;

/**
 * Created by Administrator on 2018/1/6.
 * 设备列表
 */
@EActivity(R.layout.activity_devicelist)
public class DeviceListActivity extends CustomBaseActivity {

    // Group
    @ViewById(R.id.devicelist_rg)
    RadioGroup group;
    // 全部
    @ViewById(R.id.devicelist_all_rb)
    RadioButton allRB;
    // 在线
    @ViewById(R.id.devicelist_online_rb)
    RadioButton onlineRB;
    // 离线
    @ViewById(R.id.devicelist_offline_rb)
    RadioButton offlineRB;
    // 报警
    @ViewById(R.id.devicelist_warn_rb)
    RadioButton warnRB;
    // DropDownMenu
    @ViewById(R.id.devicelist_ddm)
    DropDownMenu dropDownMenu;

    //
    ListView groupLV;
    //
    RecyclerView deviceTypeRV;
    //
    RecyclerView groupNameRV;
    //--------------------------------------------------
    // 设备型号
    List<String> deviceTypeList;
    // 分组名称
    List<String> groupNameList;
    // 设备分组列表
    List<GroupBean> deviceGroupList;

    @Override
    protected void afterViews() {
        initDropDownView();

        updateAllDeviceNum(12);
        updateOfflineDeviceNum(2);
        updateOnlineDeviceNum(4);
        updateWarnDeviceNum(5);
    }

    @Override
    protected void noSaveInstanceStateForCreate() {
        allRB.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_devicelist, menu);
        MenuItem menuItem = menu.findItem(R.id.action_devicelist_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) menuItem.getActionView();
        initSearchView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Util.print("onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Util.print("onQueryTextChange");
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 重置
        if (item.getItemId() == R.id.action_devicelist_reset) {
            Util.print("重置");
        }
        return true;
    }

    /**
     * SearchView属性修改
     */
    void initSearchView(SearchView searchView) {

        LinearLayout searchLayout = searchView.findViewById(getResources().getIdentifier("android:id/search_edit_frame", null, null));
        LinearLayout searchPlateLayout = searchView.findViewById(getResources().getIdentifier("android:id/search_plate", null, null));
        EditText searchEditText = searchView.findViewById(getResources().getIdentifier("android:id/search_src_text", null, null));

        searchEditText.setTextColor(getResources().getColor(R.color.colorBlackText));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorGrayText));
        searchPlateLayout.setBackgroundDrawable(new GradientDrawable());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.colorWhite));
        drawable.setStroke(Util.dp2px(this, 2), getResources().getColor(R.color.colorTransGray));
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(50f);
        searchLayout.setBackgroundDrawable(drawable);
    }

    void initDropDownView() {
        List<String> headers = new ArrayList<>();
        headers.add("设备型号");
        headers.add("分组名称");

        // 设备型号
        deviceTypeRV = new RecyclerView(this);
        deviceTypeRV.setLayoutManager(new GridLayoutManager(this, 1));
        deviceTypeList = new ArrayList<>();
        deviceTypeList.add("不限");
        deviceTypeRV.setAdapter(new ListDropDownAdapter(this, deviceTypeList));

        // 分组名称
        groupNameRV = new RecyclerView(this);
        groupNameRV.setLayoutManager(new GridLayoutManager(this, 1));
        groupNameList = new ArrayList<>();
        groupNameList.add("不限");
        groupNameRV.setAdapter(new ListDropDownAdapter(this, groupNameList));

        List<View> popupViews = new ArrayList<>();
        popupViews.add(deviceTypeRV);
        popupViews.add(groupNameRV);

        groupLV = new ListView(this);
        groupLV.setAdapter(new GroupTreeAdapter(this, deviceGroupList = new ArrayList<>(), 0));

        dropDownMenu.setTabTexts(headers);
        dropDownMenu.setPopupViews(popupViews);
        dropDownMenu.setContentView(groupLV);

        dropDownMenu.show();

        new MyHandler(2000){
            @Override
            public void run() {
                initGroup();
            }
        };

    }

    void initGroup() {
        GroupBean bean = null;
        for (int i = 1; i < 10; i++) {
            bean = new GroupBean();
            bean.setId(i + "");
            deviceGroupList.add(bean);
            for (int j = i * 10 + 1; j < i * 10 + 6; j++) {
                bean = new GroupBean();
                bean.setId(j + "");
                bean.setpId(i + "");
                deviceGroupList.add(bean);
                for (int m = j * 10 + 1; m < j * 10 + 6; m++) {
                    bean = new GroupBean();
                    bean.setId(m + "");
                    bean.setpId(j + "");
                    deviceGroupList.add(bean);
                }
            }
        }
        ((GroupTreeAdapter) groupLV.getAdapter()).notifyDataSetChanged();
    }

    /**
     * 更新全部设备的数量
     *
     * @param allCount
     */
    void updateAllDeviceNum(int allCount) {
        allRB.setText(String.format(getString(R.string.all), allCount));
    }

    /**
     * 更新在线设备的数量
     *
     * @param onlineCount
     */
    void updateOnlineDeviceNum(int onlineCount) {
        onlineRB.setText(String.format(getString(R.string.online), onlineCount));
    }

    /**
     * 更新离线设备的数量
     *
     * @param offlineCount
     */
    void updateOfflineDeviceNum(int offlineCount) {
        offlineRB.setText(String.format(getString(R.string.offline), offlineCount));
    }

    /**
     * 更新报警设备的数量
     *
     * @param warnCount
     */
    void updateWarnDeviceNum(int warnCount) {
        warnRB.setText(String.format(getString(R.string.warn), warnCount));
    }

}
