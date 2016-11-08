package com.snail.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.snail.common.Constants;

/**
 * 
 * <p>
 * Title: com.snail.util.AuthUtil
 * </p>
 * 
 * <p>
 * Description: 安全工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月12日
 * @deprecated
 * @version 1.0
 *
 */
public class AuthUtil {

	public static String getUserDefualtPwd() {
		return Constants.USER_DEFUAL_PWD;
	}

	/**
	 * MD5加密
	 * 
	 * @param userPwd
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getEncryptPwd(String userPwd)
			throws NoSuchAlgorithmException {
		String encryptPwd = userPwd;
		MessageDigest alg = MessageDigest.getInstance("MD5");
		encryptPwd = byte2hex(alg.digest(userPwd.getBytes()));
		return encryptPwd;
	}

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	public static void main(String[] args) {
		try {
			System.out.println(getEncryptPwd("123456"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
