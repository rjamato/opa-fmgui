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
 *  File Name: StatisticsController.java
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

import java.util.concurrent.TimeUnit;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.performance.SMInfoDataBean;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.DistributionBarController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.StatisticsViewOld;
import com.intel.stl.ui.model.GroupStatistics;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.model.StateShortTypeViz;

/**
 * @author jijunwan
 * 
 */
public class StatisticsCardOld extends
        BaseCardController<ICardListener, StatisticsViewOld> {
    private final DistributionBarController nodeTypesController;

    private final DistributionBarController nodeStatesController;

    private final DistributionBarController portTypesController;

    private final DistributionBarController portStatesController;

    public StatisticsCardOld(StatisticsViewOld view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        nodeTypesController =
                new DistributionBarController(view.getNodeTypesBar(),
                        NodeTypeViz.names, null, NodeTypeViz.colors);
        nodeStatesController =
                new DistributionBarController(view.getNodeStatesBar(),
                        StateShortTypeViz.names, null, StateShortTypeViz.colors);
        portTypesController =
                new DistributionBarController(view.getPortTypesBar(),
                        NodeTypeViz.names, null, NodeTypeViz.colors);
        portStatesController =
                new DistributionBarController(view.getPortStatesBar(),
                        StateShortTypeViz.names, null, StateShortTypeViz.colors);
    }

    /**
     * @return the nodeTypesController
     */
    public DistributionBarController getNodeTypesController() {
        return nodeTypesController;
    }

    /**
     * @return the nodeStatesController
     */
    public DistributionBarController getNodeStatesController() {
        return nodeStatesController;
    }

    /**
     * @return the portTypesController
     */
    public DistributionBarController getPortTypesController() {
        return portTypesController;
    }

    /**
     * @return the portStatesController
     */
    public DistributionBarController getPortStatesController() {
        return portStatesController;
    }

    public void updateStatistics(final GroupStatistics sta) {
        final int totalNodes = sta.getNumNodes();
        nodeTypesController.setDistribution(NodeTypeViz
                .getDistributionValues2(sta.getNodeTypesDist()));
        nodeStatesController.setDistribution(StateShortTypeViz
                .getDistributionValues(sta.getNumFailedNodes(),
                        sta.getNumSkippedNodes(), totalNodes));

        final long totalPorts = sta.getNumActivePorts();
        portTypesController.setDistribution(NodeTypeViz
                .getDistributionValues(sta.getPortTypesDist()));
        portStatesController.setDistribution(StateShortTypeViz
                .getDistributionValues(sta.getNumFailedPorts(),
                        sta.getNumSkippedPorts(), totalPorts));

        // only need to do it once
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.setTitle(getTitle(sta));
                view.setDuration(sta.getMsmUptimeInSeconds(), TimeUnit.SECONDS);
                view.setLinks(UIConstants.INTEGER.format(sta.getNumLinks()));
                view.setNodes(UIConstants.INTEGER.format(totalNodes));
                view.setPorts(UIConstants.INTEGER.format(totalPorts));
                SMInfoDataBean msm = sta.getMasterSM();
                if (msm != null) {
                    String name = msm.getSmNodeDesc();
                    String description =
                            STLConstants.K0026_LID.getValue()
                                    + ": "
                                    + StringUtils.intHexString(msm.getLid())
                                    + " "
                                    + STLConstants.K0027_PORT_GUID.getValue()
                                    + ": "
                                    + StringUtils.longHexString(msm
                                            .getSmPortGuid());
                    view.setMsmName(name, description);
                }
            }
        });

    }

    public String getTitle(GroupStatistics sta) {
        return STLConstants.K0007_SUBNET.getValue() + ": " + sta.getName();
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
