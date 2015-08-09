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
 *  File Name: PSInfoSection.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.10  2015/03/10 18:43:12  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/08/05 20:53:33  jijunwan
 *  Archive Log:    fixed a minor bug
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/11 19:27:33  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/05 17:31:14  jijunwan
 *  Archive Log:    renamed DeviceGroupStatistics to DevicesStatistics since we are using it for vFabric as well
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/20 21:26:48  jijunwan
 *  Archive Log:    added events chart to performane subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:13:09  jijunwan
 *  Archive Log:    changed performance controllers a little bit to clean up code or reuse  code
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/18 22:45:34  rjtierne
 *  Archive Log:    Added updateStates() and updateTypes() to support
 *  Archive Log:    event severity updates
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/15 18:28:32  rjtierne
 *  Archive Log:    Added updateStatistics() method
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:17:28  rjtierne
 *  Archive Log:    Renamed from PerfSummaryInfoSection and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:02  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the Info Section of the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.util.Date;
import java.util.EnumMap;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.BaseSectionController;
import com.intel.stl.ui.common.ICardController;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.DevicesStatistics;
import com.intel.stl.ui.monitor.view.PSInfoSectionView;

public class PSInfoSection extends
        BaseSectionController<ISectionListener, PSInfoSectionView> {

    private final PSStatisticsCard statisticsCard;

    private final PSEventsCard eventsCard;

    public PSInfoSection(PSInfoSectionView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        statisticsCard =
                new PSStatisticsCard(view.getStatisticsCardView(), eventBus);
        eventsCard = new PSEventsCard(view.getEventsCardView(), eventBus);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getPerfSubnetSummary(), helpAction.getHelpSet());

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.ISection#getCards()
     */
    @Override
    public ICardController<?>[] getCards() {
        return new ICardController[] { statisticsCard, eventsCard };
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.ISection#onHelp()
     */
    @Override
    public void onHelp() {
        // TODO Auto-generated method stub
    }

    public void updateStatistics(DevicesStatistics dgStats) {
        statisticsCard.updateStatistics(dgStats);
    }

    public void updateStates(EnumMap<NoticeSeverity, Integer> switchStates,
            int switchTotal) {
        Date date = new Date();
        eventsCard.updateStates(switchStates, switchTotal, date);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.ISectionController#clear()
     */
    @Override
    public void clear() {
        statisticsCard.clear();
        eventsCard.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.BaseSectionController#getSectionListener()
     */
    @Override
    protected ISectionListener getSectionListener() {
        return this;
    }

}
