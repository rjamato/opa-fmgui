/**
 * INTEL CONFIDENTIAL
 * Copyright (c) ${year} Intel Corporation All Rights Reserved.
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
 *  File Name: TreeControllerInterface.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2014/10/14 11:32:09  jypak
 *  Archive Log:    UI updates for notices.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/08/26 15:15:27  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/26 15:00:15  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/22 20:47:24  rjtierne
 *  Archive Log:    Relocated from common.view to monitor package
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/17 14:38:56  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: This class serves as the public interface for the tree controller 
 *  
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.monitor;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.event.NodeUpdateEvent;
import com.intel.stl.ui.main.Context;

/**
 * @author rjtierne
 * 
 */
public interface TreeControllerInterface {

    public void setContext(Context pContext, IProgressObserver observer);

    public void onRefresh(IProgressObserver observer);

    public void onNodeUpdate(NodeUpdateEvent evt);
}
