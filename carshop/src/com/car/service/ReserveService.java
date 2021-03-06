package com.car.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.car.dao.ClientDao;
import com.car.dao.ReserveDao;
import com.car.utils.Utils;

@Service
public class ReserveService {

	@Resource(name = "reserveDao")
	private ReserveDao reserveDao;

	protected ReserveDao getReserveDAO() {
		return reserveDao;
	}

	@Resource(name = "clientDao")
	private ClientDao clientDao;

	protected ClientDao getClientDao() {
		return clientDao;
	}

	/**
	 * 获取用户自己的车辆数据
	 * 
	 * @param clientid
	 * @return
	 */
	public List<Map<String, Object>> getClientCarsInfo(String openid) {
		List<Map<String, Object>> result = getReserveDAO().getClientCar(openid);
		return result;
	}

	private Comparator<Map<String, Object>> compartor = new Comparator<Map<String, Object>>() {

		@Override
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			String a = (String) o1.get("type");
			String b = (String) o2.get("type");
			return a.compareTo(b);
		}
	};

	/**
	 * 获取厂商数据并按照数字英文字母排序
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getCarBrandInfo() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> srcData = getReserveDAO().getCarBrandInfo();
		;
		for (Map<String, Object> map : srcData) {
			String name = (String) map.get("name");
			StringBuilder sb = new StringBuilder();
			if (name != null && name.length() > 0) {
				for (int i = 0; i < name.length(); i++) {
					String temp = Utils.convert(name.toCharArray()[i] + "");
					sb.append(temp);
				}
				map.put("type", sb.toString());
				result.add(map);
			}
		}
		// 排序
		Collections.sort(result, this.compartor);
		return result;
	}

	/**
	 * 获取4s店数据
	 * 
	 * @param openid
	 * @param clientid
	 * @return
	 */
	public List<Map<String, Object>> getShopsInfo(String openid) {
		String clientid = this.getClientDao().getClientid(openid);
		// 获取最近预约过的4s店
		String recentShopid = this.reserveDao
				.getRecentAppointmentShopid(clientid);
		// 购车的4s店
		List<String> buyCarShops = this.reserveDao.getBuyCarShopid(openid);
		// 所有的4s店
		List<Map<String, Object>> shopInfo = this.reserveDao.getAllShopInfo();
		// 排序结果返回数据
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if (recentShopid != null && recentShopid != "") {
			for (Map<String, Object> map : shopInfo) {
				String shopid = (String) map.get("own_no");
				if (recentShopid.equals(shopid)) {
					result.add(map);
					break;
				}
			}
		}

		if (buyCarShops != null && buyCarShops.size() > 0) {
			for (String sid : buyCarShops) {
				for (Map<String, Object> map : shopInfo) {
					String shopid = (String) map.get("own_no");
					if (sid.equals(shopid) && !result.contains(map)) {
						result.add(map);
						break;
					}
				}
			}
		}

		for (Map<String, Object> map : shopInfo) {
			if (result.contains(map)) {
				continue;
			} else {
				String name = (String) map.get("print_title");
				if (name != null && name != "")
					result.add(map);
			}
		}
		return result;
	}

	/**
	 * 通过店id获取可预约时间
	 * 
	 * @param shopid
	 * @return
	 */
	public List<Map<String, String>> getAppointmentTimeByShop(String shopid) {
		// 获取该4s店所有班组
		List<String> teamids = this.getReserveDAO().getShopTeam(shopid);

		// 获取可预约时间
		List<Map<String, Object>> appointmentTime = this.getReserveDAO()
				.getAppointmentTime();

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

		// 过滤重复时间点和4s的班组
		List<Date> distinct = new ArrayList<Date>();
		for (Map<String, Object> map : appointmentTime) {
			String tid = (String) map.get("team_id");
			Date dateBegin = (Date) map.get("appointment_start");
			if (teamids.contains(tid) && !distinct.contains(dateBegin)) {
				Map<String, String> temp = new HashMap<String, String>();
				Date dateEnd = (Date) map.get("appointment_end");
				temp.put("id",
						sdf.format(dateBegin) + "-" + sdf.format(dateEnd));
				temp.put("begin", sdf.format(dateBegin));
				temp.put("end", sdf.format(dateEnd));
				result.add(temp);
				distinct.add(dateBegin);
			}
		}
		if (result.size() == 0) {
			Map<String, String> temp = new HashMap<String, String>();
			temp.put("id", "other");
			temp.put("time", "预约已满");
			result.add(temp);
		}
		return result;
	}

	/**
	 * 获取4s店的某时间点的可预约班组
	 * 
	 * @param shopid
	 * @param time
	 * @return
	 */
	public List<Map<String, String>> getTeamByShopAndTime(String shopid,
			String time) {
		// 获取4s店所有班组
		List<String> teamids = this.getReserveDAO().getShopTeam(shopid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date d = null;
		try {
			d = sdf.parse(time.split("-")[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 获取某时刻的可预约班组
		List<Map<String, Object>> appointmentTeamByTime = this.getReserveDAO()
				.getAppointmentTeam(d);
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (Map<String, Object> map : appointmentTeamByTime) {
			String id = (String) map.get("id");
			String team_id = (String) map.get("team_id");
			String name = (String) map.get("name");
			if (teamids.contains(team_id)) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("id", id);
				m.put("tid", team_id);
				m.put("name", name);
				result.add(m);
			}
		}
		if (result.size() <= 0) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("id", "other");
			m.put("tid", "");
			m.put("name", "班组已预约满");
			result.add(m);
		}
		return result;
	}

	/**
	 * 获取4s店的某时间点的可预约顾问
	 * 
	 * @param shopid
	 * @param time
	 * @return
	 */
	public List<Map<String, String>> getConsultantByShopAndTime(String shopid,
			String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date d = null;
		try {
			d = sdf.parse(time.split("-")[0]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String, String>> result = this.getReserveDAO()
				.getAppointmentConsultant(d, shopid);
		if (result.size() <= 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", "other");
			map.put("name", "没有顾问");
			result.add(map);
		}
		return result;
	}

	/**
	 * 创建预约
	 * @param openid
	 * @param model
	 * @param modelCode
	 * @param carNum
	 * @param carVin
	 * @param shopid
	 * @param timeid
	 * @param teamid
	 * @param consultantid
	 * @param appointmentid
	 * @return
	 */
	public Map<String, String> createAppointment(String openid, String model,
			String modelCode, String carNum, String carVin, String shopid,
			String timeid, String teamid, String consultantid,
			String appointmentid) {
		Map<String, String> result = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Date ds = null;
		Date de = null;
		try {
			String[] dates = timeid.split("-");
			ds = sdf.parse(dates[0]);
			de = sdf.parse(dates[1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (ds.getTime() < System.currentTimeMillis()) {
			result.put("code", "2");
			result.put("msg", "预约失败，预约时间已过期。");
			return result;
		}
		if (model == null || "".equals(model)) {
			result.put("code", "3");
			result.put("msg", "车型不能为空");
			return result;
		}
		if ((carNum == null || "".equals(carNum))
				&& (carVin == null || "".equals(carVin))) {
			result.put("code", "4");
			result.put("msg", "车牌和车架号最少填写其中之一");
			return result;
		}

		// 再次检查是否还有预约名额
		int remain = this.reserveDao.getAppointmentRemain(appointmentid);
		if (remain <= 0) {
			result.put("code", "5");
			result.put("msg", "预约失败，已经没有预约名额。请选择其他预约。");
			return result;
		}
		String clientid = this.getClientDao().getClientid(openid);
		int r = this.reserveDao.createAppointment(appointmentid,teamid,consultantid,clientid,carVin,carNum, ds, de,modelCode,model,shopid);
		if (r > 0) {
			result.put("code", "1");
			result.put("msg", "预约成功。");
			return result;
		}
		result.put("code", "6");
		result.put("msg", "预约失败，很不幸给别人抢先预约了，预约名额已满。");
		return result;
	}

	/**
	 * 查询预约
	 * 
	 * @param openid
	 * @return
	 */
	public List<Map<String, Object>> queryAppointment(String openid) {
		String clientid = this.getClientDao().getClientid(openid);
		return this.getReserveDAO().getClientAppointments(clientid);
	}

	/**
	 * 取消预约
	 * 
	 * @param appointmentid
	 * @return
	 */
	public int deleteAppointment(String appointmentid) {
		return this.getReserveDAO().deleteAppointment(appointmentid);
	}

	/**
	 * 获取可评价预约
	 * 
	 * @param openid
	 * @return
	 */
	public List<Map<String, Object>> getCanRateAppointment(String openid) {
		String clientid = this.getClientDao().getClientid(openid);
		return this.getReserveDAO().getOvertimeAppointment(clientid);
	}

	/**
	 * 预约评价打分
	 * 
	 * @param openid
	 * @param appointmentid
	 * @param tscore
	 * @param cscore
	 * @return
	 */
	public int ratingAppointment(String openid, String appointmentid,
			int tscore, int cscore) {
		String clientid = this.getClientDao().getClientid(openid);
		return this.getReserveDAO().ratingAppointment(clientid, appointmentid,
				tscore, cscore);
	}

	/**
	 * 获取4s店信息
	 * 
	 * @param shopid
	 * @return
	 */
	public Map<String, Object> getShopDetail(String shopid) {
		return this.getReserveDAO().getShopDetail(shopid);
	}

	/**
	 * 获取车辆信息
	 * 
	 * @param mycarid
	 * @return
	 */
	public Map<String, Object> getMycarInfo(String mycarid) {
		return this.getReserveDAO().getMycarInfo(mycarid);
	}

	/**
	 * 获取厂商车系
	 * 
	 * @param carBrandid
	 * @return
	 */
	public List<Map<String, Object>> getCarSeries(String carBrandid) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> srcData = getReserveDAO().getCarSeries(
				carBrandid);
		;
		for (Map<String, Object> map : srcData) {
			String name = (String) map.get("name");
			StringBuilder sb = new StringBuilder();
			if (name != null && name.length() > 0) {
				for (int i = 0; i < name.length(); i++) {
					String temp = Utils.convert(name.toCharArray()[i] + "");
					sb.append(temp);
				}
				map.put("type", sb.toString());
				result.add(map);
			}
		}
		// 排序
		Collections.sort(result, this.compartor);
		return result;
	}

	/**
	 * 获取车型数据
	 * 
	 * @param carSeriesid
	 * @return
	 */
	public List<Map<String, Object>> getCarModel(String carSeriesid) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> srcData = getReserveDAO().getCarModel(
				carSeriesid);
		;
		for (Map<String, Object> map : srcData) {
			String name = (String) map.get("model");
			StringBuilder sb = new StringBuilder();
			if (name != null && name.length() > 0) {
				for (int i = 0; i < name.length(); i++) {
					String temp = Utils.convert(name.toCharArray()[i] + "");
					sb.append(temp);
				}
				map.put("type", sb.toString());
				result.add(map);
			}
		}
		// 排序
		Collections.sort(result, this.compartor);
		return result;
	}
}
