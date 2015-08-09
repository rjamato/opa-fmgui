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
 *  File Name: NodeStatusController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2015/04/06 18:55:54  jijunwan
 *  Archive Log:    PR 127211 - Null pointer exception on processStateSummary when FM GUI lost connection
 *  Archive Log:      fixed with null check plus displaying N/A when no SWs.
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2015/04/02 13:32:54  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/20 21:59:57  jijunwan
 *  Archive Log:    Bug 126599 - "Null Pointer Exception" on toggling between Pie and Bar chart under Status card
 *  Archive Log:    added null check and clear view when it's null
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/21 16:28:39  jijunwan
 *  Archive Log:    integer format adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 21:18:58  jijunwan
 *  Archive Log:    improved status visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main;

import java.util.EnumMap;

import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.api.notice.NoticeSeverity;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.main.view.NodeStatusPanel;
import com.intel.stl.ui.model.ChartStyle;
import com.intel.stl.ui.model.StateLongTypeViz;

public class NodeStatusController {
    private final NodeStatusPanel view;

    private int lastTotal;

    private final DefaultPieDataset dataset;

    private EnumMap<NoticeSeverity, Integer> lastStates;

    /**
     * Description:
     * 
     * @param view
     */
    public NodeStatusController(NodeStatusPanel view) {
        super();
        this.view = view;
        dataset = new DefaultPieDataset();
        StateLongTypeViz[] states = StateLongTypeViz.values();
        for (int i = 0; i < states.length; i++) {
            dataset.setValue(states[i].getName(), 0);
        }
        view.setDataset(dataset, StateLongTypeViz.colors);
    }

    public void updateStates(EnumMap<NoticeSeverity, Integer> states,
            final int total) {
        lastStates = states;
        lastTotal = total;

        if (lastStates == null) {
            view.clear();
            return;
        }

        final int[] counts =
                StateLongTypeViz.getDistributionValues(states, total);
        int countsLen = 0;
        if (counts != null) {
            countsLen = counts.length;
        }
        final double[] values = new double[countsLen];
        final String[] labels = new String[countsLen];
        final String[] tooltips = new String[countsLen];
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < counts.length; i++) {
                    dataset.setValue(StateLongTypeViz.values()[i].getName(),
                            counts[i]);
                    if (total > 0) {
                        values[i] = (double) counts[i] / total;
                        labels[i] =
                                UIConstants.INTEGER.format(counts[i])
                                        + " ("
                                        + UIConstants.PERCENTAGE
                                                .format(values[i]) + ") ";
                        tooltips[i] =
                                UILabels.STL10202_NODE_STATES.getDescription(
                                        UIConstants.INTEGER.format(counts[i]),
                                        UIConstants.PERCENTAGE
                                                .format(values[i]),
                                        StateLongTypeViz.names[i]);
                    } else {
                        labels[i] = STLConstants.K0039_NOT_AVAILABLE.getValue();
                        tooltips[i] = null;
                    }
                }

                view.setStates(values, labels, tooltips);
            }
        });
    }

    public void setStyle(ChartStyle style) {
        view.setStyle(style);
        updateStates(lastStates, lastTotal);
    }

    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                dataset.clear();
                view.clear();
            }
        });
    }
}
