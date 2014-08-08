package com.car.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	/**
	 * 获取日期当天x分钟后的时间
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date minToDate(Date date,int min){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sd=sdf.format(date);
		long add=min*60*1000;
		Date d=null;
		try {
			d = new Date(add+sdf.parse(sd).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 获取日期当天开始时间
	 * @param date
	 * @return
	 */
	public static Date starDate(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sd=sdf.format(date);
		Date d=null;
		try {
			d = new Date(sdf.parse(sd).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	/**
	 * 获取日期当天结束时间
	 * @param date
	 * @return
	 */
	public static Date endDate(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String sd=sdf.format(date);
		Date d=null;
		try {
			d = new Date(sdf.parse(sd).getTime()+24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
}
