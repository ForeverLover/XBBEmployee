package com.xbb.la.xbbemployee.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class AlterpwdActivity extends TitleActivity {
    @ViewInject(R.id.new_pwd_et)
    private EditText new_pwd_et;
    @ViewInject(R.id.old_pwd_et)
    private EditText old_pwd_et;
    @ViewInject(R.id.conf_pwd_et)
    private EditText conf_pwd_et;

    private String oldpwd;
    private String newpwd;
    private String confpwd;

    private String uid;
    private Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterpwd);
    }

    @Override
    public void initData() {
        super.initData();
        uid = SharePreferenceUtil.getInstance().getUserId(this);
        apiRequest = new ApiRequest(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.pc_alterpwd_label);
    }

    @Override
    public void onPrepare(int taskId) {
        super.onPrepare(taskId);
        showLoading();
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
    }

    @OnClick(R.id.alterpwd_ensure_btn)
    private void alterpwd(View v) {
        oldpwd = old_pwd_et.getText() != null ? old_pwd_et.getText().toString() : "";
        newpwd = new_pwd_et.getText() != null ? new_pwd_et.getText().toString() : "";
        confpwd = conf_pwd_et.getText() != null ? conf_pwd_et.getText().toString() : "";
        if (StringUtil.isEmpty(oldpwd)) {
            showToast(R.string.alter_oldpwd_hint);
            return;
        }
        if (oldpwd.length() < 6 || oldpwd.length() > 12) {
            showToast(R.string.alter_oldpwd_error);
            return;
        }
        if (StringUtil.isEmpty(newpwd)) {
            showToast(R.string.alter_newpwd_hint);
            return;
        }
        if (newpwd.length() < 6 || newpwd.length() > 12) {
            showToast(R.string.alter_newpwd_error);
            return;
        }
        if (StringUtil.isEmpty(confpwd)) {
            showToast(R.string.alter_confpwd_hint);
            return;
        }
        if (confpwd.length() < 6 || confpwd.length() > 12) {
            showToast(R.string.alter_confpwd_error);
            return;
        }
        if (!newpwd.equals(confpwd)) {
            showToast(R.string.alter_pwd_error);
            return;
        }
        apiRequest.changePwd(uid,oldpwd,newpwd);
    }

    @Override
    protected void onClickTitleLeft(View v) {
        hideSoftInputView();
        super.onClickTitleLeft(v);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Task.ALTER_PWD:
                if (employee==null)
                    employee=SharePreferenceUtil.getInstance().getUserInfo(this);
                employee.setPwd(newpwd);
                SharePreferenceUtil.getInstance().saveUserInfo(this,employee);
                showToast(R.string.pc_alterpwd_succeed);
                hideSoftInputView();
                finish();
                break;
        }
    }
}
