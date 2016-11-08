package com.snail.rmi.kettle;
/**
 * 
 * <p>
 * Title: com.snail.rmi.kettle.SchedulerRmiInterface
 * </p>
 * 
 * <p>
 * Description: Kettle 任务调度引擎RMI接口
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月30日
 * 
 * @version 1.0
 *
 */
public interface SchedulerRmi {
	
	/**
	 * 启动kettle Job (也包括转换)
	 * 
	 * @param jobNo
	 * @return
	 */
	public String startKettleJob(String jobNo);
	
	/**
	 * 停止kettle Job (也包括转换)
	 * 
	 * @param jobNo
	 * @return
	 */
	public String stopKettleJob(String jobNo);

	/**
	 * 向容器中添加job
	 * 
	 * @param fileName 相对路径
	 * @return
	 */
	public String addKettleJob(String fileName);
	
	/**
	 * 从容器中移除job
	 * 
	 * @param jobNo
	 * @return
	 */
	public String removeKettleJob(String jobNo);
	
	/**
	 * 查看kettle Job的状态 (也包括转换)
	 * 
	 * @param jobNo
	 * @return
	 */
	public String getKettleJobStatus(String jobNo);
	
	/**
	 * 获取容器中所有的job信息
	 * 
	 * @return
	 */
	public String getAllKettleJobs();
	
	/**
	 * 查看运行日志.
	 * 
	 * @param jobNo
	 * @return
	 */
	public String getRunLog();
}
