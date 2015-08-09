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
 *  File Name: TopTreeSelectionModel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/09/02 19:24:33  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/07 19:06:05  jijunwan
 *  Archive Log:    minor improvements:
 *  Archive Log:    1) null check
 *  Archive Log:    2) stop previous context switching when we need to switch to a new one
 *  Archive Log:    3) auto fit when we resize split panes
 *  Archive Log:    4) put layout execution on background
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;

import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

/**
 * Special tree selection model that only allows
 * 1) selections on the same level
 * 2) forbid selection on inactive port
 * 3) ignore selection on switch zero ports if they are combined with other
 * selections
 */
public class TopologyTreeSelectionModel extends DefaultTreeSelectionModel {
    private static final long serialVersionUID = 8288776306386190352L;

    private final Set<TreePath> switchZeroPorts = new HashSet<TreePath>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.DefaultTreeSelectionModel#setSelectionPaths(javax.swing
     * .tree.TreePath[])
     */
    @Override
    public void setSelectionPaths(TreePath[] pPaths) {
        if (pPaths.length <= 0) {
            return;
        }

        if (pPaths[0] != null) {
            int len = pPaths[0].getPathCount();
            super.setSelectionPaths(filterPaths(pPaths, len, false));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.tree.DefaultTreeSelectionModel#addSelectionPaths(javax.swing
     * .tree.TreePath[])
     */
    @Override
    public void addSelectionPaths(TreePath[] paths) {
        if (selection != null && selection.length > 0) {
            int len = selection[0].getPathCount();
            super.addSelectionPaths(filterPaths(paths, len, true));
        } else {
            super.addSelectionPaths(paths);
        }
    }

    protected TreePath[] filterPaths(TreePath[] paths, int len, boolean addMode) {
        List<TreePath> validPaths = new ArrayList<TreePath>();
        List<Integer> currentSwitchZeroPorts = new ArrayList<Integer>();
        // filter out inactive ports
        for (TreePath path : paths) {
            if (path == null) {
                continue;
            }
            FVResourceNode node = (FVResourceNode) path.getLastPathComponent();
            if (path.getPathCount() == len
                    && node.getType() != TreeNodeType.INACTIVE_PORT) {
                validPaths.add(path);
                if (isSwitchZeroPort(node)) {
                    currentSwitchZeroPorts.add(validPaths.size() - 1);
                }
            }
        }
        // filter out switch zero ports if they are combined with other
        // selections
        int totalSelections = validPaths.size();
        int totalSwitchZeroPorts = currentSwitchZeroPorts.size();
        if (addMode) {
            totalSelections += (selection == null ? 0 : selection.length);
            totalSwitchZeroPorts += switchZeroPorts.size();
        }
        if (totalSwitchZeroPorts != totalSelections) {
            for (int i = currentSwitchZeroPorts.size() - 1; i >= 0; i--) {
                int index = currentSwitchZeroPorts.get(i);
                validPaths.remove(index);
            }
            if (addMode) {
                removeSelectionPaths(switchZeroPorts.toArray(new TreePath[0]));
            }
            switchZeroPorts.clear();
        } else if (!currentSwitchZeroPorts.isEmpty()) {
            if (!addMode) {
                switchZeroPorts.clear();
            }
            for (int index : currentSwitchZeroPorts) {
                switchZeroPorts.add(validPaths.get(index));
            }
        }
        return validPaths.toArray(new TreePath[0]);
    }

    protected boolean isSwitchZeroPort(FVResourceNode node) {
        return node.getId() == 0 && node.getParent() != null
                && node.getParent().getType() == TreeNodeType.SWITCH;
    }
}
