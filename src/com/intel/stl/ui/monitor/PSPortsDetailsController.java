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
 *  File Name: PSStatsDetailsController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.6.2.1  2015/08/12 15:26:58  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2015/02/04 21:44:17  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/10/23 16:33:18  jijunwan
 *  Archive Log:    minor change on timers - intend to improve timer behavior so the action will be cancelled event if it's already in EDT queue
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/10/22 16:40:15  jijunwan
 *  Archive Log:    separated other ports viz for the ports not in a subnet
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/10/15 22:00:22  jijunwan
 *  Archive Log:    display other ports on UI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/08/05 18:39:03  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/21 13:48:17  jijunwan
 *  Archive Log:    added # internal, external ports on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/08 14:24:07  jijunwan
 *  Archive Log:    minor change - rename caXXX to fiXXX
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/08 14:07:05  jijunwan
 *  Archive Log:    removed route from state chart per feedback we got
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/26 20:29:34  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/05 18:32:49  jijunwan
 *  Archive Log:    changed Channel Adapter to Fabric Interface
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/29 14:25:06  jijunwan
 *  Archive Log:    jfreechart dataset is not thread safe, put all dataset related operation into EDT, so they will synchronize
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/09 20:53:33  rjtierne
 *  Archive Log:    Removed failed and skipped datasets
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:19:34  rjtierne
 *  Archive Log:    Renamed from PerfSummaryStatsDetailsController and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:02  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the statistics detail view on the Performance
 *  Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.Timer;

import org.jfree.data.general.DefaultPieDataset;

import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.FlowTypeViz;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.monitor.view.PSPortsDetailsPanel;

/**
 * per feedback we got, we do not show Router info here. we intentionally
 * change it on UI side rather than backend because we may need to support it
 * again in the future
 */
public class PSPortsDetailsController {
    private final String name;

    private final DefaultPieDataset deviceTypeDataset;

    private final DefaultPieDataset flowTypeDataset;

    private final PSPortsDetailsPanel view;

    private final NodeTypeViz[] nodeTypes;

    private final FlowTypeViz[] flowTypes;

    /**
     * To avoid a "blink" on screen, we only clear our view when the update will
     * take a time period longer than
     * {@link com.intel.stl.ui.common.UIConstants.UPDATE_TIME}
     */
    private Timer viewClearTimer;

    public PSPortsDetailsController(String name, PSPortsDetailsPanel view) {
        this.view = view;

        this.name = name;
        view.setName(name);

        nodeTypes = view.getNodeTypes();
        deviceTypeDataset = new DefaultPieDataset();
        Color[] colors = new Color[nodeTypes.length];
        for (int i = 0; i < nodeTypes.length; i++) {
            NodeTypeViz type = nodeTypes[i];
            deviceTypeDataset.setValue(type, 0);
            colors[i] = type.getColor();
        }
        view.setDeviceTypeDataset(deviceTypeDataset, colors);

        flowTypes = view.getFlowTypes();
        flowTypeDataset = new DefaultPieDataset();
        colors = new Color[flowTypes.length];
        for (int i = 0; i < flowTypes.length; i++) {
            FlowTypeViz type = flowTypes[i];
            flowTypeDataset.setValue(type, 0);
            colors[i] = type.getColor();
        }
        view.setFlowTypeDataset(flowTypeDataset, colors);
    }

    /**
     * @return the view
     */
    public PSPortsDetailsPanel getView() {
        return view;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the typeDataset
     */
    public DefaultPieDataset getTypeDataset() {
        return deviceTypeDataset;
    }

    public void setDeviceTypes(long total, EnumMap<NodeType, Long> types) {
        if (viewClearTimer != null) {
            if (viewClearTimer.isRunning()) {
                viewClearTimer.stop();
            }
            viewClearTimer = null;
        }

        final long[] counts = NodeTypeViz.getDistributionValues(types);

        final String totalNumber = UIConstants.INTEGER.format(total);
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                view.setTotalNumber(totalNumber);

                for (NodeTypeViz type : nodeTypes) {
                    long count = counts[type.ordinal()];
                    deviceTypeDataset.setValue(type, counts[type.ordinal()]);
                    String number = UIConstants.INTEGER.format(count);
                    String label =
                            count == 1 ? type.getName() : type.getPluralName();
                    view.setTypeInfo(type, number, label);
                }
            }
        });
    }

    public void setFlowType(final long internalPorts, final long externalPorts) {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                flowTypeDataset.setValue(FlowTypeViz.INTERNAL, internalPorts);
                flowTypeDataset.setValue(FlowTypeViz.EXTERNAL, externalPorts);

                String text = UIConstants.INTEGER.format(internalPorts);
                view.setFlowInfo(FlowTypeViz.INTERNAL, text);
                text = UIConstants.INTEGER.format(externalPorts);
                view.setFlowInfo(FlowTypeViz.EXTERNAL, text);
            }
        });
    }

    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                deviceTypeDataset.clear();
                flowTypeDataset.clear();
            }
        });
        view.clear();
        if (viewClearTimer == null) {
            viewClearTimer =
                    new Timer(UIConstants.UPDATE_TIME, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (viewClearTimer != null) {
                                view.clear();
                            }
                        }
                    });
            viewClearTimer.setRepeats(false);
        }
        viewClearTimer.restart();
    }
}
