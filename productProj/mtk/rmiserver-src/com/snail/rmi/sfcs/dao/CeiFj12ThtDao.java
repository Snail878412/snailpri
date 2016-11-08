package com.snail.rmi.sfcs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

public class CeiFj12ThtDao {
	private static String url = "jdbc:oracle:thin:@10.87.35.73:1521:orcl";
	private static String username = "SFCS_prod";
	private static String password = "SFCS_pro_ecd2008";
	private static String driver = "oracle.jdbc.driver.OracleDriver";

	private static String[] columns = {"Read SN","Read Idm"};
	
	private static String tab_columns = "SN,IDM";
	
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean insertToCeiFj12ThtData(JSONObject jsonData){
		String sql = "INSERT INTO CEI_FJ12_THT_DATA (SYS_ID," + tab_columns + ",CREATE_DATE) " +
				"VALUES (SYS_GUID()";
		for (int index = 0; index < columns.length; index++) {
			sql += ",?";
		}
		sql += ",SYSDATE)";
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstm = conn.prepareStatement(sql);
			int index = 1;
			for(String key:columns){
				if(jsonData.containsKey(key)){
					pstm.setString(index++, getStringValue(jsonData,key));
				}else{
					pstm.setString(index++, "");
				}
			}
			pstm.execute();
			conn.commit();
			conn = null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.rollback();
					conn.close();
					conn = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	private static String getStringValue(JSONObject jsonData,String key){
		if(!key.equals("Read SN")){
			return  jsonData.getString(key);
		}
		String sn = jsonData.getString(key);
		if(StringUtils.isBlank(sn)){
			return "";
		}
		
		if(sn.length() < 3){
			return sn;
		}
		return sn.substring(3);
	}
}
