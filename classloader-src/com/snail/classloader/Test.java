package com.snail.classloader;

import java.lang.reflect.Method;

public class Test {
	public static void main(String[] args) throws Exception {
		// 新建一个类加载器
		DecryptClassLoader cl = new DecryptClassLoader();
		// 加载类，得到Class对象
		Class<?> clazz = cl.findClass("com.snail.classloader.test.Animal");
		// 得到类的实例
		Object obj = clazz.newInstance();
		Method method = clazz.getMethod("say");
		method.invoke(obj);
	}
}
