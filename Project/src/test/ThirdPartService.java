package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import common.json.JSONException;
import common.json.JSONObject;

public class ThirdPartService {
	
	public static String thirdPartAuth(String unionid,int appid,String data){
		try {
			String url="http://127.0.0.1:8080/login";
			JSONObject request=new JSONObject();
			request.put("unionid", unionid);
			request.put("appid", appid);
			request.put("data", data);
			return doPost(url, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws JSONException {
		JSONObject json=new JSONObject();
		json.put("fuck", "you");
		System.out.println(thirdPartAuth("1", 1, json.toString()));
	}
	
	
	private static String doPost(String url, JSONObject json) {
		PrintWriter out = null;
		BufferedReader in = null;
		StringBuffer result = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setReadTimeout(2000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(json.toString());
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result.toString();
	}
}
