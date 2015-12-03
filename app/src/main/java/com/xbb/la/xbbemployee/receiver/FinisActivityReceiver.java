package com.xbb.la.xbbemployee.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xbb.la.modellibrary.config.Constant;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:30
 * 描述：finish页面
 */

public class FinisActivityReceiver extends BroadcastReceiver{
    private Activity activity;
    public FinisActivityReceiver(Activity activity){
        this.activity=activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (Constant.IntentAction.TRANSACTION_TO_FINISH.equals(action)){
            activity.finish();
        }
    }
}
