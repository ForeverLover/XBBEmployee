package com.xbb.la.xbbemployee.ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Reminder;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.provider.ReminderAdapter;
import com.xbb.la.xbbemployee.receiver.FinisActivityReceiver;
import com.xbb.la.xbbemployee.widget.CustomListView;

import java.util.HashSet;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:20
 * 描述：技师建议
 */

public class TransactionSuggestActivity extends TitleActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver finishReceiver;

    private List<Reminder> reminderList;
    private ReminderAdapter reminderAdapter;

    @ViewInject(R.id.suggest_list_lv)
    private CustomListView suggest_list_lv;
    @ViewInject(R.id.suggest_ensure_btn)
    private Button suggest_ensure_btn;
    @ViewInject(R.id.suggest_remark_et)
    private EditText suggest_remark_et;

    private int textSum = 150;
    @ViewInject(R.id.suggest_textNum)
    private TextView suggest_textNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_suggest);
    }


    @Override
    public void initData() {
        setTitle(getString(R.string.transaction_suggest));
        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        reminderList = DBHelperMethod.getInstance().getReminderList();
        if (reminderList != null && !reminderList.isEmpty()) {
            reminderAdapter = new ReminderAdapter(this, reminderList);
            suggest_list_lv.setAdapter(reminderAdapter);
        }
        finishReceiver = new FinisActivityReceiver(this);
        registerReceiver(finishReceiver, intentFilter);
        title_left_img.setOnClickListener(this);
        suggest_ensure_btn.setOnClickListener(this);
        suggest_remark_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            public void afterTextChanged(Editable s) {
                int number = s.length();
                suggest_textNum.setText("" + number);
                selectionStart = suggest_remark_et.getSelectionStart();
                selectionEnd = suggest_remark_et.getSelectionEnd();
                if (temp.length() > textSum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    suggest_remark_et.setText(s);
                    suggest_remark_et.setSelection(tempSelection);//设置光标在最后
//                    showToast(getString(R.string.feedback_content_long));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_img:
                localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH));
                break;
            case R.id.suggest_ensure_btn:
                if (reminderAdapter!=null){
                    HashSet<Reminder> selectReminderList=reminderAdapter.getSelectedReminderList();
                    if (selectReminderList==null||selectReminderList.isEmpty()){
                        return;
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(finishReceiver);
    }
}
