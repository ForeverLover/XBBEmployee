package com.xbb.la.xbbemployee.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 10:27
 * 描述：登录页面
 */

public class LoginActivity extends BaseActivity {
    private EditText login_user_et;
    private EditText login_pwd_et;
    private Button login_submit_btn;

    private String account;
    private String password;

    private IApiRequest apiRequest;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_login);
        login_pwd_et= (EditText) findViewById(R.id.login_password_et);
        login_user_et= (EditText) findViewById(R.id.login_user_et);
        login_submit_btn= (Button) findViewById(R.id.login_submit_btn);
    }

    @Override
    protected void initData() {
        super.initData();
        login_user_et.setText("18202810133");
        login_pwd_et.setText("123123");
        login_submit_btn.setOnClickListener(this);
        apiRequest=new ApiRequest(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_submit_btn:
                account=login_user_et.getText()!=null?login_user_et.getText().toString():"";
                password=login_pwd_et.getText()!=null?login_pwd_et.getText().toString():"";
                if (StringUtil.isEmpty(account)){
                    return;
                }
                if (StringUtil.isEmpty(password)){
                    return;
                }
                if (password.length()<6||password.length()<20){

                }
                apiRequest.login(account,password);
//                startActivity(MainActivity.class);
//                finish();
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId){
            case Task.LOGIN:
                Employee employee= JSON.parseObject(responseJson.getResult().toString(), Employee.class);
                SharePreferenceUtil.getInstance().saveUserInfo(this,employee);
                startActivity(MainActivity.class);
                finish();
                break;
        }
    }
}
