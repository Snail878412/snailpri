package com.snail.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Test {

	private static final String softPath = "D:\\Program\\Adobe\\Reader 11.0\\Reader\\AcroRd32.exe";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		File file = new File("\\10.87.35.21\\共享\\1.pdf");
//		System.out.println(file == null);
//		System.out.println(file.exists());
//		 openFile("D:\\1.pdf", softPath);
//		 openFile("D:\\h2.pdf", softPath);
//		 System.out.println("====");
//		System.out.println(Pattern.matches("^0*|f*|F*$", "FFFF1F"));
		System.out.println(30/40);
	}

	private static void openFile(String fileName, String openSoftPath) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(new File(fileName));
		} catch (IOException e) {
			try {
				Runtime.getRuntime().exec(openSoftPath + " " + fileName);
			} catch (IOException e1) {
				e.printStackTrace();
			}
		}
	}

}
