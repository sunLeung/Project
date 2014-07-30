package dao;

import static common.db.DbUtils.dbUtils;

import java.sql.SQLException;

import bean.PayBean;

import common.logger.Logger;
import common.logger.LoggerManger;

public class PayDao {
	private static Logger log=LoggerManger.getLogger();
	
	public static int save(PayBean bean){
		try {
			return dbUtils.insert(bean);
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}
	
	public static PayBean getPayBeanByCporderid(String cporderid){
		PayBean bean=null;
		try {
			bean=dbUtils.read(PayBean.class, "where cporderid=?", cporderid);
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return bean;
	}
	
	public static PayBean getPayBeanByOrderid(String orderid){
		PayBean bean=null;
		try {
			bean=dbUtils.read(PayBean.class, "where orderid=?", orderid);
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return bean;
	}
}
