package com.snail.rmi.impl;

import org.springframework.stereotype.Component;

import com.snail.rmi.ServerInfoService;

/**
 * 
 * <p>
 * Title: com.snail.rmi.impl.ServerInfoServiceImpl
 * </p>
 * 
 * <p>
 * Description: RMI服务器信息
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月20日
 * 
 * @version 1.0
 *
 */
@Component("serverInfo")
public class ServerInfoServiceImpl extends AbstractRmiService implements ServerInfoService{

	private static final long serialVersionUID = 8409516374845090151L;
	
	public ServerInfoServiceImpl(){
		setServiceName("serverInfo");
	}
	

}
