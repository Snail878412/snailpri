package com.snail.mtk.sfcs.monitor.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.file.MasterCleanTestDataHandler
 * </p>
 * 
 * <p>
 * Description: Master clean 站的测试记录解析.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014-8-26
 * 
 * @version 1.0
 * 
 */
public class MasterCleanHandler extends SfcsTestDataHandler {

	private static SimpleDateFormat srcDateFormater = new SimpleDateFormat(
			"MM/dd/yyyy");
	private static SimpleDateFormat targetDateFormater = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat srcTimeFormater = new SimpleDateFormat(
			"H:mm:ss.SS");
	private static SimpleDateFormat targetTimeFormater = new SimpleDateFormat(
			"HH:mm:ss");

	public MasterCleanHandler() {
		Map<String, String> fixedData = new HashMap<String, String>();
		fixedData.put("FOR_SITE", "master_clean_station");
		setFixedData(fixedData);
	}

	protected JSONObject extHandler(JSONObject jsonBean) {
		String testDate = "";
		String time = "";
		String mac = "";
		if (jsonBean.containsKey("MAC")) {
			mac = jsonBean.getString("MAC");
			mac = mac.replaceAll(":", "");
			String[] tmp = mac.split("-");
			if (tmp.length > 2) {
				mac = tmp[0] + tmp[tmp.length - 1];
			}
			jsonBean.put("MAC", mac);
		}
		if (jsonBean.containsKey("TEST_DATE")) {
			testDate = jsonBean.getString("TEST_DATE");
			try {
				testDate = targetDateFormater.format(srcDateFormater
						.parse(testDate.split(" ")[1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (jsonBean.containsKey("TIME")) {
			time = jsonBean.getString("TIME");
			try {
				time = targetTimeFormater.format(srcTimeFormater.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		jsonBean.put("TEST_TIME", testDate + " " + time);
		return jsonBean;
	}
}
