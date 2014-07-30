package common.admin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.config.PlatformUser;

public class AdminFilter implements Filter{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		((HttpServletResponse) response).setHeader("Pragma", "No-cache");
		((HttpServletResponse) response).setHeader("Cache-Control","no-cache");
		((HttpServletResponse) response).setDateHeader("Expires", 0);
		
		if (req.getRequestURI().contains("admin")&&!req.getRequestURI().equals("/admin/login")) {
			HttpSession session = req.getSession();
			PlatformUser user=(PlatformUser)session.getAttribute("loginUser");
			if(user==null){
				res.sendRedirect("/login.html");
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
