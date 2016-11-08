package com.snail.rmi.sfcs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.snail.util.ProUtils;

/**
 * 
 * <p>
 * Title: com.snail.rmi.sfcs.dao.DaoUtil
 * </p>
 * 
 * <p>
 * Description DAO 工具类.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014-8-28
 * 
 * @version 1.0
 *
 */
public class DaoUtil {

	private static String insertSql = "INSERT INTO PE_DATA_EXT (TEST_TIME,PPID, MAC, CUSTOMER_VERSION, DPN, BIOS_VERSION, SITE, SERVICE_TAG, TEST_RESULT, ERROR_CODE, FAIL_ITEM,STATUS, FOR_SITE) VALUES (to_date(?,'yyyy-mm-dd hh24:mi:ss') , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null, ?)";
	private static String url;
	private static String username;
	private static String password;
	private static String driver;
	private static Properties pro;
	private static String fileName = "monitorConfig.properties";

	static {
		initPro(fileName);

		url = pro.getProperty("url");
		username = pro.getProperty("username");
		password = pro.getProperty("password");
		driver = pro.getProperty("driver");
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

	@SuppressWarnings("unchecked")
	public static boolean saveToPeDataExt(JSONArray testDataJsonObjs) {
		if (testDataJsonObjs == null || testDataJsonObjs.isEmpty()) {
			System.out.println("没有需要保存的数据");
			return false;
		}

		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstm = conn.prepareStatement(getInsertSql());
			Iterator<JSONObject> it = testDataJsonObjs.iterator();
			while(it.hasNext()) {
				JSONObject json = it.next();
				insertTestData(pstm, json);
			}
			pstm.executeBatch();
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

	private static void insertTestData(PreparedStatement pstm, JSONObject json)
			throws SQLException {
		String testTime = "";
		String ppid = "";
		String mac = "";
		String custVer = "";
		String dpn = "";
		String biosVer = "";
		String site = "";
		String serviceTag = "";
		String testResult = "";
		String errorCode = "";
		String failItem = "";
		String forSite = "";
		if (json.containsKey("TEST_TIME")) {
			testTime = json.getString("TEST_TIME");
		}
		if (json.containsKey("PPID")) {
			ppid = json.getString("PPID");
		}
		if (json.containsKey("MAC")) {
			mac = json.getString("MAC");
		}
		if (json.containsKey("CUSTOMER_VERSION")) {
			custVer = json.getString("CUSTOMER_VERSION");
		}
		if (json.containsKey("DPN")) {
			dpn = json.getString("DPN");
		}
		if (json.containsKey("BIOS_VERSION")) {
			biosVer = json.getString("BIOS_VERSION");
		}
		if (json.containsKey("SITE")) {
			site = json.getString("SITE");
		}
		if (json.containsKey("SERVICE_TAY")) {
			serviceTag = json.getString("SERVICE_TAY");
		}
		if (json.containsKey("TEST_RESULT")) {
			testResult = json.getString("TEST_RESULT");
		}
		if (json.containsKey("ERROR_CODE")) {
			errorCode = json.getString("ERROR_CODE");
		}
		if (json.containsKey("FAIL_ITEM")) {
			failItem = json.getString("FAIL_ITEM");
		}
		if (json.containsKey("FOR_SITE")) {
			forSite = json.getString("FOR_SITE");
		}
		int i = 1;
		pstm.setString(i++, testTime);
		pstm.setString(i++, ppid);
		pstm.setString(i++, mac);
		pstm.setString(i++, custVer);
		pstm.setString(i++, dpn);
		pstm.setString(i++, biosVer);
		pstm.setString(i++, site);
		pstm.setString(i++, serviceTag);
		pstm.setString(i++, testResult);
		pstm.setString(i++, errorCode);
		pstm.setString(i++, failItem);
		pstm.setString(i++, forSite);
		pstm.addBatch();
	}

	public static void main(String[] args) {
		JSONArray testDataJsonObjs = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("TEST_TIME", "2014-08-28 0:17:00");
		json.put("PPID", "123456789");
		json.put("MAC", "123456677");
		json.put("FOR_SITE", "master_clean_station");
		testDataJsonObjs.add(json);
		saveToPeDataExt(testDataJsonObjs);
	}
	
	public static String getInsertSql() {
		return insertSql;
	}

	public static void setInsertSql(String insertSql) {
		DaoUtil.insertSql = insertSql;
	}

	private static boolean initPro(String fileName) {
		try {
			pro = ProUtils.createProperties(fileName, false);
			return true;
		} catch (IOException e) {
			System.out.println("============配置文件初始化失败[" + fileName + "]");
			e.printStackTrace();
		}
		return false;
	};
}
