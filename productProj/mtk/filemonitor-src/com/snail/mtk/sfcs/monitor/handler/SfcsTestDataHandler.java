package com.snail.mtk.sfcs.monitor.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.snail.file.monitor.jnotify.handler.FileHandler;
import com.snail.rmi.sfcs.TestDataService;

/**
 * 
 * <p>
 * Title: com.snail.file.monitor.jnotify.file.SfcsTestDataHandler
 * </p>
 * 
 * <p>
 * Description: SFCS 测试数据自动监控处理类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月27日
 * 
 * @version 1.0
 *
 */
public abstract class SfcsTestDataHandler implements FileHandler {

	private TestDataService service;
	private String encoding = "UTF-8";
	private String fields;

	private Map<String, String> fixedData = null;

	@Override
	public boolean handle(String fileName, int eventCode) {
		if (eventCode != CREATE) {
			return false;
		}
		try {
			JSONArray datas = parse(fileName);
			return getService().saveTestData(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解析文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private JSONArray parse(String fileName) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		JSONArray testDataJsonObjs = new JSONArray();
		if (StringUtils.isBlank(getFields())) {
			System.out.println("请指定字段值.");
			return testDataJsonObjs;
		}
		BufferedReader buf = null;
		try {
			File file = new File(fileName);
			buf = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), encoding));
			String line = buf.readLine();
			while (line != null) {
				JSONObject json = parseToJsonObj(line);
				if (json != null && !json.isNullObject() && !json.isEmpty()
						&& StringUtils.isNotBlank(json.getString("PPID"))) {
					if (getFixedData() != null && !getFixedData().isEmpty()) {
						json.putAll(getFixedData());
					}
					testDataJsonObjs.add(json);
				}
				line = buf.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (buf != null) {
					buf.close();
					buf = null;
				}
			} catch (IOException e) {
				// do nothing
			}
		}
		return testDataJsonObjs;
	}

	/**
	 * 将每行数据按逗号分隔,并用指定的字段名构造成JSONObject.
	 * 
	 * @param line
	 * @return
	 */
	private JSONObject parseToJsonObj(String line) {
		String[] fieldStr = fields.split(",");
		String[] datas = line.split(",");
		int len = fieldStr.length;
		if (len > datas.length) {
			len = datas.length;
		}
		JSONObject jsonBean = new JSONObject();
		for (int index = 0; index < len; index++) {
			if (StringUtils.isBlank(datas[index])) {
				continue;
			}
			jsonBean.put(fieldStr[index], datas[index]);
		}
		jsonBean = extHandler(jsonBean);
		return jsonBean;
	}

	/**
	 * 对jsonBean 做特殊的处理.
	 * 
	 * @param jsonBean
	 * @return
	 */
	protected abstract JSONObject extHandler(JSONObject jsonBean);

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public Map<String, String> getFixedData() {
		return fixedData;
	}

	public void setFixedData(Map<String, String> fixedData) {
		this.fixedData = fixedData;
	}

	public TestDataService getService() {
		return service;
	}

	public void setService(TestDataService service) {
		this.service = service;
	}
}
