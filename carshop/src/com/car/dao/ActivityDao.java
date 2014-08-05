package com.car.dao;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityDao {
	private Logger log = LoggerFactory.getLogger(ActivityDao.class);
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 获取所有未开始的活动
	 * @param values
	 * @return
	 */
	public List<Map<String,Object>> getAct(String shopid,Date date){
		final String sql = "select id,name,signup_from,signup_to,description from maketing_event where branch_id='"+shopid+"' and signup_from>?";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(new Object[]{shopid,date}) + "]");
		}
		return jdbcTemplate.queryForList(sql,date);
	}
	
	/**
	 * 获取用户参与过的活动
	 * @param userid
	 * @return
	 */
	public List<Map<String,Object>> getMyAct(String userid){
		final String sql = "select event_id from maketing_event_signup where client_id='"+userid+"'";
		if (log.isTraceEnabled()) {
			log.trace("sql:[" + sql + "] args:[" + Arrays.asList(userid) + "]");
		}
		return jdbcTemplate.queryForList(sql);
	}
	
	public int insertJoin(String uid,String openid,String actid){
		String sql="insert into maketing_event_signup(id,event_id,client_id,open_id,signup_date) values(?,?,?,?,?)";
		String id=UUID.randomUUID().toString().replace("-", "");
		return this.jdbcTemplate.update(sql,id,actid,uid,openid,new Date());
	}
}
