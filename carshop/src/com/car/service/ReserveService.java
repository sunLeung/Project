package com.car.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	public List<Map<String,Object>> getMyCar(String uid){
		List<Map<String,Object>> result=getDAO().getOwnCar(uid);
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
		Map<String,String> map1=new HashMap<String, String>();
		map1.put("id", "shop02");
		map1.put("name", "番禺店");
		result.add(map);
		result.add(map1);
		return result;
	}
	
	/**
	 * 通过店id获取可预约时间
	 * @param shopid
	 * @return
	 */
	public List<Map<String,String>> getSelectTime(String shopid){
		List<Map<String,Object>> teamids= this.getDAO().getShopTeam(shopid);
		//取出这间店的所以班组id
		List<String> filterTeamis=new ArrayList<String>();
		for(Map<String,Object> map:teamids){
			String id=(String)map.get("id");
			filterTeamis.add(id);
		}
		List<Map<String,Object>> appointmentTime=this.getDAO().getAppointmentTime();
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Map<String,Object> map:appointmentTime){
			String tid=(String)map.get("team_id");
			Date date=(Date)map.get("appointment_start");
			if(filterTeamis.contains(tid)){
				Map<String,String> temp=new HashMap<String, String>();
				temp.put("id", sdf.format(date));
				temp.put("time", sdf.format(date));
				result.add(temp);
			}
		}
		if(result.size()==0){
			Map<String,String> temp=new HashMap<String, String>();
			temp.put("id", "other");
			temp.put("time", "预约已满");
			result.add(temp);
		}
		return result;
	}
	
	/**
	 * 通过预约时间获取可预约班组
	 * @param timeid
	 * @return
	 */
	public List<Map<String,String>> getTeam(String shopid,String time){
		List<Map<String,Object>> teamids= this.getDAO().getShopTeam(shopid);
		//取出这间店的所以班组id
		List<String> filterTeamis=new ArrayList<String>();
		for(Map<String,Object> map:teamids){
			String id=(String)map.get("id");
			filterTeamis.add(id);
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String,Object>> temp=this.getDAO().getAppointmentTeam(d);
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:temp){
			String id=(String)map.get("id");
			String team_id=(String)map.get("team_id");
			String name=(String)map.get("name");
			if(filterTeamis.contains(team_id)){
				Map<String,String> m=new HashMap<String, String>();
				m.put("id", id);
				m.put("tid", team_id);
				m.put("name", name);
				result.add(m);
			}
		}
		if(result.size()<=0){
			Map<String,String> m=new HashMap<String, String>();
			m.put("id", "other");
			m.put("tid", "");
			m.put("name", "班组已预约满");
			result.add(m);
		}
		return result;
	}
	
	/**
	 * 通过预约时间获取可预约顾问
	 * @param timeid
	 * @return
	 */
	public List<Map<String,String>> getConsultant(String shopid,String timeid){
		List<Map<String,Object>> teamids= this.getDAO().getShopTeam(shopid);
		//取出这间店的所以班组id
		List<String> filterTeamis=new ArrayList<String>();
		for(Map<String,Object> map:teamids){
			String id=(String)map.get("id");
			filterTeamis.add(id);
		}
		
		
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "adk");
		map.put("name", "梁宇新");
		result.add(map);
		return result;
	}
	
	
}
