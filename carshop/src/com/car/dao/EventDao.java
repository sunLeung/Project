package com.car.dao;

import java.util.ArrayList;
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
	public List<Map<String, Object>> getActiveEventList(List<String> shopids) {
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>();
		if(shopids==null||shopids.size()==0){
			return result;
		}
		StringBuilder sql=new StringBuilder();
		sql.append("select id,name,signup_from,signup_to,title_img from maketing_event where own_no in (");
		Object[] params=new Object[shopids.size()+1];
		for(int i=0;i<shopids.size();i++){
			if(i==(shopids.size()-1)){
				sql.append("?)");
			}else{
				sql.append("?,");
			}
			params[i]=shopids.get(i);
		}
		sql.append(" and signup_from>?");
		params[shopids.size()]=new Date();
		return jdbcTemplate.queryForList(sql.toString(),params);
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
	
	/**
	 * 获取活动详细信息
	 * @param eventid
	 * @return
	 */
	public Map<String,Object> getEventDetail(String eventid){
		String sql="select description from maketing_event where id=?";
		return this.jdbcTemplate.queryForMap(sql,eventid);
	}
	
	/**
	 * 判断用户是否参与过改活动
	 * @param openid
	 * @param eventid
	 * @return
	 */
	public boolean isJoinThisEvent(String openid,String eventid){
		boolean result=false;
		String sql="select id from maketing_event_signup where event_id=? and open_id=?";
		List<String> list=this.jdbcTemplate.queryForList(sql,String.class,eventid,openid);
		if(list!=null&&list.size()>0){
			result=true;
		}
		return result;
	}
	
	/**
	 * 获取活动积分
	 * @param eventid
	 * @return
	 */
	public List<Integer> getEventScore(String eventid){
		String sql="select points_amount from maketing_event_points_config where event_id=?";
		List<Integer> list=this.jdbcTemplate.queryForList(sql,Integer.class,eventid);
		return list;
	}
}
