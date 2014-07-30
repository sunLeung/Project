package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomUtils;

import common.db.C3P0Utils;
import common.db.DbUtils;
import dao.TestDao;

public class OracleTest {
	public static void main(String[] args) throws SQLException {
//		Table1 t=new Table1();
//		t.setName("hi test");
//		int i=DbUtils.dbUtils.insertOracle(t,"seq_index.nextval");
//		System.out.println(i);
		
//		String sql="INSERT INTO SYSTEM.TABLE1 VALUES (seq_index.nextval, 'abc')";
//		DbUtils.dbUtils.runInsertOrUpdateSQLNoCache(sql);
		
//		int index=DbUtils.dbUtils.stat("select count(id) from table1");
//		Table1 t=DbUtils.dbUtils.read(Table1.class, index);
//		System.out.println(t.getName());
		
//		int index=DbUtils.dbUtils.stat("select count(id) from table1");
//		DbUtils.dbUtils.delete(Table1.class, index);
		
		
		for(int i=0;i<909;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						int r=ThreadLocalRandom.current().nextInt(0,2);
						if(r==0){
							for(int i=0;i<1000;i++){
								Table1 t=new Table1();
								t.setName(RandomStr.randomString(10));
								DbUtils.dbUtils.insertOracle(t,"seq_index.nextval");
							}
						}else if(r==1){
							int index=DbUtils.dbUtils.stat("select count(id) from table1");
							Table1 t=DbUtils.dbUtils.read(Table1.class, index);
							System.out.println(t.getName());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		}
	}
	public static void insert() throws SQLException {
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			con=C3P0Utils.getConnection();
			C3P0Utils.startTransaction();
			String sql = "insert into table2(name) values (?)";
			pst = con.prepareStatement(sql);
			pst.setString(1, "fuck");
			pst.executeUpdate();
			C3P0Utils.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				rs.close();
			}
			if(pst!=null){
				pst.close();
			}
			if(con!=null){
				con.close();
			}
		}
	}
}
