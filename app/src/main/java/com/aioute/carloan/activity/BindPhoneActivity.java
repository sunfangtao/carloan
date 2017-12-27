package com.aioute.carloan.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2017/12/27.
 * 绑定手机号
 */

@EActivity(R.layout.activity_bindphone)
public class BindPhoneActivity extends CustomBaseActivity {

    // 手机号
    @ViewById(R.id.bindphone_phone_et)
    EditText phoneET;
    // 验证码
    @ViewById(R.id.bindphone_code_et)
    EditText codeET;

    @Click(R.id.bindphone_code_btn)
    void codeBtnClick() {
        String checkResult = checkPhone();
        if (checkResult != null) {
            toast.setText(checkResult);
            return;
        }
        // 获取验证码
        getCodeByPhone();
    }

    String checkPhone() {
        String phone = phoneET.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            return getString(R.string.input_phone);
        } else {
            if (!phone.startsWith("1") || phone.length() != 11) {
                return "请输入正确的手机号";
            }
        }
        return null;
    }

    String checkCode() {
        String code = codeET.getText().toString();
        if (TextUtils.isEmpty(code)) {
            return getString(R.string.intpu_auth_code);
        }
        return null;
    }

    @Click(R.id.bindphone_bind_btn)
    void bindBtnClick() {
        String checkPhone = checkPhone();
        if (checkPhone != null) {
            toast.setText(checkPhone);
            return;
        }
        String checkCode = checkCode();
        if (checkCode != null) {
            toast.setText(checkCode);
            return;
        }
        bindPhone();
    }

    /**
     * 获取验证码
     */
    void getCodeByPhone() {
        RequestParams params = new RequestParams();
    }

    /**
     * 绑定手机号
     */
    void bindPhone() {
        RequestParams params = new RequestParams();
    }
}
