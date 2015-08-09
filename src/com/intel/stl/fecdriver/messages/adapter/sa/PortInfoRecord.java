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
package com.intel.stl.fecdriver.messages.adapter.sa;

import com.intel.stl.api.subnet.PortDownReasonBean;
import com.intel.stl.api.subnet.PortRecordBean;
import com.intel.stl.api.subnet.SAConstants;
import com.intel.stl.fecdriver.messages.adapter.ComposedDatagram;
import com.intel.stl.fecdriver.messages.adapter.SimpleDatagram;

/**
 * ref: /ALL_EMB/IbAcess/Common/Inc/stl_sa.h v1.100
 * 
 * <pre>
 * typedef struct {
 * 	struct {
 * 		uint32	EndPortLID;				
 * 		uint8	PortNum;
 * 		uint8  Reserved;	
 * 	} PACK_SUFFIX RID;
 * 	
 * 	uint16		Reserved;	
 * 
 * 	STL_PORT_INFO PortInfo;
 * 	STL_LINKDOWN_REASON LinkDownReasons[STL_NUM_LINKDOWN_REASONS];
 * } PACK_SUFFIX STL_PORTINFO_RECORD;
 * 
 * #define STL_NUM_LINKDOWN_REASONS 8
 * typedef struct {
 *     uint8 Reserved[6];
 *     uint8 NeighborLinkDownReason;
 *     uint8 LinkDownReason;
 *     uint64 Timestamp;
 * } PACK_SUFFIX STL_LINKDOWN_REASON;
 * </pre>
 * 
 * @author jijunwan
 * 
 */
public class PortInfoRecord extends ComposedDatagram<PortRecordBean> {
    private PortInfoHeader header = null;

    private SimpleDatagram<Void> reserved = null;

    private PortInfo portInfo = null;

    private PortDownReason[] linkDownReasons = null;

    public PortInfoRecord() {
        header = new PortInfoHeader();
        addDatagram(header);
        reserved = new SimpleDatagram<Void>(2);
        addDatagram(reserved);
        portInfo = new PortInfo();
        addDatagram(portInfo);
        linkDownReasons =
                new PortDownReason[SAConstants.STL_NUM_LINKDOWN_REASONS];
        for (int i = 0; i < linkDownReasons.length; i++) {
            linkDownReasons[i] = new PortDownReason();
            addDatagram(linkDownReasons[i]);
        }
    }

    /**
     * @return the header
     */
    public PortInfoHeader getHeader() {
        return header;
    }

    /**
     * @return the portInfo
     */
    public PortInfo getPortInfo() {
        return portInfo;
    }

    public PortDownReasonBean[] getLinkDownReasons() {
        PortDownReasonBean[] res =
                new PortDownReasonBean[linkDownReasons.length];
        for (int i = 0; i < linkDownReasons.length; i++) {
            res[i] = linkDownReasons[i].toObject();
        }
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.intel.hpc.stl.resourceadapter.data.ComposedDatagram#toObject()
     */
    @Override
    public PortRecordBean toObject() {
        PortRecordBean bean =
                new PortRecordBean(header.getEndPortLid(), header.getPortNum(),
                        portInfo.toObject());
        bean.setLinkDownReasons(getLinkDownReasons());
        return bean;
    }

}
