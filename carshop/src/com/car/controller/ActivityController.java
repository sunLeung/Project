package com.car.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.car.service.ActivityService;
import com.car.utils.UserUtils;

@Controller
@RequestMapping(value = "/activity")
public class ActivityController {
	private static final Logger log = LoggerFactory.getLogger(ActivityController.class);
	private static final String viewFolder = "activity/";
	
	@Resource(name = "activityService")
	private ActivityService activityService;
	
	public ActivityService getService() {
		return activityService;
	}
	
	@RequestMapping(value = "/index")
	public String index(@RequestParam(value = "openid", required = true) String openid,
			ModelMap model) {
		String uid=UserUtils.getUserIdByOpenId(openid);
		System.out.println(openid);
		List<Map<String,Object>> acts=this.getService().getAct(openid);
		model.addAttribute("acts", acts);
		model.addAttribute("openid", openid);
		return viewFolder.concat("activity");
	}
	
	@RequestMapping(value = "/join")
	public @ResponseBody Map<String, String> join(
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "actid", required = true) String actid,
			ModelMap model) {
		Map<String,String> result=new HashMap<String, String>();
		int r=this.getService().join(openid,actid);
		if(r==1){
			result.put("code", "1");
		}else{
			result.put("code", "0");
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().replace("-", ""));
	}
}
