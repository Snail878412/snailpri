package com.snail.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PickModifiedFileTool {
	private static final String NOTICE_FILES = "[java][xml][jsp][html][css][js][][gif][png][jpg][properties]";

	private static Calendar start_ca = Calendar.getInstance();
	private static String[] dirs = new String[] {
			"E:\\workspace\\work\\SimCSMS", "E:\\workspace\\work\\SnailUtil" };
	private static String[] targetDirs = new String[] {
			"E:\\workspace\\target\\SimCSMS", "E:\\workspace\\target\\SnailUtil" };

	private static String startTime = "2014-05-26 18:20:00";

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static List<String> newFileList = new ArrayList<String>();

	public static void main(String[] args) throws ParseException {
		getNewFile();
	}

	public static void getNewFile() throws ParseException {

		start_ca.setTime(SDF.parse(startTime));
		int i = 0;
		for (String fileDir : dirs) {
			String targetDir = targetDirs[i];
			newFileList.clear();
			File dir = new File(fileDir);
			File[] files = dir.listFiles();
			for (File file : files) {
				getNewFile(file);
			}

			for (String srcPath : newFileList) {
				String targetPath = srcPath.replace(fileDir, targetDir);
				copyFile(srcPath, targetPath);
			}

			i++;
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);

			if (oldfile.exists()) {
				inStream = new FileInputStream(oldPath);
				File newFile = new File(newPath);
				newFile.getParentFile().mkdirs();
				fs = new FileOutputStream(newPath);

				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void getNewFile(File file) {
		if (file.isFile()) {
			if (!isNoticeFile(file)) {
				return;
			}

			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(file.lastModified());

			if (start_ca.before(ca)) {
				newFileList.add(file.getAbsolutePath());
			}
			return;
		}

		File[] files = file.listFiles();
		for (File temp : files) {
			getNewFile(temp);
		}

	}

	private static boolean isNoticeFile(File file) {
		if (file == null) {
			return false;
		}
		String fileName = file.getName();

		if (!fileName.contains(".")) {
			return false;
		}

		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

		return NOTICE_FILES.contains("[" + fileType + "]");
	}
}
