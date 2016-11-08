package com.snail.rmi.impl.sfcs;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import com.snail.rmi.impl.AbstractRmiService;
import com.snail.rmi.sfcs.CeiFj12ThtLogService;
import com.snail.rmi.sfcs.dao.CeiFj12ThtDao;

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
@Component("ceiFj12ThtLogService")
public class CeiFj12ThtLogServiceImpl extends AbstractRmiService implements
		CeiFj12ThtLogService {

	private static final long serialVersionUID = 1424862759183765002L;

	public CeiFj12ThtLogServiceImpl() {
		setServiceName("ceiFj12ThtLogService");
	}

	@Override
	public boolean saveThtData(JSONObject jsonData) throws Exception {
		return CeiFj12ThtDao.insertToCeiFj12ThtData(jsonData);
	}

}
