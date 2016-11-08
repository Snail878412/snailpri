package com.snail.rmi.impl.sfcs;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Component;

import com.snail.rmi.impl.AbstractRmiService;
import com.snail.rmi.sfcs.TestDataService;
import com.snail.rmi.sfcs.dao.DaoUtil;

/**
 * 
 * <p>
 * Title: com.snail.mtk.sfcs.rmi.impl.TestDataServiceImpl
 * </p>
 * 
 * <p>
 * Description:
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
@Component("testDataService")
public class TestDataServiceImpl extends AbstractRmiService implements
		TestDataService {

	private static final long serialVersionUID = 8916927967176119392L;

	public TestDataServiceImpl() {
		setServiceName("testDataService");
	}

	@Override
	public boolean saveTestData(JSONArray testData) throws Exception {
		DaoUtil.saveToPeDataExt(testData);
		return true;
	}

}
