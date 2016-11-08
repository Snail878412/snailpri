package com.snail.pdf.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import com.snail.pdf.Flag;
import com.snail.pdf.main.PDFViewer;
import com.snail.pdf.sun.PDFPage;

public class PagePanel extends JPanel implements ImageObserver, MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 8978279997903443567L;

	Image currentImage;

	PDFPage currentPage;

	AffineTransform currentXform;

	int offx;

	int offy;

	Rectangle2D clip;

	Rectangle2D prevClip;

	Dimension prevSize;

	Rectangle zoomRect;

	boolean useZoom = false;

	Flag flag = new Flag();

	public PagePanel() {
		super();
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public PagePanel(PDFViewer pdfViewer) {
		this();
	}

	public synchronized void showPage(PDFPage page) {
		if (currentPage != null && prevSize != null) {
			currentPage.stop(prevSize.width, prevSize.height, prevClip);
		}
		currentPage = page;
		if (page == null) {
			currentImage = null;
			clip = null;
			currentXform = null;
			repaint();
		} else {
			flag.clear();
			Dimension sz = getSize();
			if (sz.width + sz.height == 0) {
				return;
			}
			Rectangle2D useClip = clip;
			if (clip != null && currentXform != null) {
				useClip = currentXform.createTransformedShape(clip)
						.getBounds2D();
			}
			Dimension pageSize = page.getUnstretchedSize(sz.width, sz.height,
					useClip);
			currentImage = page.getImage(pageSize.width, pageSize.height,
					useClip, this);
			currentXform = page.getInitialTransform(pageSize.width,
					pageSize.height, useClip);
			try {
				currentXform = currentXform.createInverse();
			} catch (NoninvertibleTransformException nte) {
				System.out.println("Error inverting page transform!");
				nte.printStackTrace();
			}
			prevClip = useClip;
			prevSize = pageSize;
			repaint();
		}
	}

	/**
	 * Draw the image.
	 */
	public void paint(Graphics g) {
		Dimension sz = getSize();
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		if (currentImage == null) {
			g.setColor(Color.black);
			g.drawString("No page selected", getWidth() / 2 - 30,
					getHeight() / 2);
		} else {
			int imwid = currentImage.getWidth(null);
			int imhgt = currentImage.getHeight(null);

			offx = (sz.width - imwid) / 2;
			offy = (sz.height - imhgt) / 2;

			if ((imwid == sz.width && imhgt <= sz.height)
					|| (imhgt == sz.height && imwid <= sz.width)) {
				g.drawImage(currentImage, offx, offy, this);
			} else {
				if (currentPage != null) {
					showPage(currentPage);
				}
				g.setColor(Color.red);
				g.drawLine(0, 0, getWidth(), getHeight());
				g.drawLine(0, getHeight(), getWidth(), 0);
			}
		}
		if (zoomRect != null) {
			g.setColor(Color.red);
			g.drawRect(zoomRect.x, zoomRect.y, zoomRect.width, zoomRect.height);
		}
	}

	public PDFPage getPage() {
		return currentPage;
	}

	public Dimension getCurSize() {
		return prevSize;
	}

	public Rectangle2D getCurClip() {
		return prevClip;
	}

	public void waitForCurrentPage() {
		flag.waitForFlag();
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		if ((infoflags & (SOMEBITS | ALLBITS)) != 0) {
			repaint(x + offx, y + offy, width, height);
		}
		if ((infoflags & (ALLBITS | ERROR | ABORT)) != 0) {
			flag.set();
			return false;
		} else {
			return true;
		}
	}

	public void useZoomTool(boolean use) {
		useZoom = use;
	}

	public void setClip(Rectangle2D clip) {
		this.clip = clip;
		showPage(currentPage);
	}
	int downx;
	int downy;
	public void mousePressed(MouseEvent evt) {
		downx = evt.getX();
		downy = evt.getY();
	}

	public void mouseReleased(MouseEvent evt) {
		if (!useZoom || zoomRect == null || zoomRect.width == 0
				|| zoomRect.height == 0) {
			zoomRect = null;
			return;
		}
		setClip(new Rectangle2D.Double(zoomRect.x - offx, zoomRect.y - offy,
				zoomRect.width, zoomRect.height));
		zoomRect = null;
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mouseMoved(MouseEvent evt) {
	}

	public void mouseDragged(MouseEvent evt) {
		if (useZoom) {
			int x = evt.getX();
			int y = evt.getY();
			int dx = Math.abs(x - downx);
			int dy = Math.abs(y - downy);
			if ((evt.getModifiers() & InputEvent.SHIFT_MASK) == 0) {
				float aspect = (float) dx / (float) dy;
				float waspect = (float) getWidth() / (float) getHeight();
				if (aspect > waspect) {
					dy = (int) (dx / waspect);
				} else {
					dx = (int) (dy * waspect);
				}
			}
			if (x < downx) {
				x = downx - dx;
			}
			if (y < downy) {
				y = downy - dy;
			}
			Rectangle old = zoomRect;
			if (dx < 5 || dy < 5) {
				zoomRect = null;
			} else {
				zoomRect = new Rectangle(Math.min(downx, x),
						Math.min(downy, y), dx, dy);
			}
			if (zoomRect != null) {
				if (old != null) {
					old.add(zoomRect);
				} else {
					old = new Rectangle(zoomRect);
				}
			}
			if (old != null) {
				old.width++;
				old.height++;
			}
			if (old != null) {
				repaint(old);
			}
		}
	}
}
