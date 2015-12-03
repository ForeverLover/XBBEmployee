package com.xbb.la.xbbemployee.location;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.net.data.UserLocation;
import com.xbb.la.xbbemployee.config.TmpVariable;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.utils.MyFileRecord;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

public class LocationTools {

	private static String TAG = LocationTools.class.getCanonicalName();

	private static LocationClient mLocationClient = null;
	private BDLocationListener bdLocationListener;

	private static SItude station = new SItude();
	private static Bdlister mybdlister;
	static Context context;
	private static LocationTools instance;
	private GeoCoder mSearch = null;// 搜索模块，用于单击事件坐标转换为地址信息;
	private int eventId;
	private int type;
	private boolean isFirst=true;
	private  String orderId;
	private String uid;
	
	public LocationTools(Context context) {
		super();
		this.context = context;
		this.uid= SharePreferenceUtil.getInstance().getUserId(context);
		bdLocationListener=new MyBDListener();
	}

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}


	public void startLocation() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(context);
//			mLocationClient.setAK(MyAplication.strKey);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true); // 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
			option.setPriority(LocationClientOption.GpsFirst); // 设置网络优先
//			option.setPoiNumber(1);
			option.setAddrType("all");
			option.setScanSpan((int) Constant.relocationtime);
//			option.disableCache(true);// 禁止启用缓存定位
//			option.setPoiExtraInfo(true);
			mLocationClient.setLocOption(option);
			mLocationClient.registerLocationListener(bdLocationListener);
			mLocationClient.start();// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
		}
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.start();
		} else {
			Log.d("LocSDK3", "mLocationClient is null or not started");
		}
	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener(String orderId) {
		if(mSearch!=null){
			mSearch.destroy();
			mSearch=null;
		}
		if (mLocationClient != null && mLocationClient.isStarted()) {
			Log.v(TAG,"remove locationListener");
			mLocationClient.unRegisterLocationListener(bdLocationListener);
			mLocationClient.stop();
			mLocationClient = null;
		}
		DBHelperMethod.getInstance().deletePosition(orderId,uid);
	}

	/**
	 * 更新位置并保存到SItude中
	 */
	public void updateListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}

	/**
	 * 获取经纬度信息
	 * 
	 * @return
	 */
	// public SItude getLocation() {
	// return station;
	// }
	public interface Bdlister {
		void Mysite(BDLocation location);
	}

	public void setBDlistener(Bdlister bdlister) {
		mybdlister = bdlister;
	}

	private  class MyBDListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.i("Tag",
					"================getLongitude"+location.getLongitude());
			if (location == null || location.getLongitude() == 4.9e-324) {
				//记录定位失败到文件中
				MyFileRecord.getInstance().saveRecordInfoFile(1,"");
			} else {
				if (mybdlister != null)
					mybdlister.Mysite(location);
				String address = location.getAddrStr();
				String myaddress = "";
				if (address == null || "".equals(address.trim())) {
					myaddress = "网络异常，获取本地地址失败";
				} else {
					TmpVariable.LocationAddress = address;
				}
				Log.i("Tag", "获取到定位=" + myaddress);
				TmpVariable.localLocation = new UserLocation(TmpVariable.localName, location
						.getLatitude(), location.getLongitude());
				//通知事件派警地图更新坐标位置
				Intent in = new Intent(Constant.IntentAction.LOCATION_DATA);
				context.sendBroadcast(in);
				//把数据写入到数据库
				Log.v("Tag","insertPos:"+orderId+"　fd"+uid);
				DBHelperMethod.getInstance().insertPosition(location
						.getLatitude(), location.getLongitude(),orderId, uid);
				
				SItude.writeSite(context, location.getLatitude(),
						location.getLongitude(), myaddress, "测试");
				
				//第一次定位成功,即时发送定位坐标到服务器
				if(isFirst&& TmpVariable.MSG_SOCKET_CLASS != null){
					isFirst=false;
					UserLocation sUserLocationnew = new UserLocation(location
							.getLatitude(), location.getLongitude(),orderId,uid);
					TmpVariable.MSG_SOCKET_CLASS.sendMsg(
							Constant.USER_LOCATION, sUserLocationnew);
				}

				
			}
		}

	}

	public static LocationTools getInstance(Context context) {
		if (instance == null) { // line 12
			instance = new LocationTools(context); // line 13
		}

		return instance;
	}

}
