package com.car.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.car.dao.EventDao;
import com.car.utils.Utils;

@Service
public class EventService {
	
	@Resource(name = "eventDao")
	private EventDao dao;

	protected EventDao getDAO() {
		return dao;
	}
	
	/**
	 * 获取活动页面显示数据
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getEventsData(String openid){
		//获取客户ID
		String clientid=Utils.getClientidByOpenid(openid);
		//获取4s店ID
		String shopid=Utils.getShopidByClientid(clientid);
		//获取4s店进行中的活动列表
		List<Map<String,Object>> shopEvents=getDAO().getActiveEventList(shopid);
		//获取客户参与的活动列表
		List<String> clientEvents=getDAO().getClientJoinedEvent(clientid);
		//过滤活动列表分析客户活动参与情况
		for(Map<String,Object> m:shopEvents){
			String eventid=(String)m.get("id");
			if(clientEvents.contains(eventid)){
				m.put("isjoin", true);
			}else{
				m.put("isjoin", false);
			}
		}
		return shopEvents;
	}
	
	/**
	 * 客户参与活动
	 * @param openid
	 * @param eventid
	 * @return
	 */
	public int joinEvent(String openid,String eventid){
		String clientid=Utils.getClientidByOpenid(openid);
		return this.getDAO().joinEvent(clientid,openid,eventid);
	}
}
