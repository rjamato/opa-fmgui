/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
 */
package com.intel.stl.ui.common.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * @author jijunwan
 *
 */
public class JLabelBar extends JLabel {
	private static final long serialVersionUID = 5644031069268625217L;
	
	/**
	 * The value must be normalized between 0 and 1.0
	 */
	private double normalizedBarValue;
	private Color barColor;
	private int barSize = 5;
	private int barPosition;
	private Insets barInsets = new Insets(0, 5, 0, 0);
	private Border outerBorder;
	
	public JLabelBar(Icon image, int horizontalAlignment, 
			double normalizedBarValue, Color barColor, int barPosition) {
		super(image, horizontalAlignment);
		initBar(normalizedBarValue, barColor, barPosition);
	}
	
	public JLabelBar(String text, int horizontalAlignment, 
			double normalizedBarValue, Color barColor, int barPosition) {
		super(text, horizontalAlignment);
		initBar(normalizedBarValue, barColor, barPosition);
	}
	
	protected void initBar(double normalizedBarValue, Color barColor, int barPosition) {
		if (normalizedBarValue<0 || normalizedBarValue>1.0)
			throw new IllegalArgumentException("normalizedBarValue ("+normalizedBarValue+") in not in range [0, 1]");
		if (barPosition!=TOP && barPosition!=BOTTOM && barPosition!=LEFT && barPosition!=RIGHT)
			throw new IllegalArgumentException("Invalid barPosition ("+barPosition+"). It must be TOP, BOTTOM, LEFT or RIGHT.");
		
		this.normalizedBarValue = normalizedBarValue;
		this.barColor = barColor;
		this.barPosition = barPosition;
	}
	
	/**
	 * @param barSize the barSize to set
	 */
	public void setBarSize(int barSize) {
		this.barSize = barSize;
		setBorder(outerBorder);
		repaint();
	}
	
	/**
	 * @return the barInsets
	 */
	public Insets getBarInsets() {
		return barInsets;
	}

	/**
	 * @param barInsets the barInsets to set
	 */
	public void setBarInsets(Insets barInsets) {
		this.barInsets = barInsets;
		setBorder(outerBorder);
		repaint();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setBorder(javax.swing.border.Border)
	 */
	@Override
	public void setBorder(Border border) {
		this.outerBorder = border;
		Border innerBorder = null;
		int horizontalBarSpace = barInsets.top + barInsets.bottom + barSize;
		int verticalBarSpace = barInsets.left + barInsets.right + barSize;
		
		switch(barPosition) {
		case TOP:
			innerBorder = BorderFactory.createEmptyBorder(horizontalBarSpace, 0, 0, 0);
			break;
		case BOTTOM:
			innerBorder = BorderFactory.createEmptyBorder(0, 0, horizontalBarSpace, 0);
			break;
		case LEFT:
			innerBorder = BorderFactory.createEmptyBorder(0, verticalBarSpace, 0, 0);
			break;
		case RIGHT:
			innerBorder = BorderFactory.createEmptyBorder(0, 0, 0, verticalBarSpace);
			break;
		}
		Border newBorder = BorderFactory.createCompoundBorder(
				border, innerBorder);
		super.setBorder(newBorder);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch(barPosition) {
		case TOP:
			drawTopBar(g);
			break;
		case BOTTOM:
			drawBottomBar(g);
			break;
		case LEFT:
			drawLeftBar(g);
			break;
		case RIGHT:
			drawRightBar(g);
			break;
		}
	}
	
	protected void drawTopBar(Graphics g) {
		Insets insets = getInsets();
		int w = (int) ((getWidth() - insets.left - insets.right - barInsets.left - barInsets.right) * normalizedBarValue);
		int h = barSize;
		int x = insets.left + barInsets.left;
		int y = insets.top - barInsets.bottom - barSize;
		if (y<0) {
			y = 0;
		}
		
		g.setColor(barColor);
		g.fillRect(x, y, w, h);
	}
	
	protected void drawBottomBar(Graphics g) {
		Insets insets = getInsets();
		int w = (int) ((getWidth() - insets.left - insets.right - barInsets.left - barInsets.right) * normalizedBarValue);
		int h = barSize;
		int x = insets.left + barInsets.left;
		int y = getHeight() - insets.bottom + barInsets.top;
		if (y<0) {
			y = 0;
		}
		
		g.setColor(barColor);
		g.fillRect(x, y, w, h);
	}

	protected void drawLeftBar(Graphics g) {
		Insets insets = getInsets();
		int w = barSize;
		int h = (int) ((getHeight() - insets.top - insets.bottom - barInsets.top - barInsets.bottom) * normalizedBarValue);
		int x = insets.left - barInsets.right - barSize;
		if (x<0) {
			x = 0;
		}
		int y = getHeight() - insets.bottom -barInsets.bottom - h;
		if (y<0) {
			y = 0;
		}
		
		g.setColor(barColor);
		g.fillRect(x, y, w, h);
	}

	protected void drawRightBar(Graphics g) {
		Insets insets = getInsets();
		int w = barSize;
		int h = (int) ((getHeight() - insets.top - insets.bottom - barInsets.top - barInsets.bottom) * normalizedBarValue);
		int x = getWidth() - insets.right + barInsets.left;
		if (x<0) {
			x = 0;
		}
		int y = getHeight() - insets.bottom -barInsets.bottom - h;
		if (y<0) {
			y = 0;
		}
		
		g.setColor(barColor);
		g.fillRect(x, y, w, h);
	}
}
