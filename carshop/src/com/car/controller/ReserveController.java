package com.car.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.car.service.ReserveService;
import com.car.utils.Utils;

@Controller
@RequestMapping(value = "/reserve")
public class ReserveController {
	private static final Logger log = LoggerFactory.getLogger(ReserveController.class);
	private static final String viewFolder = "reserve/";
	
	@Resource(name = "reserveService")
	private ReserveService reserveService;
	
	public ReserveService getService() {
		return reserveService;
	}
	
	
	@RequestMapping(value = "/create")
	public String index(@RequestParam(value = "openid", required = true) String openid, ModelMap model) {
		String uid=Utils.getClientidByOpenid(openid);
		System.out.println(openid);
		//获取车辆数据
		List<Map<String,Object>> mycar=getService().getMyCar(uid);
		//获取4s店数据
		List<Map<String,String>> shop=getService().getShop(uid);
		model.addAttribute("openid", openid);
		model.addAttribute("mycar", mycar);
		model.addAttribute("shop", shop);
		return viewFolder.concat("reserve");
	}
	
	
	@RequestMapping(value = "/getSelectTime")
	public @ResponseBody List<Map<String,String>> getSelectTime(@RequestParam(value = "shopid", required = true) String shopid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getSelectTime(shopid);
		return selectTime;
	}
	
	@RequestMapping(value = "/getTeam")
	public @ResponseBody List<Map<String,String>> getTeam(@RequestParam(value = "shopid", required = true) String shopid,@RequestParam(value = "timeid", required = true) String timeid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getTeam(shopid,timeid);
		return selectTime;
	}
	@RequestMapping(value = "/getConsultant")
	public @ResponseBody List<Map<String,String>> getConsultant(@RequestParam(value = "shopid", required = true) String shopid,@RequestParam(value = "timeid", required = true) String timeid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getConsultant(shopid,timeid);
		return selectTime;
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody Map<String, String> create(
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "appointmentid", required = true) String appointmentid,
			@RequestParam(value = "carid", required = true) String carid,
			@RequestParam(value = "isOther", required = true) boolean isOther,
			@RequestParam(value = "otherCarNum", required = true) String otherCarNum,
			@RequestParam(value = "otherCarVin", required = true) String otherCarVin,
			@RequestParam(value = "timeid", required = true) String timeid,
			@RequestParam(value = "teamid", required = true) String teamid,
			@RequestParam(value = "consultantid", required = true) String consultantid,
			ModelMap model) {
		System.out.println("openid:"+openid);
		System.out.println("appointmentid:"+appointmentid);
		System.out.println("carid:"+carid);
		System.out.println("isOther:"+isOther);
		System.out.println("otherCarNum:"+otherCarNum);
		System.out.println("otherCarVin:"+otherCarVin);
		System.out.println("timeid:"+timeid);
		System.out.println("teamid:"+teamid);
		System.out.println("consultantid:"+consultantid);
		return this.getService().createAppointment(openid, appointmentid, carid, isOther, otherCarNum, otherCarVin, timeid, teamid, consultantid);
	}
	
	@RequestMapping(value = "/query")
	public String query(@RequestParam(value = "openid", required = true) String openid, ModelMap model) {
		System.out.println(openid);
		List<Map<String,Object>> list=this.getService().queryAppointment(openid);
		model.addAttribute("myappointment", list);
		model.addAttribute("openid", openid);
		return viewFolder.concat("query");
	}
	
	@RequestMapping(value = "/delete")
	public @ResponseBody Map<String, String> delete(
			@RequestParam(value = "aid", required = true) String aid,
			ModelMap model) {
		Map<String,String> result=new HashMap<String, String>();
		int r=this.getService().deleteAppointment(aid);
		if(r==1){
			result.put("code", "1");
		}else{
			result.put("code", "0");
		}
		return result;
	}
	
	@RequestMapping(value = "/rate")
	public String rate(@RequestParam(value = "openid", required = true) String openid, ModelMap model) {
		System.out.println(openid);
		List<Map<String,Object>> list=this.getService().rateAppointment(openid);
		model.addAttribute("overtimeAppointment", list);
		model.addAttribute("openid", openid);
		return viewFolder.concat("rate");
	}
	
	@RequestMapping(value = "/rating")
	public @ResponseBody Map<String, String> rating(
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "aid", required = true) String aid,
			@RequestParam(value = "tscore", required = true) int tscore,
			@RequestParam(value = "cscore", required = true) int cscore,
			ModelMap model) {
		Map<String,String> result=new HashMap<String, String>();
		int r=this.getService().ratingAppointment(openid,aid,tscore,cscore);
		if(r==1){
			result.put("code", "1");
		}else{
			result.put("code", "0");
		}
		return result;
	}
}
