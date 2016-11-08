package com.snail.rmi.impl.sfcs;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.snail.rmi.impl.AbstractRmiService;
import com.snail.rmi.sfcs.EciSonyIdpLogService;
import com.snail.rmi.sfcs.dao.EciSonyIdpDao;

/**
 * 
 * <p>
 * Title: com.snail.rmi.impl.sfcs.EciSonyIdpLogServiceImpl
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
@Component("eciSonyIdpLogService")
public class EciSonyIdpLogServiceImpl extends AbstractRmiService implements
		EciSonyIdpLogService {

	private static final long serialVersionUID = 1424862759183765002L;

	public EciSonyIdpLogServiceImpl() {
		setServiceName("eciSonyIdpLogService");
	}

	@Override
	public boolean saveIdpData(JSONObject jsonData) throws Exception {
		return EciSonyIdpDao.insertToEciSonyIdpData(jsonData);
	}

}
