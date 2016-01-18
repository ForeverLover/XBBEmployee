package com.xbb.la.modellibrary.net;

import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.xbb.la.modellibrary.bean.UploadRecommand;
import com.xbb.la.modellibrary.bean.UploadTransaction;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 11:33
 * 描述：系统级别请求
 */

public class ApiRequest extends BaseRequest implements IApiRequest {
    private XRequestCallBack XRequestCallBack;

    public ApiRequest(XRequestCallBack XRequestCallBack) {
        this.XRequestCallBack = XRequestCallBack;
    }

    @Override
    public void login(String user, String pwd, String channelId, String userId, String device) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("phone", user);
        map.put("password", StringUtil.getMD5(pwd));
        map.put("user_id", userId);
        map.put("channel_id", channelId);
        map.put("device", device);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.LOGIN, "login", map, null);
    }

    public void getDIYProducts() {
        request(XRequestCallBack, Task.DIY_PRODUCT, "diy");
    }

    @Override
    public void getKindReminders() {
        request(XRequestCallBack, Task.REMINDER, "wxnotice");
    }

    @Override
    public void getOrderList(String userId, int type, int pageIndex, int pageSize) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", userId);
        map.put("p", pageIndex + "");
        map.put("orderstate", type + "");
        map.put("timeline", currentTime);

        /*RequestParams params = createRequestParams();
        params.addBodyParameter("uid", userId);
        params.addBodyParameter("p", pageIndex + "");
        params.addBodyParameter("orderstate", type + "");*/

//        requestByGet(XRequestCallBack, Task.ORDER_LIST, "orderlist" + "/uid/" + userId + "/p/" + pageIndex + "/orderstate/" + type + "/sign/" + StringUtil.getMD5(StringUtil.getSignedParams(map))+ "/timeline/" + currentTime, null,null);
        requestAfterSigned(XRequestCallBack, Task.ORDER_LIST, "orderlist", map, null);
    }

    @Override
    public void getOrderInfo(String orderId) {
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("timeline", System.currentTimeMillis()+"");
        requestAfterSigned(XRequestCallBack, Task.ORDER, "orderDetail", map,orderId);
    }

    @Override
    public void confirm(String orderId, String uid) {
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("state", "1");
        map.put("uid", uid);
        map.put("timeline", System.currentTimeMillis()+"");
        requestAfterSigned(XRequestCallBack, Task.ACCEPT, "changeState", map,orderId);
    }

    @Override
    public void leave(String orderId, String uid) {
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("state", "2");
        map.put("uid", uid);
        map.put("timeline", System.currentTimeMillis()+"");
        requestAfterSigned(XRequestCallBack, Task.SET_OUT, "changeState", map,orderId);
    }

    @Override
    public void arrive(String orderId, String uid) {
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("state", "3");
        map.put("uid", uid);
        map.put("timeline", System.currentTimeMillis()+"");
        requestAfterSigned(XRequestCallBack, Task.ARRIVE, "changeState", map,orderId);
    }

    @Override
    public void commitMission(String orderId, String uid, UploadTransaction uploadTransaction) {
       /* String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("uid", uid);
        map.put("timeline", currentTime);*/
        RequestParams params = createRequestParams();
        params.addBodyParameter("orderid", orderId);
        params.addBodyParameter("work_start_time", uploadTransaction.getStartTime());
        params.addBodyParameter("work_end_time", uploadTransaction.getEndTime());
        params.addBodyParameter("uid", uid);
        List<File> normal_before_fileList = uploadTransaction.getStartNormalFileAlbum();
        List<File> damage_before_fileList = uploadTransaction.getStartDamageFileAlbum();
        List<File> damage_after_fileList = uploadTransaction.getEndDamageFileAlbum();
        List<File> normal_after_fileList = uploadTransaction.getEndNormalFileAlbum();
        List<UploadRecommand> uploadRecommandList = uploadTransaction.getUploadRecommandList();
        if (normal_before_fileList != null && !normal_before_fileList.isEmpty()) {
            for (int i = 0; i < normal_before_fileList.size();
                 i++) {
                params.addBodyParameter("before_body" + (i + 1), normal_before_fileList.get(i));
            }
//                Log.v("Tag", "normal_before_list:" + normal_before_fileList.size());
        }
        if (damage_before_fileList != null && !damage_before_fileList.isEmpty()) {
            for (int i = 0; i < damage_before_fileList.size();
                 i++) {
                params.addBodyParameter("before_nick" + (i + 1), damage_before_fileList.get(i));
            }
//                Log.v("Tag", "damage_before_list:" + damage_before_fileList.size());
        }
        if (damage_after_fileList != null && !damage_after_fileList.isEmpty()) {
            for (int i = 0; i < damage_after_fileList.size();
                 i++) {
                params.addBodyParameter("after_nick" + (i + 1), damage_after_fileList.get(i));
            }
//                Log.v("Tag", "damage_after_list:" + damage_after_fileList.size());
        }
        if (normal_after_fileList != null && !normal_after_fileList.isEmpty()) {
            for (int i = 0; i < normal_after_fileList.size();
                 i++) {
                params.addBodyParameter("after_body" + (i + 1), normal_after_fileList.get(i));
            }
//                Log.v("Tag", "normal_after_list:" + normal_after_fileList.size());
        }
        if (uploadRecommandList != null && !uploadRecommandList.isEmpty())
//                Log.v("Tag", "recommand_list:" + uploadRecommandList.size());
            for (int i = 0; i < uploadRecommandList.size(); i++) {
                UploadRecommand uploadRecommand = uploadRecommandList.get(i);
                if (uploadRecommand != null) {
//                    params.addBodyParameter("diy[" + (i + 1) + "]['id']", uploadRecommand.getDiyID());
                    params.addBodyParameter("diyremark[" + uploadRecommand.getDiyID() + "]", uploadRecommand.getRecommandRemark());
                    List<File> recommandFileList = uploadRecommand.getRecommandFileAlbum();
                    if (recommandFileList != null && !recommandFileList.isEmpty()) {
                        for (int j = 0; j < recommandFileList.size();
                             j++) {
                            params.addBodyParameter("diy_img_" + uploadRecommand.getDiyID() + "_img" + (j + 1), recommandFileList.get(j));
                        }
//                        Log.v("Tag", "recommand_img_list:" + recommandFileList.size());
                    }
                }
            }
        request(XRequestCallBack, Task.MISSION_COMPLETE, "uploadimg", params,null);
    }

    @Override
    public void uploadLocation(String userId, String orderId, String lat, String lng) {
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("orderid", orderId);
        map.put("uid", userId);
        map.put("lat", lat);
        map.put("lng", lng);
        map.put("timeline", System.currentTimeMillis()+"");
        requestAfterSigned(XRequestCallBack, Task.LOCATION_UPLOAD, "addpoint", map,null);
    }

    @Override
    public void changeAvatar(String uid, File file) {
        RequestParams params = createRequestParams();
        params.addBodyParameter("uid", uid);
        params.addBodyParameter("file", file);
        request(XRequestCallBack, Task.AVATAR_CHANGED, "updateHeadImg", params, null);
    }

    @Override
    public void changeAge(String uid, String age) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("age", age);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.AGE_CHANGED, "updateUserInfo", map, null);
    }

    @Override
    public void chageGender(String uid, String gender) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("sex", gender);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.GENDER_CHANGED, "updateUserInfo", map, null);

    }

    @Override
    public void changeName(String uid, String name) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("emp_name", name);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.ALTER_NAME, "updateUserInfo", map, null);
    }

    @Override
    public void changePwd(String uid, String oldpwd, String newpwd) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("oldpwd", StringUtil.getMD5(oldpwd));
        map.put("newpwd", StringUtil.getMD5(newpwd));
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.ALTER_PWD, "updatePwd", map, null);
    }

    @Override
    public void feedback(String uid, String feedback) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("content", feedback);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.FEEDBACK, "feedback", map, null);
    }

    @Override
    public void insertPushChannel(String uid, String userId, String channelId,String device) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("user_id", userId);
        map.put("channel_id", channelId);
        map.put("device", device);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.INSERT_PUSH, "saveChannelId", map, null);
    }

    @Override
    public void getMessageList(String uid, int pageIndex, int pageSize) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("p", pageIndex+"");
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.GET_MESSAGE, "message", map, null);
    }

    @Override
    public void getMessageDetail(String uid, String messageId) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("id", messageId);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.GET_MESSAGE_DETAIL, "messageDetail", map, null);
    }

    @Override
    public void getUnreadMessage(String uid) {
        String currentTime=System.currentTimeMillis()+"";
        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("uid", uid);
        map.put("timeline", currentTime);
        requestAfterSigned(XRequestCallBack, Task.UNREAD_MESSAGE, "messageCount", map, null);
    }

}
