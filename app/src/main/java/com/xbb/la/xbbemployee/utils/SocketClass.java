package com.xbb.la.xbbemployee.utils;

import android.content.Context;
import android.util.Log;

import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.XRequestCallBack;
import com.xbb.la.modellibrary.net.data.UserLocation;

import java.io.Serializable;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/7 10:34
 * 描述：
 */

public class SocketClass {
    public String TAG = "SocketClass";

    private Context ctx;

    private ApiRequest apiRequest;


    public SocketClass(final String site, final int port,
                       final Context ctx) {
        this.ctx = ctx;
        this.apiRequest = new ApiRequest(new XRequestCallBack() {
            @Override
            public void onPrepare(int taskId) {

            }

            @Override
            public void onSuccess(int taskId, Object... params) {

            }

            @Override
            public void onEnd(int taskId) {

            }

            @Override
            public void onFailed(int taskId, String errorMsg) {

            }

            @Override
            public void onLoading(int taskId, long count, long current) {

            }

            @Override
            public boolean isCallBack() {
                return false;
            }
        });
    }


    // send message
    public synchronized void sendMsg(int action, Serializable msg) {
        if (msg instanceof UserLocation && msg != null) {
            apiRequest.uploadLocation(((UserLocation) msg).getUid(), ((UserLocation) msg).getOrderId(), ((UserLocation) msg).getLat() + "", ((UserLocation) msg).getLng() + "");
            Log.v("Tag", "upload:orderId:" + ((UserLocation) msg).getOrderId() + "(" + ((UserLocation) msg).getLat() + "," + ((UserLocation) msg).getLng()+")");
        }
    }


}
