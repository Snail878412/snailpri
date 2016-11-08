package com.snail.mtk.sfcs.test;

import com.snail.file.monitor.jnotify.JnotifyMonitor;
import com.snail.mtk.sfcs.monitor.handler.ExtendTestHandler;
import com.snail.mtk.sfcs.monitor.handler.MasterCleanHandler;

public class Test {

	public static void main(String[] args) {
		JnotifyMonitor monitor = JnotifyMonitor.getInstance();
		MasterCleanHandler masterCleanHander = new MasterCleanHandler();
		masterCleanHander.setFields("TEST_DATE,TIME,PPID,MAC,MODEL_CODE,BMC_VER,BIOS_VER,CPLD_VER,RESULT,SITE");
		
		ExtendTestHandler extendTestHandler = new ExtendTestHandler();
		extendTestHandler.setFields("TEST_DATE,TIME,PPID,MAC,CUSTOMER_VER,CUST_PN,BIOS_VER,SITE,ERROR_CODE,RESULT");
		
		monitor.addWatch("D:/test", masterCleanHander);
		monitor.addWatch("D:/test1",extendTestHandler);
		
		monitor.start();
	}

}
