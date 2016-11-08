package com.snail.rmi;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * 
 * <p>
 * Title: com.snail.rmi.RmiService
 * </p>
 * 
 * <p>
 * Description: RMI基础接口
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
public interface RmiService extends Remote, Serializable {
	/**
	 * 返回服务器的状态.
	 * @return
	 * @throws Exception
	 */
	public String getStatus() throws Exception;
}
