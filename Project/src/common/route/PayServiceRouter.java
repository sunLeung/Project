package common.route;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.logger.Logger;
import common.logger.LoggerManger;

/**
 * 
 * @Description 路由控制器
 * @author liangyx
 * @date 2014-6-26
 * @version V1.0
 */
public class PayServiceRouter extends HttpServlet{
	private static Logger log=LoggerManger.getLogger();
	
	/**
	 * 路由分发器
	 * @param session
	 * @param is
	 */
	public static void handlePath(HttpServletRequest req,HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("utf-8");
			resp.setHeader("content-type", "text/html;charset=UTF-8");
			String uri = req.getRequestURI();
			System.out.println(uri);
			//请求支付
			if(uri.contains("/pay/")){
				String[] date=uri.split("/");
				String union=date[2];
				String appid=date[3];
				req.setAttribute("appid", appid);
				req.getRequestDispatcher("/payservice/"+union+".jsp").forward(req, resp);
				return;
			}
			resp.setStatus(401);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			try {
				resp.getWriter().write("Wrong uri request.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handlePath(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handlePath(req,resp);
	}
}
