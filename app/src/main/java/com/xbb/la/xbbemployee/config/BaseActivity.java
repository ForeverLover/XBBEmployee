package com.xbb.la.xbbemployee.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.blueware.agent.android.BlueWare;
import com.lidroid.xutils.ViewUtils;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.XRequestCallBack;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.widget.CustomProgressDialog;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/5 16:16
 * 描述：基类
 */

public class BaseActivity extends Activity implements View.OnClickListener, XRequestCallBack,Init {
    protected LocalBroadcastManager localBroadcastManager;
    protected BaseApplication application;

    private long exitTime = 0;

    private Dialog dialog = null;

    protected ApiRequest apiRequest;


    protected TextView page_title;
    protected ImageView title_left_img;
    protected TextView title_right_text;
    protected String userId;

    public LocationClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        BlueWare.withApplicationToken(Constant.Keys.OneAPM).start(this.getApplication());
    }

    protected void setSuperContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected void setSuperContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtils.inject(this);
        init();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ViewUtils.inject(this);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ViewUtils.inject(this);
        init();
    }


    protected void init() {
        initData();
        initView();
        initListener();
    }


    /**
     * 初始化变量
     */
    private void initVariabe() {

        mLocationClient = ((BaseApplication) getApplication()).mLocationClient;
        application = (BaseApplication) getApplication();
        application.register(this);

    }


    /**
     * 设置点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);

    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepare(int taskId) {

    }

    @Override
    public void onSuccess(int taskId, Object... params) {

    }

    @Override
    public void onEnd(int taskId) {

    }

    @Override
    public void onFailed(int taskId, String errorMsg) {
        showToast(errorMsg);
    }

    @Override
    public void onLoading(int taskId, long count, long current) {

    }

    @Override
    public boolean isCallBack() {
        return true;
    }


    protected void startLocate() {
        application.startLocate();
    }

    public void showLoading() {
        if (dialog == null) {
            dialog = CustomProgressDialog.createDialog(this);
        }
        TextView tv_loadingTips = (TextView) dialog
                .findViewById(R.id.loadingText);
        tv_loadingTips.setVisibility(View.GONE);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (application != null) {
            application.unregister(this);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }
}
