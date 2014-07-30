package common.config;

import org.apache.commons.lang3.ArrayUtils;

import utils.StringUtils;

public class PlatformUser {
	private int id;
	private String name;
	private String password;
	private String auth;
	private String appids;
	/**权限数组*/
	private int[] authArray;
	/**可看应用*/
	private Integer[] appidArray;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getAppids() {
		return appids;
	}
	public void setAppids(String appids) {
		this.appids = appids;
	}
	public int[] authArray() {
		return authArray;
	}
	public Integer[] appidArray() {
		return this.appidArray;
	}
	public void initAuthArray(){
		if(StringUtils.isNotBlank(this.auth)){
			String[] as = this.auth.split(",");
			this.authArray=new int[as.length];
			for(int i=0;i<as.length;i++){
				this.authArray[i]=Integer.valueOf(as[i]);
			}
		}
	}
	public void initAppidArray(){
		if(StringUtils.isNotBlank(this.appids)){
			if("all".equalsIgnoreCase(this.appids)){
				Object[] obs=AppContent.appContent.keySet().toArray();
				this.appidArray=new Integer[obs.length];
				for(int i=0;i<obs.length;i++){
					this.appidArray[i]=(Integer) obs[i];
				}
			}else{
				String[] as = this.appids.split(",");
				this.appidArray=new Integer[as.length];
				for(int i=0;i<as.length;i++){
					this.appidArray[i]=Integer.valueOf(as[i]);
				}
			}
		}else{
			this.appidArray=new Integer[0];
		}
	}
	
	public void addApp(int appid){
		if(!"all".equalsIgnoreCase(this.appids)){
			if(StringUtils.isBlank(this.appids)){
				this.appids=appid+"";
			}else{
				this.appids+=","+appid;
			}
		}
		this.appidArray=ArrayUtils.add(this.appidArray, appid);
	}
	
	public void deleteApp(int appid){
		for(int i=0;i<this.appidArray.length;i++){
			if(this.appidArray[i]==appid){
				this.appidArray=ArrayUtils.remove(this.appidArray, i);
				break;
			}
		}
		if(!"all".equalsIgnoreCase(this.appids)){
			StringBuilder sb=new StringBuilder();
			for(int i:this.appidArray){
				sb.append(i).append(",");
			}
			this.appids=StringUtils.removeEnd(sb.toString(), ",");
		}
	}
}
