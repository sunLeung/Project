package common.config;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import utils.FileUtils;
import utils.JsonUtils;
import utils.StringUtils;
import common.logger.Logger;
import common.logger.LoggerManger;

public class AppContent {
	private static Logger log=LoggerManger.getLogger();
	
	public static Map<Integer,App> appContent=new ConcurrentHashMap<Integer,App>();
	/**统计应用ID*/
	public static AtomicInteger topAppID=new AtomicInteger(0);
	
	public static void init(){
		log.info("Star init apps json data.");
		String filePath=Config.CONFIG_DIR + File.separator + "apps.json";
		String jsonSrc=FileUtils.readFileToJSONString(filePath);
		if(StringUtils.isNotBlank(jsonSrc)){
			App[] list=(App[])JsonUtils.objectFromJson(jsonSrc, App[].class);
			for(App a:list){
				if(appContent.containsKey(a.getAppid())){
					log.error("Find the same appid "+a.getAppid()+".");
					throw new IllegalArgumentException("Find the same appid "+a.getAppid()+".");
				}
				appContent.put(a.getAppid(), a);
				a.initMapContent();
				if(a.getAppid()>topAppID.get()){
					topAppID.set(a.getAppid());
				}
			}
		}
		log.info("Init apps json data completed.");
	}
	
	
	public static void flush(){
		log.info("Star flush apps json data.");
		try {
			String filePath=Config.CONFIG_DIR + File.separator + "apps.json";
			Object[] apps=(Object[])appContent.values().toArray();
			String json=JsonUtils.jsonFromObject(apps);
			FileUtils.writeStringToFile(filePath, json);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
		log.info("Flush apps json data completed.");
	}
	
	public static void addApp(){
		
	}
	
	
	
	public static void main(String[] args) {
//		init();
//		System.out.println(appContent.get(1).getAppname());
//		System.out.println(appContent.get(1).getUniondate().get("8888").get("key"));
//		App a=new App();
//		a.setAppid(2);
//		a.setAppname("废话");
//		Map<String,String> server=new HashMap<String,String>();
//		server.put("200", "http://192.168.1.1");
//		a.setServers(server);
//		Map<String,String> u=new HashMap<String,String>();
//		u.put("key", "asdfasdlkqj");
//		Map<String,Map<String,String>> uu=new HashMap<String,Map<String,String>>();
//		uu.put("902", u);
//		a.setUniondate(uu);
//		appContent.put(2, a);
//		flush();
	}
}
