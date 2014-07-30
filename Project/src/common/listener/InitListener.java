package common.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import common.admin.AdminServlet;
import common.config.AppContent;
import common.config.Config;
import common.config.IllegalCharContent;
import common.config.PlatformUserContent;
import common.config.UnionContent;
import common.db.C3P0Utils;
import common.logger.LoggerManger;

/**
 * 
 * @Description 启服关服控制器
 * @author liangyx
 * @date 2014-6-26
 * @version V1.0
 */
public class InitListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//清理c3p0
		C3P0Utils.destroy();
		//日志回写
		LoggerManger.stopFileWriter();
		//定时器销毁
		WatchListener.stopWatch();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//初始化配置
		initConfig(arg0);
		//logger组件初始化
		LoggerManger.initLoggerConfig(Config.CONFIG_DIR+File.separator+"logger.xx");
		//启动定时器
		WatchListener.startWatch();
		//非法字符初始化
		IllegalCharContent.init();
		//初始化渠道配置
		UnionContent.watchUnionContent();
		//初始化应用配置
		AppContent.init();
		//初始化Admin管理方法
		AdminServlet.initMethods();
		//初始化平台用户数据
		PlatformUserContent.watchPlatformUserContent();
		//初始化权限数据
		PlatformUserContent.watchAuthContent();
	}
	
	private void initConfig(ServletContextEvent sce){
		Config.CONFIG_DIR=sce.getServletContext().getRealPath("")+File.separator+"WEB-INF"+File.separator+File.separator+Config.CONFIG_DIR;
	}

}
