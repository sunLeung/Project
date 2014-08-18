package com.car.utils;

import java.io.UnsupportedEncodingException;
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
	
	/**
	 * 获取中文首字母
	 */
	private static int GB_SP_DIFF = 160;
	private static int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594,
			2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390,
			4558, 4684, 4925, 5249, 5600 };
	private static char[] firstLetter = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J','K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'W', 'X', 'Y', 'Z' };

	public static char convert(String ch) {
		byte[] bytes = new byte[2];
		try {
			bytes = ch.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		char result = '-';
		int secPosValue = 0;
		int i;
		for (i = 0; i < bytes.length; i++) {
			bytes[i] -= GB_SP_DIFF;
		}
		secPosValue = bytes[0] * 100 + bytes[1];
		for (i = 0; i < 23; i++) {
			if (secPosValue >= secPosValueList[i]
					&& secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(createUUID());
	}
}
