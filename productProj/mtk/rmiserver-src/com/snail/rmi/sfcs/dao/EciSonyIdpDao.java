package com.snail.rmi.sfcs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sf.json.JSONObject;

import com.snail.util.StringUtils;

public class EciSonyIdpDao {
	private static String url = "jdbc:oracle:thin:@10.87.35.73:1521:orcl";
	private static String username = "SFCS_prod";
	private static String password = "SFCS_pro_ecd2008";
	private static String driver = "oracle.jdbc.driver.OracleDriver";

	private static String[] columns = {"IMEI","BT MAC","WiFi MAC","MSN","NWSCP","OTP Status"};
	
	private static String tab_columns = "IMEI,BT_MAC,WIFI_MAC,SN,NWSCP,OTP_STATUS";
	
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

	public static boolean insertToEciSonyIdpData(JSONObject jsonData){
		String sql = "INSERT INTO CEI_SONY_IDP_DATA (SYS_ID," + tab_columns + ",CREATE_DATE) " +
				"VALUES (SYS_GUID()";
		for (int index = 0; index < columns.length; index++) {
			sql += ",?";
		}
		sql += ",SYSDATE)";
		if (jsonData == null || jsonData.isEmpty() || !jsonData.has("MSN")
				|| StringUtils.isBlank(jsonData.getString("MSN"))) {
			return false;
		}
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstm = conn.prepareStatement(sql);
			int index = 1;
			for(String key:columns){
				if(jsonData.containsKey(key)){
					pstm.setString(index++, jsonData.getString(key));
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
}
