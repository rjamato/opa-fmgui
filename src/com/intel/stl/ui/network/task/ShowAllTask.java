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
 *  File Name: ShowAllTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/28 14:00:33  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/04/10 20:20:32  fernande
 *  Archive Log:    Changed TopologyView to be passed two background services (graphService and outlineService) which now reside in FabricController and can be properly shutdown when an error occurs.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/11/03 23:06:13  jijunwan
 *  Archive Log:    improvement on topology view - drawing graph on background
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:25  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network.task;

import javax.swing.Icon;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.LayoutChange;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;
import com.intel.stl.ui.network.TopologyTreeModel;

public class ShowAllTask extends TopologyUpdateTask {
    private TopologyTreeModel tmpTreeMode;

    private final LayoutType defaultLayout = LayoutType.TREE_SLASH;

    /**
     * Description:
     * 
     * @param controller
     * @param source
     * @param selectedResources
     */
    public ShowAllTask(TopologyGraphController controller, Object source,
            FVResourceNode[] selectedResources) {
        super(controller, source, selectedResources);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.task.TopologyUpdateTask#preBackgroundTask(com
     * .intel.stl.ui.common.ICancelIndicator)
     */
    @Override
    public void preBackgroundTask(ICancelIndicator indicator, TopGraph oldGraph) {
        String name = selectedResources[0].getName();
        Icon icon = selectedResources[0].getType().getIcon();
        resourceController.showAll(name, icon,
                controller.getFullTopTreeModel(), oldGraph);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#applyChanges(com.
     * intel.stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public boolean applyChanges(ICancelIndicator indicator, TopGraph newGraph) {
        tmpTreeMode = controller.getFullTopTreeModel();
        IModelChange change = new LayoutChange(defaultLayout, tmpTreeMode);
        change.execute(newGraph, indicator);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#onDone(com.intel.
     * stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public void onSuccess(final ICancelIndicator indicator, TopGraph newGraph) {
        super.onSuccess(indicator, newGraph);

        controller.setTopTreeModel(tmpTreeMode);

        if (!indicator.isCancelled() && source != controller) {
            controller.clearTreeSelection();
        }

        submitGraphTask(new Runnable() {
            @Override
            public void run() {
                graphView.clearEdges();

                if (!indicator.isCancelled()) {
                    graphView.clearSelection();
                }

                graphView.updateGraph();
            }
        });

        submitOutlineTask(new Runnable() {
            @Override
            public void run() {
                guideView.clearEdges();

                if (!indicator.isCancelled()) {
                    guideView.clearSelection();
                }

                guideView.updateGraph();
            }
        });

        guideView.setSelectedResources(selectedResources);
        setLayout(defaultLayout);
    }

}
