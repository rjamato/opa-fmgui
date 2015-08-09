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
 *  File Name: PSStatsDetailsController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
 *  Archive Log:    Revision 1.3  2014/08/05 18:39:02  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/21 16:28:38  jijunwan
 *  Archive Log:    integer format adjustment
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
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.Util;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.monitor.view.PSNodesDetailsPanel;

/**
 * per feedback we got, we do not show Router info here. we intentionally
 * change it on UI side rather than backend because we may need to support it
 * again in the future
 */
public class PSNodesDetailsController {
    private final String name;

    private final DefaultPieDataset typeDataset;

    private final PSNodesDetailsPanel view;

    /**
     * To avoid a "blink" on screen, we only clear our view when the update will
     * take a time period longer than
     * {@link com.intel.stl.ui.common.UIConstants.UPDATE_TIME}
     */
    private Timer viewClearTimer;

    public PSNodesDetailsController(String name, PSNodesDetailsPanel view) {
        this.view = view;

        this.name = name;
        view.setName(name);

        typeDataset = new DefaultPieDataset();
        typeDataset.setValue(NodeType.SWITCH, 0);
        typeDataset.setValue(NodeType.HFI, 0);
        // typeDataset.setValue(NodeType.ROUTER, 0);
        Color[] colors =
                new Color[] { NodeTypeViz.SWITCH.getColor(),
                        NodeTypeViz.HFI.getColor(),
                /* NodeTypeViz.ROUTER.getColor() */};
        view.setTypeDataset(typeDataset, colors);
    }

    /**
     * @return the view
     */
    public PSNodesDetailsPanel getView() {
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
        return typeDataset;
    }

    public void setTypes(int total, long other, EnumMap<NodeType, Integer> types) {
        if (viewClearTimer != null) {
            if (viewClearTimer.isRunning()) {
                viewClearTimer.stop();
            }
            viewClearTimer = null;
        }

        final long[] counts = NodeTypeViz.getDistributionValues2(types);

        final String totalNumber = UIConstants.INTEGER.format(total);

        final String caNumber = UIConstants.INTEGER.format(counts[0]);
        final String caLabel =
                counts[0] == 1 ? STLConstants.K0051_HOST.getValue()
                        : STLConstants.K0052_HOSTS.getValue();

        final String swNumber = UIConstants.INTEGER.format(counts[1]);
        final String swLabel =
                counts[1] == 1 ? STLConstants.K0017_SWITCH.getValue()
                        : STLConstants.K0048_SWITCHES.getValue();

        // final String rtNumber = Integer.toString(counts[2]);
        // final String rtLabel = counts[2] == 1 ? STLConstants.K0019_ROUTER
        // .getValue() : STLConstants.K0050_ROUTERS.getValue();

        final String otherPorts = UIConstants.INTEGER.format(other);

        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                typeDataset.setValue(NodeType.HFI, counts[0]);
                typeDataset.setValue(NodeType.SWITCH, counts[1]);
                // typeDataset.setValue(NodeType.ROUTER, counts[2]);

                view.setTotalNumber(totalNumber);
                view.setSwitches(swNumber, swLabel);
                view.setFabricInterfaes(caNumber, caLabel);
                // view.setRouters(rtNumber, rtLabel);
                view.setOtherPorts(otherPorts);
            }
        });
    }

    public void clear() {
        Util.runInEDT(new Runnable() {
            @Override
            public void run() {
                typeDataset.clear();
            }
        });
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
