package com.xbb.la.xbbemployee.config;

import com.xbb.la.modellibrary.net.data.UserLocation;
import com.xbb.la.xbbemployee.utils.SocketClass;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/7 11:16
 * 描述：
 */

public class TmpVariable {

    public static SocketClass MSG_SOCKET_CLASS = null;
    public static String SendLocationTime = "";//最后上传位置的时间
    public static String SendLocationJD;//最后上传位置的经度
    public static String SendLocationWD;//最后上传位置的纬度
    public static String localName = "";
    public static String LocationAddress="";

    public static boolean isRECONNECT = false;//是否在重连

    public static UserLocation localLocation = new UserLocation(localName, 0, 0);
}
