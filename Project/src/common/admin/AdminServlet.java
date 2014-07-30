package common.admin;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.commons.lang3.ArrayUtils;

import common.config.AuthMap;
import common.config.PlatformUser;
import common.config.PlatformUserContent;
import common.logger.Logger;
import common.logger.LoggerManger;

public class AdminServlet extends HttpServlet{
	private static Logger log=LoggerManger.getLogger();
	public static Map<String,Method> methods=new HashMap<String,Method>();
	
	public static void initMethods(){
		Method[] ms=AdminService.class.getMethods();
		for(Method m:ms){
			methods.put(m.getName(), m);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp){
		excute(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		excute(req, resp);
	}
	
	public void excute(HttpServletRequest req,HttpServletResponse resp){
		try {
			req.setCharacterEncoding("utf-8");
			resp.setHeader("content-type", "text/html;charset=UTF-8");
			String uri = req.getRequestURI();
			System.out.println(uri);
			
			//权限过滤
			AuthMap auth=PlatformUserContent.authContent.get(uri);
			if(auth!=null){
				HttpSession session = req.getSession();
				PlatformUser user = (PlatformUser) session.getAttribute("loginUser");
				if(user==null){
					resp.sendRedirect("/login.html");
					return;
				}else{
					if(!ArrayUtils.contains(user.authArray(), auth.getAuthCode())){
						resp.setStatus(401);
						return;
					}
				}
			}
			
			
			if(uri.contains("/admin/")){
				String[] date=uri.split("/");
				String mkey=date[2];
				Method method=methods.get(mkey);
				if(method!=null){
					method.invoke(AdminService.class, req,resp);
				}else{
					resp.getWriter().write("undefine method.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}
	}
}
