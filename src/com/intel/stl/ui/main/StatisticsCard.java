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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.StringUtils;
import com.intel.stl.api.performance.SMInfoDataBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.view.StatisticsView;
import com.intel.stl.ui.model.GroupStatistics;

/**
 * @author jijunwan
 * 
 */
public class StatisticsCard extends
        BaseCardController<ICardListener, StatisticsView> {
    private final StaDetailsController nodesController;

    private final StaDetailsController portsController;

    private Timer viewClearTimer;

    public StatisticsCard(StatisticsView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);
        nodesController =
                new StaDetailsController(
                        STLConstants.K0014_ACTIVE_NODES.getValue(),
                        view.getNodesPanel());
        portsController =
                new StaDetailsController(
                        STLConstants.K0024_ACTIVE_PORTS.getValue(),
                        view.getPortsPanel());

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getSubnetName(), helpAction.getHelpSet());
    }

    /**
     * @return the nodesController
     */
    public StaDetailsController getNodesController() {
        return nodesController;
    }

    /**
     * @return the portsController
     */
    public StaDetailsController getPortsController() {
        return portsController;
    }

    public void updateStatistics(final GroupStatistics sta) {
        if (viewClearTimer != null) {
            if (viewClearTimer.isRunning()) {
                viewClearTimer.stop();
            }
            viewClearTimer = null;
        }

        int totalNodes = sta.getNumNodes();
        nodesController.setStates(totalNodes, sta.getNumFailedNodes(),
                sta.getNumSkippedNodes());
        nodesController.setTypes(totalNodes, sta.getNodeTypesDist());

        long totalPorts = sta.getNumActivePorts();
        portsController.setStates(totalPorts, sta.getNumFailedPorts(),
                sta.getNumSkippedPorts());
        portsController.setTypes(totalPorts, sta.getPortTypesDist());

        // only need to do it once
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.setTitle(getTitle(sta));
                view.setDuration(sta.getMsmUptimeInSeconds(), TimeUnit.SECONDS);
                long numLinks = sta.getNumLinks();
                long numHostLinks = sta.getNodeTypesDist().get(NodeType.HFI);
                long numSwitchLinks = 0;
                // for special case b2b, numSwitchLinks should be zero
                if (sta.getNodeTypesDist().get(NodeType.SWITCH) != null) {
                    numSwitchLinks = numLinks - numHostLinks;
                }
                view.setNumSwitchLinks(UIConstants.INTEGER
                        .format(numSwitchLinks));
                view.setNumHostLinks(UIConstants.INTEGER.format(numHostLinks));
                view.setOtherPorts(UIConstants.INTEGER.format(sta
                        .getOtherPorts()));
                SMInfoDataBean[] sms = sta.getSMInfo();
                if (sta.getNumSMs() > 0) {
                    view.setMsmName(sms[0].getSmNodeDesc(),
                            getSMDescription(sms[0]));
                    String[] names = null;
                    String[] descriptions = null;
                    if (sta.getNumSMs() > 1) {
                        names = new String[sms.length - 1];
                        descriptions = new String[sms.length - 1];
                        for (int i = 1; i < sms.length; i++) {
                            names[i - 1] = sms[i].getSmNodeDesc();
                            descriptions[i - 1] = getSMDescription(sms[i]);
                        }
                    }
                    view.setStandbySMNames(names, descriptions);
                } else {
                    view.setMsmName(null, null);
                }
                view.repaint();
            }
        });

    }

    protected String getSMDescription(SMInfoDataBean sm) {
        return STLConstants.K0026_LID.getValue() + ": "
                + StringUtils.intHexString(sm.getLid()) + " "
                + STLConstants.K0027_PORT_GUID.getValue() + ": "
                + StringUtils.longHexString(sm.getSmPortGuid());
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

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseCardController#clear()
     */
    @Override
    public void clear() {
        nodesController.clear();
        portsController.clear();
        if (viewClearTimer == null) {
            viewClearTimer =
                    new Timer(UIConstants.UPDATE_TIME, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (viewClearTimer != null) {
                                view.clear();
                            }
                        }
                    });
            viewClearTimer.setRepeats(false);
        }
        viewClearTimer.restart();
    }

}
