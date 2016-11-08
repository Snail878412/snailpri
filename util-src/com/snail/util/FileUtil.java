package com.snail.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import com.snail.common.SPException;

/**
 * 
 * <p>
 * Title: com.snail.util.FileUtil
 * </p>
 * 
 * <p>
 * Description: 文件处理工具类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @author Snail
 * @date 2014年8月12日
 * 
 * @version 1.0
 * 
 */
public class FileUtil {

	private static final Logger log = Logger.getLogger(FileUtil.class);

	private static final String SEPARATOR = File.separator;

	/**
	 * 解压
	 * 
	 * @param file
	 * @param outPutDirPath
	 * @return
	 * @throws Exception
	 */
	public static File decompressFile(File file, String outPutDirPath)
			throws Exception {
		if (StringUtils.isBlank(outPutDirPath)) {
			throw new SPException("输出路径不能为空!");
		}
		if (file == null || !file.canRead()) {
			throw new SPException("待解压文件为空或不可读!");
		}
		File outPutDir = new File(outPutDirPath);
		if (outPutDir != null && !outPutDir.exists()) {
			outPutDir.mkdir();
		}

		if (!outPutDir.isDirectory() || !outPutDir.canWrite()) {
			throw new SPException("输出路径无效(不是目录或不可写)!");
		}

		String name = file.getName();
		String fileType = name.substring(name.lastIndexOf(".") + 1);
		if ("rar".equalsIgnoreCase(fileType)) {
			return decompressRarFile(file, outPutDirPath);
		} else if ("zip".equalsIgnoreCase(fileType)) {
			return decompressZipFile(file, outPutDirPath);
		} else {
			throw new Exception("不是有效的压缩文件(rar/zip)!");
		}
	}

	/**
	 * ZIP 解压
	 * 
	 * @param file
	 * @param outPutDir
	 * @return
	 */
	private static File decompressZipFile(File file, String outPutDirPath)
			throws Exception {
		ZipInputStream zipInputStream = null;
		OutputStream os = null;
		InputStream is = null;
		ZipFile zipFile = new ZipFile(file);
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(file));
			ZipEntry zipEntry = null;
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				String fileName = zipEntry.getName();
				File temp = new File(outPutDirPath + SEPARATOR + fileName);
				if (!temp.getParentFile().exists()) {
					temp.getParentFile().mkdirs();
				}
				os = new FileOutputStream(temp);
				is = zipFile.getInputStream(zipEntry);
				int len = 0;
				while ((len = is.read()) != -1) {
					os.write(len);
				}
				os.close();
				is.close();
			}
			zipInputStream.close();
			return new File(outPutDirPath);
		} catch (Exception e) {
			LogUtil.errorLog(log, "解压文件异常", e);
			throw new SPException("文件解压失败!");
		} finally {
			try {
				if (os != null) {
					os.close();
					os = null;
				}
			} catch (IOException e) {
			}

			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
			}

			try {
				if (zipInputStream != null) {
					zipInputStream.close();
					zipInputStream = null;
				}
			} catch (IOException e) {
			}
			if (zipFile != null) {
				zipFile.close();
				zipFile = null;
			}
		}
	}

	/**
	 * RAR 解压
	 * 
	 * @param file
	 * @param outPutDir
	 * @return
	 */
	private static File decompressRarFile(File file, String outPutDirPath)
			throws Exception {
		throw new Exception("暂不支持RAR文件解压");
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 * @throws Exception
	 */
	public static boolean copyFile(File sourceFile, File targetFile)
			throws Exception {
		if (sourceFile == null || targetFile == null) {
			return false;
		}

		if (!sourceFile.canRead() || !targetFile.getParentFile().canWrite()) {
			throw new SPException("源文件不可读或目标文件不可写!");
		}

		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(log, "文件复制失败", e);
			throw new SPException("文件复制失败!");
		} finally {
			// 关闭流
			try {
				if (inBuff != null) {
					inBuff.close();
				}
			} catch (IOException e) {
			}
			try {
				if (outBuff != null) {
					outBuff.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 将对象持久化到本地.
	 * 
	 * @param fileName
	 * @param obj
	 */
	public static boolean outPutFileByObject(String fileName, Object obj) {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fileName));
			oos.writeObject(obj);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 将对象从本地加载.
	 * 
	 * @param fileName
	 * @param obj
	 */
	public static Object loadObject(String fileName) {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileName));
			Object obj = ois.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
