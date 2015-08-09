/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2015 Intel Corporation All Rights Reserved.
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
 *  File Name: DevicesPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/03/24 17:46:09  jijunwan
 *  Archive Log:    init version of DeviceGroup editor
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.admin.view.devicegroups;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.intel.stl.api.StringUtils;
import com.intel.stl.ui.admin.impl.devicegroups.DeviceNode;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.view.ComponentFactory;

public class DevicesPanel extends JPanel {
    private static final long serialVersionUID = 1350511780844381213L;

    public static final String GUID = "guid";

    public static final String DESC = "desc";

    private JPanel ctrPanel;

    private JCheckBox guidBox;

    private JTree tree;

    private boolean showGUID;

    public DevicesPanel() {
        super();
        initComponent();
    }

    protected void initComponent() {
        setLayout(new BorderLayout());
        JPanel panel = getControlPanel();
        add(panel, BorderLayout.NORTH);

        JTree tree = getTree();
        JScrollPane pane = new JScrollPane(tree);
        add(pane, BorderLayout.CENTER);
    }

    protected JPanel getControlPanel() {
        if (ctrPanel == null) {
            ctrPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 2));
            ctrPanel.setOpaque(false);

            JLabel label =
                    ComponentFactory.getH4Label(
                            STLConstants.K2135_OPTIONS.getValue(), Font.PLAIN);
            ctrPanel.add(label);

            guidBox = new JCheckBox(STLConstants.K2136_USE_GUID.getValue());
            guidBox.setForeground(UIConstants.INTEL_DARK_GRAY);
            guidBox.setFont(UIConstants.H4_FONT);
            ctrPanel.add(guidBox);
        }
        return ctrPanel;
    }

    protected JTree getTree() {
        if (tree == null) {
            tree = new JTree();
            tree.setCellRenderer(new DescRenderer());
            tree.setRootVisible(false);
            tree.setShowsRootHandles(true);
        }
        return tree;
    }

    public void setTreeModel(TreeModel model) {
        tree.setModel(model);
    }

    public void addOptionsListener(ActionListener listener) {
        guidBox.addActionListener(listener);
    }

    public void removeOptionsListener(ActionListener listener) {
        guidBox.removeActionListener(listener);
    }

    public void setSelectionModel(TreeSelectionModel selectionModel) {
        tree.setSelectionModel(selectionModel);
    }

    // not used for now
    public void showGUID(boolean b) {
        if (showGUID != b) {
            showGUID = b;
            tree.setCellRenderer(b ? new GUIRenderer() : new DescRenderer());
        }
    }

    class GUIRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 3313950353093825301L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
         * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
         * boolean)
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            JLabel label =
                    (JLabel) super.getTreeCellRendererComponent(tree, value,
                            sel, expanded, leaf, row, hasFocus);
            if (value instanceof DeviceNode) {
                DeviceNode node = (DeviceNode) value;
                label.setIcon(node.getType().getIcon());
                label.setEnabled(!node.isSelected());
                if (node.getGuid() != 0) {
                    label.setText(StringUtils.longHexString(node.getGuid()));
                }
            }
            return label;
        }
    }

    class DescRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = 3313950353093825301L;

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent
         * (javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
         * boolean)
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            JLabel label =
                    (JLabel) super.getTreeCellRendererComponent(tree, value,
                            sel, expanded, leaf, row, hasFocus);
            if (value instanceof DeviceNode) {
                DeviceNode node = (DeviceNode) value;
                label.setIcon(node.getType().getIcon());
                label.setEnabled(!node.isSelected());
            }
            return label;
        }
    }

}
