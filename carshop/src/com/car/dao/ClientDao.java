package com.car.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
	protected JdbcTemplate jdbcTemplate;
	@Resource
	public void setDataSource(@Qualifier("dataSource") DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 获取客户id
	 * @param openid
	 * @return
	 */
	public String getClientid(String openid) {
		String sql = "select client_id from wechat_identity_correlation where open_id=? group by client_id";
		String clienid="";
		List<String> list=jdbcTemplate.queryForList(sql, String.class,openid);
		if(list.size()>0){
			clienid=list.get(0);
		}
		return clienid;
	}
	
	/**
	 * 获取客户绑定的车
	 * @param openid
	 * @return
	 */
	public List<String> getVehicles(String openid) {
		String sql = "select vehicle_id from wechat_identity_correlation where open_id=? and status=1";
		return jdbcTemplate.queryForList(sql,String.class,openid);
	}
	
	/**
	 * 获取客户关联的4S店
	 * @param openid
	 * @return
	 */
	public List<String> getClientOwnShop(String openid){
		String sql="select own_no from wechat_identity_correlation a left join vehicle b on a.vehicle_id=b.id where a.open_id=? and a.status=1 group by own_no";
		return jdbcTemplate.queryForList(sql,String.class,openid);
	}
}
