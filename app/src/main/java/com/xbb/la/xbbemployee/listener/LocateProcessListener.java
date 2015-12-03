package com.xbb.la.xbbemployee.listener;


import com.xbb.la.modellibrary.bean.LocationBean;

/**
 * Description: 定位流程监听
 * User: Templar
 * Date: 2015-09-17
 * Time: 11:12
 * FIXME
 */
public abstract interface LocateProcessListener extends BaseUIListener {
    abstract void onLocateStart();

    abstract void onLocateFailed();

    abstract void onSucceed(LocationBean locationBean);

}
