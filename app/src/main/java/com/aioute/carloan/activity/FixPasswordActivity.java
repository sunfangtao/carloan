package com.aioute.carloan.activity;

import android.text.TextUtils;
import android.widget.EditText;

import com.aioute.carloan.R;
import com.aioute.carloan.base.CustomBaseActivity;
import com.aioute.carloan.common.Contant;
import com.loopj.android.http.RequestParams;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2017/12/25.
 * 修改密码
 */
@EActivity(R.layout.activity_fixpassword)
public class FixPasswordActivity extends CustomBaseActivity {

    // 原始密码
    @ViewById(R.id.fixpassword_oripass_et)
    EditText oriPassET;
    // 新设密码
    @ViewById(R.id.fixpassword_newpass_et)
    EditText newPassET;
    // 确认新设密码
    @ViewById(R.id.fixpassword_confirmnewpass_et)
    EditText confirmNewPassET;

    @Click(R.id.fixpassword_btn)
    void fixPasswordClick() {
        String checkResult = checkInput();
        if (checkResult != null) {
            toast.setText(checkResult);
            return;
        }
        // 修改密码
        fixPassword();
    }

    String checkInput() {
        String oriPassword = oriPassET.getText().toString();
        String newPassword = newPassET.getText().toString();
        String confirmNewPass = confirmNewPassET.getText().toString();

        if (TextUtils.isEmpty(oriPassword)) {
            return getString(R.string.input_ori_password);
        }

        if (TextUtils.isEmpty(newPassword)) {
            return getString(R.string.input_new_password);
        } else if (newPassword.length() < Contant.PASSWORD_MIN_LENGTH) {
            return "密码长度不能小于" + Contant.PASSWORD_MIN_LENGTH;
        }

        if (TextUtils.isEmpty(confirmNewPass)) {
            return getString(R.string.input_new_password);
        } else if (confirmNewPass.length() < Contant.PASSWORD_MIN_LENGTH) {
            return "密码长度不能小于" + Contant.PASSWORD_MIN_LENGTH;
        } else if (!confirmNewPass.equals(newPassword)) {
            return "密码输入不一致！";
        }
        return null;
    }

    void fixPassword() {
        RequestParams params = new RequestParams();
        params.put("", oriPassET.getText().toString());
        params.put("", newPassET.getText().toString());


    }
}
