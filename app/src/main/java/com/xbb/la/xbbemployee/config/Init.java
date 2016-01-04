package com.xbb.la.xbbemployee.config;

public interface Init {
    /***
     * 初始化数据
     */
    void initData();

    /**
     * 初始化UI
     */
    void initView();

    /**
     * 设置监听
     */
    void initListener();
}
