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
 *  File Name: PSEventsCardView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.9  2015/08/17 18:54:25  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2015/06/25 20:50:01  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on dynamic cards that can have different data sources
 *  Archive Log:    - change to use port counter performance item
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2015/02/05 19:09:20  jijunwan
 *  Archive Log:    fixed a issue reported by klocwork that is actually not a problem
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/26 20:29:30  jijunwan
 *  Archive Log:    clear UI when we switch context
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/20 21:26:47  jijunwan
 *  Archive Log:    added events chart to performane subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/19 22:12:29  jijunwan
 *  Archive Log:    look and feel adjustment on performance page
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/18 22:55:05  rjtierne
 *  Archive Log:    Added getter methods for piePanel and barPanel. Added setStates().
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/13 13:57:53  rjtierne
 *  Archive Log:    Added panels for pie and bar charts to main panel
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/09 19:23:14  rjtierne
 *  Archive Log:    Renamed from PerfSummaryEventsCardView and completely
 *  Archive Log:    changed after MVC Refactoring
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/08 21:11:03  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: View for the events card on the Performance Summary subpage
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.monitor.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.TableXYDataset;

import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.view.ComponentFactory;
import com.intel.stl.ui.common.view.ICardListener;
import com.intel.stl.ui.common.view.JCardView;
import com.intel.stl.ui.main.view.NodeStatesPie;

public class PSEventsCardView extends JCardView<ICardListener> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -6152333230896989483L;

    private JPanel mainPanel;

    private NodeStatesPie piePanel;

    private ChartPanel barPanel;

    public PSEventsCardView(String title) {
        super(title);
        // this is unnecessary, but can stop klocwork from complaining
        getMainComponent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.stl.ui.common.view.JCardView#getMainComponent()
     */
    @Override
    protected JComponent getMainComponent() {

        if (mainPanel != null) {
            return mainPanel;
        }

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 5));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;

        piePanel = new NodeStatesPie(false);
        piePanel.setOpaque(false);
        mainPanel.add(piePanel, gc);

        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;
        barPanel = new ChartPanel(null);
        barPanel.setPreferredSize(new Dimension(60, 20));
        barPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                barPanel.setMaximumDrawHeight(e.getComponent().getHeight());
                barPanel.setMaximumDrawWidth(e.getComponent().getWidth());
                barPanel.setMinimumDrawWidth(e.getComponent().getWidth());
                barPanel.setMinimumDrawHeight(e.getComponent().getHeight());
            }
        });
        barPanel.setOpaque(false);
        mainPanel.add(barPanel, gc);

        return mainPanel;

    }

    public void setStateDataset(PieDataset dataset, Color[] colors) {
        piePanel.setDataset(dataset, colors);
    }

    /**
     * Description:
     * 
     * @param dataset
     * @param colors
     */
    public void setTrendDataset(TableXYDataset dataset, Color[] colors) {
        JFreeChart chart =
                ComponentFactory.createStackedXYBarChart(dataset, "",
                        STLConstants.K0035_TIME.getValue(),
                        STLConstants.K0055_NUM_NODES.getValue(), false);
        XYItemRenderer xyitemrenderer = chart.getXYPlot().getRenderer();
        for (int i = 0; i < colors.length; i++) {
            xyitemrenderer.setSeriesPaint(i, colors[i]);
        }

        barPanel.setChart(chart);
    }

    public void setStates(double[] values, String[] labels, String[] tooltips) {
        piePanel.setStates(values, labels, tooltips);
    }

    public NodeStatesPie getPiePanel() {
        return piePanel;
    }

    public void clear() {
        piePanel.clear();
    }
}
