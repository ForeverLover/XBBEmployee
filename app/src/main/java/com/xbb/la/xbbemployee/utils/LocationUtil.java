package com.xbb.la.xbbemployee.utils;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.xbb.la.modellibrary.bean.LocationBean;
import com.xbb.la.modellibrary.utils.StringUtil;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/18 14:21
 * 描述：坐标
 */

public class LocationUtil {
    private static LocationUtil instance = null;

    public static LocationUtil getInstance() {
        if (instance == null)
            instance = new LocationUtil();
        return instance;
    }

    public BDLocation getBDLocation(String lat, String lon) {
        if (StringUtil.isEmpty(lat) || StringUtil.isEmpty(lon))
            return null;
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLongitude(Double.parseDouble(lon));
        bdLocation.setLatitude(Double.parseDouble(lat));
        return bdLocation;
    }

    public BDLocation getBDLocation(LocationBean locationBean) {
        if (locationBean == null) {
            Log.v("BD", "locationBean is null");
            return null;
        }
        BDLocation bdLocation = new BDLocation();
        if (!StringUtil.isEmpty(locationBean.getLat()))
            bdLocation.setLatitude(Double.parseDouble(locationBean.getLat()));
        if (!StringUtil.isEmpty(locationBean.getLon()))
            bdLocation.setLongitude(Double.parseDouble(locationBean.getLon()));
        return bdLocation;
    }

    public BNRoutePlanNode getBNRoutePlanNode(BDLocation bdLocation) {
        if (bdLocation == null) {
            Log.v("BD", "bdLocation is null");
            return null;
        }
        BNRoutePlanNode bnRoutePlanNode = new BNRoutePlanNode(LocationClient.getBDLocationInCoorType(bdLocation, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLongitude(),
                LocationClient.getBDLocationInCoorType(bdLocation, BDLocation.BDLOCATION_BD09LL_TO_GCJ02).getLatitude(), "", null, BNRoutePlanNode.CoordinateType.GCJ02);
        return bnRoutePlanNode;
    }

}
