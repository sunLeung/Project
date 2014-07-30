package common.config;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.FileUtils;
import utils.JsonUtils;
import common.logger.Logger;
import common.logger.LoggerManger;

public class PlatformUserContent {
	private static Logger log=LoggerManger.getLogger();
	public static Map<String,PlatformUser> platformUserContent=new ConcurrentHashMap<String,PlatformUser>();
	private static long platformUserLastModify=0;
	
	public static Map<String,AuthMap> authContent=new ConcurrentHashMap<String, AuthMap>();
	private static long authMapLastModify=0;
	
	public static void initUserContent(){
		try {
			log.info("Star init userContent json data.");
			String filePath=Config.CONFIG_DIR + File.separator + "platformUser.json";
			String jsonSrc=FileUtils.readFileToJSONString(filePath);
			PlatformUser[] list=(PlatformUser[])JsonUtils.objectFromJson(jsonSrc, PlatformUser[].class);
			Map<String,PlatformUser> tempContent=new ConcurrentHashMap<String,PlatformUser>();
			for(PlatformUser u:list){
				if(tempContent.containsKey(u.getName())){
					throw new IllegalArgumentException("Two the same username:"+u.getName());
				}
				u.initAuthArray();
				u.initAppidArray();
				tempContent.put(u.getName(), u);
			}
			platformUserContent=tempContent;
			log.info("Init userContent json data complete.");
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void flushUserContent(){
		log.info("Star flush userContent json data.");
		try {
			String filePath=Config.CONFIG_DIR + File.separator + "platformUser.json";
			Object[] apps=(Object[])platformUserContent.values().toArray();
			String json=JsonUtils.jsonFromObject(apps);
			FileUtils.writeStringToFile(filePath, json);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		log.info("Flush userContent json data completed.");
	}
	
	public static void initAuthContent(){
		try {
			log.info("Star init authContent json data.");
			String filePath=Config.CONFIG_DIR + File.separator + "authMap.json";
			String jsonSrc=FileUtils.readFileToJSONString(filePath);
			AuthMap[] list=(AuthMap[])JsonUtils.objectFromJson(jsonSrc, AuthMap[].class);
			Map<String,AuthMap> tempContent=new ConcurrentHashMap<String, AuthMap>();
			for(AuthMap a:list){
				if(tempContent.containsKey(a.getResource())){
					throw new IllegalArgumentException("Two the same resource:"+a.getResource());
				}
				tempContent.put(a.getResource(),a);
			}
			authContent=tempContent;
			log.info("Init authContent json data complete.");
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 监控用户文件
	 */
	public static void watchPlatformUserContent(){
		String filePath=Config.CONFIG_DIR + File.separator + "platformUser.json";
		File f = new File(filePath);
		if(f.exists()){
			if(f.lastModified()!=platformUserLastModify){
				platformUserLastModify=f.lastModified();
				initUserContent();
			}
		}else{
			throw new IllegalArgumentException("Can not find "+filePath);
		}
	}
	
	/**
	 * 监控权限文件
	 */
	public static void watchAuthContent(){
		String filePath=Config.CONFIG_DIR + File.separator + "authMap.json";
		File f = new File(filePath);
		if(f.exists()){
			if(f.lastModified()!=authMapLastModify){
				authMapLastModify=f.lastModified();
				initAuthContent();
			}
		}else{
			throw new IllegalArgumentException("Can not find "+filePath);
		}
	}
}
