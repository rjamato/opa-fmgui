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
 *  File Name: NodeStatesCard.java
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

import java.util.EnumMap;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.DistributionPieController;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.NodeStatesViewOld;
import com.intel.stl.ui.model.StateLongTypeViz;

/**
 * @author jijunwan
 * 
 */
public class NodeStatesCardOld extends
        BaseCardController<ICardListener, NodeStatesViewOld> {
    private final DistributionPieController pieController;

    public NodeStatesCardOld(NodeStatesViewOld view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        pieController =
                new DistributionPieController(view.getDistributionPiePanel(),
                        StateLongTypeViz.names, null, StateLongTypeViz.colors,
                        2);
    }

    /**
     * @return the pieController
     */
    public DistributionPieController getPieController() {
        return pieController;
    }

    /**
     * @param swStates
     * @param totalSWs
     */
    public void updateStates(EnumMap<NoticeSeverity, Integer> states, int total) {
        final int[] counts =
                StateLongTypeViz.getDistributionValues(states, total);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                pieController.setDistribution(counts);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#getCardListener()
     */
    @Override
    public ICardListener getCardListener() {
        return this;
    }

}
