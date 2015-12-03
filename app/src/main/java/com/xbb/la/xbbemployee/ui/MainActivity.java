package com.xbb.la.xbbemployee.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.Reminder;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.DensityUtil;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.OrderAdapter;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.location.LocationTools;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.service.LocationService;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.RoundImageView;

import java.util.List;


public class MainActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, RadioGroup.OnCheckedChangeListener {
    private IntentFilter intentFilter;
    private Intent serviceIntent;

    private RoundImageView employee_head_img;

    /**
     * 当前数据类型
     * 0 等待确认
     * 1 进行中
     * 2 已完成
     */
    private int type;

    private OrderAdapter noConfirmOrderAdapter;
    private OrderAdapter ingOrderAdapter;
    private OrderAdapter finishedOrderAdapter;

    private List<OrderInfo> noConfirmDataOrderList;
    private List<OrderInfo> ingDataOrderList;
    private List<OrderInfo> finishedDataOrderList;

    private List<OrderInfo> noConfirmShowOrderList;
    private List<OrderInfo> ingShowOrderList;
    private List<OrderInfo> finishedShowOrderList;

    private boolean noConfirmIsAdd;
    private boolean ingIsAdd;
    private boolean finishedIsAdd;

    private PullToRefreshListView order_list_lv;

    private int noConfirmPageIndex;
    private int ingPageIndex;
    private int finishedPageIndex;

    private int pageSize = 10;

    private RadioGroup employee_orderstate_group;
    private RadioButton employee_order_noAccept;
    private RadioButton employee_order_ing;
    private RadioButton employee_order_finished;

    private boolean showLoading;

    private OrderInfo operateOrderInfo;
    private int operateIndex;

    private TextView main_title;

    private boolean flag = true;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String orderId = intent.getStringExtra(Constant.IntentVariable.ORDER_ID);
            Log.v("Tag", "receiver_orderID:" + orderId + " action:" + action);
            if (Constant.IntentAction.STOP_LOCATION_UPLOAD.equals(action)) {
                stopUploadInstantLocation(orderId);
            }
            if (Constant.IntentAction.START_LOCATION_UPLOAD.equals(action)) {
                uploadInstantLocation(orderId);
            }
            if (Constant.IntentAction.MISSION_COMPLETE.equals(action)) {
                changeList(2);
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

    @Override
    protected void initViews() {
        super.initViews();
        setContentView(R.layout.activity_main);
        employee_orderstate_group = (RadioGroup) findViewById(R.id.employee_orderstate_group);
        employee_head_img = (RoundImageView) findViewById(R.id.employee_head_img);
        order_list_lv = (PullToRefreshListView) findViewById(R.id.order_list_lv);
        employee_order_noAccept = (RadioButton) findViewById(R.id.employee_order_noAccept);
        employee_order_ing = (RadioButton) findViewById(R.id.employee_order_ing);
        employee_order_finished = (RadioButton) findViewById(R.id.employee_order_finished);
        main_title = (TextView) findViewById(R.id.main_title);
    }

    @Override
    protected void initData() {
        super.initData();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.IntentAction.STOP_LOCATION_UPLOAD);
        intentFilter.addAction(Constant.IntentAction.START_LOCATION_UPLOAD);
        intentFilter.addAction(Constant.IntentAction.MISSION_COMPLETE);
        localBroadcastManager.registerReceiver(receiver, intentFilter);
        apiRequest = new ApiRequest(this);
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        apiRequest.getDIYProducts();
        apiRequest.getKindReminders();
        showLoading = true;
        order_list_lv.setMode(PullToRefreshBase.Mode.BOTH);
        type = 2;
        noConfirmPageIndex = 1;
        ingPageIndex = 1;
        finishedPageIndex = 1;
        employee_orderstate_group.check(R.id.employee_order_noAccept);
        changeList(0);
        employee_head_img.setOnClickListener(this);
        main_title.setOnClickListener(this);
        order_list_lv.setOnRefreshListener(this);
        employee_orderstate_group.setOnCheckedChangeListener(this);
    }

    public void changeList(int select) {
        showLoading = true;
        order_list_lv.setAdapter(null);
        order_list_lv.setDividerHeight(0);
        type = select;

        switch (select) {
            case 0:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 1:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                break;
        }
        Log.v("Tag", "update_data");
        getNewestDataList();

    }

    public void getOrderList() {
        if (apiRequest == null)
            apiRequest = new ApiRequest(this);
        switch (type) {
            case 0:
                apiRequest.getOrderList(userId, type, noConfirmPageIndex, pageSize);
                break;
            case 1:
                apiRequest.getOrderList(userId, type, ingPageIndex, pageSize);
                break;
            case 2:
                apiRequest.getOrderList(userId, type, finishedPageIndex, pageSize);
                break;
        }
    }

    /**
     * 下拉刷新
     */
    private void getNewestDataList() {
        showLoading = false;
        switch (type) {
            case 0:
                noConfirmPageIndex = 1;
                break;
            case 1:
                ingPageIndex = 1;
                break;
            case 2:
                finishedPageIndex = 1;
                break;

        }

        getOrderList();
    }

    /**
     * 上拉加载
     */
    private void getMoreDataList() {
        showLoading = false;
        switch (type) {
            case 0:
                noConfirmIsAdd = true;
                noConfirmPageIndex++;
                break;
            case 1:
                ingIsAdd = true;
                ingPageIndex++;
                break;
            case 2:
                finishedIsAdd = true;
                finishedPageIndex++;
                break;
        }
        getOrderList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.employee_head_img:
//                LocationBean locationBean = new LocationBean();
//                locationBean.setLat("30.663456");
//                locationBean.setLon("104.072227");
//                startActivity(new Intent(this, RoutePlanActivity.class).putExtra("targetLocation", locationBean));
                if (flag)
                    uploadInstantLocation("1");
                else
                    stopUploadInstantLocation("1");
                flag = !flag;
                break;
            case R.id.main_title:
                stopUploadInstantLocation("1");
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(receiver);
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
            case Task.ORDER_LIST:
                showDivider(true);
                switch (type) {
                    case 0:
                        noConfirmDataOrderList = ParseUtil.getInstance().parseOrderList(responseJson.getResult().toString());
                        if (noConfirmDataOrderList != null && !noConfirmDataOrderList.isEmpty()) {
                           /* for (int i=0;i<noConfirmDataOrderList.size();i++){
                                mateCircleMap.put(dataList.get(i).getId(),dataList.get(i));
                            }*/
                            if (noConfirmIsAdd) {
                                noConfirmShowOrderList.addAll(noConfirmDataOrderList);
                                noConfirmOrderAdapter.notifyDataSetChanged();
                                noConfirmIsAdd = false;
                            } else {
                                noConfirmShowOrderList = noConfirmDataOrderList;
                                noConfirmOrderAdapter = new OrderAdapter(noConfirmShowOrderList, MainActivity.this, type, handler);
                                order_list_lv.setAdapter(noConfirmOrderAdapter);
                            }
                        } else {
                            if (noConfirmIsAdd) {
                                noConfirmPageIndex--;
                                showToast(getString(R.string.no_more_data));
                                noConfirmIsAdd = false;
                            }
                        }
                        break;
                    case 1:
                        ingDataOrderList = ParseUtil.getInstance().parseOrderList(responseJson.getResult().toString());
                        if (ingDataOrderList != null && ingDataOrderList.size() != 0) {
                           /* for (int i=0;i<noConfirmDataOrderList.size();i++){
                                mateCircleMap.put(dataList.get(i).getId(),dataList.get(i));
                            }*/
                            if (ingIsAdd) {
                                ingShowOrderList.addAll(ingDataOrderList);
                                ingOrderAdapter.notifyDataSetChanged();
                                ingIsAdd = false;
                            } else {
                                ingShowOrderList = ingDataOrderList;
                                ingOrderAdapter = new OrderAdapter(ingShowOrderList, MainActivity.this, type, handler);

                                order_list_lv.setAdapter(ingOrderAdapter);
                            }
                        } else {
                            if (ingIsAdd) {
                                ingPageIndex--;
                                showToast(getString(R.string.no_more_data));
                                ingIsAdd = false;
                            }
                        }
                        break;
                    case 2:
                        finishedDataOrderList = ParseUtil.getInstance().parseOrderList(responseJson.getResult().toString());
                        if (finishedDataOrderList != null && finishedDataOrderList.size() != 0) {
                           /* for (int i=0;i<noConfirmDataOrderList.size();i++){
                                mateCircleMap.put(dataList.get(i).getId(),dataList.get(i));
                            }*/
                            if (finishedIsAdd) {
                                finishedShowOrderList.addAll(finishedDataOrderList);
                                finishedOrderAdapter.notifyDataSetChanged();
                                finishedIsAdd = false;
                            } else {
                                finishedShowOrderList = finishedDataOrderList;
                                finishedOrderAdapter = new OrderAdapter(finishedShowOrderList, MainActivity.this, type, handler);

                                order_list_lv.setAdapter(finishedOrderAdapter);
                            }
                        } else {
                            if (finishedIsAdd) {
                                finishedPageIndex--;
                                showToast(getString(R.string.no_more_data));
                                finishedIsAdd = false;
                            }
                        }
                        break;
                }
                break;
            case Task.ACCEPT:
                operateOrderInfo.setOrderState("1");
                noConfirmOrderAdapter.notifyDataSetChanged();
                break;
            case Task.SET_OUT:
                changeList(1);
                uploadInstantLocation(operateOrderInfo.getOrderId());
                break;
        }
        order_list_lv.onRefreshComplete();
    }

    @Override
    public void onFailed(int taskId, String errorMsg) {
        super.onFailed(taskId, errorMsg);
        dismissLoading();
        if (order_list_lv != null) {
            order_list_lv.onRefreshComplete();
        }
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getNewestDataList();
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        getMoreDataList();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.employee_order_noAccept:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.white));
                changeList(0);
                break;
            case R.id.employee_order_ing:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.white));
                changeList(1);
                break;
            case R.id.employee_order_finished:
                employee_order_noAccept.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_ing.setBackgroundColor(getResources().getColor(R.color.white));
                employee_order_finished.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                changeList(2);
                break;
        }
    }

    private void showDivider(boolean isShow) {
        if (order_list_lv != null)
            if (!isShow)
                order_list_lv.setDividerHeight(0);
            else if (order_list_lv.getDividerHeight() == 0)
                order_list_lv.setDividerHeight(DensityUtil.dip2px(this, 10));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            int select = data.getIntExtra("select", -1);
            switch (select) {
                case 1:
                    operateOrderInfo.setOrderState("1");
                    noConfirmOrderAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    changeList(1);
                case 3:
                    if (type == 0)
                        changeList(1);
                    break;
            }
        }
    }

    private void uploadInstantLocation(String orderId) {
        Log.v("Tag", "upload_orderId:" + orderId);
        serviceIntent = new Intent(this, LocationService.class);
        serviceIntent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
        serviceIntent.putExtra("uid", SharePreferenceUtil.getInstance().getUserId(this));
        startService(serviceIntent);
    }

    private void stopUploadInstantLocation(String orderId) {
        if (serviceIntent == null)
            serviceIntent = new Intent(this, LocationService.class);
        stopService(serviceIntent);
        LocationTools.getInstance(getApplicationContext()).stopListener(orderId);
    }
}
