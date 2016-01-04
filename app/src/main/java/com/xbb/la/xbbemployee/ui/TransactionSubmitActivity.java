package com.xbb.la.xbbemployee.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
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
 * <p/>
 * 创建时间：2015/11/6 16:21
 * 描述：提交任务
 */

public class TransactionSubmitActivity extends TitleActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver finishReceiver;

    private UploadTransaction uploadTransaction;

    private String orderId;

    @ViewInject(R.id.transaction_commit_btn)
    private Button transaction_commit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_submit);
    }


    @Override
    public void initData() {
        setTitle(getString(R.string.transaction_submit_title));
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        finishReceiver = new FinisActivityReceiver(this);
        localBroadcastManager.registerReceiver(finishReceiver, intentFilter);

        getUploadTransactionInfo();
        title_left_img.setOnClickListener(this);
        transaction_commit_btn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                Intent intent = new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH);
                intent.putExtra("step", 4);
                intent.putExtra("insert", true);
                localBroadcastManager.sendBroadcast(intent);
                break;
            case R.id.transaction_commit_btn:
                if (uploadTransaction != null) {
                    if (apiRequest == null)
                        apiRequest = new ApiRequest(this);
                    apiRequest.commitMission(orderId, userId, uploadTransaction);
                } else {
                    showToast(getString(R.string.order_submit_info));
                }
                break;
        }
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
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Task.MISSION_COMPLETE:
                DBHelperMethod.getInstance().deleteTransaction(orderId, userId);
                showToast("上传成功");
                Intent intent=new Intent(Constant.IntentAction.MISSION_COMPLETE);
                localBroadcastManager.sendBroadcast(intent);
                finish();
                break;
        }
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
    }
}
