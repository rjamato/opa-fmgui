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
 *  File Name: NodeInfoProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/04/02 13:32:57  jypak
 *  Archive Log:    Klockwork: Front End Critical Without Unit Test. 47 open issues fixed. All of them are for null checks.
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2014/11/04 14:24:55  fernande
 *  Archive Log:    Changed Active property to be set base on actual state of the node.
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/22 01:47:47  jijunwan
 *  Archive Log:    renamed
 *  Archive Log:    PropertyPageCategory to DevicePropertyCategory,
 *  Archive Log:    PropertyItem to DevicePropertyItem,
 *  Archive Log:    PropertyPageGroup to DevicePropertyGroup
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:05:30  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.common.STLConstants.K0322_PORT_LINK_ACTIVE;
import static com.intel.stl.ui.common.STLConstants.K0524_INACTIVE;
import static com.intel.stl.ui.model.DeviceProperty.BASE_VERSION;
import static com.intel.stl.ui.model.DeviceProperty.DEVICE_ID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_GUID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_LID;
import static com.intel.stl.ui.model.DeviceProperty.NODE_NAME;
import static com.intel.stl.ui.model.DeviceProperty.NODE_STATE;
import static com.intel.stl.ui.model.DeviceProperty.NODE_TYPE;
import static com.intel.stl.ui.model.DeviceProperty.PARTITION_CAP;
import static com.intel.stl.ui.model.DeviceProperty.REVISION;
import static com.intel.stl.ui.model.DeviceProperty.SMA_VERSION;
import static com.intel.stl.ui.model.DeviceProperty.SYSTEM_IMAGE_GUID;
import static com.intel.stl.ui.model.DeviceProperty.VENDOR_ID;

import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.NodeTypeViz;

public class NodeInfoProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        NodeRecordBean nodeBean = context.getNode();
        NodeInfoBean nodeInfo = context.getNodeInfo();
        // TODO: need to come from API
        String nodeActive = K0322_PORT_LINK_ACTIVE.getValue();
        if (!nodeBean.isActive()) {
            nodeActive = K0524_INACTIVE.getValue();
        }
        addProperty(category, NODE_STATE, nodeActive);
        if (nodeInfo == null) {
            getEmptyNodeDeviceInfo(category);
            return;
        }
        addProperty(category, NODE_LID, hex(nodeBean.getLid()));
        addProperty(category, NODE_NAME, nodeBean.getNodeDesc());
        if (nodeInfo != null) {
            NodeTypeViz nodeTypeViz =
                    NodeTypeViz.getNodeTypeViz(nodeInfo.getNodeTypeEnum());
            if (nodeTypeViz != null) {
                addProperty(category, NODE_TYPE, nodeTypeViz.getName());
            }
            addProperty(category, NODE_GUID, hex(nodeInfo.getNodeGUID()));
            addProperty(category, SYSTEM_IMAGE_GUID,
                    hex(nodeInfo.getSysImageGUID()));
            addProperty(category, PARTITION_CAP,
                    dec(nodeInfo.getPartitionCap()));
            addProperty(category, BASE_VERSION, dec(nodeInfo.getBaseVersion()));
            addProperty(category, SMA_VERSION, dec(nodeInfo.getClassVersion()));
            addProperty(category, DEVICE_ID, hex(nodeInfo.getDeviceID()));
            addProperty(category, VENDOR_ID, hex(nodeInfo.getVendorID()));
            addProperty(category, REVISION, hex(nodeInfo.getRevision()));
        }

    }

    private void getEmptyNodeDeviceInfo(DevicePropertyCategory category) {
        addProperty(category, NODE_LID, "");
        addProperty(category, NODE_TYPE, "");
        addProperty(category, NODE_NAME, "");
        addProperty(category, NODE_GUID, "");
        addProperty(category, SYSTEM_IMAGE_GUID, "");
        addProperty(category, PARTITION_CAP, "");
        addProperty(category, BASE_VERSION, "");
        addProperty(category, SMA_VERSION, "");
        addProperty(category, DEVICE_ID, "");
        addProperty(category, VENDOR_ID, "");
        addProperty(category, REVISION, "");
    }

}
