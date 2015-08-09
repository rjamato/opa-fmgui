/**
 * INTEL CONFIDENTIAL
 * Copyright (c) ${year} Intel Corporation All Rights Reserved.
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
 *  File Name: NodeRenderer.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2014/12/11 18:46:33  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/09 21:24:50  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/02 19:24:32  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/08/05 18:39:08  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/09 21:17:35  jijunwan
 *  Archive Log:    renamed CA_ICON to FI_ICON, CA_GROUP_ICON to FI_GROUP_ICON
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/24 20:23:32  rjtierne
 *  Archive Log:    Changed HCA to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/05 17:34:11  jijunwan
 *  Archive Log:    added vFabric into Tree View
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/18 22:50:35  rjtierne
 *  Archive Log:    No longer using specific group types to render groups;
 *  Archive Log:    using DEVICE_GROUP only.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/15 18:47:45  rjtierne
 *  Archive Log:    Updated renderer to accommodate new device groups
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/24 18:33:46  rjtierne
 *  Archive Log:    Added icons for HCA and Switch groups
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/23 19:55:25  rjtierne
 *  Archive Log:    Handled new INACTIVE_PORT case
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:52:26  rjtierne
 *  Archive Log:    Moved from common.view to monitor.view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:37:58  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class renders tree nodes with select Intel approved icons
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.monitor.tree.FVResourceNode;

/**
 * @author tierney
 * 
 */
public class NodeRenderer extends DefaultTreeCellRenderer {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5607199679146131774L;

    private static Logger log = LoggerFactory.getLogger(NodeRenderer.class);

    public NodeRenderer() {
    } // NodeRenderer

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

        FVResourceNode node = null;

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);

        try {
            node = (FVResourceNode) value;
            setIcon(node.getType().getIcon());
        } catch (ClassCastException cce) {
            log.debug("NodeRenderer unable to cast value to FVResourceNode!");
        }

        return this;
    } // getTreeCellRendererComponent

} // class NodeRenderer
