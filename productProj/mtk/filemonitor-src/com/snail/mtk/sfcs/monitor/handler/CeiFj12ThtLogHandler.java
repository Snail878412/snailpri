package com.snail.mtk.sfcs.monitor.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import net.sf.json.JSONObject;

import com.snail.file.monitor.jnotify.handler.FileHandler;
import com.snail.rmi.sfcs.CeiFj12ThtLogService;
import com.snail.util.StringUtils;

public class CeiFj12ThtLogHandler implements FileHandler{

	private static final long serialVersionUID = -7723687173734754300L;
	
	private CeiFj12ThtLogService service;
	private String encoding = "UTF-8";
	
	@Override
	public boolean handle(String fileName, int eventCode) {
		if (eventCode != CREATE) {
			return false;
		}
		try {
			JSONObject jsonData = parse(fileName);
			return getService().saveThtData(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private JSONObject parse(String fileName) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		JSONObject thtDataJson = new JSONObject();
		BufferedReader buf = null;
		try {
			File file = new File(fileName);
			buf = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), getEncoding()));
			String line = buf.readLine();
			while (line != null) {
				if(StringUtils.isBlank(line) || !line.contains(":")){
					line = buf.readLine();
					continue;
				}
				String[] tmps = line.split(":");
				if(tmps != null &&  tmps.length > 1){
					thtDataJson.put(tmps[0].trim(),tmps[1].trim());
				}
				line = buf.readLine();
			}
		} catch (Exception e) {
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
		return thtDataJson;
	}

	public CeiFj12ThtLogService getService() {
		return service;
	}

	public void setService(CeiFj12ThtLogService service) {
		this.service = service;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	
	public static void main(String[] args) {
	}
}
