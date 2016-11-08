package com.snail.security;

import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.security.SecurityUtil
 * </p>
 * 
 * <p>
 * Description 安全类工具类.
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
public class SecurityUtil {
	
	private static int byteNum = 2; 

	/**
	 * 将字节数组转换为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuffer strBuf = new StringBuffer();
		for (int n = 0; n < bytes.length; n++) {
			strBuf.append(StringUtils.leftPad(Integer.toHexString(bytes[n] & 0XFF), 2, "0"));
		}
		return strBuf.toString();
	}
	
	/**
	 * ASCII码转BCD码
	 * 
	 * @param ascii
	 * @param asc_len
	 * @return
	 */
	public static byte[] ascToBcd(byte[] ascii) {
		int asc_len = ascii.length;
		byte[] bcd = new byte[asc_len / byteNum];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / byteNum; i++) {
			for (int count = 0; count < byteNum; count++) {
				if (j >= asc_len) {
					bcd[i] = (byte) ((0x00) + (bcd[i] << 4));
				} else {
					bcd[i] = (byte) (ascToBcd(ascii[j++]) + (bcd[i] << 4));
				}
			}
		}
		return bcd;
	}

	public static byte ascToBcd(byte asc) {
		byte bcd;
		if ((asc >= '0') && (asc <= '9')) {
			bcd = (byte) (asc - '0');
		} else if ((asc >= 'A') && (asc <= 'F')) {
			bcd = (byte) (asc - 'A' + 10);
		} else if ((asc >= 'a') && (asc <= 'f')) {
			bcd = (byte) (asc - 'a' + 10);
		} else {
			bcd = (byte) (asc - 48);
		}
		return bcd;
	}

	/**
	 * BCD转字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bcdToStr(byte[] bytes) {
		char temp[] = new char[bytes.length * byteNum], val;
		for (int i = 0; i < bytes.length; i++) {
			for (int j = 0; j < byteNum; j++) {
				val = (char) (((bytes[i] & (0xf << (4 * (byteNum - j - 1)))) >> (4 * (byteNum
						- j - 1))) & 0x0f);
				temp[i * byteNum + j] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			}
		}
		return new String(temp);
	}
}
