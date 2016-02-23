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
 *  File Name: TreeView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.30  2015/10/23 19:09:33  jijunwan
 *  Archive Log:    PR 129357 - Be able to hide inactive ports
 *  Archive Log:    - changed input argument to FVTreeModel rather than TreeModel
 *  Archive Log:
 *  Archive Log:    Revision 1.29  2015/08/17 18:54:24  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.28  2015/08/05 04:04:48  jijunwan
 *  Archive Log:    PR 129359 - Need navigation feature to navigate within FM GUI
 *  Archive Log:    - applied undo mechanism on Performance Page
 *  Archive Log:
 *  Archive Log:    Revision 1.27  2015/06/22 13:11:53  jypak
 *  Archive Log:    PR 128980 - Be able to search devices by name or lid.
 *  Archive Log:    New feature added to enable search devices by name, lid or node guid. The search results are displayed as a tree and when a result node from the tree is selected, original tree is expanded and the corresponding node is highlighted.
 *  Archive Log:
 *  Archive Log:    Revision 1.26  2015/04/10 20:19:04  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.25  2015/04/07 22:14:04  jijunwan
 *  Archive Log:    turn off "Marked Node" on device tree
 *  Archive Log:
 *  Archive Log:    Revision 1.24  2015/02/05 21:21:48  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/11/05 16:23:31  jijunwan
 *  Archive Log:    Removed the code that ignores open operation if a stack panel is already opened. This allows us to re-select a tree node to fire other panels to update their contents
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/10/13 15:16:40  jijunwan
 *  Archive Log:    Improved to automatically open a tree when we intend to open one branch in the tree
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/09/02 19:24:32  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/09/02 19:00:15  jijunwan
 *  Archive Log:    minor improvement - select first row and make it visible when no selections
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/08/26 15:15:26  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/08/12 21:07:32  jijunwan
 *  Archive Log:    fixed default tree issue on TreeView
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/07/16 15:02:16  jijunwan
 *  Archive Log:    fixed a bug happens when we open a tree and then switch to another subnet. It may lead to multiple trees opened.
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/07/11 19:28:16  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/07/07 19:06:06  jijunwan
 *  Archive Log:    minor improvements:
 *  Archive Log:    1) null check
 *  Archive Log:    2) stop previous context switching when we need to switch to a new one
 *  Archive Log:    3) auto fit when we resize split panes
 *  Archive Log:    4) put layout execution on background
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/03 22:21:25  jijunwan
 *  Archive Log:    extended TreeController and TreeView to support multi-selection and programmly operate a tree
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/06/05 19:25:46  jijunwan
 *  Archive Log:    automatically reselect current path when switch from one tree to another
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/06/05 17:34:11  jijunwan
 *  Archive Log:    added vFabric into Tree View
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/05/29 14:26:34  jijunwan
 *  Archive Log:    added code to select first item on first available tree by default
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/05/15 14:33:16  jijunwan
 *  Archive Log:    minor change on tree controller to support generic
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/05/01 16:25:30  rjtierne
 *  Archive Log:    Now provides abstract method getMainComponet(). No
 *  Archive Log:    longer implements TreeSelectionListener. Added new
 *  Archive Log:    addTreeSelectionListener() method which loops through
 *  Archive Log:    the stacked panels and calls addTreeListener() in the
 *  Archive Log:    StackPanel inner class to add the tree listener (TreeController)
 *  Archive Log:     to the trees
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/04/25 15:18:46  rjtierne
 *  Archive Log:    Removed warnings
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/04/24 21:40:21  rjtierne
 *  Archive Log:    Assigned a minimum size to the tree split pane
 *  Archive Log:    to prevent it from being collapsed when the
 *  Archive Log:    main screen is too small
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/04/24 18:34:28  rjtierne
 *  Archive Log:    Renamed SUBNET_TREE to DEVICE_TYPES_TREE
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/04/23 21:10:33  jijunwan
 *  Archive Log:    minor adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/04/23 18:28:22  rjtierne
 *  Archive Log:    Removed main panel and added UI components
 *  Archive Log:    to "this" panel; cleanup
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/04/23 13:45:38  jijunwan
 *  Archive Log:    improvement on TreeView
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/22 21:20:51  rjtierne
 *  Archive Log:    Added up/down arrows on tree stacked panels
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:52:26  rjtierne
 *  Archive Log:    Moved from common.view to monitor.view
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/22 17:56:54  rjtierne
 *  Archive Log:    Changed tree buttons to panels and added MouseListeners
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Tree View for channel adapters, switches, device groups, virtual 
 *  fabrics, and others
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EnumMap;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.intel.stl.ui.common.IBackgroundService;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.IntelSplitPaneUI;
import com.intel.stl.ui.monitor.TreeTypeEnum;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.monitor.tree.FVTreeModel;

/**
 * @author tierney
 * 
 */
public abstract class TreeView extends JPanel implements TreeViewInterface,
        IStack {

    private static final long serialVersionUID = 849119323304459300L;

    /**
     * Tree panel for left component of the split pane
     */
    private JPanel mPnlTree;

    private EnumMap<TreeTypeEnum, StackPanel> stackPanels;

    /**
     * Temporary status label for data panel
     */
    private JLabel mlblStatus;

    protected final IBackgroundService graphService;

    protected final IBackgroundService outlineService;

    private SearchView searchView;

    /**
     * 
     * Description: Constructor for the TreeView class
     * 
     */
    public TreeView(IBackgroundService graphService,
            IBackgroundService outlineService) {
        super();
        this.graphService = graphService;
        this.outlineService = outlineService;
        setOpaque(true);
        initComponents();
    } // TreeView

    /**
     * 
     * Description: Initializes the UI components for the tree view
     * 
     */
    private void initComponents() {

        // Set the layout for this panel
        setLayout(new BorderLayout());

        // Create the main split pane
        JSplitPane spltpnMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spltpnMain.setResizeWeight(.02);
        spltpnMain.setDividerSize(5);

        // Create the tree split pane
        JSplitPane splpnTree = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splpnTree.setMinimumSize(new Dimension(250, 300));
        splpnTree.setResizeWeight(.8);
        splpnTree.setDividerSize(15);
        splpnTree.setUI(new IntelSplitPaneUI());
        splpnTree.setOneTouchExpandable(true);

        // Create the tree panel
        mPnlTree = new JPanel(new GridBagLayout());
        mPnlTree.setBackground(UIConstants.INTEL_WHITE);
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        stackPanels = getStackPanels();
        for (StackPanel panel : stackPanels.values()) {
            mPnlTree.add(panel, gc);
        }
        gc.weighty = 1;
        mPnlTree.add(Box.createGlue(), gc);

        // Create a scroll pane for the tree
        JScrollPane scrpnTree = new JScrollPane();
        ScrollPaneLayout spTreeLayout = new ScrollPaneLayout();
        scrpnTree.createHorizontalScrollBar();
        scrpnTree.createVerticalScrollBar();
        spTreeLayout
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spTreeLayout
                .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrpnTree.getVerticalScrollBar().setUnitIncrement(10);
        scrpnTree.setLayout(spTreeLayout);

        searchView = new SearchView();
        splpnTree.setBottomComponent(searchView);

        // Add the tree panel to the tree scroll pane, and add the tree
        // scroll pane to the top component of the tree split pane.
        // Then add the tree split pane to the left component of the main
        // split pane
        scrpnTree.add(mPnlTree);
        scrpnTree.setViewportView(mPnlTree);
        splpnTree.setTopComponent(scrpnTree);
        spltpnMain.setLeftComponent(splpnTree);

        // Create the data panel and add a label to it
        JComponent rightComp = getDataComponent();
        rightComp.setOpaque(false);

        // Add the main component of the derived class on the right
        // side of the main split pane
        spltpnMain.setRightComponent(getMainComponent());

        add(spltpnMain, BorderLayout.CENTER);

    } // initialize

    public SearchView getSearchView() {
        return searchView;
    }

    protected JComponent getDataComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        mlblStatus = new JLabel("Data Goes Here!!!");
        mlblStatus.setFont(UIConstants.H3_FONT.deriveFont(Font.BOLD));
        panel.add(mlblStatus);
        return panel;
    }

    protected EnumMap<TreeTypeEnum, StackPanel> getStackPanels() {
        EnumMap<TreeTypeEnum, StackPanel> panels =
                new EnumMap<TreeTypeEnum, StackPanel>(TreeTypeEnum.class);
        panels.put(TreeTypeEnum.DEVICE_TYPES_TREE, new StackPanel(
                TreeTypeEnum.DEVICE_TYPES_TREE, createTree(), this));
        panels.put(TreeTypeEnum.DEVICE_GROUPS_TREE, new StackPanel(
                TreeTypeEnum.DEVICE_GROUPS_TREE, createTree(), this));
        panels.put(TreeTypeEnum.VIRTUAL_FABRICS_TREE, new StackPanel(
                TreeTypeEnum.VIRTUAL_FABRICS_TREE, createTree(), this));
        // panels.put(TreeTypeEnum.TOP_10_CONGESTED_TREE, new StackPanel(
        // TreeTypeEnum.TOP_10_CONGESTED_TREE, null));
        return panels;
    }

    protected JTree createTree() {
        JTree res = new JTree();
        res.setModel(null);
        return res;
    }

    @Override
    public void stackChange(TreeTypeEnum stack) {
        for (TreeTypeEnum id : stackPanels.keySet()) {
            if (id != stack) {
                stackPanels.get(id).close();
            }
        }
    }

    protected void showNode(FVResourceNode node) {
        mlblStatus.setText("You have selected node: " + node);
    }

    @Override
    public void addTreeSelectionListener(TreeSelectionListener treeListener) {
        for (TreeTypeEnum id : stackPanels.keySet()) {
            stackPanels.get(id).addTreeListener(treeListener);
        }
    }

    /**
     * 
     * Description: The derived class returns its main component which is put on
     * the right side of the main split pane
     * 
     */
    protected abstract JComponent getMainComponent();

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.trees.TreeViewInterface#setViewSize(java.awt.Dimension
     * )
     */
    @Override
    public void setViewSize(Dimension pSize) {
        this.setPreferredSize(pSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.trees.TreeViewInterface#getMainPanel()
     */
    @Override
    public JPanel getMainPanel() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.hpc.stl.ui.trees.TreeViewInterface#setTreeModel(com.intel.hpc
     * .stl.ui.trees.TreeTypeEnum, com.intel.hpc.stl.ui.trees.FVResourceNode)
     */
    @Override
    public void setTreeModel(final TreeTypeEnum pTreeType,
            final FVTreeModel pModel) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                getStackPanel(pTreeType).setTreeModel(pModel);
            }
        });
    } // setTreeModel

    @Override
    public void setTreeSelection(final TreeTypeEnum pTreeType) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                StackPanel sp = getStackPanel(pTreeType);
                sp.open();
            }
        });
    }

    @Override
    public void setTreeSelection(final TreeTypeEnum pTreeType, final int index) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                StackPanel sp = getStackPanel(pTreeType);
                sp.open();
                sp.select(index);
            }
        });
    }

    protected StackPanel getStackPanel(TreeTypeEnum type) {
        StackPanel sp = stackPanels.get(type);
        if (sp != null) {
            return sp;
        } else {
            throw new IllegalArgumentException("Couldn't find StackPanel for "
                    + type);
        }
    }

    public void setTreeSelection(final FVTreeModel model, final TreePath[] paths) {
        final boolean[] isExpanded = new boolean[paths.length];
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (StackPanel sp : stackPanels.values()) {
                    if (sp.getTreeModel() == model) {
                        sp.select(paths, isExpanded);
                    }
                }
            }
        });
    }

    public void clearTreeSelection(final FVTreeModel model) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (StackPanel sp : stackPanels.values()) {
                    if (sp.getTreeModel() == model) {
                        sp.clearSelection();
                    }
                }
            }
        });
    }

    public void collapseTreePath(FVTreeModel model, TreePath path) {
        for (StackPanel sp : stackPanels.values()) {
            if (sp.getTreeModel() == model) {
                sp.collapse(path);
            }
        }
    }

    public void expandTreePath(FVTreeModel model, TreePath path) {
        for (StackPanel sp : stackPanels.values()) {
            if (sp.getTreeModel() == model) {
                sp.expand(path);
            }
        }
    }

    @Override
    public void expandAndSelectTreePath(FVTreeModel model, TreePath[] paths,
            boolean[] isExpanded) {
        for (StackPanel sp : stackPanels.values()) {
            if (sp.getTreeModel() == model) {
                if (!sp.isOpened()) {
                    sp.open();
                }
                sp.select(paths, isExpanded);
            } else {
                sp.close();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.monitor.view.TreeViewInterface#setSelectionMode(int)
     */
    @Override
    public void setSelectionMode(int selectionMode) {
        for (StackPanel sp : stackPanels.values()) {
            sp.setSelectionMode(selectionMode);
        }
    }

    @Override
    public void clear() {
        setTreeSelection(TreeTypeEnum.DEVICE_TYPES_TREE, 0);
        stackChange(TreeTypeEnum.DEVICE_TYPES_TREE);
    }

} // TreeView
