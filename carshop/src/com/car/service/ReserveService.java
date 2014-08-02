package com.car.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.car.dao.ReserveDao;

@Service
public class ReserveService {
	
	@Resource(name = "reserveDao")
	private ReserveDao dao;

	protected ReserveDao getDAO() {
		return dao;
	}
	
	/**
	 * 获取用户自己的车辆数据
	 * @param uid
	 * @return
	 */
	public List<Map<String,String>> getMyCar(String uid){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "01");
		map.put("name", "大众新高尔夫");
		map.put("num", "粤A315Y");
		result.add(map);
		return result;
	}
	
	/**
	 * 获取4s店数据
	 * @return
	 */
	public List<Map<String,String>> getShop(String uid){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "shop01");
		map.put("name", "天河店");
		result.add(map);
		return result;
	}
	
	/**
	 * 通过店id获取可预约时间
	 * @param shopid
	 * @return
	 */
	public List<Map<String,String>> getSelectTime(String shopid){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "adk");
		map.put("time", "2014-08-02 10:00:00");
		result.add(map);
		return result;
	}
	
	/**
	 * 通过预约时间获取可预约班组
	 * @param timeid
	 * @return
	 */
	public List<Map<String,String>> getTeam(String timeid){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "adk");
		map.put("name", "Ateam");
		result.add(map);
		return result;
	}
	
	/**
	 * 通过预约时间获取可预约顾问
	 * @param timeid
	 * @return
	 */
	public List<Map<String,String>> getConsultant(String timeid){
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "adk");
		map.put("name", "梁宇新");
		result.add(map);
		return result;
	}
}
