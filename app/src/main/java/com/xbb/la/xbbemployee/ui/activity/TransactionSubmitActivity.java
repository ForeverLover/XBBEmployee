package com.xbb.la.xbbemployee.ui.activity;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.modellibrary.bean.Recommand;
import com.xbb.la.modellibrary.bean.Transaction;
import com.xbb.la.modellibrary.bean.UploadRecommand;
import com.xbb.la.modellibrary.bean.UploadTransaction;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.modellibrary.utils.SystemUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.receiver.FinisActivityReceiver;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * <p>
 * 创建时间：2015/11/6 16:21
 * 描述：提交任务
 */

public class TransactionSubmitActivity extends BaseActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver finishReceiver;

    private UploadTransaction uploadTransaction;

    private String orderId;

    @ViewInject(R.id.submit_tip_tv)
    private TextView submit_tip_tv;

    private boolean canOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_submit);
    }


    @Override
    public void initData() {
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        finishReceiver = new FinisActivityReceiver(this);
        localBroadcastManager.registerReceiver(finishReceiver, intentFilter);
        canOp = true;
        getUploadTransactionInfo();
    }

    @Override
    public void initView() {
        super.initView();
        submit_tip_tv.setText(StringUtil.getDiffStr(getString(R.string.transaction_submit_tip), 12, this, 16, 14));
    }

    private void getUploadTransactionInfo() {
        Transaction transaction = DBHelperMethod.getInstance().getTransaction(orderId, userId);
        if (transaction != null) {
            uploadTransaction = new UploadTransaction();
            uploadTransaction.setStartNormalFileAlbum(SystemUtil.getInstance().convertPathListToFileList(StringUtil.covertStringToStringList(transaction.getNormalAlbumBefore())));
            uploadTransaction.setStartDamageFileAlbum(SystemUtil.getInstance().convertPathListToFileList(StringUtil.covertStringToStringList(transaction.getDamageAlbumBefore())));
            uploadTransaction.setEndDamageFileAlbum(SystemUtil.getInstance().convertPathListToFileList(StringUtil.covertStringToStringList(transaction.getDamageAlbumAfter())));
            uploadTransaction.setEndNormalFileAlbum(SystemUtil.getInstance().convertPathListToFileList(StringUtil.covertStringToStringList(transaction.getNormalAlbumAfter())));
            uploadTransaction.setEndTime(transaction.getEndTime());
            uploadTransaction.setStartTime(transaction.getStartTime());
            uploadTransaction.setOrderId(orderId);
            uploadTransaction.setEmployeeId(userId);
            List<Recommand> recommandList = DBHelperMethod.getInstance().getRecommandList(orderId, userId);
            List<UploadRecommand> uploadRecommandList = null;
            if (recommandList != null) {
                uploadRecommandList = new ArrayList<UploadRecommand>();
                for (Recommand recommand : recommandList) {
                    if (recommand != null) {
                        UploadRecommand uploadRecommand = new UploadRecommand();
                        uploadRecommand.setDiyID(recommand.getSelectedDIYProduct().getId());
                        uploadRecommand.setRecommandFileAlbum(SystemUtil.getInstance().convertPathListToFileList(recommand.getIntroAlbum()));
                        uploadRecommand.setRecommandRemark(recommand.getRemark());
                        uploadRecommandList.add(uploadRecommand);
                    }
                }
            }
            uploadTransaction.setUploadRecommandList(uploadRecommandList);
        }

    }

    protected void markSucceed() {
        Intent intent = new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH);
        intent.putExtra("step", 4);
        intent.putExtra("insert", true);
        localBroadcastManager.sendBroadcast(intent);
        Constant.TempSet.orderUpdateArray.put(2, true);
    }

    @OnClick({R.id.transaction_cancel_btn, R.id.transaction_submit_btn})
    public void onClick(View v) {
        if (canOp) {
            switch (v.getId()) {
                case R.id.transaction_submit_btn:
                    if (uploadTransaction != null) {
                        if (apiRequest == null)
                            apiRequest = new ApiRequest(this);
                        apiRequest.commitMission(orderId, userId, uploadTransaction);
                        canOp = false;
                    } else {
                        showToast(getString(R.string.order_submit_info));
                    }
                    break;
                case R.id.transaction_cancel_btn:
                    finish();
                    break;
            }
        } else {
            showToast(getString(R.string.transaction_uploading));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (canOp)
                finish();
            else
                showToast(getString(R.string.transaction_uploading));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(finishReceiver);
    }

    @Override
    public void onPrepare(int taskId) {
        super.onPrepare(taskId);
        showLoading();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                showToast(getString(R.string.transaction_uploading));
                return false;
            }
        });

    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Task.MISSION_COMPLETE:
                DBHelperMethod.getInstance().deleteTransaction(orderId, userId);
                showToast("上传成功");
                markSucceed();
                Intent intent = new Intent(Constant.IntentAction.MISSION_COMPLETE);
                intent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
                localBroadcastManager.sendBroadcast(intent);
                finish();
                break;
        }
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
        canOp = true;
    }
}
