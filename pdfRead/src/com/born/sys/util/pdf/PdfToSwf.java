package com.born.sys.util.pdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <ul>
 * <li>文件名称: com.born.sys.util.pdf.PdfToSwf.java</li>
 * <li>文件描述: pdf生成swf</li>
 * <li>版权所有: 版权所有(C)2001-2006</li>
 * <li>公 司: born</li>
 * <li>内容摘要:</li>
 * <li>其他说明:</li>
 * <li>完成日期：2010-5-21</li>
 * <li>修改记录0：无</li>
 * </ul>
 * 
 * @version 1.0
 * @author 许力多
 */
public class PdfToSwf {
	public int convertPDF2SWF(String sourcePath, String destPath, String fileName) throws IOException {
		// 目标路径不存在则建立目标路径
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest.mkdirs();
		}

		// 源文件不存在则返回
		File source = new File(sourcePath);
		if (!source.exists()) {
			return 0;
		}

		// 调用pdf2swf命令进行转换
		// D:\tools\SWFTools>pdf2swf.exe -z -B rfxview.swf -s flashversion=9
		// d:/人员管理系
		// 统PersonalManagementSystem简介.pdf -o d:/test.swf

		// 要把D:\\tools\\SWFTools\\放在path里面……不然使用不了播放器

		// 先生成flash
		String[] envp = new String[1];
		envp[0] = "PATH=C:\\SWFTools\\";//C:\Program Files (x86)\SWFTools    D:\\tools\\SWFTools\\
		String command = " C:\\SWFTools\\pdf2swf -z -s flashversion=9 \"" + sourcePath + "\" -o \"" + destPath + fileName + "\"";

		Process pro = Runtime.getRuntime().exec(command, envp);
		// System.out.println(command);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		while (bufferedReader.readLine() != null) {
			String text = bufferedReader.readLine();
			System.out.println(text);
		}
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 然后在套播放器
		/*
		 * swfcombine -z -X 720 -Y 540 "D:\tools\SWFTools\swfs\rfxview.swf"
		 * viewport="d:/人
		 * 员管理系统PersonalManagementSystem简介.swf" -o "d:/人员管理系统PersonalManagemen
		 * tSystem简介.swf"
		 */
		command = "C:\\SWFTools\\swfcombine -z -X 720 -Y 540 \"D:/tools/SWFTools/swfs/rfxview.swf\" viewport=\"" + destPath + fileName + "\" -o \"" + destPath + fileName + "\"";
		pro = Runtime.getRuntime().exec(command, envp);
		System.out.println(command);
		bufferedReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		while (bufferedReader.readLine() != null) {
			String text = bufferedReader.readLine();
			System.out.println(text);
		}
		try {
			pro.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pro.exitValue();

	}

	public static void main(String[] args) {
		String sourcePath = "D:/office/2.pdf";
		String destPath = "D:/office/";
		String fileName = "2.swf";
		try {
			System.out.println(new PdfToSwf().convertPDF2SWF(sourcePath, destPath, fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
