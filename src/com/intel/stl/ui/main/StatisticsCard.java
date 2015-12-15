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
 *  Functional Group: FabricViewer
 *
 *  File Name: StatisticsController.java
 *
 *  Archive Source: $Source$
 * 
 *  Archive Log: $Log$
 *  Archive Log: Revision 1.22  2015/08/17 18:53:38  jijunwan
 *  Archive Log: PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log: - changed frontend files' headers
 *  Archive Log:
 *  Archive Log: Revision 1.21  2015/06/09 18:37:27  jijunwan
 *  Archive Log: PR 129069 - Incorrect Help action
 *  Archive Log: - moved help action from view to controller
 *  Archive Log: - only enable help button when we have HelpID
 *  Archive Log: - fixed incorrect HelpIDs
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#getHelpID()
     */
    @Override
    public String getHelpID() {
        return HelpAction.getInstance().getSubnetStatisticsName();
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
