package com.snail.security.asymmetric;

import java.io.File;
import java.io.FilenameFilter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.log4j.Logger;

import com.snail.common.SPException;
import com.snail.security.SecurityUtil;
import com.snail.util.FileUtil;
import com.snail.util.LogUtil;
import com.snail.util.StringUtils;

/**
 * 
 * <p>
 * Title: com.snail.security.asymmetric.RsaProcessor
 * </p>
 * 
 * <p>
 * Description: RSA处理器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月8日
 * 
 * @version 1.0
 * 
 */
public class RsaProcessor {
	private static Logger log = Logger.getLogger(RsaProcessor.class);

	private static KeyPairGenerator keyPairGen;
	private static KeyFactory keyFactory;
	private RSAPublicKey publicKey;
	private RSAPrivateKey privateKey;
	private static final String keyDir = "E:\\Git_home\\snailelementaryproj\\securityKeys\\";
	private static final String publicKeyFileName = "rsaPublicKey.key";
	private static final String privateKeyFileName = "rsaPrivateKey.key";

	private static RsaProcessor instance;

	public static synchronized RsaProcessor getInstance() {
		if (instance == null) {
			instance = new RsaProcessor();
			try {
				instance.generateKeyPair();
			} catch (SPException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	static {
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.errorLog(log, "KeyPairGenerator 产生失败!", e);
		}
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.errorLog(log, "KeyFactory 产生失败!", e);
		}
	}

	public RsaProcessor() {
	}

	public RsaProcessor(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	/**
	 * 产生密钥对
	 * 
	 * @throws SPException
	 */
	public void generateKeyPair() throws SPException {
		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;
		File keyDirFile = new File(getKeyDir());
		if (keyDirFile != null && keyDirFile.exists()) {
			File[] keyFiles = keyDirFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String fileName) {
					return fileName.equals(getPrivatekeyfilename())
							|| fileName.equals(getPublickeyfilename());
				}
			});
			if (keyFiles != null && keyFiles.length == 2) {
				String fileName1 = keyFiles[0].getAbsolutePath();
				String fileName2 = keyFiles[1].getAbsolutePath();
				if (fileName1.endsWith(getPrivatekeyfilename())) {
					privateKey = loadRSAPrivateKey(fileName1);
					publicKey = loadRSAPublicKey(fileName2);
				} else {
					privateKey = loadRSAPrivateKey(fileName2);
					publicKey = loadRSAPublicKey(fileName1);
				}
			}
		}

		try {
			if(privateKey == null || publicKey == null){
				KeyPair keyPair = keyPairGen.generateKeyPair();
				outPutRSAKey(keyPair);
				publicKey = (RSAPublicKey) keyPair.getPublic();
				privateKey = (RSAPrivateKey) keyPair.getPrivate();
			}
			String modulus = publicKey.getModulus().toString();
			String publicExponent = publicKey.getPublicExponent().toString();
			String privateExponent = privateKey.getPrivateExponent().toString();
			this.publicKey = generatePublicKey(modulus, publicExponent);
			this.privateKey = generatePrivateKey(modulus, privateExponent);
		} catch (Exception e) {
			LogUtil.errorLog(log, "产生密钥对失败!", e);
			throw new SPException("密钥对产生失败!");
		}
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	private RSAPublicKey loadRSAPublicKey(String fileName) {
		Object obj = FileUtil.loadObject(fileName);
		return obj == null ? null : (RSAPublicKey) obj;
	}

	private RSAPrivateKey loadRSAPrivateKey(String fileName) {
		Object obj = FileUtil.loadObject(fileName);
		return obj == null ? null : (RSAPrivateKey) obj;
	}

	private void outPutRSAKey(KeyPair keyPair) {
		FileUtil.outPutFileByObject(getKeyDir() + getPublickeyfilename(),
				keyPair.getPublic());
		FileUtil.outPutFileByObject(getKeyDir() + getPrivatekeyfilename(),
				keyPair.getPrivate());
	}

	/**
	 * 使用模和指数生成RSA公钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public RSAPublicKey generatePublicKey(String modulus, String exponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(
				new BigInteger(modulus), new BigInteger(exponent));
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

	/**
	 * 使用模和指数生成RSA私钥
	 * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
	 * /None/NoPadding】
	 * 
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public RSAPrivateKey generatePrivateKey(String modulus, String exponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(new BigInteger(
				modulus), new BigInteger(exponent));
		return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return 密文
	 * @throws SPException
	 */
	public String encrypt(String data, RSAPublicKey publicKey)
			throws SPException {
		if (publicKey == null) {
			publicKey = getPublicKey();
		}
		if (publicKey == null) {
			throw new SPException("请指定加密密钥!");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return encrypt(data, cipher, publicKey.getModulus().bitLength() / 8);
		} catch (Exception e) {
			LogUtil.errorLog(log, "加密失败!", e);
			throw new SPException("加密失败,详情请参见日志!");
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @return
	 * @throws SPException
	 */
	public String encrypt(String data) {
		try {
			return encrypt(data, getPrivateKey());
		} catch (SPException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return 密文
	 * @throws SPException
	 */
	public String encrypt(String data, RSAPrivateKey privateKey)
			throws SPException {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		if (privateKey == null) {
			throw new SPException("请指定加密密钥!");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return encrypt(data, cipher,
					privateKey.getModulus().bitLength() / 8);
		} catch (Exception e) {
			LogUtil.errorLog(log, "加密失败!", e);
			throw new SPException("加密失败,详情请参见日志!");
		}
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param cipher
	 * @param modulusLen
	 * @return 密文
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String encrypt(String data, Cipher cipher, int modulusLen)
			throws IllegalBlockSizeException, BadPaddingException {
		String[] datas = StringUtils.splitString(data, modulusLen - 11);
		StringBuffer buf = new StringBuffer();
		for (String s : datas) {
			buf.append(SecurityUtil.bcdToStr(cipher.doFinal(s.getBytes())));
		}
		return buf.toString();
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @return
	 * @throws SPException
	 */
	public String decrypt(String data) {
		try {
			return decrypt(data, getPublicKey());
		} catch (SPException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param publicKey
	 * @return 明文
	 * @throws SPException
	 * @throws Exception
	 */
	public String decrypt(String data, RSAPublicKey publicKey)
			throws SPException {
		if (publicKey == null) {
			publicKey = getPublicKey();
		}
		if (publicKey == null) {
			throw new SPException("请指定解密密钥!");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return decrypt(data, cipher, publicKey.getModulus().bitLength() / 8);
		} catch (Exception e) {
			LogUtil.errorLog(log, "解密失败!", e);
			throw new SPException("解密失败,详情请参见日志!");
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return 明文
	 * @throws SPException
	 * @throws Exception
	 */
	public String decrypt(String data, RSAPrivateKey privateKey)
			throws SPException {
		if (privateKey == null) {
			privateKey = getPrivateKey();
		}
		if (privateKey == null) {
			throw new SPException("请指定解密密钥!");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return decrypt(data, cipher,
					privateKey.getModulus().bitLength() / 8);
		} catch (Exception e) {
			LogUtil.errorLog(log, "解密失败!", e);
			throw new SPException("解密失败,详情请参见日志!");
		}
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param cipher
	 * @param modulusLen
	 * @return 密文
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String decrypt(String data, Cipher cipher, int modulusLen)
			throws IllegalBlockSizeException, BadPaddingException {
		byte[] bytes = data.getBytes();
		byte[] bcd = SecurityUtil.ascToBcd(bytes);
		StringBuffer buf = new StringBuffer();
		byte[][] arrays = StringUtils.splitArray(bcd, modulusLen);
		for (byte[] arr : arrays) {
			buf.append(new String(cipher.doFinal(arr)));
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		System.out.println(RsaProcessor.getInstance().decrypt("50CC149BE15BB3AD19FCFD068617E658CBAB8DC5BE0345C4B1D779D17F6030CBA605BF8BDCA989E2FBE8B143AB14675FD3C7C3431D8ED5487209E62F24E77777D63D7189F874258FC76157C027E9CDA3ED1848285EEF24842852BE96CFDF919236FB5390A342F1E13AEF530FDA6E7B88D88DCBB62FF94AEA20FD221A2C724BC9"));
	}
	
	public RSAPublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(RSAPublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(RSAPrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public String getKeyDir() {
		return keyDir;
	}

	public static String getPublickeyfilename() {
		return publicKeyFileName;
	}

	public static String getPrivatekeyfilename() {
		return privateKeyFileName;
	}
}
