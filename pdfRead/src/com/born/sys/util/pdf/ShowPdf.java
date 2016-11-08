package com.born.sys.util.pdf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import example.PagePanel;

public class ShowPdf {

	public static void setup() throws IOException {

		// set up the frame and panel
		JFrame frame = new JFrame("PDF Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PagePanel panel = new PagePanel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		// load a pdf from a byte buffer
		File file = new File("D:/office/4.pdf");
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);
		// show the first page
		PDFPage page = pdffile.getPage(1);
		panel.showPage(page);
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowPdf.setup();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}
