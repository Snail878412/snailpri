package com.snail.common.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * <p>
 * Title: com.snail.common.log4j.Log4jEvaluator
 * </p>
 * 
 * <p>
 * Description: 自定义触发器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年9月10日
 * 
 * @version 1.0
 *
 */
public class Log4jEvaluator implements TriggeringEventEvaluator{

	@Override
	public boolean isTriggeringEvent(LoggingEvent event) {
		return false;
	}

}
