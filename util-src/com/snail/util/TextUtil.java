package com.snail.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * 
 * <p>
 * Title: com.snail.util.TextUtil
 * </p>
 * 
 * <p>
 * Description: 文本文件处理工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月13日
 * 
 * @version 1.0
 *
 */
public class TextUtil {

	private static Logger LOG = Logger.getLogger(TextUtil.class);
	/**
	 * 将items中的内容写大文件中.
	 * @param file
	 * @param items
	 */
	public void exportTxt(File file, String[] items) {
		if(file == null){
			LogUtil.errorLog(LOG, "未指定输出文件!");
			return;
		}
		if(items == null || items.length <1){
			return;
		}
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < items.length; i++) {
				bufferedWriter.write(items[i]);
				bufferedWriter.write("\r");
			}
		} catch (IOException e) {
			LogUtil.errorLog(LOG, "向文件中写入数据失败!", e);
		} finally {
			try {
				bufferedWriter.close();
			} catch (IOException e) {
				LogUtil.errorLog(LOG, "bufferedWriter关闭异常!");
			}
		}
	}

	/**
	 * 将输入流中的数据读取出来.
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public String impTxtItemValue(InputStream in) throws Exception {
		if(in == null){
			return "";
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		String data = br.readLine();
		while (data != null && data.trim().length() > 0) {
			buffer.append(data).append(",");
			data = br.readLine();
		}
		return buffer.substring(0, buffer.toString().lastIndexOf(","));
	}
}
