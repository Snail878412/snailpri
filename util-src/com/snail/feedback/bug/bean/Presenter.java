package com.snail.feedback.bug.bean;

import com.snail.util.StringUtils;

/**
 * bug提出人
 * 
 * @author Snail
 * 
 */
public class Presenter extends Contact {
	public Presenter() {
		super();
		setNo(StringUtils.getUuid());
	}
}
