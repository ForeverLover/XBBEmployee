package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Message;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.MessageAdapter;
import com.xbb.la.xbbemployee.adapter.OrderAdapter;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.CustomListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MessageListActivity extends TitleActivity {
    @ViewInject(R.id.msg_list_lv)
    private PullToRefreshListView msg_list_lv;

    private List<Message> msgShowList;
    private List<Message> msgDataList;
    private MessageAdapter messageAdapter;

    private String uid;

    private int pageIndex;
    private int pageSize;
    private boolean showLoading;
    private boolean isAdd;

    private Map<String, Message> msgMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msglist);
    }

    @Override
    public void initData() {
        super.initData();
        uid = SharePreferenceUtil.getInstance().getUserId(this);
        apiRequest = new ApiRequest(this);
        msgMap = new HashMap<String, Message>();
        pageIndex = 1;
        pageSize = 10;

    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.menu_msg_text);
        msg_list_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        getMessageList();
    }

    @Override
    public void initListener() {
        super.initListener();
        msg_list_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                showLoading = false;
                isAdd = true;
                pageIndex++;
                getMessageList();
            }


        });

        msg_list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && position <=msgShowList.size()) {
                    Message msg = msgShowList.get(position-1);
                    Intent intent = new Intent(MessageListActivity.this, MessageDetailsActivity.class);
                    intent.putExtra(Constant.IntentVariable.MESSAGE_ID, msg.getMsgId());
                    startActivityForResult(intent, Constant.RequestCode.READ_MSG);
                    msgMap.put(msg.getMsgId(), msg);
                }
            }
        });
    }

    private void getMessageList() {
        if (apiRequest == null)
            apiRequest = new ApiRequest(this);
        apiRequest.getMessageList(uid, pageIndex, pageSize);
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        switch (taskId) {
            case Task.GET_MESSAGE:
                msg_list_lv.onRefreshComplete();
                break;
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.GET_MESSAGE:
                msgDataList = ParseUtil.getInstance().parseMessageList(responseJson.getResult().toString());
                if (msgDataList != null && !msgDataList.isEmpty()) {
                    if (isAdd) {
                        msgShowList.addAll(msgDataList);
                        messageAdapter.notifyDataSetChanged();
                        isAdd = false;
                    } else {
                        msgShowList = msgDataList;
                        messageAdapter = new MessageAdapter<>(this, msgShowList, R.layout.msg_list_item);
                        msg_list_lv.setAdapter(messageAdapter);
                    }
                } else {
                    if (isAdd) {
                        pageIndex--;
                        showToast(getString(R.string.no_more_data));
                        isAdd = false;
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            switch (requestCode) {
                case Constant.RequestCode.READ_MSG:
                    boolean readFlag = data.getBooleanExtra(Constant.IntentVariable.MESSAGE_ISREAD, false);
                    if (readFlag) {
                        String msgId = data.getStringExtra(Constant.IntentVariable.MESSAGE_ID);
                        Message opMessage = null;
                        if (msgMap != null && msgMap.containsKey(msgId)) {
                            opMessage = msgMap.get(msgId);
                        } else {
                            if (msgShowList != null)
                                for (Message message : msgShowList) {
                                    if (message.getMsgId().equals(msgId))
                                        opMessage = message;
                                }
                        }
                        if (opMessage != null)
                            opMessage.setRead(true);
                        messageAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
