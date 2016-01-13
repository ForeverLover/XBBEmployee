package com.xbb.la.xbbemployee.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.listener.AnimateFirstDisplayListener;
import com.xbb.la.xbbemployee.listener.DealOrderSetListener;
import com.xbb.la.xbbemployee.utils.MImageLoader;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.RoundImageView;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 10:27
 * 描述：登录页面
 */

public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login_user_et)
    private EditText login_user_et;
    @ViewInject(R.id.login_password_et)
    private EditText login_pwd_et;
    @ViewInject(R.id.login_submit_btn)
    private Button login_submit_btn;

    private String account;
    private String password;

    private IApiRequest apiRequest;

    @ViewInject(R.id.login_forget_layout)
    private LinearLayout login_forget_layout;

    @ViewInject(R.id.employee_head_img)
    private RoundImageView employee_head_img;
    private Employee employee;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (Constant.IntentAction.AVATAR_CHANGED.equals(action)){
                employee=SharePreferenceUtil.getInstance().getUserInfo(LoginActivity.this);
                if (employee!=null){
                    MImageLoader.getInstance(LoginActivity.this).displayImageByHalfUrl(employee.getAvatar(), employee_head_img, R.mipmap.main_avatar_default_img, new AnimateFirstDisplayListener());
                }

            }



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initData() {
        login_submit_btn.setOnClickListener(this);
        apiRequest = new ApiRequest(this);
        employee = SharePreferenceUtil.getInstance().getUserInfo(this);

    }

    @Override
    public void initView() {
        super.initView();
        if (employee != null) {
            MImageLoader.getInstance(this).displayImageByHalfUrl(employee.getAvatar(), employee_head_img, R.mipmap.login_head_default_img, new AnimateFirstDisplayListener());
            login_user_et.setText(employee.getTel());
            login_pwd_et.setText("123123");
            login_pwd_et.requestFocus();
            login_user_et.requestFocus();
        }
    }

    @Override
    public void initListener() {
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit_btn:
                account = login_user_et.getText() != null ? login_user_et.getText().toString() : "";
                password = login_pwd_et.getText() != null ? login_pwd_et.getText().toString() : "";
                if (StringUtil.isEmpty(account)) {
                    return;
                }
                if (StringUtil.isEmpty(password)) {
                    return;
                }
                if (password.length() < 6 || password.length() < 20) {

                }
                apiRequest.login(account, password);
//                startActivity(MainActivity.class);
//                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.LOGIN:
                Employee employee = ParseUtil.getInstance().parseEmployeeInfo(responseJson.getResult().toString());
                employee.setLogin(true);
                SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                startActivity(MainActivity.class);
                finish();
                break;
        }
    }
}
