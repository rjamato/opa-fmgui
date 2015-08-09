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
 *  File Name: PSStatisticsCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14  2015/03/10 18:43:12  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/10/22 16:40:15  jijunwan
 *  Archive Log:    separated other ports viz for the ports not in a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/10/15 22:00:22  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/21 13:48:17  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/11 19:27:33  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/26 20:29:34  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/05 17:31:14  jijunwan
 *  Archive Log:    renamed DeviceGroupStatistics to DevicesStatistics since we are using it for vFabric as well
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/18 22:47:00  rjtierne
 *  Archive Log:    Removed method updateEventsDistribution()
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/15 20:31:53  rjtierne
 *  Archive Log:    Constants K0014 and K0024 were renamed to designate
 *  Archive Log:    active nodes and ports
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/15 18:29:01  rjtierne
 *  Archive Log:    Added updateStatistics() method
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/09 19:48:14  rjtierne
 *  Archive Log:    Add the portsController
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:18:23  rjtierne
 *  Archive Log:    Renamed from PerfSummaryStatisticsCard and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:02  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the statistics card on the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DevicesStatistics;
import com.intel.stl.ui.model.GroupStatistics;
import com.intel.stl.ui.monitor.view.PSStatisticsCardView;

public class PSStatisticsCard extends
        BaseCardController<ICardListener, PSStatisticsCardView> {
    private final PSNodesDetailsController nodeController;

    private final PSPortsDetailsController portController;

    public PSStatisticsCard(PSStatisticsCardView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        nodeController =
                new PSNodesDetailsController(
                        STLConstants.K0014_ACTIVE_NODES.getValue(),
                        view.getNodesPanel());

        portController =
                new PSPortsDetailsController(
                        STLConstants.K0024_ACTIVE_PORTS.getValue(),
                        view.getPortsPanel());
        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getStatistics(), helpAction.getHelpSet());
    }

    /**
     * @return the nodesController
     */
    public PSNodesDetailsController getNodesController() {
        return nodeController;
    }

    /**
     * @return the portsController
     */
    public PSPortsDetailsController getPortsController() {
        return portController;
    }

    public void updateStatistics(DevicesStatistics dgStats) {

        long totalPorts = dgStats.getNumAtivePorts();
        portController.setDeviceTypes(totalPorts, dgStats.getPortTypesDist());
        portController.setFlowType(dgStats.getInternalPorts(),
                dgStats.getExternalPorts());

        int totalNodes = dgStats.getNumNodes();
        nodeController.setTypes(totalNodes, dgStats.getOtherPorts(),
                dgStats.getNodeTypesDist());
    }

    public String getTitle(GroupStatistics stats) {
        return STLConstants.K0007_SUBNET.getValue() + ": " + stats.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ICardController#clear()
     */
    @Override
    public void clear() {
        nodeController.clear();
        portController.clear();
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
