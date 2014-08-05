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

import com.car.dao.ActivityDao;
import com.car.utils.UserUtils;

@Service
public class ActivityService {
	
	@Resource(name = "activityDao")
	private ActivityDao dao;

	protected ActivityDao getDAO() {
		return dao;
	}
	
	/**
	 * 获取活动数据
	 * @param uid
	 * @return
	 */
	public List<Map<String,Object>> getAct(String openid){
		String uid=UserUtils.getUserIdByOpenId(openid);
		String shopid=UserUtils.getUserShopid(uid);
		List<Map<String,Object>> result=getDAO().getAct(shopid,new Date());
		List<Map<String,Object>> myAct=getDAO().getMyAct(uid);
		List<String> myActList=new ArrayList<String>();
		for(Map<String,Object> m:myAct){
			String actid=(String)m.get("event_id");
			myActList.add(actid);
		}
		for(Map<String,Object> m:result){
			String actid=(String)m.get("id");
			if(myActList.contains(actid)){
				m.put("isjoin", true);
			}else{
				m.put("isjoin", false);
			}
		}
		return result;
	}
	
	public int join(String openid,String actid){
		String uid=UserUtils.getUserIdByOpenId(openid);
		return this.getDAO().insertJoin(uid,openid,actid);
	}
}
