package common.listener;

import java.io.File;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import utils.TimerManagerUtils;
import common.config.Config;
import common.config.PlatformUserContent;
import common.config.UnionContent;
import common.logger.Logger;
import common.logger.LoggerManger;

public class WatchListener{
	public static ScheduledFuture futrue;
	public static Logger log=LoggerManger.getLogger();
	
	public static void startWatch(){
		log.info("Start watching thread.");
		futrue=TimerManagerUtils.schedule(new Runnable() {
			@Override
			public void run() {
				//监控日志配置文件
				LoggerManger.watchLogConfig(Config.CONFIG_DIR+File.separator+"logger.xx");
				//监控平台用户配置文件
				PlatformUserContent.watchPlatformUserContent();
				//监控平台权限文件
				PlatformUserContent.watchAuthContent();
				//初始化渠道配置
				UnionContent.watchUnionContent();
			}
		}, Config.WATCH_SECOND, TimeUnit.SECONDS);
		log.info("Start watching thread completed.");
	}
	
	public static void stopWatch(){
		log.info("Stop watching thread.");
		if(futrue!=null){
			futrue.cancel(true);
			TimerManagerUtils.many_t_scheduler.shutdown();
			TimerManagerUtils.single_t_scheduler.shutdown();
		}
		log.info("Stop watching thread completed.");
	}
}
