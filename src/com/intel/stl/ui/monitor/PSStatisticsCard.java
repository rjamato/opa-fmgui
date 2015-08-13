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
 *  File Name: PSStatisticsCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.14.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
