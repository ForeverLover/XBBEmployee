package com.xbb.la.modellibrary.net;

import com.xbb.la.modellibrary.bean.UploadTransaction;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 11:31
 * Describe：网络请求接口
 */
public interface IApiRequest {
    /**
     * 登录
     * @param user 用户名
     * @param pwd  密码
     */
    void login(String user,String pwd);

    /**
     * 获取diy产品
     */
    void getDIYProducts();

    /**
     * 获取温馨提示
     */
    void getKindReminders();

    /**
     * 获取订单列表
     */
    void getOrderList(String userId,int type,int pageIndex,int pageSize);

    /**
     * 获取订单详情
     * @param orderId
     */
    void getOrderInfo(String orderId);

    /**
     * 确认接单
     * @param orderId
     * @param uid
     */
    void confirm(String orderId,String uid);

    /**
     * 员工出发
     * @param orderId
     * @param uid
     */
    void leave(String orderId,String uid);

    /**
     * 员工到达目的地
     * @param orderId
     * @param uid
     */
    void arrive(String orderId,String uid);


    /**
     * 提交任务
     * @param orderId
     * @param uid
     * @param uploadTransaction
     */
    void commitMission(String orderId,String uid,UploadTransaction uploadTransaction);

    /**
     * 上传经纬度
     * @param userId
     * @param orderId
     * @param lat
     * @param lng
     */
    void uploadLocation(String userId,String orderId,String lat,String lng);
}
