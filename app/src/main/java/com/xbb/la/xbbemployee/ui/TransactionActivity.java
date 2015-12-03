package com.xbb.la.xbbemployee.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.Transaction;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 15:27
 * 描述：业务区域
 */

public class TransactionActivity extends BaseActivity {
    private Button transaction_start_btn;
    private Button transaction_end_btn;
    private Button transaction_recommand_btn;
    private Button transaction_suggest_btn;
    private Button transaction_submit_btn;


    private String orderId;

    private int step;

    private Transaction transaction;

    private IntentFilter intentFilter;
    private BroadcastReceiver transactionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.IntentAction.TRANSACTION_TO_FINISH.equals(action)) {
                boolean isInsert = intent.getBooleanExtra("insert", false);
                int currentStep = intent.getIntExtra("step", 0);
                if (isInsert) {
                    step = currentStep;
                    switch (currentStep) {
                        case 1:
                            transaction_start_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                            break;
                        case 2:
                            transaction_end_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                            break;
                        case 3:
                            transaction_recommand_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                            break;
                        case 4:
                            transaction_submit_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                            break;
                    }
                }
            }
            if (Constant.IntentAction.MISSION_COMPLETE.equals(action)){
                finish();
            }
        }
    };

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_transaction);
        page_title = (TextView) findViewById(R.id.title_center_txt);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        transaction_start_btn = (Button) findViewById(R.id.transaction_start_btn);
        transaction_end_btn = (Button) findViewById(R.id.transaction_end_btn);
        transaction_recommand_btn = (Button) findViewById(R.id.transaction_recommand_btn);
        transaction_suggest_btn = (Button) findViewById(R.id.transaction_suggest_btn);
        transaction_submit_btn = (Button) findViewById(R.id.transaction_submit_btn);
    }

    @Override
    protected void initData() {
        page_title.setText(getString(R.string.transaction_title));
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        showToast("orderId:" + orderId);
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        transaction = DBHelperMethod.getInstance().getTransaction(orderId, userId);
        if (transaction != null) {
            step = transaction.getCurrentStep();
        }
        switch (step) {
            case 4:
                transaction_submit_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            case 3:
                transaction_recommand_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            case 2:
                transaction_end_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            case 1:
                transaction_start_btn.setBackgroundColor(getResources().getColor(R.color.dark_gray));
                break;
        }
        intentFilter=new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        intentFilter.addAction(Constant.IntentAction.MISSION_COMPLETE);
        localBroadcastManager.registerReceiver(transactionReceiver,intentFilter);
        transaction_start_btn.setOnClickListener(this);
        transaction_end_btn.setOnClickListener(this);
        transaction_recommand_btn.setOnClickListener(this);
//        transaction_suggest_btn.setOnClickListener(this);
        transaction_submit_btn.setOnClickListener(this);
        title_left_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showToast("step=" + step);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.transaction_start_btn:
                intent = new Intent(TransactionActivity.this, TransactionStartActivity.class);
                if (step >= 1)
                    intent.putExtra("select", 2);
                break;
            case R.id.transaction_end_btn:
                if (step >= 1)
                    intent = new Intent(TransactionActivity.this, TransactionEndActivity.class);
                if (step >= 2)
                    intent.putExtra("select", 2);
                break;
            /*case R.id.transaction_suggest_btn:
                if (step >= 4)
                    intent = new Intent(TransactionActivity.this, TransactionSuggestActivity.class);
                break;*/
            case R.id.transaction_submit_btn:
                if (step >= 3)
                    intent = new Intent(TransactionActivity.this, TransactionSubmitActivity.class);
                if (step >= 4)
                    intent.putExtra("select", 2);
                break;
            case R.id.transaction_recommand_btn:
                if (step >= 2)
                    intent = new Intent(TransactionActivity.this, TransactionRecommandActivity.class);
                if (step >= 3)
                    intent.putExtra("select", 2);
                break;
        }
        if (intent != null) {
            showToast("orderId:"+orderId);
            intent.putExtra(Constant.IntentVariable.ORDER_ID, orderId);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(transactionReceiver);
    }
}
