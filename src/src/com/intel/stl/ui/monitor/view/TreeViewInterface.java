/**
 * Copyright (c) 2015, Intel Corporation
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Intel Corporation nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 *  Archive Log:    Revision 1.9  2015/10/23 19:09:33  jijunwan
 *  Archive Log:    PR 129357 - Be able to hide inactive ports
 *  Archive Log:    - changed input argument to FVTreeModel rather than TreeModel
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/08/17 18:54:25  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/08/05 04:04:48  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
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

    public void setTreeModel(TreeTypeEnum pTreeType, FVTreeModel pModel);

    public void setTreeSelection(TreeTypeEnum pTreeType);

    public void setTreeSelection(TreeTypeEnum pTreeType, int index);

    public void addTreeSelectionListener(TreeSelectionListener listener);

    public void setSelectionMode(int selectionMode);

    public void expandAndSelectTreePath(FVTreeModel treeModel,
            TreePath[] paths, boolean[] isExpanded);

    public void clear();
}
