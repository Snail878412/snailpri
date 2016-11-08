package com.born.sys.util.pdf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;

import com.born.sys.util.pdf.MyPDFViewer.Operator;
import com.sun.pdfview.OutlineNode;
import com.sun.pdfview.PDFDestination;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFObject;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.action.GoToAction;
import com.sun.pdfview.action.PDFAction;

import example.Flag;
import example.FullScreenWindow;
import example.PDFPrintPage;
import example.PageChangeListener;
import example.PagePanel;
import example.ThumbPanel;
import javax.swing.JToolBar;
import javax.swing.JButton;


public class JPdfPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String PREFERRED_LOOK_AND_FEEL = null;
	public final static String TITLE = "SwingLabs PDF Viewer";
	/** The current PDFFile */
	PDFFile curFile;
	/** the name of the current document */
	String docName;
	/** The thumbnail display */
	ThumbPanel thumbs;
	/** The full screen page display, or null if not in full screen mode */
	PagePanel fspp;

	// Thread anim;
	/** The current page number (starts at 0), or -1 if no page */
	int curpage = -1;
	/** the full screen button */
	JToggleButton fullScreenButton;
	/** the full screen window, or null if not in full screen mode */
	FullScreenWindow fullScreen;
	/** the root of the outline, or null if there is no outline */
	OutlineNode outline = null;
	/** The page format for printing */
	PageFormat pformat = PrinterJob.getPrinterJob().defaultPage();
	/** true if the thumb panel should exist at all */
	boolean doThumb = true;
	/** flag to indicate when a newly added document has been announced */
	Flag docWaiter;
	/** a thread that pre-loads the next page for faster response */
	PagePreparer pagePrep;
	/** the window containing the pdf outline, or null if one doesn't exist */
	JDialog olf;
	/** the document menu */
	JMenu docMenu;
	Operator opor;
	JFrame app;

	// / FILE MENU
	Action openAction = new AbstractAction("Open...") {
		public void actionPerformed(ActionEvent evt) {
			opor.doOpen();
		}
	};
	Action pageSetupAction = new AbstractAction("Page setup...") {
		public void actionPerformed(ActionEvent evt) {
			opor.doPageSetup();
		}
	};
	Action printAction;  //  @jve:decl-index=0:
	Action closeAction = new AbstractAction("Close") {
		public void actionPerformed(ActionEvent evt) {
			opor.doClose();
		}
	};
	Action quitAction = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent evt) {
			opor.doQuit();
		}
	};

	ZoomAction zoomInAction = new ZoomAction("Zoom in", getIcon("zoomin.gif"), 2.0);
	ZoomAction zoomOutAction = new ZoomAction("Zoom out", getIcon("zoomout.gif"), 0.5);  //  @jve:decl-index=0:
	Action zoomToolAction;  //  @jve:decl-index=0:
	Action fitInWindowAction;  //  @jve:decl-index=0:
	ThumbAction thumbAction = new ThumbAction();  //  @jve:decl-index=0:
	Action fullScreenAction;  //  @jve:decl-index=0:
	Action nextAction;  //  @jve:decl-index=0:
	Action firstAction;  //  @jve:decl-index=0:
	Action lastAction;  //  @jve:decl-index=0:
	Action prevAction;  //  @jve:decl-index=0:

	/**
	 * A file filter for PDF files.
	 */
	FileFilter pdfFilter = new FileFilter() {
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().endsWith(".pdf");
		}

		public String getDescription() {
			return "Choose a PDF file";
		}
	};
	private File prevDirChoice;
	PageBuilder pb = new PageBuilder();  //  @jve:decl-index=0:
	private JToolBar jJToolBarBar = null;
	private JButton jBFirst = null;
	private JButton jBPrev = null;
	private JButton jBNext = null;
	private JButton jBLast = null;
	private JButton jBFullScreen = null;
	private JButton jBZoomTool = null;
	private JButton jBFitInView = null;
	private JButton jBPaint = null;
	private JTextField jTFPage = null;
	private JSplitPane jSplitPane = null;
	private PagePanel pagePanel = null;
	private JScrollPane thumbscroll = null;
	
	public JPdfPanel(JFrame jf,boolean useThumbs) {
		app=jf;
		opor=new Operator();
		doThumb = useThumbs;
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setSize(981, 503);
		this.add(getJJToolBarBar(), BorderLayout.NORTH);
		if (doThumb) {
			this.add(getJSplitPane(), BorderLayout.CENTER);
		} else {
			this.add(getPagePanel(),BorderLayout.CENTER);
		}
		
		app.setJMenuBar(getJMenuBar());
		setEnabling();
	}

	private JMenuBar getJMenuBar() {
		JMenuBar mb = new JMenuBar();
		JMenu file = new JMenu("File");
		file.add(openAction);
		file.add(closeAction);
		file.addSeparator();
		file.add(pageSetupAction);
		file.add(printAction);
		file.addSeparator();
		file.add(quitAction);
		mb.add(file);
		JMenu view = new JMenu("View");
		JMenu zoom = new JMenu("Zoom");
		zoom.add(zoomInAction);
		zoom.add(zoomOutAction);
		zoom.add(fitInWindowAction);
		zoom.setEnabled(true);
		view.add(zoom);
		view.add(fullScreenAction);

		if (doThumb) {
			view.addSeparator();
			view.add(thumbAction);
		}

		mb.add(view);
		return mb;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL + " on this platform:" + e.getMessage());
		}
	}
	public Icon getIcon(String name) {
		Icon icon = null;
		URL url = null;
		try {
			url = getClass().getResource("/example/gfx/"+name);
	
			icon = new ImageIcon(url);
			if (icon == null) {
				System.out.println("Couldn't find " + url);
			}
		} catch (Exception e) {
			System.out.println("Couldn't find " + getClass().getName() + "/" + name);
			e.printStackTrace();
		}
		return icon;
	}
	/**
	 * Enable or disable all of the actions based on the current state.
	 */
	public void setEnabling() {
		boolean fileavailable = curFile != null;
		boolean pageshown = ((fspp != null) ? fspp.getPage() != null : pagePanel.getPage() != null);
		boolean printable = fileavailable && curFile.isPrintable();

		jTFPage.setEnabled(fileavailable);
		printAction.setEnabled(printable);
		closeAction.setEnabled(fileavailable);
		fullScreenAction.setEnabled(pageshown);
		prevAction.setEnabled(pageshown);
		nextAction.setEnabled(pageshown);
		firstAction.setEnabled(fileavailable);
		lastAction.setEnabled(fileavailable);
		zoomToolAction.setEnabled(pageshown);
		fitInWindowAction.setEnabled(pageshown);
		zoomInAction.setEnabled(pageshown);
		zoomOutAction.setEnabled(pageshown);
	}
	public void openFile(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();
		int contentLength = urlConnection.getContentLength();
		InputStream istr = urlConnection.getInputStream();
		byte[] byteBuf = new byte[contentLength];
		int offset = 0;
		int read = 0;
		while (read >= 0) {
			read = istr.read(byteBuf, offset, contentLength - offset);
			if (read > 0) {
				offset += read;
			}
		}
		if (offset != contentLength) {
			throw new IOException("Could not read all of URL file.");
		}
		ByteBuffer buf = ByteBuffer.allocate(contentLength);
		buf.put(byteBuf);
		openPDFByteBuffer(buf, url.toString(), url.getFile());
	}
	public void openFile(File file) throws IOException {
		// first open the file for random access
		RandomAccessFile raf = new RandomAccessFile(file, "r");

		// extract a file channel
		FileChannel channel = raf.getChannel();

		// now memory-map a byte-buffer
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		openPDFByteBuffer(buf, file.getPath(), file.getName());
	}
//
//	/**
//	 * <p>
//	 * Open a specific pdf file. Creates a DocumentInfo from the file, and opens
//	 * that.
//	 * </p>
//	 * 
//	 * <p>
//	 * <b>Note:</b> By not memory mapping the file its contents are not locked
//	 * down while PDFFile is open.
//	 * </p>
//	 * 
//	 * @param file
//	 *            the file to open
//	 */
//	public void openFileUnMapped(File file) throws IOException {
//		DataInputStream istr = null;
//		try {
//			// load a pdf from a byte buffer
//			// avoid using a RandomAccessFile but fill a ByteBuffer directly
//			istr = new DataInputStream(new FileInputStream(file));
//			long len = file.length();
//			if (len > Integer.MAX_VALUE) {
//				throw new IOException("File too long to decode: " + file.getName());
//			}
//			int contentLength = (int) len;
//			byte[] byteBuf = new byte[contentLength];
//			int offset = 0;
//			int read = 0;
//			while (read >= 0) {
//				read = istr.read(byteBuf, offset, contentLength - offset);
//				if (read > 0) {
//					offset += read;
//				}
//			}
//			ByteBuffer buf = ByteBuffer.allocate(contentLength);
//			buf.put(byteBuf);
//			openPDFByteBuffer(buf, file.getPath(), file.getName());
//		} catch (FileNotFoundException fnfe) {
//			fnfe.printStackTrace();
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		} finally {
//			if (istr != null) {
//				try {
//					istr.close();
//				} catch (Exception e) {
//					// ignore error on close
//				}
//			}
//		}
//	}
	private void openPDFByteBuffer(ByteBuffer buf, String path, String name) {

		// create a PDFFile from the data
		PDFFile newfile = null;
		try {
			newfile = new PDFFile(buf);
		} catch (IOException ioe) {
			openError(path + " doesn't appear to be a PDF file." + "\n: " + ioe.getMessage());
			return;
		}

		// Now that we're reasonably sure this document is real, close the
		// old one.
		opor.doClose();

		// set up our document
		this.curFile = newfile;
		docName = name;
		app.setTitle(TITLE + ": " + docName);

		// set up the thumbnails
		if (doThumb) {
			thumbs = new ThumbPanel(curFile);
			thumbs.addPageChangeListener(opor);
			thumbscroll.getViewport().setView(thumbs);
			thumbscroll.getViewport().setBackground(Color.gray);
		}

		setEnabling();

		// display page 1.
		opor.forceGotoPage(0);

		// if the PDF has an outline, display it.
		try {
			outline = curFile.getOutline();
		} catch (IOException ioe) {
		}
		if (outline != null) {
			if (outline.getChildCount() > 0) {
				olf = new JDialog(app, "Outline");
				olf.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				olf.setLocation(this.getLocation());
				JTree jt = new JTree(outline);
				jt.setRootVisible(false);
				jt.addTreeSelectionListener(opor);
				JScrollPane jsp = new JScrollPane(jt);
				olf.getContentPane().add(jsp);
				olf.pack();
				olf.setVisible(true);
			} else {
				if (olf != null) {
					olf.setVisible(false);
					olf = null;
				}
			}
		}
	}
	public void openError(String message) {
		JOptionPane.showMessageDialog(jSplitPane, message, "Error opening file", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Starts or ends full screen mode.
	 * 
	 * @param full
	 *            true to enter full screen mode, false to leave
	 * @param force
	 *            true if the user should be prompted for a screen to use the
	 *            second time full screen mode is entered.
	 */
	public void setFullScreenMode(boolean full, boolean force) {
		// curpage= -1;
		if (full && fullScreen == null) {
			fullScreenAction.setEnabled(false);
			new Thread(new PerformFullScreenMode(force), getClass().getName() + ".setFullScreenMode").start();
			fullScreenButton.setSelected(true);
		} else if (!full && fullScreen != null) {
			fullScreen.close();
			fspp = null;
			fullScreen = null;
			opor.gotoPage(curpage);
			fullScreenButton.setSelected(false);
		}
	}
	/**
	 * This method initializes jJToolBarBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	private JToolBar getJJToolBarBar() {
		if (jJToolBarBar == null) {
			jJToolBarBar = new JToolBar();
			jJToolBarBar.add(getJBFirst());
			jJToolBarBar.add(getJBPrev());
			jJToolBarBar.add(getJTFPage());
			jJToolBarBar.add(getJBNext());
			jJToolBarBar.add(getJBLast());
			jJToolBarBar.add(Box.createGlue());
			jJToolBarBar.add(getJBFullScreen());
			jJToolBarBar.add(Box.createGlue());
			jJToolBarBar.add(getJBZoomTool());
			jJToolBarBar.add(getJBFitInView());
			jJToolBarBar.add(Box.createGlue());
			jJToolBarBar.add(getJBPaint());
		}
		return jJToolBarBar;
	}
	private JButton getJBFirst() {
		if (jBFirst == null) {
			firstAction = new AbstractAction("", getIcon("first.gif")) {
				public void actionPerformed(ActionEvent evt) {
					opor.doFirst();  //  @jve:decl-index=0:
				}
			};
			jBFirst = new JButton(firstAction);
		}
		return jBFirst;
	}

	/**
	 * This method initializes jBPrev	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBPrev() {
		if (jBPrev == null) {
			prevAction = new AbstractAction("", getIcon("prev.gif")) {
				public void actionPerformed(ActionEvent evt) {
					opor.doPrev();
				}
			};
			jBPrev = new JButton(prevAction);
		}
		return jBPrev;
	}

	/**
	 * This method initializes jBNext	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBNext() {
		if (jBNext == null) {
			 nextAction = new AbstractAction("", getIcon("next.gif")) {
					public void actionPerformed(ActionEvent evt) {
						opor.doNext();  //  @jve:decl-index=0:
					}
				};
			jBNext = new JButton(nextAction);
		}
		return jBNext;
	}

	/**
	 * This method initializes jBLast	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBLast() {
		if (jBLast == null) {
			 lastAction = new AbstractAction("", getIcon("last.gif")) {
					public void actionPerformed(ActionEvent evt) {
						opor.doLast();  //  @jve:decl-index=0:
					}
				};
			jBLast = new JButton(lastAction);
		}
		return jBLast;
	}

	/**
	 * This method initializes jBFullScreen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBFullScreen() {
		if (jBFullScreen == null) {
			fullScreenAction = new AbstractAction("", getIcon("fullscrn.gif")) {
				public void actionPerformed(ActionEvent evt) {
					opor.doFullScreen((evt.getModifiers() & evt.SHIFT_MASK) != 0);  //  @jve:decl-index=0:
				}
			};
			jBFullScreen = new JButton(fullScreenAction);
		}
		return jBFullScreen;
	}

	/**
	 * This method initializes jBZoomTool	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBZoomTool() {
		if (jBZoomTool == null) {
			zoomToolAction = new AbstractAction("", getIcon("zoom.gif")) {
				public void actionPerformed(ActionEvent evt) {
					opor.doZoomTool();  //  @jve:decl-index=0:
				}
			};
			jBZoomTool = new JButton(zoomToolAction);
		}
		return jBZoomTool;
	}

	/**
	 * This method initializes jBFitInView	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBFitInView() {
		if (jBFitInView == null) {
			fitInWindowAction = new AbstractAction("", getIcon("fit.gif")) {
				public void actionPerformed(ActionEvent evt) {
					opor.doFitInWindow();  //  @jve:decl-index=0:
				}
			};
			jBFitInView = new JButton(fitInWindowAction);
		}
		return jBFitInView;
	}

	/**
	 * This method initializes jBPaint	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBPaint() {
		if (jBPaint == null) {
			 printAction = new AbstractAction("", getIcon("print.gif")) {
					public void actionPerformed(ActionEvent evt) {
						opor.doPrint();  //  @jve:decl-index=0:
					}
				};
			jBPaint = new JButton(printAction);
		}
		return jBPaint;
	}
	private JTextField getJTFPage() {
		if (jTFPage == null) {
			jTFPage = new JTextField();
			jTFPage.setPreferredSize(new Dimension(50,25));
			
			jTFPage = new JTextField("-", 3);
			// jTFPage.setEnabled(false);
			jTFPage.setMaximumSize(new Dimension(45, 32));
			jTFPage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					opor.doPageTyped();
				}
			});
		}
		return jTFPage;
	}

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			jSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, thumbAction);
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setRightComponent(getPagePanel());
			jSplitPane.setLeftComponent(getThumbscroll());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes pagePanel	
	 * 	
	 * @return example.PagePanel	
	 */
	private PagePanel getPagePanel() {
		if (pagePanel == null) {
			pagePanel = new PagePanel();
		}
		return pagePanel;
	}

	/**
	 * This method initializes thumbscroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getThumbscroll() {
		if (thumbscroll == null) {
			thumbscroll = new JScrollPane(getThumbs(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return thumbscroll;
	}

	private ThumbPanel getThumbs(){
		if(thumbs==null){
			thumbs = new ThumbPanel(null);
		}
		return thumbs;
	}
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("JPdfPanel");
				JPdfPanel content = new JPdfPanel(frame,true);
				content.setPreferredSize(content.getSize());
				frame.add(content, BorderLayout.CENTER);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Runs the FullScreenMode change in another thread
	 */
	class PerformFullScreenMode implements Runnable {
	
		boolean force;
	
		public PerformFullScreenMode(boolean forcechoice) {
			force = forcechoice;
		}
	
		public void run() {
			fspp = new PagePanel();
			fspp.setBackground(Color.black);
			pagePanel.showPage(null);
			fullScreen = new FullScreenWindow(fspp, force);
			fspp.addKeyListener(opor);
			opor.gotoPage(curpage);
			fullScreenAction.setEnabled(true);
		}
	}

	/**
	 * A thread for printing in.
	 */
	class PrintThread extends Thread {
	
		PDFPrintPage ptPages;
		PrinterJob ptPjob;
	
		public PrintThread(PDFPrintPage pages, PrinterJob pjob) {
			ptPages = pages;
			ptPjob = pjob;
			setName(getClass().getName());
		}
	
		public void run() {
			try {
				ptPages.show(ptPjob);
				ptPjob.print();
			} catch (PrinterException pe) {
				JOptionPane.showMessageDialog(JPdfPanel.this, "Printing Error: " + pe.getMessage(), "Print Aborted", JOptionPane.ERROR_MESSAGE);
			}
			ptPages.hide();
		}
	}

	/**
	 * A class to pre-cache the next page for better UI response
	 */
	class PagePreparer extends Thread {
	
		int waitforPage;
		int prepPage;
	
		/**
		 * Creates a new PagePreparer to prepare the page after the current one.
		 * 
		 * @param waitforPage
		 *            the current page number, 0 based
		 */
		public PagePreparer(int waitforPage) {
			setDaemon(true);
			setName(getClass().getName());
	
			this.waitforPage = waitforPage;
			this.prepPage = waitforPage + 1;
		}
	
		public void quit() {
			waitforPage = -1;
		}
	
		public void run() {
			Dimension size = null;
			Rectangle2D clip = null;
	
			// wait for the current page
			// System.out.println("Preparer waiting for page " + (waitforPage +
			// 1));
			if (fspp != null) {
				fspp.waitForCurrentPage();
				size = fspp.getCurSize();
				clip = fspp.getCurClip();
			} else if (pagePanel != null) {
				pagePanel.waitForCurrentPage();
				size = pagePanel.getCurSize();
				clip = pagePanel.getCurClip();
			}
	
			if (waitforPage == curpage) {
				// don't go any further if the user changed pages.
				// System.out.println("Preparer generating page " + (prepPage +
				// 2));
				PDFPage pdfPage = curFile.getPage(prepPage + 1, true);
				if (pdfPage != null && waitforPage == curpage) {
					// don't go any further if the user changed pages
					// System.out.println("Generating image for page " +
					// (prepPage + 2));
	
					pdfPage.getImage(size.width, size.height, clip, null, true, true);
					// System.out.println("Generated image for page "+
					// (prepPage+2));
				}
			}
		}
	}

	class ZoomAction extends AbstractAction {
	
		double zoomfactor = 1.0;
	
		public ZoomAction(String name, double factor) {
			super(name);
			zoomfactor = factor;
		}
	
		public ZoomAction(String name, Icon icon, double factor) {
			super(name, icon);
			zoomfactor = factor;
		}
	
		public void actionPerformed(ActionEvent evt) {
			opor.doZoom(zoomfactor);
		}
	}

	class ThumbAction extends AbstractAction implements PropertyChangeListener {
	
		boolean isOpen = true;
	
		public ThumbAction() {
			super("Hide thumbnails");
		}
	
		public void propertyChange(PropertyChangeEvent evt) {
			int v = ((Integer) evt.getNewValue()).intValue();
			if (v <= 1) {
				isOpen = false;
				putValue(ACTION_COMMAND_KEY, "Show thumbnails");
				putValue(NAME, "Show thumbnails");
			} else {
				isOpen = true;
				putValue(ACTION_COMMAND_KEY, "Hide thumbnails");
				putValue(NAME, "Hide thumbnails");
			}
		}
	
		public void actionPerformed(ActionEvent evt) {
			opor.doThumbs(!isOpen);
		}
	}

	/**
	 * Combines numeric key presses to build a multi-digit page number.
	 */
	class PageBuilder implements Runnable {
	
		int value = 0;
		long timeout;
		Thread anim;
		static final long TIMEOUT = 500;
	
		/** add the digit to the page number and start the timeout thread */
		public synchronized void keyTyped(int keyval) {
			value = value * 10 + keyval;
			timeout = System.currentTimeMillis() + TIMEOUT;
			if (anim == null) {
				anim = new Thread(this);
				anim.setName(getClass().getName());
				anim.start();
			}
		}
	
		/**
		 * waits for the timeout, and if time expires, go to the specified page
		 * number
		 */
		public void run() {
			long now, then;
			synchronized (this) {
				now = System.currentTimeMillis();
				then = timeout;
			}
			while (now < then) {
				try {
					Thread.sleep(timeout - now);
				} catch (InterruptedException ie) {
				}
				synchronized (this) {
					now = System.currentTimeMillis();
					then = timeout;
				}
			}
			synchronized (this) {
				opor.gotoPage(value - 1);
				anim = null;
				value = 0;
			}
		}
	}
	 class Operator implements PageChangeListener,KeyListener,TreeSelectionListener{

			/**
			 * Changes the displayed page, desyncing if we're not on the same page as a
			 * presenter.
			 * 
			 * @param pagenum
			 *            the page to display
			 */
			public void gotoPage(int pagenum) {
				if (pagenum < 0) {
					pagenum = 0;
				} else if (pagenum >= curFile.getNumPages()) {
					pagenum = curFile.getNumPages() - 1;
				}
				forceGotoPage(pagenum);
			}

			/**
			 * Changes the displayed page.
			 * 
			 * @param pagenum
			 *            the page to display
			 */
			public void forceGotoPage(int pagenum) {
				if (pagenum <= 0) {
					pagenum = 0;
				} else if (pagenum >= curFile.getNumPages()) {
					pagenum = curFile.getNumPages() - 1;
				}
				// System.out.println("Going to page " + pagenum);
				curpage = pagenum;

				// update the page text field
				jTFPage.setText(String.valueOf(curpage + 1));

				// fetch the page and show it in the appropriate place
				PDFPage pg = curFile.getPage(pagenum + 1);
				if (fspp != null) {
					fspp.showPage(pg);
					fspp.requestFocus();
				} else {
					pagePanel.showPage(pg);
					pagePanel.requestFocus();
				}

				// update the thumb panel
				if (doThumb) {
					thumbs.pageShown(pagenum);
				}

				// stop any previous page prepper, and start a new one
				if (pagePrep != null) {
					pagePrep.quit();
				}
				pagePrep = new PagePreparer(pagenum);
				pagePrep.start();

				setEnabling();
			}

		/**
		 * Ask the user for a PDF file to open from the local file system
		 */
		public void doOpen() {
			try {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(prevDirChoice);
				fc.setFileFilter(pdfFilter);
				fc.setMultiSelectionEnabled(false);
				int returnVal = fc.showOpenDialog(app);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						prevDirChoice = fc.getSelectedFile();
						openFile(fc.getSelectedFile());
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(jSplitPane, "Opening files from your local " + "disk is not available\nfrom the " + "Java Web Start version of this " + "program.\n", "Error opening directory", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}

		/**
		 * Open a local file, given a string filename
		 * 
		 * @param name
		 *            the name of the file to open
		 */
		public void doOpen(String name) {
			try {
				URL url = new URL(name);
				openFile(new URL(name));
			} catch (IOException ioe) {
				try {
					openFile(new File(name));
				} catch (IOException ex) {
					Logger.getLogger(MyPDFViewer.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}

		/**
		 * Posts the Page Setup dialog
		 */
		public void doPageSetup() {
			PrinterJob pjob = PrinterJob.getPrinterJob();
			pformat = pjob.pageDialog(pformat);
		}

		/**
		 * Print the current document.
		 */
		public void doPrint() {
			PrinterJob pjob = PrinterJob.getPrinterJob();
			pjob.setJobName(docName);
			Book book = new Book();
			PDFPrintPage pages = new PDFPrintPage(curFile);
			book.append(pages, pformat, curFile.getNumPages());

			pjob.setPageable(book);
			if (pjob.printDialog()) {
				new PrintThread(pages, pjob).start();
			}
		}

		/**
		 * Close the current document.
		 */
		public void doClose() {
			if (thumbs != null) {
				thumbs.stop();
			}
			if (olf != null) {
				olf.setVisible(false);
				olf = null;
			}
			if (doThumb) {
				thumbs = new ThumbPanel(null);
				thumbscroll.getViewport().setView(thumbs);
			}

			setFullScreenMode(false, false);
			pagePanel.showPage(null);
			curFile = null;
			app.setTitle(TITLE);
			setEnabling();
		}

		/**
		 * Shuts down all known threads. This ought to cause the JVM to quit if the
		 * PDFViewer is the only application running.
		 */
		public void doQuit() {
			// if (thumbs != null) {
			// thumbs.stop();
			// }
			doClose();
			app.dispose();
			System.exit(0);
		}

		/**
		 * Turns on zooming
		 */
		public void doZoomTool() {
			if (fspp == null) {
				pagePanel.useZoomTool(true);
			}
		}

		/**
		 * Turns off zooming; makes the page fit in the window
		 */
		public void doFitInWindow() {
			if (fspp == null) {
				pagePanel.useZoomTool(false);
				pagePanel.setClip(null);
			}
		}

		/**
		 * Shows or hides the thumbnails by moving the split pane divider
		 */
		public void doThumbs(boolean show) {
			if (show) {
				jSplitPane.setDividerLocation((int) thumbs.getPreferredSize().width + (int) thumbscroll.getVerticalScrollBar().getWidth() + 4);
			} else {
				jSplitPane.setDividerLocation(0);
			}
		}

		/**
		 * Enter full screen mode
		 * 
		 * @param force
		 *            true if the user should be prompted for a screen to use in a
		 *            multiple-monitor setup. If false, the user will only be
		 *            prompted once.
		 */
		public void doFullScreen(boolean force) {
			setFullScreenMode(fullScreen == null, force);
		}

		public void doZoom(double factor) {
		}

		// public void doOpenMeetingDoc(DocumentInfo doc) {
		// }

		/**
		 * Goes to the next page
		 */
		public void doNext() {
			gotoPage(curpage + 1);
		}

		/**
		 * Goes to the previous page
		 */
		public void doPrev() {
			gotoPage(curpage - 1);
		}

		/**
		 * Goes to the first page
		 */
		public void doFirst() {
			gotoPage(0);
		}

		/**
		 * Goes to the last page
		 */
		public void doLast() {
			gotoPage(curFile.getNumPages() - 1);
		}

		/**
		 * Goes to the page that was typed in the page number text field
		 */
		public void doPageTyped() {
			int pagenum = -1;
			try {
				pagenum = Integer.parseInt(jTFPage.getText()) - 1;
			} catch (NumberFormatException nfe) {
			}
			if (pagenum >= curFile.getNumPages()) {
				pagenum = curFile.getNumPages() - 1;
			}
			if (pagenum >= 0) {
				if (pagenum != curpage) {
					gotoPage(pagenum);
				}
			} else {
				jTFPage.setText(String.valueOf(curpage));
			}
		}

		/**
		 * Handle a key press for navigation
		 */
		public void keyPressed(KeyEvent evt) {
			int code = evt.getKeyCode();
			if (code == evt.VK_LEFT) {
				opor.doPrev();
			} else if (code == evt.VK_RIGHT) {
				opor.doNext();
			} else if (code == evt.VK_UP) {
				opor.doPrev();
			} else if (code == evt.VK_DOWN) {
				opor.doNext();
			} else if (code == evt.VK_HOME) {
				opor.doFirst();
			} else if (code == evt.VK_END) {
				opor.doLast();
			} else if (code == evt.VK_PAGE_UP) {
				opor.doPrev();
			} else if (code == evt.VK_PAGE_DOWN) {
				opor.doNext();
			} else if (code == evt.VK_SPACE) {
				opor.doNext();
			} else if (code == evt.VK_ESCAPE) {
				setFullScreenMode(false, false);
			}
		}

		public void keyReleased(KeyEvent evt) {
		}

		/**
		 * gets key presses and tries to build a page if they're numeric
		 */
		public void keyTyped(KeyEvent evt) {
			char key = evt.getKeyChar();
			if (key >= '0' && key <= '9') {
				int val = key - '0';
				pb.keyTyped(val);
			}
		}

		/**
		 * Someone changed the selection of the outline tree. Go to the new page.
		 */
		public void valueChanged(TreeSelectionEvent e) {
			if (e.isAddedPath()) {
				OutlineNode node = (OutlineNode) e.getPath().getLastPathComponent();
				if (node == null) {
					return;
				}
		
				try {
					PDFAction action = node.getAction();
					if (action == null) {
						return;
					}
		
					if (action instanceof GoToAction) {
						PDFDestination dest = ((GoToAction) action).getDestination();
						if (dest == null) {
							return;
						}
		
						PDFObject page = dest.getPage();
						if (page == null) {
							return;
						}
		
						int pageNum = curFile.getPageNumber(page);
						if (pageNum >= 0) {
							opor.gotoPage(pageNum);
						}
					}
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
