/*
 * $Id: PDFPage.java,v 1.7 2010-05-24 22:02:35 lujke Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.snail.pdf.sun;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A PDFPage encapsulates the parsed commands required to render a single page
 * from a PDFFile. The PDFPage is not itself drawable; instead, create a
 * PDFImage to display something on the screen.
 * <p>
 * This file also contains all of the PDFCmd commands that might be a part of
 * the command stream in a PDFPage. They probably should be inner classes of
 * PDFPage instead of separate non-public classes.
 * 
 * @author Mike Wessler
 */
public class PDFPage {

	/**
	 * the array of commands. The length of this array will always be greater
	 * than or equal to the actual number of commands.
	 */
	private List<PDFCmd> commands;
	/**
	 * whether this page has been finished. If true, there will be no more
	 * commands added to the cmds list.
	 */
	private boolean finished = false;
	/** the page number used to find this page */
	private int pageNumber;
	/**
	 * the bounding box of the page, in page coordinates, straight from the page
	 * dictionary
	 */
	private Rectangle2D pageDictBbox;

	/**
	 * the post-rotation bounding box in page points with the x,y co-ordinates
	 * at 0,0
	 */
	private Rectangle2D targetBbox;

	/** the rotation of this page, in degrees */
	private int rotation;
	/**
	 * a map from image info (width, height, clip) to a soft reference to the
	 * rendered image
	 */
	private Cache cache;
	/** a map from image info to weak references to parsers that are active */
	private Map<ImageInfo, WeakReference> renderers;

	/**
	 * create a PDFPage with dimensions in bbox and rotation.
	 */
	public PDFPage(Rectangle2D bbox, int rotation) {
		this(-1, bbox, rotation, null);
	}

	/**
	 * create a PDFPage
	 * 
	 * @param pageNumber
	 *            the page number
	 * @param bbox
	 *            the bounding box, specified in pre-rotation page co-ordinates
	 * @param rotation
	 *            the rotation to apply to the page; must be 0/90/180/270
	 * @param cache
	 *            a cache to use
	 */
	public PDFPage(int pageNumber, Rectangle2D bbox, int rotation, Cache cache) {
		this.pageNumber = pageNumber;
		this.cache = cache;

		if (bbox == null) {
			bbox = new Rectangle2D.Double(0, 0, 1, 1);
		}
		this.pageDictBbox = bbox;

		if (rotation < 0) {
			rotation += 360;
		}

		this.rotation = rotation;
		if (rotation == 0 || rotation == 180) {
			this.targetBbox = new Rectangle2D.Double(0, 0,
					pageDictBbox.getWidth(), pageDictBbox.getHeight());
		} else {
			this.targetBbox = new Rectangle2D.Double(0, 0,
					pageDictBbox.getHeight(), pageDictBbox.getWidth());
		}

		// initialize the cache of images and parsers
		renderers = Collections
				.synchronizedMap(new HashMap<ImageInfo, WeakReference>());

		// initialize the list of commands
		commands = Collections.synchronizedList(new ArrayList<PDFCmd>(250));
	}

	/**
	 * Get the width and height of this image in the correct aspect ratio. The
	 * image returned will have at least one of the width and height values
	 * identical to those requested. The other dimension may be smaller, so as
	 * to keep the aspect ratio the same as in the original page.
	 * 
	 * @param width
	 *            the maximum width of the image
	 * @param height
	 *            the maximum height of the image
	 * @param clip
	 *            the region in <b>page space co-ordinates</b> of the page to
	 *            display. It may be null, in which the page crop/media box is
	 *            used.
	 */
	public Dimension getUnstretchedSize(int width, int height, Rectangle2D clip) {

		if (clip == null) {
			clip = pageDictBbox;
		}

		final boolean swapDimensions = doesRotationSwapDimensions();
		final double srcHeight = swapDimensions ? clip.getWidth() : clip
				.getHeight();
		final double srcWidth = swapDimensions ? clip.getHeight() : clip
				.getWidth();
		double ratio = srcHeight / srcWidth;
		double askratio = (double) height / (double) width;
		if (askratio > ratio) {
			height = (int) (width * ratio + 0.5);
		} else {
			width = (int) (height / ratio + 0.5);
		}
		return new Dimension(width, height);
	}

	private boolean doesRotationSwapDimensions() {
		return getRotation() == 90 || getRotation() == 270;
	}

	public Image getImage(int width, int height, Rectangle2D clip,
			ImageObserver observer) {
		return getImage(width, height, clip, observer, true, false);
	}

	public Image getImage(int width, int height, Rectangle2D clip,
			ImageObserver observer, boolean drawbg, boolean wait) {
		BufferedImage image = null;
		PDFRenderer renderer = null;
		ImageInfo info = new ImageInfo(width, height, clip, null);

		if (cache != null) {
			image = cache.getImage(this, info);
			renderer = cache.getImageRenderer(this, info);
		}
		if (image == null) {
			if (drawbg) {
				info.bgColor = Color.WHITE;
			}

			image = new RefImage(info.width, info.height,
					BufferedImage.TYPE_INT_ARGB);
			renderer = new PDFRenderer(this, info, image);

			if (cache != null) {
				cache.addImage(this, info, image, renderer);
			}

			renderers.put(info, new WeakReference<PDFRenderer>(renderer));
		}
		if (renderer != null) {
			if (observer != null) {
				renderer.addObserver(observer);
			}

			if (!renderer.isFinished()) {
				renderer.go(wait);
			}
		}
		return image;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public float getAspectRatio() {
		return getWidth() / getHeight();
	}

	public Rectangle2D getPageBox() {
		return pageDictBbox;
	}

	public Rectangle2D getBBox() {
		return targetBbox;
	}

	public float getWidth() {
		return (float) targetBbox.getWidth();
	}

	public float getHeight() {
		return (float) targetBbox.getHeight();
	}

	public int getRotation() {
		return rotation;
	}

	public AffineTransform getInitialTransform(int width, int height,
			Rectangle2D clip) {

		if (clip == null) {
			clip = pageDictBbox;
		}

		AffineTransform at;
		switch (getRotation()) {
		case 0:
			at = new AffineTransform(1, 0, 0, -1, 0, height);
			break;
		case 90:
			at = new AffineTransform(0, 1, 1, 0, 0, 0);
			break;
		case 180:
			at = new AffineTransform(-1, 0, 0, 1, width, 0);
			break;
		case 270:
			at = new AffineTransform(0, -1, -1, 0, width, height);
			break;
		default:
			throw new IllegalArgumentException("Non-quadrant rotation: "
					+ getRotation());
		}

		double scaleX = (doesRotationSwapDimensions() ? height : width)
				/ clip.getWidth();
		double scaleY = (doesRotationSwapDimensions() ? width : height)
				/ clip.getHeight();
		at.scale(scaleX, scaleY);
		at.translate(-clip.getMinX(), -clip.getMinY());

		return at;
	}

	public int getCommandCount() {
		return commands.size();
	}
	public PDFCmd getCommand(int index) {
		return commands.get(index);
	}
	public List<PDFCmd> getCommands() {
		return commands;
	}

	public List getCommands(int startIndex) {
		return getCommands(startIndex, getCommandCount());
	}

	public List getCommands(int startIndex, int endIndex) {
		return commands.subList(startIndex, endIndex);
	}
	public void addCommand(PDFCmd cmd) {
		synchronized (commands) {
			commands.add(cmd);
		}
		updateImages();
	}

	public void addCommands(PDFPage page) {
		addCommands(page, null);
	}

	public void addCommands(PDFPage page, AffineTransform extra) {
		synchronized (commands) {
			addPush();
			if (extra != null) {
				addXform(extra);
			}
			commands.addAll(page.getCommands());
			addPop();
		}
		updateImages();
	}

	public void clearCommands() {
		synchronized (commands) {
			commands.clear();
		}
		updateImages();
	}

	public boolean isFinished() {
		return finished;
	}

	public synchronized void waitForFinish() throws InterruptedException {
		if (!finished) {
			wait();
		}
	}
	public void stop(int width, int height, Rectangle2D clip) {
		ImageInfo info = new ImageInfo(width, height, clip);

		synchronized (renderers) {
			WeakReference rendererRef = renderers.get(info);
			if (rendererRef != null) {
				PDFRenderer renderer = (PDFRenderer) rendererRef.get();
				if (renderer != null) {
					renderer.stop();
				}
			}
		}
	}

	/**
	 * The entire page is done. This must only be invoked once. All observers
	 * will be notified.
	 */
	public synchronized void finish() {
		// System.out.println("Page finished!");
		finished = true;
		notifyAll();

		// notify any outstanding images
		updateImages();
	}

	/** push the graphics state */
	public void addPush() {
		addCommand(new PDFPushCmd());
	}

	/** pop the graphics state */
	public void addPop() {
		addCommand(new PDFPopCmd());
	}

	/** concatenate a transform to the graphics state */
	public void addXform(AffineTransform at) {
		// PDFXformCmd xc= lastXformCmd();
		// xc.at.concatenate(at);
		addCommand(new PDFXformCmd(new AffineTransform(at)));
	}

	/**
	 * set the stroke width
	 * 
	 * @param w
	 *            the width of the stroke
	 */
	public void addStrokeWidth(float w) {
		PDFChangeStrokeCmd sc = new PDFChangeStrokeCmd();
		// if (w == 0) {
		// w = 0.1f;
		// }
		sc.setWidth(w);
		addCommand(sc);
	}

	/**
	 * set the end cap style
	 * 
	 * @param capstyle
	 *            the cap style: 0 = BUTT, 1 = ROUND, 2 = SQUARE
	 */
	public void addEndCap(int capstyle) {
		PDFChangeStrokeCmd sc = new PDFChangeStrokeCmd();

		int cap = BasicStroke.CAP_BUTT;
		switch (capstyle) {
		case 0:
			cap = BasicStroke.CAP_BUTT;
			break;
		case 1:
			cap = BasicStroke.CAP_ROUND;
			break;
		case 2:
			cap = BasicStroke.CAP_SQUARE;
			break;
		}
		sc.setEndCap(cap);

		addCommand(sc);
	}

	/**
	 * set the line join style
	 * 
	 * @param joinstyle
	 *            the join style: 0 = MITER, 1 = ROUND, 2 = BEVEL
	 */
	public void addLineJoin(int joinstyle) {
		PDFChangeStrokeCmd sc = new PDFChangeStrokeCmd();

		int join = BasicStroke.JOIN_MITER;
		switch (joinstyle) {
		case 0:
			join = BasicStroke.JOIN_MITER;
			break;
		case 1:
			join = BasicStroke.JOIN_ROUND;
			break;
		case 2:
			join = BasicStroke.JOIN_BEVEL;
			break;
		}
		sc.setLineJoin(join);

		addCommand(sc);
	}

	/**
	 * set the miter limit
	 */
	public void addMiterLimit(float limit) {
		PDFChangeStrokeCmd sc = new PDFChangeStrokeCmd();

		sc.setMiterLimit(limit);

		addCommand(sc);
	}

	/**
	 * set the dash style
	 * 
	 * @param dashary
	 *            the array of on-off lengths
	 * @param phase
	 *            offset of the array at the start of the line drawing
	 */
	public void addDash(float[] dashary, float phase) {
		PDFChangeStrokeCmd sc = new PDFChangeStrokeCmd();

		sc.setDash(dashary, phase);

		addCommand(sc);
	}

	/**
	 * set the current path
	 * 
	 * @param path
	 *            the path
	 * @param style
	 *            the style: PDFShapeCmd.STROKE, PDFShapeCmd.FILL,
	 *            PDFShapeCmd.BOTH, PDFShapeCmd.CLIP, or some combination.
	 */
	public void addPath(GeneralPath path, int style) {
		addCommand(new PDFShapeCmd(path, style));
	}

	/**
	 * set the fill paint
	 */
	public void addFillPaint(PDFPaint p) {
		addCommand(new PDFFillPaintCmd(p));
	}

	/** set the stroke paint */
	public void addStrokePaint(PDFPaint p) {
		addCommand(new PDFStrokePaintCmd(p));
	}

	/**
	 * set the fill alpha
	 */
	public void addFillAlpha(float a) {
		addCommand(new PDFFillAlphaCmd(a));
	}

	/** set the stroke alpha */
	public void addStrokeAlpha(float a) {
		addCommand(new PDFStrokeAlphaCmd(a));
	}

	/**
	 * draw an image
	 * 
	 * @param image
	 *            the image to draw
	 */
	public void addImage(PDFImage image) {
		addCommand(new PDFImageCmd(image));
	}

	/**
	 * Notify all images we know about that a command has been added
	 */
	public void updateImages() {
		for (Iterator i = renderers.values().iterator(); i.hasNext();) {
			WeakReference ref = (WeakReference) i.next();
			PDFRenderer renderer = (PDFRenderer) ref.get();

			if (renderer != null) {
				if (renderer.getStatus() == Watchable.NEEDS_DATA) {
					// there are watchers. Set the state to paused and
					// let the watcher decide when to start.
					renderer.setStatus(Watchable.PAUSED);
				}
			}
		}
	}
}

/**
 * draw an image
 */
class PDFImageCmd extends PDFCmd {

	PDFImage image;

	public PDFImageCmd(PDFImage image) {
		this.image = image;
	}

	public Rectangle2D execute(PDFRenderer state) {
		return state.drawImage(image);
	}
}

/**
 * set the fill paint
 */
class PDFFillPaintCmd extends PDFCmd {

	PDFPaint p;

	public PDFFillPaintCmd(PDFPaint p) {
		this.p = p;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.setFillPaint(p);
		return null;
	}
}

/**
 * set the stroke paint
 */
class PDFStrokePaintCmd extends PDFCmd {

	PDFPaint p;

	public PDFStrokePaintCmd(PDFPaint p) {
		this.p = p;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.setStrokePaint(p);
		return null;
	}
}

/**
 * set the fill paint
 */
class PDFFillAlphaCmd extends PDFCmd {

	float a;

	public PDFFillAlphaCmd(float a) {
		this.a = a;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.setFillAlpha(a);
		return null;
	}
}

/**
 * set the stroke paint
 */
class PDFStrokeAlphaCmd extends PDFCmd {

	float a;

	public PDFStrokeAlphaCmd(float a) {
		this.a = a;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.setStrokeAlpha(a);
		return null;
	}
}

/**
 * push the graphics state
 */
class PDFPushCmd extends PDFCmd {

	public Rectangle2D execute(PDFRenderer state) {
		state.push();
		return null;
	}
}

/**
 * pop the graphics state
 */
class PDFPopCmd extends PDFCmd {

	public Rectangle2D execute(PDFRenderer state) {
		state.pop();
		return null;
	}
}

/**
 * concatenate a transform to the graphics state
 */
class PDFXformCmd extends PDFCmd {

	AffineTransform at;

	public PDFXformCmd(AffineTransform at) {
		if (at == null) {
			throw new RuntimeException("Null transform in PDFXformCmd");
		}
		this.at = at;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.transform(at);
		return null;
	}

	public String toString(PDFRenderer state) {
		return "PDFXformCmd: " + at;
	}

	@Override
	public String getDetails() {
		StringBuffer buf = new StringBuffer();
		buf.append("PDFXformCommand: \n");
		buf.append(at.toString());

		return buf.toString();
	}
}

/**
 * change the stroke style
 */
class PDFChangeStrokeCmd extends PDFCmd {

	float w, limit, phase;
	int cap, join;
	float[] ary;

	public PDFChangeStrokeCmd() {
		this.w = PDFRenderer.NOWIDTH;
		this.cap = PDFRenderer.NOCAP;
		this.join = PDFRenderer.NOJOIN;
		this.limit = PDFRenderer.NOLIMIT;
		this.ary = PDFRenderer.NODASH;
		this.phase = PDFRenderer.NOPHASE;
	}

	/**
	 * set the width of the stroke. Rendering needs to account for a minimum
	 * stroke width in creating the output.
	 * 
	 * @param w
	 *            float
	 */
	public void setWidth(float w) {
		this.w = w;
	}

	public void setEndCap(int cap) {
		this.cap = cap;
	}

	public void setLineJoin(int join) {
		this.join = join;
	}

	public void setMiterLimit(float limit) {
		this.limit = limit;
	}

	public void setDash(float[] ary, float phase) {
		if (ary != null) {
			// make sure no pairs start with 0, since having no opaque
			// region doesn't make any sense.
			for (int i = 0; i < ary.length - 1; i += 2) {
				if (ary[i] == 0) {
					/* Give a very small value, since 0 messes java up */
					ary[i] = 0.00001f;
					break;
				}
			}
		}
		this.ary = ary;
		this.phase = phase;
	}

	public Rectangle2D execute(PDFRenderer state) {
		state.setStrokeParts(w, cap, join, limit, ary, phase);
		return null;
	}

	public String toString(PDFRenderer state) {
		return "STROKE: w=" + w + " cap=" + cap + " join=" + join + " limit="
				+ limit + " ary=" + ary + " phase=" + phase;
	}
}
