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
 *  File Name: PerformanceSubpageView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
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
