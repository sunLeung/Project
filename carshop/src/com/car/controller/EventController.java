package com.car.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.car.service.EventService;

@Controller
@RequestMapping(value = "/event")
public class EventController {
	private static final String viewFolder = "event/";
	
	@Resource(name = "eventService")
	private EventService eventService;
	
	public EventService getService() {
		return eventService;
	}
	
	/**
	 * 打开活动主页
	 * @param openid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(@RequestParam(value = "openid", required = true) String openid,
			ModelMap model) {
		List<Map<String,Object>> events=this.getService().getEventsData(openid);
		model.addAttribute("events", events);
		model.addAttribute("openid", openid);
		return viewFolder.concat("events");
	}
	
	
	/**
	 * 参与活动
	 * @param openid
	 * @param eventid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/join")
	public @ResponseBody Map<String, String> join(
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "eventid", required = true) String eventid,
			ModelMap model) {
		Map<String,String> result=new HashMap<String, String>();
		int r=this.getService().joinEvent(openid,eventid);
		if(r==1){
			result.put("code", "1");
		}else{
			result.put("code", "0");
		}
		return result;
	}
}
