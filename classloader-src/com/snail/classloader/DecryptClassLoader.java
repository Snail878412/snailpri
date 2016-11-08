package com.snail.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.snail.classloader.encrypt.Fileencrypter;
import com.snail.common.SPException;

public class DecryptClassLoader extends ClassLoader {

	@Override
	protected Class<?> findClass(String className)
			throws ClassNotFoundException {
		byte[] data = loadClassData(className);
		Fileencrypter encrypter = new Fileencrypter();
		String key = "llftes";
		String pass1 = key.substring(0, 2);
		String pass2 = key.substring(2, 4);
		String pass3 = key.substring(4);
		try {
			data = new Fileencrypter().decrypt(data, encrypter.md5s(pass1) + encrypter.md5s(pass2) + encrypter.md5s(pass3));
		} catch (SPException e) {
			e.printStackTrace();
		}
		return this.defineClass(className, data, 0, data.length);
	}

	public byte[] loadClassData(String name) {
		FileInputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			name = name.replace(".", "//");
			String path = "E:\\个人资料\\workspace\\ClassLoader\\bin\\";
			is = new FileInputStream(new File(path + name + ".class"));
			baos = new ByteArrayOutputStream();
			int b = 0;
			while ((b = is.read()) != -1) {
				baos.write(b);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
