package com.xbb.la.xbbemployee.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.DensityUtil;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.modellibrary.utils.SystemUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.OrderAdapter;
import com.xbb.la.xbbemployee.config.BaseFragment;
import com.xbb.la.xbbemployee.listener.DealOrderSetListener;
import com.xbb.la.xbbemployee.listener.MyTabActivityResultListener;
import com.xbb.la.xbbemployee.ui.activity.MainActivity;
import com.xbb.la.xbbemployee.ui.activity.OrderInfoActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.utils.SystemUtils;

import java.util.List;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MissionListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, MyTabActivityResultListener, DealOrderSetListener {
    private IApiRequest apiRequest;
    private String userId;

    private List<OrderInfo> showOrderList;
    private List<OrderInfo> dataOrderList;

    private boolean noConfirmIsAdd;


    private int pageIndex;

    private int pageSize = 10;

    private boolean showLoading;

    private int type;
    private OrderAdapter orderAdapter;
    private OrderInfo operateOrderInfo;
    private int operateIndex;
    private boolean shareData;
    @ViewInject(R.id.mission_list_lv)
    private PullToRefreshListView mission_list_lv;
    private boolean needUpdate = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            operateOrderInfo = (OrderInfo) msg.obj;
            operateIndex = msg.arg1;
            SystemUtils.getInstance().addOperateOrder(operateOrderInfo.getOrderId(), operateOrderInfo);
            switch (msg.what) {
                case Task.ACCEPT:
                    if (apiRequest == null)
                        apiRequest = new ApiRequest(MissionListFragment.this);
                    apiRequest.confirm(operateOrderInfo.getOrderId(), userId);
                    break;
                case Task.SET_OUT:
                    if (apiRequest == null)
                        apiRequest = new ApiRequest(MissionListFragment.this);
                    apiRequest.leave(operateOrderInfo.getOrderId(), userId);
                    break;
                case 100:
                    Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                    intent.putExtra(Constant.IntentVariable.ORDER_ID, operateOrderInfo.getOrderId());
                    getActivity().startActivityForResult(intent, 1000);
                    break;

            }
        }
    };

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_mission_list;
    }

    @Override
    protected void onCreateView(View contentView, Bundle savedInstanceState, LayoutInflater inflater) {

    }

    @Override
    public void initData() {
        super.initData();
//        if (getArguments() != null) {
//            type = getArguments().getInt("type", -1);
//        }

    }

    @Override
    public void initView() {
        super.initView();
        mission_list_lv.setMode(PullToRefreshBase.Mode.BOTH);
        mission_list_lv.setOnRefreshListener(this);
    }

    @Override
    protected void lazyLoad() {
        if (getArguments() != null) {
            type = getArguments().getInt("type", -1);
        }
        shareData = Constant.TempSet.orderUpdateArray.get(type, false);
        if (needUpdate || shareData) {
            apiRequest = new ApiRequest(this);
            userId = SharePreferenceUtil.getInstance().getUserId(getActivity());
            pageIndex = 1;
            getOrderList();
        }
    }

    public void getOrderList() {
        if (apiRequest == null)
            apiRequest = new ApiRequest(this);

        apiRequest.getOrderList(userId, type, pageIndex, pageSize);

    }

    /**
     * 下拉刷新
     */
    private void getNewestDataList() {
        showLoading = false;
        pageIndex = 1;
        getOrderList();

    }

    /**
     * 上拉加载
     */
    private void getMoreDataList() {
        showLoading = false;
        noConfirmIsAdd = true;
        pageIndex++;
        getOrderList();
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
            case Task.ORDER_LIST:
                showDivider(true);
                dataOrderList = ParseUtil.getInstance().parseOrderList(responseJson.getResult().toString());
                if (dataOrderList != null && !dataOrderList.isEmpty()) {
                           /* for (int i=0;i<dataOrderList.size();i++){
                                mateCircleMap.put(dataList.get(i).getId(),dataList.get(i));
                            }*/
                    if (noConfirmIsAdd) {
                        showOrderList.addAll(dataOrderList);
                        orderAdapter.notifyDataSetChanged();
                        noConfirmIsAdd = false;
                    } else {
                        showOrderList = dataOrderList;
                        orderAdapter = new OrderAdapter(showOrderList, getActivity(), type, handler);
                        mission_list_lv.setAdapter(orderAdapter);
                    }
                } else {
                    if (noConfirmIsAdd) {
                        pageIndex--;
                        showToast(getString(R.string.no_more_data));
                        noConfirmIsAdd = false;
                    }
                }
                if (needUpdate || shareData) {
                    Constant.TempSet.orderUpdateArray.put(type, false);
                    needUpdate = false;
                }
                break;
        }
        mission_list_lv.onRefreshComplete();
    }

    @Override
    public void onSuccess(int taskId, String flag, Object... params) {
        ResponseJson responseJson = (ResponseJson) params[0];
        String orderId = flag;
        switch (taskId) {
            case Task.ACCEPT:
                operateOrderInfo = SystemUtils.getInstance().getOrderById(orderId);
                if (operateOrderInfo == null) {
                    for (OrderInfo orderInfo : showOrderList) {
                        if (orderInfo.getOrderId().equals(orderId)) {
                            operateOrderInfo = orderInfo;
                            break;
                        }
                    }
                }
                operateOrderInfo.setOrderState("1");
                orderAdapter.notifyDataSetChanged();
                break;
            case Task.SET_OUT:
                operateOrderInfo = SystemUtils.getInstance().getOrderById(orderId);
                if (operateOrderInfo == null) {
                    for (OrderInfo orderInfo : showOrderList) {
                        if (orderInfo.getOrderId().equals(orderId)) {
                            operateOrderInfo = orderInfo;
                            break;
                        }
                    }
                }
                if (operateOrderInfo != null) {
                    showOrderList.remove(operateOrderInfo);
                    orderAdapter.notifyDataSetChanged();
                }
                SystemUtils.getInstance().removeOperateOrder(orderId);
                Constant.TempSet.orderUpdateArray.put(1, true);
                ((MainActivity) getActivity()).employee_orderstate_group.check(R.id.employee_order_ing);
                ((MainActivity) getActivity()).uploadInstantLocation(orderId);
                break;
        }
    }

    @Override
    public void onFailed(int taskId, String errorMsg) {
        super.onFailed(taskId, errorMsg);
        dismissLoading();
        if (mission_list_lv != null) {
            mission_list_lv.onRefreshComplete();
        }
    }

    private void showDivider(boolean isShow) {
        if (mission_list_lv != null) {
            if (!isShow)
                mission_list_lv.setDividerHeight(0);
            else {
                if (mission_list_lv.getDividerHeight() <= 0) {
                    mission_list_lv.setDividerHeight(DensityUtil.dip2px(getActivity(), 10));
                }
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        getNewestDataList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        getMoreDataList();
    }

    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (getActivity().RESULT_OK == resultCode && data != null) {
            int select = data.getIntExtra("select", -1);
            switch (requestCode) {
                case 1000:
                    switch (select) {
                        case 1:
                            String orderId = data.getStringExtra("id");
                            if (showOrderList != null) {
                                OrderInfo opOrder = SystemUtils.getInstance().getOrderById(orderId);
                                if (opOrder == null) {
                                    for (OrderInfo orderInfo : showOrderList) {
                                        if (orderInfo.getOrderId().equals(orderId)) {
                                            opOrder = orderInfo;
                                            break;
                                        }
                                    }
                                }
                                opOrder.setOrderState("1");
                                orderAdapter.notifyDataSetChanged();
                            }
                            break;
                    }
                    break;
            }

        }
    }

    @Override
    public void dealOrderSetById(String orderId) {
        if (showOrderList != null) {
            OrderInfo opOrder = SystemUtils.getInstance().getOrderById(orderId);
            if (opOrder == null) {
                for (OrderInfo orderInfo : showOrderList) {
                    if (orderInfo.getOrderId().equals(orderId)) {
                        opOrder = orderInfo;
                        break;
                    }
                }
            }
            if (opOrder != null) {
                showOrderList.remove(opOrder);
                orderAdapter.notifyDataSetChanged();
            }
        }
        SystemUtils.getInstance().removeOperateOrder(orderId);
    }

    @Override
    public void onResume() {
        super.onResume();
        MLog.v("Tag", "resume:" + type);
//        getOrderList();
    }
}