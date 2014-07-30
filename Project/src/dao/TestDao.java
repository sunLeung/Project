package dao;

import static common.db.DbUtils.dbUtils;

import java.sql.SQLException;

import test.Table1;
import test.Table2;

import common.logger.Logger;
import common.logger.LoggerManger;

public class TestDao {
	private static Logger log=LoggerManger.getLogger();
	
	public static int save(Table1 bean){
		try {
			return dbUtils.insert(bean);
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}
	public static int save2(Table2 bean){
		try {
			return dbUtils.insert(bean);
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}
}
