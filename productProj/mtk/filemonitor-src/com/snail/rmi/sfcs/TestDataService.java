package com.snail.rmi.sfcs;

import net.sf.json.JSONArray;

import com.snail.common.SPException;
import com.snail.rmi.RmiService;

/**
 * 
 * <p>
 * Title: com.snail.mtk.sfcs.rmi.TestDataService
 * </p>
 * 
 * <p>
 * Description: 测试数据处理服务接口.
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
