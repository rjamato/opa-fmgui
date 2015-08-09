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
 *  File Name: NeighborLinkDownReasonProcessor.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.1  2015/02/06 00:28:13  jijunwan
 *  Archive Log:    added neighbor link down reason to match FM 325
 *  Archive Log:
 *  
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.configuration;

import static com.intel.stl.ui.model.DeviceProperty.NEIGHBOR_LINK_DOWN_REASON;

import com.intel.stl.api.subnet.PortDownReasonBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.ui.common.STLConstants;
import com.intel.stl.ui.model.DevicePropertyCategory;
import com.intel.stl.ui.model.DevicePropertyItem;
import com.intel.stl.ui.model.LinkDownReasonViz;

public class NeighborLinkDownErrorLogProcessor extends BaseCategoryProcessor {

    @Override
    public void process(ICategoryProcessorContext context,
            DevicePropertyCategory category) {
        PortRecordBean portBean = context.getPort();

        if (portBean == null) {
            return;
        }

        PortDownReasonBean[] portDownReasons = portBean.getLinkDownReasons();
        for (PortDownReasonBean bean : portDownReasons) {
            if (bean.getTimestamp() != 0) {
                // TODO: The time is in local time. Does it need to be in GMT?
                DevicePropertyItem property =
                        new DevicePropertyItem(NEIGHBOR_LINK_DOWN_REASON, bean
                                .getTimestampDate().toString(),
                                LinkDownReasonViz.getLinkDownReasonStr(bean
                                        .getNeighborLinkDownReason()));
                category.addPropertyItem(property);
            }
        }
        if (category.getItems().isEmpty()) {
            String na = STLConstants.K0039_NOT_AVAILABLE.getValue();
            DevicePropertyItem property =
                    new DevicePropertyItem(NEIGHBOR_LINK_DOWN_REASON, na, na);
            category.addPropertyItem(property);
        }
    }
}
