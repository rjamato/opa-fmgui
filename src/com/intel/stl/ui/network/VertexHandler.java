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
 *  File Name: VertexHandler.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2014/11/03 23:06:12  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import com.intel.stl.ui.common.UIConstants;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxVertexHandler;
import com.mxgraph.view.mxCellState;

public class VertexHandler extends mxVertexHandler {

    /**
     * Description:
     * 
     * @param graphComponent
     * @param state
     */
    public VertexHandler(mxGraphComponent graphComponent, mxCellState state) {
        super(graphComponent, state);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.mxgraph.swing.handler.mxVertexHandler#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        Stroke selStroke = getSelectionStroke();

        Rectangle bounds = getState().getRectangle();
        if (bounds.height == 0) {
            bounds.height = 1;
        }
        if (bounds.width == 0) {
            bounds.width = 1;
        }
        if (bounds.width < 5 || bounds.height < 5) {
            selStroke = UIConstants.VERTEX_SEL_STROKE2;
        }

        if (g.hitClip(bounds.x, bounds.y, bounds.width, bounds.height)) {
            Graphics2D g2 = (Graphics2D) g;

            Stroke stroke = g2.getStroke();
            try {
                g2.setStroke(selStroke);
                g2.setColor(getSelectionColor());
                g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            } finally {
                g2.setStroke(stroke);
            }
        }

        super.paint(g);
    }

}
