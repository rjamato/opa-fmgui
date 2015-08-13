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
 *  File Name: NodeStatusController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5.2.1  2015/08/12 15:26:34  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
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
