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


/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: JBar.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/04/12 19:47:19  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/08 17:32:55  jijunwan
 *  Archive Log:    introduced new summary section for "Home Page"
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common.view;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JLabel;

public class JHorizontalBar extends JLabel {
    private static final long serialVersionUID = 3695428455610236209L;

    private double normalizedValue;
    private double upperMargin = 0.25;
    private double lowerMargin = 0.25;
    
    public JHorizontalBar() {
        super();
        setOpaque(true);
    }

    /**
     * @return the normalizedValue
     */
    public double getNormalizedValue() {
        return normalizedValue;
    }

    /**
     * @param normalizedValue the normalizedValue to set
     */
    public void setNormalizedValue(double normalizedValue) {
        check(normalizedValue);
        this.normalizedValue = normalizedValue;
        repaint();
    }

    /**
     * @return the upperMargin
     */
    public double getUpperMargin() {
        return upperMargin;
    }

    /**
     * @param upperMargin the upperMargin to set
     */
    public void setUpperMargin(double upperMargin) {
        check(upperMargin);
        this.upperMargin = upperMargin;
        repaint();
    }

    /**
     * @return the lowerMargin
     */
    public double getLowerMargin() {
        return lowerMargin;
    }

    /**
     * @param lowerMargin the lowerMargin to set
     */
    public void setLowerMargin(double lowerMargin) {
        check(lowerMargin);
        this.lowerMargin = lowerMargin;
        repaint();
    }
    
    protected void check(double value) {
        if (value<0 || value>1) {
            throw new IllegalArgumentException("Value "+value+" is not in range [0, 1]");
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Insets insets = getInsets();
        
        g.setColor(getForeground());
        int w = (int) ((getWidth() - insets.left - insets.right) * normalizedValue);
        int height = getHeight() - insets.top - insets.bottom;
        int y = insets.top + (int)(height * upperMargin);
        int h = (int) (height * (1 - upperMargin - lowerMargin));
        if (h>0 && w>0) {
            g.fillRect(insets.left, y, w, h);
        }
    }
    
}
