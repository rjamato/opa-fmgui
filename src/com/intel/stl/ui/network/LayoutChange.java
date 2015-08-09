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
 *  File Name: LayoutChange.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/02 21:26:16  jijunwan
 *  Archive Log:    fixed issued found by FindBugs
 *  Archive Log:    Some auto-reformate
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/05 13:46:23  jijunwan
 *  Archive Log:    new implementation on topology control that uses double models to avoid synchronization issues on model and view
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.model.LayoutType;
import com.intel.stl.ui.network.TreeLayout.Style;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

public class LayoutChange implements IModelChange {
    private final TopologyTreeModel topTreeModel;

    private final LayoutType type;

    /**
     * Description:
     * 
     * @param topTreeModel
     * @param type
     */
    public LayoutChange(LayoutType type, TopologyTreeModel topTreeModel) {
        super();
        this.type = type;
        this.topTreeModel = topTreeModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.IModelChange#execute(com.intel.stl.ui.network
     * .TopGraph, com.intel.stl.ui.common.ICancelIndicator)
     */
    @Override
    public boolean execute(TopGraph graph, ICancelIndicator indicator) {
        mxIGraphLayout layout = null;
        switch (type) {
            case FORCE_DIRECTED:
                graph.expandAll();
                layout = new mxFastOrganicLayout(graph);
                break;
            case HIERARCHICAL:
                graph.expandAll();
                layout = new mxHierarchicalLayout(graph);
                break;
            case TREE_CIRCLE:
                layout = new TreeLayout(graph, topTreeModel, Style.CIRCLE);
                break;
            case TREE_SLASH:
                layout = new TreeLayout(graph, topTreeModel, Style.SLASH);
                break;
            case TREE_LINE:
                layout = new TreeLayout(graph, topTreeModel, Style.LINE);
                break;
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }

        if (layout instanceof TreeLayout) {
            ((TreeLayout) layout).execute(graph.getDefaultParent(), indicator);
        } else {
            layout.execute(graph.getDefaultParent());
        }
        return true;
    }

}
