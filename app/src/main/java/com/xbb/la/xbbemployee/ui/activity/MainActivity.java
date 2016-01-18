package com.xbb.la.xbbemployee.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.PushServiceBean;
import com.xbb.la.modellibrary.bean.Reminder;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.DensityUtil;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.modellibrary.utils.SystemUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.MissionPagerAdapter;
import com.xbb.la.xbbemployee.adapter.OrderAdapter;
import com.xbb.la.xbbemployee.config.SlideBaseActivity;
import com.xbb.la.xbbemployee.listener.AnimateFirstDisplayListener;
import com.xbb.la.xbbemployee.listener.DealOrderSetListener;
import com.xbb.la.xbbemployee.listener.MyTabActivityResultListener;
import com.xbb.la.xbbemployee.location.LocationTools;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.service.LocationService;
import com.xbb.la.xbbemployee.utils.MImageLoader;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.utils.SystemUtils;
import com.xbb.la.xbbemployee.widget.PageIndicator;
import com.xbb.la.xbbemployee.widget.RoundImageView;

import java.util.List;
import java.util.logging.SocketHandler;


public class MainActivity extends SlideBaseActivity implements RadioGroup.OnCheckedChangeListener {
    private Employee employee;
    private IntentFilter intentFilter;
    private Intent serviceIntent;

    private RoundImageView employee_head_img;
    private RoundImageView menu_head_img;

    private ImageView point_new_img;
    private ImageView menu_message_img;


    private int noConfirmPageIndex;
    private int ingPageIndex;
    private int finishedPageIndex;

    private int pageSize = 10;

    public RadioGroup employee_orderstate_group;
    private RadioButton employee_order_noAccept;
    private RadioButton employee_order_ing;
    private RadioButton employee_order_finished;

    private boolean showLoading;

    private OrderInfo operateOrderInfo;
    private int operateIndex;

    private int type = 0;
    private boolean flag = true;

    private Dialog logoutDialog;

    private MissionPagerAdapter missionPagerAdapter;
    private ViewPager mission_content_vp;
    private PageIndicator mission_content_indicator;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String orderId = intent.getStringExtra(Constant.IntentVariable.ORDER_ID);
            if (Constant.IntentAction.STOP_LOCATION_UPLOAD.equals(action)) {
                stopUploadInstantLocation(orderId);
            }
            if (Constant.IntentAction.START_LOCATION_UPLOAD.equals(action)) {
                uploadInstantLocation(orderId);

            }
            if (Constant.IntentAction.NEW_MESSAGE.equals(action)) {
                if (apiRequest == null)
                    apiRequest = new ApiRequest(MainActivity.this);
                apiRequest.getUnreadMessage(userId);
            }
            if (Constant.IntentAction.MISSION_COMPLETE.equals(action)) {
                employee_orderstate_group.check(R.id.employee_order_finished);
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof DealOrderSetListener) {
                        DealOrderSetListener listener = (DealOrderSetListener) fragment;
                        listener.dealOrderSetById(orderId);
                    }
                }
            }
            if (Constant.IntentAction.AVATAR_CHANGED.equals(action)) {
                employee = SharePreferenceUtil.getInstance().getUserInfo(MainActivity.this);
                if (employee != null) {
                    MImageLoader.getInstance(MainActivity.this).displayImageByHalfUrl(employee.getAvatar(), employee_head_img, R.mipmap.main_avatar_default_img, new AnimateFirstDisplayListener());
                    MImageLoader.getInstance(MainActivity.this).displayImageByHalfUrl(employee.getAvatar(), menu_head_img, R.mipmap.login_head_default_img, new AnimateFirstDisplayListener());
                }

            }

            if (Constant.IntentAction.NAME_CHANGED.equals(action)) {
                employee = SharePreferenceUtil.getInstance().getUserInfo(MainActivity.this);
                if (employee != null) {
                    menu_nickname_tv.setText(employee.getNickname());
                }
            }

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            operateOrderInfo = (OrderInfo) msg.obj;
            operateIndex = msg.arg1;
            switch (msg.what) {
                case Task.ACCEPT:
                    showToast("accept");
                    if (apiRequest == null)
                        apiRequest = new ApiRequest(MainActivity.this);
                    apiRequest.confirm(operateOrderInfo.getOrderId(), userId);
                    break;
                case Task.SET_OUT:
                    showToast("setout");
                    if (apiRequest == null)
                        apiRequest = new ApiRequest(MainActivity.this);
                    apiRequest.leave(operateOrderInfo.getOrderId(), userId);
                    break;
                case 100:
                    Intent intent = new Intent(MainActivity.this, OrderInfoActivity.class);
                    intent.putExtra(Constant.IntentVariable.ORDER_ID, operateOrderInfo.getOrderId());
                    startActivityForResult(intent, 1000);
                    break;

            }
        }
    };
    private TextView menu_nickname_tv;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        setContentView(R.layout.activity_main);
        employee_orderstate_group = (RadioGroup) findViewById(R.id.employee_orderstate_group);
        employee_head_img = (RoundImageView) findViewById(R.id.employee_head_img);
        employee_order_noAccept = (RadioButton) findViewById(R.id.employee_order_noAccept);
        employee_order_ing = (RadioButton) findViewById(R.id.employee_order_ing);
        employee_order_finished = (RadioButton) findViewById(R.id.employee_order_finished);
        mission_content_indicator = (PageIndicator) findViewById(R.id.mission_content_indicator);
        mission_content_vp = (ViewPager) findViewById(R.id.mission_content_vp);
        point_new_img = (ImageView) findViewById(R.id.point_new_img);
        //初始化滑动菜单
        initSlidingMenu(savedInstanceState);
        initLogoutDialog();
    }

    @Override
    protected void initData() {
        super.initData();
        SystemUtils.getInstance().clearOperateOrder();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.IntentAction.STOP_LOCATION_UPLOAD);
        intentFilter.addAction(Constant.IntentAction.START_LOCATION_UPLOAD);
        intentFilter.addAction(Constant.IntentAction.NEW_MESSAGE);
        intentFilter.addAction(Constant.IntentAction.MISSION_COMPLETE);
        intentFilter.addAction(Constant.IntentAction.AVATAR_CHANGED);
        intentFilter.addAction(Constant.IntentAction.NAME_CHANGED);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
        apiRequest = new ApiRequest(this);
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        employee = SharePreferenceUtil.getInstance().getUserInfo(this);
        if (employee != null && !employee.hasChannel()) {
            PushServiceBean pushServiceBean = SharePreferenceUtil.getInstance().getPushServiceBean(this);
            if (pushServiceBean != null) ;
            apiRequest.insertPushChannel(userId, pushServiceBean.getUserId(), pushServiceBean.getChannelId(), "1");
        }
        apiRequest.getDIYProducts();
        apiRequest.getKindReminders();
        showLoading = true;
        noConfirmPageIndex = 1;
        ingPageIndex = 1;
        finishedPageIndex = 1;
        employee_orderstate_group.check(R.id.employee_order_noAccept);
        missionPagerAdapter = new MissionPagerAdapter(getSupportFragmentManager());
        mission_content_vp.setAdapter(missionPagerAdapter);
//        mission_content_vp.setOffscreenPageLimit(1);
        mission_content_indicator.setViewPager(mission_content_vp);
        employee_head_img.setOnClickListener(this);
        employee_orderstate_group.setOnCheckedChangeListener(this);
        mission_content_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        employee_orderstate_group.check(R.id.employee_order_noAccept);
                        break;
                    case 1:
                        employee_orderstate_group.check(R.id.employee_order_ing);
                        break;
                    case 2:
                        employee_orderstate_group.check(R.id.employee_order_finished);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        if (employee != null)
            MImageLoader.getInstance(this).displayImageByHalfUrl(employee.getAvatar(), employee_head_img, R.mipmap.main_avatar_default_img, new AnimateFirstDisplayListener());
    }

    /**
     * 初始化滑动菜单
     */
    private void initSlidingMenu(Bundle savedInstanceState) {
        if (employee == null)
            employee = SharePreferenceUtil.getInstance().getUserInfo(this);
        View v = LayoutInflater.from(this).inflate(R.layout.menu_content, null);
        ((TextView) v.findViewById(R.id.menu_version_tv)).setText("v" + SystemUtils.getClientVersion(this));
        menu_nickname_tv = (TextView) v.findViewById(R.id.menu_nickname_tv);
        menu_nickname_tv.setText(employee.getNickname());
        menu_message_img = (ImageView) v.findViewById(R.id.menu_message_img);
        ((TextView) v.findViewById(R.id.menu_tel_tv)).setText(employee.getTel());
        v.findViewById(R.id.menu_personalinfo_layout).setOnClickListener(this);
        v.findViewById(R.id.menu_msg_layout).setOnClickListener(this);
        v.findViewById(R.id.menu_other_layout).setOnClickListener(this);
        v.findViewById(R.id.menu_logout_layout).setOnClickListener(this);
        menu_head_img = (RoundImageView) v.findViewById(R.id.menu_head_img);
        if (employee != null)
            MImageLoader.getInstance(this).displayImageByHalfUrl(employee.getAvatar(), menu_head_img, R.mipmap.login_head_default_img, new AnimateFirstDisplayListener());
        // 设置滑动菜单的视图
        setBehindContentView(v);
        showMenu();
//        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuActivity()).commit();
        // 实例化滑动菜单对象
        SlidingMenu sm = getSlidingMenu();
        // 设置滑动阴影的宽度
        sm.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动阴影的图像资源
        sm.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        sm.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.employee_head_img:
//                LocationBean locationBean = new LocationBean();
//                locationBean.setLat("30.663456");
//                locationBean.setLon("104.072227");
//                startActivity(new Intent(this, RoutePlanActivity.class).putExtra("targetLocation", locationBean));
               /* if (flag)
                    uploadInstantLocation("1");
                else
                    stopUploadInstantLocation("1");*/
                flag = !flag;

                toggle();
                break;
            case R.id.main_title:
                stopUploadInstantLocation("1");
                break;
            case R.id.menu_msg_layout:
                startActivity(MessageListActivity.class);
                break;
            case R.id.menu_personalinfo_layout:
                startActivity(PersonalCenterActivity.class);
                break;
            case R.id.menu_other_layout:
                startActivity(OtherActivity.class);
                break;
            case R.id.menu_logout_layout:
                if (logoutDialog != null && !logoutDialog.isShowing())
                    logoutDialog.show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (apiRequest == null)
            apiRequest = new ApiRequest(this);
        apiRequest.getUnreadMessage(userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
        SystemUtils.getInstance().clearOperateOrder();
        Constant.TempSet.orderUpdateArray.clear();
    }

    @Override
    public void onPrepare(int taskId) {
        super.onPrepare(taskId);

        if (showLoading)
            showLoading();
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
    }

    @Override
    public void onFailed(int taskId, int errorCode, String errorMsg) {

    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.DIY_PRODUCT:
                List<DIYProduct> diyProducts = ParseUtil.getInstance().parseDiyProducts(responseJson.getResult().toString());
                DBHelperMethod.getInstance().insertDIYProducts(diyProducts);
                break;
            case Task.REMINDER:
                List<Reminder> reminderList = ParseUtil.getInstance().parseKindReminders(responseJson.getResult().toString());
                DBHelperMethod.getInstance().insertRemminders(reminderList);
                break;
            case Task.UNREAD_MESSAGE:
                if (StringUtil.isNumeric(responseJson.getResult().toString())) {
                    int count = Integer.parseInt(responseJson.getResult().toString());
                    if (count > 0) {
                        point_new_img.setVisibility(View.VISIBLE);
                        menu_message_img.setImageResource(R.mipmap.menu_newmsg_img);
                    } else {
                        menu_message_img.setImageResource(R.mipmap.menu_message_img);
                        point_new_img.setVisibility(View.GONE);
                    }
                }
                break;
            case Task.INSERT_PUSH:
                if (employee == null)
                    employee = SharePreferenceUtil.getInstance().getUserInfo(this);
                employee.setChannel(true);
                SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                break;

        }
    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {

    }

    @Override
    public void onFailed(int taskId, String errorMsg) {
        super.onFailed(taskId, errorMsg);
        dismissLoading();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.employee_order_noAccept:
                type = 0;
                mission_content_vp.setCurrentItem(0);
                break;
            case R.id.employee_order_ing:
                type = 1;
                mission_content_vp.setCurrentItem(1);
                break;
            case R.id.employee_order_finished:
                type = 2;
                mission_content_vp.setCurrentItem(2);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            int select = data.getIntExtra("select", -1);
            String orderId = data.getStringExtra("id");
            switch (requestCode) {
                case 1000:
                    switch (select) {
                        case 1:
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if (fragment instanceof MyTabActivityResultListener) {
                                    MyTabActivityResultListener listener = (MyTabActivityResultListener) fragment;
                                    listener.onTabActivityResult(requestCode, resultCode, data);
                                }
                            }
                            break;
                        case 3:
                            if (type != 0)
                                break;
                        case 2:
                            Constant.TempSet.orderUpdateArray.put(1, true);
                            employee_orderstate_group.check(R.id.employee_order_ing);
                            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                                if (fragment instanceof DealOrderSetListener) {
                                    DealOrderSetListener listener = (DealOrderSetListener) fragment;
                                    listener.dealOrderSetById(orderId);
                                }
                            }
                            break;
                    }
                    break;
            }

        }
    }

    public void uploadInstantLocation(String orderId) {
        serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
        serviceIntent.putExtra("uid", userId);
        serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(serviceIntent);
    }

    private void stopUploadInstantLocation(String orderId) {
        if (serviceIntent == null)
            serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
        LocationTools.getInstance(getApplicationContext()).stopListener(orderId);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitSystem();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 初始化退出登录对话框
     */
    private void initLogoutDialog() {
        logoutDialog = new AlertDialog.Builder(this).
                setMessage(getString(R.string.dialog_logout_tip)).setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutDialog.dismiss();
            }
        }).setPositiveButton(getString(R.string.dialog_ensure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (employee == null)
                    employee.setLogin(true);
                employee.setLogin(false);
                SharePreferenceUtil.getInstance().saveUserInfo(MainActivity.this, employee);
               /* Intent intentb = new Intent(Constant.IntentAction.EXIT_SYSTEM);
                sendBroadcast(intentb);*/
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
                logoutDialog.dismiss();
            }
        }).create();


    }
}
