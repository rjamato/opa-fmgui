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
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: PSGraphSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2015/03/16 14:41:25  jijunwan
 *  Archive Log:    renamed DevieGroup to DefaultDeviceGroup because it's an enum of default DGs, plus we need to use DeviceGroup for the DG definition used in opafm.xml
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/03/10 18:43:12  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2015/02/06 20:49:36  jypak
 *  Archive Log:    1. TaskScheduler changed to handle two threads.
 *  Archive Log:    2. All four(VFInfo, VFPortCounters, GroupInfo, PortCounters) attributes history query related updates.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2015/02/03 21:12:39  jypak
 *  Archive Log:    Short Term PA history changes for Group Info only.
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/01/11 21:36:26  jijunwan
 *  Archive Log:    adapt to latest data structure changes on FM
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/22 18:48:39  jijunwan
 *  Archive Log:    moved ChartsSectionView/ChartsSectionController to common package
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/21 17:30:45  jijunwan
 *  Archive Log:    renamed IDataObserver.Type to DataType, and put it under model package
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/17 16:25:40  jijunwan
 *  Archive Log:    improvement to support sleep mode so we can reduce FE traffic
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/16 21:38:09  jijunwan
 *  Archive Log:    added 3 type error counters
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/16 15:19:30  jijunwan
 *  Archive Log:    applied new performance framework and performance group viz to support bandwidth, packet rate, congestion and integrity data
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/11 19:27:33  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.subnet.DefaultDeviceGroup;
import com.intel.stl.ui.common.ChartsSectionController;
import com.intel.stl.ui.common.view.ChartsSectionView;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DataType;
import com.intel.stl.ui.model.HistoryType;
import com.intel.stl.ui.performance.BaseGroupFactory;
import com.intel.stl.ui.performance.IGroupController;

public class PSGraphSection extends ChartsSectionController {
    private IGroupController[] utilGroups;

    private IGroupController[] errGroups;

    public PSGraphSection(ChartsSectionView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getGeneralSummary(), helpAction.getHelpSet());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.main.ChartsSectionController#getUtilGroups()
     */
    @Override
    protected IGroupController[] getUtilGroups() {
        if (utilGroups == null) {
            String source = DefaultDeviceGroup.ALL.getName();
            utilGroups =
                    new IGroupController[] {
                            BaseGroupFactory.createBandwidthGroup(eventBus,
                                    topN, DataType.ALL, HistoryType.CURRENT,
                                    source),
                            BaseGroupFactory.createPacketRateGroup(eventBus,
                                    topN, DataType.ALL, HistoryType.CURRENT,
                                    source) };
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
            String source = DefaultDeviceGroup.ALL.getName();
            errGroups =
                    new IGroupController[] {
                            BaseGroupFactory.createCongestionGroup(eventBus,
                                    topN, DataType.ALL, HistoryType.CURRENT,
                                    source),
                            BaseGroupFactory.createSmaCongestionGroup(eventBus,
                                    topN, DataType.ALL, HistoryType.CURRENT,
                                    source),
                            BaseGroupFactory.createSignalIntegrityGroup(
                                    eventBus, topN, DataType.ALL,
                                    HistoryType.CURRENT, source),
                            BaseGroupFactory.createBubbleGroup(eventBus, topN,
                                    DataType.ALL, HistoryType.CURRENT, source),
                            BaseGroupFactory.createSecurityGroup(eventBus,
                                    topN, DataType.ALL, HistoryType.CURRENT,
                                    source),
                            BaseGroupFactory.createRoutingGroup(eventBus, topN,
                                    DataType.ALL, HistoryType.CURRENT, source) };
        }
        return errGroups;
    }
}
