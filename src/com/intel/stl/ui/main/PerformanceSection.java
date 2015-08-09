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
