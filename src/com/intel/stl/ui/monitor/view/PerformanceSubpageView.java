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
 *  File Name: PerformanceSubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.7.2.1  2015/08/12 15:27:16  jijunwan
 *  Archive Log:    PR 129955 - Need to change file header's copyright text to BSD license text
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/10/09 21:24:50  jijunwan
 *  Archive Log:    improvement on TreeNodeType:
 *  Archive Log:    1) Added icon to TreeNodeType
 *  Archive Log:    2) Rename PORT to ACTIVE_PORT
 *  Archive Log:    3) Removed NODE
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/24 20:23:48  rjtierne
 *  Archive Log:    Changed HCA to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/05/28 22:21:57  jijunwan
 *  Archive Log:    added port preview to performance subpage
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/21 14:49:33  rjtierne
 *  Archive Log:    Refactored subpage to accommodate Performance Port
 *  Archive Log:    and Node views on a card layout
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/19 15:19:05  rjtierne
 *  Archive Log:    Removed txtLabel
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/01 17:53:33  jypak
 *  Archive Log:    Performance page performance subpage graph section implemented.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/05/01 16:14:58  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This is the view for the performance subpage.  It serves as a wrapper
 *  for swapping out the Performance "Node" and "Port" views on a CardLayout
 *  depending on whether a node or port is selected. 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import java.awt.CardLayout;
import java.util.Map;

import javax.swing.JPanel;

import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.monitor.TreeNodeType;

public class PerformanceSubpageView extends JPanel {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -2465696391555897980L;
    private CardLayout layout;


    public PerformanceSubpageView() {
        super();
        initComponents();
    }
    
    
    private void initComponents() {
        layout = new CardLayout();
        setLayout(layout);
    }
    
    
    public void initializeViews(Map<TreeNodeType, IPerfSubpageController> subpages) {
        add(subpages.get(TreeNodeType.ACTIVE_PORT).getView(), TreeNodeType.ACTIVE_PORT.name());
        add(subpages.get(TreeNodeType.NODE).getView(), TreeNodeType.NODE.name());
        layout.show(this, TreeNodeType.NODE.name());
    }
    
    
    public void showView(TreeNodeType type) {
        String name = new String(TreeNodeType.ACTIVE_PORT.name());
        if ((type == TreeNodeType.HFI) || (type == TreeNodeType.SWITCH)) {
            name = TreeNodeType.NODE.name();
        }
        
        layout.show(this, name);
    }
    
    
}
