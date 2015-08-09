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
 *  File Name: TopEdgeHandler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import com.intel.stl.ui.common.UIConstants;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellHandler;
import com.mxgraph.view.mxCellState;

public class MarkedEdgeHandler extends mxCellHandler {

    /**
     * Description:
     * 
     * @param graphComponent
     * @param state
     */
    public MarkedEdgeHandler(mxGraphComponent graphComponent, mxCellState state) {
        super(graphComponent, state);
        handlesVisible = false;
    }

    @Override
    public Color getSelectionColor() {
        return UIConstants.EDGE_MARK_COLOR;
    }

    /**
     * 
     */
    @Override
    public Stroke getSelectionStroke() {
        return UIConstants.EDGE_MARK_STROKE;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        RenderingHints tmp = g2.getRenderingHints();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Stroke stroke = g2.getStroke();
            g2.setStroke(getSelectionStroke());
            g.setColor(getSelectionColor());

            Point last = state.getAbsolutePoint(0).getPoint();

            for (int i = 1; i < state.getAbsolutePointCount(); i++) {
                Point current = state.getAbsolutePoint(i).getPoint();
                Line2D line =
                        new Line2D.Float(last.x, last.y, current.x, current.y);

                Rectangle bounds =
                        g2.getStroke().createStrokedShape(line).getBounds();

                if (g.hitClip(bounds.x, bounds.y, bounds.width, bounds.height)) {
                    g2.draw(line);
                }

                last = current;
            }

            g2.setStroke(stroke);
            super.paint(g);

        } finally {
            g2.setRenderingHints(tmp);
        }
    }
}
