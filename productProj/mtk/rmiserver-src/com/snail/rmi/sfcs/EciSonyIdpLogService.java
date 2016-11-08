package com.snail.rmi.sfcs;

import net.sf.json.JSONObject;

import com.snail.rmi.RmiService;

/**
 * 
 * <p>
 * Title: com.snail.rmi.sfcs.EciSonyIdpLogService
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
public interface EciSonyIdpLogService extends RmiService {

	public boolean saveIdpData(JSONObject jsonData) throws Exception;

}
