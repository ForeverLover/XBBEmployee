package com.xbb.la.xbbemployee.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.LocationBean;
import com.xbb.la.modellibrary.bean.LocationUploadOrder;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;

import java.util.Map;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/7 11:13
 * 描述：
 */

public class SharePreferenceUtil {

    private static SharePreferenceUtil instance = null;

    public static SharePreferenceUtil getInstance() {
        if (instance == null)
            instance = new SharePreferenceUtil();
        return instance;
    }

    public static String getsharepre(Context context) {
        SharedPreferences sp = context.getSharedPreferences("user",
                Context.MODE_PRIVATE);
        if (sp.contains("name")) {
            return sp.getString("name", "admin");
        } else {
            return "admin";
        }
    }

    public void saveUserInfo(Context context, Employee employee) {
        if (employee == null)
            return;
        SharedPreferences sp = context.getSharedPreferences(Constant.SP.UserInfo,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("u_id", employee.getUid());
        editor.putString("name", employee.getNickname());
        editor.putString("age", employee.getAge());
        editor.putString("gender", employee.getGender());
        editor.putString("empno", employee.getEmpNo());
        editor.putString("tel", employee.getTel());
        editor.putString("wx", employee.getWx());
        editor.putString("pwd", employee.getPwd());
        editor.putString("avatar", employee.getAvatar());
        editor.putBoolean("islogin", employee.isLogin());
        editor.commit();
    }

    public String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP.UserInfo,
                Context.MODE_PRIVATE);
        return sp.getString("u_id", "-1");
    }

    public Employee getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP.UserInfo,
                Context.MODE_PRIVATE);
        Employee employee = new Employee();
        employee.setUid(sp.getString("u_id", "-1"));
        employee.setAvatar(sp.getString("avatar", "-1"));
        employee.setNickname(sp.getString("name", ""));
        employee.setTel(sp.getString("tel", ""));
        employee.setAge(sp.getString("age", ""));
        employee.setGender(sp.getString("gender", ""));
        employee.setEmpNo(sp.getString("empno", ""));
        employee.setWx(sp.getString("wx", ""));
        employee.setPwd(sp.getString("pwd", ""));
        employee.setLogin(sp.getBoolean("islogin", false));
        return employee;

    }

    /**
     * 获取保存的位置信息
     *
     * @param context
     * @return
     */
    public LocationBean getLocationInfo(Context context) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        String lon = defaultSharedPreferences.getString("lon", null);
        String lat = defaultSharedPreferences.getString("lat", null);
        String city = defaultSharedPreferences.getString("city", null);
        String addr = defaultSharedPreferences.getString("addr", null);
        LocationBean locationBean = null;
        if (!StringUtil.isEmpty(lon) && !StringUtil.isEmpty(lat)) {
            locationBean = new LocationBean();
            locationBean.setLon(lon);
            locationBean.setLat(lat);
            locationBean.setCity(city);
            locationBean.setAddr(addr);
        }
        return locationBean;
    }

    /**
     * 保存当前位置信息
     *
     * @param context
     * @param locationBean
     */
    public void setLocationInfo(Context context,
                                LocationBean locationBean) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        if (null == locationBean) {
        } else {
            SharedPreferences.Editor editor = defaultSharedPreferences.edit();
            if (StringUtil.isEmpty(locationBean.getLon()) || StringUtil.isEmpty(locationBean.getLat()))
                return;
            editor.putString("addr", locationBean.getAddr());
            editor.putString("city", locationBean.getCity());
            editor.putString("lon", locationBean.getLon()).putString("lat",
                    locationBean.getLat());
            editor.commit();
        }
    }

    /**
     * 清除缓存的地址信息
     *
     * @param context
     */
    public static void clearLocationInfo(Context context) {
        SharedPreferences defaultSharedPreferences = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        defaultSharedPreferences.edit().clear().commit();
    }

    /***
     * 版本更新状态
     ***/
    public static void saveState(Context con, boolean state) {
        SharedPreferences sp = con.getSharedPreferences("UpdateState",
                Context.MODE_PRIVATE);
        sp.edit().putBoolean("state", state).commit();
    }

    public static boolean getStateIsUpdating(Context con) {
        SharedPreferences sp = con.getSharedPreferences("UpdateState",
                Context.MODE_PRIVATE);
        return sp.getBoolean("state", false);
    }

    public static void clear(Context con) {
        SharedPreferences sp = con.getSharedPreferences("UpdateState",
                Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public static long getVersionTime(Context con) {
        SharedPreferences sp = con.getSharedPreferences("VersionTime",
                Context.MODE_PRIVATE);
        return sp.getLong("time", 0);
    }

    public static void setVersionTime(Context con, long time) {
        SharedPreferences sp = con.getSharedPreferences("VersionTime",
                Context.MODE_PRIVATE);
        sp.edit().putLong("time", time).commit();
    }

    public void setLocationUploadOrder(Context con,String orderId, String uid) {
        SharedPreferences sp = con.getSharedPreferences("VersionTime",
                Context.MODE_PRIVATE);
        sp.edit().putString("orderId", orderId).putString("uid", uid).commit();
    }
    public LocationUploadOrder getLocationUploadOrder(Context con) {
        SharedPreferences sp = con.getSharedPreferences("VersionTime",
                Context.MODE_PRIVATE);
        String orderId=sp.getString("orderId", "");
        String uid=sp.getString("uid","");
        if (!StringUtil.isEmpty(orderId)&&!StringUtil.isEmpty(uid)){
            LocationUploadOrder locationUploadOrder=new LocationUploadOrder();
            locationUploadOrder.setOrderId(orderId);
            locationUploadOrder.setOrderId(uid);
            return  locationUploadOrder;
        }
        return null;
    }

}
