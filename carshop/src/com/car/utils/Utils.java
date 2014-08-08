package com.car.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Utils {
	/**测试数据 openid 与 clientid 映射表*/
	public static Map<String,String> ocMap=new HashMap<String, String>();
	/**测试数据 clientid 与 shopid 映射表*/
	public static Map<String,String> csMap=new HashMap<String, String>();
	/**测试数据*/
	static{
		ocMap.put("liangyx", "2df9a11c64ac4d63b693f77ab73c852f");
		
		csMap.put("2df9a11c64ac4d63b693f77ab73c852f", "f340562657e64e4099695c4b865017dd");
		
		//shop01 id f340562657e64e4099695c4b865017dd
		//shop02 id 45196726607940129ebf9b7dbf46ebd7
	}
	
	/**
	 * 通过微信openid获取4s店用户系统的客户Id
	 * @param openid
	 * @return
	 */
	public static String getClientidByOpenid(String openid){
		return ocMap.get(openid);
	}
	
	/**
	 * 通过客户ID获取4s店ID
	 * @param clientid
	 * @return
	 */
	public static String getShopidByClientid(String clientid){
		return csMap.get(clientid);
	}
	
	/**
	 * 创建32位的唯一字符串
	 * @return
	 */
	public static String createUUID(){
		String id=UUID.randomUUID().toString().replace("-", "");
		return id;
	}
	
	public static void main(String[] args) {
		System.out.println(createUUID());
	}
}
