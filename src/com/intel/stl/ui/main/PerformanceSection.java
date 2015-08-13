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
 *  File Name: PerformanceSection.java
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

import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.ui.common.ChartsSectionController;
import com.intel.stl.ui.common.view.ChartsSectionView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.CompactGroupFactory;
import com.intel.stl.ui.performance.IGroupController;

/**
 * @author jijunwan
 * 
 */
public class PerformanceSection extends ChartsSectionController {
    private IGroupController[] utilGroups;

    private IGroupController[] errGroups;

    public PerformanceSection(ChartsSectionView view,
            MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getSubnetPerformance(), helpAction.getHelpSet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.ChartsSectionController#getUtilGroups()
     */
    @Override
    protected IGroupController[] getUtilGroups() {
        if (utilGroups == null) {
            String[] sourceNames =
                    new String[] { DefaultDeviceGroup.ALL.getName(),
                            DefaultDeviceGroup.SW.getName() };
            utilGroups =
                    new IGroupController[] {
                            CompactGroupFactory.createBandwidthGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createPacketRateGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames) };
        }
        return utilGroups;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.ChartsSectionController#getErrorGroups()
     */
    @Override
    protected IGroupController[] getErrorGroups() {
        if (errGroups == null) {
            String[] sourceNames =
                    new String[] { DefaultDeviceGroup.ALL.getName(),
                            DefaultDeviceGroup.SW.getName() };
            errGroups =
                    new IGroupController[] {
                            CompactGroupFactory.createCongestionGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createSmaCongestionGroup(
                                    eventBus, topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createSignalIntegrityGroup(
                                    eventBus, topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createBubbleGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createSecurityGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames),
                            CompactGroupFactory.createRoutingGroup(eventBus,
                                    topN, DataType.INTERNAL,
                                    HistoryType.CURRENT, sourceNames) };
        }
        return errGroups;
    }

}
