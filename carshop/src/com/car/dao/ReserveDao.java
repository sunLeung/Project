package com.car.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.car.utils.DateUtils;
import com.car.utils.Utils;

@Repository
public class ReserveDao {
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * 获取客户的车辆信息
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getClientCar(String openid){
		String sql = "select b.id,b.model_code,b.model from wechat_identity_correlation a left join vehicle b on a.vehicle_id=b.id where a.open_id=?";
		return jdbcTemplate.queryForList(sql,openid);
	}
	
	
	/**
	 * 获取4s店的班组
	 * @param shopid
	 * @return
	 */
	public List<String> getShopTeam(String shopid){
		String sql = "select id from appointment_team where own_no=?";
		return jdbcTemplate.queryForList(sql,String.class,shopid);
	}
	
	/**
	 * 获取可预约时间
	 * @return
	 */
	public synchronized List<Map<String,Object>> getAppointmentTime(){
		//因为只能预约今天和明天所以需要检查今天和明天的数据是否初始化
		initAppointmentTeamRemainsData(new Date());
		initAppointmentTeamRemainsData(new Date(System.currentTimeMillis()+24*60*60*1000));
		
		Date starDate=new Date();
		Date endDate=DateUtils.endDate(new Date(System.currentTimeMillis()+24*60*60*1000));
		String sql = "select team_id,appointment_start,appointment_end from appointment_team_remains where remains>0 and appointment_start>=? and appointment_start<=?";
		return jdbcTemplate.queryForList(sql,starDate,endDate);
	}
	
	/**
	 * 初始化某天的预约表数据
	 * @param date
	 */
	private void initAppointmentTeamRemainsData(Date date){
		//检查appointment_team_remains表是否已经从预约模板生成预约数据
		String checkSql="select count(id) from appointment_team_remains where appointment_start>?";
		int i=jdbcTemplate.queryForInt(checkSql,DateUtils.starDate(date));
		//还没有初始化的话就去模板表取出数据初始化
		if(i==0){
			//过滤放假的班组
			String selectOffDayTeamSql="select team_id from appointment_team_dayoff where dayoff_date>=? and dayoff_date<=?";
			List<String> offDayTeamids=jdbcTemplate.queryForList(selectOffDayTeamSql,String.class, DateUtils.starDate(date),DateUtils.endDate(date));
			
			//获取班组配置表数据
			String selectTeamConfigSql="select team_id,start_mins_of_day,end_mins_of_day,capacity from appointment_team_capacity_cfg";
			List<Map<String,Object>> getTeamConfigDate=jdbcTemplate.queryForList(selectTeamConfigSql);
			
			//新建预约表数据
			String insertAppointmentTeamRemainsSql="insert into appointment_team_remains(id,team_id,appointment_start,appointment_end,hour_of_day,remains) values(?,?,?,?,?,?)";
			List<Object[]> params=new ArrayList<Object[]>();
			for(Map<String,Object> voconfig:getTeamConfigDate){
				String team_id=(String)voconfig.get("team_id");
				BigDecimal start_mins_of_day=(BigDecimal)voconfig.get("start_mins_of_day");
				BigDecimal end_mins_of_day=(BigDecimal)voconfig.get("end_mins_of_day");
				BigDecimal capacity=(BigDecimal)voconfig.get("capacity");
				if(offDayTeamids.contains(team_id)){
					continue;
				}
				Date appointment_start=DateUtils.minToDate(date,start_mins_of_day.intValue());
				Date appointment_end=DateUtils.minToDate(date,end_mins_of_day.intValue());
				int hour_of_day=(int) ((appointment_end.getTime()-appointment_start.getTime())/1000/60/60);
				hour_of_day=hour_of_day<0?0:hour_of_day;
				Object[] param=new Object[]{Utils.createUUID(),team_id,appointment_start,appointment_end,hour_of_day,capacity.intValue()};
				params.add(param);
			}
			jdbcTemplate.batchUpdate(insertAppointmentTeamRemainsSql,params);
		}
	}
	
	/**
	 * 获取某时间班组
	 * @param date
	 * @return
	 */
	public List<Map<String, Object>> getAppointmentTeam(Date date) {
		String sql = "select appointment_team_remains.id,appointment_team_remains.team_id,appointment_team.name from appointment_team_remains join appointment_team on appointment_team.id=appointment_team_remains.team_id where appointment_team_remains.appointment_start=? and appointment_team_remains.remains>0";
		return jdbcTemplate.queryForList(sql, date);
	}
	
	/**
	 * 获取顾问
	 * @param date
	 * @param shopid
	 * @return
	 */
	public List<Map<String,String>> getAppointmentConsultant(Date date,String shopid){
		//获取放假的顾问id
		String selectDayoffConsultantSql="select consultant_id from appointment_consultant_dayoff where dayoff_date>=? and dayoff_date<=?";
		List<String> dayoffConsultants=jdbcTemplate.queryForList(selectDayoffConsultantSql,String.class, DateUtils.starDate(date),DateUtils.endDate(date));
		
		//获取某4s店的顾问
		String selectConsultantSql="select id,name from appointment_consultant where own_no=?";
		List<Map<String,Object>> consultantList=jdbcTemplate.queryForList(selectConsultantSql,shopid);
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		
		//过滤放假的顾问
		for(Map<String,Object> map:consultantList){
			String cid=(String)map.get("id");
			String cname=(String)map.get("name");
			if(dayoffConsultants.contains(cid)){
				continue;
			}else{
				Map<String,String> m=new HashMap<String, String>();
				m.put("id", cid);
				m.put("name", cname);
				result.add(m);
			}
		}
		return result;
	}
	
	/**
	 * 创建预约
	 * @param openid
	 * @param appointmentStart
	 * @param appointmentEnd
	 * @param appointmentid
	 * @param carid
	 * @param isOther
	 * @param otherCarNum
	 * @param otherCarVin
	 * @param Shopid
	 * @param timeid
	 * @param teamid
	 * @param consultantid
	 * @return
	 */
	public synchronized int createAppointment(String clientid,
			Date appointmentStart,Date appointmentEnd, String appointmentid, String carid,
			boolean isOther, String otherCarNum, String otherCarVin,String Shopid,
			String timeid, String teamid, String consultantid) {
		String sql = "insert into appointment_detail(id,team_id,consultant_id,client_id,appointment_start,appointment_end,vin,register_no,own_no,status) values(?,?,?,?,?,?,?,?,?,?)";
		// 再次检查是否还有预约名额
		int remain = getAppointmentRemain(appointmentid);
		if(remain>0){
			int r = this.jdbcTemplate.update(sql, Utils.createUUID(), teamid,consultantid, clientid, appointmentStart,appointmentEnd, otherCarVin,otherCarNum,Shopid, 1);
			if (r > 0) {
				String updateRemains = "update appointment_team_remains set remains=(remains-1) where id=?";
				return this.jdbcTemplate.update(updateRemains, appointmentid);
			}
		}
		return -1;
	}
	
	/**
	 * 获取车辆信息
	 * @param carid
	 * @return
	 */
	public List<Map<String,Object>> getCarInfo(String carid){
		String sql="select vin,register_no from vehicle where id=?";
		return this.jdbcTemplate.queryForList(sql,carid);
	}
	
	/**
	 * 获取某个预约实例的剩余数
	 * @param appointmentid
	 * @return
	 */
	public int getAppointmentRemain(String appointmentid){
		String sql="select remains from appointment_team_remains where id=?";
		return this.jdbcTemplate.queryForInt(sql,appointmentid);
	}
	
	/**
	 * 获取客户预约信息
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getClientAppointments(String clientid){
		String sql="select a.id,a.appointment_start,a.appointment_end,a.model,b.address,b.print_title,b.telephone,a.vin,a.register_no from appointment_detail a left join USER_REG_INFO b on a.own_no=b.own_no where a.status=1 and a.client_id=? and a.appointment_start>?";
		return this.jdbcTemplate.queryForList(sql,clientid,new Date());
	}
	
	/**
	 * 取消预约
	 * @param appointmentid
	 * @return
	 */
	public int deleteAppointment(String appointmentid){
		String sql="update appointment_detail set status=? where id=?";
		return this.jdbcTemplate.update(sql,2,appointmentid);
	}
	
	/**
	 * 获取已完成服务的预约
	 * @param clientid
	 * @return
	 */
	public List<Map<String,Object>> getOvertimeAppointment(String clientid){
		String sql="select * from appointment_detail where status=3 and client_id=?";
		return this.jdbcTemplate.queryForList(sql,clientid);
	}
	
	/**
	 * 预约评价打分
	 * @param uid
	 * @param appointmentid
	 * @param tscore
	 * @param cscore
	 * @return
	 */
	public int ratingAppointment(String uid,String appointmentid,int tscore,int cscore){
		String insertRateSql="insert into appointment_rating(id,appointment_detail_id,team_grade,consultant_grade,client_id) values(?,?,?,?,?)";
		int i= this.jdbcTemplate.update(insertRateSql,Utils.createUUID(),appointmentid,tscore,cscore,uid);
		int result=0;
		if(i==1){
			String updateAppointmentSql="update appointment_detail set status=? where id=?";
			result=this.jdbcTemplate.update(updateAppointmentSql,4,appointmentid);
		}
		return result;
	}
	
	/**
	 * 获取客户最近一次的预约店铺ID
	 * @param clientid
	 * @return
	 */
	public String getRecentAppointmentShopid(String clientid){
		String sql="SELECT own_no FROM (SELECT own_no FROM appointment_detail where client_id=? ORDER BY appointment_start) WHERE ROWNUM <= 1 ORDER BY ROWNUM DESC";
		List<String> shopid = this.jdbcTemplate.queryForList(sql, String.class,clientid);
		if(shopid.size()>0){
			return shopid.get(0);
		}
		return null;
	}
	
	/**
	 * 获取客户购买车的4s店
	 * @param openid
	 * @return
	 */
	public List<String> getBuyCarShopid(String openid){
		String sql="select own_no from wechat_identity_correlation a left join vehicle b on a.vehicle_id=b.id where open_id=?";
		List<String> shopids = this.jdbcTemplate.queryForList(sql, String.class,openid);
		return shopids;
	}
	
	/**
	 * 获取所有4s店
	 * @return
	 */
	public List<Map<String,Object>> getAllShopInfo(){
		String sql="select own_no,print_title from user_reg_info";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取4S店信息
	 * @param shopid
	 * @return
	 */
	public Map<String,Object> getShopDetail(String shopid){
		String sql="select address,telephone from user_reg_info where own_no=?";
		return this.jdbcTemplate.queryForMap(sql, shopid);
	}
	
	/**
	 * 获取车辆信息
	 * @param mycarid
	 * @return
	 */
	public Map<String,Object> getMycarInfo(String mycarid){
		String sql="select vin,register_no from vehicle where id=?";
		return this.jdbcTemplate.queryForMap(sql, mycarid);
	}
}
