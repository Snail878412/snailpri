package com.snail.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 * 
 * <p>
 * Title: com.snail.util.ZipXLS
 * </p>
 * 
 * <p>
 * Description: ZIP压缩XLS文件.不是工具类,需要重此包中移除.
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
@Deprecated
public class ZipXLS {
	
	public void ZipFiles(File inFile, String savepath, String comment) {
		try {
			FileOutputStream fout = new FileOutputStream(savepath);
			CheckedOutputStream cs = new CheckedOutputStream(fout,
					new Adler32());
			ZipOutputStream zout = new ZipOutputStream(
					new BufferedOutputStream(cs));
			comment = comment == null ? "This is the report" : comment;
			zout.setComment(comment);
			zip(zout, inFile, "", cs);
			zout.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void zip(ZipOutputStream out, File inFile, String root,
			CheckedOutputStream cs) throws Exception {
		if (inFile.isDirectory()) {
			File[] files = inFile.listFiles();
			out.putNextEntry(new ZipEntry(root + "/"));
			root = root.length() == 0 ? "" : root + "/";
			for (int i = 0; i < files.length; i++) {
				zip(out, files[i], root + files[i].getName(), cs);
			}
		} else {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(inFile));
			out.putNextEntry(new ZipEntry(root));
			int c;
			while ((c = in.read()) != -1)
				out.write(c);
			in.close();
		}
	}

}
