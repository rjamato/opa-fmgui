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
 *  File Name: SubnetSummaryProperties.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2015/02/23 22:45:59  jijunwan
 *  Archive Log:    improved to include/exclude inactive nodes/links in query
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2015/02/04 21:44:19  jijunwan
 *  Archive Log:    impoved to handle unsigned values
 *  Archive Log:     - we promote to a "bigger" data type
 *  Archive Log:     - port numbers are now short
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/10/22 02:21:26  jijunwan
 *  Archive Log:    1) moved update tasks into task package
 *  Archive Log:    2) added topology summary panel
 *  Archive Log:    3) improved models to be able to calculate ports distribution, nodes not in fat tree etc.
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.network;

import java.util.EnumMap;

import com.intel.stl.api.subnet.ISubnetApi;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.api.subnet.SubnetDataNotFoundException;
import com.intel.stl.ui.common.ICancelIndicator;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.common.UIConstants;
import com.intel.stl.ui.model.NodeTypeViz;
import com.intel.stl.ui.model.PropertyItem;
import com.intel.stl.ui.model.SimplePropertyCategory;
import com.intel.stl.ui.model.SimplePropertyGroup;
import com.intel.stl.ui.model.SimplePropertyKey;

public class SubnetSummaryProcessor {
    private final String name;

    private final ISubnetApi subnetApi;

    private final ICancelIndicator cancelIndicator;

    /**
     * Description:
     * 
     * @param subnetApi
     * @param cancelIndicator
     */
    public SubnetSummaryProcessor(String name, ISubnetApi subnetApi,
            ICancelIndicator cancelIndicator) {
        super();
        this.subnetApi = subnetApi;
        this.cancelIndicator = cancelIndicator;
        this.name = name;
    }

    public SimplePropertyGroup populate() {
        SimplePropertyGroup group = new SimplePropertyGroup(name);
        if (!cancelIndicator.isCancelled()) {
            group.addPropertyCategory(populateNodes());
        }
        if (!cancelIndicator.isCancelled()) {
            group.addPropertyCategory(populatePorts());
        }
        return group;
    }

    protected SimplePropertyCategory populateNodes() {
        SimplePropertyCategory category =
                new SimplePropertyCategory(
                        STLConstants.K0014_ACTIVE_NODES.getValue(), null);
        category.setShowHeader(true);
        try {
            EnumMap<NodeType, Integer> dist = subnetApi.getNodesTypeDist(false);

            NodeTypeViz type = NodeTypeViz.SWITCH;
            Integer count = dist.get(type.getType());
            PropertyItem<SimplePropertyKey> item =
                    populateCountItem(type, (long) count);
            category.addItem(item);

            type = NodeTypeViz.HFI;
            count = dist.get(type.getType());
            item = populateCountItem(type, (long) count);
            category.addItem(item);

        } catch (SubnetDataNotFoundException e) {
            e.printStackTrace();
        }
        return category;
    }

    protected SimplePropertyCategory populatePorts() {
        SimplePropertyCategory category =
                new SimplePropertyCategory(
                        STLConstants.K0024_ACTIVE_PORTS.getValue(), null);
        category.setShowHeader(true);
        try {
            EnumMap<NodeType, Long> dist = subnetApi.getPortsTypeDist(true);

            NodeTypeViz type = NodeTypeViz.SWITCH;
            Long count = dist.get(type.getType());
            PropertyItem<SimplePropertyKey> item =
                    populateCountItem(type, count);
            category.addItem(item);

            type = NodeTypeViz.HFI;
            count = dist.get(type.getType());
            item = populateCountItem(type, count);
            category.addItem(item);

            type = NodeTypeViz.OTHER;
            count = dist.get(type.getType());
            item = populateCountItem(type, count);
            category.addItem(item);
        } catch (SubnetDataNotFoundException e) {
            e.printStackTrace();
        }
        return category;
    }

    protected PropertyItem<SimplePropertyKey> populateCountItem(
            NodeTypeViz type, Long count) {
        SimplePropertyKey key = new SimplePropertyKey(type.getPluralName());
        String countString =
                count == null ? STLConstants.K0039_NOT_AVAILABLE.getValue()
                        : UIConstants.INTEGER.format(count);
        return new PropertyItem<SimplePropertyKey>(key, countString);
    }
}
