package com.xbb.la.xbbemployee.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.LocationBean;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.location.LocationTools;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 13:31
 * 描述：订单详情
 */

public class OrderInfoActivity extends TitleActivity {
    @ViewInject(R.id.orderInfo_call_img)
    private ImageView orderInfo_call_img;
    @ViewInject(R.id.orderinfo_orderNo_tv)
    private TextView orderinfo_orderNo_tv;
    @ViewInject(R.id.orderinfo_orderTime_Tv)
    private TextView orderinfo_orderTime_Tv;
    @ViewInject(R.id.orderinfo_orderContent_Tv)
    private TextView orderinfo_orderContent_Tv;
    @ViewInject(R.id.orderinfo_orderPrice_Tv)
    private TextView orderinfo_orderPrice_Tv;
    @ViewInject(R.id.orderinfo_payWay_Tv)
    private TextView orderinfo_payWay_Tv;
    @ViewInject(R.id.orderinfo_payState_Tv)
    private TextView orderinfo_payState_Tv;
    @ViewInject(R.id.orderinfo_serviceTime_Tv)
    private TextView orderinfo_serviceTime_Tv;
    @ViewInject(R.id.orderinfo_orderState_Tv)
    private TextView orderinfo_orderState_Tv;
    @ViewInject(R.id.orderinfo_driver_tv)
    private TextView orderinfo_driver_tv;
    @ViewInject(R.id.orderinfo_driverTel_Tv)
    private TextView orderinfo_driverTel_Tv;
    @ViewInject(R.id.orderinfo_carInfo_Tv)
    private TextView orderinfo_carInfo_Tv;
    @ViewInject(R.id.orderinfo_carType_Tv)
    private TextView orderinfo_carType_Tv;
    @ViewInject(R.id.orderinfo_plateNo_Tv)
    private TextView orderinfo_plateNo_Tv;
    @ViewInject(R.id.orderinfo_carLocation_Tv)
    private TextView orderinfo_carLocation_Tv;
//    @ViewInject(R.id.orderinfo_addressAudio_Tv)
//    private TextView orderinfo_addressAudio_Tv;
    @ViewInject(R.id.orderinfo_finish_Tv)
    private TextView orderinfo_finish_Tv;
    @ViewInject(R.id.orderinfo_finish_layout)
    private LinearLayout orderinfo_finish_layout;
    @ViewInject(R.id.orderInfo_ing_layout)
    private LinearLayout orderInfo_ing_layout;
    @ViewInject(R.id.orderInfo_op_btn)
    private Button orderInfo_op_btn;
    @ViewInject(R.id.orderInfo_arrive_btn)
    private Button orderInfo_arrive_btn;
    @ViewInject(R.id.orderInfo_navigate_btn)
    private Button orderInfo_navigate_btn;

    private Intent serviceIntent;
    private IntentFilter intentFilter;

    private OrderInfo orderInfo;

    private String orderId;

    private int type = -1;


    private LocationBean targetLocation;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.IntentAction.STOP_LOCATION_UPLOAD.equals(action)) {
//                unbindService(serviceConnection);
                if (serviceIntent != null)
                    stopService(serviceIntent);
            }
            if (Constant.IntentAction.CHANGE_STATE.equals(action)) {
                if (apiRequest == null)
                    apiRequest = new ApiRequest(OrderInfoActivity.this);
                apiRequest.getOrderInfo(orderId);

            }
            if (Constant.IntentAction.MISSION_COMPLETE.equals(action)) {
                finish();

            }
        }
    };

    private boolean updateData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
    }

    @Override
    public void initData() {
        setTitle(getString(R.string.order_info_title));
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        if (StringUtil.isNumeric(orderId)&&Integer.parseInt(orderId)>0) {
            userId = SharePreferenceUtil.getInstance().getUserId(this);
            apiRequest = new ApiRequest(this);
            apiRequest.getOrderInfo(orderId);
            intentFilter = new IntentFilter(Constant.IntentAction.STOP_LOCATION_UPLOAD);
            intentFilter.addAction(Constant.IntentAction.CHANGE_STATE);
            intentFilter.addAction(Constant.IntentAction.MISSION_COMPLETE);
            localBroadcastManager.registerReceiver(receiver, intentFilter);
            orderInfo_call_img.setOnClickListener(this);
            orderInfo_op_btn.setOnClickListener(this);
            orderInfo_arrive_btn.setOnClickListener(this);
            orderInfo_navigate_btn.setOnClickListener(this);
        }else {
            showToast(R.string.orderinfo_error_order);
            finish();
        }

    }

    private void initOrderInfo() {
        orderinfo_orderNo_tv.setText(orderInfo.getOrderNo());
        orderinfo_orderTime_Tv.setText(orderInfo.getOrderGenerateTime());
        orderinfo_orderContent_Tv.setText(orderInfo.getOrderContent());
        if (StringUtil.isEmpty(orderInfo.getOrderSum()))
            orderinfo_orderPrice_Tv.setText("￥0.00");
        else
            orderinfo_orderPrice_Tv.setText(orderInfo.getOrderSum());
        orderinfo_payWay_Tv.setText(orderInfo.getPayType());
        orderinfo_orderContent_Tv.setText(orderInfo.getOrderName());
        orderinfo_orderState_Tv.setText(orderInfo.getOrderState());
        orderinfo_driver_tv.setText(orderInfo.getDriverName());
        orderinfo_driverTel_Tv.setText(orderInfo.getDriverTel());
        orderinfo_carInfo_Tv.setText(orderInfo.getCarInfo());
        orderinfo_carType_Tv.setText(orderInfo.getCarType());
        orderinfo_plateNo_Tv.setText(orderInfo.getPlateNo());
        orderinfo_carLocation_Tv.setText(orderInfo.getCarLocation());
        orderinfo_payState_Tv.setText(orderInfo.getPayState());
        orderinfo_serviceTime_Tv.setText(orderInfo.getServiceTime());
        orderinfo_orderState_Tv.setText(orderInfo.getOrderState());
        if (!StringUtil.isEmpty(orderInfo.getCarLat()) && !StringUtil.isEmpty(orderInfo.getCarLon())) {
            targetLocation = new LocationBean();
            targetLocation.setLat(orderInfo.getCarLat());
            targetLocation.setLon(orderInfo.getCarLon());
        }
        if (!StringUtil.isEmpty(orderInfo.getOrderFinishedTime())) {
            orderinfo_finish_layout.setVisibility(View.VISIBLE);
            orderinfo_finish_Tv.setText(orderInfo.getOrderFinishedTime());
        }
        if (StringUtil.isNumeric(orderInfo.getMissionState()))
            type = Integer.parseInt(orderInfo.getMissionState());
        switch (type) {
            case 0:
                orderInfo_op_btn.setVisibility(View.VISIBLE);
                orderInfo_op_btn.setText(getString(R.string.order_accept));
                break;
            case 1:
                orderInfo_op_btn.setVisibility(View.VISIBLE);
                orderInfo_op_btn.setText(getString(R.string.order_setOut));
                break;
            case 2:
                orderInfo_ing_layout.setVisibility(View.VISIBLE);
                orderInfo_op_btn.setVisibility(View.GONE);
                break;
            case 3:
                orderInfo_ing_layout.setVisibility(View.GONE);
                orderInfo_op_btn.setVisibility(View.VISIBLE);
                orderInfo_op_btn.setText(getString(R.string.order_mission_continue));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.orderInfo_call_img:
                if (orderInfo != null)
                    if (!StringUtil.isEmpty(orderInfo.getDriverTel()) && StringUtil.isNumeric(orderInfo.getDriverTel())) {
                        intent = new Intent(Intent.ACTION_DIAL);
                        //需要拨打的号码
                        intent.setData(Uri.parse("tel:" + orderInfo.getDriverTel()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else
                        showToast(getString(R.string.order_tel_error));
                break;
            case R.id.orderInfo_op_btn:
                if (apiRequest != null)
                    switch (type) {
                        case 0:
                            apiRequest.confirm(orderId, userId);
                            break;
                        case 1:
                            apiRequest.leave(orderId, userId);
                            break;
                        case 3:
                            intent = new Intent(this, TransactionActivity.class);
                            intent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
                            startActivity(intent);
                            break;
                    }
                break;
            case R.id.orderInfo_arrive_btn:
                if (apiRequest == null)
                    apiRequest = new ApiRequest(this);
                apiRequest.arrive(orderId, userId);
                break;
            case R.id.orderInfo_navigate_btn:
                intent = new Intent(this, RoutePlanActivity.class);
                if (targetLocation != null) {
                    intent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
                    intent.putExtra(Constant.IntentVariable.DESTINATION_LOCATION, targetLocation);
                    startActivity(intent);
                } else
                    showToast("用户地址信息有误");
                break;
        }
    }

    @Override
    protected void onClickTitleLeft(View v) {
        doBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
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

    @Override
    public void onSuccess(int taskId, String flag,Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        LocationTools mylocation = null;
        switch (taskId) {
            case Task.ORDER:
                orderInfo = ParseUtil.getInstance().parseOrderInfo(responseJson.getResult().toString());
                if (orderInfo != null)
                    initOrderInfo();
                break;
            case Task.ACCEPT:
                updateData = true;
                type = 1;
                orderInfo.setMissionState("1");
                initOrderInfo();
                break;
            case Task.SET_OUT:
                updateData = true;
                orderInfo.setMissionState("2");
                type = 2;
                initOrderInfo();
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.START_LOCATION_UPLOAD).putExtra(Constant.IntentVariable.ORDER_ID, orderId));

                break;
            case Task.ARRIVE:
                type = 3;
                orderInfo.setMissionState("3");
                initOrderInfo();
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.STOP_LOCATION_UPLOAD).putExtra(Constant.IntentVariable.ORDER_ID, orderId));
                startActivity(new Intent(this, TransactionActivity.class).putExtra(Constant.IntentVariable.ORDER_ID, orderId));
                break;

        }
    }


    @Override
    public void onFailed(int taskId, String errorMsg) {
        super.onFailed(taskId, errorMsg);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doBack() {
        if (updateData) {
            Intent intent = new Intent();
            intent.putExtra("select", type);
            intent.putExtra("id", orderId);
            setResult(RESULT_OK, intent);
            finish();
        } else
            finish();
    }
}
