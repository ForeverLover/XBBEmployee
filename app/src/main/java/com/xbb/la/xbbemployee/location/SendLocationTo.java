package com.xbb.la.xbbemployee.location;

import android.database.Cursor;
import android.util.Log;

import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.net.data.UserLocation;
import com.xbb.la.xbbemployee.config.TmpVariable;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendLocationTo implements Runnable {

    private boolean stopCommand = true;
    private String sLongitude = "0.0";
    private String sLatitude = "0.0";
    private String orderId;
    private String uid;
    String TAG = this.getClass().getName();

    @Override
    public void run() {
        // TODO Auto-generated method stub
        stopCommand = false;
        Log.i(TAG, "启动发送地址线程");
        while (!stopCommand) {
            Log.v(TAG, "stopcommand:" + stopCommand);
            sendLocation();
            try {
                Thread.sleep(Constant.relocationtime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public SendLocationTo(String orderId, String uid) {
        this.orderId = orderId;
        this.uid = uid;
        Log.v("Tag","uid:"+uid+" orderId:"+orderId);
    }

    public void sendLocation() {
        // 读取数据
        Cursor cursor = DBHelperMethod.getInstance().query(Constant.DB.TABLE_POSITION,
                null, Constant.DB.PositionColumns.POSITION_ORDERID + " = ? and " + Constant.DB.PositionColumns.POSITION_UID + " = ?", new String[]{orderId, uid}, null, null, null, null);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        try {
//			if (cursor.getCount() == 0) {
//				if (TmpVariable.MSG_SOCKET_CLASS != null) {
//					TmpVariable.SendLocationTime = time;
//					TmpVariable.SendLocationJD = "0.0";
//					TmpVariable.SendLocationWD = "0.0";
//					TmpVariable.MSG_SOCKET_CLASS.sendMsg(
//							Constant.USER_LOCATION_FAILD,
//							new UserLocation(TmpVariable.localName, 0.0, 0.0));
//				}
//				return;
//			}
            if (cursor != null) {
                Log.v("Tag", "position-count:" + cursor.getCount()+" orderId:"+orderId+" uid:"+uid);
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(Constant.DB.PositionColumns._ID));
                    sLongitude = cursor.getString(cursor
                            .getColumnIndex(Constant.DB.PositionColumns.POSITION_LNG));
                    sLatitude = cursor.getString(cursor.getColumnIndex(Constant.DB.PositionColumns.POSITION_LAT));
                    orderId = cursor.getString(cursor.getColumnIndex(Constant.DB.PositionColumns.POSITION_ORDERID));
                    uid = cursor.getString(cursor.getColumnIndex(Constant.DB.PositionColumns.POSITION_UID));
                    UserLocation sUserLocationnew = new UserLocation(
                            Float.valueOf(sLatitude),
                            Float.valueOf(sLongitude), orderId, uid);
                    if (TmpVariable.MSG_SOCKET_CLASS != null) {
                        TmpVariable.SendLocationTime = time;
                        TmpVariable.SendLocationJD = sLongitude;
                        TmpVariable.SendLocationWD = sLatitude;
                        TmpVariable.MSG_SOCKET_CLASS.sendMsg(
                                Constant.USER_LOCATION, sUserLocationnew);
                        Log.v("Tag", "sendLocation:(" + sLatitude + "," + sLongitude + ")");
                        // 删除已上传的位置
                        DBHelperMethod.getInstance().updateSql(
                                "delete from " + Constant.DB.TABLE_POSITION + " where "+Constant.DB.PositionColumns._ID+"= " + id);
                    } else {

                    }
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }

    public void stop() {
        stopCommand = true;
    }

}
