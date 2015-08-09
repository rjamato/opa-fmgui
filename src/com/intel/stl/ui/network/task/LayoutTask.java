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
 *  File Name: LayoutTask.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/04/28 14:00:33  jijunwan
 *  Archive Log:    1) improved topology viz to use TopGraph copy for outline display. This will avoid graph and outline views share internal graph view that may cause sync issues.
 *  Archive Log:    2) added more debug info in log
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

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.network.IModelChange;
import com.intel.stl.ui.network.LayoutChange;
import com.intel.stl.ui.network.TopGraph;
import com.intel.stl.ui.network.TopologyGraphController;

public class LayoutTask extends TopologyUpdateTask {
    private final LayoutType type;

    private final IModelChange preChange;

    /**
     * Description:
     * 
     * @param graphView
     * @param guideView
     * @param type
     * @param preChange
     */
    public LayoutTask(TopologyGraphController controller, LayoutType type,
            IModelChange preChange) {
        super(controller, null, null);
        this.type = type;
        this.preChange = preChange;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#createNewGraph(com
     * .intel.stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public TopGraph createNewGraph(ICancelIndicator indicator, TopGraph oldGraph) {
        return graphView.getGraph();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.network.TopologyUpdateTask#applyChanges(com.
     * intel.stl.ui.common.ICancelIndicator, com.intel.stl.ui.network.TopGraph)
     */
    @Override
    public boolean applyChanges(ICancelIndicator indicator, TopGraph newGraph) {
        if (preChange != null) {
            preChange.execute(newGraph, indicator);
        }
        IModelChange change =
                new LayoutChange(type, controller.getTopTreeModel());
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

        graphView.enableUndo(false);
        graphView.enableRedo(false);
        graphView.setLayoutType(type);
        controller.setCurrentLayout(type);
    }

}
