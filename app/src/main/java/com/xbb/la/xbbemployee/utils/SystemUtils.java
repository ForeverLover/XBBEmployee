package com.xbb.la.xbbemployee.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.SystemUtil;

import java.util.TreeMap;

import static com.xbb.la.modellibrary.config.Constant.TempSet.operateOrderMap;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class SystemUtils {
    private static SystemUtils instance;

    public static SystemUtils getInstance() {
        if (instance == null)
            instance = new SystemUtils();
        return instance;
    }

    public void addOperateOrder(String orderId, OrderInfo orderInfo) {
        operateOrderMap.put(orderId, orderInfo);
    }

    public void removeOperateOrder(String orderId) {
        operateOrderMap.remove(orderId);
    }

    public void clearOperateOrder() {
        operateOrderMap.clear();
    }

    public OrderInfo getOrderById(String orderId) {
        if (operateOrderMap.containsKey(orderId)) {
            return operateOrderMap.get(orderId);
        }
        return null;
    }

    /**
     * 获取当前应用的版本号
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static String getClientVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
