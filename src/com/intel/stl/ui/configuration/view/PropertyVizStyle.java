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
 *  File Name: PropertyVizStyle.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/10/22 02:15:33  jijunwan
 *  Archive Log:    1) abstracted property related panels to general panels that can be reused at other places
 *  Archive Log:    2) introduced renderer into property panels to allow customizes property render
 *  Archive Log:    3) generalized property style to be able to apply on any ui component
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/09/04 19:56:45  jijunwan
 *  Archive Log:    minor L&F adjustments on property viz
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/09/04 16:54:25  jijunwan
 *  Archive Log:    added code to support changing property viz style through UI
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import com.intel.stl.ui.common.UIConstants;

public class PropertyVizStyle {
    private boolean showBorder;

    private Color borderColor = UIConstants.INTEL_LIGHT_GRAY;

    private boolean alternateRows;

    private Color alternationColor = UIConstants.INTEL_BACKGROUND_GRAY;

    /**
     * Description:
     * 
     */
    public PropertyVizStyle() {
        super();
    }

    /**
     * Description:
     * 
     * @param showBorder
     * @param useAlternation
     */
    public PropertyVizStyle(boolean showBorder, boolean alternateRows) {
        super();
        this.showBorder = showBorder;
        this.alternateRows = alternateRows;
    }

    /**
     * @return the showBorder
     */
    public boolean isShowBorder() {
        return showBorder;
    }

    /**
     * @param showBorder
     *            the showBorder to set
     */
    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    /**
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor
     *            the borderColor to set
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the useAlternation
     */
    public boolean isAlternatRows() {
        return alternateRows;
    }

    /**
     * @param useAlternation
     *            the useAlternation to set
     */
    public void setAlternateRows(boolean alternateRows) {
        this.alternateRows = alternateRows;
    }

    /**
     * @return the alternationColor
     */
    public Color getAlternationColor() {
        return alternationColor;
    }

    /**
     * @param alternationColor
     *            the alternationColor to set
     */
    public void setAlternationColor(Color alternationColor) {
        this.alternationColor = alternationColor;
    }

    public void decorateKey(JComponent comp, int row) {
        if (alternateRows && (row & 0x01) == 0x01) {
            comp.setOpaque(true);
            comp.setBackground(alternationColor);
        }
        if (showBorder) {
            // String text = label.getText();
            // if (!text.endsWith(" ")) {
            // label.setText(text + " ");
            // }
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor),
                    comp.getBorder()));
        } else {
            // String text = label.getText();
            // if (!text.endsWith(":")) {
            // label.setText(text + ":");
            // }
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(1, 1, 1, 1),
                    comp.getBorder()));
        }
    }

    public void decorateValue(JComponent comp, int row) {
        if (alternateRows && (row & 0x01) == 0x01) {
            comp.setOpaque(true);
            comp.setBackground(alternationColor);
        }
        if (showBorder) {
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 1, 1, borderColor),
                    comp.getBorder()));
        } else {
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(1, 0, 1, 1),
                    comp.getBorder()));
        }
    }

    public void decorateHeaderKey(JComponent comp, int row) {
        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
    }

    public void decorateHeaderValue(JComponent comp, int row) {
        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PropertyVizStyle [showBorder=" + showBorder + ", borderColor="
                + borderColor + ", alternateRows=" + alternateRows
                + ", alternationColor=" + alternationColor + "]";
    }

}
