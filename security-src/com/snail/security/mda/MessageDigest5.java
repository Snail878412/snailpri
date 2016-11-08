package com.snail.security.mda;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.snail.security.SecurityUtil;
import com.snail.util.LogUtil;

/**
 * 
 * <p>
 * Title: com.snail.security.mda.MessageDigest
 * </p>
 * 
 * <p>
 * Description MD5
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014-9-9
 * 
 * @version 1.0
 * 
 */
public class MessageDigest5 {

	private static Logger log = LogUtil.getLogger(MessageDigest5.class);

	private static MessageDigest md5;
	private static MessageDigest5 instance;

	private MessageDigest5() {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5算法初始化失败!", e);
		}
	}

	public synchronized static MessageDigest5 getInstance() {
		if (instance == null) {
			instance = new MessageDigest5();
		}
		return instance;
	}

	/**
	 * 加密
	 * 
	 * @param srcStr
	 * @return
	 */
	public String encrypt(String srcStr) {
		return SecurityUtil.byte2hex(md5.digest(srcStr.getBytes()));
	}

	/**
	 * 生成文件的md5校验值
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String getFileMD5String(File file) throws IOException {
		InputStream fis;
		fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = fis.read(buffer)) > 0) {
			md5.update(buffer, 0, numRead);
		}
		fis.close();
		return SecurityUtil.byte2hex(md5.digest());
	}
	
	/**
	 * 密码是否匹配
	 * 
	 * @param srcStr
	 * @param cipher
	 * @return
	 */
	public boolean match(String srcStr, String cipher) {
		return cipher.equals(encrypt(srcStr));
	}
}
