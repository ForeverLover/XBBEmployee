package com.xbb.la.xbbemployee.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.XRequestCallBack;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.service.LocationService;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.CustomProgressDialog;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/5 16:16
 * 描述：基类
 */

public class BaseActivity extends Activity implements View.OnClickListener, XRequestCallBack {
    protected LocationService locationService;
    protected LocalBroadcastManager localBroadcastManager;
    protected BaseApplication application;

    private long exitTime = 0;

    private Dialog dialog = null;

    protected ApiRequest apiRequest;

    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationService = ((LocationService.LocationBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
        }
    };

    protected TextView page_title;
    protected ImageView title_left_img;
    protected TextView title_right_text;
    protected String userId;

    public LocationClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        initVariabe();
        initViews();
        initData();
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
     * 初始化控件信息
     */
    protected void initViews() {
    }

    /**
     * 设置数据
     */
    protected void initData() {
    }

    ;

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

    /**
     * 多次点击退出程序
     */
    public void exitSystem() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.exit_back), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            application.exitSystem(BaseActivity.this);
            SharePreferenceUtil.getInstance().clear(getApplicationContext());
            BaseActivity.this.finish();
        }
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
}
