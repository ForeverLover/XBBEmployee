package com.xbb.la.modellibrary.bean;

import java.io.Serializable;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:13
 * 描述：订单信息
 */

public class OrderInfo implements Serializable {
    private String orderId;
    private String orderNo;
    private String orderName;
    private String orderGenerateTime;
    private String orderContent;
    private String orderSum;
    private String payType;
    private String orderState;
    private String payTime;
    private String payState;
    /**
     * 订单处理时间
     *  派单时间 接单时间 完成时间
     */
    private String dealTime;
    /**
     *服务时间：即时，预约时间段
     */
    private String serviceTime;
    /**
     * 派单状态
     */
    private String orderDeliveryState;
    private String orderFinishedTime;

    private String driverName;
    private String driverTel;
    /**
     * 车辆型号
     */
    private String carType;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 车辆信息
     */
    private String carInfo;
    private String carLocation;
    private String carCoordinates;
    private String carLat;
    private String carLon;

    /**
     * 地址录音
     */
    private String adressAudio;

    /**
     * 任务状态
     * 0 未接单
     * 1 未出发
     * 2 未到达
     * 3 未完成
     * 4 已完成
     */
    private String missionState;

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getMissionState() {
        return missionState;
    }

    public void setMissionState(String missionState) {
        this.missionState = missionState;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }

    public String getCarLat() {
        return carLat;
    }

    public void setCarLat(String carLat) {
        this.carLat = carLat;
    }

    public String getCarLon() {
        return carLon;
    }

    public void setCarLon(String carLon) {
        this.carLon = carLon;
    }

    public String getCarCoordinates() {
        return carCoordinates;
    }

    public void setCarCoordinates(String carCoordinates) {
        this.carCoordinates = carCoordinates;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderGenerateTime() {
        return orderGenerateTime;
    }

    public void setOrderGenerateTime(String orderGenerateTime) {
        this.orderGenerateTime = orderGenerateTime;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(String orderSum) {
        this.orderSum = orderSum;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderDeliveryState() {
        return orderDeliveryState;
    }

    public void setOrderDeliveryState(String orderDeliveryState) {
        this.orderDeliveryState = orderDeliveryState;
    }

    public String getOrderFinishedTime() {
        return orderFinishedTime;
    }

    public void setOrderFinishedTime(String orderFinishedTime) {
        this.orderFinishedTime = orderFinishedTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getCarLocation() {
        return carLocation;
    }

    public void setCarLocation(String carLocation) {
        this.carLocation = carLocation;
    }

    public String getAdressAudio() {
        return adressAudio;
    }

    public void setAdressAudio(String adressAudio) {
        this.adressAudio = adressAudio;
    }
}
