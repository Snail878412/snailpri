package com.snail.rmi.sfcs;

import net.sf.json.JSONObject;

import com.snail.rmi.RmiService;

/**
 * 
 * <p>
 * Title: com.snail.rmi.sfcs.EciFj12ThtLogService
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
public interface CeiFj12ThtLogService extends RmiService {

	public boolean saveThtData(JSONObject jsonData) throws Exception;

}
