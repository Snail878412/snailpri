package com.snail.pdf.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;

import com.snail.pdf.panel.PagePanel;
import com.snail.pdf.sun.PDFFile;
import com.snail.pdf.sun.PDFPage;
import com.sun.pdfview.PageChangeListener;

public class PdfSimpleViewer extends JFrame implements PageChangeListener{

	private static final long serialVersionUID = -9051838539658947870L;
	private PagePanel pagePanel;
	private JScrollPane scrollPane;
	private JToolBar toolBar;
	private JTextField curPage;
	private JLabel pageCount;
	private JLabel curPageLabel;
	private JButton nextBtn;
	private JButton prevBtn;
	private int width = 600;
	private int height = 800;
	
	private PDFFile pdfFile;
	private int totalPages = 0;
	private int curPages = 0;
	
	public static void main(String[] args) throws Exception {
		new PdfSimpleViewer().setPdfFile("D:/h2.pdf");;
	}
	
	public PdfSimpleViewer(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		pagePanel = new PagePanel();
		scrollPane = new JScrollPane(pagePanel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		toolBar = new JToolBar();
		toolBar.setEnabled(false);
		toolBar.setLayout(new FlowLayout());
		nextBtn = new JButton("下一页");
		prevBtn = new JButton("上一页");
		prevBtn.setEnabled(false);
		nextBtn.setEnabled(false);
		curPage = new JTextField("0");
		curPage.setEditable(false);
		curPage.setEnabled(false);
		curPage.setPreferredSize(new Dimension(50, 20));
		pageCount = new JLabel(" 共:0页");
		curPageLabel = new JLabel(" 跳转:");
		toolBar.add(prevBtn);
		toolBar.add(nextBtn);
		toolBar.add(curPageLabel);
		toolBar.add(curPage);
		toolBar.add(pageCount);
		this.add(scrollPane,BorderLayout.CENTER);
		this.add(toolBar,BorderLayout.SOUTH);
		custInit();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public  PdfSimpleViewer(PDFFile pdfFile){
		this();
		setPdfFile(pdfFile);
	}
	
	private void showPdfFile(){
		if(this.pdfFile == null){
			return;
		}
		if(pdfFile.getNumPages() < 1){
			return;
		}
		setTotalPages(pdfFile.getNumPages());
		gotoPage(1);
	}
	
	private void custInit(){
		this.nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				nextPage();
			}
		});
		this.prevBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				prevPage();
			}
		});
		this.curPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String page = curPage.getText();
				if(StringUtils.isNumeric(page)){
					gotoPage(Integer.parseInt(page));
				}
			}
		});
	}
	
	private PDFFile getPdfFile(String fileName) throws Exception{
		PDFFile pdffile = null;
		RandomAccessFile raf = null;
		try {
			File file = new File(fileName);
			if(!file.exists() || !file.isFile()){
				throw new Exception("文件不存在或不是文件!");
			}
			raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			pdffile = new PDFFile(buf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(raf != null){
				raf.close();
			}
		}
		return pdffile;
	}

	@Override
	public void gotoPage(int num) {
		if(num < 0){
			num = 0;
		}
		if(num > getTotalPages()){
			return;
		}
		this.setCurPages(num);
		if(num<1){
			return;
		}
		PDFPage pdfPage = pdfFile.getPage(num);
		pagePanel.setPreferredSize(getUnstretchedSize(pdfPage));
		this.scrollPane.getViewport().add(pagePanel,null);
		pagePanel.showPage(pdfPage);
	}

	private Dimension getUnstretchedSize(PDFPage pg) {
        final double srcWidth = this.scrollPane.getSize().getWidth();
        final double srcHeight = this.scrollPane.getSize().getHeight();
        float pgWidth = pg.getWidth();
        float pgHeight = pg.getHeight();
        double ratio = srcHeight / pgHeight;
        double askratio = srcWidth / pgWidth;
        int width = 0;
		int height = 0;
        if (askratio > ratio) {
        	width = (int) (pgWidth * askratio);
        	height = (int) (pgHeight * askratio);
        } else {
        	width = (int) (pgWidth * ratio);
        	height = (int) (pgHeight * ratio);
        }
		return new Dimension(width-20, height);
    }
	
	private void nextPage(){
		if(this.pdfFile == null ){
			return ;
		}
		if(getCurPages() >= getTotalPages()){
			return;
		}
		gotoPage(getCurPages()+1);
	}
	
	private void prevPage(){
		if(this.pdfFile == null ){
			return ;
		}
		if(getCurPages() <= 0){
			return;
		}
		gotoPage(getCurPages() - 1);
	}
	
	public PDFFile getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(PDFFile pdfFile) {
		this.pdfFile = pdfFile;
		showPdfFile();
	}

	public void setPdfFile(String fileName) throws Exception{
		if(StringUtils.isBlank(fileName)){
			throw new Exception("文件名不能为空!");
		}
		if(!fileName.endsWith("pdf")){
			throw new Exception("不是pdf文件!");
		}
		setPdfFile(getPdfFile(fileName));
	}
	
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		this.pageCount.setText("共 "+this.totalPages+" 页");
	}

	public int getCurPages() {
		return curPages;
	}

	public void setCurPages(int curPages) {
		this.curPages = curPages;
		this.curPage.setEditable(curPages >0);
		this.curPage.setEnabled(curPages >0);
		this.curPage.setText(String.valueOf(this.curPages));
		this.prevBtn.setEnabled(curPages>1);
		this.nextBtn.setEnabled(curPages>0 && curPages < getTotalPages());
	}

}
