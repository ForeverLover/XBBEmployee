package com.xbb.la.xbbemployee.receiver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.PushServiceBean;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.XRequestCallBack;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.ui.activity.MessageDetailsActivity;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

/*
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 *onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 *onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 返回值中的errorCode，解释如下：
 *0 - Success
 *10001 - Network Problem
 *10101  Integrate Check Error
 *30600 - Internal Server Error
 *30601 - Method Not Allowed
 *30602 - Request Params Not Valid
 *30603 - Authentication Failed
 *30604 - Quota Use Up Payment Required
 *30605 -Data Required Not Found
 *30606 - Request Time Expires Timeout
 *30607 - Channel Token Timeout
 *30608 - Bind Relation Not Found
 *30609 - Bind Number Too Many
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 *
 */

public class MyPushMessageReceiver extends PushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = MyPushMessageReceiver.class
            .getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(final Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 绑定成功
            Log.d(TAG, "绑定成功");
            PushServiceBean pushServiceBean = new PushServiceBean();
            pushServiceBean.setAppId(appid);
            pushServiceBean.setUserId(userId);
            pushServiceBean.setChannelId(channelId);
            pushServiceBean.setRequestId(requestId);
            SharePreferenceUtil.getInstance().savePushService(context, pushServiceBean);
            final Employee employee = SharePreferenceUtil.getInstance().getUserInfo(context);
            String uid = SharePreferenceUtil.getInstance().getUserId(context);
            if (employee.isLogin() && !employee.hasChannel()) {
                ApiRequest apiRequest = new ApiRequest(new XRequestCallBack() {
                    @Override
                    public void onPrepare(int taskId) {
                        MLog.v(TAG, "prepare channelid upload succeed");
                    }

                    @Override
                    public void onSuccess(int taskId, Object... params) {
                        employee.setChannel(true);
                        SharePreferenceUtil.getInstance().saveUserInfo(context, employee);
                    }

                    @Override
                    public void onSuccess(int taskId, String flag, Object... params) {

                    }

                    @Override
                    public void onEnd(int taskId) {

                    }

                    @Override
                    public void onFailed(int taskId, int errorCode, String errorMsg) {

                    }

                    @Override
                    public void onFailed(int taskId, String errorMsg) {

                    }

                    @Override
                    public void onLoading(int taskId, long count, long current) {

                    }

                    @Override
                    public boolean isCallBack() {
                        return true;
                    }
                });
                apiRequest.insertPushChannel(uid, userId, channelId, "1");
            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, responseString);
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message
                + "\" customContentString=" + customContentString;
        Log.d(TAG, messageString);

        // 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
        if (!StringUtil.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String myvalue = null;
                if (!customJson.isNull("mykey")) {
                    myvalue = customJson.getString("mykey");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, messageString);
    }

    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
        String notifyString = "通知点击 title=\"" + title + "\" description=\""
                + description + "\" customContent=" + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!StringUtil.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String msgId = null;
                if (customJson != null)
                    if (customJson.has("msgid") && !customJson.isNull("msgid") && !StringUtil.isEmpty(customJson.getString("msgid"))) {
                        msgId = customJson.getString("msgid");
                        Intent intent = new Intent(context.getApplicationContext(), MessageDetailsActivity.class);
                        intent.putExtra(Constant.IntentVariable.MESSAGE_ID, msgId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                MLog.v(TAG, "error:" + e.getMessage());
            }
        }

    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        Log.d(TAG, notifyString);

        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
        if (!StringUtil.isEmpty(customContentString)) {
            JSONObject customJson = null;
            try {
                customJson = new JSONObject(customContentString);
                String msgId = null;
                if (customJson != null)
                    if (customJson.has("msgid") && !customJson.isNull("msgid") && !StringUtil.isEmpty(customJson.getString("msgid"))) {
                        msgId = customJson.getString("msgid");
                        if (!StringUtil.isEmpty(msgId)) {
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                            localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.NEW_MESSAGE));
                        }
                    }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        // 你可以參考 onNotificationClicked中的提示从自定义内容获取具体值
        updateContent(context, notifyString);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param failTags  设置失败的tag
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * delTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param failTags  删除失败的tag
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        Log.d(TAG, responseString);

        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        Log.d(TAG, responseString);

        if (errorCode == 0) {
            // 解绑定成功
            Log.d(TAG, "解绑成功");
        }
        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
        updateContent(context, responseString);
    }

    private void updateContent(Context context, String content) {
//        Log.d(TAG, "updateContent");
//        String logText = "" + PushUtil.logStringCache;
//
//        if (!logText.equals("")) {
//            logText += "\n";
//        }
//
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
//        logText += sDateFormat.format(new Date()) + ": ";
//        logText += content;
//
//        PushUtil.logStringCache = logText;
//
//        Intent intent = new Intent();
//        intent.setClass(context.getApplicationContext(), PushDemoActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.getApplicationContext().startActivity(intent);
    }

}
