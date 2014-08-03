package com.car.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
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
	
	public static void main(String[] args) {
		Date a=minToDate(new Date(),60);
		Date b=minToDate(new Date(System.currentTimeMillis()+24*60*60*1000),120);
		int i=(int) ((b.getTime()-a.getTime())/1000/60/60);
		System.out.println(i);
	}
}
