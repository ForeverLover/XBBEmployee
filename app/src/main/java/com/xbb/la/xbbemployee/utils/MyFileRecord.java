package com.xbb.la.xbbemployee.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/7 10:37
 * 描述：
 */

public class MyFileRecord {
    private static MyFileRecord instance;
    public static final String TAG = "myrecord";
    private DateFormat formatter = new SimpleDateFormat("HH-mm-ss");
    private DateFormat filenamematter = new SimpleDateFormat("yyyy-MM-dd");

    public MyFileRecord() {
    }

    public static MyFileRecord getInstance() {
        if (instance == null) {
            instance = new MyFileRecord();
        }
        return instance;
    }

    /**
     * 保存信息到文件中
     * @params which 1定位失败
     * 2 socket 断开
     * 3 socket 重新连接
     * 4 网络可用
     * 5 没有可用网络
     * 6 用户登陆
     * 7 用户退出
     */
    public synchronized String saveRecordInfoFile(int which,String append) {
        Log.i("", "saveCrashInfo2File=" + which);
        StringBuffer sb = new StringBuffer();
        String records = "";
        if (which == 1) {
            records = "定位失败-";
        } else if (which == 2) {
            records = "socket 断开";
        } else if (which == 3) {
            records = "socket 重新连接";
        }else if(which==4){
            records = "\n网络名:"+append;
        }else if(which==5){
            records ="\n"+append;
        }
        sb.append(records);

        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileTime = filenamematter.format(new Date());
            sb.append(time+"\n");
            String fileName = "record-" + fileTime+".log";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                        + "/record/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(path + fileName,true);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}

