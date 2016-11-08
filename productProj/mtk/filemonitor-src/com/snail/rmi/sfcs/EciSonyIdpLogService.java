package com.snail.rmi.sfcs;

import net.sf.json.JSONObject;

import com.snail.rmi.RmiService;

public interface EciSonyIdpLogService extends RmiService {

	public boolean saveIdpData(JSONObject jsonData) throws Exception ;

}
