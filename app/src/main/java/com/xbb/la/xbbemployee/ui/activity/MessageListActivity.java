package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Message;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.MessageAdapter;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.CustomListView;

import java.util.List;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MessageListActivity extends TitleActivity {
    @ViewInject(R.id.msg_list_lv)
    private CustomListView msg_list_lv;

    private List<Message> msgList;
    private MessageAdapter messageAdapter;

    private String uid;
    private IApiRequest mApiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msglist);
    }

    @Override
    public void initData() {
        super.initData();
        uid= SharePreferenceUtil.getInstance().getUserId(this);
        mApiRequest=new ApiRequest(this);

    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.menu_msg_text);
    }

    @Override
    public void initListener() {
        super.initListener();
        msg_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessageListActivity.this, MessageDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.GET_MESSAGE:
                if (msgList != null && !msgList.isEmpty()) {
                    messageAdapter = new MessageAdapter(this, msgList, R.layout.msg_list_item);
                    msg_list_lv.setAdapter(messageAdapter);
                }
                break;
        }
    }
}
