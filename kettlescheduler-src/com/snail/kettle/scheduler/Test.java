package com.snail.kettle.scheduler;

import java.io.File;
import java.util.Properties;

import org.pentaho.di.core.exception.KettleException;

import com.snail.kettle.scheduler.beans.KettleJob;


public class Test {
	public static void main(String[] args) throws Exception {
		new Thread("test") {
			public void run() {
				while (true) {
					try {
						Properties kettleJobs = NativeEngine.getInstance()
								.getKettleJobs();
						for (Object key : kettleJobs.keySet()) {
							Object obj = kettleJobs.get(key);
							if (obj != null && obj instanceof KettleJob) {
								KettleJob kettleJob = (KettleJob) obj;
								System.out.println(kettleJob.getJobNo()
										+ ","
										+ kettleJob.getJobName()
										+ ","
										+ kettleJob.getStatus()
										+ ","
										+ kettleJob
												.getFileName()
												.replace(
														NativeEngine
																.getInstance()
																.getJobHome(),
														File.separator)
												.replace(File.separator, "/"));
							}
						}
					} catch (KettleException e) {
						e.printStackTrace();
					}
					try {
						sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();

		// NativeEngine.getInstance().add("kjb.xml");
		// NativeEngine.getInstance().add("Test.ktr");
		// NativeEngine.getInstance().startAll();

	}
}
