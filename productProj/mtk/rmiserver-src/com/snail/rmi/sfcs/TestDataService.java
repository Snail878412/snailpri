package com.snail.rmi.sfcs;

import net.sf.json.JSONArray;

import com.snail.common.SPException;
import com.snail.rmi.RmiService;

/**
 * 
 * <p>
 * Title: com.snail.rmi.sfcs.TestDataService
 * </p>
 * 
 * <p>
 * Description 
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014-12-28
 * 
 * @version 1.0
 *
 */
public interface TestDataService extends RmiService {
	/**
	 * 将测试数据保存起来.
	 * 
	 * @param testData
	 * @return 保存成功与否 true:保存成功,false:保存失败
	 * @throws SPException
	 */
	public boolean saveTestData(JSONArray testData) throws Exception;
}
