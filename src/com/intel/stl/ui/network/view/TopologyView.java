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
 *  File Name: TopologyView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/08/17 18:54:15  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/10 20:21:01  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/12/11 18:48:38  fernande
 *  Archive Log:    Switch from log4j to slf4j+logback
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/03 23:06:14  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 01:24:17  jijunwan
 *  Archive Log:    moved TopologyView to view package
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/10/09 21:29:45  jijunwan
 *  Archive Log:    new Topology Viz
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/08/12 21:08:15  jijunwan
 *  Archive Log:    enable buttons only after we have a valid graph
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/07 19:06:05  jijunwan
 *  Archive Log:    minor improvements:
 *  Archive Log:    1) null check
 *  Archive Log:    2) stop previous context switching when we need to switch to a new one
 *  Archive Log:    3) auto fit when we resize split panes
 *  Archive Log:    4) put layout execution on background
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/03 14:18:44  rjtierne
 *  Archive Log:    Removed the scrollpane from the topology page view
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/06/26 21:23:01  jijunwan
 *  Archive Log:    layout adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/24 20:29:52  rjtierne
 *  Archive Log:    Added a split pane to the Topology page with the map as the
 *  Archive Log:    top component, and the Resource Detail pages as the bottom component
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/23 04:56:56  jijunwan
 *  Archive Log:    new topology code to support interactions with a topology graph
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/17 16:55:41  jijunwan
 *  Archive Log:    pan and zoom at current mouse point; shift+mouse drag to move nodes on screen; right control to clear selections; undoable node collapse and expand
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/28 15:54:40  jijunwan
 *  Archive Log:    minor adjustments on topology: 1) scale vertices when they begin to overlap each other, 2) pick vertex first when we find cell under a mouse point
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/27 22:08:10  jijunwan
 *  Archive Log:    added tooltip for topology
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/27 13:48:39  jijunwan
 *  Archive Log:    added connection highlight
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/23 19:47:54  jijunwan
 *  Archive Log:    init version of topology page
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network.view;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intel.stl.ui.common.IBackgroundService;
import com.intel.stl.ui.monitor.view.TreeView;
import com.intel.stl.ui.network.TopologyTreeSelectionModel;

public class TopologyView extends TreeView {
    private static final long serialVersionUID = -1174727662197941419L;

    private static final Logger log = LoggerFactory
            .getLogger(TopologyView.class);

    private JSplitPane spltPane;

    private JSplitPane graphSpltPane;

    private TopologyGuideView guideView;

    private TopologyGraphView graphView;

    private ResourceView resourceView;

    /**
     * Description:
     * 
     */
    public TopologyView(IBackgroundService graphService,
            IBackgroundService outlineService) {
        super(graphService, outlineService);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.view.TreeView#createTree()
     */
    @Override
    protected JTree createTree() {
        JTree tree = super.createTree();
        tree.setSelectionModel(new TopologyTreeSelectionModel());
        return tree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.monitor.view.TreeView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {
        if (spltPane == null) {
            spltPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            spltPane.setResizeWeight(.8);
            spltPane.setDividerSize(5);

            JComponent comp = getGraphComponent();
            spltPane.setTopComponent(comp);

            comp = getResourceView();
            spltPane.setBottomComponent(comp);
        }

        return spltPane;
    }

    protected JComponent getGraphComponent() {
        if (graphSpltPane == null) {
            graphSpltPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            graphSpltPane.setResizeWeight(0.1);
            graphSpltPane.setDividerSize(5);
            graphSpltPane.setOneTouchExpandable(true);

            final TopologyGuideView guideView = getGuideView();
            graphSpltPane.setLeftComponent(guideView);
            // graphSpltPane.addPropertyChangeListener(
            // JSplitPane.DIVIDER_LOCATION_PROPERTY,
            // new PropertyChangeListener() {
            // @Override
            // public void propertyChange(PropertyChangeEvent evt) {
            // guideView.update();
            // }
            // });
            // graphSpltPane.addComponentListener(new ComponentAdapter() {
            // /*
            // * (non-Javadoc)
            // *
            // * @see
            // * java.awt.event.ComponentAdapter#componentResized(java.awt
            // * .event.ComponentEvent)
            // */
            // @Override
            // public void componentResized(ComponentEvent e) {
            // guideView.update();
            // }
            // });

            TopologyGraphView graphView = getGraphView();
            graphSpltPane.setRightComponent(graphView);
        }
        return graphSpltPane;
    }

    public TopologyGuideView getGuideView() {
        if (guideView == null) {
            guideView = new TopologyGuideView(outlineService);
        }
        return guideView;
    }

    public TopologyGraphView getGraphView() {
        if (graphView == null) {
            graphView = new TopologyGraphView(graphService);
        }
        return graphView;
    }

    public ResourceView getResourceView() {
        if (resourceView == null) {
            resourceView = new ResourceView();
        }
        return resourceView;
    }

    /**
     * <i>Description:</i>
     * 
     */
    public void initView() {
        getGraphView().initView();
        getGuideView().initView();
    }

}
