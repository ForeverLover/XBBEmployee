package com.xbb.la.xbbemployee.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.config.Constant;
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
public class AlterNicknameActivity extends TitleActivity {
    @ViewInject(R.id.nickname_et)
    private EditText nickname_et;

    private String name;
    private String oldname;
    private boolean hasChange;

    private Employee employee;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altername);
    }

    @Override
    public void initData() {
        super.initData();
        setTitle(R.string.alter_name_title);
        employee = SharePreferenceUtil.getInstance().getUserInfo(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        oldname = getIntent().getStringExtra("nickname");
        uid = employee.getUid();
        apiRequest = new ApiRequest(this);
    }

    @Override
    public void initView() {
        super.initView();
        nickname_et.setText(oldname);
        nickname_et.requestFocus();
    }

    @Override
    public void initListener() {
        super.initListener();
        nickname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hasChange = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.nickname_ensure_btn)
    private void alterName(View v) {
        if (!hasChange) {
            showToast(R.string.alter_no_change);
            return;
        }
        name = nickname_et.getText() != null ? nickname_et.getText().toString() : "";
        if (StringUtil.isEmpty(name)) {
            showToast(R.string.alter_name_hint);
            return;
        }
        if (name.equals(oldname)) {
            showToast(R.string.alter_no_change);
            return;
        }
        apiRequest.changeName(uid, name);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onClickTitleLeft(View v) {
        doBack();
    }

    private void doBack() {
        name = nickname_et.getText() != null ? nickname_et.getText().toString() : "";
        if (hasChange||!name.equals(oldname))
            new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_title_tip)).setMessage(getString(R.string.alter_tip_message)).setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton(getString(R.string.dialog_ensure), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).create().show();
        else
            finish();
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Task.ALTER_NAME:
                employee.setNickname(name);
                SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.NAME_CHANGED));
                showToast(R.string.pc_altername_succeed);
                Intent intent = new Intent();
                intent.putExtra("text", name);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
