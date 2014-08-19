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

import com.car.dao.ClientDao;
import com.car.dao.ReserveDao;

@Service
public class ReserveService {
	
	@Resource(name = "reserveDao")
	private ReserveDao reserveDao;

	protected ReserveDao getReserveDAO() {
		return reserveDao;
	}
	
	@Resource(name = "clientDao")
	private ClientDao clientDao;
	
	protected ClientDao getClientDao() {
		return clientDao;
	}
	
	/**
	 * 获取用户自己的车辆数据
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getClientCarsInfo(String openid){
		String clientid=this.getClientDao().getClientid(openid);
		List<Map<String,Object>> result=getReserveDAO().getClientCar(clientid);
		return result;
	}
	
	/**
	 * 获取4s店数据
	 * @param openid
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getShopsInfo(String openid){
		String clientid=this.getClientDao().getClientid(openid);
		//获取最近预约过的4s店
		String recentShopid=this.reserveDao.getRecentAppointmentShopid(clientid);
		//购车的4s店
		List<String> buyCarShops=this.reserveDao.getBuyCarShopid(openid);
		//所有的4s店
		List<Map<String,Object>> shopInfo=this.reserveDao.getAllShopInfo();
		//排序结果返回数据
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		
		if(recentShopid!=null&&recentShopid!=""){
			for(Map<String,Object> map:shopInfo){
				String shopid=(String)map.get("own_no");
				if(recentShopid.equals(shopid)){
					result.add(map);
					break;
				}
			}
		}
		
		if(buyCarShops!=null&&buyCarShops.size()>0){
			for(String sid:buyCarShops){
				for(Map<String,Object> map:shopInfo){
					String shopid=(String)map.get("own_no");
					if(sid.equals(shopid)&&!result.contains(map)){
						result.add(map);
						break;
					}
				}
			}
		}
		
		for(Map<String,Object> map:shopInfo){
			if(result.contains(map)){
				continue;
			}else{
				String name=(String)map.get("print_title");
				if(name!=null&&name!="")
					result.add(map);
			}
		}
		return result;
	}
	
	/**
	 * 通过店id获取可预约时间
	 * @param shopid
	 * @return
	 */
	public List<Map<String,String>> getAppointmentTimeByShop(String shopid){
		//获取该4s店所有班组
		List<String> teamids= this.getReserveDAO().getShopTeam(shopid);
		
		//获取可预约时间
		List<Map<String,Object>> appointmentTime=this.getReserveDAO().getAppointmentTime();
		
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		
		//过滤重复时间点和4s的班组
		List<Date> distinct=new ArrayList<Date>();
		for(Map<String,Object> map:appointmentTime){
			String tid=(String)map.get("team_id");
			Date dateBegin=(Date)map.get("appointment_start");
			if(teamids.contains(tid)&&!distinct.contains(dateBegin)){
				Map<String,String> temp=new HashMap<String, String>();
				Date dateEnd=(Date)map.get("appointment_end");
				temp.put("id", sdf.format(dateBegin)+"-"+sdf.format(dateEnd));
				temp.put("begin", sdf.format(dateBegin));
				temp.put("end", sdf.format(dateEnd));
				result.add(temp);
				distinct.add(dateBegin);
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
		List<String> teamids= this.getReserveDAO().getShopTeam(shopid);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date d = null;
		try {
			d = sdf.parse(time.split("-")[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//获取某时刻的可预约班组
		List<Map<String,Object>> appointmentTeamByTime=this.getReserveDAO().getAppointmentTeam(d);
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
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date d = null;
		try {
			d = sdf.parse(time.split("-")[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String,String>> result=this.getReserveDAO().getAppointmentConsultant(d,shopid);
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
			String otherCarVin,String shopid, String timeid, String teamid,
			String consultantid) {
		Map<String,String> result=new HashMap<String, String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ds = null;
		Date de = null;
		try {
			String[] dates=timeid.split("-");
			ds = sdf.parse(dates[0]);
			de = sdf.parse(dates[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(ds.getTime()<System.currentTimeMillis()){
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
			List<Map<String,Object>> carInfo=this.reserveDao.getCarInfo(carid);
			if(carInfo.size()==0){
				result.put("code", "3");
				result.put("msg", "预约失败，没有找到该车信息，请手动填写。");
				return result;
			}else{
				Map<String,Object> car=carInfo.get(0);
				otherCarNum=(String)car.get("register_no");
				otherCarVin=(String)car.get("vin");
			}
		}
		//再次检查是否还有预约名额
		int remain=this.reserveDao.getAppointmentRemain(appointmentid);
		if(remain<=0){
			result.put("code", "4");
			result.put("msg", "预约失败，已经没有预约名额。请选择其他预约。");
			return result;
		}
		String clientid=this.getClientDao().getClientid(openid);
		int r=this.reserveDao.createAppointment(clientid, ds,de, appointmentid, carid, isOther, otherCarNum, otherCarVin,shopid, timeid, teamid, consultantid);
		if(r>0){
			result.put("code", "1");
			result.put("msg", "预约成功。");
			return result;
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
		String clientid=this.getClientDao().getClientid(openid);
		return this.getReserveDAO().getClientAppointments(clientid);
	}
	
	/**
	 * 取消预约
	 * @param appointmentid
	 * @return
	 */
	public int deleteAppointment(String appointmentid){
		return this.getReserveDAO().deleteAppointment(appointmentid);
	}
	
	/**
	 * 获取可评价预约
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getCanRateAppointment(String openid){
		String clientid=this.getClientDao().getClientid(openid);
		return this.getReserveDAO().getOvertimeAppointment(clientid);
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
		String clientid=this.getClientDao().getClientid(openid);
		return this.getReserveDAO().ratingAppointment(clientid,appointmentid,tscore,cscore);
	}
	
	public Map<String,Object> getShopDetail(String shopid){
		return this.getReserveDAO().getShopDetail(shopid);
	}
}
