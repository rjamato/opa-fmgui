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
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: PSInfoSectionView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/05 19:11:39  jijunwan
 *  Archive Log:    code cleanup
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/19 22:12:29  jijunwan
 *  Archive Log:    look and feel adjustment on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:26:31  rjtierne
 *  Archive Log:    Renamed from PerfSummaryInfoSectionView and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:03  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the info section on the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ISectionListener;
import com.intel.stl.ui.common.view.JSectionView;

public class PSInfoSectionView extends JSectionView<ISectionListener> {

    private static final long serialVersionUID = 7004235726509918990L;

    private JPanel mainPanel;

    private PSStatisticsCardView statisticsCardView;

    private PSEventsCardView eventsCardView;

    public PSInfoSectionView() {
        super(STLConstants.K0102_HOME_SUMMARY.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.ui.common.JSection#getMainPanel()
     */
    @Override
    protected JPanel getMainComponent() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout(5, 2));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            statisticsCardView =
                    new PSStatisticsCardView(
                            STLConstants.K0413_STATISTICS.getValue());
            mainPanel.add(statisticsCardView, BorderLayout.WEST);

            eventsCardView =
                    new PSEventsCardView(
                            STLConstants.K0206_EVENTS_SUMMARY.getValue());
            mainPanel.add(eventsCardView, BorderLayout.CENTER);
        }

        return mainPanel;
    }

    public PSStatisticsCardView getStatisticsCardView() {
        return statisticsCardView;
    }

    public PSEventsCardView getEventsCardView() {
        return eventsCardView;
    }

}
