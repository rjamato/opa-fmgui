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
 *  File Name: TopSummaryGroupController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2.2.1  2015/08/12 15:26:50  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/03/10 18:43:14  jypak
 *  Archive Log:    JavaHelp System introduced to enable online help.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.awt.Color;
import java.util.List;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.common.UILabels;
import com.intel.stl.ui.main.HelpAction;
import com.intel.stl.ui.model.SimplePropertyCategory;
import com.intel.stl.ui.model.SimplePropertyGroup;
import com.intel.stl.ui.network.TopologyTier.Quality;
import com.intel.stl.ui.network.view.TopSummaryGroupPanel;

public class TopSummaryGroupController {
    private final TopSummaryGroupPanel view;

    /**
     * Description:
     * 
     * @param view
     */
    public TopSummaryGroupController(TopSummaryGroupPanel view) {
        super();
        this.view = view;

        HelpAction helpAction = HelpAction.getInstance();
        helpAction.getHelpBroker().enableHelpOnButton(view.getHelpButton(),
                helpAction.getTopologySummary(), helpAction.getHelpSet());
    }

    public void setModel(SimplePropertyGroup model) {
        view.init(model.size());
        List<SimplePropertyCategory> categories = model.getList();
        int maxPortsSegment = getMaxPortsSegment(categories);
        for (int i = 0; i < categories.size(); i++) {
            TopologyTier tier =
                    (TopologyTier) categories.get(i).getItems().iterator()
                            .next().getObject();
            updateTierView(i, tier, maxPortsSegment);
        }
    }

    protected void updateTierView(int index, TopologyTier tier,
            double maxPortsSegment) {
        view.setTierName(index, tier.getName());

        String numSwitches = UIConstants.INTEGER.format(tier.getNumSwitches());
        String numHFIs = UIConstants.INTEGER.format(tier.getNumHFIs());
        String numPorts = UIConstants.INTEGER.format(tier.getTotalPorts());
        view.setSummary(index, numSwitches, numHFIs, numPorts);

        Quality up = tier.getUpQuality();
        Quality down = tier.getDownQuality();
        // ports
        double[] normalizedVals = new double[3];
        normalizedVals[0] = up.getTotalPorts() / maxPortsSegment;
        normalizedVals[1] = down.getTotalPorts() / maxPortsSegment;
        normalizedVals[2] = tier.getNumOtherPorts() / maxPortsSegment;
        String[] values = new String[3];
        values[0] = UIConstants.INTEGER.format(up.getTotalPorts());
        values[1] = UIConstants.INTEGER.format(down.getTotalPorts());
        values[2] = UIConstants.INTEGER.format(tier.getNumOtherPorts());
        String[] labels = new String[3];
        labels[0] = STLConstants.K2069_UP_PORTS.getValue();
        labels[1] = STLConstants.K2070_DOWN_PORTS.getValue();
        labels[2] = STLConstants.K2071_OTHER_PORTS.getValue();
        Color[] colors =
                new Color[] { UIConstants.INTEL_DARK_BLUE,
                        UIConstants.INTEL_DARK_GREEN,
                        UIConstants.INTEL_DARK_ORANGE };
        view.setPortsDist(index, normalizedVals, values, colors, labels, null);

        // slow ports
        normalizedVals = new double[2];
        normalizedVals[0] = up.getSlowPorts() / maxPortsSegment;
        normalizedVals[1] = down.getSlowPorts() / maxPortsSegment;
        values = new String[2];
        values[0] = UIConstants.INTEGER.format(up.getSlowPorts());
        values[1] = UIConstants.INTEGER.format(down.getSlowPorts());
        labels = new String[2];
        labels[0] = STLConstants.K2072_SLOW_UP_PORTS.getValue();
        labels[1] = STLConstants.K2073_SLOW_DOWN_PORTS.getValue();
        colors =
                new Color[] { UIConstants.INTEL_DARK_BLUE,
                        UIConstants.INTEL_DARK_GREEN };
        String[] tooltips = new String[2];
        tooltips[0] =
                tooltips[1] = UILabels.STL70002_SLOW_PORTS.getDescription();
        view.setSlowPortsDist(index, normalizedVals, values, colors, labels,
                tooltips);

        // degraded ports
        normalizedVals[0] = up.getDegPorts() / maxPortsSegment;
        normalizedVals[1] = down.getDegPorts() / maxPortsSegment;
        values[0] = UIConstants.INTEGER.format(up.getDegPorts());
        values[1] = UIConstants.INTEGER.format(down.getDegPorts());
        labels[0] = STLConstants.K2074_DEG_UP_PORTS.getValue();
        labels[1] = STLConstants.K2075_DEG_DOWN_PORTS.getValue();
        tooltips[0] =
                tooltips[1] = UILabels.STL70003_DEG_PORTS.getDescription();
        view.setDegPortsDist(index, normalizedVals, values, colors, labels,
                tooltips);
    }

    protected int getMaxPortsSegment(List<SimplePropertyCategory> categories) {
        int max = 0;
        for (int i = 0; i < categories.size(); i++) {
            TopologyTier tier =
                    (TopologyTier) categories.get(i).getItems().iterator()
                            .next().getObject();
            Quality up = tier.getUpQuality();
            Quality down = tier.getDownQuality();
            int tmp =
                    maxNumber(up.getTotalPorts(), down.getTotalPorts(),
                            tier.getNumOtherPorts());
            if (tmp > max) {
                max = tmp;
            }
        }
        return max;
    }

    private int maxNumber(int... vals) {
        int max = 0;
        for (int val : vals) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

}
