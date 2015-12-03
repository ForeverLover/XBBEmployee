package com.xbb.la.modellibrary.utils;

import android.text.TextUtils;
import android.util.Log;

import com.xbb.la.modellibrary.config.Constant;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：AndCampus
 * 类描述：字符处理工具类
 * 创建人：Templar
 * 创建时间：2015/7/2 14:54
 * 修改备注：
 */
public class StringUtil {

    public static final String ILLLEAGAL_STR = "[^ !@#$%\\\\^&*()]+ ";

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }


    /**
     * 判断字符串是否是合法的手机号
     *
     * @param telStr
     * @return
     */
    public static boolean isTel(String telStr) {
        if (!isEmpty(telStr)) {
            Pattern p = Pattern.compile("^[1][34578]\\d{9}$");
            if (p != null) {
                Matcher matcher = p.matcher(telStr);
                if (matcher.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断固话格式是否正确
     *
     * @param fixedPhone
     * @return
     */
    public static boolean isFixedPhone(String fixedPhone) {
        if (!isEmpty(fixedPhone)) {
            Pattern p = Pattern.compile("^(\\d{3,4}-)?\\d{7,8}$");
            if (p != null) {
                Matcher matcher = p.matcher(fixedPhone);
                if (matcher.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (!isEmpty(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }


    /**
     * 进行md5加密
     *
     * @param val
     * @return
     */
    public static String getMD5(String val) {
        if (StringUtil.isEmpty(val))
            return "";
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    val.getBytes(/*"utf-8"*/"gb2312"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        return bytetoString(hash);
    }

    public static String bytetoString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1) {
                str = str + "0" + tempStr;
            } else {
                str = str + tempStr;
            }
        }
        Log.v("Tag", "hash:" + str.toLowerCase());
        return str.toLowerCase();
    }


    /**
     * 将日期字符串格式统一为yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String convertMonth(String time) {
        SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat original = new SimpleDateFormat("yyyy-M-d");
        Date date = null;
        try {
            date = original.parse(time);
            long timeStemp = date.getTime();
            String after = target.format(new Date(timeStemp));
            return after;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取两位小数.价格
     *
     * @param str
     * @return
     */
    public static String getMoneyStyleStr(String str) {
        if (isEmpty(str)) {
            return "";
        }
        Log.v("Tag", "str.contains(\".\")" + str.contains(".") + " " + str.contains(".") + " str:" + str);
        if (str.contains(".")) {

            String st[] = str.split("\\.");
            Log.v("Tag", "st[1].length() == 1?" + (st[1].length() == 1));
            if (st[1].length() == 1)
                return str + "0";

            return str;
        } else
            return str + ".00";

    }

    public static String covertStringListToString(List<String> sourceList) {
        if (sourceList == null || sourceList.isEmpty())
            return "";
        String target = "";
        for (String str : sourceList) {
            target += str + "~";
        }
        if (target.endsWith("~"))
            target = target.substring(0, target.length() - 1);
        return target;
    }

    public static List<String> covertStringToStringList(String source) {
        if (isEmpty(source))
            return null;
        List<String> targetList = new ArrayList<String>();
        String[] sourceArray = source.split("~");
        for (int i = 0; i < sourceArray.length; i++) {
            targetList.add(sourceArray[i]);
        }
        return targetList;
    }

    public static String getTimeFromStamp(String source) {
        String timeStr = "";
        if (isNumeric(source)) {
            Date date = new Date(Long.parseLong(source));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeStr = formatter.format(date);
        }
        return timeStr;
    }

    public static String getDealTime(String time) {
        if (time == null || "".equals(time)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if (current.after(today)) {
            return "今天 " + time.split(" ")[1];
        } else if (current.before(today) && current.after(yesterday)) {

            return "昨天 " + time.split(" ")[1];
        } else {
            int index = time.indexOf("-") + 1;
            return time.substring(index, time.length()).split(" ")[0];
//            return time.split(" ")[0];
        }

    }

    //转换为时分
    public static String getHourMinute(int minutes) {
        if (minutes < 60)
            return minutes + "分钟";
        else if (minutes % 60 == 0)
            return minutes / 60 + "小时";
        else {
            int hour = minutes / 60;
            int minute = minutes % 60;
            return hour + "小时" + minute + "分钟";
        }

    }

    // 距离转换
    public static String distanceFormatter(int distance) {
        if (distance < 1000) {
            return distance + "米";
        } else if (distance % 1000 == 0) {
            return distance / 1000 + "公里";
        } else {
            DecimalFormat df = new DecimalFormat("0.0");
            int a1 = distance / 1000; // 十位

            double a2 = distance % 1000;
            double a3 = a2 / 1000; // 得到个位

            String result = df.format(a3);
            double total = Double.parseDouble(result) + a1;
            return total + "公里";
        }
    }

    public static String getSignedParams(Map<String, String> params) {
        if (params == null || params.isEmpty())
            return "";
        String str = "";
        for (Map.Entry<String, String> nameValuePair : params.entrySet()) {
            str = str + nameValuePair.getKey() + "=" + nameValuePair.getValue() + "&";
        }
        if (str.endsWith("&"))
            str = str.substring(0, str.length() - 1);
        return str + Constant.IntentVariable.PARAMS_KEY;
    }

    /**
     * 生成md5编码字符串.
     *
     * @param str     源字符串
     * @param charset 编码方式
     * @return
     */
    public static String md5(String str, String charset) {
        if (str == null)
            return null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        MessageDigest md5MessageDigest = null;
        byte[] md5Bytes = null;
        char md5Chars[] = null;
        byte[] strBytes = null;
        try {
            strBytes = str.getBytes(charset);
            md5MessageDigest = MessageDigest.getInstance("MD5");
            md5MessageDigest.update(strBytes);
            md5Bytes = md5MessageDigest.digest();
            int j = md5Bytes.length;
            md5Chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte md5Byte = md5Bytes[i];
                md5Chars[k++] = hexDigits[md5Byte >>> 4 & 0xf];
                md5Chars[k++] = hexDigits[md5Byte & 0xf];
            }
            return new String(md5Chars);
        } catch (NoSuchAlgorithmException e) {
            //Log.output(e.toString(), Log.STD_ERR);
            return null;
        } catch (UnsupportedEncodingException e) {
            //Log.output(e.toString(), Log.STD_ERR);
            return null;
        } finally {
            md5MessageDigest = null;
            strBytes = null;
            md5Bytes = null;
        }
    }

    public static  boolean compareStringList(List<String> source, List<String> target) {
        if (source.size() != target.size()) return false;
        for (int i=0;i<source.size();i++)
            if (!source.get(i).equals(target.get(i))) return false;
        return true;
    }
}
