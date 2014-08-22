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

import com.car.service.ReserveService;

@Controller
@RequestMapping(value = "/reserve")
public class ReserveController {
	private static final String viewFolder = "reserve/";
	
	@Resource(name = "reserveService")
	private ReserveService reserveService;
	
	public ReserveService getService() {
		return reserveService;
	}
	
	@RequestMapping(value = "/create")
	public String index(@RequestParam(value = "openid", required = true) String openid, ModelMap model) {
		//获取车辆数据
		List<Map<String,Object>> mycar=getService().getClientCarsInfo(openid);
		//获取车辆厂商数据
		List<Map<String,Object>> carBrands=getService().getCarBrandInfo();
		//获取4s店数据
		List<Map<String,Object>> shop=getService().getShopsInfo(openid);
		model.addAttribute("openid", openid);
		model.addAttribute("mycar", mycar);
		model.addAttribute("carBrands", carBrands);
		model.addAttribute("shop", shop);
		return viewFolder.concat("reserve");
	}
	
	
	@RequestMapping(value = "/getSelectTime")
	public @ResponseBody List<Map<String,String>> getSelectTime(@RequestParam(value = "shopid", required = true) String shopid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getAppointmentTimeByShop(shopid);
		return selectTime;
	}
	
	@RequestMapping(value = "/getTeam")
	public @ResponseBody List<Map<String,String>> getTeam(@RequestParam(value = "shopid", required = true) String shopid,@RequestParam(value = "timeid", required = true) String timeid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getTeamByShopAndTime(shopid,timeid);
		return selectTime;
	}
	@RequestMapping(value = "/getConsultant")
	public @ResponseBody List<Map<String,String>> getConsultant(@RequestParam(value = "shopid", required = true) String shopid,@RequestParam(value = "timeid", required = true) String timeid, ModelMap model) {
		//获取车辆数据
		List<Map<String,String>> selectTime=getService().getConsultantByShopAndTime(shopid,timeid);
		return selectTime;
	}
	
	@RequestMapping(value = "/save")
	public @ResponseBody Map<String, String> create(
			@RequestParam(value = "openid", required = true) String openid,
			@RequestParam(value = "model", required = true) String model,
			@RequestParam(value = "modelCode", required = true) String modelCode,
			@RequestParam(value = "carNum", required = true) String carNum,
			@RequestParam(value = "carVin", required = true) String carVin,
			@RequestParam(value = "shopid", required = true) String shopid,
			@RequestParam(value = "timeid", required = true) String timeid,
			@RequestParam(value = "teamid", required = true) String teamid,
			@RequestParam(value = "consultantid", required = true) String consultantid,
			@RequestParam(value = "appointmentid", required = true) String appointmentid) {
		System.out.println("openid:"+openid);
		System.out.println("model:"+model);
		System.out.println("modelCode:"+modelCode);
		System.out.println("carNum:"+carNum);
		System.out.println("carVin:"+carVin);
		System.out.println("shopid:"+shopid);
		System.out.println("timeid:"+timeid);
		System.out.println("teamid:"+teamid);
		System.out.println("consultantid:"+consultantid);
		System.out.println("appointmentid:"+appointmentid);
		return this.getService().createAppointment(openid, model, modelCode, carNum, carVin, shopid,timeid, teamid, consultantid,appointmentid);
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
		List<Map<String,Object>> list=this.getService().getCanRateAppointment(openid);
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
	
	@RequestMapping(value = "/getShopDetail")
	public @ResponseBody Map<String, Object> getShopDetail(
			@RequestParam(value = "shopid", required = true) String shopid) {
		return this.getService().getShopDetail(shopid);
	}
	
	/**
	 * 获取车辆信息
	 * @param mycarid
	 * @return
	 */
	@RequestMapping(value = "/getMycarInfo")
	public @ResponseBody Map<String, Object> getMycarInfo(
			@RequestParam(value = "mycarid", required = true) String mycarid) {
		return this.getService().getMycarInfo(mycarid);
	}
	
	/**
	 * 获取车系数据
	 * @param carBrandid
	 * @return
	 */
	@RequestMapping(value = "/getCarSeries")
	public @ResponseBody List<Map<String, Object>> getCarSeries(
			@RequestParam(value = "carBrandid", required = true) String carBrandid) {
		return this.getService().getCarSeries(carBrandid);
	}
	
	/**
	 * 获取车型数据
	 * @param carBrandid
	 * @return
	 */
	@RequestMapping(value = "/getCarModel")
	public @ResponseBody List<Map<String, Object>> getCarModel(
			@RequestParam(value = "carSeriesid", required = true) String carSeriesid) {
		return this.getService().getCarModel(carSeriesid);
	}
}
