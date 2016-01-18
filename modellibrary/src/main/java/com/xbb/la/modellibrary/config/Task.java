package com.xbb.la.modellibrary.config;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 12:21
 * Describe：任务id
 */
public interface Task {
    /**
     * 登录任务id
     */
    public static int LOGIN = 0x1;

    public static int DIY_PRODUCT = 0x2;

    public static int REMINDER = 0x3;

    public static int ORDER_LIST = 0x4;

    public static int ORDER = 0x5;

    public static int ACCEPT = 0x6;

    public static int SET_OUT = 0x7;

    public static int ARRIVE = 0x8;

    public static int MISSION_COMPLETE = 0x9;

    public static int LOCATION_UPLOAD = 0x10;

    public static int ALTER_PWD = 0x11;
    /**
     *
     */
    public static int ALTER_NAME = 0x12;
    /**
     * 反馈
     */
    public static int FEEDBACK = 0x13;
    /**
     * 获取消息列表
     */
    public static int GET_MESSAGE = 0x14;
    /**
     * 修改头像
     */
    public static int AVATAR_CHANGED = 0x15;
    /**
     * 修改性别
     */
    public static int GENDER_CHANGED = 0x16;
    /**
     * 修改年龄
     */
    public static int AGE_CHANGED = 0x17;
    /**
     * 上传推送的消息通道信息
     */
    public static int INSERT_PUSH = 0x18;
    /**
     * 消息详情
     */
    public static int GET_MESSAGE_DETAIL = 0x19;
    /**
     * 未读消息
     */
    public static int UNREAD_MESSAGE = 0x20;


}
