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
 *  File Name: ResourceCategoryContext.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.4  2015/02/04 21:44:14  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.3  2015/01/11 21:30:25  jijunwan
 *  Archive Log:    adapt change on FM that uses port number 1 for HFI in link query
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/10/13 22:16:29  fernande
 *  Archive Log:    Fixing unit test error due to changes in TreeNodeType
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/13 21:04:11  fernande
 *  Archive Log:    Changed GetDevicePropertiesTask to be driven by the PropertiesDisplayOptions in UserSettings instead of hard coded
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: fernande
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import com.intel.stl.api.configuration.IConfigurationApi;
import com.intel.stl.api.performance.IPerformanceApi;
import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.LinkRecordBean;
import com.intel.stl.api.subnet.NodeInfoBean;
import com.intel.stl.api.subnet.NodeRecordBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.PortInfoBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.api.subnet.SwitchInfoBean;
import com.intel.stl.api.subnet.SwitchRecordBean;
import com.intel.stl.ui.main.Context;
import com.intel.stl.ui.monitor.TreeNodeType;
import com.intel.stl.ui.monitor.tree.FVResourceNode;

public class CategoryProcessorContext implements ICategoryProcessorContext {

    private final FVResourceNode resourceNode;

    private final Context context;

    private NodeRecordBean node;

    private NodeInfoBean nodeInfo;

    private SwitchRecordBean switchBean;

    private SwitchInfoBean switchInfo;

    private PortRecordBean portBean;

    private PortInfoBean portInfo;

    private LinkRecordBean linkBean;

    private NodeRecordBean neighbor;

    private boolean endPort;

    public CategoryProcessorContext(FVResourceNode node, Context context) {
        this.resourceNode = node;
        this.context = context;
        try {
            switch (node.getType()) {
                case SWITCH:
                    setupForSwitch();
                    break;
                case HFI:
                    setupForNode();
                    break;
                case PORT:
                    setupForPort();
                    break;
                case ACTIVE_PORT:
                    setupForPort();
                    break;
                case INACTIVE_PORT:
                    break;
                default:
                    break;
            }
        } catch (SubnetDataNotFoundException e) {
            RuntimeException re = new RuntimeException(e.getMessage());
            re.initCause(e);
            throw re;
        }
    }

    @Override
    public FVResourceNode getResourceNode() {
        return resourceNode;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public ISubnetApi getSubnetApi() {
        return context.getSubnetApi();
    }

    @Override
    public IConfigurationApi getConfigurationApi() {
        return context.getConfigurationApi();
    }

    @Override
    public IPerformanceApi getPerformanceApi() {
        return context.getPerformanceApi();
    }

    @Override
    public NodeRecordBean getNode() {
        return node;
    }

    @Override
    public NodeInfoBean getNodeInfo() {
        return nodeInfo;
    }

    @Override
    public SwitchRecordBean getSwitch() {
        return switchBean;
    }

    @Override
    public SwitchInfoBean getSwitchInfo() {
        return switchInfo;
    }

    @Override
    public PortRecordBean getPort() {
        return portBean;
    }

    @Override
    public PortInfoBean getPortInfo() {
        return portInfo;
    }

    @Override
    public LinkRecordBean getLink() {
        return linkBean;
    }

    @Override
    public NodeRecordBean getNeighbor() {
        return neighbor;
    }

    @Override
    public boolean isEndPort() {
        return endPort;
    }

    private void setupForNode() throws SubnetDataNotFoundException {
        int lid = resourceNode.getId();
        node = getNode(lid);
        nodeInfo = null;
        if (node != null) {
            nodeInfo = node.getNodeInfo();
        }
    }

    private void setupForSwitch() throws SubnetDataNotFoundException {
        int lid = resourceNode.getId();
        node = getNode(lid);
        switchBean = getSubnetApi().getSwitch(lid);
        nodeInfo = null;
        switchInfo = null;
        if (node != null) {
            nodeInfo = node.getNodeInfo();
        }
        if (switchBean != null) {
            switchInfo = switchBean.getSwitchInfo();
        }
    }

    private void setupForPort() throws SubnetDataNotFoundException {
        FVResourceNode parent = resourceNode.getParent();
        TreeNodeType type = parent.getType();
        int lid = parent.getId();
        int portNum = resourceNode.getId();
        node = getNode(lid);
        portBean =
                type == TreeNodeType.SWITCH ? getSubnetApi().getPortByPortNum(
                        lid, (short) portNum) : getSubnetApi()
                        .getPortByLocalPortNum(lid, (short) portNum);
        nodeInfo = null;
        portInfo = null;
        linkBean = null;
        neighbor = null;
        endPort = false;
        if (node != null) {
            nodeInfo = node.getNodeInfo();
            NodeType parentType = nodeInfo.getNodeTypeEnum();
            // According to the IB spec
            // Endport: A Port which can be a destination of LID-routed
            // communication within the same Subnet as the sender. All Channel
            // Adapter ports on the subnet are endports of that subnet, as is
            // Port 0 of each Switch in the subnet. Switch ports other than Port
            // 0 may not be endports. When port is used without qualification,
            // it may be assumed to mean endport whenever the context indicates
            // that it is a destination of communication.
            if ((parentType != NodeType.SWITCH)
                    || (parentType == NodeType.SWITCH && portNum == 0)) {
                endPort = true;
            }
        }
        if (portBean != null) {
            portInfo = portBean.getPortInfo();
        }
        if (portBean != null) {
            linkBean = null;
            if (type != TreeNodeType.SWITCH) {
                linkBean = getSubnetApi().getLinkBySource(lid, (short) 1);
            } else if (portNum != 0) {
                linkBean = getSubnetApi().getLinkBySource(lid, (short) portNum);
            }
            if (linkBean != null) {
                neighbor = getNode(linkBean.getToLID());
            }
        }
    }

    private NodeRecordBean getNode(int lid) throws SubnetDataNotFoundException {
        NodeRecordBean node = getSubnetApi().getNode(lid);
        return node;
    }
}
