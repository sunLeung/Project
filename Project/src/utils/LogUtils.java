package utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import common.logger.Logger;

public class LogUtils {
	
	/**
	 * 记录请求params
	 * @param log
	 * @param req
	 */
	public static void logReqParams(Logger log,HttpServletRequest req){
		StringBuilder sbReq=new StringBuilder();
		Map<String,String[]> keys=req.getParameterMap();
		for(java.util.Map.Entry<String,String[]> entry:keys.entrySet()){
			String key=entry.getKey();
			String value=(String)entry.getValue()[0];
			sbReq.append(key).append("=").append(value).append("&");
		}
		String postInfo=StringUtils.removeEnd(sbReq.toString(), "&");
		log.info(req.getMethod()+":"+postInfo);
	}
}
