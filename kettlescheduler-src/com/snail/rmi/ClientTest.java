package com.snail.rmi;

import com.snail.rmi.kettle.SchedulerRmi;

public class ClientTest {

	public static void main(String[] args) {
		RmiClientContext client = new RmiClientContext();
		SchedulerRmi schedulerRmi = client.getRmiService("schedulerRmi",
				SchedulerRmi.class);
		System.out.println(schedulerRmi.getAllKettleJobs());
	}
}
