/*
 * KettleServerForm.java
 *
 * Created on __DATE__, __TIME__
 */

package com.snail.ui.kettle.server;

import java.io.File;
import java.util.Properties;

import javax.swing.JFrame;

import org.pentaho.di.core.exception.KettleException;

import com.snail.kettle.scheduler.NativeEngine;
import com.snail.kettle.scheduler.beans.KettleJob;

/**
 * 
 * 
 * <p>
 * Title: com.snail.ui.kettle.server.KettleServerForm
 * </p>
 * 
 * <p>
 * Description Kettle 服务器
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * 
 * @author Snail
 * @date 2015-1-4
 * 
 * @version 1.0
 *
 */
public class KettleServerForm extends JFrame {

	private static final long serialVersionUID = 2696457525781639485L;

	/** Creates new form KettleServerForm */
	public KettleServerForm() {
		initComponents();
		custInit();
	}

	private void custInit() {
		this.setLocationRelativeTo(null);
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		menuBar = new javax.swing.JMenuBar();
		generalMenu = new javax.swing.JMenu();
		startServerMenuItem = new javax.swing.JMenuItem();
		stopServerMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		settingMenu = new javax.swing.JMenu();
		cutMenuItem = new javax.swing.JMenuItem();
		copyMenuItem = new javax.swing.JMenuItem();
		pasteMenuItem = new javax.swing.JMenuItem();
		deleteMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		contentsMenuItem = new javax.swing.JMenuItem();
		aboutMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		generalMenu.setText("General");

		startServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/ui/images/run.png"))); // NOI18N
		startServerMenuItem.setText("Start Server");
		startServerMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						startServerMenuItemActionPerformed(evt);
					}
				});
		generalMenu.add(startServerMenuItem);

		stopServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/ui/images/stop.png"))); // NOI18N
		stopServerMenuItem.setText("Stop Server");
		generalMenu.add(stopServerMenuItem);

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		generalMenu.add(exitMenuItem);

		menuBar.add(generalMenu);

		settingMenu.setText("Setting");

		cutMenuItem.setText("Cut");
		settingMenu.add(cutMenuItem);

		copyMenuItem.setText("Copy");
		settingMenu.add(copyMenuItem);

		pasteMenuItem.setText("Paste");
		settingMenu.add(pasteMenuItem);

		deleteMenuItem.setText("Delete");
		settingMenu.add(deleteMenuItem);

		menuBar.add(settingMenu);

		helpMenu.setText("Help");

		contentsMenuItem.setText("Contents");
		helpMenu.add(contentsMenuItem);

		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 675,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 499,
				Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void startServerMenuItemActionPerformed(
			java.awt.event.ActionEvent evt) {
		new Thread("test") {
			public void run() {
				while (true) {
					try {
						Properties kettleJobs = NativeEngine.getInstance()
								.getKettleJobs();
						for (Object key : kettleJobs.keySet()) {
							Object obj = kettleJobs.get(key);
							if (obj != null && obj instanceof KettleJob) {
								KettleJob kettleJob = (KettleJob) obj;
								System.out.println(kettleJob.getJobNo()
										+ ","
										+ kettleJob.getJobName()
										+ ","
										+ kettleJob.getStatus()
										+ ","
										+ kettleJob
												.getFileName()
												.replace(
														NativeEngine
																.getInstance()
																.getJobHome(),
														File.separator)
												.replace(File.separator, "/"));
							}
						}
					} catch (KettleException e) {
						e.printStackTrace();
					}
					try {
						sleep(1000 * 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	}//GEN-LAST:event_exitMenuItemActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new KettleServerForm().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem contentsMenuItem;
	private javax.swing.JMenuItem copyMenuItem;
	private javax.swing.JMenuItem cutMenuItem;
	private javax.swing.JMenuItem deleteMenuItem;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu generalMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem pasteMenuItem;
	private javax.swing.JMenu settingMenu;
	private javax.swing.JMenuItem startServerMenuItem;
	private javax.swing.JMenuItem stopServerMenuItem;
	// End of variables declaration//GEN-END:variables

}