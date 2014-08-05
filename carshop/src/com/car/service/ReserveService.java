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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.car.dao.ReserveDao;
import com.car.utils.UserUtils;

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
		List<Date> distinct=new ArrayList<Date>();
		for(Map<String,Object> map:appointmentTime){
			String tid=(String)map.get("team_id");
			Date date=(Date)map.get("appointment_start");
			if(filterTeamis.contains(tid)&&!distinct.contains(date)){
				Map<String,String> temp=new HashMap<String, String>();
				temp.put("id", sdf.format(date));
				temp.put("time", sdf.format(date));
				result.add(temp);
				distinct.add(date);
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
	public List<Map<String,String>> getConsultant(String shopid,String time){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String,String>> result=this.getDAO().getAppointmentConsultant(d,shopid);
		if(result.size()<=0){
			Map<String,String> map=new HashMap<String, String>();
			map.put("id", "other");
			map.put("name", "没有顾问");
			result.add(map);
		}
		return result;
	}
	
	/**
	 * 创建预约
	 * @param openid
	 * @param appointmentid
	 * @param carid
	 * @param isOther
	 * @param otherCarNum
	 * @param otherCarVin
	 * @param timeid
	 * @param teamid
	 * @param consultantid
	 * @return
	 */
	public Map<String, String> createAppointment(String openid,String appointmentid,
			String carid, boolean isOther, String otherCarNum,
			String otherCarVin, String timeid, String teamid,
			String consultantid) {
		return this.getDAO().createAppointment(openid, appointmentid, carid, isOther, otherCarNum, otherCarVin, timeid, teamid, consultantid);
	}
	
	/**
	 * 查询预约
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> queryAppointment(String openid){
		String uid=UserUtils.getUserIdByOpenId(openid);
		return this.getDAO().getAppointment(uid);
	}
	
	public int deleteAppointment(String aid){
		return this.getDAO().deleteAppointment(aid);
	}
	
	public List<Map<String,Object>> rateAppointment(String openid){
		String uid=UserUtils.getUserIdByOpenId(openid);
		return this.getDAO().getOvertimeAppointment(uid);
	}
	public int ratingAppointment(String openid,String aid,int tscore,int cscore){
		String uid=UserUtils.getUserIdByOpenId(openid);
		return this.getDAO().ratingAppointment(uid,aid,tscore,cscore);
	}
}
