package com.xbb.la.modellibrary.config;

import android.os.Environment;

import java.io.File;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 13:32
 * 描述：系统常量基类
 */

public class Constant {

    public interface IntentAction {
        public static String START_LOCATION_UPLOAD = "com.xbb.la.employee.start_location_upload";
        public static String STOP_LOCATION_UPLOAD = "com.xbb.la.employee.stop_location_upload";
        public static String STOP_LOCATION_UPDATE= "com.xbb.la.employee.stop_location_update";
        public static String TRANSACTION_TO_FINISH = "com.xbb.la.employee.transaction_to_finish";
        public static String LOCATION_DATA = "com.xbb.la.employee.update_user_location";
        /**
         * 从Intent获取图片路径的KEY
         */
        public static final String KEY_PHOTO_PATH = "com.xbb.la.employee.photo_path";

        public static final String ROUTE_PLAN_NODE = "com.xbb.la.employee.routePlanNode";

        public static final String CHANGE_STATE="com.xbb.la.employee.change_state";

        public static final String MISSION_COMPLETE="com.xbb.la.employee.mission_complete";

    }

    public interface IntentVariable{
        public static final String DESTINATION_LOCATION="destination_location";

        public static final String ORDER_ID="order_id";

        public static final String PARAMS_KEY="123456";
    }

    public interface Path {
        String PATH_BASE = Environment.getExternalStorageDirectory() + File.separator + "XBBEmployee";

        String URL_PREFIX_FILE = Url.BASE_URL + "/";

        String URL_WEB_BASE_PRODUCT = Url.BASE_URL + "/Weixin/Diy/index?p_id=";


        String PATH_VOICE = PATH_BASE + File.separator + "Voice";

        String PATH_PIC = PATH_BASE + File.separator + "Photo";

        String PATH_CACHE = PATH_PIC + File.separator + "Cache";

        String UPDATEA_PATH = PATH_BASE + File.separator + "xbb_employee.apk";

        String PATH_CRASH = PATH_BASE + File.separator + "CrashLog";

        String PATH_NAVI = PATH_BASE + File.separator + "BaiduNaviSDK_SO";


        int LOCATION_SCAN_SPAN = 20000;

        /**
         * 存储根目录
         */
        public static final String APP_ROOT_PATH = Environment.getExternalStorageDirectory().toString();
        /**
         * 图片缓存路径
         */
        public static final String PICTURE_ALBUM_PATH = APP_ROOT_PATH + "/XBBEmployee/";

        /**
         * 系统图片存储路径
         */
        public static final String PHOTO_SYS_PATH = APP_ROOT_PATH + "/DCIM/Camera/";

        String ADD_PIC_Path ="file:///android_asset/add";
    }

    public interface SP {
        public static String PREFERENCES = "remember";
        public static String UserInfo = "remember";
    }

    public interface DB {
        public static final String DB_NAME = "xbbEmployee.db";
        public static final String TABLE_TRANSACTION = "transactions";

        interface TransactionColumns {
            public static final String _ID = "_id";
            public static final String ORDER_ID = "orderId";
            public static final String EMPLOYEE_ID = "employeeId";
            public static final String ALBUM_NORMAL_BEFORE = "normal_album_before";
            public static final String ALBUM_DAMAGE_BEFORE = "damage_album_before";
            public static final String ALBUM_NORMAL_AFTER = "normal_album_after";
            public static final String ALBUM_DAMAGE_AFTER = "damage_album_after";
            public static final String EMPLOYEE_RECOMMAND = "employee_recommand";
            public static final String EMPLOYEE_SUGGEST = "employee_suggest";
            public static final String START_TIME = "start_time";
            public static final String END_TIME = "end_time";
            public static final String UPLOAD = "hasUpload";
            public static final String CURRENT_STEP = "currentStep";
        }

        public static final  String TABLE_DIY="diy";
        interface DIYColumns {
            public static final String _ID = "_id";
            public static final String DIY_ID = "diy_id";
            public static final String DIY_NAME = "diy_name";
            public static final String DIY_SELECTED_IMG = "diy_selected_img";
            public static final String DIY_UNSELECT_IMG = "diy_unselect_img";

        }

        public static final  String TABLE_REMINDER="reminder";
        interface ReminderColumns {
            public static final String _ID = "_id";
            public static final String NOTICE_ID = "notice_id";
            public static final String NOTICE_CONTENT = "notice_content";
            public static final String NOTICE_THUMB = "notice_thumb";
            public static final String NOTICE_TITLE = "notice_title";

        }

        public static final  String TABLE_RECOMMAND="recommand";
        interface RecommandColumns {
            public static final String _ID = "_id";
            public static final String RECOMMAND_ID = "recommand_id";
            public static final String RECOMMAND_ORDER_ID = "recommand_order_id";
            public static final String RECOMMAND_EMPLOYEE_ID = "recommand_employee_id";
            public static final String RECOMMAND_DIY_ID = "recommand_diy_id";
            public static final String RECOMMAND_DIY_NAME = "recommand_diy_name";
            public static final String RECOMMAND_DIY_WIMG = "recommand_wimg";
            public static final String RECOMMAND_DIY_XIMG = "recommand_ximg";
            public static final String RECOMMAND_INTRO_ALBUM = "recommand_intro_album";
            public static final String RECOMMAND_REMARK = "recommand_remark";

        }

        public static final  String TABLE_POSITION="position";
        interface PositionColumns {
            public static final String _ID = "_id";
            public static final String POSITION_LAT = "lat";
            public static final String POSITION_LNG= "lng";
            public static final String POSITION_ORDERID = "orderid";
            public static final String POSITION_UID = "userid";

        }
    }

    public final static long RECONNECTTIME = 10 * 1000;//十秒重连一次
    /**
     * SOCKETCLASS连接成功
     */
    public static final int SOCKETCLASS_SUC = 10001;
    /**
     * SOCKETCLASS连接失败
     */
    public static final int SOCKETCLASS_ERR = 10002;
    public static long refreshtime = 10 * 1000;//十秒刷新一次界面
    public static long relocationtime = 10 * 1000;//30秒定位一次

    /**
     * 用户位置获取失败
     */
    public static final int USER_LOCATION_FAILD = 10013;
    /**
     * 用户位置上传
     */
    public static final int USER_LOCATION = 11;

    public interface Url{
        String BASE_URL = "http://captainoak.cn";
    }

}
