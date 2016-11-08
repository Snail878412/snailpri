package com.snail.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.snail.common.Constants;

/**
 * 
 * <p>
 * Title: com.snail.util.ReturnUtil
 * </p>
 * 
 * <p>
 * Description: 返回内容工具类.
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
public class ReturnUtil {

	/**
	 * 创建一个返回Map.Map中包括三个键值对{<data,"">,<success,false>,<errorMsg,"">}
	 * @return 
	 */
	public static Map<String, Object> createReturnMap() {
		return resetReturnMap(null);
	}

	/**
	 * 重置或创建一个返回Map.Map中包括三个键值对{<data,"">,<success,false>,<errorMsg,"">}
	 * @param map
	 * @return
	 */
	public static Map<String, Object> resetReturnMap(Map<String, Object> map) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put(Constants.RESULT_MAP_DATA, "");
		map.put(Constants.RESULT_MAP_RESULT_FLAG, false);
		map.put(Constants.RESULT_MAP_ERROR_MESSAGES, "");
		return map;
	}

	/**
	 * 创建一个返回成功Map.Map中包括三个键值对{<data,data>,<success,true>,<errorMsg,"">}
	 * @param map
	 * @param data
	 * @return
	 */
	public static Map<String, Object> getSuccessReturnMap(
			Map<String, Object> map, Object data) {
		if (map == null) {
			map = resetReturnMap(null);
		}
		map.put(Constants.RESULT_MAP_RESULT_FLAG, true);
		map.put(Constants.RESULT_MAP_DATA, data);
		return map;
	}

	/**
	 * 创建一个返回失败Map.Map中包括三个键值对{<data,"">,<success,false>,<errorMsg,errorMsg>}
	 * @param map
	 * @param errorMsg
	 * @return
	 */
	public static Map<String, Object> getFailReturnMap(Map<String, Object> map,
			String errorMsg) {
		map = resetReturnMap(map);
		map.put(Constants.RESULT_MAP_ERROR_MESSAGES, errorMsg);
		return map;
	}

	/**
	 * 创建一个返回成功Map.Map中包括三个键值对{<data,data>,<success,true>,<errorMsg,"">}
	 * @param data
	 * @return
	 */
	public static Map<String, Object> getSuccessReturnMap(Object data) {
		Map<String, Object> map = resetReturnMap(null);
		map.put(Constants.RESULT_MAP_RESULT_FLAG, true);
		map.put(Constants.RESULT_MAP_DATA, data);
		return map;
	}

	/**
	 * 创建一个返回失败Map.Map中包括三个键值对{<data,"">,<success,false>,<errorMsg,errorMsg>}
	 * @param errorMsg
	 * @return
	 */
	public static Map<String, Object> getFailReturnMap(String errorMsg) {
		Map<String, Object> map = resetReturnMap(null);
		map.put(Constants.RESULT_MAP_ERROR_MESSAGES, errorMsg);
		return map;
	}

	/**
	 * 创建一个返回失败JSON字符串.形如:{data:"",success:false,errorMsg:errorMsg}
	 * @param errorMsg
	 * @return
	 */
	public static String getFailReturnStr(String errorMsg) {
		return JSONObject.fromObject(getFailReturnMap(errorMsg)).toString();
	}

	/**
	 * 创建一个返回成功JSON字符串.形如:{data:dataJson,success:true,errorMsg:""}
	 * @param data
	 * @return
	 */
	public static String getSuccessReturnStr(Object data) {
		return JSONObject.fromObject(getSuccessReturnMap(data)).toString();
	}
	/**
	 * 校验list中是否有有效数据.即存在不为空或null的元素.
	 * @param data
	 * @return
	 */
	@Deprecated
	public static boolean listDataValidness(List<?> data) {
		if (data == null || data.isEmpty()) {
			return false;
		}
		for (Object obj : data) {
			if ((obj instanceof String && StringUtils
					.isNotEmpty(obj.toString()))
					|| (!(obj instanceof String) && obj != null)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验list中指定的元素是否非空.
	 * @param data
	 * @param index
	 * @return
	 */
	@Deprecated
	public static boolean listDataValidness(List<?> data, int index) {
		if (data == null || data.isEmpty()) {
			return false;
		}
		if (data.size() + 1 < index) {
			return false;
		}
		Object obj = data.get(index);
		if ((obj instanceof String && StringUtils.isNotEmpty(obj.toString()))
				|| (!(obj instanceof String) && obj != null)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断指定的map是否是成功的map
	 * 
	 * @param map
	 * @return map != null && !map.isEmpty() && map.containsKey("success") &&
	 *         (Boolean) map.get("success")
	 */
	public static boolean isSuccess(Map<String, Object> map) {
		if (map == null || map.isEmpty()
				|| !map.containsKey(Constants.RESULT_MAP_RESULT_FLAG)) {
			return false;
		}
		return (Boolean) map.get(Constants.RESULT_MAP_RESULT_FLAG);
	}

	/**
	 * 取出成功MAP中的data的内容,若不是成功的map则返回null
	 * @param map
	 * @return
	 */
	public static Object getReturnData(Map<String, Object> map) {
		if (map == null || !isSuccess(map)
				|| !map.containsKey(Constants.RESULT_MAP_DATA)) {
			return null;
		}
		return map.get(Constants.RESULT_MAP_DATA);
	}
}
