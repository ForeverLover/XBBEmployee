package com.xbb.la.xbbemployee.ui.activity;

import android.os.Bundle;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.utils.PushUtil;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class LoadingActivity extends BaseActivity {
    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void initData() {
        super.initData();
        employee = SharePreferenceUtil.getInstance().getUserInfo(this);
        // Push: 无账号初始化，用api key绑定
        // checkApikey();
        PushManager.startWork(getApplicationContext(),
                PushConstants.LOGIN_TYPE_API_KEY,
                PushUtil.getMetaValue(this,"api_key"));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toNext();
            }
        }, 3 * 1000);
    }

    private void toNext() {
        if (employee != null && employee.isLogin()) {
            startActivity(MainActivity.class);
        } else
            startActivity(LoginActivity.class);
        finish();
    }
}
