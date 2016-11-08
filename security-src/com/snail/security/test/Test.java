package com.snail.security.test;

import org.apache.log4j.Logger;

import com.snail.util.LogUtil;

public class Test {
	private static final Logger log = Test1.getLogger();
	private static final Logger log1 = Logger.getLogger(Test.class);
	
	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getContextClassLoader().getClass().getName());
		test();
	}
	
	public static void test(){
		log.debug("test");
		log1.debug("test");
		StackTraceElement[] temp = Thread.currentThread().getStackTrace();
		StackTraceElement a = (StackTraceElement) temp[temp.length-1];
		System.out.println(a.getLineNumber());
	}
	
}

class Test1{
	public static Logger getLogger(){
		return LogUtil.getLogger();
	}
}