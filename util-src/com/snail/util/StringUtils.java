package com.snail.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 
 * <p>
 * Title: com.snail.util.StringUtils
 * </p>
 * 
 * <p>
 * Description: 字符串处理工具类
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
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	/**
	 * 将object转换成string.若为null,则返回空串.
	 * 
	 * @param obj
	 * @return
	 */
	public static final String convertToStr(Object obj) {
		return convertToValue(obj, "");
	}

	/**
	 * 将object转换成string.若为null,则返回指定的字符串.
	 * 
	 * @param obj
	 * @return
	 */
	public static final String convertToValue(Object obj, String value) {
		if (obj == null) {
			return value;
		}
		return obj.toString();
	}

	/**
	 * 将string转换成list.
	 * 
	 * @param str
	 * @param regex
	 *            分割正在表达式
	 * @return
	 */
	public static List<String> strToList(String str, String regex) {
		List<String> returnList = new ArrayList<String>();
		if (StringUtils.isBlank(str) || StringUtils.isEmpty(regex)) {
			return returnList;
		}
		String[] strs = str.split(regex);
		for (String tmp : strs) {
			returnList.add(tmp.trim());
		}
		return returnList;
	}

	/**
	 * 将string转换成map
	 * 
	 * @param key
	 * @param value
	 * @param regex
	 *            key和value使用统一的正则分割.
	 * @return
	 */
	public static Map<String, String> strToMap(String key, String value,
			String regex) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value)
				|| StringUtils.isEmpty(regex)) {
			return returnMap;
		}
		String[] keys = key.split(regex);
		String[] values = value.split(regex);
		int len = Math.max(keys.length, values.length);
		for (int i = 0; i < len; i++) {
			returnMap.put(keys[i], values[i]);
		}
		return returnMap;
	}

	/**
	 * 产生一个UUID.去掉"-".
	 * 
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 将字符串拆分成等长的字符数组.
	 * 
	 * @param srcStr
	 * @param preLen
	 * @return
	 */
	public static String[] splitString(String srcStr, int preLen) {
		if (StringUtils.isBlank(srcStr) || preLen < 1) {
			return null;
		}
		int srcStrLen = srcStr.length();
		int arrySize = srcStrLen / preLen;
		if (srcStrLen % preLen > 0) {
			arrySize++;
		}
		String[] targetStrArray = new String[arrySize];
		for (int i = 0; i < arrySize; i++) {
			int startIndex = preLen * i;
			int endIndex = preLen * (i + 1);
			if (endIndex > srcStrLen) {
				endIndex = srcStrLen;
			}
			targetStrArray[i] = srcStr.substring(startIndex, endIndex);
		}
		return targetStrArray;
	}

	/**
	 * 将字节数组拆分成二维数组.
	 * 
	 * @param data
	 * @param preLen
	 * @return
	 */
	public static byte[][] splitArray(byte[] data, int preLen) {
		int srcLen = data.length;
		int arraySize = srcLen / preLen;
		int rears = srcLen % preLen;
		if (rears > 0) {
			arraySize++;
		}
		byte[][] targetArray = new byte[arraySize][];
		for (int i = 0; i < arraySize; i++) {
			int startIndex = preLen * i;
			int endIndex = preLen * (i + 1);
			if (endIndex > srcLen) {
				endIndex = srcLen;
			}
			targetArray[i] = ArrayUtils.subarray(data, startIndex, endIndex);
		}
		return targetArray;
	}
}
