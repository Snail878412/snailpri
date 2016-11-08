/**
 * 
 */
package com.born.sys.util.pdf;

import java.io.File;
import java.net.ConnectException;
import java.util.Date;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * <ul>
 * <li>文件名称: com.born.sys.util.pdf.JOD4DocToPDF.java</li>
 * <li>文件描述:</li>
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
public class JOD4DocToPDF extends java.lang.Thread {
	private File inputFile;// 需要转换的文件
	private File outputFile;// 输出的文件

	public JOD4DocToPDF(File inputFile, File outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void docToPdf() {
		Date start = new Date();
		// connect to an OpenOffice.org instance running on port 8100
		OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
		try {
			connection.connect();

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputFile, outputFile);
		} catch (ConnectException cex) {
			cex.printStackTrace();
		} finally {
			// close the connection
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
		long l = (start.getTime() - new Date().getTime());
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		System.out.println("生成" + outputFile.getName() + "耗费：" + min + "分" + s + "秒");
	}

	/**
	 * 由于服务是线程不安全的，所以……需要启动线程
	 */
	public void run() {
		this.docToPdf();

	}

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JOD4DocToPDF tools = new JOD4DocToPDF(new File("D:/office/7.docx"), new File("D:/office/7.pdf"));
		tools.start();

	}

}
