package com.snail.mtk.sfcs.monitor.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import com.snail.file.monitor.jnotify.handler.FileHandler;
import com.snail.rmi.sfcs.EciSonyIdpLogService;
import com.snail.util.StringUtils;

public class EciSonyIdpLogHandler implements FileHandler{

	private static final long serialVersionUID = -7723687173734754300L;
	
	private EciSonyIdpLogService service;
	private String encoding = "UTF-8";
	
	@Override
	public boolean handle(String fileName, int eventCode) {
		if (eventCode != CREATE) {
			return false;
		}
		try {
			JSONObject jsonData = parse(fileName);
			return getService().saveIdpData(jsonData);
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
		JSONObject idpDataJson = new JSONObject();
		BufferedReader buf = null;
		try {
			File file = new File(fileName);
			buf = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), getEncoding()));
			String line = buf.readLine();
			while (line != null) {
				if(StringUtils.isBlank(line) || !line.contains("==>")){
					line = buf.readLine();
					continue;
				}
				String[] tmps = line.split("==>");
				if(tmps != null &&  tmps.length > 1){
					idpDataJson.put(tmps[0],tmps[1]);
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
		return idpDataJson;
	}

	public EciSonyIdpLogService getService() {
		return service;
	}

	public void setService(EciSonyIdpLogService service) {
		this.service = service;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	
	public static void main(String[] args) {
		BufferedReader buf = null;
		try {
			File file = new File("D:\\433464708446.txt");
			System.out.println(file.getName().split("\\.")[0]);
			buf = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), "UTF-8"));
			String line = buf.readLine();
			int i = 0;
			while (line != null) {
				i++;
				if(StringUtils.isBlank(line)){
					line = buf.readLine();
					continue;
				}
				if(!line.startsWith("NWSCP==>")){
					line = buf.readLine();
					continue;
				}
				String[] tmps = line.split("==>");
				if(tmps != null &&  tmps.length > 1){
					System.out.println(i+":"+line.split("==>")[1]);
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
	}
}
