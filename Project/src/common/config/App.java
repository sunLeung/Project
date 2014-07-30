package common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utils.StringUtils;

/**
 * 应用实体类
 * @author L
 *
 */
public class App {
	private int appid;
	private String appname;
	private Map<String,String>[] uniondata;
	private Map<String,String>[] servers;
	private Map<String,Map<String,String>> unionContent=new ConcurrentHashMap<String,Map<String,String>>();
	private Map<String,Map<String,String>> serverContent=new ConcurrentHashMap<String,Map<String,String>>();
	public int getAppid() {
		return appid;
	}
	public void setAppid(int appid) {
		this.appid = appid;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public Map<String, String>[] getUniondata() {
		return uniondata;
	}
	public void setUniondata(Map<String, String>[] uniondata) {
		this.uniondata = uniondata;
	}
	public Map<String, String>[] getServers() {
		return servers;
	}
	public void setServers(Map<String, String>[] servers) {
		this.servers = servers;

	}
	public Map<String, Map<String, String>> unionContent() {
		return unionContent;
	}
	public Map<String, Map<String, String>> serverContent() {
		return serverContent;
	}
	public void initMapContent(){
		//初始化服务器检索表
		for(Map<String,String> map:servers){
			String sid=map.get("serverid");
			if(StringUtils.isNotBlank(sid))
				serverContent.put(sid, map);
		}
		//初始化渠道检索表
		for(Map<String,String> map:uniondata){
			String unionid=map.get("unionid");
			if(StringUtils.isNotBlank(unionid))
				unionContent.put(unionid, map);
		}
	}
	
	/**
	 * 获取渠道参数
	 * @param unionid
	 * @param key
	 * @return
	 */
	public String getUnionParam(String unionid,String key){		
		Map<String,String> map=unionContent.get(unionid);
		if(map!=null){
			return map.get(key);
		}
		return null;
	}
	
	/**
	 * 获取道具服的地址
	 * @param serverid
	 * @return
	 */
	public String getServerUrl(String serverid){
		Map<String,String> map=serverContent.get(serverid);
		if(map!=null){
			return map.get("url");
		}
		return null;
	}
}
