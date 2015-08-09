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
 *  File Name: ResourcePortView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/07/03 14:17:39  rjtierne
 *  Archive Log:    Added the connectivity table view to the topology port page panel
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/25 13:45:54  rjtierne
 *  Archive Log:    Initial Version - renamed from ResourceRouteView
 *  Archive Log:
 *
 *  Overview: Resource Port subpage view on the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.intel.stl.ui.monitor.view.ConnectivitySubpageView;

public class ResourcePortView extends JPanel {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 4539202706133024906L;
    
    public ResourcePortView() {
        
    }
    
    
    public void addTableView(ConnectivitySubpageView tableView) {
        
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridwidth = GridBagConstraints.REMAINDER;        
        
        add(tableView, gc);
    }

}
