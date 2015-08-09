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
package com.intel.stl.fecdriver.messages.command;

import com.intel.stl.api.performance.ImageIdBean;
import com.intel.stl.api.subnet.NodeType;
import com.intel.stl.fecdriver.messages.adapter.sa.GID;

/**
 * @author jijunwan
 * 
 */
public class InputArgument {
    public enum InputType {
        InputTypeNoInput, /* No input. returns all records */
        InputTypeNodeType, /* NodeType */
        InputTypeSystemImageGuid, /* Guid - a system Image guid */
        InputTypeNodeGuid, /* Guid - a node guid */
        InputTypePortGuid, /* Guid - a port guid */
        InputTypePortGid, /* Gid - a gid associated with a port */
        InputTypeMcGid, /* Gid - a multicast gid */
        InputTypePortGuidPair, /* GuidPair - a pair of port guids */
        InputTypeGidPair, /* GidPair - a pair of gids */
        InputTypePathRecord, /* PathRecord */
        // #ifdef IB_STACK_OPENIB
        // InputTypePathRecordNetworkOrder,/* PathRecord in network byte order*/
        // #endif
        InputTypeLid, /* Lid - a lid in the local subnet */
        InputTypePKey, /* PKey - a pkey */
        InputTypeSL, /* SL - a service level */
        InputTypeIndex, /* Index - an index associated with a VF */
        InputTypeServiceId, /* ServiceId */
        InputTypeNodeDesc, /* NodeDesc - a node description/name */
        InputTypeServiceRecord, /*
                                 * ServiceRecordValue - complete SA
                                 * SERVICE_RECORD and component mask
                                 */
        InputTypeMcMemberRecord, /*
                                  * McMemberRecordValue - complete SA
                                  * MCMEMBER_RECORD and component mask
                                  */
        InputTypePortGuidList, /* GuidList - a list of port guids */
        InputTypeGidList, /* GidList - a list of gids */
        InputTypeMultiPathRecord, /* MultiPathRecord */

        InputTypeImageId, /*
                           * ImageId - may be used with groupInfo, groupConfig,
                           * portCounters (delta)
                           */
        InputTypeGroupName, /* GroupName - group name for groupInfo query */
        InputTypeVFName, /* VFName - vFabric name for vfInfo query */
        InputTypeVFSID,
        InputTypeLidPortNumber, /*
                                 * lid of node for portCounters query, port
                                 * number for portCounters query
                                 */
        InputTypeVFNamePort, 
        InputTypeVFSIDPort, 
        InputTypeFocus, /*
                        * Argument for getting sorted list of ports using
                        * utilization or error values (from group buckets)
                        */
        InputTypeVFNameFocus, 
        InputTypeVFSIDFocus 
    }

    /**
     * @return the type
     */
    public InputType getType() {
        return InputType.InputTypeNoInput;
    }

    public int getLid() {
        throw new UnsupportedOperationException();
    }

    public String getNodeDesc() {
        throw new UnsupportedOperationException();
    }

    public long getNodeGuid() {
        throw new UnsupportedOperationException();
    }

    public NodeType getNodeType() {
        throw new UnsupportedOperationException();
    }

    public long getPortGuid() {
        throw new UnsupportedOperationException();
    }

    public long getSystemImageGuid() {
        throw new UnsupportedOperationException();
    }

    public short getPKey() {
        throw new UnsupportedOperationException();
    }

    public byte getSL() {
        throw new UnsupportedOperationException();
    }

    public long getServiceId() {
        throw new UnsupportedOperationException();
    }

    public long getSourcePortGuid() {
        throw new UnsupportedOperationException();
    }

    public long getDestPortGuid() {
        throw new UnsupportedOperationException();
    }

    public GID<?> getSourceGid() {
        throw new UnsupportedOperationException();
    }

    public GID<?> getDestGid() {
        throw new UnsupportedOperationException();
    }

    public GID<?> getPortGid() {
        throw new UnsupportedOperationException();
    }

    public String getGroupName() {
        throw new UnsupportedOperationException();
    }

    public String getVfName() {
        throw new UnsupportedOperationException();
    }
    
    public long getVfSid() {
        throw new UnsupportedOperationException();
    }
    
    public ImageIdBean getImageId() {
        throw new UnsupportedOperationException();
    }

    public byte getPortNumber() {
        throw new UnsupportedOperationException();
    }

    public boolean isDelta() {
        throw new UnsupportedOperationException();
    }

    public int getSelect() {
        throw new UnsupportedOperationException();
    }

    public int getStart() {
        throw new UnsupportedOperationException();
    }

    public int getRange() {
        throw new UnsupportedOperationException();
    }
}
