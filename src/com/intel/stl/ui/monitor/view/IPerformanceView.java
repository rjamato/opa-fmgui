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
 *  File Name: IPerformanceView.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.5  2014/09/02 19:24:32  jijunwan
 *  Archive Log:    renamed FVTreeBuilder to tree.FVTreeManager, moved FVResourceNode and FVTreeModel  to package tree
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/05/09 21:00:42  jijunwan
 *  Archive Log:    added property; fixed remembering last subpage issue; fixed position problem on IntelTabbedPane
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/05/02 16:35:53  rjtierne
 *  Archive Log:    Removed setContext() from the interface since the
 *  Archive Log:    context is only used by the controller
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/05/01 16:10:05  rjtierne
 *  Archive Log:    Added getMainPanel(), getView(), setNodeName(), and
 *  Archive Log:    setTabs() to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:52:26  rjtierne
 *  Archive Log:    Moved from common.view to monitor.view
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:34  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Interface for the test view
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor.view;

import java.util.List;

import javax.swing.JComponent;

import com.intel.stl.ui.common.IPerfSubpageController;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public interface IPerformanceView {
    
    public JComponent getView();
    
    public void setNodeName(FVResourceNode node);
    
    public void setTabs(List<IPerfSubpageController> subpages, int selection);

}
