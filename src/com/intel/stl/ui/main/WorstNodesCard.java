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
 *  Functional Group: FabricViewer
 *
 *  File Name: WorstNodesCard.java
 *
 *  Archive Source: 
 *
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.event.JumpDestination;
import com.intel.stl.ui.event.NodeSelectedEvent;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.IWorstNodesListener;
import com.intel.stl.ui.main.view.WorstNodesView;
import com.intel.stl.ui.model.NodeScore;

/**
 * @author jijunwan
 * 
 */
public class WorstNodesCard extends
        BaseCardController<IWorstNodesListener, WorstNodesView> implements
        IWorstNodesListener {
    public WorstNodesCard(WorstNodesView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getWorstNodes(), helpAction.getHelpSet());
    }

    /**
     * @param nodes
     */
    public void updateWorstNodes(final NodeScore[] nodes) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.updateNodes(nodes);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onNodeSelected(int,
     * com.intel.stl.api.subnet.NodeType)
     */
    @Override
    public void jumpTo(int lid, NodeType type, JumpDestination descination) {
        NodeSelectedEvent event =
                new NodeSelectedEvent(lid, type, this, descination);
        eventBus.publish(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public IWorstNodesListener getCardListener() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.clear();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onMore()
     */
    @Override
    public void onMore() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.view.IWorstNodesListener#onSizeChange(int)
     */
    @Override
    public void onSizeChanged(int size) {
        view.setSize(size);
    }

}
