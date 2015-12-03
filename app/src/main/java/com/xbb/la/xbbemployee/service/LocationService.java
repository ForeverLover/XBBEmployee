package com.xbb.la.xbbemployee.service;

        import android.app.Service;
        import android.content.Intent;
        import android.os.Binder;
        import android.os.IBinder;
        import android.util.Log;

        import com.xbb.la.modellibrary.config.Constant;
        import com.xbb.la.modellibrary.config.NetConfig;
        import com.xbb.la.xbbemployee.config.TmpVariable;
        import com.xbb.la.xbbemployee.location.LocationTools;
        import com.xbb.la.xbbemployee.location.SendLocationTo;
        import com.xbb.la.xbbemployee.utils.SocketClass;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/5 15:40
 * 描述：上传位置服务
 */

public class LocationService extends Service {
    private String TAG = "OnlineServier";
    private final IBinder binder = new LocationBinder();
    private SocketClass socketClass = null;
    private SendLocationTo sendLocationTo;
    private boolean serviceStop = true;

    private String orderId;
    private String uid;

    @Override
    public void onCreate() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null) {
        orderId = intent.getStringExtra(Constant.IntentVariable.ORDER_ID);
        uid = intent.getStringExtra("uid");
        Log.v("Tag", "service_command:" + orderId + " " + uid);
//            SharePreferenceUtil.getInstance().setLocationUploadOrder(this, orderId, uid);
//        } else {
//            LocationUploadOrder locationUploadOrder = SharePreferenceUtil.getInstance().getLocationUploadOrder(this);
//            if (locationUploadOrder != null) {
//                orderId = locationUploadOrder.getOrderId();
//                uid = locationUploadOrder.getUserId();
//            }
//        }
//        if (!StringUtil.isEmpty(orderId) && !StringUtil.isEmpty(uid)) {
        if (socketClass == null) {
            socketClass = new SocketClass(NetConfig.ONLINE_IP,
                    NetConfig.ONLINE_DOWN_PORT, getApplicationContext());
        }
        TmpVariable.MSG_SOCKET_CLASS = socketClass;
        LocationTools lt = LocationTools.getInstance(getApplicationContext());
        lt.setOrderId(orderId);
        lt.startLocation();
        sendLocationTo = new SendLocationTo(orderId, uid);
        new Thread(sendLocationTo).start();
        super.onStartCommand(intent, flags, startId);
//        }
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "k_test OnlineService onDestroy");

        if (TmpVariable.MSG_SOCKET_CLASS != null) {
            TmpVariable.MSG_SOCKET_CLASS = null;
            socketClass = null;
        }
        serviceStop = true;
        if (sendLocationTo != null)
            sendLocationTo.stop();
       /* unregisterReceiver(receiver);*/

    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

}
