package com.xbb.la.xbbemployee.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xbb.la.modellibrary.config.Constant;

public class SItude {
//	public static double latitude;//精度
//	public static double longitude;//纬度
//	public static HashMap<String, String> requesthm;//接收方存储的请求列表
//	public static HashMap<String, String> connecthm;//发送方存储的接受列表
//	public static HashMap<String, String> myuuid;//用户消息间控制的请求id。必须一一对应
//	public static HashMap<String, Double[]>  ago_site;//之前的位置点
//    public static MyLBSActivity instance;//为避免用户在定位页面有新定位出现，在用户同意时直接关闭旧页面，重新进入
//    public static String myaddressname;//本地返回的地址
//    public static String  frendsaddr;
    
    
    /**
     * 
     * @param context
     * @param frendsaddr 
     * @param latitude 精度
     * @param longitude 纬度
     * @return将经纬度存储在sharepreference
     */
	public static Boolean writeSite(Context context,double latitude,double longitude,String myaddressname, String frendsaddr){
		SharedPreferences prefs = context.getSharedPreferences(Constant.SP.PREFERENCES,Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString("latitude", ""+latitude);
		ed.putString("longitude", ""+longitude);
		if(myaddressname!=null&&!"".equals(myaddressname.trim())){
			ed.putString("myaddressname", myaddressname);
		}
		if(frendsaddr==null||"".equals(frendsaddr.trim())){
			ed.putString("frendsaddr", "暂无");
		}
		ed.commit();
		return null;	
	}
	/**
	 * 返回经纬度的值，如果没有则返回的是0,0
	 * @param context
	 * @return
	 */
	public static double[] readSite(Context context,double[] result){
		SharedPreferences prefs = context.getSharedPreferences(Constant.SP.PREFERENCES,Context.MODE_PRIVATE);
		if(prefs.contains("latitude")&&prefs.contains("longitude")){
			String latitude =prefs.getString("latitude", "");
			String longitude = prefs.getString("longitude", "");
			if(latitude!=null&&!"".equals(latitude.trim())&&longitude!=null&&!"".equals(longitude.trim())){
				result[0]=Double.parseDouble(latitude);
				result[1]=Double.parseDouble(longitude);;
			}	
		}
		return result;
	}

}
