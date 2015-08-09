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
 *  File Name: IResourceSubpageController.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/22 20:08:35  rjtierne
 *  Archive Log:    Changed prototype of showPath(). Added a second showPath() method
 *  Archive Log:    to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/18 13:39:02  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of nodes
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/07/10 21:20:52  rjtierne
 *  Archive Log:    Added showPath() method to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/07/10 15:41:26  rjtierne
 *  Archive Log:    Added setDescription() method to the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/07/10 14:31:01  rjtierne
 *  Archive Log:    Added new prototype showLink()
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/07/08 20:20:57  rjtierne
 *  Archive Log:    Removed IProgressObserver from interface prototypes
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/07/03 14:09:02  rjtierne
 *  Archive Log:    Added observer to showNode() prototype
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import com.intel.stl.ui.common.IPageController;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public interface IResourceNodeSubpageController extends IPageController {

    public void showNode(FVResourceNode source, GraphNode node);
}
