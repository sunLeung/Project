package com.car.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.car.utils.Utils;

@Repository
public class EventDao {
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 获取某4s店的可报名活动列表
	 * @param values
	 * @return
	 */
	public List<Map<String, Object>> getActiveEventList(String shopid) {
		String sql = "select id,name,signup_from,signup_to,description from maketing_event where branch_id=? and signup_from>?";
		return jdbcTemplate.queryForList(sql, shopid,new Date());
	}
	
	/**
	 * 获取客户参与的活动列表
	 * @param clientid
	 * @return
	 */
	public List<String> getClientJoinedEvent(String clientid){
		String sql = "select event_id from maketing_event_signup where client_id=?";
		return jdbcTemplate.queryForList(sql,String.class,clientid);
	}
	
	/**
	 * 客户参与活动
	 * @param clientid
	 * @param openid
	 * @param eventid
	 * @return
	 */
	public int joinEvent(String clientid,String openid,String eventid){
		String sql="insert into maketing_event_signup(id,event_id,client_id,open_id,signup_date) values(?,?,?,?,?)";
		return this.jdbcTemplate.update(sql,Utils.createUUID(),eventid,clientid,openid,new Date());
	}
}
