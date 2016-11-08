package com.snail.mtk.sfcs.monitor.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.file.ExtendTestHandler
 * </p>
 * 
 * <p>
 * Description : 延长测试站的测试记录解析.
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
public class ExtendTestHandler extends SfcsTestDataHandler {

	public ExtendTestHandler() {
		Map<String, String> fixedData = new HashMap<String, String>();
		fixedData.put("FOR_SITE", "extend_test_station");
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
		}
		if (jsonBean.containsKey("TIME")) {
			time = jsonBean.getString("TIME");
		}
		jsonBean.put("TEST_TIME", testDate + " " + time);
		return jsonBean;
	}
}
