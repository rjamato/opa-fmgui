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
 *  File Name: ResourceLinkTabView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/24 20:55:26  jijunwan
 *  Archive Log:    added connect icon to link tab
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/18 13:43:21  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the Link/Path tab on a Link or Path tabbed pane to house
 *  2 stacked labels for the to/from nodes on a link or path
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UIImages;
import com.intel.stl.ui.common.view.ComponentFactory;

public class ResourceLinkTabView extends JPanel {

    private static final long serialVersionUID = -7811464011121142244L;

    private static final byte FROM_NODE_NAME_IDX = 0;

    private static final byte TO_NODE_NAME_IDX = 1;

    private JLabel lblFromNodeName;

    private JLabel lblToNodeName;

    private final String[] nodeNames;

    public ResourceLinkTabView(String[] nodeNames) {
        super();
        this.nodeNames = nodeNames;
        initComponent();
    }

    protected void initComponent() {

        setLayout(new GridBagLayout());
        setOpaque(false);
        // lblFromNodeName = new JLabel(nodeNames[FROM_NODE_NAME_IDX]);
        // lblToNodeName = new JLabel(nodeNames[TO_NODE_NAME_IDX]);
        lblFromNodeName =
                ComponentFactory.getH5Label(nodeNames[FROM_NODE_NAME_IDX],
                        Font.PLAIN);
        lblToNodeName =
                ComponentFactory.getH5Label(nodeNames[TO_NODE_NAME_IDX],
                        Font.PLAIN);
        lblToNodeName.setIcon(UIImages.CONNECT_GRAY.getImageIcon());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.ipadx = 10;
        add(lblFromNodeName, gbc);

        gbc.gridy++;
        add(lblToNodeName, gbc);
    }

    public JPanel getMainComponent() {
        return this;
    }

    /**
     * @return the lblFromNodeName
     */
    public JLabel getLblFromNodeName() {
        return lblFromNodeName;
    }

    /**
     * @param lblFromNodeName
     *            the lblFromNodeName to set
     */
    public void setLblFromNodeName(JLabel lblFromNodeName) {
        this.lblFromNodeName = lblFromNodeName;
    }

    /**
     * @return the lblToNodeName
     */
    public JLabel getLblToNodeName() {
        return lblToNodeName;
    }

    /**
     * @param lblToNodeName
     *            the lblToNodeName to set
     */
    public void setLblToNodeName(JLabel lblToNodeName) {
        this.lblToNodeName = lblToNodeName;
    }

    public void setLabelProperties(boolean highlight) {

        if (highlight) {
            lblFromNodeName.setBackground(UIConstants.INTEL_WHITE);
            lblFromNodeName.setForeground(UIConstants.INTEL_DARK_GRAY);

            lblToNodeName.setBackground(UIConstants.INTEL_WHITE);
            lblToNodeName.setForeground(UIConstants.INTEL_DARK_GRAY);

            lblToNodeName.setIcon(UIImages.CONNECT_GRAY.getImageIcon());
        } else {
            lblFromNodeName.setBackground(UIConstants.INTEL_BLUE);
            lblFromNodeName.setForeground(UIConstants.INTEL_WHITE);

            lblToNodeName.setBackground(UIConstants.INTEL_BLUE);
            lblToNodeName.setForeground(UIConstants.INTEL_WHITE);

            lblToNodeName.setIcon(UIImages.CONNECT_WHITE.getImageIcon());
        }
    }

}
