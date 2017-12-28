package com.aioute.carloan.activity;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.sft.view.AlwaysMarqueeTextView;

/**
 * Created by Administrator on 2017/12/27.
 * 更换手机号
 */

@EActivity(R.layout.activity_changephone)
public class ChangePhoneActivity extends CustomBaseActivity {

    // 手机号
    @ViewById(R.id.changephone_phone_tv)
    AlwaysMarqueeTextView phoneTV;

    @Override
    protected void afterViews() {
    }

    @Override
    protected void noSaveInstanceStateForCreate() {
        updatePhone("18562172893");
    }

    /**
     * 显示用的手机号
     *
     * @param phone
     */
    void updatePhone(String phone) {
        phone = phone.substring(0, 3) + "****" + phone.substring(7);
        phoneTV.setText(String.format(getString(R.string.bind_phone), phone));
    }

    @Click(R.id.changephone_change_btn)
    void changeBtnClick() {
        BindPhoneActivity_.intent(this).start();
    }
}
