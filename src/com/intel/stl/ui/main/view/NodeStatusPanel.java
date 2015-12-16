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
 *  File Name: NodeStatusPanel.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/08/17 18:54:02  jijunwan
 *  Archive Log:    PR 129983 - Need to change file header's copyright text to BSD license txt
 *  Archive Log:    - changed frontend files' headers
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/06/25 20:24:57  jijunwan
 *  Archive Log:    Bug 126755 - Pin Board functionality is not working in FV
 *  Archive Log:    - applied pin framework on fabric viewer and simple 'static' cards
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/07/09 21:18:59  jijunwan
 *  Archive Log:    improved status visualization
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.main.view;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.data.general.PieDataset;

import com.intel.stl.ui.model.ChartStyle;

public class NodeStatusPanel extends JPanel {
    private static final long serialVersionUID = -5896416689826992723L;

    private ChartStyle style = ChartStyle.PIE;

    private final CardLayout layout;

    private final NodeStatesBar barPanel;

    private final NodeStatesPie piePanel;

    public NodeStatusPanel() {
        this(false);
    }

    public NodeStatusPanel(boolean concise) {
        super();

        layout = new CardLayout();
        setLayout(layout);

        barPanel = new NodeStatesBar(concise);
        barPanel.setOpaque(false);
        add(barPanel, ChartStyle.BAR.name());

        piePanel = new NodeStatesPie(concise);
        piePanel.setOpaque(false);
        add(piePanel, ChartStyle.PIE.name());

        layout.show(this, ChartStyle.PIE.name());
    }

    public void setStyle(ChartStyle style) {
        this.style = style;
        layout.show(this, style.name());
    }

    public void setDataset(PieDataset dataset, Color[] colors) {
        piePanel.setDataset(dataset, colors);
    }

    public void setStates(double[] values, String[] labels, String[] tooltips) {
        if (style == ChartStyle.PIE) {
            piePanel.setStates(values, labels, tooltips);
        } else if (style == ChartStyle.BAR) {
            barPanel.setStates(values, labels, tooltips);
        }
    }

    public void clear() {
        barPanel.clear();
        piePanel.clear();
    }

}
