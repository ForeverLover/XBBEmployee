package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Message;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
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
public class MessageDetailsActivity extends TitleActivity {
    @ViewInject(R.id.msginfo_date_tv)
    private TextView msginfo_date_tv;
    @ViewInject(R.id.msginfo_time_tv)
    private TextView msginfo_time_tv;
    @ViewInject(R.id.msginfo_content_tv)
    private TextView msginfo_content_tv;
    @ViewInject(R.id.msginfo_toOrder_btn)
    private TextView msginfo_toOrder_btn;

    private String uid;
    private String messageId;
    private int messageType;
    private Message message;
    private boolean isRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
        MLog.v("Tag","details");
    }

    @Override
    public void initData() {
        super.initData();
        uid = SharePreferenceUtil.getInstance().getUserId(this);
        messageId = getIntent().getStringExtra(Constant.IntentVariable.MESSAGE_ID);
        apiRequest = new ApiRequest(this);
        apiRequest.getMessageDetail(uid, messageId);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.msg_details_title);
    }

    @Override
    public void initListener() {
        super.initListener();
        msginfo_toOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (messageType) {
                    case 1:
                        Intent intent = new Intent(MessageDetailsActivity.this, OrderInfoActivity.class);
                        intent.putExtra(Constant.IntentVariable.ORDER_ID, message.getExtraId());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void bindViewWithData() {
        if (message != null) {
            isRead = true;
            msginfo_content_tv.setText(message.getContent());
            msginfo_date_tv.setText(StringUtil.getTimeFromStamp(message.getTime()));
            messageType=message.getType();
        }

    }


    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.GET_MESSAGE_DETAIL:
                message = ParseUtil.getInstance().parserMessageInfo(responseJson.getResult().toString());
                bindViewWithData();
                break;
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

    @Override
    protected void onClickTitleLeft(View v) {
        doFinish();
    }

    private void doFinish() {
        Intent intent=new Intent();
        intent.putExtra(Constant.IntentVariable.MESSAGE_ISREAD,isRead);
        intent.putExtra(Constant.IntentVariable.MESSAGE_ID,messageId);
        setResult(RESULT_OK, intent);
        finish();
    }


}
