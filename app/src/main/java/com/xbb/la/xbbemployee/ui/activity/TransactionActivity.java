package com.xbb.la.xbbemployee.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Transaction;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 15:27
 * 描述：业务区域
 */

public class TransactionActivity extends TitleActivity {
    @ViewInject(R.id.transaction_start_btn)
    private Button transaction_start_btn;
    @ViewInject(R.id.transaction_end_btn)
    private Button transaction_end_btn;
    @ViewInject(R.id.transaction_recommand_btn)
    private Button transaction_recommand_btn;
    @ViewInject(R.id.transaction_submit_btn)
    private Button transaction_submit_btn;

    @ViewInject(R.id.transaction_recommand_img)
    private ImageView transaction_recommand_img;
    @ViewInject(R.id.transaction_end_img)
    private ImageView transaction_end_img;
    @ViewInject(R.id.transaction_start_img)
    private ImageView transaction_start_img;


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
                            transaction_start_btn.setTextColor(Color.WHITE);
                            transaction_start_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                            transaction_start_img.setImageResource(R.mipmap.process_finished_img);
                            break;
                        case 2:
                            transaction_end_btn.setTextColor(Color.WHITE);
                            transaction_end_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                            transaction_end_img.setImageResource(R.mipmap.process_finished_img);
                            break;
                        case 3:
                            transaction_recommand_btn.setTextColor(Color.WHITE);
                            transaction_recommand_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                            transaction_recommand_img.setImageResource(R.mipmap.process_finished_img);
                            break;
                        case 4:
                            transaction_submit_btn.setTextColor(Color.WHITE);
                            transaction_submit_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                            break;
                    }
                }
            }
            if (Constant.IntentAction.MISSION_COMPLETE.equals(action)) {
                finish();
            }
        }
    };


    @Override
    public void initData() {
        setTitle(getString(R.string.transaction_title));
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        transaction = DBHelperMethod.getInstance().getTransaction(orderId, userId);
        if (transaction != null) {
            step = transaction.getCurrentStep();
        }
        switch (step) {
            case 4:
                transaction_submit_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                transaction_submit_btn.setTextColor(Color.WHITE);
            case 3:
                transaction_recommand_btn.setTextColor(Color.WHITE);
                transaction_recommand_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                transaction_recommand_img.setImageResource(R.mipmap.process_finished_img);
            case 2:
                transaction_end_btn.setTextColor(Color.WHITE);
                transaction_end_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                transaction_end_img.setImageResource(R.mipmap.process_finished_img);
            case 1:
                transaction_start_btn.setTextColor(Color.WHITE);
                transaction_start_btn.setBackgroundResource(R.drawable.btn_solid_s_bg);
                transaction_start_img.setImageResource(R.mipmap.process_finished_img);
                break;
        }
        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        intentFilter.addAction(Constant.IntentAction.MISSION_COMPLETE);
        localBroadcastManager.registerReceiver(transactionReceiver, intentFilter);
        transaction_start_btn.setOnClickListener(this);
        transaction_end_btn.setOnClickListener(this);
        transaction_recommand_btn.setOnClickListener(this);
//        transaction_suggest_btn.setOnClickListener(this);
        transaction_submit_btn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
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
