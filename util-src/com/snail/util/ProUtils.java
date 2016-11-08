package com.snail.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
/**
 * 
 * <p>
 * Title: com.snail.util.ProUtils
 * </p>
 * 
 * <p>
 * Description: 属性文件工具类.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月13日
 * 
 * @version 1.0
 *
 */
public class ProUtils {

	public static final Logger log = Logger.getLogger(ProUtils.class);

	public static String custPropertiesPath = "/config/custProperties.properties";

	public static Properties custProperties = new Properties();

	static {
		try {
			custProperties.load(ProUtils.class
					.getResourceAsStream(custPropertiesPath));
		} catch (Exception e) {
			log.error("load custPropertiesPERTIES fial !");
		}
	}

	/**
	 * 返回属性文件Properties. 属性文件默认为:/config/custProperties.properties
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Properties getPro() throws IOException {
		if (custProperties == null) {
			custProperties = createProperties(custPropertiesPath);
		}
		if (custProperties.isEmpty()) {
			custProperties.load(ProUtils.class
					.getResourceAsStream(custPropertiesPath));
		}
		return custProperties;
	}

	/**
	 * 重新加载属性文件.属性文件默认为:/config/custProperties.properties
	 * @throws IOException
	 */
	public static void updatePro() throws IOException {
		if (custProperties == null) {
			custProperties = new Properties();
		}
		custProperties.load(ProUtils.class
				.getResourceAsStream(custPropertiesPath));
	}

	/**
	 * 查询属性文件的内容.属性文件默认为:/config/custProperties.properties
	 * @param key
	 * @return
	 */
	public static String getPropertyValue(String key) {
		return custProperties.getProperty(key);
	}

	/**
	 * 构建Properties
	 * @param propertiesFileName
	 * @param inClassPath 文件名是否是CLASSPATH下的文件.
	 * @return
	 * @throws Exception
	 */
	public static Properties createProperties(String propertiesFileName,boolean inClassPath) throws IOException{
		Properties properties = new Properties();
		if(inClassPath){
			properties.load(ProUtils.class
					.getResourceAsStream(propertiesFileName));
		}else{
			properties.load(new FileInputStream(propertiesFileName));
		}
		return properties;
	}
	
	/**
	 * 使用CLASSPATH下的文件创建Properties
	 * 
	 * @param propertiesFileName
	 * @return
	 * @throws Exception
	 */
	public static Properties createProperties(String propertiesFileName) throws IOException{
		return createProperties(propertiesFileName, true);
	}
	
}
