package com.snail.util;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 * <p>
 * Title: com.snail.util.XMLUtil
 * </p>
 * 
 * <p>
 * Description: XML工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年7月28日
 * 
 * @version 1.0
 * 
 */
public class XmlUtil {

	/**
	 * 从输入流程读取Document
	 * @param in
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(InputStream in) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(in);
	}

	/**
	 * 从输入流程读取根节点
	 * @param in
	 * @return
	 * @throws DocumentException
	 */
	public static Element getRootElement(InputStream in)
			throws DocumentException {
		return getDocument(in).getRootElement();
	}
}
