package com.xbb.la.xbbemployee.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeRender {

	private static SimpleDateFormat formatBuilder;

	public static String getDate(String format) {
		formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(new Date());
	}

	public static String getDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}
	
	//从初始数据中获取时间
	public static String getDelayTime(String souce){
		String regEx = "(?<=stamp=\").*(?=\">)";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(souce);
		boolean rs1 = mat.find();
		String time = getDate();
		if (rs1) {
			time = mat.group(0);//20140822T01:58:40
			time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8)+ " " + time.substring(9);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
															
			try {
				Date date = format.parse(time);//2014-08-22 01:58:40
				long time_1 = date.getTime();  //1970-01-06 11:45:55
				time_1 = time_1 + 8*3600*1000;
				time = format.format(time_1);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return time;
	}

	public static Boolean DateCompare(String now,String ago) { 

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

		if(ago==null||"".equals(ago.trim())){
			ago="1970-01-01 00:00:00";
		}
		try {
		Date d1 = sdf.parse(now);
		Date d2 = sdf.parse(ago); 
		if(d1.getTime() - d2.getTime()>=0) { 
			
			return  true;
		} else{
			return  false;
		}
		} catch (Exception e) {
			return  false;
		}

	
	}
	
	/**
	 * 时间转换，去除秒数   yyyy-MM-dd HH:mm:ss  ----》》》  yyyy-MM-dd HH:mm
	 * @param time
	 * @return
	 */
	public static String timeFormat(String time){
		if(time == null){
			return "";
		}
		SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String s = null;
		try {
			s = out.format(in.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	/**
	 * 时间转换   yyyy-MM-dd HH:mm:ss  转  yyyy年MM月dd日  HH时mm分
	 * @param time
	 * @return
	 */
	
	public static String timeFormats(String time){
		if(time == null){
			return "";
		}
		SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat out = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
		String s = null;
		try {
			s = out.format(in.parse(time));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	
	/**
	 * 毫秒数转换成时间格式   时分秒
	 * @param l
	 * @return
	 */
	public static String formatLongToTimeStr(Long l) {
		int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        String time = "";
        second = l.intValue() / 1000;
 
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        if(hour>24){
       	 day = hour / 24;
       	 hour = hour%24;
       	 time = (getTwoLength(day)+"天"+getTwoLength(hour) + "小时" + getTwoLength(minute)  + "分"  + getTwoLength(second)+"秒");
       }else{
    	   time = getTwoLength(hour) + "小时" + getTwoLength(minute)  + "分"  + getTwoLength(second)+"秒";
       }
        return time;
    }
    
    private static String getTwoLength(final int data) {
        if(data < 10) {
        	if(data == 0){
        		return ""+data;
        	}
            return "0" + data;
        } else {
            return "" + data;
        }
    }
}
