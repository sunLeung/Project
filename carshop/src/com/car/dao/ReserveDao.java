package com.car.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.util.StringUtils;

import com.car.utils.DateUtils;
import com.car.utils.UserUtils;

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
		
		Date starDate=new Date();
		Date endDate=DateUtils.endDate(new Date(System.currentTimeMillis()+24*60*60*1000));
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
	
	/**
	 * 获取顾问
	 * @param date
	 * @param shopid
	 * @return
	 */
	public List<Map<String,String>> getAppointmentConsultant(Date date,String shopid){
		//获取放假的顾问
		String getdayoff="select consultant_id from appointment_consultant_dayoff where dayoff_date>=? and dayoff_date<=?";
		List<Map<String,Object>> getdayoffconsultant=jdbcTemplate.queryForList(getdayoff, DateUtils.starDate(date),DateUtils.endDate(date));
		List<String> dayoffconId=new ArrayList<String>();
		for(Map<String,Object> idMap:getdayoffconsultant){
			String consultantId=(String)idMap.get("consultant_id");
			dayoffconId.add(consultantId);
		}
		
		String getConsultantSql="select id,name from appointment_consultant where branch_id='"+shopid+"'";
		List<Map<String,Object>> consultantList=jdbcTemplate.queryForList(getConsultantSql);
		List<Map<String,String>> result=new ArrayList<Map<String,String>>();
		//过滤放假的顾问
		for(Map<String,Object> map:consultantList){
			String cid=(String)map.get("id");
			String cname=(String)map.get("name");
			if(dayoffconId.contains(cid)){
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
			String getCarSql="select vin,register_no from vehicle where id=?";
			List<Map<String,Object>> carinfo=this.jdbcTemplate.queryForList(getCarSql,carid);
			if(carinfo.size()==0){
				result.put("code", "3");
				result.put("msg", "预约失败，没有找到该车信息，请手动填写。");
				return result;
			}else{
				Map<String,Object> car=carinfo.get(0);
				otherCarNum=(String)car.get("register_no");
				otherCarVin=(String)car.get("vin");
			}
			//再次检查是否还有预约名额
			String checkRemains="select remains from appointment_team_remains where id=?";
			int remain=this.jdbcTemplate.queryForInt(checkRemains,appointmentid);
			if(remain<=0){
				result.put("code", "4");
				result.put("msg", "预约失败，已经没有预约名额。请选择其他预约。");
				return result;
			}
			
			String insertAppointment="insert into appointment_detail(id,team_id,consultant_id,client_id,appointment_day,vin,register_no,status) values(?,?,?,?,?,?,?,?)";
			String id=UUID.randomUUID().toString().replace("-", "");
			String clientid=UserUtils.getUserIdByOpenId(openid);
			int r=this.jdbcTemplate.update(insertAppointment,id,teamid,consultantid,clientid,d,otherCarVin,otherCarNum,1);
			if(r>0){
				String updateRemains="update appointment_team_remains set remains=? where id=?";
				this.jdbcTemplate.update(updateRemains,remain-1,appointmentid);
				result.put("code", "1");
				result.put("msg", "预约成功。");
				return result;
			}
		}
		result.put("code", "5");
		result.put("msg", "预约失败。");
		return result;
	}
	
	public List<Map<String,Object>> getAppointment(String uid){
		String sql="select * from appointment_detail where status=1 and client_id='"+uid+"'";
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public int deleteAppointment(String aid){
		String sql="update appointment_detail set status=? where id=?";
		return this.jdbcTemplate.update(sql,2,aid);
	}
	
	public List<Map<String,Object>> getOvertimeAppointment(String uid){
		String sql="select * from appointment_detail where status=1 and client_id='"+uid+"' and appointment_day<?";
		return this.jdbcTemplate.queryForList(sql,new Date());
	}
	
	public int ratingAppointment(String uid,String aid, int tscore, int cscore){
		String sql="update appointment_detail set status=? where id=?";
		String sql1="insert into appointment_rating(id,appointment_detail_id,team_grade,consultant_grade,client_id) values(?,?,?,?,?)";
		String id=UUID.randomUUID().toString().replace("-", "");
		int i= this.jdbcTemplate.update(sql1,id,aid,tscore,cscore,uid);
		int result=0;
		if(i==1){
			result=this.jdbcTemplate.update(sql,3,aid);
		}
		return result;
	}
}
