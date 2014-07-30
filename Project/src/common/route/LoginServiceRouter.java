package common.route;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.Def;
import utils.ReqUtils;
import utils.RespUtils;
import utils.StringUtils;
import common.config.Union;
import common.config.UnionContent;
import common.json.JSONObject;
import common.logger.Logger;
import common.logger.LoggerManger;

/**
 * 
 * @Description 登陆服务路由控制器
 * @author liangyx
 * @date 2014-6-26
 * @version V1.0
 */
public class LoginServiceRouter extends HttpServlet{
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
			
			String postStr = ReqUtils.getPostString(req);
			
			if(StringUtils.isNotBlank(postStr)){
				JSONObject json = new JSONObject(postStr);
				String unionid=json.getString("unionid");
				Union union=UnionContent.unionContent.get(unionid);
				req.setAttribute("postContent", postStr);
				if(union!=null){
					req.getRequestDispatcher("/loginservice/"+union.getLoginService()+".jsp").forward(req, resp);
					return;
				}
			}
			resp.setStatus(401);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			try {
				RespUtils.LoginAuthResp(resp, Def.LoginException, "");
			} catch (Exception e1) {
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
