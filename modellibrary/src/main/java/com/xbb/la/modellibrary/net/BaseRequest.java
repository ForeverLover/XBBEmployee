package com.xbb.la.modellibrary.net;

import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.xbb.la.modellibrary.utils.StringUtil;

import java.util.Map;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 11:36
 * 描述：网络请求基类
 */

public class BaseRequest {
    /**
     * 接口地址
     */
    public static final String URL_API = "http://captainoak.cn/Api/worker/";


    protected static void request(XRequestCallBack XRequestCallBack, int taskId, String doUrl) {
        request(XRequestCallBack, taskId, doUrl, null,null);
    }

    protected static void request(XRequestCallBack XRequestCallBack, int taskId, String doUrl,
                                  RequestParams requestParams,String flag) {
        XHttpRequest xHttpRequest = new XHttpRequest(taskId, XRequestCallBack, URL_API + doUrl,flag);
        xHttpRequest.requestPost(requestParams);
    }


    protected static void requestAfterSigned(XRequestCallBack XRequestCallBack, int taskId, String doUrl,
                                             Map<String, String> map,String flag) {
        XHttpRequest xHttpRequest = new XHttpRequest(taskId, XRequestCallBack, URL_API + doUrl,flag);
        RequestParams params = new RequestParams();
        params.addBodyParameter("sign", StringUtil.getMD5(StringUtil.getSignedParams(map)));
        Log.v("Tag","sign:"+StringUtil.getSignedParams(map));
        for (Map.Entry<String, String> paramMap : map.entrySet()) {
            params.addBodyParameter(paramMap.getKey(), paramMap.getValue());
        }
        xHttpRequest.requestPost(params);
    }

    protected static void requestByGet(XRequestCallBack XRequestCallBack, int taskId, String doUrl,
                                       RequestParams requestParams,String flag) {
        Log.v("Tag", "url:" + URL_API + doUrl);
        XHttpRequest xHttpRequest = new XHttpRequest(taskId, XRequestCallBack, URL_API + doUrl,flag);
        xHttpRequest.requestGet(requestParams);
    }

    protected XRequestParams createRequestParams() {
        return new XRequestParams();
    }
}
