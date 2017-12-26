package com.aioute.carloan.fragment;

import com.aioute.carloan.R;
import com.aioute.carloan.activity.SettingActivity_;
import com.aioute.carloan.base.CustomBaseFragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/25.
 * 我的
 */

@EFragment(R.layout.fragment_my)
public class MyFragment extends CustomBaseFragment {

    // 账号
    @ViewById(R.id.my_account_tv)
    AlwaysMarqueeTextView accountTV;

    @Override
    protected void afterViews() {
        updateAccountData("18562172893", "测试");
    }

    /**
     * 更新账号和分组
     *
     * @param account 账号
     * @param group   分组
     */
    void updateAccountData(String account, String group) {
        String text = "<customfont size='20sp' color='#00a65a'>账号：18562172893"
                + "</customfont><br><br><customfont size='10sp' color='#666666'>所属组：</customfont>";
        accountTV.setHtmlText(String.format(text, account, group));
    }

    @Click(R.id.my_aboutus_tv)
    void aboutUsClick() {

    }

    @Click(R.id.my_setting_tv)
    void settingClick() {
        SettingActivity_.intent(this).start();
    }

    @Click(R.id.my_phone_tv)
    void phoneClick() {

    }

    @Click(R.id.my_exit_btn)
    void exit() {

    }
}
