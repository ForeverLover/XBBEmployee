package com.xbb.la.modellibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目名称：AndCampus
 * 类描述：
 * 创建人：Templar
 * 创建时间：2015/7/2 13:19
 * 修改备注：
 */
public class SystemUtil {

    private static SystemUtil instance = null;

    public static SystemUtil getInstance() {
        if (instance == null) {
            instance = new SystemUtil();
        }
        return instance;
    }

    /**
     * 判断当前环境网络是否可用
     *
     * @param mContext
     * @return true-网络可用 false-网络不可用
     */
    public boolean isNetworkEnable(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return false;
            } else {
                return info.isAvailable();
            }
        }
        return false;
    }

    public void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public String getSdcarDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean sdCardIsExit() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     *
     * @return /sdcard/
     */
    public static String getSDCardPath() {
        if (sdCardIsExit()) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
    }

    /**
     * 创建文件
     * <p>
     * 如果是/sdcard/download/123.doc则只需传入filePath=download/123.doc
     *
     * @param filePath 文件路径
     * @return 创建文件的路径
     * @throws IOException
     */
    public static String creatFile2SDCard(String filePath) throws IOException {
        // 无论传入什么值 都是从根目录开始 即/sdcard/+filePath
        // 创建文件路径包含的文件夹
        String filedir = creatDir2SDCard(getFileDir(filePath));
        String fileFinalPath = filedir + getFileName(filePath);
        File file = new File(fileFinalPath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return fileFinalPath;
    }

    /**
     * 创建文件夹
     *
     * @param dirPath
     */
    public static String creatDir2SDCard(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dirPath;
    }

    /**
     * 获取文件名
     *
     * @param filePath
     * @return
     */
    private static String getFileName(String filePath) {
        int index = 0;
        String tempName = "";
        if ((index = filePath.lastIndexOf("/")) != -1) {
            // 如果有后缀名才
            tempName = filePath.substring(index + 1);
        }
        return tempName.contains(".") ? tempName : "";
    }

    /**
     * 获取文件目录路径
     *
     * @param filePath
     * @return
     */
    private static String getFileDir(String filePath) {
        if (filePath.startsWith(getSDCardPath())) {
            return filePath.replace(getFileName(filePath), "");
        }
        return getSDCardPath() + filePath.replace(getFileName(filePath), "");
    }

    /**
     * @param inputStream
     * @param filePath
     */
    public static void creatFileByInputStream(InputStream inputStream,
                                              String filePath) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            creatFile2SDCard(filePath);
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(
                    new FileOutputStream(new File(filePath)));
            byte array[] = new byte[2048];
            int num = 0;
            while ((num = bufferedInputStream.read(array)) != -1) {
                bufferedOutputStream.write(array, 0, num);
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(
                fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: "
                            + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * 获取当前版本号
     *
     * @return
     * @throws
     */
    public String getClientVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            Log.v("Tag", "Name:" + context.getPackageName());
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.v("Tag", "versionName:" + packageInfo.versionName);
        return packageInfo.versionName;
    }


    /**
     * 将bitmap保存成文件
     *
     * @param bmp
     * @return
     */
    public String saveBitmap2file(Bitmap bmp) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        FileOutputStream stream = null;
        String path = getSDCardPath() + "/XBBEMPLOYEE/head.png";
        File file;
        try {
            file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            stream = new FileOutputStream(file);
            if (bmp.compress(format, quality, stream)) {
                stream.close();
                return path;
            } else {
                return "";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void saveAddPic(Bitmap bmp) {
        try {
            FileOutputStream fop = new FileOutputStream("/sdcard/mateadd.png");
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fop);
            //压缩bitmap写进outputStream 参数：输出格式  输出质量  目标OutputStream
            //格式可以为jpg,png,jpg不能存储透明
            fop.close();
            //关闭流
//            FileInputStream fip = new FileInputStream("/sdcard/mateadd.jpg");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] InputStreamToByte(InputStream is) {

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        try {
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();

            return imgdata;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String getCurrentTime() {
        Log.v("Tag", "" + System.currentTimeMillis());
        String currentTime = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date());
    }

    public Bitmap getBitmapFromFile(String path) {
        if (!StringUtil.isEmpty(path)) {
            InputStream is = null;
            try {
                is = new FileInputStream(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];

            opts.inPreferredConfig = Bitmap.Config.RGB_565;

            opts.inPurgeable = true;

            opts.inSampleSize = 4;

            opts.inInputShareable = true;
            Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
            return bitmap;
        }

        return null;
    }

    public File getFileByPath(String path) {
        if (StringUtil.isEmpty(path))
            return null;

        return new File(path);
    }

    public List<File> convertPathListToFileList(List<String> pathList) {
        if (pathList == null || pathList.isEmpty())
            return null;
        try{
            List<File> fileList = new ArrayList<File>();
            for (String path : pathList
                    ) {
                fileList.add(new File(path));
            }
            return fileList;
        }catch (Exception e){
            MLog.e("error","convertPathListToFileList:"+e.getMessage());
        }
        return null;

    }
}
