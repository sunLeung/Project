package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import common.logger.Logger;

public class ReqUtils {
	private static String defaultCharset="utf-8";
	
	public static String getPostString(HttpServletRequest req){
		return getPostString(req,defaultCharset);
	}
	public static String getPostString(HttpServletRequest req,String charset){
		BufferedReader br=null;
		StringBuilder sb=new StringBuilder();
		try {
			br=new BufferedReader(new InputStreamReader(req.getInputStream(), charset));
			String temp="";
			while((temp=br.readLine())!=null){
				sb.append(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (Exception e2) {
				e2.toString();
			}
		}
		return sb.toString();
	}
	
	public static void requestGameServer(Logger log,String url,int loginid,long uuid,float money,String unionid,int statue,long orderid){
		requestGameServer(log, url, Def.RequestLoop, loginid, uuid, money, unionid, statue, orderid);
	}
	public static void requestGameServer(Logger log,String url,int loop,int loginid,long uuid,float money,String unionid,int statue,long orderid){
		try {
			Map<String,String> params=new HashMap<String,String>();
			params.put("loginid", loginid+"");
			params.put("uuid", uuid+"");
			params.put("money", money+"");
			params.put("unionid", unionid);
			params.put("statue", statue+"");
			params.put("orderid", orderid+"");
			int state = 0;
			for (int i = 0; i < loop; i++) {
				url=HttpUtils.linkParams(url, params);
				System.out.println(url);
				URL realUrl = new URL(url);
				URLConnection con = realUrl.openConnection();
				HttpURLConnection conn  =  (HttpURLConnection) con;
				conn.setRequestProperty("accept", "*/*");
				conn.setRequestProperty("connection", "Keep-Alive");
				conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				conn.setReadTimeout(2000);
				conn.connect();
				state = conn.getResponseCode();
				if (state == 200) {
					log.info("Request url SUCCEED("+state+"): "+url);
					break;
				}else{
					log.info("Request url FAIL("+state+"): "+url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
	}
}
