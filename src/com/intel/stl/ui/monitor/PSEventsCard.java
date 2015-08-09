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
 *  File Name: PSEventsCard.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.12  2015/03/10 18:43:12  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2015/02/05 21:21:45  jijunwan
 *  Archive Log:    fixed NPE issues found by klocwork
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/09/15 15:24:32  jijunwan
 *  Archive Log:    changed AppEventBus to 3rd party lib mbassador
 *  Archive Log:    some code reformat
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/21 16:28:38  jijunwan
 *  Archive Log:    integer format adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/11 19:26:54  fernande
 *  Archive Log:    Adding EventBus and linking UI elements to the Performance tab
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/06/26 20:29:34  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/05/29 14:25:06  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/20 21:26:48  jijunwan
 *  Archive Log:    added events chart to performane subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:13:09  jijunwan
 *  Archive Log:    changed performance controllers a little bit to clean up code or reuse  code
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/18 22:44:49  rjtierne
 *  Archive Log:    Added updateStates() and updateTypes() to support
 *  Archive Log:    event severity pie chart updates
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/13 13:55:14  rjtierne
 *  Archive Log:    Provided return value for getCardListener()
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:16:02  rjtierne
 *  Archive Log:    Renamed from PerfSummaryEventsCard and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:02  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Events card for the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.awt.Color;
import java.util.Date;
import java.util.EnumMap;

import net.engio.mbassy.bus.MBassador;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeTableXYDataset;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.BaseCardController;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.NoticeSeverityViz;
import com.intel.stl.ui.monitor.view.PSEventsCardView;

public class PSEventsCard extends
        BaseCardController<ICardListener, PSEventsCardView> {
    private final TimeTableXYDataset trendDataset;

    private final DefaultPieDataset stateDataset;

    private final int maxDataPoints = 10;

    public PSEventsCard(PSEventsCardView view, MBassador<IAppEvent> eventBus) {
        super(view, eventBus);

        stateDataset = new DefaultPieDataset();
        NoticeSeverityViz[] states = NoticeSeverityViz.values();
        Color[] colors = new Color[states.length + 1];
        for (int i = 0; i < states.length; i++) {
            stateDataset.setValue(states[i].getName(), 0);
            colors[i] = states[i].getColor();
        }
        stateDataset.setValue(STLConstants.K0056_NONE.getValue(), 0);
        colors[states.length] = UIConstants.INTEL_GRAY;
        view.setStateDataset(stateDataset, colors);

        trendDataset = new TimeTableXYDataset();
        view.setTrendDataset(trendDataset, NoticeSeverityViz.colors);

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getEvents(), helpAction.getHelpSet());
    }

    /**
     * @param swStates
     * @param totalSWs
     */
    public void updateStates(final EnumMap<NoticeSeverity, Integer> states,
            final int total, final Date date) {

        final NoticeSeverityViz[] items = NoticeSeverityViz.values();
        final int[] counts = new int[items.length + 1];
        final double[] values = new double[counts.length];
        final String[] labels = new String[counts.length];
        final String[] tooltips = new String[counts.length];

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                boolean needDelete =
                        trendDataset.getItemCount() > maxDataPoints;
                int sum = 0;
                trendDataset.setNotify(false);
                for (int i = 0; i < counts.length; i++) {
                    if (i < counts.length - 1) {
                        Integer count = states.get(items[i].getSeverity());
                        counts[i] = count == null ? 0 : count;
                        sum += counts[i];
                        trendDataset.add(new Second(date), counts[i], items[i],
                                false);
                        if (needDelete) {
                            trendDataset.remove(trendDataset.getTimePeriod(0),
                                    items[i], true);
                        }
                        stateDataset.setValue(items[i].getName(), counts[i]);
                    } else {
                        counts[i] = total - sum;
                        stateDataset.setValue(
                                STLConstants.K0056_NONE.getValue(), counts[i]);
                    }
                    values[i] = (double) counts[i] / total;
                    labels[i] =
                            UIConstants.INTEGER.format(counts[i]) + " ("
                                    + UIConstants.PERCENTAGE.format(values[i])
                                    + ") ";
                    if (i < counts.length - 1) {
                        tooltips[i] =
                                UILabels.STL10203_NODE_EVENTS.getDescription(
                                        UIConstants.INTEGER.format(counts[i]),
                                        UIConstants.PERCENTAGE
                                                .format(values[i]),
                                        NoticeSeverityViz.names[i]);
                    } else {
                        tooltips[i] =
                                UILabels.STL10204_NODE_NO_EVENTS
                                        .getDescription(UIConstants.INTEGER
                                                .format(counts[i]),
                                                UIConstants.PERCENTAGE
                                                        .format(values[i]));
                    }
                }
                trendDataset.setNotify(true);
                view.setStates(values, labels, tooltips);
            }
        });

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
        super.clear();
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                stateDataset.clear();
                trendDataset.clear();
            }
        });
        view.clear();
    }

}
