package com.xbb.la.xbbemployee.config;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.xbb.la.modellibrary.bean.LocationBean;
import com.xbb.la.xbbemployee.listener.BaseUIListener;
import com.xbb.la.xbbemployee.listener.LocateProcessListener;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:48
 * 描述：
 */

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    public LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;
    public Vibrator mVibrator;
    private LocationClientOption option;

    public List<Activity> activities;
    private Map<Class<? extends BaseUIListener>, Collection<? extends BaseUIListener>> uiListeners;
    private boolean closed;
    private final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        uiListeners = new HashMap<Class<? extends BaseUIListener>, Collection<? extends BaseUIListener>>();
        if (activities == null) {
            activities = new Vector<Activity>();
        }
        closed = false;
        // 异常处理，不需要处理时注释掉这两句即可！
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        sInstance = this;
        initLocate();
        Log.v("Tag", "" + System.currentTimeMillis());
        startLocate();
        Log.v("Tag", "" + System.currentTimeMillis());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopLocate();
    }


    private void initLocate() {
        mLocationClient = new LocationClient(this.getApplicationContext());

        mMyLocationListener = new MyLocationListener();
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setPriority(LocationClientOption.GpsFirst); // 设置网络优先
        option.setScanSpan(300 * 1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void startLocate() {
        Log.v("Tag", "in" + System.currentTimeMillis());
        Log.v("Tag", "in " +mLocationClient.isStarted() );

        if (mLocationClient != null /*&& !mLocationClient.isStarted()*/) {
            mLocationClient.registerLocationListener(mMyLocationListener);
            mLocationClient.requestLocation();
            mLocationClient.start();
            onLocatestart();
            Log.v("Tag", "in-n" + System.currentTimeMillis());
        }
    }

    public void stopLocate() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
            mLocationClient.stop();
        }

    }

    /**
     * Retrieve application's context
     *
     * @return Android context
     */
    public static Context getContext() {
        return sInstance;
    }

    /**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            Log.i("Tag", sb.toString());
            // mLocationClient.setEnableGpsRealTimeTransfer(true);*/
            if (location != null) {
                Log.v("Tag", "current-location:" + location.getCity() + " " + location.getLongitude() + "," + location.getLatitude());
                LocationBean locationBean = new LocationBean();
                locationBean.setCity(location.getCity());
                locationBean.setLat("" + location.getLatitude());
                locationBean.setLon("" + location.getLongitude());
                locationBean.setAddr(location.getAddrStr());
                SharePreferenceUtil.getInstance().setLocationInfo(getApplicationContext(),
                        locationBean);
                onLocateSucceed(locationBean);
                stopLocate();
            } else {
                onLocateFailed();
            }
        }


    }


    public synchronized void register(Activity activity) {
        activities.add(activity);
    }

    public synchronized void unregister(Activity activity) {
        if (activities.size() != 0) {
            activities.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        } else {
        }
    }

    public synchronized void removeAll() {
        if (activities.size() != 0) {
            Activity activity = null;
            Iterator<Activity> iterator = activities.iterator();

            while (iterator.hasNext()) {
                activity = iterator.next();
                if (!activity.isFinishing()) {
                    activity.finish();
                    iterator.remove();
                }
            }
        } else {
        }
    }

    /**
     * 完全退出系统
     *
     * @param context
     */
    public synchronized void exitSystem(Context context) {

        removeAll();
        System.gc();

        Intent intent = new Intent(Intent.ACTION_MAIN);

        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        closed = true;
        System.exit(0);
    }


    /**
     * @param cls Requested class of listeners.
     * @return List of registered UI listeners.
     */
    public <T extends BaseUIListener> Collection<T> getUIListeners(Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseUIListener> Collection<T> getOrCreateUIListeners(
            Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    /**
     * Register new listener.
     * <p/>
     * Should be called from {@link Activity#onResume()}.
     *
     * @param cls
     * @param listener
     */
    public <T extends BaseUIListener> void addUIListener(Class<T> cls,
                                                         T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }

    /**
     * Unregister listener.
     * <p/>
     * Should be called from {@link Activity#onPause()}.
     *
     * @param cls
     * @param listener
     */
    public <T extends BaseUIListener> void removeUIListener(Class<T> cls,
                                                            T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    /**
     * Submits request to be executed in UI thread.
     *
     * @param runnable
     */
    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    private void onLocatestart() {
        Log.v("Tag","notice start");

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (LocateProcessListener listener : getUIListeners(LocateProcessListener.class)) {
                    listener.onLocateStart();
                }
            }
        });

    }

    /**
     * 定位失败
     */
    private void onLocateFailed() {
        Log.v("Tag","notice failed");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (LocateProcessListener listener : getUIListeners(LocateProcessListener.class)) {
                    listener.onLocateFailed();
                }
            }
        });
    }

    /**
     * 定位完成
     *
     * @param locationBean
     */
    private void onLocateSucceed(final LocationBean locationBean) {
        Log.v("Tag","notice succeed");
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (LocateProcessListener listener : getUIListeners(LocateProcessListener.class)) {
                    listener.onSucceed(locationBean);
                }
            }
        });
    }
}
