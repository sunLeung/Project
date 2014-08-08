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
import com.car.utils.Utils;

@Service
public class ReserveService {
	
	@Resource(name = "reserveDao")
	private ReserveDao dao;

	protected ReserveDao getDAO() {
		return dao;
	}
	
	/**
	 * 获取用户自己的车辆数据
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getClientCarsInfo(String clientid){
		List<Map<String,Object>> result=getDAO().getClientCar(clientid);
		return result;
	}
	
	/**
	 * 获取4s店数据
	 * @param clientid
	 * @return
	 */
	public List<Map<String,String>> getShopsInfo(String clientid){
		//模拟数据
		
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String, String>();
		map.put("id", "f340562657e64e4099695c4b865017dd");
		map.put("name", "天河店");
		Map<String,String> map1=new HashMap<String, String>();
		map1.put("id", "45196726607940129ebf9b7dbf46ebd7");
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
	public List<Map<String,String>> getAppointmentTimeByShop(String shopid){
		//获取该4s店所有班组
		List<String> teamids= this.getDAO().getShopTeam(shopid);
		
		//获取可预约时间
		List<Map<String,Object>> appointmentTime=this.getDAO().getAppointmentTime();
		
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//过来重复时间点和4s的班组
		List<Date> distinct=new ArrayList<Date>();
		for(Map<String,Object> map:appointmentTime){
			String tid=(String)map.get("team_id");
			Date date=(Date)map.get("appointment_start");
			if(teamids.contains(tid)&&!distinct.contains(date)){
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
	 * 获取4s店的某时间点的可预约班组
	 * @param shopid
	 * @param time
	 * @return
	 */
	public List<Map<String,String>> getTeamByShopAndTime(String shopid,String time){
		//获取4s店所有班组
		List<String> teamids= this.getDAO().getShopTeam(shopid);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获取某时刻的可预约班组
		List<Map<String,Object>> appointmentTeamByTime=this.getDAO().getAppointmentTeam(d);
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:appointmentTeamByTime){
			String id=(String)map.get("id");
			String team_id=(String)map.get("team_id");
			String name=(String)map.get("name");
			if(teamids.contains(team_id)){
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
	 * 获取4s店的某时间点的可预约顾问
	 * @param shopid
	 * @param time
	 * @return
	 */
	public List<Map<String,String>> getConsultantByShopAndTime(String shopid,String time){
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
		Map<String,String> result=new HashMap<String, String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(timeid);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(d.getTime()<System.currentTimeMillis()){
			result.put("code", "6");
			result.put("msg", "预约失败，预约时间已过期。");
			return result;
		}
		if(isOther){
			if(otherCarNum.length()<=0&&otherCarVin.length()<=0){
				result.put("code", "2");
				result.put("msg", "预约失败，手动输入必须填写车牌号或车架号。");
				return result;
			}
		}else{
			List<Map<String,Object>> carInfo=this.dao.getCarInfo(carid);
			if(carInfo.size()==0){
				result.put("code", "3");
				result.put("msg", "预约失败，没有找到该车信息，请手动填写。");
				return result;
			}else{
				Map<String,Object> car=carInfo.get(0);
				otherCarNum=(String)car.get("register_no");
				otherCarVin=(String)car.get("vin");
			}
			//再次检查是否还有预约名额
			int remain=this.dao.getAppointmentRemain(appointmentid);
			if(remain<=0){
				result.put("code", "4");
				result.put("msg", "预约失败，已经没有预约名额。请选择其他预约。");
				return result;
			}
			int r=this.dao.createAppointment(openid, d, appointmentid, carid, isOther, otherCarNum, otherCarVin, timeid, teamid, consultantid);
			if(r>0){
				result.put("code", "1");
				result.put("msg", "预约成功。");
				return result;
			}
		}
		result.put("code", "5");
		result.put("msg", "预约失败，很不幸给别人抢先预约了，预约名额已满。");
		return result;
	}
	
	/**
	 * 查询预约
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> queryAppointment(String openid){
		String uid=Utils.getClientidByOpenid(openid);
		return this.getDAO().getClientAppointments(uid);
	}
	
	/**
	 * 取消预约
	 * @param appointmentid
	 * @return
	 */
	public int deleteAppointment(String appointmentid){
		return this.getDAO().deleteAppointment(appointmentid);
	}
	
	/**
	 * 获取可评价预约
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCanRateAppointment(String openid){
		String uid=Utils.getClientidByOpenid(openid);
		return this.getDAO().getOvertimeAppointment(uid);
	}
	
	/**
	 * 预约评价打分
	 * @param openid
	 * @param appointmentid
	 * @param tscore
	 * @param cscore
	 * @return
	 */
	public int ratingAppointment(String openid,String appointmentid,int tscore,int cscore){
		String uid=Utils.getClientidByOpenid(openid);
		return this.getDAO().ratingAppointment(uid,appointmentid,tscore,cscore);
	}
}
