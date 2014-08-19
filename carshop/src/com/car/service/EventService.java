package com.car.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.car.dao.ClientDao;
import com.car.dao.EventDao;

@Service
public class EventService {
	
	@Resource(name = "eventDao")
	private EventDao evetnDao;

	protected EventDao getEventDAO() {
		return evetnDao;
	}
	
	@Resource(name = "clientDao")
	private ClientDao clientDao;
	
	protected ClientDao getClientDao() {
		return clientDao;
	}
	
	/**
	 * 获取活动页面显示数据
	 * @param openid
	 * @return
	 */
	public List<Map<String,Object>> getEventsData(String openid){
		String clientid=this.getClientDao().getClientid(openid);
		//获取4s店ID
		List<String> shopids=this.getClientDao().getClientOwnShop(openid);
		//获取4s店进行中的活动列表
		List<Map<String,Object>> shopEvents=getEventDAO().getActiveEventList(shopids);
		//获取客户参与的活动列表
		List<String> clientEvents=getEventDAO().getClientJoinedEvent(clientid);
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
		String clientid=this.getClientDao().getClientid(openid);
		return this.getEventDAO().joinEvent(clientid,openid,eventid);
	}
	
	/**
	 * 获取活动信息
	 * @param eventid
	 * @return
	 */
	public Map<String,Object> getEventDetail(String eventid){
		return this.getEventDAO().getEventDetail(eventid);
	}
	
	/**
	 * 用户完成活动
	 * @param openid
	 * @param eventid
	 * @return
	 */
	public Map<String,Object> finishEvent(String openid,String eventid){
		Map<String,Object> result=new HashMap<String, Object>();
		//判断用户是否之前已经报名
		boolean isJoin=this.getEventDAO().isJoinThisEvent(openid, eventid);
		if(!isJoin){
			result.put("code", 1);
			result.put("msg", "该用户没有参与该活动，加分失败。");
			return result;
		}
		//获取该活动的积分
		List<Integer> score=this.getEventDAO().getEventScore(eventid);
		if(score==null||score.size()==0){
			result.put("code", 2);
			result.put("msg", "系统没有找到该活动积分配置，加分失败。");
			return result;
		}
		int s=score.get(0);
		
		// TODO:调用外部接口给客户加积分
		result.put("code", 0);
		result.put("msg", "已加："+s+"积分。");
		return result;
	}
	
	
}
