package com.xbb.la.xbbemployee.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.Recommand;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.RecommandAdapter;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.receiver.FinisActivityReceiver;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:21
 * 描述：技师推荐
 */

public class TransactionRecommandActivity extends BaseActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver finishReceiver;

    private static final int GET_RECOMMAND = 1004;
    private static final int UPDATE_RECOMMAND = 1005;

    private List<Recommand> recommandList;
    private RecommandAdapter recommandAdapter;

    private ListView recommand_list_lv;

    private String userId;
    private String orderId;

    private Button recommand_skip_btn;
    private Button recommand_submit_btn;

    /**
     * 1-添加建议数据
     * 2-修改建议数据
     */
    private int select;

    private int pointIndex;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_transaction_recommand);
        page_title = (TextView) findViewById(R.id.title_center_txt);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_right_text = (TextView) findViewById(R.id.title_right_text);
        recommand_list_lv = (ListView) findViewById(R.id.recommand_list_lv);
        recommand_skip_btn = (Button) findViewById(R.id.recommand_skip_btn);
        recommand_submit_btn = (Button) findViewById(R.id.recommand_submit_btn);
    }

    @Override
    protected void initData() {
        page_title.setText(getString(R.string.transaction_recommand));
        title_right_text.setText(getString(R.string.recommand_add_lable));
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        select = getIntent().getIntExtra("select", 1);
        if (1 != select)
            getLocalList();

        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        finishReceiver = new FinisActivityReceiver(this);
        localBroadcastManager.registerReceiver(finishReceiver, intentFilter);
        title_left_img.setOnClickListener(this);
        title_right_text.setOnClickListener(this);
        recommand_submit_btn.setOnClickListener(this);
        recommand_skip_btn.setOnClickListener(this);
        recommand_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TransactionRecommandActivity.this, AddRecommandActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("recommand", recommandList.get(position));
                startActivityForResult(intent, UPDATE_RECOMMAND);
                pointIndex = position;
            }


        });
    }

    private void getLocalList() {
        recommandList = DBHelperMethod.getInstance().getRecommandList(orderId, userId);
        if (recommandList != null && !recommandList.isEmpty()) {
            recommandAdapter = new RecommandAdapter(this, recommandList);
            recommand_list_lv.setAdapter(recommandAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH));
                break;
            case R.id.title_right_text:

                startActivityForResult(new Intent(this, AddRecommandActivity.class).putExtra("type",1), GET_RECOMMAND);
                break;

            case R.id.recommand_submit_btn:
                if (recommandList != null && !recommandList.isEmpty()) {
                    boolean flag;
                    if (1 == select)
                        flag = DBHelperMethod.getInstance().insertRecommands(recommandList, userId, orderId, true);
                    else
                        flag = DBHelperMethod.getInstance().insertRecommands(recommandList, userId, orderId, false);
                    if (flag) {
                        Intent intent = new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH);
                        intent.putExtra("step", 3);
                        intent.putExtra("insert", 1 == select);
                        localBroadcastManager.sendBroadcast(intent);
                    } else {
                        showToast("插入失败");
                    }

                }
                break;
            case R.id.recommand_skip_btn:
                if (1 == select) {
                    if (DBHelperMethod.getInstance().skipRecommand(orderId, userId)) {
                        Intent skipIntent = new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH);
                        skipIntent.putExtra("step", 3);
                        skipIntent.putExtra("insert", true);
                        localBroadcastManager.sendBroadcast(skipIntent);
                    } else {
                        showToast("保存失败");
                    }
                } else
                    localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(finishReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            Recommand recommand = (Recommand) data.getSerializableExtra("recommand");
            switch (requestCode) {
                case GET_RECOMMAND:

                    if (recommand != null) {
                        if (recommandList == null)
                            recommandList = new ArrayList<Recommand>();
                        recommandList.add(recommand);
                        if (recommandAdapter == null) {
                            recommandAdapter = new RecommandAdapter(this, recommandList);
                            recommand_list_lv.setAdapter(recommandAdapter);
                        } else {
                            recommandAdapter.notifyDataSetChanged();
                        }
                    }
                case UPDATE_RECOMMAND:
                    boolean hasChange = data.getBooleanExtra("change", false);
                    if (hasChange) {
                        if (recommand != null && pointIndex > -1)
                            recommandList.set(pointIndex, recommand);
                        recommandAdapter.notifyDataSetChanged();
                        pointIndex = -1;
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doFinish() {
        if (recommandList == null || recommandList.isEmpty()) {
            finish();
        } else {
            if (1 == select)
                new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_title_tip)).setMessage(getString(R.string.dialog_content_tip)).setNegativeButton(getString(R.string.dialog_cancel_tip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton(getString(R.string.dialog_ensure_tip), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
            else
                finish();
        }
    }
}
