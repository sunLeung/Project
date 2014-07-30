package common.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.JsonUtils;
import utils.ReqUtils;
import utils.RespUtils;
import utils.StringUtils;

import common.config.App;
import common.config.AppContent;
import common.config.PlatformUser;
import common.config.PlatformUserContent;
import common.config.UnionContent;
import common.json.JSONObject;
import common.logger.Logger;
import common.logger.LoggerManger;

public class AdminService {
	private static Logger log=LoggerManger.getLogger();
	
	public static void getAppsInfo(HttpServletRequest res,HttpServletResponse resp){
		try {
			HttpSession session = res.getSession();
			PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
			Map<Integer,App> tempContent=new HashMap<Integer,App>();
			for(Integer id:user.appidArray()){
				App app=AppContent.appContent.get(id);
				if(app!=null)
					tempContent.put(id, app);
			}
			RespUtils.jsonResp(resp, tempContent,"application/json;charset=UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	
	public static void getUnionsInfo(HttpServletRequest res,HttpServletResponse resp){
		RespUtils.jsonResp(resp, UnionContent.unionContent,"application/json;charset=UTF-8");
	}
	
	public static void createApp(HttpServletRequest res,HttpServletResponse resp){
		try {
			String postContent=ReqUtils.getPostString(res);
			if(StringUtils.isNotBlank(postContent)){
				int appid=AppContent.topAppID.incrementAndGet();
				App app=(App)JsonUtils.objectFromJson(postContent, App.class);
				app.setAppid(appid);
				app.initMapContent();
				AppContent.appContent.put(appid, app);
				AppContent.flush();
				
				//关联操作用户
				HttpSession session = res.getSession();
				PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
				PlatformUser temp = PlatformUserContent.platformUserContent.get(user.getName());
				temp.addApp(appid);
				PlatformUserContent.flushUserContent();
				
				RespUtils.commonResp(resp, RespUtils.CODE.SUCCESS);
				log.info(user.getName() + " create app [appname"+ app.getAppname()+" appid="+app.getAppid()+"] succeed");
			}else{
				RespUtils.commonResp(resp, RespUtils.CODE.FAIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	
	public static void updateApp(HttpServletRequest res,HttpServletResponse resp){
		try {
			String postContent=ReqUtils.getPostString(res);
			if(StringUtils.isNotBlank(postContent)){
				App app=(App)JsonUtils.objectFromJson(postContent, App.class);
				app.initMapContent();
				
				App oldApp=AppContent.appContent.get(app.getAppid());
				if(oldApp!=null){
					AppContent.appContent.put(app.getAppid(), app);
					AppContent.flush();
					
					HttpSession session = res.getSession();
					PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
					RespUtils.commonResp(resp, RespUtils.CODE.SUCCESS);
					log.info(user.getName()+" update app [appname"+ app.getAppname()+" appid="+app.getAppid()+"] succeed");
					return;
				}
			}
			RespUtils.commonResp(resp, RespUtils.CODE.FAIL);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	public static void deleteAppById(HttpServletRequest res,HttpServletResponse resp){
		try {
			String postContent=ReqUtils.getPostString(res);
			if(StringUtils.isNotBlank(postContent)){
				int appid=Integer.valueOf(postContent);
				App oldApp=AppContent.appContent.get(appid);
				if(oldApp!=null){
					AppContent.appContent.remove(appid);
					AppContent.flush();
					
					for(Entry<String,PlatformUser> entry:PlatformUserContent.platformUserContent.entrySet()){
						entry.getValue().deleteApp(appid);
					}
					PlatformUserContent.flushUserContent();
					HttpSession session = res.getSession();
					PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
					RespUtils.commonResp(resp, RespUtils.CODE.SUCCESS);
					log.info(user.getName()+" delete app [appname"+ oldApp.getAppname()+" appid="+oldApp.getAppid()+"] succeed");
					return;
				}
			}
			RespUtils.commonResp(resp, RespUtils.CODE.FAIL);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	
	public static void login(HttpServletRequest res,HttpServletResponse resp){
		try {
			String postContent=ReqUtils.getPostString(res);
			if(StringUtils.isNotBlank(postContent)){
				JSONObject json=new JSONObject(postContent);
				String username=json.getString("username").trim();
				String password=json.getString("password").trim();
				PlatformUser user=PlatformUserContent.platformUserContent.get(username);
				if(user!=null&&user.getPassword().equals(password)){
					HttpSession session=res.getSession();
					session.setAttribute("loginUser", user);
					RespUtils.commonResp(resp, RespUtils.CODE.SUCCESS);
					return;
				}
			}
			RespUtils.commonResp(resp, RespUtils.CODE.FAIL);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	
	public static void logout(HttpServletRequest res,HttpServletResponse resp){
		try {
			HttpSession session=res.getSession();
			session.setAttribute("loginUser", null);
			RespUtils.commonResp(resp, RespUtils.CODE.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
	
	public static void loadUser(HttpServletRequest res, HttpServletResponse resp) {
		try {
			JSONObject json = new JSONObject();
			HttpSession session = res.getSession();
			PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
			if (user != null) {
				PlatformUser freshUser = PlatformUserContent.platformUserContent.get(user.getName());
				if(freshUser!=null){
					session.setAttribute("loginUser", freshUser);
					json.put("isLogin", true);
					json.put("username", freshUser.getName());
					json.put("auth", freshUser.getAuth());
				}
			} else {
				json.put("isLogin", false);
			}
			RespUtils.stringResp(resp, json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			RespUtils.commonResp(resp, RespUtils.CODE.EXCEPTION);
		}
	}
}
