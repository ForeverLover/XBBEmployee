package com.xbb.la.xbbemployee.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class FeedBackActivity extends TitleActivity {
    @ViewInject(R.id.content_count_tv)
    private TextView content_count_tv;
    @ViewInject(R.id.feedback_content_et)
    private EditText feedback_content_et;

    private String feedback;
    private int textSum;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.other_feedback);
    }

    @Override
    public void initData() {
        super.initData();
        textSum = 200;
        uid = SharePreferenceUtil.getInstance().getUserId(this);
        apiRequest = new ApiRequest(this);
    }

    @Override
    public void initListener() {
        super.initListener();
        feedback_content_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            public void afterTextChanged(Editable s) {
                int number = s.length();
                content_count_tv.setText("" + number);
                selectionStart = feedback_content_et.getSelectionStart();
                selectionEnd = feedback_content_et.getSelectionEnd();
                if (temp.length() > textSum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    feedback_content_et.setText(s);
                    feedback_content_et.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    protected void onClickTitleLeft(View v) {
        hideSoftInputView();
        super.onClickTitleLeft(v);

    }

    @OnClick(R.id.feedback_submit_btn)
    private void submitFeedback(View v) {
        feedback = feedback_content_et.getText() != null ? feedback_content_et.getText().toString() : "";
        if (StringUtil.isEmpty(feedback)) {
            showToast(R.string.feedback_null);
            return;
        }
        if (feedback.length() > textSum) {
            showToast(R.string.feedback_error);
            return;
        }
        apiRequest.feedback(uid, feedback);
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        switch (taskId) {
            case Task.FEEDBACK:
                showToast(R.string.feedback_succeed);
                hideSoftInputView();
                finish();
                break;
        }
    }
}
