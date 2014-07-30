package common.config;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.FileUtils;
import utils.JsonUtils;
import common.logger.Logger;
import common.logger.LoggerManger;

public class UnionContent {
	private static Logger log=LoggerManger.getLogger();
	
	public static Map<String,Union> unionContent=new ConcurrentHashMap<String,Union>();
	public static Map<String,Union> uriContent=new ConcurrentHashMap<String,Union>();
	private static long unionConentLastModify=0;
	
	private static void init(){
		log.info("Star init unions json data.");
		String filePath=Config.CONFIG_DIR + File.separator + "unions.json";
		String jsonSrc=FileUtils.readFileToJSONString(filePath);
		Union[] list=(Union[])JsonUtils.objectFromJson(jsonSrc, Union[].class);
		Map<String,Union> tempUnionContent=new ConcurrentHashMap<String,Union>();
		Map<String,Union> tempUriContent=new ConcurrentHashMap<String,Union>();
		for(Union u:list){
			if(tempUnionContent.containsKey(u.getUnionid())){
				log.error("Find the same unionid "+u.getUnionid()+".");
				throw new IllegalArgumentException("Find the same unionid "+u.getUnionid()+".");
			}
			if(tempUriContent.containsKey(u.getUri())){
				log.error("Find the same uri "+u.getUri()+" in union:"+u.getUnionid()+".");
				throw new IllegalArgumentException("Find the same uri "+u.getUri()+" in union:"+u.getUnionid()+".");
			}
			if(IllegalCharContent.hasIllegalCharLike(u.getParams())){
				log.error("Find illegal char params "+u.getParams()+" in union:"+u.getUnionid()+".");
				throw new IllegalArgumentException("Find illegal char params "+u.getParams()+" in union:"+u.getUnionid()+".");
			}
			tempUnionContent.put(u.getUnionid(), u);
			tempUriContent.put(u.getUri(), u);
		}
		unionContent=tempUnionContent;
		uriContent=tempUriContent;
		log.info("Init unions json data complete.");
	}
	
	/**
	 * 监控权限文件
	 */
	public static void watchUnionContent(){
		String filePath=Config.CONFIG_DIR + File.separator + "unions.json";
		File f = new File(filePath);
		if(f.exists()){
			if(f.lastModified()!=unionConentLastModify){
				unionConentLastModify=f.lastModified();
				init();
			}
		}else{
			throw new IllegalArgumentException("Can not find "+filePath);
		}
	}
}
