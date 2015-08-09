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
 *  File Name: ResourceNodePage.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.24  2014/10/23 16:00:04  jijunwan
 *  Archive Log:    changed topology information display to use device property panels, and JSectionView
 *  Archive Log:
 *  Archive Log:    Revision 1.23  2014/10/09 12:37:03  fernande
 *  Archive Log:    Adding IContextAware interface to generalize context operations (setContext) and changes to the IProgressObserver interface
 *  Archive Log:
 *  Archive Log:    Revision 1.22  2014/09/05 15:44:04  fernande
 *  Archive Log:    Changed DatabaseException into an unchecked exception. Then change error handling in the Datamanager so that certain FMExceptions (checked exceptions) bubble up to the API layer. Higher layers can then decide how to handle those exceptions. Any other Hibernate/HSQLDb/DAO exceptions are signaled as a DatabaseException (unchecked) to layers above the Datamanager.
 *  Archive Log:
 *  Archive Log:    Revision 1.21  2014/08/26 15:15:19  jijunwan
 *  Archive Log:    added refresh function to all pages
 *  Archive Log:
 *  Archive Log:    Revision 1.20  2014/08/20 18:27:57  rjtierne
 *  Archive Log:    Removed update partition info that is no longer available
 *  Archive Log:    in SwitchInfoBean
 *  Archive Log:
 *  Archive Log:    Revision 1.19  2014/08/20 18:01:58  fernande
 *  Archive Log:    Remove references to flags that do not exist anymore in SwitchInfoBean
 *  Archive Log:
 *  Archive Log:    Revision 1.18  2014/08/05 18:39:05  jijunwan
 *  Archive Log:    renamed FI to HFI
 *  Archive Log:
 *  Archive Log:    Revision 1.17  2014/08/05 17:59:45  jijunwan
 *  Archive Log:    ensure we update UI on EDT, changed to use SingleTaskManager to manager concurrent UI update tasks
 *  Archive Log:
 *  Archive Log:    Revision 1.16  2014/07/22 20:09:31  rjtierne
 *  Archive Log:    Implemented showPath() interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.15  2014/07/18 19:24:38  rjtierne
 *  Archive Log:    Removed Link page when node selected from topology page.
 *  Archive Log:    Added Link Statistics panel to the Node page.
 *  Archive Log:
 *  Archive Log:    Revision 1.14  2014/07/18 13:40:42  rjtierne
 *  Archive Log:    Changed prototype for showPath() to accept a list of
 *  Archive Log:    nodes to match the interface
 *  Archive Log:
 *  Archive Log:    Revision 1.13  2014/07/11 13:16:28  jypak
 *  Archive Log:    Added runtime, non runtime exceptions handler for SubnetApi, ConfigApi, PerformanceApi.
 *  Archive Log:    As of now, all different exceptions are generally handled as 'Exception' but when we define how to handle differently for different exception, based on the error code, handler (catch block will be different). Also, we are thinking of centralized 'failure recovery' process to handle all exceptions in one place.
 *  Archive Log:
 *  Archive Log:    Revision 1.12  2014/07/10 21:23:20  rjtierne
 *  Archive Log:    Added newly required interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.11  2014/07/10 15:42:53  rjtierne
 *  Archive Log:    Added new interface method setDescription()
 *  Archive Log:
 *  Archive Log:    Revision 1.10  2014/07/10 14:36:10  rjtierne
 *  Archive Log:    Added new interface methods
 *  Archive Log:
 *  Archive Log:    Revision 1.9  2014/07/08 20:24:52  rjtierne
 *  Archive Log:    Removed IProgressObserver from showNode(), added unimplemented
 *  Archive Log:    methods from interface
 *  Archive Log:
 *  Archive Log:    Revision 1.8  2014/07/03 22:23:47  jijunwan
 *  Archive Log:    1) improved Topology to support multiple edges selection
 *  Archive Log:    2) added Tree and Graph selection synchronization
 *  Archive Log:
 *  Archive Log:    Revision 1.7  2014/07/03 14:14:09  rjtierne
 *  Archive Log:    Added observer to showNode()
 *  Archive Log:
 *  Archive Log:    Revision 1.6  2014/06/30 21:05:01  rjtierne
 *  Archive Log:    Put API access code in swing worker. Added Partition Enforcement update method
 *  Archive Log:
 *  Archive Log:    Revision 1.5  2014/06/26 21:23:01  jijunwan
 *  Archive Log:    layout adjustment
 *  Archive Log:
 *  Archive Log:    Revision 1.4  2014/06/26 20:34:22  rjtierne
 *  Archive Log:    Used a blank field before Device Group names
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/06/26 20:26:25  rjtierne
 *  Archive Log:    Updated showNode() to put data on the NodeView panels
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/06/26 15:00:18  jijunwan
 *  Archive Log:    added progress indication to subnet initialization
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/06/24 20:30:31  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Controller for the Node subpage on the Topology page
 *
 *  @author: rjtierne
 *
 ******************************************************************************/
package com.intel.stl.ui.network;

import net.engio.mbassy.bus.MBassador;

import com.intel.stl.ui.common.IProgressObserver;
import com.intel.stl.ui.configuration.DevicePropertiesController;
import com.intel.stl.ui.configuration.view.DevicePropertiesPanel;
import com.intel.stl.ui.framework.IAppEvent;
import com.intel.stl.ui.model.DeviceProperties;
import com.intel.stl.ui.model.GraphNode;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class ResourceNodePage extends DevicePropertiesController implements
        IResourceNodeSubpageController {

    /**
     * Description:
     * 
     * @param model
     * @param view
     * @param eventBus
     */
    public ResourceNodePage(DeviceProperties model, DevicePropertiesPanel view,
            MBassador<IAppEvent> eventBus) {
        super(model, view, eventBus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.intel.stl.ui.network.IResourceNodeSubpageController#showNode(com.
     * intel.stl.ui.monitor.tree.FVResourceNode)
     */
    @Override
    public void showNode(FVResourceNode source, GraphNode node) {
        showNode(source, (IProgressObserver) null);
    }
}
