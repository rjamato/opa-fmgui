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
 *  File Name: TreeViewController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6  2014/09/18 14:57:41  jijunwan
 *  Archive Log:    supported jumpTo events
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/08/26 15:15:26  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/16 15:02:16  jijunwan
 *  Archive Log:    fixed a bug happens when we open a tree and then switch to another subnet. It may lead to multiple trees opened.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/29 14:26:34  jijunwan
 *  Archive Log:    added code to select first item on first available tree by default
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/01 16:26:19  rjtierne
 *  Archive Log:    Added addTreeSelectionListener() method to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:52:26  rjtierne
 *  Archive Log:    Moved from common.view to monitor.view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Public interface for the tree view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.intel.stl.ui.monitor.TreeTypeEnum;
import com.intel.stl.ui.monitor.tree.FVTreeModel;

/**
 * @author rjtierne
 * 
 */
public interface TreeViewInterface {

    public void setViewSize(Dimension pSize);

    public JPanel getMainPanel();

    public void setTreeModel(TreeTypeEnum pTreeType, TreeModel pModel);

    public void setTreeSelection(TreeTypeEnum pTreeType);

    public void setTreeSelection(TreeTypeEnum pTreeType, int index);

    public void addTreeSelectionListener(TreeSelectionListener listener);

    public void setSelectionMode(int selectionMode);

    public void expandAndSelectTreePath(FVTreeModel treeModel, TreePath path);

    public void clear();
}
