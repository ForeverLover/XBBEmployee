package com.xbb.la.modellibrary.bean;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class PushServiceBean {
    /**
     *系统返回的应用标识
     */
    private String appId;
    /**
     * 系统返回的设备连接的通道标识
     */
    private String channelId;
    /**
     * 系统返回的绑定Baidu Channel的用户标识
     */
    private String userId;

    /**
     * 向服务端发起的请求id。在追查问题时有用；
     */
    private String requestId;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
