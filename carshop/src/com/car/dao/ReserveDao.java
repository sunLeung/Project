package com.car.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.car.utils.DateUtils;

@Repository
public class ReserveDao {
	private Logger log = LoggerFactory.getLogger(ReserveDao.class);
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/*值班的班组 key是日期yyyy-MM-dd value是teamid*/
	public Map<String,String> ondutyTeamContent=new ConcurrentHashMap<String, String>();
	
	
	/**
	 * 获取自己的车
	 * @param values
	 * @return
	 */
	public List<Map<String,Object>> getOwnCar(Object... values){
		final String sql = "select id,register_no,model from vehicle where member_id=?";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(values) + "]");
		}
		return jdbcTemplate.queryForList(sql,values);
	}
	
	/**
	 * 获取点铺的班组
	 * @param shopid
	 * @return
	 */
	public List<Map<String,Object>> getShopTeam(String shopid){
		String sql = "select id from appointment_team where BRANCH_ID='"+shopid+"'";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(shopid) + "]");
		}
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 获取可预约时间
	 * @return
	 */
	public List<Map<String,Object>> getAppointmentTime(){
		//因为只能预约今天和明天所以需要检查今天和明天的数据是否初始化
		initAppointmentTeamRemainsData(new Date());
		initAppointmentTeamRemainsData(new Date(System.currentTimeMillis()+24*60*60*1000));
		
		Date starDate=DateUtils.starDate(new Date());
		Date endDate=DateUtils.starDate(new Date(System.currentTimeMillis()+24*60*60*1000));
		final String sql = "select team_id,appointment_start from appointment_team_remains where remains>0 and appointment_start>=? and appointment_start<=?";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(new Object[]{starDate,endDate}) + "]");
		}
		return jdbcTemplate.queryForList(sql,starDate,endDate);
	}
	
	private void initAppointmentTeamRemainsData(Date date){
		String checkSql="select count(id) from appointment_team_remains where appointment_start>?";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + checkSql + "] args:[" + Arrays.asList(date) + "]");
		}
		int i=jdbcTemplate.queryForInt(checkSql,DateUtils.starDate(date));
		System.out.println(i);
		//还没有初始化的话就去模板表取出数据初始化
		if(i==0){
			//过滤放假的班组
			String getdayoff="select team_id from appointment_team_dayoff where dayoff_date>=? and dayoff_date<=?";
			List<Map<String,Object>> getdayoffTeam=jdbcTemplate.queryForList(getdayoff, DateUtils.starDate(date),DateUtils.endDate(date));
			List<String> dayoffTeamId=new ArrayList<String>();
			for(Map<String,Object> idMap:getdayoffTeam){
				String team_id=(String)idMap.get("team_id");
				dayoffTeamId.add(team_id);
			}
			
			String getTeamConfig="select team_id,start_mins_of_day,end_mins_of_day,capacity from appointment_team_capacity_cfg";
			List<Map<String,Object>> getTeamConfigDate=jdbcTemplate.queryForList(getTeamConfig);
			String insertAppointmentTeamRemains="insert into appointment_team_remains(id,team_id,appointment_start,appointment_end,hour_of_day,remains) values(?,?,?,?,?,?)";
			List<Object[]> params=new ArrayList<Object[]>();
			for(Map<String,Object> voconfig:getTeamConfigDate){
				String team_id=(String)voconfig.get("team_id");
				BigDecimal start_mins_of_day=(BigDecimal)voconfig.get("start_mins_of_day");
				BigDecimal end_mins_of_day=(BigDecimal)voconfig.get("end_mins_of_day");
				BigDecimal capacity=(BigDecimal)voconfig.get("capacity");
				if(dayoffTeamId.contains(team_id)){
					continue;
				}
				String id=UUID.randomUUID().toString();
				id=id.replace("-", "");
				Date appointment_start=DateUtils.minToDate(date,start_mins_of_day.intValue());
				Date appointment_end=DateUtils.minToDate(date,end_mins_of_day.intValue());
				int hour_of_day=(int) ((appointment_end.getTime()-appointment_start.getTime())/1000/60/60);
				hour_of_day=hour_of_day<0?0:hour_of_day;
				Object[] param=new Object[]{id,team_id,appointment_start,appointment_end,hour_of_day,capacity.intValue()};
				params.add(param);
			}
			jdbcTemplate.batchUpdate(insertAppointmentTeamRemains,params);
		}
	}
	
	public List<Map<String,Object>> getAppointmentTeam(Object... values){
		final String sql = "select appointment_team_remains.id,appointment_team_remains.team_id,appointment_team.name from appointment_team_remains join appointment_team on appointment_team.id=appointment_team_remains.team_id where appointment_team_remains.appointment_start=? and appointment_team_remains.remains>0";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(values) + "]");
		}
		return jdbcTemplate.queryForList(sql,values);
	}
	
//	public List<Map<String,Object>> getAppointmentConsultant(Date date){
//		//过滤放假的班组
//		String getdayoff="select consultant_id from appointment_consultant_dayoff where dayoff_date>=? and dayoff_date<=?";
//		List<Map<String,Object>> getdayoffconsultant=jdbcTemplate.queryForList(getdayoff, DateUtils.starDate(date),DateUtils.endDate(date));
//		List<String> dayoffconId=new ArrayList<String>();
//		for(Map<String,Object> idMap:getdayoffconsultant){
//			String team_id=(String)idMap.get("team_id");
//			dayoffconId.add(team_id);
//		}
		
//		String 
//		
//		final String sql = "select appointment_team_remains.id,appointment_team_remains.team_id,appointment_team.name from appointment_team_remains join appointment_team on appointment_team.id=appointment_team_remains.team_id where appointment_team_remains.appointment_start=? and appointment_team_remains.remains>0";
//		if (log.isTraceEnabled()) {
//			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(values) + "]");
//		}
//		return jdbcTemplate.queryForList(sql,values);
//	}
}
