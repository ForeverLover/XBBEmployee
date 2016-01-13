package com.xbb.la.modellibrary.utils;

import android.util.Log;

import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.bean.Reminder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 13:01
 * 描述：
 */

public class ParseUtil {
    private static ParseUtil instance = null;

    public static ParseUtil getInstance() {
        if (instance == null)
            instance = new ParseUtil();
        return instance;
    }

    /**
     * 解析diy产品集合
     *
     * @param json
     * @return
     */
    public List<DIYProduct> parseDiyProducts(String json) {
        JSONArray jsonArray = null;
        List<DIYProduct> diyProductList = null;
        try {
            jsonArray = new JSONArray(json);
            if (jsonArray != null && jsonArray.length() > 0) {
                diyProductList = new ArrayList<DIYProduct>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        DIYProduct diyProduct = new DIYProduct();
                        if (jsonObject.has("id") && !jsonObject.isNull("id") && !StringUtil.isEmpty(jsonObject.getString("id"))) {
                            diyProduct.setId(jsonObject.getString("id"));
                        }
                        if (jsonObject.has("p_name") && !jsonObject.isNull("p_name") && !StringUtil.isEmpty(jsonObject.getString("p_name"))) {
                            diyProduct.setP_name(jsonObject.getString("p_name"));
                        }
                        if (jsonObject.has("p_wimg") && !jsonObject.isNull("p_wimg") && !StringUtil.isEmpty(jsonObject.getString("p_wimg"))) {
                            diyProduct.setP_wimg(jsonObject.getString("p_wimg"));
                        }
                        if (jsonObject.has("p_ximg") && !jsonObject.isNull("p_ximg") && !StringUtil.isEmpty(jsonObject.getString("p_ximg"))) {
                            diyProduct.setP_ximg(jsonObject.getString("p_ximg"));
                        }
                        diyProductList.add(diyProduct);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return diyProductList;

    }

    /**
     * 解析温馨提示
     *
     * @param json
     * @return
     */
    public List<Reminder> parseKindReminders(String json) {
        JSONArray jsonArray = null;
        List<Reminder> reminderList = null;
        try {
            jsonArray = new JSONArray(json);
            if (jsonArray != null && jsonArray.length() > 0) {
                reminderList = new ArrayList<Reminder>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        Reminder reminder = new Reminder();
                        if (jsonObject.has("id") && !jsonObject.isNull("id") && !StringUtil.isEmpty(jsonObject.getString("id"))) {
                            reminder.setId(jsonObject.getString("id"));
                        }
                        if (jsonObject.has("content") && !jsonObject.isNull("content") && !StringUtil.isEmpty(jsonObject.getString("content"))) {
                            reminder.setReminderText(jsonObject.getString("content"));
                        }
                        if (jsonObject.has("thumb") && !jsonObject.isNull("thumb") && !StringUtil.isEmpty(jsonObject.getString("thumb"))) {
                            reminder.setThumb(jsonObject.getString("thumb"));
                        }
                        if (jsonObject.has("title") && !jsonObject.isNull("title") && !StringUtil.isEmpty(jsonObject.getString("title"))) {
                            reminder.setTitle(jsonObject.getString("title"));
                        }
                        reminderList.add(reminder);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reminderList;

    }


    /**
     * 解析订单列表
     *
     * @param json
     * @return
     */
    public List<OrderInfo> parseOrderList(String json) {
        List<OrderInfo> orderInfoList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            Log.e("Tag", "begin parse");
            if (jsonArray != null && json.length() > 0) {
                Log.e("Tag", jsonArray.length() + "");
                orderInfoList = new ArrayList<OrderInfo>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        OrderInfo orderInfo = new OrderInfo();
                        if (jsonObject.has("orderId") && !jsonObject.isNull("orderId") && !StringUtil.isEmpty(jsonObject.getString("orderId")))
                            orderInfo.setOrderId(jsonObject.getString("orderId"));
                        if (jsonObject.has("orderNum") && !jsonObject.isNull("orderNum") && !StringUtil.isEmpty(jsonObject.getString("orderNum")))
                            orderInfo.setOrderNo(jsonObject.getString("orderNum"));
                        if (jsonObject.has("orderTime") && !jsonObject.isNull("orderTime") && !StringUtil.isEmpty(jsonObject.getString("orderTime")))
                            orderInfo.setDealTime(jsonObject.getString("orderTime"));
                        if (jsonObject.has("orderName") && !jsonObject.isNull("orderName") && !StringUtil.isEmpty(jsonObject.getString("orderName")))
                            orderInfo.setOrderName(jsonObject.getString("orderName"));
                        if (jsonObject.has("orderType") && !jsonObject.isNull("orderType") && !StringUtil.isEmpty(jsonObject.getString("orderType")))
                            orderInfo.setServiceTime(jsonObject.getString("orderType"));
                        if (jsonObject.has("licensePlate") && !jsonObject.isNull("licensePlate") && !StringUtil.isEmpty(jsonObject.getString("licensePlate")))
                            orderInfo.setPlateNo(jsonObject.getString("licensePlate"));
                        if (jsonObject.has("location_lg") && !jsonObject.isNull("location_lg") && !StringUtil.isEmpty(jsonObject.getString("location_lg")))
                            orderInfo.setCarLon(jsonObject.getString("location_lg"));
                        if (jsonObject.has("location_lt") && !jsonObject.isNull("location_lt") && !StringUtil.isEmpty(jsonObject.getString("location_lt")))
                            orderInfo.setCarLat(jsonObject.getString("location_lt"));
                        if (jsonObject.has("name") && !jsonObject.isNull("name") && !StringUtil.isEmpty(jsonObject.getString("name")))
                            orderInfo.setDriverName(jsonObject.getString("name"));
                        if (jsonObject.has("location") && !jsonObject.isNull("location") && !StringUtil.isEmpty(jsonObject.getString("location")))
                            orderInfo.setCarLocation(jsonObject.getString("location"));
                        if (jsonObject.has("carCate") && !jsonObject.isNull("carCate") && !StringUtil.isEmpty(jsonObject.getString("carCate")))
                            orderInfo.setCarType(jsonObject.getString("carCate"));
                        if (jsonObject.has("carInfo") && !jsonObject.isNull("carInfo") && !StringUtil.isEmpty(jsonObject.getString("carInfo")))
                            orderInfo.setCarInfo(jsonObject.getString("carInfo"));
                        if (jsonObject.has("worker_state") && !jsonObject.isNull("worker_state") && !StringUtil.isEmpty(jsonObject.getString("worker_state")))
                            orderInfo.setOrderState(jsonObject.getString("worker_state"));

                        orderInfoList.add(orderInfo);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Tag", e.getMessage());
        }
        return orderInfoList;
    }

    /**
     * 解析订单详情
     *
     * @param json
     * @return
     */
    public OrderInfo parseOrderInfo(String json) {
        if (StringUtil.isEmpty(json)) return null;
        OrderInfo orderInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject != null) {
                orderInfo = new OrderInfo();
                if (jsonObject.has("orderId") && !jsonObject.isNull("orderId") &&
                        !StringUtil.isEmpty(jsonObject.getString("orderId"))) {
                    orderInfo.setOrderId(jsonObject.getString("orderId"));
                }
                if (jsonObject.has("orderNum") && !jsonObject.isNull("orderNum") &&
                        !StringUtil.isEmpty(jsonObject.getString("orderNum"))) {
                    orderInfo.setOrderNo(jsonObject.getString("orderNum"));
                }
                if (jsonObject.has("orderName") && !jsonObject.isNull("orderName") &&
                        !StringUtil.isEmpty(jsonObject.getString("orderName"))) {
                    orderInfo.setOrderName(jsonObject.getString("orderName"));
                }
                if (jsonObject.has("total_price") && !jsonObject.isNull("total_price") &&
                        !StringUtil.isEmpty(jsonObject.getString("total_price"))) {
                    orderInfo.setOrderSum(jsonObject.getString("total_price"));
                }
                if (jsonObject.has("pay_type") && !jsonObject.isNull("pay_type") &&
                        !StringUtil.isEmpty(jsonObject.getString("pay_type"))) {
                    orderInfo.setPayType(jsonObject.getString("pay_type"));
                }
                if (jsonObject.has("pay_state") && !jsonObject.isNull("pay_state") &&
                        !StringUtil.isEmpty(jsonObject.getString("pay_state"))) {
                    orderInfo.setPayState(jsonObject.getString("pay_state"));
                }
                if (jsonObject.has("serverTime") && !jsonObject.isNull("serverTime") &&
                        !StringUtil.isEmpty(jsonObject.getString("serverTime"))) {
                    orderInfo.setServiceTime(jsonObject.getString("serverTime"));
                }
                if (jsonObject.has("name") && !jsonObject.isNull("name") &&
                        !StringUtil.isEmpty(jsonObject.getString("name"))) {
                    orderInfo.setDriverName(jsonObject.getString("name"));
                }
                if (jsonObject.has("telephone") && !jsonObject.isNull("telephone") &&
                        !StringUtil.isEmpty(jsonObject.getString("telephone"))) {
                    orderInfo.setDriverTel(jsonObject.getString("telephone"));
                }
                if (jsonObject.has("carInfo") && !jsonObject.isNull("carInfo") &&
                        !StringUtil.isEmpty(jsonObject.getString("carInfo"))) {
                    orderInfo.setCarInfo(jsonObject.getString("carInfo"));
                }
                if (jsonObject.has("carCate") && !jsonObject.isNull("carCate") &&
                        !StringUtil.isEmpty(jsonObject.getString("carCate"))) {
                    orderInfo.setCarType(jsonObject.getString("carCate"));
                }
                if (jsonObject.has("licensePlate") && !jsonObject.isNull("licensePlate") &&
                        !StringUtil.isEmpty(jsonObject.getString("licensePlate"))) {
                    orderInfo.setPlateNo(jsonObject.getString("licensePlate"));
                }
                if (jsonObject.has("location") && !jsonObject.isNull("location") &&
                        !StringUtil.isEmpty(jsonObject.getString("location"))) {
                    orderInfo.setCarLocation(jsonObject.getString("location"));
                }
                if (jsonObject.has("audio") && !jsonObject.isNull("audio") &&
                        !StringUtil.isEmpty(jsonObject.getString("audio"))) {
                    orderInfo.setAdressAudio(jsonObject.getString("audio"));
                }
                if (jsonObject.has("orderTime") && !jsonObject.isNull("orderTime") &&
                        !StringUtil.isEmpty(jsonObject.getString("orderTime"))) {
                    orderInfo.setOrderGenerateTime(jsonObject.getString("orderTime"));
                }
                if (jsonObject.has("location_lg") && !jsonObject.isNull("location_lg") &&
                        !StringUtil.isEmpty(jsonObject.getString("location_lg"))) {
                    orderInfo.setCarLon(jsonObject.getString("location_lg"));
                }
                if (jsonObject.has("location_lt") && !jsonObject.isNull("location_lt") &&
                        !StringUtil.isEmpty(jsonObject.getString("location_lt"))) {
                    orderInfo.setCarLat(jsonObject.getString("location_lt"));
                }
                if (jsonObject.has("payTime") && !jsonObject.isNull("payTime") &&
                        !StringUtil.isEmpty(jsonObject.getString("payTime"))) {
                    orderInfo.setPayTime(jsonObject.getString("payTime"));
                }
                if (jsonObject.has("completeTime") && !jsonObject.isNull("completeTime") &&
                        !StringUtil.isEmpty(jsonObject.getString("completeTime"))) {
                    orderInfo.setOrderFinishedTime(jsonObject.getString("completeTime"));
                }
                if (jsonObject.has("worker_state") && !jsonObject.isNull("worker_state") && !StringUtil.isEmpty(jsonObject.getString("worker_state")))
                    orderInfo.setMissionState(jsonObject.getString("worker_state"));
                if (jsonObject.has("orderState") && !jsonObject.isNull("orderState") && !StringUtil.isEmpty(jsonObject.getString("orderState")))
                    orderInfo.setOrderState(jsonObject.getString("orderState"));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderInfo;
    }

    public Employee parseEmployeeInfo(String json){
        if (StringUtil.isEmpty(json)) return null;
        Employee employee=null;
        try {
            JSONObject jsonObject=new JSONObject(json);
            if (jsonObject!=null){
                employee=new Employee();
                if (jsonObject.has("id")&&!jsonObject.isNull("id")&&!StringUtil.isEmpty(jsonObject.getString("id"))){
                    employee.setUid(jsonObject.getString("id"));
                }
                if (jsonObject.has("identiy_id")&&!jsonObject.isNull("identiy_id")&&!StringUtil.isEmpty(jsonObject.getString("identiy_id"))){
                    employee.setEmpNo(jsonObject.getString("identiy_id"));
                }
                if (jsonObject.has("emp_name")&&!jsonObject.isNull("emp_name")&&!StringUtil.isEmpty(jsonObject.getString("emp_name"))){
                    employee.setNickname(jsonObject.getString("emp_name"));
                }
                if (jsonObject.has("emp_img")&&!jsonObject.isNull("emp_img")&&!StringUtil.isEmpty(jsonObject.getString("emp_img"))){
                    employee.setAvatar(jsonObject.getString("emp_img"));
                }
                if (jsonObject.has("age")&&!jsonObject.isNull("age")&&!StringUtil.isEmpty(jsonObject.getString("age"))){
                    employee.setAge(jsonObject.getString("age"));
                }
                if (jsonObject.has("sex")&&!jsonObject.isNull("sex")&&!StringUtil.isEmpty(jsonObject.getString("sex"))){
                    employee.setGender(jsonObject.getString("sex"));
                }
                if (jsonObject.has("emp_iphone")&&!jsonObject.isNull("emp_iphone")&&!StringUtil.isEmpty(jsonObject.getString("emp_iphone"))){
                    employee.setTel(jsonObject.getString("emp_iphone"));
                }
                if (jsonObject.has("emp_wx")&&!jsonObject.isNull("emp_wx")&&!StringUtil.isEmpty(jsonObject.getString("emp_wx"))){
                    employee.setWx(jsonObject.getString("emp_wx"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return employee;
    }

    public String parseAvatarPath(String json){
        if (StringUtil.isEmpty(json)) return null;
        String avatarPath="";
        try {
            JSONObject jsonObject=new JSONObject(json);
            if (jsonObject!=null){
                if (jsonObject.has("emp_img")&&!jsonObject.isNull("emp_img")&&!StringUtil.isEmpty(jsonObject.getString("emp_img"))){
                    avatarPath=jsonObject.getString("emp_img");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return avatarPath;
    }
}
